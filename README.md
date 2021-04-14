# Android-App-for-PlantDiseaseDetection
An android application for Plant leaf disease detection usign Deep learning.

This application is developed in Android Studio.
Kotlin is used as the primary language.

For deep learning model https://www.kaggle.com/akashbhakat/plant-leaf-disease-detection this is taken as model with some minor changes and a TensorFlow Lite file is created and deployed on to the application.
The dataset for training this deep learning is https://www.kaggle.com/abdallahalidev/plantvillage-dataset.
Above model uses MobileNet Architecture following transfer learning.

The basic UI of the app has two button to take a photo either using camera or from gallery.
Then the photo is resized and passed on the tflite and a result is shown.
