package com.app.activities

import AllLocationAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.ar.LocationModel
import com.app.bases.BaseActivity
import com.app.databinding.ActivitySearchBinding
import com.app.models.AllLocationsModel
import com.app.ui.adapters.ListAdapter
import com.app.ui.adapters.OutdoorListAdapter
import com.app.ui.main.MainViewModel
import com.app.utils.LocationsManager


class SearchActivity : BaseActivity<ActivitySearchBinding, MainViewModel>() {

    override val mViewModel: MainViewModel by viewModels()
    var locationModels = ArrayList<LocationModel>()

    //    lateinit var adapter: ListAdapter


    override fun initBinding() = ActivitySearchBinding.inflate(layoutInflater)

    override fun initViews(savedInstanceState: Bundle?) {
        val list = ArrayList<AllLocationsModel>()


        list.addAll(LocationsManager.getUniLocations(this))


        val headerAdapter = AllLocationAdapter(this, list) {
            closeActivity(it)

        }

        mViewBinding.rvLst.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mViewBinding.rvLst.adapter = headerAdapter

//        adapter1 = OutdoorListAdapter(this, LocationsManager.getOutdoorLocations(this)) {
//            closeActivity(it)
//        }
//        mViewBinding.outdoorList.adapter = adapter1

        //setupActionBar()


    }

//    private fun setupActionBar() {
//        setSupportActionBar(mViewBinding.toolbar)
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
//        }
//    }


    private fun closeActivity(model: LocationModel) {
        val resultIntent = Intent()
        resultIntent.putExtra("SEARCH_RESULT", model)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun addViewModelObservers() {
    }

    override fun attachListens() {
//        mViewBinding.indoorSearch.setOnClickListener{
//            val resultIntent = Intent()
//            resultIntent.putExtra("key", 456)
//            setResult(Activity.RESULT_OK, resultIntent)
//            finish()
//         }
//        mViewBinding.outdoorSearch.setOnClickListener {
//            val resultIntent = Intent()
//            resultIntent.putExtra("SEARCH_RESULT", 456)
//            setResult(RESULT_OK, resultIntent)
//            finish()
//
//        }
//        mViewBinding.cvSearch.setOnClickListener {
//            if (mViewBinding.rvList.visibility == View.VISIBLE) {
//                mViewBinding.rvList.visibility = View.GONE
//                mViewBinding.expandMoreBtn.visibility = View.GONE
//                mViewBinding.expandBtn.visibility = View.VISIBLE
//
//            } else {
//                mViewBinding.rvList.visibility = View.VISIBLE
//                mViewBinding.expandMoreBtn.visibility = View.VISIBLE
//                mViewBinding.expandBtn.visibility = View.GONE
//
//            }
//        }
//        mViewBinding.otSearch.setOnClickListener {
//            if (mViewBinding.outdoorList.visibility == View.VISIBLE) {
//                mViewBinding.outdoorList.visibility = View.GONE
//                mViewBinding.expandMoreBtn1.visibility = View.GONE
//                mViewBinding.expandBtn1.visibility = View.VISIBLE
//
//            } else {
//                mViewBinding.outdoorList.visibility = View.VISIBLE
//                mViewBinding.expandMoreBtn1.visibility = View.VISIBLE
//                mViewBinding.expandBtn1.visibility = View.GONE
//
//            }
//        }
//        mViewBinding.toolbar.setNavigationOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//
//        }
    }
}
