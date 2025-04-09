<?php
// backend/api/classes/Contact.php
class Contact {
    // Database connection and table name
    private $conn;
    private $table_name = "Contacts";
    private $info_table = "ContactInformation";
    private $address_table = "Addresses";

    // Object properties
    public $contact_id;
    public $user_id;
    public $first_name;
    public $last_name;
    public $company;
    public $job_title;
    public $profile_picture;
    public $notes;
    public $favorite;
    public $created_at;
    public $updated_at;
    public $last_contacted_at;

    // Additional properties for related data
    public $contact_info = [];
    public $addresses = [];
    public $groups = [];
    public $tags = [];

    // Constructor with DB connection
    public function __construct($db) {
        $this->conn = $db;
    }

    // Create contact
    public function create() {
        // Query to insert record
        $query = "INSERT INTO " . $this->table_name . "
                  SET user_id = :user_id,
                      first_name = :first_name,
                      last_name = :last_name,
                      company = :company,
                      job_title = :job_title,
                      profile_picture = :profile_picture,
                      notes = :notes,
                      favorite = :favorite";

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $this->user_id = htmlspecialchars(strip_tags($this->user_id));
        $this->first_name = htmlspecialchars(strip_tags($this->first_name));
        $this->last_name = htmlspecialchars(strip_tags($this->last_name));
        $this->company = htmlspecialchars(strip_tags($this->company));
        $this->job_title = htmlspecialchars(strip_tags($this->job_title));
        $this->profile_picture = htmlspecialchars(strip_tags($this->profile_picture));
        $this->notes = htmlspecialchars(strip_tags($this->notes));
        $this->favorite = $this->favorite ? 1 : 0;

        // Bind values
        $stmt->bindParam(":user_id", $this->user_id);
        $stmt->bindParam(":first_name", $this->first_name);
        $stmt->bindParam(":last_name", $this->last_name);
        $stmt->bindParam(":company", $this->company);
        $stmt->bindParam(":job_title", $this->job_title);
        $stmt->bindParam(":profile_picture", $this->profile_picture);
        $stmt->bindParam(":notes", $this->notes);
        $stmt->bindParam(":favorite", $this->favorite);

        // Execute query
        if($stmt->execute()) {
            $this->contact_id = $this->conn->lastInsertId();
            return true;
        }

        return false;
    }

    // Read all contacts for a user
    public function readAll($user_id, $search = "", $filter = [], $sort = [], $page = 1, $limit = 10) {
        // Calculate offset for pagination
        $offset = ($page - 1) * $limit;

        // Base query
        $query = "SELECT c.*,
                    (SELECT GROUP_CONCAT(CONCAT(info_type, ':', info_value) SEPARATOR '|')
                     FROM " . $this->info_table . "
                     WHERE contact_id = c.contact_id AND is_primary = 1) as contact_info
                  FROM " . $this->table_name . " c
                  WHERE c.user_id = :user_id";

        // Add search condition if provided
        if(!empty($search)) {
            $query .= " AND (
                c.first_name LIKE :search OR
                c.last_name LIKE :search OR
                c.company LIKE :search OR
                c.job_title LIKE :search OR
                EXISTS (
                    SELECT 1 FROM " . $this->info_table . "
                    WHERE contact_id = c.contact_id AND info_value LIKE :search
                )
            )";
        }

        // Add filtering conditions
        if(!empty($filter)) {
            if(isset($filter['favorite']) && $filter['favorite']) {
                $query .= " AND c.favorite = 1";
            }

            if(isset($filter['group_id']) && $filter['group_id']) {
                $query .= " AND EXISTS (
                    SELECT 1 FROM ContactGroupMembership
                    WHERE contact_id = c.contact_id AND group_id = :group_id
                )";
            }

            if(isset($filter['tag_id']) && $filter['tag_id']) {
                $query .= " AND EXISTS (
                    SELECT 1 FROM ContactTags
                    WHERE contact_id = c.contact_id AND tag_id = :tag_id
                )";
            }
        }

        // Add sorting
        if(!empty($sort)) {
            $sort_field = isset($sort['field']) ? $sort['field'] : 'first_name';
            $sort_direction = isset($sort['direction']) && strtolower($sort['direction']) === 'desc' ? 'DESC' : 'ASC';

            // Safe sort fields
            $allowed_sort_fields = ['first_name', 'last_name', 'company', 'created_at', 'updated_at', 'last_contacted_at'];

            if(in_array($sort_field, $allowed_sort_fields)) {
                $query .= " ORDER BY c." . $sort_field . " " . $sort_direction;
            } else {
                $query .= " ORDER BY c.first_name ASC";
            }
        } else {
            $query .= " ORDER BY c.first_name ASC";
        }

        // Add pagination
        $query .= " LIMIT :limit OFFSET :offset";

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Bind user ID
        $stmt->bindParam(":user_id", $user_id);

        // Bind search if provided
        if(!empty($search)) {
            $search_term = "%" . $search . "%";
            $stmt->bindParam(":search", $search_term);
        }

        // Bind filter parameters if provided
        if(!empty($filter)) {
            if(isset($filter['group_id']) && $filter['group_id']) {
                $stmt->bindParam(":group_id", $filter['group_id']);
            }

            if(isset($filter['tag_id']) && $filter['tag_id']) {
                $stmt->bindParam(":tag_id", $filter['tag_id']);
            }
        }

        // Bind pagination parameters
        $stmt->bindParam(":limit", $limit, PDO::PARAM_INT);
        $stmt->bindParam(":offset", $offset, PDO::PARAM_INT);

        // Execute query
        $stmt->execute();

        return $stmt;
    }

    // Count total contacts for pagination
    public function countAll($user_id, $search = "", $filter = []) {
        // Base query
        $query = "SELECT COUNT(*) as total FROM " . $this->table_name . " c WHERE c.user_id = :user_id";

        // Add search condition if provided
        if(!empty($search)) {
            $query .= " AND (
                c.first_name LIKE :search OR
                c.last_name LIKE :search OR
                c.company LIKE :search OR
                c.job_title LIKE :search OR
                EXISTS (
                    SELECT 1 FROM " . $this->info_table . "
                    WHERE contact_id = c.contact_id AND info_value LIKE :search
                )
            )";
        }

        // Add filtering conditions
        if(!empty($filter)) {
            if(isset($filter['favorite']) && $filter['favorite']) {
                $query .= " AND c.favorite = 1";
            }

            if(isset($filter['group_id']) && $filter['group_id']) {
                $query .= " AND EXISTS (
                    SELECT 1 FROM ContactGroupMembership
                    WHERE contact_id = c.contact_id AND group_id = :group_id
                )";
            }

            if(isset($filter['tag_id']) && $filter['tag_id']) {
                $query .= " AND EXISTS (
                    SELECT 1 FROM ContactTags
                    WHERE contact_id = c.contact_id AND tag_id = :tag_id
                )";
            }
        }

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Bind user ID
        $stmt->bindParam(":user_id", $user_id);

        // Bind search if provided
        if(!empty($search)) {
            $search_term = "%" . $search . "%";
            $stmt->bindParam(":search", $search_term);
        }

        // Bind filter parameters if provided
        if(!empty($filter)) {
            if(isset($filter['group_id']) && $filter['group_id']) {
                $stmt->bindParam(":group_id", $filter['group_id']);
            }

            if(isset($filter['tag_id']) && $filter['tag_id']) {
                $stmt->bindParam(":tag_id", $filter['tag_id']);
            }
        }

        // Execute query
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        return $row['total'];
    }

    // Read contact detail by ID
    public function readOne($contact_id, $user_id) {
        // Query to get contact basic info
        $query = "SELECT * FROM " . $this->table_name . "
                  WHERE contact_id = :contact_id AND user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $contact_id);
        $stmt->bindParam(":user_id", $user_id);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if(!$row) {
            return false;
        }

        // Set contact properties
        $this->contact_id = $row["contact_id"];
        $this->user_id = $row["user_id"];
        $this->first_name = $row["first_name"];
        $this->last_name = $row["last_name"];
        $this->company = $row["company"];
        $this->job_title = $row["job_title"];
        $this->profile_picture = $row["profile_picture"];
        $this->notes = $row["notes"];
        $this->favorite = $row["favorite"];
        $this->created_at = $row["created_at"];
        $this->updated_at = $row["updated_at"];
        $this->last_contacted_at = $row["last_contacted_at"];

        // Get contact information (phone, email, etc)
        $this->getContactInformation();

        // Get addresses
        $this->getAddresses();

        // Get groups
        $this->getGroups();

        // Get tags
        $this->getTags();

        // Log this view as a recent contact
        $this->logContactView();

        return true;
    }

    // Get contact information (phone, email, etc)
    private function getContactInformation() {
        $query = "SELECT * FROM " . $this->info_table . "
                  WHERE contact_id = :contact_id
                  ORDER BY is_primary DESC, info_type, info_label";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->execute();

        $this->contact_info = [];

        while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $this->contact_info[] = [
                'info_id' => $row['info_id'],
                'info_type' => $row['info_type'],
                'info_label' => $row['info_label'],
                'info_value' => $row['info_value'],
                'is_primary' => $row['is_primary']
            ];
        }
    }

    // Get addresses
    private function getAddresses() {
        $query = "SELECT * FROM " . $this->address_table . "
                  WHERE contact_id = :contact_id
                  ORDER BY is_primary DESC, address_label";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->execute();

        $this->addresses = [];

        while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $this->addresses[] = [
                'address_id' => $row['address_id'],
                'address_label' => $row['address_label'],
                'street_address' => $row['street_address'],
                'unit_number' => $row['unit_number'],
                'city' => $row['city'],
                'state_province' => $row['state_province'],
                'postal_code' => $row['postal_code'],
                'country' => $row['country'],
                'is_primary' => $row['is_primary']
            ];
        }
    }

    // Get groups
    private function getGroups() {
        $query = "SELECT g.* FROM ContactGroups g
                  JOIN ContactGroupMembership m ON g.group_id = m.group_id
                  WHERE m.contact_id = :contact_id
                  ORDER BY g.group_name";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->execute();

        $this->groups = [];

        while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $this->groups[] = [
                'group_id' => $row['group_id'],
                'group_name' => $row['group_name'],
                'description' => $row['description'],
                'color' => $row['color']
            ];
        }
    }

    // Get tags
    private function getTags() {
        $query = "SELECT t.* FROM Tags t
                  JOIN ContactTags ct ON t.tag_id = ct.tag_id
                  WHERE ct.contact_id = :contact_id
                  ORDER BY t.tag_name";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->execute();

        $this->tags = [];

        while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $this->tags[] = [
                'tag_id' => $row['tag_id'],
                'tag_name' => $row['tag_name'],
                'color' => $row['color']
            ];
        }
    }

    // Log this contact view in recent_contacts
    private function logContactView() {
        $query = "INSERT INTO recent_contacts (user_id, contact_id, viewed_at)
                  VALUES (:user_id, :contact_id, CURRENT_TIMESTAMP)
                  ON DUPLICATE KEY UPDATE viewed_at = CURRENT_TIMESTAMP";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":user_id", $this->user_id);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->execute();

        // Also update the last_contacted_at field in contacts
        $query = "UPDATE " . $this->table_name . "
                  SET last_contacted_at = CURRENT_TIMESTAMP
                  WHERE contact_id = :contact_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->execute();
    }

    // Update contact
    public function update() {
        // Query to update record
        $query = "UPDATE " . $this->table_name . "
                  SET first_name = :first_name,
                      last_name = :last_name,
                      company = :company,
                      job_title = :job_title,
                      profile_picture = :profile_picture,
                      notes = :notes,
                      favorite = :favorite
                  WHERE contact_id = :contact_id AND user_id = :user_id";

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $this->first_name = htmlspecialchars(strip_tags($this->first_name));
        $this->last_name = htmlspecialchars(strip_tags($this->last_name));
        $this->company = htmlspecialchars(strip_tags($this->company));
        $this->job_title = htmlspecialchars(strip_tags($this->job_title));
        $this->profile_picture = htmlspecialchars(strip_tags($this->profile_picture));
        $this->notes = htmlspecialchars(strip_tags($this->notes));
        $this->favorite = $this->favorite ? 1 : 0;

        // Bind values
        $stmt->bindParam(":first_name", $this->first_name);
        $stmt->bindParam(":last_name", $this->last_name);
        $stmt->bindParam(":company", $this->company);
        $stmt->bindParam(":job_title", $this->job_title);
        $stmt->bindParam(":profile_picture", $this->profile_picture);
        $stmt->bindParam(":notes", $this->notes);
        $stmt->bindParam(":favorite", $this->favorite);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":user_id", $this->user_id);

        // Execute query
        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Delete contact
    public function delete() {
        // Query to delete contact
        $query = "DELETE FROM " . $this->table_name . "
                  WHERE contact_id = :contact_id AND user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":user_id", $this->user_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Toggle favorite status
    public function toggleFavorite() {
        $query = "UPDATE " . $this->table_name . "
                  SET favorite = NOT favorite
                  WHERE contact_id = :contact_id AND user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":user_id", $this->user_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Add contact information (phone, email, etc)
    public function addContactInfo($info_type, $info_value, $info_label = null, $is_primary = 0) {
        $query = "INSERT INTO " . $this->info_table . "
                  (contact_id, info_type, info_label, info_value, is_primary)
                  VALUES (:contact_id, :info_type, :info_label, :info_value, :is_primary)";

        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $info_type = htmlspecialchars(strip_tags($info_type));
        $info_value = htmlspecialchars(strip_tags($info_value));
        $info_label = htmlspecialchars(strip_tags($info_label));
        $is_primary = $is_primary ? 1 : 0;

        // If setting this as primary, unset other primary for this type
        if($is_primary) {
            $this->unsetPrimaryContactInfo($info_type);
        }

        // Bind values
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":info_type", $info_type);
        $stmt->bindParam(":info_label", $info_label);
        $stmt->bindParam(":info_value", $info_value);
        $stmt->bindParam(":is_primary", $is_primary);

        if($stmt->execute()) {
            return $this->conn->lastInsertId();
        }

        return false;
    }

    // Unset primary contact info for a specific type
    private function unsetPrimaryContactInfo($info_type) {
        $query = "UPDATE " . $this->info_table . "
                  SET is_primary = 0
                  WHERE contact_id = :contact_id AND info_type = :info_type";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":info_type", $info_type);
        $stmt->execute();
    }

    // Update contact information
    public function updateContactInfo($info_id, $info_value, $info_label = null, $is_primary = null) {
        // First check if this contact info belongs to this contact
        $check_query = "SELECT info_type FROM " . $this->info_table . "
                        WHERE info_id = :info_id AND contact_id = :contact_id";

        $check_stmt = $this->conn->prepare($check_query);
        $check_stmt->bindParam(":info_id", $info_id);
        $check_stmt->bindParam(":contact_id", $this->contact_id);
        $check_stmt->execute();

        if($check_stmt->rowCount() == 0) {
            return false;
        }

        $row = $check_stmt->fetch(PDO::FETCH_ASSOC);
        $info_type = $row['info_type'];

        // Update query
        $query = "UPDATE " . $this->info_table . "
                  SET info_value = :info_value";

        if($info_label !== null) {
            $query .= ", info_label = :info_label";
        }

        if($is_primary !== null) {
            $query .= ", is_primary = :is_primary";
        }

        $query .= " WHERE info_id = :info_id AND contact_id = :contact_id";

        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $info_value = htmlspecialchars(strip_tags($info_value));

        // Bind values
        $stmt->bindParam(":info_value", $info_value);

        if($info_label !== null) {
            $info_label = htmlspecialchars(strip_tags($info_label));
            $stmt->bindParam(":info_label", $info_label);
        }

        if($is_primary !== null) {
            $is_primary = $is_primary ? 1 : 0;
            $stmt->bindParam(":is_primary", $is_primary);

            // If setting this as primary, unset other primary for this type
            if($is_primary) {
                $this->unsetPrimaryContactInfo($info_type);
            }
        }

        $stmt->bindParam(":info_id", $info_id);
        $stmt->bindParam(":contact_id", $this->contact_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Delete contact information
    public function deleteContactInfo($info_id) {
        // First check if this contact info belongs to this contact
        $check_query = "SELECT COUNT(*) FROM " . $this->info_table . "
                        WHERE info_id = :info_id AND contact_id = :contact_id";

        $check_stmt = $this->conn->prepare($check_query);
        $check_stmt->bindParam(":info_id", $info_id);
        $check_stmt->bindParam(":contact_id", $this->contact_id);
        $check_stmt->execute();

        $count = $check_stmt->fetchColumn();

        if($count == 0) {
            return false;
        }

        // Delete query
        $query = "DELETE FROM " . $this->info_table . "
                  WHERE info_id = :info_id AND contact_id = :contact_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":info_id", $info_id);
        $stmt->bindParam(":contact_id", $this->contact_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Add contact to group
    public function addToGroup($group_id) {
        // Check if already in group
        $check_query = "SELECT COUNT(*) FROM ContactGroupMembership
                        WHERE contact_id = :contact_id AND group_id = :group_id";

        $check_stmt = $this->conn->prepare($check_query);
        $check_stmt->bindParam(":contact_id", $this->contact_id);
        $check_stmt->bindParam(":group_id", $group_id);
        $check_stmt->execute();

        $count = $check_stmt->fetchColumn();

        if($count > 0) {
            return true; // Already in group
        }

        // Add to group
        $query = "INSERT INTO ContactGroupMembership (contact_id, group_id)
                  VALUES (:contact_id, :group_id)";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":group_id", $group_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Remove contact from group
    public function removeFromGroup($group_id) {
        $query = "DELETE FROM ContactGroupMembership
                  WHERE contact_id = :contact_id AND group_id = :group_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":group_id", $group_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Add tag to contact
    public function addTag($tag_id) {
        // Check if tag already applied
        $check_query = "SELECT COUNT(*) FROM ContactTags
                        WHERE contact_id = :contact_id AND tag_id = :tag_id";

        $check_stmt = $this->conn->prepare($check_query);
        $check_stmt->bindParam(":contact_id", $this->contact_id);
        $check_stmt->bindParam(":tag_id", $tag_id);
        $check_stmt->execute();

        $count = $check_stmt->fetchColumn();

        if($count > 0) {
            return true; // Tag already applied
        }

        // Add tag
        $query = "INSERT INTO ContactTags (contact_id, tag_id)
                  VALUES (:contact_id, :tag_id)";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":tag_id", $tag_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Remove tag from contact
    public function removeTag($tag_id) {
        $query = "DELETE FROM ContactTags
                  WHERE contact_id = :contact_id AND tag_id = :tag_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":contact_id", $this->contact_id);
        $stmt->bindParam(":tag_id", $tag_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }
}
?>