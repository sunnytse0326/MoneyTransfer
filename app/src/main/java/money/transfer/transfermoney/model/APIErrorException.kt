package money.transfer.transfermoney.model

import org.json.JSONException
import org.json.JSONObject

class APIErrorException(error: Exception?, response: String?) {
    lateinit var errorMessage: String
    private var errorCode: Int? = 0
    lateinit var errorType: Type

    enum class Type {
        DataParseError, NoNetworkConnection, TokenExpired
    }

    init{
        if(error != null){
            errorMessage = error.localizedMessage
            if(error is JSONException){
                errorType = Type.DataParseError
            }
        }
        if(response != null){
            try{
                var data = JSONObject(response)
                if(data.has("code") && data.getInt("code") == 401){
                    errorCode = data.getInt("code")
                    errorType = Type.TokenExpired
                }
                if(data.has("message")){
                    errorMessage = data.getString("message")
                }
            } catch (err: JSONException){
                errorType = Type.DataParseError
            }
        }
    }
}