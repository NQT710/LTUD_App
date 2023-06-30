package com.ltud.ltud_app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ltud.ltud_app.R
import com.ltud.ltud_app.model.Species


class SpeciesAdapter(
    private val listSpecies : ArrayList<Species>,
    private val context : Context
): RecyclerView.Adapter<SpeciesAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val imgSpecies : ImageView = itemView.findViewById(R.id.img_species)
        val name : TextView = itemView.findViewById(R.id.tv_name_species)
        val kingdom : TextView = itemView.findViewById(R.id.tv_kingdom)
        val family : TextView = itemView.findViewById(R.id.tv_family)
        val description : TextView = itemView.findViewById(R.id.tv_description)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.species_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(listSpecies[position].image_species)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.background_page).into(holder.imgSpecies)
        Log.i("TAG_L", "image_species: = ${listSpecies[position].image_species}")

        holder.name.text = listSpecies[position].name
        holder.kingdom.text = listSpecies[position].kingdom
        holder.family.text = listSpecies[position].family
        holder.description.text = listSpecies[position].description
    }

    override fun getItemCount(): Int {
        return listSpecies.size
    }
}