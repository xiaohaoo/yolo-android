// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("yolo");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("yolo")
//      }
//    }
#include <android/log.h>
#include "main.h"


extern "C"
JNIEXPORT void JNICALL
Java_com_xiaohaoo_yolo_ui_activity_MainActivity_00024Companion_initialize(JNIEnv *env, jobject thiz, jobject context) {
    auto context_class = env->GetObjectClass(context);
    auto method_id = env->GetMethodID(context_class, "getAssets", "()Landroid/content/res/AssetManager;");
    auto *aAssetManager = AAssetManager_fromJava(env, env->CallObjectMethod(context, method_id));
    AAsset *asset = AAssetManager_open(aAssetManager, "yolov8n_float32.tflite", AASSET_MODE_BUFFER);
    auto model_size = AAsset_getLength(asset);

    __android_log_print(ANDROID_LOG_INFO, TAG, "model size: %ld", model_size);

    char *model_data = (char *) malloc(model_size);
    AAsset_read(asset, model_data, model_size);
    AAsset_close(asset);
    auto *tf_lite_model = TfLiteModelCreate(model_data, model_size);
    auto options = TfLiteInterpreterOptionsCreate();
    auto delegate = TfLiteGpuDelegateV2Create(nullptr);
    TfLiteInterpreterOptionsAddDelegate(options, delegate);
    auto interpreter = TfLiteInterpreterCreate(tf_lite_model, options);

    if (interpreter) {
        TfLiteInterpreterDelete(interpreter);
    }
    if (delegate) {
        TfLiteGpuDelegateV2Delete(delegate);
    }
    if (options) {
        TfLiteInterpreterOptionsDelete(options);
    }
    if (tf_lite_model) {
        TfLiteModelDelete(tf_lite_model);
    }
}