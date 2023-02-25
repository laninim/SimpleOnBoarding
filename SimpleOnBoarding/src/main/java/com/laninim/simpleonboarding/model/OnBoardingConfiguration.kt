package com.laninim.simpleonboarding.model

import com.laninim.simpleonboarding.R

data class OnBoardingConfiguration(
    val step : Int = 3,
    var onboardingStyle : OnBoardingStyle = OnBoardingStyle(R.drawable.unchecked_black,R.drawable.checked_black,R.drawable.arrowbackblack,R.drawable.arrownextblack),
    val stepWidth : Int,
    val stepHeigth : Int,
    val marginStartStep : Int,
    val marginEndStep : Int,
    val marginBottomStep : Int,
    val marginTopStep : Int
)
