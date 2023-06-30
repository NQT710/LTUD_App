package com.ltud.ltud_app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ltud.ltud_app.fragment.ArticlesProfileFragment
import com.ltud.ltud_app.fragment.SpeciesProfileFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager,lifecycle) {
    //ctrl + i
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                SpeciesProfileFragment()
            }
            else->{
                ArticlesProfileFragment()
            }

        }
    }
}