package com.laninim.simpleonboarding.model

data class OnBoardingConfiguration(
    val step : Int = 3,
    val uncheckedDrawable : Int,
    val checkedDrawable : Int,
    val stepWidth : Int,
    val stepHeigth : Int,
    val marginStartStep : Int,
    val marginEndStep : Int,
    val marginBottomStep : Int,
    val marginTopStep : Int
)
