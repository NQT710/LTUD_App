package com.ltud.ltud_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ltud.ltud_app.R
import com.ltud.ltud_app.model.Articles

class ArticlesAdapter(
    private val listArticles : ArrayList<Articles>,
    private val context : Context
)
    : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val imgArticles : ImageView = itemView.findViewById(R.id.img_articles)
        val titleArticles : TextView = itemView.findViewById(R.id.title_articles)
        val avtAuthorArticles : ImageView = itemView.findViewById(R.id.avt_detail)
        val nameAuthorArticles : TextView = itemView.findViewById(R.id.name_detail)
        val dayArticles : TextView = itemView.findViewById(R.id.day_detail)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.articles_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return listArticles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(listArticles[position].image_articles)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.background_page).into(holder.imgArticles)

        holder.titleArticles.text = listArticles[position].title

        Glide.with(context).load(listArticles[position].avatar_author)
            .centerCrop()
            .error(R.drawable.avatar_default).into(holder.avtAuthorArticles)

        holder.nameAuthorArticles.text = listArticles[position].name
        holder.dayArticles.text = listArticles[position].day
    }

}