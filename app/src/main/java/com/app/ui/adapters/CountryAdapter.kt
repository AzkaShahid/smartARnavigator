package com.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.ar.LocationModel
import com.app.models.countries.CountriesResponseModel
import com.app.models.countries.CountryItemModel
import com.bumptech.glide.Glide

class CountryAdapter(
    private val context: Context,
    private var countries: List<CountryItemModel>,
    private val onClickEvent: (model: CountryItemModel) -> Unit

) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.textViewName.text = country.name?.official
        holder.textViewCapital.text = country.capital!![0]!!

        Glide.with(context)
            .load(country.flags?.png)
            .into(holder.imageViewFlag)

        holder.itemView.setOnClickListener {
            onClickEvent.invoke(country)
        }

    }

    fun updateAdapter(newCountries: List<CountryItemModel>) {
        countries = newCountries
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.country_Name)
        val textViewCapital: TextView = itemView.findViewById(R.id.country_Capital)
        val imageViewFlag: ImageView = itemView.findViewById(R.id.country_Flag)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
            }
        }
    }
}