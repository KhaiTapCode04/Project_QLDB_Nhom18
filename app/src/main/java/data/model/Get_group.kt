package data.model

data class Get_group(
    val isSuccess: Boolean,
    val reason: String,
    val data: List<group>
)
data class group(
    val group_id: String,
    val group_name: String,
)