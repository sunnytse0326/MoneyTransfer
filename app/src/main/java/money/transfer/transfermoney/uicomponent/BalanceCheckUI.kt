package money.transfer.transfermoney.uicomponent

import android.app.ProgressDialog
import android.graphics.Typeface
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import money.transfer.transfermoney.activity.BalanceCheckActivity
import money.transfer.transfermoney.R
import org.jetbrains.anko.*


class BalanceCheckUI : AnkoComponent<BalanceCheckActivity> {
    lateinit var homeAmountEt: EditText
    lateinit var homeTransferBtn: Button

    var dialog: ProgressDialog? = null

    override fun createView(ui: AnkoContext<BalanceCheckActivity>) =  with(ui)  {
        verticalLayout {
            padding = dip(30)
            textView {
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
                textResource = R.string.money_transfer
            }.lparams{
                width = matchParent
                height = wrapContent
                bottomMargin = dip(10)
            }
            homeAmountEt = editText {
                hintResource = R.string.type_amount
                textSize = 18f
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            homeTransferBtn = button(R.string.transfer) {
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