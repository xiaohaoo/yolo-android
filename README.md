# YOLOv8 TFLite Android App

This Android application uses the YOLOv8 model implemented in TensorFlow Lite (TFLite) for real-time object detection on mobile devices.

## Branches

- **main (Kotlin):** The main branch contains the Kotlin-only version of the application. It provides a native Kotlin experience for developers and users.

- **dev (JNI C++):** The dev branch includes additional functionalities or optimizations implemented using C++ code through the Java Native Interface (JNI).
  This version may offer improved performance or access to native features.

## Features

- Camera access for real-time object detection using YOLOv8 TFLite model.
- Support for various object classes.
- Simple and intuitive user interface.

## Installation

1. Clone or download the repository to your local machine:

   ```bash
   git clone https://github.com/xiaohaoo/yolo-android.git
   ```

2. Switch to the desired branch (`main` or `dev`) using Git:

   ```bash
   git checkout main
   # or
   git checkout dev
   ```

3. Open the project in Android Studio.

4. Build and run the application on your Android device or emulator.

## Usage

1. Launch the YOLOv8 TFLite app on your Android device.

2. Grant necessary permissions for camera access when prompted.

3. Point the camera towards objects to detect.

4. Detected objects will be highlighted in real-time with bounding boxes and labels.

## Screenshots

TODO

## Model Conversion

The YOLOv8 model was converted to TensorFlow Lite format using the following steps:

1. Train the YOLOv8 model using your preferred framework (e.g., TensorFlow, PyTorch).
2. Convert the trained model to TensorFlow Lite format using the TensorFlow Lite Converter.
3. Optimize the converted model for mobile inference (quantization, model size reduction).
4. Integrate the optimized TFLite model into your Android app.

For detailed model conversion and optimization steps, refer to the official TensorFlow Lite documentation.

## Credits

- YOLOv8 Model: [https://github.com/ultralytics/ultralytics](https://github.com/ultralytics/ultralytics)
- TensorFlow Lite: [https://www.tensorflow.org/lite](https://www.tensorflow.org/lite)

## License

This project is licensed under the [MIT License](LICENSE).

