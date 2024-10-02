package com.app.ui.sidenavigation.about

import com.app.bases.BaseViewModel
import com.app.respository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel
@Inject
constructor(
    private val repository: MainRepository
) : BaseViewModel() {


}