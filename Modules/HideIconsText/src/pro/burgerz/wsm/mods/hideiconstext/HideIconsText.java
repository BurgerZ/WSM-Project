package pro.burgerz.wsm.mods.hideiconstext;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HideIconsText implements IXposedHookLoadPackage {

    private static final String MODULE = "HideIconsText";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.miui.home")) {
			log(MODULE, "Start hook in: " + loadPackageParam.packageName);
			ClassLoader classLoader = loadPackageParam.classLoader;
			try {
				Class.forName("com.miui.home.launcher.ShortcutIcon", false, classLoader);
				log(MODULE, "Searching for methods...");
				XposedHelpers.findAndHookMethod("com.miui.home.launcher.ShortcutIcon", classLoader,
						"onFinishInflate", new XC_MethodHook() {
							@Override
							protected void afterHookedMethod(MethodHookParam param) throws Throwable {
								FrameLayout v = (FrameLayout) param.thisObject;
								final Context context = v.getContext();
								int resID = context.getResources().getIdentifier("icon_title", 
										"id", "com.miui.home");
								v.findViewById(resID).setVisibility(View.INVISIBLE);
							}
				});
				XposedHelpers.findAndHookMethod("com.miui.home.launcher.FolderIcon", classLoader,
						"onFinishInflate", new XC_MethodHook() {
							@Override
							protected void afterHookedMethod(MethodHookParam param) throws Throwable {
								FrameLayout v = (FrameLayout) param.thisObject;
								final Context context = v.getContext();
								int resID = context.getResources().getIdentifier("icon_title", 
										"id", "com.miui.home");
								v.findViewById(resID).setVisibility(View.INVISIBLE);
							}
				});
			} catch (ClassNotFoundException ignored) {
				log(MODULE, "Class not found! Skipping...");
			} catch (NoSuchMethodError ignored) {
				log(MODULE, "Method not found! Skipping...");
			}
		}
    }

}
