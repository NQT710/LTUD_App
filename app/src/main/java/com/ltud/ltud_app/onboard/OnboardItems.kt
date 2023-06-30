package com.ltud.ltud_app.onboard

import com.ltud.ltud_app.R

class OnboardItems (
    val image : Int,
    val title : Int,
    val desc : Int
) {
    companion object {
        fun getData() : List<OnboardItems> {
            return listOf(
                OnboardItems(R.drawable.o1, R.string.title_intro_1, R.string.text_intro_1),
                OnboardItems(R.drawable.o2, R.string.title_intro_2, R.string.text_intro_2),
                OnboardItems(R.drawable.o3, R.string.title_intro_3, R.string.text_intro_3)
            )
        }
    }
}