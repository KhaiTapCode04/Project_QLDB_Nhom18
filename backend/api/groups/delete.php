<?php
// backend/api/groups/delete.php - Delete group

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: DELETE");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include required files
include_once '../config/database.php';
include_once '../classes/ContactGroup.php';
include_once '../classes/Auth.php';
include_once '../classes/Response.php';

// Handle preflight OPTIONS request
if($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Check if it's a DELETE request
if($_SERVER['REQUEST_METHOD'] !== 'DELETE') {
    Response::error("Only DELETE method is allowed", 405);
}

// Get database connection
$database = new Database();
$db = $database->getConnection();

// Initialize auth and group objects
$auth = new Auth();
$group = new ContactGroup($db);

// Get token from headers
$token = $auth->getBearerToken();

// Validate token
if(!$token || !($user_data = $auth->validateToken($token))) {
    Response::unauthorized("Invalid or missing token");
}

// Get user ID from token
$user_id = $user_data->user_id;

// Get group ID from URL
$group_id = isset($_GET['id']) && is_numeric($_GET['id']) ? (int)$_GET['id'] : 0;

if($group_id <= 0) {
    Response::error("Invalid group ID");
}

// Check if group exists and belongs to user
if(!$group->readOne($group_id, $user_id)) {
    Response::notFound("Group not found");
}

// Delete the group
if($group->delete()) {
    // Return success response
    Response::success("Group deleted successfully");
} else {
    // Failed to delete group
    Response::error("Failed to delete group");
}
?>