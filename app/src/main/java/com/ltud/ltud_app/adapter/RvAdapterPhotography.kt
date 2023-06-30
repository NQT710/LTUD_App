package com.ltud.ltud_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ltud.ltud_app.R
import com.ltud.ltud_app.model.OutDataPhotography
import kotlinx.android.synthetic.main.layout_item_photography.view.txtTagProtocol
import kotlinx.android.synthetic.main.layout_item_plant_typle.view.*

class RvAdapterPhotography (
    var ds: List<OutDataPhotography>
):RecyclerView.Adapter<RvAdapterPhotography.PhimViewHolder>(){
    //make class viewHolder
    inner class PhimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //ctrl + i
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhimViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_photography,parent,false)
        return PhimViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhimViewHolder, position: Int) {
        holder.itemView.apply {
            txtTagProtocol.text = ds[position].tag
            imgPlantTyples.setBackgroundResource(ds[position].image)
        }
    }
    override fun getItemCount(): Int {
        return ds.size
    }
}
