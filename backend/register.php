<?php
// backend/api/register.php

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include required files
include_once 'config/database.php';
include_once 'classes/User.php';
include_once 'classes/Response.php';

// Check if it's a POST request
if($_SERVER['REQUEST_METHOD'] !== 'POST') {
    Response::error("Only POST method is allowed", 405);
}

// Get database connection
$database = new Database();
$db = $database->getConnection();

// Get posted data
$data = json_decode(file_get_contents("php://input"));

// Check if required fields are provided
if(
    empty($data->username) ||
    empty($data->password) ||
    empty($data->email) ||
    empty($data->first_name) ||
    empty($data->last_name)
) {
    Response::error("Incomplete data. All fields are required.");
}

// Create user object
$user = new User($db);
$user->username = $data->username;
$user->password_hash = $data->password;
$user->email = $data->email;
$user->first_name = $data->first_name;
$user->last_name = $data->last_name;

// Attempt to register
if($user->register()) {
    // Registration successful
    Response::success("User registered successfully");
} else {
    // Registration failed
    Response::error("Username or email already exists");
}
?>