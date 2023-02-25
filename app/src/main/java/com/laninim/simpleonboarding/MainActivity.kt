package com.laninim.simpleonboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.laninim.simpleonboarding.model.OnBoardingConfiguration
import com.laninim.simpleonboarding.model.OnBoardingStyle
import com.laninim.simpleonboarding.model.PresetStyle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customView = findViewById<SimpleOnBoarding>(R.id.customView)
        val textview = findViewById<TextView>(R.id.textView)


        val onBoardingConfiguration = OnBoardingConfiguration(
            8,
            stepWidth = 25,
            stepHeigth = 25,
            marginStartStep =14,
            marginBottomStep = 0,
            marginTopStep = 0,
            marginEndStep = 14
        )

        customView.loadOnboardingConfiguration(onBoardingConfiguration)
        customView.setAutomaticOnBoarding(SimpleOnBoarding.SpeedOnBoarding.FAST)

        customView.initSimpleOnBoarding()

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




