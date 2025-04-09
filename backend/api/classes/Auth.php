<?php
// backend/api/classes/Auth.php
class Auth {
    // Secret key for JWT - change this to a secure random string
    private $secret_key = "your_secret_key_for_contact_manager_app";

    // Token expiration time (in seconds)
    private $token_expiry = 86400; // 24 hours

    // Generate JWT token
    public function generateToken($user_id, $username, $role) {
        $issued_at = time();
        $expiration = $issued_at + $this->token_expiry;

        $payload = [
            "iss" => "contact_manager_api", // Issuer
            "iat" => $issued_at,            // Issued at
            "exp" => $expiration,           // Expiration
            "user_id" => $user_id,
            "username" => $username,
            "role" => $role
        ];

        // Generate JWT
        $jwt = $this->encodeToken($payload);

        return [
            "token" => $jwt,
            "expires" => $expiration
        ];
    }

    // Validate JWT token
    public function validateToken($token) {
        try {
            $decoded = $this->decodeToken($token);

            // Check if token is expired
            if($decoded->exp < time()) {
                return false;
            }

            return $decoded;
        } catch(Exception $e) {
            return false;
        }
    }

    // Get user ID from token
    public function getUserIdFromToken($token) {
        $decoded = $this->validateToken($token);

        if($decoded) {
            return $decoded->user_id;
        }

        return null;
    }

    // Check if user is admin from token
    public function isAdmin($token) {
        $decoded = $this->validateToken($token);

        if($decoded && isset($decoded->role)) {
            return $decoded->role === "admin";
        }

        return false;
    }

    // Encode JWT token (simplified implementation)
    private function encodeToken($payload) {
        // Base64Url encode the header
        $header = json_encode(["typ" => "JWT", "alg" => "HS256"]);
        $base64UrlHeader = $this->base64UrlEncode($header);

        // Base64Url encode the payload
        $base64UrlPayload = $this->base64UrlEncode(json_encode($payload));

        // Create signature
        $signature = hash_hmac('sha256', $base64UrlHeader . "." . $base64UrlPayload, $this->secret_key, true);
        $base64UrlSignature = $this->base64UrlEncode($signature);

        // Create JWT
        $jwt = $base64UrlHeader . "." . $base64UrlPayload . "." . $base64UrlSignature;

        return $jwt;
    }

    // Decode JWT token
    private function decodeToken($token) {
        // Split the token
        $tokenParts = explode(".", $token);

        if(count($tokenParts) != 3) {
            throw new Exception("Invalid token format");
        }

        $header = $tokenParts[0];
        $payload = $tokenParts[1];
        $signatureProvided = $tokenParts[2];

        // Check the signature
        $signature = hash_hmac('sha256', $header . "." . $payload, $this->secret_key, true);
        $base64UrlSignature = $this->base64UrlEncode($signature);

        if($base64UrlSignature !== $signatureProvided) {
            throw new Exception("Invalid signature");
        }

        // Get payload data
        $payloadData = json_decode($this->base64UrlDecode($payload));

        return $payloadData;
    }

    // Base64Url encode
    private function base64UrlEncode($data) {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    // Base64Url decode
    private function base64UrlDecode($data) {
        return base64_decode(strtr($data, '-_', '+/'));
    }

    // Get token from headers
    public function getBearerToken() {
        $headers = null;

        if(isset($_SERVER['Authorization'])) {
            $headers = trim($_SERVER["Authorization"]);
        } else if(isset($_SERVER['HTTP_AUTHORIZATION'])) {
            $headers = trim($_SERVER["HTTP_AUTHORIZATION"]);
        } else if(function_exists('apache_request_headers')) {
            $requestHeaders = apache_request_headers();
            $requestHeaders = array_combine(
                array_map('ucwords', array_keys($requestHeaders)),
                array_values($requestHeaders)
            );

            if(isset($requestHeaders['Authorization'])) {
                $headers = trim($requestHeaders['Authorization']);
            }
        }

        // Get the token from the headers
        if($headers) {
            if(preg_match('/Bearer\s(\S+)/', $headers, $matches)) {
                return $matches[1];
            }
        }

        return null;
    }
}
?>