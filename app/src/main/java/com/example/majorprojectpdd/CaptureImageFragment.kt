package com.example.majorprojectpdd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.majorprojectpdd.model.ImageViewModel

class CaptureImageFragment(): Fragment() {

    private val sharedViewModel: ImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.process_selected_image,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val temp: ImageView = view.findViewById(R.id.selected_image)
        temp.setImageBitmap(sharedViewModel.image.value)
        view.findViewById<Button>(R.id.detect_button_id).setOnClickListener{
            findNavController().navigate(R.id.action_captureImageFragment_to_resultFragment)
        }
    }
}