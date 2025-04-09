<?php
// backend/api/groups/read.php - Get all groups

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include required files
include_once '../config/database.php';
include_once '../classes/ContactGroup.php';
include_once '../classes/Auth.php';
include_once '../classes/Response.php';

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

// Get groups
$stmt = $group->readAll($user_id);
$num = $stmt->rowCount();

if($num > 0) {
    // Groups array
    $groups_arr = [];
    $groups_arr["records"] = [];

    // Retrieve and format groups
    while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $group_item = [
            "group_id" => $group_id,
            "group_name" => $group_name,
            "description" => $description,
            "color" => $color,
            "contact_count" => $contact_count,
            "created_at" => $created_at,
            "updated_at" => $updated_at
        ];

        array_push($groups_arr["records"], $group_item);
    }

    // Return groups
    Response::success("Groups retrieved successfully", $groups_arr);
} else {
    // No groups found
    Response::success("No groups found", ["records" => []]);
}
?>