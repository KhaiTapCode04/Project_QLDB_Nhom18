package data.model

data class ApiBlockContactResponse(val isSuccess: Boolean)

data class BlockedContact(
    val contact_id: Int,
    val name: String
)

data class BlockedContactsResponse(
    val isSuccess: Boolean,
    val data: List<BlockedContact>
)
