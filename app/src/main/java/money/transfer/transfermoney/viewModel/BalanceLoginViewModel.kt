package money.transfer.transfermoney.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import money.transfer.transfermoney.encryption.DataEncryption
import money.transfer.transfermoney.network.NetworkAPI
import money.transfer.transfermoney.network.NetworkRequest
import money.transfer.transfermoney.BuildConfig
import money.transfer.transfermoney.model.APIErrorException
import money.transfer.transfermoney.model.AccessToken
import money.transfer.transfermoney.model.Balance
import money.transfer.transfermoney.model.BalanceStatus
import money.transfer.transfermoney.utils.AppStorage
import org.json.JSONObject

class BalanceLoginViewModel: ViewModel(){
    var networkRequest: NetworkRequest = NetworkRequest()
    var dataEncryption: DataEncryption = DataEncryption()

    var token: MutableLiveData<AccessToken> = MutableLiveData()
    var errorException: MutableLiveData<APIErrorException> = MutableLiveData()

    internal fun login(login: String, password: String) {
        networkRequest.request(NetworkAPI.LOGIN_API, NetworkRequest.Method.POST, null, NetworkAPI.getLoginPostBody(dataEncryption.encryptData(login, BuildConfig.dataPublic), dataEncryption.encryptData(password, BuildConfig.dataPublic)).toString(),
                object : NetworkRequest.RequestListener {
                    override fun onSuccess(result: String?) {
                        try{
                            val data = JSONObject(result)

                            if(data.has("token")){
                                val tokenData =  data.getString("token") //dataEncryption.decryptData(balanceObj.getString("balance"),  BuildConfig.dataPrivate)
                                AppStorage.setToken(dataEncryption.encryptData(tokenData, BuildConfig.dataPublic))
                                token.postValue(AccessToken(tokenData))
                            } else{
                                errorException.postValue(APIErrorException(null, result))
                            }
                        } catch (err: Exception){
                            errorException.postValue(APIErrorException(null, result))
                        }
                    }
                    override fun onError(err: Exception, response: String?) {
                        errorException.postValue(APIErrorException(err, response))
                    }
                }
        )
    }
}