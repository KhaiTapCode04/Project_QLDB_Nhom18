<?php
// backend/api/contacts/read.php - Get all contacts

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

// Get parameters for pagination, search, filtering, sorting
$page = isset($_GET['page']) && is_numeric($_GET['page']) ? (int)$_GET['page'] : 1;
$limit = isset($_GET['limit']) && is_numeric($_GET['limit']) ? (int)$_GET['limit'] : 10;
$search = isset($_GET['search']) ? $_GET['search'] : "";

// Prepare filter array
$filter = [];
if(isset($_GET['favorite']) && $_GET['favorite'] === '1') {
    $filter['favorite'] = true;
}
if(isset($_GET['group_id']) && is_numeric($_GET['group_id'])) {
    $filter['group_id'] = (int)$_GET['group_id'];
}
if(isset($_GET['tag_id']) && is_numeric($_GET['tag_id'])) {
    $filter['tag_id'] = (int)$_GET['tag_id'];
}

// Prepare sort array
$sort = [];
if(isset($_GET['sort_field']) && isset($_GET['sort_direction'])) {
    $sort['field'] = $_GET['sort_field'];
    $sort['direction'] = $_GET['sort_direction'];
}

// Get contacts
$stmt = $contact->readAll($user_id, $search, $filter, $sort, $page, $limit);
$num = $stmt->rowCount();

// Count total records for pagination
$totalRecords = $contact->countAll($user_id, $search, $filter);
$totalPages = ceil($totalRecords / $limit);

if($num > 0) {
    // Contacts array
    $contacts_arr = [];
    $contacts_arr["records"] = [];
    $contacts_arr["pagination"] = [
        "page" => $page,
        "limit" => $limit,
        "total_records" => $totalRecords,
        "total_pages" => $totalPages
    ];

    // Retrieve and format contacts
    while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        // Format primary contact info (phone, email, etc.) from the combined string
        $formatted_contact_info = [];
        if(isset($contact_info) && !empty($contact_info)) {
            $info_parts = explode("|", $contact_info);
            foreach($info_parts as $part) {
                list($type, $value) = explode(":", $part, 2);
                $formatted_contact_info[$type] = $value;
            }
        }

        $contact_item = [
            "contact_id" => $contact_id,
            "first_name" => $first_name,
            "last_name" => $last_name,
            "company" => $company,
            "job_title" => $job_title,
            "profile_picture" => $profile_picture,
            "favorite" => (bool)$favorite,
            "primary_info" => $formatted_contact_info,
            "created_at" => $created_at,
            "updated_at" => $updated_at,
            "last_contacted_at" => $last_contacted_at
        ];

        array_push($contacts_arr["records"], $contact_item);
    }

    // Return contacts
    Response::success("Contacts retrieved successfully", $contacts_arr);
} else {
    // No contacts found
    Response::success("No contacts found", ["records" => [], "pagination" => [
        "page" => $page,
        "limit" => $limit,
        "total_records" => 0,
        "total_pages" => 0
    ]]);
}
?>