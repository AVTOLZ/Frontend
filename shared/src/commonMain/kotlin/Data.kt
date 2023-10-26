import com.russhwolf.settings.Settings

object Data {
    private val settings = Settings()

    var bearerToken
        get() = settings.getStringOrNull("bearerToken")
        set(value) = settings.putString("bearerToken", value ?: "")

    var personId
        get() = settings.getIntOrNull("personId") ?: throw Exception("No person id")
        set(value) = settings.putInt("personId", value)



    fun clearData() {
        settings.clear()
    }

}