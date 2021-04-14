package com.example.majorprojectpdd.model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel: ViewModel() {
    private val _image = MutableLiveData<Bitmap>()
    val image :LiveData<Bitmap> = _image

    private val _plantname = MutableLiveData<String>()
    val plantName: LiveData<String> = _plantname

    private val _diseasename = MutableLiveData<String>()
    val diseaseName: LiveData<String> = _diseasename

    fun setImage(img: Bitmap){
        _image.value = img
    }

    fun setPlantName(name:String){
        _plantname.value=name
    }

    fun setDiseaseName(name:String){
        _diseasename.value = name
    }
    fun toFloat32(){
        for(i in 0..128){
            for(j in 0..128){

            }
        }
    }
    fun resetData(){
        var conf = Bitmap.Config.ARGB_8888
        _image.value = Bitmap.createBitmap(1,1,conf)
        _diseasename.value=""
        _plantname.value=""
    }
}