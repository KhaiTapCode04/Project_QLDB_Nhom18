package data.model

data class Get_contacts(
    val isSuccess: Boolean,
    val reason: String,
    val data: List<Contact>
)
data class Contact(
    val contact_id: Int,
    val user_id: Int,
    val name: String,
    val group_id: Int,
    val group_name: String
)