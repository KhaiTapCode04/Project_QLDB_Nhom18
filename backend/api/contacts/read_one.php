<?php
// backend/api/contacts/read_one.php - Get contact details

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include required files
include_once '../config/database.php';
include_once '../classes/Contact.php';
include_once '../classes/Auth.php';
include_once '../classes/Response.php';

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

// Get contact details
if($contact->readOne($contact_id, $user_id)) {
    // Create an array with contact data
    $contact_arr = [
        "contact_id" => $contact->contact_id,
        "first_name" => $contact->first_name,
        "last_name" => $contact->last_name,
        "company" => $contact->company,
        "job_title" => $contact->job_title,
        "profile_picture" => $contact->profile_picture,
        "notes" => $contact->notes,
        "favorite" => (bool)$contact->favorite,
        "created_at" => $contact->created_at,
        "updated_at" => $contact->updated_at,
        "last_contacted_at" => $contact->last_contacted_at,
        "contact_info" => $contact->contact_info,
        "addresses" => $contact->addresses,
        "groups" => $contact->groups,
        "tags" => $contact->tags
    ];

    // Return contact details
    Response::success("Contact found", $contact_arr);
} else {
    // Contact not found or doesn't belong to user
    Response::notFound("Contact not found");
}
?>