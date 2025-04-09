<?php
// backend/api/classes/Tag.php
class Tag {
    // Database connection and table name
    private $conn;
    private $table_name = "Tags";
    private $contacts_tags_table = "ContactTags";

    // Object properties
    public $tag_id;
    public $user_id;
    public $tag_name;
    public $color;
    public $created_at;
    public $updated_at;

    // Additional properties
    public $contact_count;

    // Constructor with DB connection
    public function __construct($db) {
        $this->conn = $db;
    }

    // Create new tag
    public function create() {
        // Query to insert record
        $query = "INSERT INTO " . $this->table_name . "
                  SET user_id = :user_id,
                      tag_name = :tag_name,
                      color = :color";

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $this->user_id = htmlspecialchars(strip_tags($this->user_id));
        $this->tag_name = htmlspecialchars(strip_tags($this->tag_name));
        $this->color = htmlspecialchars(strip_tags($this->color));

        // Bind values
        $stmt->bindParam(":user_id", $this->user_id);
        $stmt->bindParam(":tag_name", $this->tag_name);
        $stmt->bindParam(":color", $this->color);

        // Execute query
        if($stmt->execute()) {
            $this->tag_id = $this->conn->lastInsertId();
            return true;
        }

        return false;
    }

    // Check if tag name already exists
    public function tagNameExists() {
        $query = "SELECT COUNT(*) FROM " . $this->table_name . "
                  WHERE user_id = :user_id AND tag_name = :tag_name";

        if($this->tag_id) {
            $query .= " AND tag_id != :tag_id";
        }

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":user_id", $this->user_id);
        $stmt->bindParam(":tag_name", $this->tag_name);

        if($this->tag_id) {
            $stmt->bindParam(":tag_id", $this->tag_id);
        }

        $stmt->execute();

        return $stmt->fetchColumn() > 0;
    }

    // Read all tags for a user
    public function readAll($user_id) {
        $query = "SELECT t.*,
                 (SELECT COUNT(*) FROM " . $this->contacts_tags_table . " ct WHERE ct.tag_id = t.tag_id) as contact_count
                 FROM " . $this->table_name . " t
                 WHERE t.user_id = :user_id
                 ORDER BY t.tag_name";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":user_id", $user_id);
        $stmt->execute();

        return $stmt;
    }

    // Read one tag by ID
    public function readOne($tag_id, $user_id) {
        $query = "SELECT t.*,
                 (SELECT COUNT(*) FROM " . $this->contacts_tags_table . " ct WHERE ct.tag_id = t.tag_id) as contact_count
                 FROM " . $this->table_name . " t
                 WHERE t.tag_id = :tag_id AND t.user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":tag_id", $tag_id);
        $stmt->bindParam(":user_id", $user_id);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if(!$row) {
            return false;
        }

        // Set properties
        $this->tag_id = $row['tag_id'];
        $this->user_id = $row['user_id'];
        $this->tag_name = $row['tag_name'];
        $this->color = $row['color'];
        $this->created_at = $row['created_at'];
        $this->updated_at = $row['updated_at'];
        $this->contact_count = $row['contact_count'];

        return true;
    }

    // Update tag
    public function update() {
        // Check if tag name already exists
        if($this->tagNameExists()) {
            return false;
        }

        $query = "UPDATE " . $this->table_name . "
                  SET tag_name = :tag_name,
                      color = :color
                  WHERE tag_id = :tag_id AND user_id = :user_id";

        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $this->tag_name = htmlspecialchars(strip_tags($this->tag_name));
        $this->color = htmlspecialchars(strip_tags($this->color));

        // Bind values
        $stmt->bindParam(":tag_name", $this->tag_name);
        $stmt->bindParam(":color", $this->color);
        $stmt->bindParam(":tag_id", $this->tag_id);
        $stmt->bindParam(":user_id", $this->user_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Delete tag
    public function delete() {
        $query = "DELETE FROM " . $this->table_name . "
                  WHERE tag_id = :tag_id AND user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":tag_id", $this->tag_id);
        $stmt->bindParam(":user_id", $this->user_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Get contacts with this tag
    public function getContacts($page = 1, $limit = 10) {
        // Calculate offset for pagination
        $offset = ($page - 1) * $limit;

        $query = "SELECT c.*,
                 (SELECT GROUP_CONCAT(CONCAT(info_type, ':', info_value) SEPARATOR '|')
                  FROM ContactInformation
                  WHERE contact_id = c.contact_id AND is_primary = 1) as contact_info
                 FROM Contacts c
                 JOIN " . $this->contacts_tags_table . " ct ON c.contact_id = ct.contact_id
                 WHERE ct.tag_id = :tag_id
                 ORDER BY c.first_name, c.last_name
                 LIMIT :limit OFFSET :offset";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":tag_id", $this->tag_id);
        $stmt->bindParam(":limit", $limit, PDO::PARAM_INT);
        $stmt->bindParam(":offset", $offset, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt;
    }

    // Count contacts with this tag for pagination
    public function countContacts() {
        $query = "SELECT COUNT(*) as total
                 FROM " . $this->contacts_tags_table . "
                 WHERE tag_id = :tag_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":tag_id", $this->tag_id);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        return $row['total'];
    }

    // Add tag to contacts (bulk)
    public function addToContacts($contact_ids) {
        // Begin transaction
        $this->conn->beginTransaction();

        try {
            foreach($contact_ids as $contact_id) {
                // Check if already tagged
                $check_query = "SELECT COUNT(*) FROM " . $this->contacts_tags_table . "
                                WHERE contact_id = :contact_id AND tag_id = :tag_id";

                $check_stmt = $this->conn->prepare($check_query);
                $check_stmt->bindParam(":contact_id", $contact_id);
                $check_stmt->bindParam(":tag_id", $this->tag_id);
                $check_stmt->execute();

                if($check_stmt->fetchColumn() == 0) {
                    // Not tagged, add it
                    $query = "INSERT INTO " . $this->contacts_tags_table . " (contact_id, tag_id)
                              VALUES (:contact_id, :tag_id)";

                    $stmt = $this->conn->prepare($query);
                    $stmt->bindParam(":contact_id", $contact_id);
                    $stmt->bindParam(":tag_id", $this->tag_id);
                    $stmt->execute();
                }
            }

            // Commit transaction
            $this->conn->commit();
            return true;
        } catch(Exception $e) {
            // Rollback in case of error
            $this->conn->rollBack();
            return false;
        }
    }

    // Remove tag from contacts (bulk)
    public function removeFromContacts($contact_ids) {
        // Begin transaction
        $this->conn->beginTransaction();

        try {
            foreach($contact_ids as $contact_id) {
                $query = "DELETE FROM " . $this->contacts_tags_table . "
                          WHERE contact_id = :contact_id AND tag_id = :tag_id";

                $stmt = $this->conn->prepare($query);
                $stmt->bindParam(":contact_id", $contact_id);
                $stmt->bindParam(":tag_id", $this->tag_id);
                $stmt->execute();
            }

            // Commit transaction
            $this->conn->commit();
            return true;
        } catch(Exception $e) {
            // Rollback in case of error
            $this->conn->rollBack();
            return false;
        }
    }
}
?>