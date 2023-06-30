package com.ltud.ltud_app.onboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardAdapter (
    private val list : List<OnboardItems>,
    manager: FragmentManager,
    lifecycle : Lifecycle
): FragmentStateAdapter(
    manager,
    lifecycle
) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return OnboardFragment(list[position])
    }
}