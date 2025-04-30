package ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.repository.UploadImg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import ui.viewmodel.users.UploadImgViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class img : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TransparentCircleCropperScreen()
                }
            }
        }
    }
}

@Composable
fun TransparentCircleCropperScreen() {
    val context = LocalContext.current

    // Trạng thái ảnh
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isCropping by remember { mutableStateOf(false) }

    // Trạng thái vị trí và tỷ lệ
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Kích thước container
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    // Trạng thái upload
    var isUploading by remember { mutableStateOf(false) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    // Để chọn ảnh
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                isCropping = true
                // Reset các giá trị
                scale = 1f
                offset = Offset.Zero
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi tải ảnh: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

//     Transformable state cho phép zoom và pan
    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        // Cập nhật scale với giới hạn
        scale = (scale * zoomChange).coerceIn(0.5f, 5f)
        // Tính toán giới hạn di chuyển
        val maxX = containerSize.width * scale * 0.5f
        val maxY = containerSize.height * scale * 0.5f
        // Cập nhật offset
        offset = Offset(
            x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
            y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
        )
    }
    // Hàm cắt ảnh thành hình tròn
    fun cropCircularImage(bitmap: Bitmap): Bitmap {
        val centerX = containerSize.width / 2f
        val centerY = containerSize.height / 2f
        val radius = min(containerSize.width, containerSize.height) * 0.35f

        // Xác định vị trí và scale của ảnh hiện tại
        val imageWidth = bitmap.width.toFloat()
        val imageHeight = bitmap.height.toFloat()

        // Đầu tiên, tính toán tỷ lệ hiển thị của ảnh trong container
        val contentScale = ContentScale.Fit
        val containerRatio = containerSize.width.toFloat() / containerSize.height.toFloat()
        val imageRatio = imageWidth / imageHeight

        // Tính kích thước thực tế của ảnh khi đã fit vào container (trước khi scale)
        var scaledWidth: Float
        var scaledHeight: Float

        if (containerRatio > imageRatio) {
            // Ảnh cao hơn container, sẽ fit theo chiều cao
            scaledHeight = containerSize.height.toFloat()
            scaledWidth = imageWidth * (scaledHeight / imageHeight)
        } else {
            // Ảnh rộng hơn container, sẽ fit theo chiều rộng
            scaledWidth = containerSize.width.toFloat()
            scaledHeight = imageHeight * (scaledWidth / imageWidth)
        }

        // Tính vị trí của ảnh trong container (trước khi áp dụng offset và scale)
        val imageLeft = (containerSize.width - scaledWidth) / 2
        val imageTop = (containerSize.height - scaledHeight) / 2

        // Tính tọa độ tương đối của điểm trung tâm trên ảnh đã scale
        val relativeX = centerX - (offset.x / scale) - imageLeft
        val relativeY = centerY - (offset.y / scale) - imageTop

        // Chuyển đổi sang tọa độ thực trên bitmap gốc
        val bitmapX = relativeX * (imageWidth / scaledWidth)
        val bitmapY = relativeY * (imageHeight / scaledHeight)

        // Tính bán kính vòng tròn trên bitmap gốc
        val bitmapRadius = (radius / scale) * (imageWidth / scaledWidth)

        // Giới hạn vùng cắt trong bitmap
        val left = max(0f, bitmapX - bitmapRadius).toInt()
        val top = max(0f, bitmapY - bitmapRadius).toInt()
        val right = min(bitmap.width.toFloat(), bitmapX + bitmapRadius).toInt()
        val bottom = min(bitmap.height.toFloat(), bitmapY + bitmapRadius).toInt()

        val width = right - left
        val height = bottom - top
        if (width <= 0 || height <= 0) {

            return bitmap
        }

        // Cắt bitmap thành hình chữ nhật
        val squareBitmap = try {
            Bitmap.createBitmap(bitmap, left, top, width, height)
        } catch (e: Exception) {
            // Trường hợp lỗi, log và trả về bitmap nguyên bản
            Log.e("CropImage", "Error cropping: ${e.message}")
            return bitmap
        }

        // Tạo bitmap hình tròn
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Vẽ hình tròn
        val canvas = android.graphics.Canvas(outputBitmap)
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.BLACK
        }

        canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f, paint)

        // Sử dụng PorterDuff để lấy phần giao nhau
        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(squareBitmap, 0f, 0f, paint)

        return outputBitmap
    }

    if (isCropping && bitmap != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size
                }
                // QUAN TRỌNG: Thêm transformable vào Box chính
                .transformable(state = transformableState)
        ) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Ảnh để cắt",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    ),
                contentScale = ContentScale.Fit
            )

            // Lớp overlay chỉ ở bên ngoài hình tròn
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = min(size.width, size.height) * 0.35f

                // Tạo path cho hình tròn
                val circlePath = Path().apply {
                    addOval(
                        Rect(
                            left = centerX - radius,
                            top = centerY - radius,
                            right = centerX + radius,
                            bottom = centerY + radius
                        )
                    )
                }

                // Tạo path cho toàn bộ màn hình
                val fullScreenPath = Path().apply {
                    addRect(Rect(0f, 0f, size.width, size.height))
                }

                // Tạo path cho phần ngoài hình tròn bằng cách trừ hình tròn khỏi toàn màn hình
                val outsidePath = Path.combine(
                    PathOperation.Difference,
                    fullScreenPath,
                    circlePath
                )

                // Chỉ vẽ overlay cho phần ngoài hình tròn
                drawPath(
                    path = outsidePath,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                // Vẽ viền trắng cho hình tròn
                drawCircle(
                    color = Color.White,
                    center = Offset(centerX, centerY),
                    radius = radius,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // Các nút điều khiển
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = {
                        isCropping = false
                    },
                    containerColor = Color.Gray,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Hủy",
                        tint = Color.White
                    )
                }

                FloatingActionButton(
                    onClick = {
                        croppedBitmap = cropCircularImage(bitmap!!)
                        isCropping = false
                        if(croppedBitmap != null){
                            scope.launch {
                                var a = UploadImgViewModel.UploadImg1(context,1,croppedBitmap!!)
                                if(a?.isSuccess==true){
                                    Toast.makeText(context,a.toString(), Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Xác nhận",
                        tint = Color.White
                    )
                }
            }
        }
    } else {
        // Màn hình chính
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ảnh Đại Diện",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Hiển thị ảnh đại diện
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray, CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                croppedBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Ảnh đại diện",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } ?: Text("Chưa có ảnh")
            }

            // Nút chọn ảnh
            Button(
                onClick = { launcher.launch("image/*") }
            ) {
                Text(
                    text = if (croppedBitmap == null) "Chọn ảnh" else "Thay đổi ảnh"
                )
            }

//             Nút tải ảnh lên nếu đã cắt xong
            if (croppedBitmap != null) {
                Button(
                    onClick = {
                        scope.launch {
                            var a = UploadImgViewModel.UploadImg1(context,1,croppedBitmap!!)
                            if(a?.isSuccess == true){
                                Toast.makeText(context,a.toString(), Toast.LENGTH_LONG).show()
                            }
                        }

                    },
                    enabled = !isUploading
                ) {
                    Text("Tải ảnh lên API")
                }
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Hiển thị thông báo trạng thái
                uploadStatus?.let { status ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (status.startsWith("Lỗi"))
                                Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                        )
                    ) {
                        Text(
                            text = status,
                            color = if (status.startsWith("Lỗi")) Color.Red else Color.Green,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                uploadedImageUrl?.let { url ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "URL ảnh:",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = url,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}