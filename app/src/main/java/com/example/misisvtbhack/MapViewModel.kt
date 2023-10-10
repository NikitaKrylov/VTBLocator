package com.example.misisvtbhack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


data class UiState(
    var zoom: Float
)


class MapViewModel : ViewModel() {
    val uiState = MutableLiveData<UiState>()
}