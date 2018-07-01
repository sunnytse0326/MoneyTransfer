package money.transfer.transfermoney.network

import org.json.JSONObject

class NetworkAPI{
    companion object {
        val BASE_URL = "https://sunnytse0326.github.io"

        val TRANSFER_MONEY_API = "${BASE_URL}/MockJson/balance/result.json"

        fun getBalancePostBody(balance: String):JSONObject{
            val body = JSONObject()
            body.put("balance", balance)
            return body
        }
    }
}