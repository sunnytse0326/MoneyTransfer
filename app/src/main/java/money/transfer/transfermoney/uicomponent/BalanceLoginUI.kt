package money.transfer.transfermoney.uicomponent

import android.app.ProgressDialog
import android.graphics.Typeface
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import money.transfer.transfermoney.activity.BalanceCheckActivity
import money.transfer.transfermoney.R
import money.transfer.transfermoney.activity.BalanceLoginActivity
import org.jetbrains.anko.*


class BalanceLoginUI : AnkoComponent<BalanceLoginActivity> {
    lateinit var loginUserNameEt: EditText
    lateinit var loginPasswordEt: EditText
    lateinit var loginActionBtn: Button

    var dialog: ProgressDialog? = null

    override fun createView(ui: AnkoContext<BalanceLoginActivity>) =  with(ui)  {
        verticalLayout {
            padding = dip(30)
            textView {
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
                textResource = R.string.login
            }.lparams{
                width = matchParent
                height = wrapContent
                bottomMargin = dip(10)
            }
            loginUserNameEt = editText {
                hintResource = R.string.login
                textSize = 18f
            }
            loginPasswordEt = editText {
                hintResource = R.string.password
                textSize = 18f
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            loginActionBtn = button(R.string.submit) {
                textSize = 14f
                padding = dip(5)
            }.lparams{
                width = matchParent
                height = wrapContent
                topMargin = dip(5)
            }

        }
    }

}