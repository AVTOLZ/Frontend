import api.person.info.AVTRanks
import com.russhwolf.settings.Settings

object Data {
    private val settings = Settings()

    var bearerToken
        get() = settings.getStringOrNull("bearerToken")
        set(value) = settings.putString("bearerToken", value ?: "")

    var personId
        get() = settings.getIntOrNull("personId") ?: throw Exception("No person id")
        set(value) = settings.putInt("personId", value)

    var verified
        get() = settings.getBooleanOrNull("verified") ?: false
        set(value) = settings.putBoolean("verified", value)

    var username
        get() = settings.getStringOrNull("username")
        set(value) = settings.putString("username", value ?: "")

    var userFirstname
        get() = settings.getStringOrNull("userFirstname")
        set(value) = settings.putString("userFirstname", value ?: "")

    var userLastname
        get() = settings.getStringOrNull("userLastname")
        set(value) = settings.putString("userLastname", value ?: "")

    var userRank
        get() = settings.getIntOrNull("userRank") ?: 0
        set(value) = settings.putInt("userRank", value)

    var userRankString
        get() = settings.getStringOrNull("userRankString")
        set(value) = settings.putString("userRankString", value ?: AVTRanks.Brugger.name)

    var onboardingCompleted
        get() = settings.getBoolean("onboardingCompleted", false)
        set(value) = settings.putBoolean("onboardingCompleted", value)

    fun clearData() {
        settings.clear()
    }

}