package money.transfer.transfermoney.model

import java.io.Serializable

class BalanceStatus(val success: Boolean, val balance: Balance) : Serializable