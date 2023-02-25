package com.laninim.simpleonboarding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.laninim.simpleonboarding.exception.MaxStepAllowedException
import com.laninim.simpleonboarding.model.OnBoardingConfiguration
import com.laninim.simpleonboarding.model.OnBoardingStyle
import com.laninim.simpleonboarding.model.PresetStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG = "SimpleOnBoarding"
const val DEFAULT_STEP = 3
class SimpleOnBoarding @JvmOverloads constructor(context:Context,attrs: AttributeSet? = null,defStyle : Int = 0, defStyleRes : Int = 0) : LinearLayout(context,attrs,defStyle,defStyleRes){


    private var onBoardingstyle = OnBoardingStyle(
        R.drawable.unchecked_black,
        R.drawable.checked_black,
        R.drawable.arrowbackblack,
        R.drawable.arrownextblack
    )

     var onBoardingConfiguration : OnBoardingConfiguration? = OnBoardingConfiguration(
        step = DEFAULT_STEP,
        onboardingStyle = onBoardingstyle,
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


    private var currentStep = 0

    private var changeStepListener = mutableListOf<OnChangeStepListener>()

    private lateinit var stepWidget : MutableList<TextView>

    private var presetStyle = PresetStyle.DEFAULT

    private var AUTOMATE_STEP = false

    enum class SpeedOnBoarding {
        SLOW, MEDIUM, FAST
    }

    private var automateOnBoardingSpeed = SpeedOnBoarding.SLOW



    private var params = LayoutParams(onBoardingConfiguration!!.stepWidth,
        onBoardingConfiguration!!.stepHeigth).apply {
        this.setMargins(
            onBoardingConfiguration!!.marginStartStep,
            onBoardingConfiguration!!.marginTopStep,
            onBoardingConfiguration!!.marginEndStep,
            onBoardingConfiguration!!.marginBottomStep
            )
    }

    override fun setId(id: Int) {
        super.setId(id)
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.simpleonboarding,this, true)
        orientation = HORIZONTAL

        stepBox = view.findViewById(R.id.stepbox)
        prevButton = view.findViewById(R.id.prevbutton)
        nextButton = view.findViewById(R.id.nextbutton)


    }

    fun initSimpleOnBoarding(){

        loadPresetStyle()
        loadStepWidget()
        addStepToStepBox()
        checkProgressOnBoarding()
        onBoardingController()

        if(AUTOMATE_STEP){
            val delay : Long = when(automateOnBoardingSpeed){
                SpeedOnBoarding.SLOW -> 4000
                SpeedOnBoarding.MEDIUM -> 2000
                SpeedOnBoarding.FAST -> 1000
            }

            GlobalScope.launch(Dispatchers.Main) {
                doAutomateStepWork(delay)
            }
        }

    }

    private fun loadStepWidget() {
        stepWidget = mutableListOf()
        for(step in 0..onBoardingConfiguration?.step!! - 1){
            stepWidget.add(TextView(context).apply {
                background = context.getDrawable(onBoardingConfiguration!!.onboardingStyle.uncheckedImage)
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

    private fun checkProgressOnBoarding(){
        for(step in stepWidget){
            if(stepWidget.indexOf(step) == currentStep){
                step.background = context.getDrawable(onBoardingConfiguration!!.onboardingStyle.checkedImage)
            }else{
                step.background = context.getDrawable(onBoardingConfiguration!!.onboardingStyle.uncheckedImage)
            }
        }
    }

    private fun onBoardingController(){
        prevButton.setOnClickListener{
            if(currentStep > 0){
                currentStep--
                checkProgressOnBoarding()
                if(changeStepListener.size > 0){
                    notifyListener()
                }
            }
        }
        nextButton.setOnClickListener{
            if(currentStep == onBoardingConfiguration!!.step -1){
                for(listener in changeStepListener){
                    listener.onOnBoardingComplete()
                }
            }
            if(currentStep < onBoardingConfiguration!!.step - 1){
                currentStep++
                checkProgressOnBoarding()
                if(changeStepListener.size > 0){
                    notifyListener()
                }
            }
        }
    }

    private fun notifyListener(){
        for(listener in changeStepListener){
            listener.onStepChange(currentStep)
        }
    }

    @Throws(MaxStepAllowedException::class)
    fun loadOnboardingConfiguration(configuration : OnBoardingConfiguration){
        if(configuration.step < 10){
            this.onBoardingConfiguration = configuration
            params = LayoutParams(
                configuration.stepWidth,
                configuration.stepHeigth
            ).apply {
                setMargins(configuration.marginStartStep,configuration.marginTopStep,configuration.marginEndStep,configuration.marginBottomStep)
            }
        }else{
            throw MaxStepAllowedException("The max step allowed is 9")
            return
        }
    }

    fun setOnChangeStepListener(listener : OnChangeStepListener){
        changeStepListener.add(listener)
    }

    private fun changePrevButton(drawable : Int){
        prevButton.background = context.getDrawable(drawable)
    }

    private fun changeNextButton(drawable : Int){
        nextButton.background = context.getDrawable(drawable)
    }

    private fun loadPresetStyle(){
        when(presetStyle){
            PresetStyle.DEFAULT -> {
                    onBoardingstyle = OnBoardingStyle(
                    R.drawable.unchecked_black,
                    R.drawable.checked_black,
                    R.drawable.arrowbackblack,
                    R.drawable.arrownextblack
                )
                applyStyle(onBoardingstyle)

            }
            PresetStyle.RED -> {
                    onBoardingstyle = OnBoardingStyle(
                    R.drawable.unchecked_red,
                    R.drawable.checked_red,
                    R.drawable.arrowbackred,
                    R.drawable.arrownextred
                )
                applyStyle(onBoardingstyle)
            }
            PresetStyle.PURPLE -> {
                    onBoardingstyle = OnBoardingStyle(
                    R.drawable.unchecked_purple,
                    R.drawable.checked_purple,
                    R.drawable.arrowbackpurple,
                    R.drawable.arrownextpurple
                )
                applyStyle(onBoardingstyle)
            }
        }
    }

    private fun applyStyle(style : OnBoardingStyle){
        val newOnBoardingConfiguration = OnBoardingConfiguration(
            onBoardingConfiguration!!.step,
            style,
            onBoardingConfiguration!!.stepWidth,
            onBoardingConfiguration!!.stepHeigth,
            onBoardingConfiguration!!.marginStartStep,
            onBoardingConfiguration!!.marginEndStep,
            onBoardingConfiguration!!.marginBottomStep,
            onBoardingConfiguration!!.marginTopStep
        )
        onBoardingConfiguration = newOnBoardingConfiguration
        changePrevButton(style.prevButtonIcon)
        changeNextButton(style.nextButtonIcon)
    }

    fun applyDefaultStyle(defaultStyle : PresetStyle){
        presetStyle = defaultStyle
        loadPresetStyle()
    }

    fun setAutomaticOnBoarding(speed : SpeedOnBoarding){
        AUTOMATE_STEP = true
        automateOnBoardingSpeed = speed
    }


    private suspend fun doAutomateStepWork(delay : Long){
        do{
            Log.d(TAG,"CoroutineCurrent: $currentStep")
            Log.d(TAG,"CoroutineOnboarding: ${onBoardingConfiguration!!.step - 1}")
            prevButton.visibility = View.INVISIBLE
            nextButton.visibility = View.INVISIBLE
            delay(delay)
            currentStep++
            checkProgressOnBoarding()
            if(changeStepListener.size > 0){
                notifyListener()
            }

            if(currentStep == onBoardingConfiguration!!.step){
                for(listener in changeStepListener){
                    listener.onOnBoardingComplete()
                }
                Thread.currentThread().join()
            }

        }while (currentStep < onBoardingConfiguration!!.step)

    }



}