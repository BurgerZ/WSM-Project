package pro.burgerz.wsm.mods.currenttracklaunch;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CurrentTrackLaunch implements IXposedHookLoadPackage {

    private static final String MODULE = "CurrentTrackLaunch";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.miui.player")) {
			log(MODULE, "Start hook in: " + loadPackageParam.packageName);
			ClassLoader classLoader = loadPackageParam.classLoader;
			try {
				Class.forName("com.miui.player.ui.MusicIconPanel", false, classLoader);
				log(MODULE, "Searching for methods...");
				XposedHelpers.findAndHookMethod("com.miui.player.ui.MusicIconPanel", classLoader,
						"onWidgetAttached", View.class, new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(MethodHookParam param) throws Throwable {
								View v = (View) param.args[0];
								final Context context = v.getContext();
								int resID = context.getResources().getIdentifier("album_foreground", 
										"id", "com.miui.player");
								v.findViewById(resID).setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Intent i = new Intent("android.intent.action.MIUI_MUSIC_PLAYER");
										Intent i = new Intent("com.miui.player.PLAYBACK_VIEWER");
										context.startActivity(i);
									}
									
								});
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
