package com.example.majorprojectpdd

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.majorprojectpdd.model.ImageViewModel

private val CAMERA_CODE = 0
private val FILE_CODE = 1

class ImageSelectionFragment: Fragment() {
    private val sharedViewModel:ImageViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.image_selection,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.capture_button_id).setOnClickListener{
            val callCamIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCamIntent, CAMERA_CODE)
        }
        view.findViewById<Button>(R.id.file_button_id).setOnClickListener{
            val callFileIntent: Intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(callFileIntent, FILE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_CODE -> {
                if(resultCode== Activity.RESULT_OK && data!=null){
                    var temp: Bitmap = data.extras?.get("data") as Bitmap
                    temp = Bitmap.createScaledBitmap(temp,224,224,true)
                    sharedViewModel.setImage(temp)
                    findNavController().navigate(R.id.action_imageSelectionFragment2_to_captureImageFragment)
                }else{
                    Toast.makeText(activity, "Camera cancelled or not available" ,Toast.LENGTH_SHORT)
                }
            }
            FILE_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val photoUri: Uri = Uri.parse(data.data.toString())
                    var bitmap:Bitmap
                    if(Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                context?.contentResolver,
                                photoUri
                        )
                    } else {
                        val source = context?.let { ImageDecoder.createSource(it.contentResolver,photoUri) }
                        bitmap = source?.let { ImageDecoder.decodeBitmap(it).copy(Bitmap.Config.RGBA_F16, true) }!!
                    }
                    sharedViewModel.setImage(bitmap)
                    findNavController().navigate(R.id.action_imageSelectionFragment2_to_captureImageFragment)
                }
            }
        }
    }
}