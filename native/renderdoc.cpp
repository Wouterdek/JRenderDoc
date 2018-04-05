#ifdef _WIN32
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#endif

#include "java_headers/com_github_wouterdek_jrenderdoc_RenderDoc_Natives.h"
#include "renderdoc_app.h"

jint throwRuntimeException(JNIEnv *env, const char *message)
{
	const jclass exClass = env->FindClass("java/lang/RuntimeException");
	return env->ThrowNew(exClass, message);
}

JNIEXPORT jboolean JNICALL Java_renderdoc_API_n_1isAttached(JNIEnv* env, jclass clazz)
{
	HMODULE module = GetModuleHandle(L"renderdoc.dll");
	return module != nullptr;
}

jlong JNICALL Java_renderdoc_API_n_1getApi(JNIEnv* env, jclass clazz)
{
	HMODULE module = LoadLibrary(L"renderdoc.dll");
	if (module == nullptr)
	{
		throwRuntimeException(env, "Failed to load renderdoc.dll");
		return 0;
	}

	pRENDERDOC_GetAPI RENDERDOC_GetAPI = (pRENDERDOC_GetAPI)GetProcAddress(module, "RENDERDOC_GetAPI");
	if (RENDERDOC_GetAPI == nullptr)
	{
		throwRuntimeException(env, "Failed to find RENDERDOC_GetAPI");
		return 0;
	}

	RENDERDOC_API_1_1_2* rdoc_api = nullptr;
	int ret = RENDERDOC_GetAPI(eRENDERDOC_API_Version_1_1_2, (void **)&rdoc_api);
	if (ret != 1)
	{
		throwRuntimeException(env, "API initialization failed");
		return 0;
	}

	return (jlong)rdoc_api;
}

void JNICALL Java_renderdoc_API_n_1startFrameCapture(JNIEnv* env, jclass clazz, jlong apiPtr, jlong device)
{
	RENDERDOC_API_1_1_2* api = (RENDERDOC_API_1_1_2*)apiPtr;
	api->StartFrameCapture((RENDERDOC_DevicePointer)device, nullptr);
}

void JNICALL Java_renderdoc_API_n_1endFrameCapture(JNIEnv* env, jclass clazz, jlong apiPtr, jlong device)
{
	RENDERDOC_API_1_1_2* api = (RENDERDOC_API_1_1_2*)apiPtr;
	api->EndFrameCapture((RENDERDOC_DevicePointer)device, nullptr);
}