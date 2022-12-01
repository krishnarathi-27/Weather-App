package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val btn:Button = findViewById(R.id.continueBtn)
        val textInput : EditText = findViewById(R.id.inputLocation)

        btn.setOnClickListener {
            if(textInput.text.isEmpty())
            {
                Toast.makeText(this,"Enter the location",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val intent = Intent(this,MainActivity::class.java)
                val message =textInput.text.toString()
                intent.putExtra("message_key", message)
                startActivity(intent)
                finish()
            }
        }
    }
}