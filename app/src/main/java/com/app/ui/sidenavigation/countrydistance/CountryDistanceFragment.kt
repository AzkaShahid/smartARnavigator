package com.app.ui.sidenavigation.countrydistance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.ar.LocationModel
import com.app.bases.BaseFragment
import com.app.databinding.FragmentCountryBinding
import com.app.models.countries.CountryItemModel
import com.app.network.Resource
import com.app.ui.adapters.CountryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryDistanceFragment : BaseFragment<FragmentCountryBinding, CountryDistanceViewModel>() {

    override val mViewModel: CountryDistanceViewModel by viewModels()
    private lateinit var adapter: CountryAdapter
    private lateinit var countryRecyclerView: RecyclerView

    private var name: String = "Pakistan"
    private var latitude: Double = 29.418068
    private var longitude: Double = 71.655458
    private var capital: String = "Islamabad"
    private var flag: String = "https://flagcdn.com/w320/ad.png"


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCountryBinding {
        return FragmentCountryBinding.inflate(inflater, container, false)
    }

    override fun getToolbarBinding() = mViewBinding.toolbar

    override fun getToolbarTitle() = R.string.menu_country_distance

    override fun isMenuButton() = true

    override fun setupUI(savedInstanceState: Bundle?) {
        countryRecyclerView = mViewBinding.countryRecyclerView
        adapter = CountryAdapter(requireContext(), emptyList()) {
            omItemClick(it)

        }
        countryRecyclerView.adapter = adapter
        mViewModel.callGetCountryList()
        callCountryDistance()
    }

    private fun omItemClick(countryItemModel: CountryItemModel) {

        val model = LocationModel(
            countryItemModel.name?.common, countryItemModel.name?.official,
            countryItemModel.latlng?.get(1), countryItemModel.latlng?.get(0)!!
        )

        mainActivity.onLocationClick(model)

    }

    private fun callCountryDistance() {
        mViewModel.countryResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showProgressDialog()
                }

                is Resource.Success -> {
                    hideProgressDialog()
                    val list = resource.value
                    list.let {
                        adapter.updateAdapter(it)
                    }
                }

                is Resource.Failure -> {
                    hideProgressDialog()
                    Toast.makeText(mainActivity, resource.errorString, Toast.LENGTH_LONG).show()
                }
            }
        }

    }


    override fun attachListener() {
    }

    override fun observeViewModel() {
    }
}
