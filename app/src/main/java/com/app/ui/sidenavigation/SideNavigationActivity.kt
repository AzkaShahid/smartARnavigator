package com.app.ui.sidenavigation

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.ar.LocationModel
import com.app.models.MenuItemModel
import com.app.listeners.MenuListener
import com.app.ui.adapters.MenuItemAdapter
import com.app.ui.main.MainViewModel
import com.app.databinding.ActivitySideNavigationBinding
import com.app.bases.BaseActivity
import com.app.models.countries.CountryItemModel
import com.app.ui.sidenavigation.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SideNavigationActivity : BaseActivity<ActivitySideNavigationBinding, MainViewModel>(),
    MenuListener,
    NavigationBarView.OnItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: MenuItemAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override val mViewModel: MainViewModel by viewModels()

    override fun initBinding() = ActivitySideNavigationBinding.inflate(layoutInflater)

    override fun initViews(savedInstanceState: Bundle?) {

        navController = findNavController(R.id.nav_host_fragment_content_side_navigation)
        bottomNavigationView = mViewBinding.appBarSideNavigation.bottomNav
        drawerLayout = mViewBinding.drawerLayout
        adapter = MenuItemAdapter(this, this)
        recycler = mViewBinding.drawerLayout.findViewById(R.id.recyclerView)
        recycler.adapter = adapter
    }

    override fun addViewModelObservers() {

    }

    override fun attachListens() {

        bottomNavigationView.setOnItemSelectedListener(this)

    }


    override fun onMenuItemClicked(menuItem: MenuItemModel) {
        openCloseDrawer()
        drawerLayout.close()
        updateNavigation(menuItem.menuID)
    }


    override fun getMenuItems(): ArrayList<MenuItemModel> {
        val list = ArrayList<MenuItemModel>()
        list.add(MenuItemModel(MenuItemModel.MenuID.HOME, R.drawable.ic_home_black_24dp, getString(R.string.menu_home)))
        list.add(MenuItemModel(MenuItemModel.MenuID.INDOOR,R.drawable.ic_indoor,getString(R.string.menu_indoor_locations)))
        list.add(MenuItemModel(MenuItemModel.MenuID.USER_PROFILE, R.drawable.ic_add_location, getString(R.string.menu_add_location)))
        list.add(MenuItemModel(MenuItemModel.MenuID.COUNTRYDISTANCE, R.drawable.ic_distance, getString(R.string.menu_country_distance)))
        list.add(MenuItemModel(MenuItemModel.MenuID.SETTINGS, R.drawable.ic_settings, getString(R.string.menu_settings)))
        list.add(MenuItemModel(MenuItemModel.MenuID.ABOUT, R.drawable.ic_about, getString(R.string.menu_about)))


        return list
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                updateNavigation(MenuItemModel.MenuID.HOME)
            }
            R.id.navigation_settings -> {
                updateNavigation(MenuItemModel.MenuID.SETTINGS)
            }
            R.id.navigation_indoor ->{
                updateNavigation(MenuItemModel.MenuID.INDOOR)
            }
            R.id.navigation_country_distance ->{
                updateNavigation(MenuItemModel.MenuID.COUNTRYDISTANCE)
            }
            R.id.navigation_about ->{
                updateNavigation(MenuItemModel.MenuID.ABOUT)
            }

            else -> {
                updateNavigation(MenuItemModel.MenuID.USER_PROFILE)
            }

        }
        return true
    }


    // add fragment navigation
    private fun updateNavigation(menuID: MenuItemModel.MenuID, stopBackStack: Boolean = false) {
        if (stopBackStack) navController.popBackStack()
        when (menuID) {
            MenuItemModel.MenuID.HOME -> {
                navController.navigate(R.id.navigation_home)
            }
            MenuItemModel.MenuID.SETTINGS -> {
                navController.navigate(R.id.navigation_settings)
            }
            MenuItemModel.MenuID.INDOOR-> {
                navController.navigate(R.id.navigation_indoor)
            }
            MenuItemModel.MenuID.COUNTRYDISTANCE-> {
                navController.navigate(R.id.navigation_country_distance)
            }
            MenuItemModel.MenuID.ABOUT-> {
                navController.navigate(R.id.navigation_about)
            }

            else -> {
                navController.navigate(R.id.navigation_profile)
            }
        }
    }

    open fun openCloseDrawer() {
        if (drawerLayout.isOpen) {
            drawerLayout.close()
        } else {
            drawerLayout.open()
        }
    }
    fun onCountryClick(model:LocationModel){
        //model.iconID = R.drawable.ic_ar_location_default
        navController.popBackStack()
        val bundle = Bundle().apply {
            putParcelable("COUNTRY_RESULT",model)
        }
        navController.navigate(R.id.navigation_home , bundle)

    }

    fun onLocationClick(model: LocationModel) {
        model.iconID = R.drawable.ic_ar_location_default
        navController.popBackStack()
        val bundle = Bundle().apply {
            putParcelable("SEARCH_RESULT",model)
        }
        navController.navigate(R.id.navigation_home , bundle)


    }

}