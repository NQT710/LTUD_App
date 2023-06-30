package com.ltud.ltud_app.fragment

import android.content.Intent
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
import com.ltud.ltud_app.adapter.ArticlesAdapter
import com.ltud.ltud_app.model.Articles
import com.ltud.ltud_app.viewmodel.AuthViewModel

class ArticlesProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: AuthViewModel
    private lateinit var articles : ArrayList<Articles>
    private lateinit var articlesAdapter: ArticlesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_articles_profile, container, false)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        articles = ArrayList()
        viewModel.loadData()
        viewModel.loadStatus.observe(viewLifecycleOwner, Observer { data ->
            data?.let { viewModel.loadArticles(it)!! }
        })

        viewModel.articlesList.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                val ds = ArrayList<Articles>()
                for(id in articles) {
                    ds.add(id)
                }
                val recyclerView: RecyclerView = view.findViewById(R.id.rvTab2)
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                articlesAdapter = ArticlesAdapter(ds, requireContext())
                recyclerView.adapter = articlesAdapter

                itemClick(ds)
            }
        })
        return view
    }

    private fun itemClick(inputList: kotlin.collections.ArrayList<Articles>){
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
                bundle.putString("activity","2")
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
    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}