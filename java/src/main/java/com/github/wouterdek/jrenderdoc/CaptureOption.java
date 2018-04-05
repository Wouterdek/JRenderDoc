package com.github.wouterdek.jrenderdoc;

public enum CaptureOption {
	/** 
	 * Allow the application to enable vsync
	 *
	 * Default - enabled
	 *
	 * 1 - The application can enable or disable vsync at will
	 * 0 - vsync is force disabled
	 */
	ALLOW_V_SYNC(0),

			/**
	 * Allow the application to enable fullscreen
	 *
	 * Default - enabled
	 *
	 * 1 - The application can enable or disable fullscreen at will
	 * 0 - fullscreen is force disabled
		 */
	ALLOW_FULLSCREEN(1),

	/** 
	 * Record API debugging events and messages
	 *
	 * Default - disabled
	 *
	 * 1 - Enable built-in API debugging features and records the results into
	 * the capture, which is matched up with events on replay
	 * 0 - no API debugging is forcibly enabled
	 */
	API_VALIDATION(2),

	/** 
	 * Capture CPU callstacks for API events
	 *
	 * Default - disabled
	 *
	 * 1 - Enables capturing of callstacks
	 * 0 - no callstacks are captured
	 */
	CAPTURE_CALLSTACKS(3),

	/** 
	 * When capturing CPU callstacks, only capture them from drawcalls.
	 * This option does nothing without the above option being enabled
	 *
	 * Default - disabled
	 *
	 * 1 - Only captures callstacks for drawcall type API events.
	 * Ignored if CaptureCallstacks is disabled
	 * 0 - Callstacks, if enabled, are captured for every event.
	 */
	CAPTURE_CALLSTACKS_ONLY_DRAWS(4),

	/** 
	 * Specify a delay in seconds to wait for a debugger to attach, after
	 * creating or injecting into a process, before continuing to allow it to run.
	 *
	 * 0 indicates no delay, and the process will run immediately after injection
	 *
	 * Default - 0 seconds
	 *
	 */
	DELAY_FOR_DEBUGGER(5),

	/** 
	 * Verify any writes to mapped buffers, by checking the memory after the
	 * bounds of the returned pointer to detect any modification.
	 *
	 * Default - disabled
	 *
	 * 1 - Verify any writes to mapped buffers
	 * 0 - No verification is performed, and overwriting bounds may cause
	 * crashes or corruption in RenderDoc
	 */
	VERIFY_MAP_WRITES(6),

	/** 
	 * Hooks any system API calls that create child processes, and injects
	 * RenderDoc into them recursively with the same options.
	 *
	 * Default - disabled
	 *
	 * 1 - Hooks into spawned child processes
	 * 0 - Child processes are not hooked by RenderDoc
	 */
	HOOK_INTO_CHILDREN(7),

	/** 
	 * By default RenderDoc only includes resources in the final capture necessary
	 * for that frame, this allows you to override that behaviour.
	 *
	 * Default - disabled
	 *
	 * 1 - all live resources at the time of capture are included in the capture
	 * and available for inspection
	 * 0 - only the resources referenced by the captured frame are included
	 */
	REF_ALL_RESOURCES(8),

	/** 
	 * By default RenderDoc skips saving initial states for resources where the
	 * previous contents don't appear to be used, assuming that writes before
	 * reads indicate previous contents aren't used.
	 *
	 * Default - disabled
	 *
	 * 1 - initial contents at the start of each captured frame are saved, even if
	 * they are later overwritten or cleared before being used.
	 * 0 - unless a read is detected, initial contents will not be saved and will
	 * appear as black or empty data.
	 */
	SAVE_ALL_INITIALS(9),

	/** 
	 * In APIs that allow for the recording of command lists to be replayed later,
	 * RenderDoc may choose to not capture command lists before a frame capture is
	 * triggered, to reduce overheads. This means any command lists recorded once
	 * and replayed many times will not be available and may cause a failure to
	 * capture.
	 *
	 * Note this is only true for APIs where multithreading is difficult or
	 * discouraged. Newer APIs like Vulkan and D3D12 will ignore this option
	 * and always capture all command lists since the API is heavily oriented
	 * around it and the overheads have been reduced by API design.
	 *
	 * 1 - All command lists are captured from the start of the application
	 * 0 - Command lists are only captured if their recording begins during
	 * the period when a frame capture is in progress.
	 */
	CAPTURE_ALL_CMD_LISTS(10),

	/** 
	 * Mute API debugging output when the API validation mode option is enabled
	 *
	 * Default - enabled
	 *
	 * 1 - Mute any API debug messages from being displayed or passed through
	 * 0 - API debugging is displayed as normal
	 */
	DEBUG_OUTPUT_MUTE(11);

	private final int intValue;
	CaptureOption(int intValue){
		this.intValue = intValue;
	}

	public int getIntValue(){
		return intValue;
	}
}