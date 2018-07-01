package money.transfer.transfermoney.uicomponent

import android.graphics.Typeface
import android.widget.TextView
import money.transfer.transfermoney.R
import money.transfer.transfermoney.activity.BalanceResultActivity
import org.jetbrains.anko.*


class BalanceResultUI : AnkoComponent<BalanceResultActivity> {
    lateinit var homeResultTv: TextView

    override fun createView(ui: AnkoContext<BalanceResultActivity>) =  with(ui)  {
        verticalLayout {
            padding = dip(30)
            textView {
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
                textResource = R.string.result
            }.lparams{
                width = matchParent
                height = wrapContent
            }
            homeResultTv = textView {
                textSize = 16f
            }
        }
    }

}