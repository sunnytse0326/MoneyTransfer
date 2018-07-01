package money.transfer.transfermoney.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import money.transfer.transfermoney.R
import money.transfer.transfermoney.uicomponent.BalanceCheckUI
import money.transfer.transfermoney.uicomponent.BalanceLoginUI
import money.transfer.transfermoney.utils.AppStorage
import money.transfer.transfermoney.viewModel.BalanceCheckViewModel
import money.transfer.transfermoney.viewModel.BalanceLoginViewModel
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.setContentView


class BalanceLoginActivity : AppCompatActivity() {
    lateinit var mainUI: BalanceLoginUI
    lateinit var viewModel: BalanceLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AppStorage.getToken()?.isNotEmpty() == true){
            startActivity(Intent(this@BalanceLoginActivity, BalanceCheckActivity::class.java))
            finish()
        } else{
            initView()
            initData()
        }
    }

    private fun initData(){
        viewModel = ViewModelProviders.of(this).get(BalanceLoginViewModel::class.java)
        viewModel.token.observe(this, Observer { balanceStatus ->
            mainUI.dialog?.dismiss()
            startActivity(Intent(this@BalanceLoginActivity, BalanceCheckActivity::class.java))
            finish()
        })
        viewModel.errorException.observe(this, Observer { error ->
            mainUI.dialog?.dismiss()
            Toast.makeText(this@BalanceLoginActivity, error?.errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initView() {
        mainUI = BalanceLoginUI()
        mainUI.setContentView(this)

        mainUI.loginPasswordEt.setOnEditorActionListener { v, actionId, event ->
            loginChecking()
            false
        }
        mainUI.loginActionBtn.setOnClickListener {
            loginChecking()
        }
    }

    private fun loginChecking(){
        if(mainUI.loginUserNameEt.text.isEmpty()){
            Toast.makeText(this@BalanceLoginActivity, this@BalanceLoginActivity.getString(R.string.input_username), Toast.LENGTH_SHORT).show()
        } else if(mainUI.loginPasswordEt.text.isEmpty()){
            Toast.makeText(this@BalanceLoginActivity, this@BalanceLoginActivity.getString(R.string.input_password), Toast.LENGTH_SHORT).show()
        } else{
            loginAction()
        }
    }

    private fun loginAction(){
        mainUI.dialog = indeterminateProgressDialog(this.resources.getString(R.string.loading))
        viewModel.login( mainUI.loginUserNameEt.text.toString(), mainUI.loginPasswordEt.text.toString())
    }
}
