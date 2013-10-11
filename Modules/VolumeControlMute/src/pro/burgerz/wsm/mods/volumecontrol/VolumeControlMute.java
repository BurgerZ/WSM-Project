package pro.burgerz.wsm.mods.volumecontrol;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XCallback;

public class VolumeControlMute implements IXposedHookZygoteInit  {

    private static final String MODULE = "VolumeControlMute";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    private static void volumeControlMute() {
        patchCode("miui.view.VolumePanel", "onPlaySound", 
        		int.class, int.class, replacement);
    }
    
    private static XC_MethodReplacement replacement = new XC_MethodReplacement(XCallback.PRIORITY_HIGHEST) {
		@Override
		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
			return null;
		}
	};

    private static void patchCode(String clazz, String methodName, Object... parameterTypesAndCallback) {

        log(MODULE, "Hooking: " + clazz + "." + methodName + "(?)");
        try {
        	XposedHelpers.findAndHookMethod(Class.forName(clazz), methodName, parameterTypesAndCallback);
        	log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
        	log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
        	log(MODULE, "Method not found! Skipping...");
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
		volumeControlMute();
    }

}
