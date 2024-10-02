import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.app.R
import com.app.ar.LocationModel
import com.app.databinding.AdapterAllLocationBinding
import com.app.models.AllLocationsModel
import com.app.ui.adapters.ListAdapter
import com.app.utils.LocationsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AllLocationAdapter(
    private var context: Context,
    private var locationList: List<AllLocationsModel>,
    private val onClickEvent: (model: LocationModel) -> Unit
) :
    RecyclerView.Adapter<AllLocationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterAllLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationList[position]

        holder.bind.tvHeader.text = location.title


        val adapter = ListAdapter(context, location.list) {
            onClickEvent.invoke(it)
        }
        holder.bind.rvList.layoutManager = LinearLayoutManager(context)
        holder.bind.rvList.adapter = adapter

        holder.bind.root.setOnClickListener {
            if (holder.bind.rvList.visibility == View.VISIBLE) {
                holder.bind.rvList.visibility = View.GONE
                holder.bind.expandBtn.setImageResource(R.drawable.ic_expand)

            } else {
                holder.bind.rvList.visibility = View.VISIBLE
                holder.bind.expandBtn.setImageResource(R.drawable.ic_expand_more)

            }
        }

    }

    override fun getItemCount(): Int {
        return locationList.size
    }


    inner class ViewHolder(var bind: AdapterAllLocationBinding) :
        RecyclerView.ViewHolder(bind.root) {

    }
}
