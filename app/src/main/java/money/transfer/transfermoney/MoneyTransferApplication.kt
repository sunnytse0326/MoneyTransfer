package money.transfer.transfermoney

import android.app.Application
import android.content.Context

class MoneyTransferApplication:Application(){
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }
}