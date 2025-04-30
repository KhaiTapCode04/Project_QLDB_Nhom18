package ui.view
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.viewmodel.users.UploadImgViewModel
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.UserService
import ui.viewmodel.users.User_state
import kotlin.math.max
import kotlin.math.min

@Composable
fun EditProfile(navController: NavHostController, viewModel: User_state) {
    val context = LocalContext.current

    // Collect initial values from viewModel
    val userId by viewModel.user_id.collectAsState()
    val initialName by viewModel.userName.collectAsState()
    val initialEmail by viewModel.email.collectAsState()
    val initialPhone by viewModel.phone.collectAsState()
    val profileImage by viewModel.profile_picturel.collectAsState()

    // Create mutable state for editable fields
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf(initialPhone ?: "") }
    var isLoading by remember { mutableStateOf(false) }

    // State for edit mode (profile info or password change)
    var editMode by remember { mutableStateOf("profile") } // "profile" or "password"
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


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
                                var a = UploadImgViewModel.UploadImg1(context,userId,croppedBitmap!!)
                                if(a?.isSuccess == true){
                                    val userPrefs = UserPreferencesManager(context)
                                    viewModel.updatePicture(userPrefs.getProfilePicture())
                                    Toast.makeText(context,a.imageUrl, Toast.LENGTH_LONG).show()
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
    }else{
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (editMode == "profile") "Chỉnh sửa hồ sơ" else "Thay đổi mật khẩu"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image always visible
            Box(
                modifier = Modifier.padding(bottom = 8.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(profileImage)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Camera/Edit icon for profile image (only shown in profile edit mode)
                if (editMode == "profile") {
                    FloatingActionButton(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        modifier = Modifier.size(40.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Change profile picture",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle buttons for edit mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { editMode = "profile" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (editMode == "profile")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Edit Profile"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thông tin cá nhân")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { editMode = "password" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (editMode == "password")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Change Password"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đổi mật khẩu")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Information Section - only shown when editMode is "profile"
            if (editMode == "profile") {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Số điện thoại") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Password Change Section - only shown when editMode is "password"
            if (editMode == "password") {
                Text(
                    text = "Cập nhật mật khẩu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))


                // Đã xóa khai báo trùng lặp passwordVisible

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    // Sửa elevation thành Material 3 style
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Đổi Mật Khẩu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF5722)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Mật khẩu hiện tại") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(id = if (passwordVisible) R.drawable.hide else R.drawable.unhide),
                                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Mật khẩu mới") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                    Icon(
                                        painter = painterResource(id = if (newPasswordVisible) R.drawable.hide else R.drawable.unhide),
                                        contentDescription = if (newPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                    )
                                }
                            },
                            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Xác nhận mật khẩu mới") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        painter = painterResource(id = if (confirmPasswordVisible) R.drawable.hide else R.drawable.unhide),
                                        contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            isError = newPassword != confirmPassword && confirmPassword.isNotEmpty(),
                        )

                        if (newPassword != confirmPassword && confirmPassword.isNotEmpty()) {
                            Text(
                                text = "Mật khẩu xác nhận không khớp",
                                // Sửa lại để sử dụng Material 3
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save button
            Button(
                onClick = {

                    isLoading = true
                    if (editMode == "profile") {
                        scope.launch {
                            try {
                                val loginHandler = withContext(Dispatchers.IO) {
                                    UserService.update_user(context, userId, email, phone)
                                }

                                if (loginHandler == true) {
                                    val userPrefs = UserPreferencesManager(context)
                                    viewModel.updateUser(
                                        userPrefs.getUserId(),
                                        userPrefs.getUserName(),
                                        userPrefs.getUserEmail(),
                                        userPrefs.getUserPhone(),
                                        userPrefs.getProfilePicture()
                                    )
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Không thể thay đổi thông tin", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Không thể thay đổi thông tin", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    } else{

                        if(newPassword != confirmPassword){
                            Toast.makeText(context, "Không đúng", Toast.LENGTH_SHORT).show()
                            isLoading = false
                    }
                        else{
                        scope.launch {
                            try {
                                val loginHandler = withContext(Dispatchers.IO) {
                                    UserService.update_pass( userId, currentPassword,newPassword)
                                }
                                if (loginHandler == true) {
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Không thể thay đổi thông tin1", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Không thể thay đổi thông tin", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (editMode == "profile") "Lưu thông tin" else "Cập nhật mật khẩu")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Huỷ bỏ")
            }
        }
        }
    }
}