#ifdef _WIN32
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#endif

#include "java_headers/com_github_wouterdek_jrenderdoc_RenderDoc_Natives.h"
#include "renderdoc_app.h"
#include <memory>

#define CAPTURE_CLASS "com/github/wouterdek/jrenderdoc/Capture"
typedef RENDERDOC_API_1_1_2 RENDERDOC_API;

jint throwRuntimeException(JNIEnv *env, const char *message)
{
	const jclass exClass = env->FindClass("java/lang/RuntimeException");
	return env->ThrowNew(exClass, message);
}

JNIEXPORT jlong JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getApi(JNIEnv* env, jobject obj, jstring jPathToLib)
{
	//Check if renderdoc.dll was already loaded, or we can load from the current directory
	HMODULE module = LoadLibrary(L"renderdoc.dll");
	if(module == nullptr && jPathToLib != nullptr)
	{
		//Load dll from pathToLib
		const jchar* pathToLib = env->GetStringChars(jPathToLib, nullptr);
		module = LoadLibrary((TCHAR*)pathToLib);
		env->ReleaseStringChars(jPathToLib, pathToLib);
	}

	if (module == nullptr)
	{
		throwRuntimeException(env, "Failed to load renderdoc library");
		return 0;
	}

	pRENDERDOC_GetAPI RENDERDOC_GetAPI = (pRENDERDOC_GetAPI)GetProcAddress(module, "RENDERDOC_GetAPI");
	if (RENDERDOC_GetAPI == nullptr)
	{
		throwRuntimeException(env, "Failed to find RENDERDOC_GetAPI");
		return 0;
	}

	RENDERDOC_API* rdoc_api = nullptr;
	int ret = RENDERDOC_GetAPI(eRENDERDOC_API_Version_1_1_2, (void **)&rdoc_api);
	if (ret != 1)
	{
		throwRuntimeException(env, "API initialization failed");
		return 0;
	}

	return (jlong)rdoc_api;
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getAPIVersion(JNIEnv* env, jobject obj, jlong apiPtr, jintArray jBufferArray)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	jsize len = env->GetArrayLength(jBufferArray);
	if(len < 3)
	{
		throwRuntimeException(env, "Buffer is too small");
	}

	jint* jBuffer = env->GetIntArrayElements(jBufferArray, nullptr);

	int major, minor, patch;
	api->GetAPIVersion(&major, &minor, &patch);
	jBuffer[0] = major;
	jBuffer[1] = minor;
	jBuffer[2] = patch;

	env->ReleaseIntArrayElements(jBufferArray, jBuffer, 0);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1setCaptureOptionU32(JNIEnv* env, jobject obj, jlong apiPtr, jint option, jint value)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->SetCaptureOptionU32((RENDERDOC_CaptureOption)option, value);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1setCaptureOptionF32(JNIEnv* env, jobject obj, jlong apiPtr, jint option, jfloat value)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->SetCaptureOptionF32((RENDERDOC_CaptureOption)option, value);
}

JNIEXPORT jint JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getCaptureOptionU32(JNIEnv* env, jobject obj, jlong apiPtr, jint option)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	return api->GetCaptureOptionU32((RENDERDOC_CaptureOption)option);
}

JNIEXPORT jfloat JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getCaptureOptionF32(JNIEnv* env, jobject obj, jlong apiPtr, jint option)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	return api->GetCaptureOptionF32((RENDERDOC_CaptureOption)option);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1setFocusToggleKeys(JNIEnv* env, jobject obj, jlong apiPtr, jintArray jKeyArray)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	jsize len = env->GetArrayLength(jKeyArray);
	jint* jKeys = env->GetIntArrayElements(jKeyArray, nullptr);
	auto keys = std::make_unique<RENDERDOC_InputButton[]>(len);
	for (auto i = 0; i < len; i++)
	{
		keys[i] = (RENDERDOC_InputButton)jKeys[i];
	}
	api->SetFocusToggleKeys(keys.get(), len);
	env->ReleaseIntArrayElements(jKeyArray, jKeys, JNI_ABORT);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1setCaptureKeys(JNIEnv* env, jobject obj, jlong apiPtr, jintArray jKeyArray)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	jsize len = env->GetArrayLength(jKeyArray);
	jint* jKeys = env->GetIntArrayElements(jKeyArray, nullptr);
	auto keys = std::make_unique<RENDERDOC_InputButton[]>(len);
	for(auto i = 0; i < len; i++)
	{
		keys[i] = (RENDERDOC_InputButton)jKeys[i];
	}
	api->SetCaptureKeys(keys.get(), len);
	env->ReleaseIntArrayElements(jKeyArray, jKeys, JNI_ABORT);
}

JNIEXPORT jint JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getOverlayBits(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	return api->GetOverlayBits();
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1maskOverlayBits(JNIEnv* env, jobject obj, jlong apiPtr, jint andVal, jint orVal)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->MaskOverlayBits(andVal, orVal);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1shutdown(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->Shutdown();
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1unloadCrashHandler(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->UnloadCrashHandler();
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1setCaptureFilePathTemplate(JNIEnv* env, jobject obj, jlong apiPtr, jstring jPathTemplate)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	const char* pathTemplate = env->GetStringUTFChars(jPathTemplate, nullptr);
	api->SetCaptureFilePathTemplate(pathTemplate);
	env->ReleaseStringUTFChars(jPathTemplate, pathTemplate);
}

JNIEXPORT jstring JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getCaptureFilePathTemplate(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	const char* pathTemplate = api->GetCaptureFilePathTemplate();
	return env->NewStringUTF(pathTemplate);
}

JNIEXPORT jint JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getNumCaptures(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	return api->GetNumCaptures();
}

jclass captureClass = nullptr;
jmethodID captureClassConstructor = nullptr;

JNIEXPORT jobject JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1getCapture(JNIEnv* env, jobject obj, jlong apiPtr, jint idx)
{
	if (captureClass == nullptr)
	{
		const jclass captureClassLocalRef = env->FindClass(CAPTURE_CLASS);
		if (captureClassLocalRef == nullptr)
		{
			return nullptr;
		}
		captureClass = (jclass)env->NewGlobalRef(captureClassLocalRef);
		env->DeleteLocalRef(captureClassLocalRef);

		captureClassConstructor = env->GetMethodID(captureClass, "<init>", "(JLjava/lang/String;)V");
	}

	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	
	uint32_t pathLength;
	if(!api->GetCapture(idx, nullptr, &pathLength, nullptr))
	{
		throwRuntimeException(env, "Invalid capture index");
	}

	auto path = std::make_unique<char[]>(pathLength);
	uint64_t timestamp;
	api->GetCapture(idx, path.get(), &pathLength, &timestamp);

	jstring jPath = env->NewStringUTF(path.get());
	jobject result = env->NewObject(captureClass, captureClassConstructor, timestamp, jPath);
	env->DeleteLocalRef(jPath);

	return result;
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1triggerCapture(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->TriggerCapture();
}

JNIEXPORT jboolean JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1isTargetControlConnected(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	return api->IsTargetControlConnected();
}

JNIEXPORT jint JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1launchReplayUI(JNIEnv* env, jobject obj, jlong apiPtr, jboolean connectTargetcontrol, jstring cmdLineJString)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	const char* cmdLine = env->GetStringUTFChars(cmdLineJString, nullptr);
	jint returnValue = api->LaunchReplayUI(connectTargetcontrol, cmdLine);
	env->ReleaseStringUTFChars(cmdLineJString, cmdLine);
	return returnValue;
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1setActiveWindow(JNIEnv* env, jobject obj, jlong apiPtr, jlong device, jlong window)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->SetActiveWindow((RENDERDOC_DevicePointer)device, (RENDERDOC_WindowHandle)window);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1startFrameCapture(JNIEnv* env, jobject obj, jlong apiPtr, jlong device, jlong window)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->StartFrameCapture((RENDERDOC_DevicePointer)device, (RENDERDOC_WindowHandle)window);
}

JNIEXPORT jboolean JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1isFrameCapturing(JNIEnv* env, jobject obj, jlong apiPtr)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	return api->IsFrameCapturing();
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1endFrameCapture(JNIEnv* env, jobject obj, jlong apiPtr, jlong device, jlong window)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->EndFrameCapture((RENDERDOC_DevicePointer)device, nullptr);
}

JNIEXPORT void JNICALL Java_com_github_wouterdek_jrenderdoc_RenderDoc_00024Natives_n_1triggerMultiFrameCapture(JNIEnv* env, jobject obj, jlong apiPtr, jint numFrames)
{
	RENDERDOC_API* api = (RENDERDOC_API*)apiPtr;
	api->TriggerMultiFrameCapture(numFrames);
}