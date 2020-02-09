package com.bornbytes.socketconnectionclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IntegerRes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connect_btn.setOnClickListener {

            val intent = Intent(this, Client::class.java)
            intent.putExtra("ip", ipadress.text.toString());
            intent.putExtra("port",editText.text.toString().toInt())
            startActivity(intent)

        }

    }
}
