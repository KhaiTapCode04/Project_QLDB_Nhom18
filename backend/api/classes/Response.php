<?php
// backend/api/classes/Response.php
class Response {
    public static function json($data, $status_code = 200) {
        // Set the HTTP response code
        http_response_code($status_code);

        // Set headers for JSON response
        header("Content-Type: application/json; charset=UTF-8");
        header("Access-Control-Allow-Origin: *");
        header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
        header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

        // Return the JSON response
        echo json_encode($data);
        exit;
    }

    public static function success($message, $data = []) {
        return self::json([
            "status" => "success",
            "message" => $message,
            "data" => $data
        ]);
    }

    public static function error($message, $status_code = 400, $errors = []) {
        return self::json([
            "status" => "error",
            "message" => $message,
            "errors" => $errors
        ], $status_code);
    }

    public static function unauthorized($message = "Unauthorized access") {
        return self::error($message, 401);
    }

    public static function notFound($message = "Resource not found") {
        return self::error($message, 404);
    }

    public static function serverError($message = "Internal server error") {
        return self::error($message, 500);
    }
}
?>