package com.app.utils

import android.app.Activity
import com.app.R
import com.app.ar.LocationModel
import com.app.models.AllLocationsModel
import com.app.ui.sidenavigation.SideNavigationActivity
import java.util.ArrayList

object LocationsManager {

    fun getArLocations(mainActivity: SideNavigationActivity): ArrayList<LocationModel> {
        val list = ArrayList<LocationModel>()

        list.add(
            LocationModel(
                "Saraiki Chowk, Bahawalpur",
                29.394602,
                71.664996,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "IUB Baghdad Campus",
                29.378498, 71.7544288, R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Evento Solutions, Lahore",
                31.531636128000972,
                74.35266753425795,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Evento Solutions, Bahawalpur",
                29.40031430057256,
                71.65658748307042,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Evento Solutions, Dubai",
                25.26094555534115,
                55.33220689955402,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "SS World Family Park, Bahawalpur",
                29.3941211,
                71.6617734,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Girls High School Model Town A, Bahawalpur",
                29.3939331,
                71.6629994,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Data Sweets and Bakers Saraiki Chowk, Bahawalpur",
                29.3941358,
                71.6639717,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Cantt Police Station, Bahawalpur",
                29.3944381, 71.6629663,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Police Khidmat Markaz, Bahawalpur",
                29.3941814,
                71.6635493,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Govt. Secondary School For The Blind, Bahawalpur",
                29.3916003,
                71.6616887,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Government Technical High School, Bahawalpur",
                29.3949997,
                71.6842735,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "National Bank Of Pakistan, Bahawalpur",
                29.3955118, 71.684,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "MCB ATM, Bahawalpur",
                29.3954772, 71.6838867,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Fareed Gate, Bahawalpur",
                29.3957795, 71.6835801,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "University Chowk, Bahawalpur",
                29.3956517, 71.6900218,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "The Government Sadiq College Women University - GSCWU, Bahawalpur",
                29.3988232, 71.6937351,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Bahawal Victoria Hospital, Bahawalpur",
                29.3890635,71.6807568,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Tandoori Restaurant Desert Grill, Bahawalpur",
                29.3908752,71.6787067,
                R.drawable.ic_ar_location_default
            )
        )


        return list
    }


    fun getUniLocations(mainActivity: Activity): ArrayList<AllLocationsModel> {

        val list = ArrayList<AllLocationsModel>()

        val gcu = ArrayList<LocationModel>()
        val iub = ArrayList<LocationModel>()

        gcu.add(
            LocationModel(
                "Principle Office",
                29.399882, 71.695607,
                R.drawable.ic_ar_location_default, "GSCWU"
            )
        )
        gcu.add(
            LocationModel(
                "Library",
                29.399013, 71.696602,
                R.drawable.ic_ar_location_default, "GSCWU"
            )
        )
        gcu.add(
            LocationModel(
                "Lab",
                29.399050, 71.695505, R.drawable.ic_ar_location_default, "GSCWU"
            )
        )
        gcu.add(
            LocationModel(
                "Exit Gate",
                29.399772, 71.694995,
                R.drawable.ic_ar_location_default, "GSCWU"
            )
        )
        gcu.add(
            LocationModel(
                "Masjid",
                29.3981277, 71.695121,
                R.drawable.ic_ar_location_default, "GSCWU"
            )
        )
        gcu.add(
            LocationModel(
                "Computer Science Department",
                29.3995105, 71.6957759,
                R.drawable.ic_ar_location_default, "GSCWU"
            )
        )
        iub.add(
            LocationModel(
                "Principle Office",
                29.399882, 71.695607,
                R.drawable.ic_ar_location_default, "IUB"
            )
        )
        iub.add(
            LocationModel(
                "Library",
                29.399013, 71.696602,
                R.drawable.ic_ar_location_default, "IUB"
            )
        )
        iub.add(
            LocationModel(
                "Lab",
                29.399050, 71.695505, R.drawable.ic_ar_location_default, "IUB"
            )
        )
        iub.add(
            LocationModel(
                "Exit Gate",
                29.399772, 71.694995,
                R.drawable.ic_ar_location_default, "IUB"
            )
        )
        iub.add(
            LocationModel(
                "Masjid",
                29.3981277, 71.695121,
                R.drawable.ic_ar_location_default, "IUB"
            )
        )
        iub.add(
            LocationModel(
                "Computer Science Department",
                29.3995105, 71.6957759,
                R.drawable.ic_ar_location_default, "IUB"
            )
        )

        list.add(AllLocationsModel("GSCWU", gcu))
        list.add(AllLocationsModel("IUB", iub))


        return list
    }


    fun getOutdoorLocations(mainActivity: Activity): ArrayList<LocationModel> {
        val list = ArrayList<LocationModel>()

        list.add(
            LocationModel(
                "Saraiki Chowk, Bahawalpur",
                29.394602,
                71.664996,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Azka Home",
                29.3954877, 71.6714738,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "IUB Baghdad Campus",
                29.378498, 71.7544288, R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Evento Solutions, Lahore",
                31.531636128000972,
                74.35266753425795,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Evento Solutions, Bahawalpur",
                29.40031430057256,
                71.65658748307042,
                R.drawable.ic_ar_location_default
            )
        )
        list.add(
            LocationModel(
                "Evento Solutions, Dubai",
                25.26094555534115,
                55.33220689955402,
                R.drawable.ic_ar_location_default
            )
        )
        //list.addAll(getUniLocations(mainActivity))
        return list
    }


}
