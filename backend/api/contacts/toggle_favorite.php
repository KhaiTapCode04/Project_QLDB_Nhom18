<?php
// backend/api/contacts/toggle_favorite.php - Toggle contact favorite status

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: PUT");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include required files
include_once '../config/database.php';
include_once '../classes/Contact.php';
include_once '../classes/Auth.php';
include_once '../classes/Response.php';

// Handle preflight OPTIONS request
if($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Check if it's a PUT request
if($_SERVER['REQUEST_METHOD'] !== 'PUT') {
    Response::error("Only PUT method is allowed", 405);
}

// Get database connection
$database = new Database();
$db = $database->getConnection();

// Initialize auth and contact objects
$auth = new Auth();
$contact = new Contact($db);

// Get token from headers
$token = $auth->getBearerToken();

// Validate token
if(!$token || !($user_data = $auth->validateToken($token))) {
    Response::unauthorized("Invalid or missing token");
}

// Get user ID from token
$user_id = $user_data->user_id;

// Get contact ID from URL
$contact_id = isset($_GET['id']) && is_numeric($_GET['id']) ? (int)$_GET['id'] : 0;

if($contact_id <= 0) {
    Response::error("Invalid contact ID");
}

// Set contact properties
$contact->contact_id = $contact_id;
$contact->user_id = $user_id;

// Toggle favorite status
if($contact->toggleFavorite()) {
    // Get updated status
    $contact->readOne($contact_id, $user_id);

    // Return success response
    Response::success("Favorite status toggled successfully", [
        "favorite" => (bool)$contact->favorite
    ]);
} else {
    // Failed to toggle favorite status
    Response::error("Failed to toggle favorite status");
}
?>