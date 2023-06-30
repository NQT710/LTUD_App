package com.ltud.ltud_app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ltud.ltud_app.R
import com.ltud.ltud_app.adapter.SpeciesAdapter
import com.ltud.ltud_app.databinding.FragmentSpeciesBinding
import com.ltud.ltud_app.model.Species
import java.lang.reflect.Array.newInstance
import java.util.*
import kotlin.collections.ArrayList

class SpeciesFragment : Fragment(), View.OnClickListener {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var binding : FragmentSpeciesBinding

    private var listSpecies: ArrayList<Species> = ArrayList()
    private lateinit var speciesAdapter: SpeciesAdapter
    private var filteredList: ArrayList<Species> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpeciesBinding.inflate(inflater)
        binding.revSpecies.layoutManager = LinearLayoutManager(requireActivity())
        loadRecyclerview()
        searchSpecies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBackSpecies.setOnClickListener(this@SpeciesFragment)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_back_species -> {
                val homeFragment = HomeFragment()
                replaceFragment(homeFragment)
            }

        }
    }
    private fun loadRecyclerview(){
        database.collection("species").get()
            .addOnSuccessListener {
                for (document in it){
                    val species = Species()
                    species.image_species = document.getString("image_species") ?: ""
                    species.name = document.getString("name") ?: ""
                    species.kingdom = document.getString("kingdom") ?: ""
                    species.family = document.getString("family") ?: ""
                    species.description = document.getString("description") ?: ""
                    //   species.likeList = (document.getString("listLike") ?: "") as ArrayList<String>

                    listSpecies.add(species)
                }
                // Hiển thị adapter
                speciesAdapter = SpeciesAdapter(listSpecies, requireContext())
                binding.revSpecies.adapter = speciesAdapter

                itemClick(listSpecies)
            }
            .addOnFailureListener {
            }
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
                bundle.putString("activity","1")
                val detailSpeciesFragment = DetailSpeciesFragment()
                detailSpeciesFragment.arguments = bundle

                replaceFragment(detailSpeciesFragment)
            }
        })
    }

    private fun searchSpecies(){
        binding.searchViewSpecies.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterSpecies(query)
                performSearch(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filterSpecies(newText)
                return true
            }
        })
    }

    private fun filterSpecies(query: String) {
        filteredList.clear()
        if (query.isNotEmpty()) {
            val searchQuery = query.lowercase(Locale.getDefault())

            for (species in listSpecies) {
                if (species.family.lowercase(Locale.getDefault()).contains(searchQuery) ||
                    species.name.lowercase(Locale.getDefault()).contains(searchQuery)) {
                    filteredList.add(species)
                }
            }
        } else {
            filteredList.addAll(listSpecies)
        }
        speciesAdapter = SpeciesAdapter(filteredList, requireContext())
        binding.revSpecies.adapter = speciesAdapter
        itemClick(filteredList)
    }
    //Ẩn bàn phím khi nhấn Enter
    private fun performSearch(query: String) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchViewSpecies.windowToken, 0)
    }
}