package com.app.ui.sidenavigation.addlocation

import LocationAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.R
import com.app.ar.LocationModel
import com.app.bases.BaseFragment
import com.app.databinding.FragmentLocationBinding
import com.app.ui.sidenavigation.profile.ProfileViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

 class AddLocationFragment : BaseFragment<FragmentLocationBinding, ProfileViewModel>() {

    override val mViewModel: ProfileViewModel by viewModels()
    lateinit var adapter: LocationAdapter



    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocationBinding {
        return FragmentLocationBinding.inflate(inflater, container, false)
    }

    override fun getToolbarBinding() = mViewBinding.toolbar

    override fun getToolbarTitle() = R.string.menu_add_location

    override fun isMenuButton() = true


    override fun setupUI(savedInstanceState: Bundle?) {
        adapter = LocationAdapter(emptyList()){
            mainActivity.onLocationClick(it)
        }
        mViewBinding.recyclerView.adapter = adapter
        showProgressDialog()

    }


     override fun attachListener() {
        mViewBinding.buttonAdd.setOnClickListener {
//            AddLocationDialogFragment()
//                .show(childFragmentManager, "")
            val intent = Intent(requireContext(), AddLocationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun observeViewModel() {
    }


    override fun onResume() {
        super.onResume()
        updateRecyclerView()

    }


    private fun updateRecyclerView() {

        val database = FirebaseDatabase.getInstance()

        val locationRef = database.getReference("locations")

        locationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hideProgressDialog()
                val locationList = ArrayList<LocationModel>()

                for (snapshot in dataSnapshot.children) {
                    val location = snapshot.getValue(LocationModel::class.java)
                    location?.let {
                        locationList.add(it)
                    }
                }

                adapter.updateData(locationList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                hideProgressDialog()
            }
        })
    }

}