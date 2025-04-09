<?php
// backend/api/config/config.php - General configuration settings

// Application settings
define("APP_NAME", "Contact Manager");
define("APP_VERSION", "1.0.0");

// API settings
define("API_BASE_URL", "http://localhost/contact_manager/backend/api"); // Change to your actual base URL
define("API_UPLOAD_DIR", "../uploads/");
define("API_MAX_UPLOAD_SIZE", 5 * 1024 * 1024); // 5MB

// Profile image settings
define("PROFILE_IMAGE_DIR", API_UPLOAD_DIR . "profile_images/");
define("PROFILE_IMAGE_MAX_WIDTH", 500);
define("PROFILE_IMAGE_MAX_HEIGHT", 500);
define("PROFILE_IMAGE_FORMATS", ["jpg", "jpeg", "png"]);

// Import/Export settings
define("IMPORT_FILE_DIR", API_UPLOAD_DIR . "import_files/");
define("SUPPORTED_IMPORT_FORMATS", ["csv", "json", "vcf"]);
define("MAX_IMPORT_FILE_SIZE", 10 * 1024 * 1024); // 10MB

// JWT settings
define("JWT_SECRET_KEY", "your_secret_key_for_contact_manager_app"); // Change this in production
define("JWT_EXPIRY", 24 * 60 * 60); // 24 hours (in seconds)

// Pagination defaults
define("DEFAULT_PAGE_LIMIT", 10);

// Ensure upload directories exist
if (!file_exists(API_UPLOAD_DIR)) {
    mkdir(API_UPLOAD_DIR, 0755, true);
}

if (!file_exists(PROFILE_IMAGE_DIR)) {
    mkdir(PROFILE_IMAGE_DIR, 0755, true);
}

if (!file_exists(IMPORT_FILE_DIR)) {
    mkdir(IMPORT_FILE_DIR, 0755, true);
}
?>