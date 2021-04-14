package com.example.majorprojectpdd

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.majorprojectpdd.ml.PlantDiseaseModel
import com.example.majorprojectpdd.model.ImageViewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ResultFragment: Fragment() {
    private val sharedViewModel: ImageViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var plantName = view.findViewById<TextView>(R.id.plant_name_id)
        var diseaseName = view.findViewById<TextView>(R.id.disease_name_id)
        view.findViewById<Button>(R.id.take_another_button_id).setOnClickListener{
            sharedViewModel.resetData()
            findNavController().popBackStack(R.id.imageSelectionFragment2,false)
            //findNavController().navigate(R.id.action_resultFragment_to_imageSelectionFragment2)
        }

        view.findViewById<ImageView>(R.id.selected_image).setImageBitmap(sharedViewModel.image.value)

        //val model = PlantDiseaseCnn.newInstance(this.requireContext())
        val model = PlantDiseaseModel.newInstance(this.requireContext())
        val filename = "labels.txt"
        val inputString: String = requireContext().assets.open(filename).bufferedReader().use{it.readText()}
        var plantList = inputString.split("\n")
        val diseasesfile = "diseases_labels.txt"
        val diseasesString: String = requireContext().assets.open(diseasesfile).bufferedReader().use{it.readText()}
        var diseasesList = diseasesString.split("\n")

// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

        var tbuffer = sharedViewModel.image.value?.let { convertBitmapToByteBuffer(it) }

        if (tbuffer != null) {
            inputFeature0.loadBuffer(tbuffer)
        }

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var maxIndex = getMax(outputFeature0.floatArray)
        sharedViewModel.setPlantName(plantList[maxIndex])
        sharedViewModel.setDiseaseName(diseasesList[maxIndex])
        plantName.setText("Plant Name: "+sharedViewModel.plantName.value)
        diseaseName.setText("Disease Name: "+sharedViewModel.diseaseName.value)
// Releases model resources if no longer used.
        model.close()
    }


    private fun convertBitmapToByteBuffer(bp: Bitmap): ByteBuffer? {
        val imgData: ByteBuffer = ByteBuffer.allocateDirect(java.lang.Float.BYTES * 224 * 224 * 3)
        imgData.order(ByteOrder.nativeOrder())
        val bitmap = Bitmap.createScaledBitmap(bp, 224, 224, true)
        val intValues = IntArray(224 * 224)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Convert the image to floating point.
        var pixel = 0
        for (i in 0..223) {
            for (j in 0..223) {
                val `val` = intValues[pixel++]
                imgData.putFloat((`val` shr 16 and 0xFF) / 255f)
                imgData.putFloat((`val` shr 8 and 0xFF) / 255f)
                imgData.putFloat((`val` and 0xFF) / 255f)
            }
        }
        return imgData
    }

    private fun getMax(arr:FloatArray):Int{
        var ind:Int = 0
        var maxi:Float = 0f
        for(i in 0..(arr.size-1)){
            if(arr[i]>maxi){
                maxi=arr[i]
                ind=i
            }
        }
        return ind
    }
}