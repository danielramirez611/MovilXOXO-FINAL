package com.xoxo.xoxo

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class App : Application() {

    var clientID = "ARl6ePgXm6ek9hY2WLyYfRu4EcBpF_A0tMIAXvUJgmoFH3cCSNdobIk-lQfawXg_Azl1tXFlIMb_PjLc"
    var returnUrl = "com.skincare.xoxo://paypalpay"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()

        val config = CheckoutConfig(
            application = this,
            clientId = clientID,
            environment = Environment.SANDBOX,
            returnUrl = returnUrl,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)
    }
}
