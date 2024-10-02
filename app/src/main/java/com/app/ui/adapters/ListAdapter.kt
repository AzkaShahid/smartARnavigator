package com.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.app.ar.LocationModel
import com.app.databinding.HeaderItemBinding
import com.app.databinding.ListItemBinding

import com.app.extention.toGone
import com.app.extention.toVisible
class ListAdapter(
    private val context: Context,
    private var arrayList: ArrayList<LocationModel>,
    val clickUnit: (locationModel: LocationModel) -> Unit
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                HeaderItemBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_LIST_ITEM -> ListItemViewHolder(
                ListItemBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrayList[position]
        when (holder) {
            is HeaderViewHolder -> holder.bindHeader(model)
            is ListItemViewHolder -> holder.bindListItem(model)
        }

        holder.itemView.setOnClickListener { clickUnit.invoke(model) }

    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_LIST_ITEM
//        return if (position == 0 || arrayList[position].heading != arrayList[position - 1].heading) {
//            VIEW_TYPE_HEADER
//        } else {
//            VIEW_TYPE_LIST_ITEM
//        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HeaderViewHolder(private val binding: HeaderItemBinding) :
        ViewHolder(binding.root) {

        fun bindHeader(model: LocationModel) {
            binding.headingText.text = model.heading
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
            binding.headingLayout.setOnClickListener{

            }


        }
    }

    inner class ListItemViewHolder(private val binding: ListItemBinding) :
        ViewHolder(binding.root) {

        fun bindListItem(model: LocationModel) {
            binding.tvTitle.text = model.name

        }
    }


    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_LIST_ITEM = 1
    }

//    private fun toggleChildLayoutVisibility(childLayout: View) {
//        if (childLayout.visibility == View.VISIBLE) {
//            childLayout.visibility = View.GONE
//        } else {
//            childLayout.visibility = View.VISIBLE
//        }
//    }
}


