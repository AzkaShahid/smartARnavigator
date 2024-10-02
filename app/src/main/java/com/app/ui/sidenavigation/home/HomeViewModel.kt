package com.app.ui.sidenavigation.home


import com.app.bases.BaseViewModel
import com.app.respository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val repository: MainRepository
) : BaseViewModel() {


}
