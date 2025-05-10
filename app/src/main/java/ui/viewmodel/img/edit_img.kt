package ui.viewmodel.img
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import kotlin.math.max
import kotlin.math.min
object ImageProcessor {

    fun cropCircularImage(
        bitmap: Bitmap,
        containerSize: IntSize,
        scale: Float,
        offset: Offset
    ): Bitmap? {
        val centerX = containerSize.width / 2f
        val centerY = containerSize.height / 2f
        val radius = min(containerSize.width, containerSize.height) * 0.35f

        // Xác định vị trí và scale của ảnh hiện tại
        val imageWidth = bitmap.width.toFloat()
        val imageHeight = bitmap.height.toFloat()

        // Tính toán tỷ lệ hiển thị của ảnh trong container
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
            Log.e("CropImage", "Invalid crop dimensions: width=$width, height=$height")
            return null
        }

        // Cắt bitmap thành hình chữ nhật
        val squareBitmap = try {
            Bitmap.createBitmap(bitmap, left, top, width, height)
        } catch (e: Exception) {
            Log.e("CropImage", "Error cropping: ${e.message}")
            return null
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
}