package com.ltud.ltud_app.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ltud.ltud_app.R
import com.ltud.ltud_app.adapter.ArticlesAdapter
import com.ltud.ltud_app.databinding.FragmentArticlesBinding
import com.ltud.ltud_app.model.Articles
import java.util.*
import kotlin.collections.ArrayList

class ArticlesFragment : Fragment(), View.OnClickListener {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var binding : FragmentArticlesBinding

    private var listArticles: ArrayList<Articles> = ArrayList()
    private lateinit var articlesAdapter: ArticlesAdapter
    private var filteredList: ArrayList<Articles> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticlesBinding.inflate(inflater)
        binding.revArticles.layoutManager = LinearLayoutManager(requireActivity())
        loadRecyclerview()
        searchArticles()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBackArticles.setOnClickListener(this@ArticlesFragment)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_back_articles -> {
                val homeFragment = HomeFragment()
                replaceFragment(homeFragment)
            }

        }
    }

    private fun loadRecyclerview(){
        database.collection("articles").get()
            .addOnSuccessListener {

                //Rea data từ firestore, add vào listArticles
                for (document in it) {
                    val articles = Articles()
                    articles.image_articles = document.getString("image_articles") ?: ""
                    articles.title = document.getString("title") ?: ""
                    articles.avatar_author = document.getString("avatar_author") ?: ""
                    articles.name = document.getString("name") ?: ""
                    articles.day = document.getString("day") ?: ""
                    articles.tag_articles = document.getString("tag_articles") ?: ""
                    articles.description_articles = document.getString("description_articles") ?: ""

                    listArticles.add(articles)
                }

                // Hiển thị adapter
                articlesAdapter = ArticlesAdapter(listArticles, requireContext())
                binding.revArticles.adapter = articlesAdapter

                itemClick(listArticles)
            }
            .addOnFailureListener {
            }
    }
    private fun itemClick(inputList : ArrayList<Articles>) {
        articlesAdapter.setOnItemClickListener(object : ArticlesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("image_articles", inputList[position].image_articles)
                bundle.putString("title", inputList[position].title)
                bundle.putString("avatar_author", inputList[position].avatar_author)
                bundle.putString("name", inputList[position].name)
                bundle.putString("day", inputList[position].day)
                bundle.putString("tag_articles", inputList[position].tag_articles)
                bundle.putString("description_articles", inputList[position].description_articles)
                bundle.putString("activity","1")
                val detailArticlesFragment = DetailArticlesFragment()
                detailArticlesFragment.arguments = bundle

                replaceFragment(detailArticlesFragment)
            }
        })
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun searchArticles(){
        binding.searchViewArticles.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterArticles(query)
                performSearch(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filterArticles(newText)
                return true
            }
        })
    }
    private fun filterArticles(query: String) {
        filteredList.clear()

        if (query.isNotEmpty()) {
            val searchQuery = query.lowercase(Locale.getDefault())

            for (article in listArticles) {
                if (article.title.lowercase(Locale.getDefault()).contains(searchQuery) ||
                    article.name.lowercase(Locale.getDefault()).contains(searchQuery)
                ) {
                    filteredList.add(article)
                }
            }
        } else {
            filteredList.addAll(listArticles)
        }
        articlesAdapter = ArticlesAdapter(filteredList, requireContext())
        binding.revArticles.adapter = articlesAdapter
        itemClick(filteredList)
    }

    //Ẩn bàn phím khi nhấn Enter
    private fun performSearch(query: String) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchViewArticles.windowToken, 0)
    }
}