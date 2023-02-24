package com.laninim.simpleonboarding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.core.view.setMargins

const val TAG = "SimpleOnBoarding"
class SimpleOnBoarding @JvmOverloads constructor(context:Context,attrs: AttributeSet? = null,defStyle : Int = 0, defStyleRes : Int = 0) : LinearLayout(context,attrs,defStyle,defStyleRes){

     var onBoardingConfiguration : OnBoardingConfiguration? = OnBoardingConfiguration(
        step = 3,
        uncheckedDrawable = R.drawable.unchecked_black,
        checkedDrawable = R.drawable.unchecked_black,
        stepWidth = 30,
        stepHeigth = 30,
        marginStartStep = 8,
        marginTopStep = 0,
        marginEndStep = 8,
        marginBottomStep = 0
    )


    private var stepBox : LinearLayout
    private var prevButton : TextView
    private var nextButton : TextView




    private var checkedDrawable = R.drawable.unchecked_black

    private var onBoardingStepWidth = 25

    private var onBoardingStepHeigth = 25

    private lateinit var stepWidget : MutableList<TextView>

    private val params = LayoutParams(onBoardingStepWidth,onBoardingStepHeigth).apply {
        this.setMargins(
            onBoardingConfiguration!!.marginStartStep,
            onBoardingConfiguration!!.marginTopStep,
            onBoardingConfiguration!!.marginEndStep,
            onBoardingConfiguration!!.marginBottomStep
            )
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.simpleonboarding,this, true)
        orientation = HORIZONTAL

        stepBox = view.findViewById(R.id.stepbox)
        prevButton = view.findViewById(R.id.prevbutton)
        nextButton = view.findViewById(R.id.nextbutton)

        initSimpleOnBoarding()
    }

    private fun initSimpleOnBoarding(){
        loadStepWidget()
        addStepToStepBox()
    }

    private fun loadStepWidget() {
        stepWidget = mutableListOf()
        for(step in 0..onBoardingConfiguration?.step!!){
            stepWidget.add(TextView(context).apply {
                background = context.getDrawable(onBoardingConfiguration!!.uncheckedDrawable)
                width = onBoardingConfiguration!!.stepWidth
                height = onBoardingConfiguration!!.stepHeigth
                layoutParams = params
            })
        }
        Log.d(TAG,"stepNumber: ${stepWidget.size}")
    }

    private fun addStepToStepBox(){
        for(step in stepWidget){
            stepBox.addView(step)
        }
    }

}