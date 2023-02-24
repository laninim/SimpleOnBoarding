package com.laninim.simpleonboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    override fun onStart() {
        super.onStart()
        val customView = findViewById<SimpleOnBoarding>(R.id.customView)
        val textview = findViewById<TextView>(R.id.textView)
        customView.setOnChangeStepListener(object : OnChangeStepListener{
            override fun onStepChange(step: Int) {
                textview.text = step.toString()
            }

            override fun onOnBoardingComplete() {
               Toast.makeText(applicationContext,"On boarding complete",Toast.LENGTH_LONG).show()
            }

        })
    }
}