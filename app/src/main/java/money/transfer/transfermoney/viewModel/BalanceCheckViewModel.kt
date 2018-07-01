package money.transfer.transfermoney.viewModel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.util.Log
import money.transfer.transfermoney.encryption.DataEncryption
import money.transfer.transfermoney.network.NetworkAPI
import money.transfer.transfermoney.network.NetworkRequest
import money.transfer.transfermoney.BuildConfig
import money.transfer.transfermoney.model.APIErrorException
import money.transfer.transfermoney.model.Balance
import money.transfer.transfermoney.model.BalanceStatus
import org.json.JSONObject

class BalanceCheckViewModel: ViewModel(){
    var networkRequest: NetworkRequest = NetworkRequest()
    var dataEncryption: DataEncryption = DataEncryption()

    var balanceStatus: MutableLiveData<BalanceStatus> = MutableLiveData()
    var errorException: MutableLiveData<APIErrorException> = MutableLiveData()

    internal fun moneyTransfer(amount: String, accessToken: String) {
        networkRequest.request(NetworkAPI.TRANSFER_MONEY_API, NetworkRequest.Method.POST, accessToken, NetworkAPI.getBalancePostBody(dataEncryption.encryptData(amount, BuildConfig.dataPublic) ).toString(),
                object : NetworkRequest.RequestListener {
                    override fun onSuccess(result: String?) {
                        try{
                            val data = JSONObject(result)

                            if(data.has("success") && data.getBoolean("success")){
                                val balanceObj = data.getJSONObject("data")
                                val decryptData = dataEncryption.decryptData(balanceObj.getString("balance"),  BuildConfig.dataPrivate)
                                balanceStatus.postValue(BalanceStatus(data.getBoolean("success"), Balance(decryptData, "HKD")))
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