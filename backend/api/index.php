<?php
// backend/api/index.php - Main API router

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include response class
include_once 'classes/Response.php';

// Get request URI
$request_uri = $_SERVER['REQUEST_URI'];

// Parse the request path
$path = parse_url($request_uri, PHP_URL_PATH);
$path = ltrim($path, '/');

// Remove 'api/' prefix if present
if (strpos($path, 'api/') === 0) {
    $path = substr($path, 4);
}

// Handle preflight OPTIONS request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Define API endpoints mapping
$endpoints = [
    // Authentication
    'login' => 'login.php',
    'register' => 'register.php',
     // Endpoint kiểm tra kết nối
        'connection/test' => 'connection/test.php',

    // Contacts
    'contacts' => 'contacts/read.php',
    'contacts/read_one' => 'contacts/read_one.php',
    'contacts/create' => 'contacts/create.php',
    'contacts/update' => 'contacts/update.php',
    'contacts/delete' => 'contacts/delete.php',
    'contacts/toggle_favorite' => 'contacts/toggle_favorite.php',

    // Groups
    'groups' => 'groups/read.php',
    'groups/read_one' => 'groups/read_one.php',
    'groups/create' => 'groups/create.php',
    'groups/update' => 'groups/update.php',
    'groups/delete' => 'groups/delete.php',

    // Tags
    'tags' => 'tags/read.php',
    'tags/read_one' => 'tags/read_one.php',
    'tags/create' => 'tags/create.php',
    'tags/update' => 'tags/update.php',
    'tags/delete' => 'tags/delete.php',

    // Settings
    'settings' => 'settings/read.php',
    'settings/update' => 'settings/update.php',

    // Import/Export
    'import' => 'import_export/import.php',
    'export' => 'import_export/export.php',

    // User profile
    'user/profile' => 'user/profile.php',
    'user/update' => 'user/update.php',
    'user/change_password' => 'user/change_password.php'
];

// Check if the path exists in our endpoints
if (isset($endpoints[$path])) {
    // Include the appropriate endpoint file
    include_once $endpoints[$path];
} else {
    // Endpoint not found
    Response::notFound("API endpoint not found");
}
?>