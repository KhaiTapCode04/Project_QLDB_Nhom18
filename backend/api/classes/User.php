<?php
// backend/api/classes/User.php
class User {
    // Database connection and table name
    private $conn;
    private $table_name = "Users";

    // Object properties based on the Users table structure
    public $user_id;
    public $username;
    public $password_hash;
    public $email;
    public $first_name;
    public $last_name;
    public $profile_picture;
    public $is_active;
    public $role;
    public $created_at;
    public $updated_at;
    public $last_login_at;

    // Constructor with DB connection
    public function __construct($db) {
        $this->conn = $db;
    }

    // Login user
    public function login($username, $password) {
        // Query to find user by username or email
        $query = "SELECT user_id, username, password_hash, first_name, last_name, email, is_active, role
                  FROM " . $this->table_name . "
                  WHERE username = :username OR email = :email";

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Sanitize and bind
        $username = htmlspecialchars(strip_tags($username));
        $stmt->bindParam(":username", $username);
        $stmt->bindParam(":email", $username); // Allow login with email too

        // Execute query
        $stmt->execute();

        // Check if user exists
        if($stmt->rowCount() > 0) {
            // Get user data
            $row = $stmt->fetch(PDO::FETCH_ASSOC);

            // Check if user is active
            if(!$row["is_active"]) {
                return false; // User account is not active
            }

            // Verify password
            if(password_verify($password, $row["password_hash"])) {
                // Set user properties
                $this->user_id = $row["user_id"];
                $this->username = $row["username"];
                $this->first_name = $row["first_name"];
                $this->last_name = $row["last_name"];
                $this->email = $row["email"];
                $this->role = $row["role"];

                // Update last login
                $this->updateLastLogin();

                return true;
            }
        }

        return false;
    }

    // Register new user
    public function register() {
        // Check if username or email already exists
        if($this->usernameExists() || $this->emailExists()) {
            return false;
        }

        // Query to insert record
        $query = "INSERT INTO " . $this->table_name . "
                  SET username = :username,
                      password_hash = :password_hash,
                      email = :email,
                      first_name = :first_name,
                      last_name = :last_name,
                      is_active = :is_active,
                      role = :role";

        // Prepare query
        $stmt = $this->conn->prepare($query);

        // Sanitize inputs
        $this->username = htmlspecialchars(strip_tags($this->username));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->first_name = htmlspecialchars(strip_tags($this->first_name));
        $this->last_name = htmlspecialchars(strip_tags($this->last_name));

        // Hash the password
        $password_hash = password_hash($this->password_hash, PASSWORD_DEFAULT);

        // Set default values
        $is_active = 1;
        $role = "user";

        // Bind values
        $stmt->bindParam(":username", $this->username);
        $stmt->bindParam(":password_hash", $password_hash);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":first_name", $this->first_name);
        $stmt->bindParam(":last_name", $this->last_name);
        $stmt->bindParam(":is_active", $is_active);
        $stmt->bindParam(":role", $role);

        // Execute query
        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Check if username exists
    private function usernameExists() {
        $query = "SELECT user_id FROM " . $this->table_name . " WHERE username = :username";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":username", $this->username);
        $stmt->execute();

        return $stmt->rowCount() > 0;
    }

    // Check if email exists
    private function emailExists() {
        $query = "SELECT user_id FROM " . $this->table_name . " WHERE email = :email";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":email", $this->email);
        $stmt->execute();

        return $stmt->rowCount() > 0;
    }

    // Update last login timestamp
    private function updateLastLogin() {
        $query = "UPDATE " . $this->table_name . "
                  SET last_login_at = CURRENT_TIMESTAMP
                  WHERE user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":user_id", $this->user_id);
        $stmt->execute();
    }

    // Get user by ID
    public function getById($id) {
        $query = "SELECT * FROM " . $this->table_name . " WHERE user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":user_id", $id);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if($row) {
            $this->user_id = $row["user_id"];
            $this->username = $row["username"];
            $this->email = $row["email"];
            $this->first_name = $row["first_name"];
            $this->last_name = $row["last_name"];
            $this->profile_picture = $row["profile_picture"];
            $this->is_active = $row["is_active"];
            $this->role = $row["role"];
            $this->created_at = $row["created_at"];
            $this->updated_at = $row["updated_at"];
            $this->last_login_at = $row["last_login_at"];

            return true;
        }

        return false;
    }

    // Update user profile
    public function update() {
        $query = "UPDATE " . $this->table_name . "
                  SET first_name = :first_name,
                      last_name = :last_name,
                      profile_picture = :profile_picture
                  WHERE user_id = :user_id";

        $stmt = $this->conn->prepare($query);

        // Sanitize inputs
        $this->first_name = htmlspecialchars(strip_tags($this->first_name));
        $this->last_name = htmlspecialchars(strip_tags($this->last_name));
        $this->profile_picture = htmlspecialchars(strip_tags($this->profile_picture));

        // Bind values
        $stmt->bindParam(":first_name", $this->first_name);
        $stmt->bindParam(":last_name", $this->last_name);
        $stmt->bindParam(":profile_picture", $this->profile_picture);
        $stmt->bindParam(":user_id", $this->user_id);

        if($stmt->execute()) {
            return true;
        }

        return false;
    }

    // Change user password
    public function changePassword($current_password, $new_password) {
        // Verify current password
        $query = "SELECT password_hash FROM " . $this->table_name . " WHERE user_id = :user_id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":user_id", $this->user_id);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if(password_verify($current_password, $row["password_hash"])) {
            // Update password
            $query = "UPDATE " . $this->table_name . "
                      SET password_hash = :password_hash
                      WHERE user_id = :user_id";

            $stmt = $this->conn->prepare($query);

            // Hash new password
            $new_password_hash = password_hash($new_password, PASSWORD_DEFAULT);

            // Bind values
            $stmt->bindParam(":password_hash", $new_password_hash);
            $stmt->bindParam(":user_id", $this->user_id);

            if($stmt->execute()) {
                return true;
            }
        }

        return false;
    }
}
?>