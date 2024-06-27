package com.xoxo.xoxo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.skincare.xoxo.R
import com.skincare.xoxo.databinding.ActivityPagoBinding

class PagoActivity : ComponentActivity() {

    private lateinit var binding: ActivityPagoBinding
    private val TAG = "PagoActivity"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el total de la compra y los nombres de los productos desde el Intent
        val totalCompra = intent.getDoubleExtra("total_compra", 0.0)
        val nombresProductos = intent.getStringExtra("nombres_productos") ?: ""

        // Mostrar el total y los nombres de los productos en los TextViews correspondientes
        binding.textTotalValue.text = getString(R.string.price_format, totalCompra)
        binding.textNombresProductos.text = nombresProductos

        // Configuración del PaymentButtonContainer de PayPal
        binding.paymentButtonContainer.setup(
            createOrder = CreateOrder { createOrderActions ->
                val totalPagarStr = binding.textTotalValue.text.toString().substring(2)
                val totalPagar = totalPagarStr.toDouble()


                // Crear la orden de PayPal
                val order = OrderRequest(
                    intent = OrderIntent.CAPTURE,
                    appContext = AppContext(userAction = UserAction.PAY_NOW),
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            amount = Amount(
                                currencyCode = CurrencyCode.USD,
                                value = totalPagar.toString()
                            ),
                        )
                    )
                )
                createOrderActions.create(order)
            },
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.d(TAG, "CaptureOrderResult: $captureOrderResult")
                    Toast.makeText(this, "Pago exitoso", Toast.LENGTH_SHORT).show()
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "El comprador canceló esta compra")
                Toast.makeText(this, "Pago cancelado", Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Toast.makeText(this, "Error en el pago:", Toast.LENGTH_LONG).show()
            }
        )

        // Ejemplo de cómo aplicar un cupón
        binding.buttonApplyCupon.setOnClickListener {
            val cupon = binding.editTextCupon.text.toString().trim()
            aplicarCupon(cupon)
        }
    }

    private fun aplicarCupon(cupon: String) {
        val totalActual = 200.0 // Reemplaza por el total real
        var nuevoTotal = totalActual

        when (cupon.toLowerCase()) {
            "xoxo" -> {
                if (totalActual >= 200) {
                    val descuento = totalActual * 0.1
                    nuevoTotal = totalActual - descuento
                }
            }
            "skincare" -> {
                if (totalActual >= 300) {
                    val descuento = totalActual * 0.2
                    nuevoTotal = totalActual - descuento
                }
            }
            else -> {
                nuevoTotal = totalActual
            }
        }

        binding.textTotalValue.text = getString(R.string.price_format, nuevoTotal)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
