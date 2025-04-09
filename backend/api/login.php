<?php
// backend/api/login.php

// Set headers for CORS
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include required files
include_once 'config/database.php';
include_once 'classes/User.php';
include_once 'classes/Auth.php';
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

// Check if username/email and password are provided
if(empty($data->username) || empty($data->password)) {
    Response::error("Username/email and password are required");
}

// Initialize user object
$user = new User($db);

// Attempt login
if($user->login($data->username, $data->password)) {
    // Login successful, generate JWT token
    $auth = new Auth();
    $token_data = $auth->generateToken($user->user_id, $user->username, $user->role);

    // Return success with token and user info
    Response::success("Login successful", [
        "token" => $token_data["token"],
        "expires" => $token_data["expires"],
        "user" => [
            "user_id" => $user->user_id,
            "username" => $user->username,
            "first_name" => $user->first_name,
            "last_name" => $user->last_name,
            "email" => $user->email,
            "role" => $user->role
        ]
    ]);
} else {
    // Login failed
    Response::error("Invalid username/email or password", 401);
}
?>