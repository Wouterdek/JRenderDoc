package com.github.wouterdek.jrenderdoc;

import java.io.*;

/**
 * This class provides Java bindings for the RenderDoc in-application API.
 */
public class RenderDoc {
	private static RenderDoc instance = null;

	public static boolean isAttached(){
		return Natives.get().n_isAttached();
	}

	/**
	 * Returns the API singleton instance.
	 */
	public static RenderDoc get(){
		if(instance == null){
			instance = new RenderDoc(Natives.get().n_getApi());
		}
		return instance;
	}

	private final long apiPtr;

	private RenderDoc(long apiPtr){
		this.apiPtr = apiPtr;
	}

	public int[] getAPIVersion(){
		int[] version = new int[3];
		Natives.get().n_getAPIVersion(apiPtr, version);
		return version;
	}

	public void setCaptureOptionU32(CaptureOption option, int value){
		Natives.get().n_setCaptureOptionU32(apiPtr, option.getIntValue(), value);
	}

	public void setCaptureOptionF32(CaptureOption option, float value){
		Natives.get().n_setCaptureOptionF32(apiPtr, option.getIntValue(), value);
	}

	public int getCaptureOptionU32(CaptureOption option){
		return Natives.get().n_getCaptureOptionU32(apiPtr, option.getIntValue());
	}

	public float getCaptureOptionF32(CaptureOption option){
		return Natives.get().n_getCaptureOptionF32(apiPtr, option.getIntValue());
	}

	public void setFocusToggleKeys(InputButton[] keys){
		int[] keyIds = new int[keys.length];
		for(int i = 0; i < keys.length; i++){
			keyIds[i] = keys[i].getIntValue();
		}
		Natives.get().n_setFocusToggleKeys(apiPtr, keyIds);
	}
	
	public void setCaptureKeys(InputButton[] keys){
		int[] keyIds = new int[keys.length];
		for(int i = 0; i < keys.length; i++){
			keyIds[i] = keys[i].getIntValue();
		}
		Natives.get().n_setCaptureKeys(apiPtr, keyIds);
	}

	public int getOverlayBits(){
		return Natives.get().n_getOverlayBits(apiPtr);
	}

	public void maskOverlayBits(int and, int or){
		Natives.get().n_maskOverlayBits(apiPtr, and, or);
	}

	public void shutdown(){
		Natives.get().n_shutdown(apiPtr);
	}
	
	public void unloadCrashHandler(){
		Natives.get().n_unloadCrashHandler(apiPtr);
	}

	public void setCaptureFilePathTemplate(String pathTemplate){
		Natives.get().n_setCaptureFilePathTemplate(apiPtr, pathTemplate);
	}
	
	public String getCaptureFilePathTemplate(){
		return Natives.get().n_getCaptureFilePathTemplate(apiPtr);
	}
	
	public int getNumCaptures(){
		return Natives.get().n_getNumCaptures(apiPtr);
	}
	
	public Capture getCapture(int idx){
		return Natives.get().n_getCapture(apiPtr, idx);
	}
	
	public void triggerCapture(){
		Natives.get().n_triggerCapture(apiPtr);
	}
	
	public boolean isTargetControlConnected(){
		return Natives.get().n_isTargetControlConnected(apiPtr);
	}
	
	public int launchReplayUI(boolean connectTargetControl, String cmdLine){
		return Natives.get().n_launchReplayUI(apiPtr, connectTargetControl, cmdLine);
	}
	
	public void setActiveWindow(long devicePtr, long wndHandle){
		Natives.get().n_setActiveWindow(apiPtr, devicePtr, wndHandle);
	}
	
	public void startFrameCapture(long devicePtr, long wndHandle){
		Natives.get().n_startFrameCapture(apiPtr, devicePtr, wndHandle);
	}
	
	public boolean isFrameCapturing(){
		return Natives.get().n_isFrameCapturing(apiPtr);
	}
	
	public void endFrameCapture(long devicePtr, long wndHandle){
		Natives.get().n_endFrameCapture(apiPtr, devicePtr, wndHandle);
	}
	
	public void triggerMultiFrameCapture(int numFrames){
		Natives.get().n_triggerMultiFrameCapture(apiPtr, numFrames);
	}

	/**
	 * Native bindings
	 */
	private static class Natives {
		private static Natives instance = null;

		/**
		 * Returns an interface to the native functions of the RenderDoc API.
		 * The first time this function is run, this will load the bindings DLL.
		 */
		static Natives get(){
			if(instance == null){
				try {
					initNatives();
				}catch(IOException ex) {
					throw new UncheckedIOException(ex);
				}
				instance = new Natives();
			}
			return instance;
		}

		private static void initNatives() throws IOException {
			File libFile;
			String arch = System.getProperty("os.arch");
			switch(arch){
				case "x86":
				case "x86_32":
					libFile = Utilities.extractResourceToTempFile("/renderdoc_wrapper_x32.dll", "renderdoc_wrapper_x32_", "dll");
					break;
				case "amd64":
				case "x86_64":
					libFile = Utilities.extractResourceToTempFile("/renderdoc_wrapper_x64.dll", "renderdoc_wrapper_x64_", "dll");
					break;
				default:
					throw new RuntimeException("Unsupported architecture '"+arch+"'");
			}

			System.load(libFile.getAbsolutePath());
		}

		private Natives(){ }

		private native boolean n_isAttached();

		//int RENDERDOC_GetAPI(RENDERDOC_Version version, void **outAPIPointers)
		private native long n_getApi() throws RuntimeException;

		//void GetAPIVersion(int *major, int *minor, int *patch)
		private native void n_getAPIVersion(long apiPtr, int[] buffer);

		//void SetCaptureOptionU32(RENDERDOC_CaptureOption opt, uint32_t val)
		private native void n_setCaptureOptionU32(long apiPtr, int option, int value);

		//void SetCaptureOptionF32(RENDERDOC_CaptureOption opt, float val)
		private native void n_setCaptureOptionF32(long apiPtr, int option, float value);

		//uint32_t GetCaptureOptionU32(RENDERDOC_CaptureOption opt)
		private native int n_getCaptureOptionU32(long apiPtr, int option);

		//float GetCaptureOptionF32(RENDERDOC_CaptureOption opt)
		private native float n_getCaptureOptionF32(long apiPtr, int option);

		//void SetFocusToggleKeys(RENDERDOC_InputButton *keys, int num)
		private native void n_setFocusToggleKeys(long apiPtr, int[] keys);

		//void SetCaptureKeys(RENDERDOC_InputButton *keys, int num)
		private native void n_setCaptureKeys(long apiPtr, int[] keys);

		//uint32_t GetOverlayBits()
		private native int n_getOverlayBits(long apiPtr);

		//void MaskOverlayBits(uint32_t And, uint32_t Or)
		private native void n_maskOverlayBits(long apiPtr, int and, int or);

		//void Shutdown()
		private native void n_shutdown(long apiPtr);
		
		//void UnloadCrashHandler()
		private native void n_unloadCrashHandler(long apiPtr);
		
		//void SetCaptureFilePathTemplate(const char *pathtemplate)
		private native void n_setCaptureFilePathTemplate(long apiPtr, String pathTemplate);
		
		//const char *GetCaptureFilePathTemplate()
		private native String n_getCaptureFilePathTemplate(long apiPtr);
		
		//uint32_t GetNumCaptures()
		private native int n_getNumCaptures(long apiPtr);
		
		//uint32_t GetCapture(uint32_t idx, char *filename, uint32_t *pathlength, uint64_t *timestamp)
		private native Capture n_getCapture(long apiPtr, int idx);
		
		//void TriggerCapture()
		private native void n_triggerCapture(long apiPtr);
		
		//uint32_t IsTargetControlConnected()
		private native boolean n_isTargetControlConnected(long apiPtr);
		
		//uint32_t LaunchReplayUI(uint32_t connectTargetControl, const char *cmdline)
		private native int n_launchReplayUI(long apiPtr, boolean connectTargetControl, String cmdLine);
		
		//void SetActiveWindow(RENDERDOC_DevicePointer device, RENDERDOC_WindowHandle wndHandle)
		private native void n_setActiveWindow(long apiPtr, long devicePtr, long wndHandle);
		
		//void StartFrameCapture(RENDERDOC_DevicePointer device, RENDERDOC_WindowHandle wndHandle)
		private native void n_startFrameCapture(long apiPtr, long devicePtr, long wndHandle);
		
		//uint32_t IsFrameCapturing()
		private native boolean n_isFrameCapturing(long apiPtr);
		
		//void EndFrameCapture(RENDERDOC_DevicePointer device, RENDERDOC_WindowHandle wndHandle)
		private native void n_endFrameCapture(long apiPtr, long devicePtr, long wndHandle);
		
		//void TriggerMultiFrameCapture(uint32_t numFrames)
		private native void n_triggerMultiFrameCapture(long apiPtr, int numFrames);
	}
}
