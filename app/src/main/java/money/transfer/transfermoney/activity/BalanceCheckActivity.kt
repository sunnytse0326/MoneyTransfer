package money.transfer.transfermoney.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import money.transfer.transfermoney.BuildConfig
import money.transfer.transfermoney.R
import money.transfer.transfermoney.encryption.DataEncryption
import money.transfer.transfermoney.uicomponent.BalanceCheckUI
import money.transfer.transfermoney.utils.AppStorage
import money.transfer.transfermoney.viewModel.BalanceCheckViewModel
import money.transfer.transfermoney.viewModel.BalanceLoginViewModel
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.setContentView


class BalanceCheckActivity : AppCompatActivity() {
    lateinit var mainUI: BalanceCheckUI
    lateinit var viewModel: BalanceCheckViewModel
    var dataEncryption: DataEncryption = DataEncryption()

    val BALANCESTATUS = "BALANCESTATUS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initData(){
        viewModel = ViewModelProviders.of(this).get(BalanceCheckViewModel::class.java)

        viewModel.balanceStatus.observe(this, Observer { balanceStatus ->
            mainUI.dialog?.dismiss()
            var intent = Intent(this@BalanceCheckActivity, BalanceResultActivity::class.java)
            intent.putExtra(BALANCESTATUS, balanceStatus)
            startActivity(intent)
        })
        viewModel.errorException.observe(this, Observer { error ->
            mainUI.dialog?.dismiss()
            Toast.makeText(this@BalanceCheckActivity, error?.errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initView() {
        mainUI = BalanceCheckUI()
        mainUI.setContentView(this)

        mainUI.homeAmountEt.setOnEditorActionListener { v, actionId, event ->
            if(v.text.isEmpty()){
                Toast.makeText(this@BalanceCheckActivity, this@BalanceCheckActivity.getString(R.string.input_balance), Toast.LENGTH_SHORT).show()
            } else{
                transferMoneyAction()
            }
            false
        }
        mainUI.homeTransferBtn.setOnClickListener {
            if(mainUI.homeAmountEt.text.isEmpty()){
                Toast.makeText(this@BalanceCheckActivity, this@BalanceCheckActivity.getString(R.string.input_balance), Toast.LENGTH_SHORT).show()
            } else{
                transferMoneyAction()
            }
        }
    }

    private fun transferMoneyAction(){
        mainUI.dialog = indeterminateProgressDialog(this.resources.getString(R.string.loading))

        viewModel.moneyTransfer(mainUI.homeAmountEt.text.toString(),dataEncryption.decryptData(AppStorage.getToken()?:"", BuildConfig.dataPrivate))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                AppStorage.setToken(null)
                startActivity(Intent(this@BalanceCheckActivity, BalanceLoginActivity::class.java))
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
