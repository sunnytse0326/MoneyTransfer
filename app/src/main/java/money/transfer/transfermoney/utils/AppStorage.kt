package money.transfer.transfermoney.utils

class AppStorage{
    companion object {
        val TOKEN = "TOKEN"

        fun getToken(): String? {
            return if (Memory.getStringData(TOKEN) != null) Memory.getStringData(TOKEN) else null
        }

        fun setToken(token: String) {
            Memory.saveData(TOKEN, token)
        }
    }
}