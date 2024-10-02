package com.app.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ar.LocationModel
import com.app.databinding.AdapterSearchArLocationBinding
import com.app.extention.toGone
import com.app.extention.toVisible

class SearchAdapter(
    private val context: Context,
    private var arrayList: ArrayList<LocationModel>,
    val clickUnit: (locationModel: LocationModel) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            AdapterSearchArLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model = arrayList[position]
        val binding = holder.getBinding()
        holder.bind(context, model, position)
        holder.itemView.setOnClickListener { clickUnit.invoke(model) }
    }

    fun updateAdapter(arrayList: ArrayList<LocationModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(private val binding: AdapterSearchArLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, model: LocationModel, position: Int) {
            binding.tvTitle.text = model.name
//            AppUtils.loadImage(context, model.icon.toString(), 0, binding.imageView)
            if (model.category.isNullOrEmpty()) {
                binding.cvCategory.toGone()
            } else {
                binding.cvCategory.toVisible()

                binding.tvCategory.text = model.category
            }
            if (model.pavilionName.isNullOrEmpty()) {
                binding.cvPavilion.toGone()
            } else {
                binding.cvPavilion.toVisible()
                binding.tvPavilion.text = model.pavilionName
            }
        }

        fun getBinding(): AdapterSearchArLocationBinding {
            return binding
        }
    }

}
