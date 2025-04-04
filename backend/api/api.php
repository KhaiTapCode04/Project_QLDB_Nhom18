<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once "../config/db_connect.php";

// Phương thức request
$method = $_SERVER['REQUEST_METHOD'];

// Lấy dữ liệu từ client
$data = json_decode(file_get_contents("php://input"), true);

// Kiểm tra kết nối và trả về kết quả
switch($method) {
    case 'GET':
        // Kiểm tra kết nối
        if(isset($_GET['check_connection'])) {
            echo json_encode(["status" => "success", "message" => "Kết nối thành công đến cơ sở dữ liệu"]);
            break;
        }

        // Lấy tất cả contacts
        $sql = "SELECT c.id, c.name, c.profile_picture, c.is_favorite,
                       p.number as phone, e.email
                FROM contacts c
                LEFT JOIN phone_numbers p ON c.id = p.contact_id AND p.is_primary = 1
                LEFT JOIN emails e ON c.id = e.contact_id AND e.is_primary = 1
                WHERE c.is_deleted = 0 AND c.user_id = 1
                ORDER BY c.name";

        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            $contacts = [];
            while($row = $result->fetch_assoc()) {
                $contacts[] = $row;
            }
            echo json_encode(["status" => "success", "data" => $contacts]);
        } else {
            echo json_encode(["status" => "success", "data" => []]);
        }
        break;

    case 'POST':
        // Thêm contact mới
        if(!empty($data)) {
            // Bắt đầu transaction
            $conn->begin_transaction();

            try {
                // Thêm contact
                $name = $data['name'];
                $is_favorite = isset($data['is_favorite']) ? $data['is_favorite'] : 0;

                $sql = "INSERT INTO contacts (user_id, name, is_favorite) VALUES (1, ?, ?)";
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("si", $name, $is_favorite);
                $stmt->execute();

                $contact_id = $conn->insert_id;

                // Thêm số điện thoại nếu có
                if(isset($data['phone']) && !empty($data['phone'])) {
                    $phone = $data['phone'];
                    $sql = "INSERT INTO phone_numbers (contact_id, number, label, is_primary) VALUES (?, ?, 'MOBILE', 1)";
                    $stmt = $conn->prepare($sql);
                    $stmt->bind_param("is", $contact_id, $phone);
                    $stmt->execute();
                }

                // Thêm email nếu có
                if(isset($data['email']) && !empty($data['email'])) {
                    $email = $data['email'];
                    $sql = "INSERT INTO emails (contact_id, email, label, is_primary) VALUES (?, ?, 'HOME', 1)";
                    $stmt = $conn->prepare($sql);
                    $stmt->bind_param("is", $contact_id, $email);
                    $stmt->execute();
                }

                // Commit transaction
                $conn->commit();

                echo json_encode(["status" => "success", "message" => "Contact thêm thành công", "id" => $contact_id]);
            } catch (Exception $e) {
                // Rollback transaction nếu có lỗi
                $conn->rollback();
                echo json_encode(["status" => "error", "message" => "Lỗi: " . $e->getMessage()]);
            }
        }
        break;

    default:
        echo json_encode(["status" => "error", "message" => "Phương thức không được hỗ trợ"]);
        break;
}

$conn->close();
?>