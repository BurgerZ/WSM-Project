package pro.burgerz.wsm.mods.yellowpagemodule;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class YellowPageModule implements IXposedHookZygoteInit {

    private static final String MODULE = "YellowPageModule";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    private static void yellowPagesFixes() {

        patchCode("miui.provider.yellowpage.YellowPageUtils", "isYellowPageAvailable", XC_MethodReplacement.returnConstant(true));
        try {
            XposedHelpers.findAndHookMethod(Class.forName("miui.provider.yellowpage.request.HostManager"), "getYellowPageUpdateUrl", XC_MethodReplacement.returnConstant("http://localhost"));
            log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }

        try {
        	XposedHelpers.findAndHookMethod(Class.forName("miui.provider.yellowpage.request.HostManager"), "getCategoryUpdateUrl", XC_MethodReplacement.returnConstant("http://localhost"));
            log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }

        try {
        	XposedHelpers.findAndHookMethod(Class.forName("miui.provider.yellowpage.request.HostManager"), "getYellowDetailUrl", XC_MethodReplacement.returnConstant("http://localhost"));
            log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }

        try {
        	XposedHelpers.findAndHookMethod(Class.forName("miui.provider.yellowpage.request.HostManager"), "getAntispamUpdateUrl", XC_MethodReplacement.returnConstant("http://localhost"));
            log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }

        try {
        	XposedHelpers.findAndHookMethod(Class.forName("miui.provider.yellowpage.request.HostManager"), "getYellowPageBaseUrl", XC_MethodReplacement.returnConstant("http://localhost"));
            log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }

    }

    private static void patchCode(String clazz, String methodName, Object... parameterTypesAndCallback) {
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
        yellowPagesFixes();
    }

}
