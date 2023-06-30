package com.ltud.ltud_app.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.FragmentDetailArticlesBinding

class DetailArticlesFragment : Fragment(), View.OnClickListener {

    private lateinit var id : String
    private lateinit var binding : FragmentDetailArticlesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailArticlesBinding.inflate(inflater)
        loadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener(this@DetailArticlesFragment)
    }

    private fun loadData(){
        val bundle = arguments

        if (bundle != null){
            val imageArticles = bundle.getString("image_articles")
            val title = bundle.getString("title")
            val avatarAuthor = bundle.getString("avatar_author")
            val name = bundle.getString("name")
            val day = bundle.getString("day")
            val tagArticles = bundle.getString("tag_articles")
            val descriptionArticles = bundle.getString("description_articles")
            id = bundle.getString("activity").toString()
            Glide.with(this).load(imageArticles).centerCrop().into(binding.imgArticles)
            binding.titleDetail.text = title
            Glide.with(this).load(avatarAuthor).centerCrop().into(binding.avtDetail)
            binding.nameDetail.text = name
            binding.dayDetail.text = day
            binding.description.text = descriptionArticles

            setDataTagArticles(tagArticles)
        }
    }

    private fun setDataTagArticles(tagArticles: String?) {
        val tagArticlesArray = tagArticles?.split(", ")

        if (tagArticles?.isNotEmpty() == true) {
            if (tagArticlesArray != null) {
                for (tag in tagArticlesArray) {
                    val textViewTag = TextView(requireContext())
                    textViewTag.setBackgroundColor(Color.parseColor("#1A2F91EB"))
                    textViewTag.setTextColor(Color.parseColor("#2F91EB"))
                    textViewTag.text = tag
                    textViewTag.textSize = 14f
                    textViewTag.setTypeface(null, Typeface.BOLD)
                    textViewTag.gravity = 20.dpToPx(requireContext())
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        25.dpToPx(requireContext())
                    )
                    layoutParams.setMargins(0, 0, 20.dpToPx(requireContext()), 0)
                    textViewTag.layoutParams = layoutParams

                    binding.layoutTag.addView(textViewTag)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_back -> {
                if (id == "1") {
                    val articlesFragment = ArticlesFragment()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frame_layout, articlesFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
    fun Int.dpToPx(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}