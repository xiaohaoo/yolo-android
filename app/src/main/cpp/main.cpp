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
#include "main.h"

static TfLiteInterpreter *interpreter;

extern "C"
JNIEXPORT void JNICALL
Java_com_xiaohaoo_yolo_ui_activity_MainActivity_00024Companion_initialize(JNIEnv *env, jobject thiz, jobject context) {
jclass context_class = env->GetObjectClass(context);
jmethodID method_id = env->GetMethodID(context_class, "getAssets", "()Landroid/content/res/AssetManager;");
AAssetManager *aAssetManager = AAssetManager_fromJava(env, env->CallObjectMethod(context, method_id));
AAsset *asset = AAssetManager_open(aAssetManager, "yolov8n_int8.tflite", AASSET_MODE_BUFFER);
off_t model_size = AAsset_getLength(asset);
    __android_log_print(ANDROID_LOG_INFO, TAG, "model size: %ld", model_size);
    char *model_data = (char *) malloc(model_size);
    AAsset_read(asset, model_data, model_size);
    AAsset_close(asset);
TfLiteModel *tf_lite_model = TfLiteModelCreate(model_data, model_size);
TfLiteInterpreterOptions *interpreter_options = TfLiteInterpreterOptionsCreate();
TfLiteInterpreterOptionsSetNumThreads(interpreter_options,
10);
TfLiteDelegate *gpu_delegate = TfLiteGpuDelegateV2Create(nullptr);
TfLiteInterpreterOptionsAddDelegate(interpreter_options, gpu_delegate
);
interpreter = TfLiteInterpreterCreate(tf_lite_model, interpreter_options);
TfLiteModelDelete(tf_lite_model);
TfLiteInterpreterOptionsDelete(interpreter_options);
TfLiteGpuDelegateV2Delete(gpu_delegate);
free(model_data);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xiaohaoo_yolo_ui_activity_MainActivity_00024Companion_finalize(JNIEnv
*env,
jobject thiz
) {
    if (interpreter) {
        TfLiteInterpreterDelete(interpreter);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xiaohaoo_yolo_ui_activity_MainActivity_00024Companion_run(JNIEnv
*env,
jobject thiz, jobject
input,
jobject output
) {
TfLiteStatus status = TfLiteInterpreterAllocateTensors(interpreter);
TfLiteTensor *input_tensor = TfLiteInterpreterGetInputTensor(interpreter, 0);
TfLiteTensorCopyFromBuffer(input_tensor, env
->
GetDirectBufferAddress(input), env
->
GetDirectBufferCapacity(input)
);
auto start = std::chrono::high_resolution_clock::now();
TfLiteInterpreterInvoke(interpreter);
auto end = std::chrono::high_resolution_clock::now();
auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
__android_log_print(ANDROID_LOG_INFO, TAG,
"duration size: %lld", duration.

count()

);
TfLiteType tensor_type = TfLiteTensorType(input_tensor);
const TfLiteTensor *output_tensor = TfLiteInterpreterGetOutputTensor(interpreter, 0);
memcpy(env
->
GetDirectBufferAddress(output), TfLiteTensorData(output_tensor), env
->
GetDirectBufferCapacity(output)
);
}