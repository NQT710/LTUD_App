package com.ltud.ltud_app.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ltud.ltud_app.databinding.FragmentOnboardBinding

class OnboardFragment(onboardItems: OnboardItems) : Fragment() {
    private lateinit var binding : FragmentOnboardBinding
    private val item : OnboardItems = onboardItems



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardBinding.inflate(inflater)

        binding.imgOnboard.setImageResource(item.image)
        binding.tvOnboardTitle.text = getText(item.title)
        binding.tvOnboardDescription.text = getText(item.desc)

        return binding.root
    }

}