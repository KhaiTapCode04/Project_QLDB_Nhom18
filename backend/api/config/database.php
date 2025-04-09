<?php
// backend/api/config/database.php
class Database {
    // Database credentials
    private $host = "localhost";
    private $db_name = "contact_management_db"; // Match exactly with your phpMyAdmin database name
    private $username = "root";  // Default XAMPP username, change if different
    private $password = "";      // Default XAMPP empty password, change if different
    private $conn;

    // Get database connection
    public function getConnection() {
        $this->conn = null;

        try {
            $this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);
            $this->conn->exec("set names utf8");
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch(PDOException $exception) {
            echo "Connection error: " . $exception->getMessage();
        }

        return $this->conn;
    }
}
?>