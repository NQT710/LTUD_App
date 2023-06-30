package com.ltud.ltud_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ltud.ltud_app.R
import com.ltud.ltud_app.adapter.SpeciesAdapter
import com.ltud.ltud_app.model.OutData
import com.ltud.ltud_app.model.Species
import com.ltud.ltud_app.viewmodel.AuthViewModel

class SpeciesProfileFragment :  Fragment(), View.OnClickListener {
    private lateinit var viewModel: AuthViewModel
    private lateinit var species : ArrayList<OutData>

    private lateinit var speciesAdapter: SpeciesAdapter
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_species_profile, container, false)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        species = ArrayList()
        viewModel.loadData()
        viewModel.loadStatus.observe(viewLifecycleOwner, Observer { data ->
            data?.let { viewModel.loadSpecies(it)!! }
        })

        viewModel.speciesList.observe(viewLifecycleOwner, Observer { species  ->
            species?.let {
                val ds = ArrayList<Species>()
                for(id in species) {
                    ds.add(id)
                }
                val recyclerView: RecyclerView = view.findViewById(R.id.rvTab1)
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                speciesAdapter = SpeciesAdapter(ds, requireContext())
                recyclerView.adapter = speciesAdapter

                itemClick(ds)
            }
        })
        return view
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun itemClick(inputList : ArrayList<Species>) {
        speciesAdapter.setOnItemClickListener(object : SpeciesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("image_species", inputList[position].image_species)
                bundle.putString("name", inputList[position].name)
                bundle.putString("kingdom", inputList[position].kingdom)
                bundle.putString("family", inputList[position].family)
                bundle.putString("description", inputList[position].description)
                bundle.putString("activity","2")
                bundle.putString("like","1")
                val detailSpeciesFragment = DetailSpeciesFragment()
                detailSpeciesFragment.arguments = bundle
                replaceFragment(detailSpeciesFragment)
            }
        })
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}