package pro.burgerz.wsm.mods.extrabatteryinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ExtraBatteryInfo implements IXposedHookLoadPackage {

    private static final String MODULE = "ExtraBatteryInfo";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {     

        if (loadPackageParam.packageName.equals("com.android.settings")) {
            log(MODULE, "Start hook in: " + loadPackageParam.packageName);
            ClassLoader classLoader = loadPackageParam.classLoader;

            log(MODULE, "Hooking: com.android.settings.fuelgauge.BatterySettings.onCreate(Bundle)");
            try {
                Class.forName("com.android.settings.fuelgauge.BatterySettings", false, classLoader);
                XposedHelpers.findAndHookMethod("com.android.settings.fuelgauge.BatterySettings", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        PreferenceScreen screen = ((PreferenceFragment) param.thisObject).getPreferenceScreen();
                        Context context = ((PreferenceFragment) param.thisObject).getActivity();

                        String testing_battery_info = "Battery info";
                        try {
                        	int nameResourceID = context.getResources().getIdentifier("testing_battery_info", "string",
                            		"com.android.settings");
                        	testing_battery_info = context.getString(nameResourceID);
                        } catch (Exception e) {
                        	
                        }
                        
                        Preference infoScreen = new Preference(context);
                        infoScreen.setTitle(testing_battery_info);//"@string/testing_battery_info"
                        Intent intent = new Intent();
                        intent.setPackage("com.android.settings");
                        intent.setClassName("com.android.settings", "com.android.settings.BatteryInfo");
                        intent.setAction(Intent.ACTION_MAIN);
                        infoScreen.setIntent(intent);

                        screen.addPreference(infoScreen);

                    }
                });
                log(MODULE, "Done!");
            } catch (ClassNotFoundException ignored) {
                log(MODULE, "Class not found! Skipping...");
            } catch (NoSuchMethodError ignored) {
                log(MODULE, "Method not found! Skipping...");
            }
        }

    }

}
