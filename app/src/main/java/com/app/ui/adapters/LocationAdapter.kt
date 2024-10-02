import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.ar.LocationModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LocationAdapter(
    private var locationList: List<LocationModel>,
    private val onClickEvent: (model: LocationModel) -> Unit
) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationList[position]
        holder.bind(location)

        holder.itemView.setOnClickListener {
            onClickEvent.invoke(location)
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    fun updateData(newLocationList: List<LocationModel>) {
        locationList = newLocationList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val latitudeTextView: TextView = itemView.findViewById(R.id.latitudeTextView)
        private val longitudeTextView: TextView = itemView.findViewById(R.id.longitudeTextView)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)


        fun bind(location: LocationModel) {
            nameTextView.text = location.name
            categoryTextView.text = location.category
            latitudeTextView.text = location.lati.toString()
            longitudeTextView.text = location.longi.toString()

            deleteButton.setOnClickListener {
                val ref = FirebaseDatabase.getInstance().getReference("id")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.removeValue()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException())
                    }
                })

            }
        }
    }
}
