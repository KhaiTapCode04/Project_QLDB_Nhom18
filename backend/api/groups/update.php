<?php
// backend/api/groups/update.php - Update existing group

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: PUT");
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

// Check if it's a PUT request
if($_SERVER['REQUEST_METHOD'] !== 'PUT') {
    Response::error("Only PUT method is allowed", 405);
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

// Get posted data
$data = json_decode(file_get_contents("php://input"));

// Check if group_id is provided
if(!property_exists($data, 'group_id') || empty($data->group_id)) {
    Response::error("Group ID is required");
}

// Check if group exists and belongs to user
if(!$group->readOne($data->group_id, $user_id)) {
    Response::notFound("Group not found");
}

// Update group properties if provided
if(property_exists($data, 'group_name')) {
    $group->group_name = $data->group_name;
}
if(property_exists($data, 'description')) {
    $group->description = $data->description;
}
if(property_exists($data, 'color')) {
    $group->color = $data->color;
}

// Check if group name already exists (only if name is being changed)
if(property_exists($data, 'group_name') && $group->groupNameExists()) {
    Response::error("Group name already exists");
}

// Update the group
if($group->update()) {
    // Handle contact additions and removals if provided
    if(property_exists($data, 'add_contacts') && is_array($data->add_contacts)) {
        $group->addContacts($data->add_contacts);
    }

    if(property_exists($data, 'remove_contacts') && is_array($data->remove_contacts)) {
        $group->removeContacts($data->remove_contacts);
    }

    // Return success response
    Response::success("Group updated successfully");
} else {
    // Failed to update group
    Response::error("Failed to update group");
}
?>