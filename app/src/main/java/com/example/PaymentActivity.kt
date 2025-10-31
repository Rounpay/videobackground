package com.example

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.edgelibrary.EdgeManager
import com.example.edgelibrary.callbacks.EdgeResponseCallback
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class PaymentActivity : AppCompatActivity(),EdgeResponseCallback{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<TextView>(R.id.onClick).setOnClickListener {
            val edgeManager = EdgeManager()
            edgeManager.startPayment(this, "https://api.pluralonline.com/api/v3/checkout-bff/redirect/checkout?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",this )
        }
    }

    override fun onCancelTxn(code: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onErrorOccured(code: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onInternetNotAvailable(code: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onPressedBackButton(code: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onTransactionResponse() {
        TODO("Not yet implemented")
    }
}