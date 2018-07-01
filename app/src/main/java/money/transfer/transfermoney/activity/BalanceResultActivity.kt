package money.transfer.transfermoney.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import money.transfer.transfermoney.R
import money.transfer.transfermoney.model.Balance
import money.transfer.transfermoney.model.BalanceStatus
import money.transfer.transfermoney.network.NetworkAPI
import money.transfer.transfermoney.network.NetworkRequest
import money.transfer.transfermoney.uicomponent.BalanceResultUI
import org.jetbrains.anko.setContentView


class BalanceResultActivity : AppCompatActivity() {
    lateinit var mainUI: BalanceResultUI
    lateinit var balanceStatus: BalanceStatus

    val BALANCESTATUS = "BALANCESTATUS";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        mainUI = BalanceResultUI()
        mainUI.setContentView(this)
    }

    private fun initData(){
        balanceStatus = this.intent.extras.getSerializable(BALANCESTATUS) as BalanceStatus
        mainUI.homeResultTv.text = "${this.getString(R.string.success)}: ${balanceStatus.success}\n${balanceStatus.balance.balanceAmount} ${balanceStatus.balance.currency}"
    }
}
