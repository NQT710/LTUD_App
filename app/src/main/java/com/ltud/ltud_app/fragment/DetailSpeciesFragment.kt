package com.ltud.ltud_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.FragmentDetailSpeciesBinding


class DetailSpeciesFragment : Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentDetailSpeciesBinding
    private lateinit var id : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailSpeciesBinding.inflate(inflater)
        loadData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener(this@DetailSpeciesFragment)
    }
    private fun loadData(){
        val bundle = arguments

        if (bundle != null){
            val imgSpecies = bundle.getString("image_species")
            val name = bundle.getString("name")
            val kingdom = bundle.getString("kingdom")
            val family = bundle.getString("family")
            val description = bundle.getString("description")
            id = bundle.getString("activity").toString()
            Glide.with(this).load(imgSpecies).centerCrop().into(binding.imgSpeciesDetail)
            binding.tvNameSpeciesDetail.text = name
            binding.tvKingdomDetail.text = kingdom
            binding.tvFamilyDetail.text = family
            binding.tvDescriptionDetail.text = description
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_back -> {
                if (id == "1") {
                    val speciesFragment = SpeciesFragment()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frame_layout, speciesFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
}