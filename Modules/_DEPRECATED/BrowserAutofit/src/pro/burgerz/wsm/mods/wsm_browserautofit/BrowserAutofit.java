package pro.burgerz.wsm.mods.wsm_browserautofit;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BrowserAutofit implements IXposedHookLoadPackage {

    private static final String MODULE = "BrowserAutofit";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.android.browser")) return;

        log(MODULE, "Start hook in: " + loadPackageParam.packageName);
        ClassLoader classLoader = loadPackageParam.classLoader;

        log(MODULE, "com.android.browser.preferences.GeneralPreferencesFragment.onCreate");
        try {
            Class.forName("com.android.browser.preferences.GeneralPreferencesFragment", false, classLoader);
            XposedHelpers.findAndHookMethod("com.android.browser.preferences.GeneralPreferencesFragment", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    PreferenceScreen screen = ((PreferenceFragment) param.thisObject).getPreferenceScreen();
                    Context context = ((PreferenceFragment) param.thisObject).getActivity();

                    CheckBoxPreference makeautofit = new CheckBoxPreference(context);
                    makeautofit.setKey("autofit_pages");
                    makeautofit.setDefaultValue(true);
                    makeautofit.setTitle(context.getResources().getIdentifier("pref_content_autofit","string","com.android.browser"));
                    makeautofit.setSummary(context.getResources().getIdentifier("pref_content_autofit_summary","string","com.android.browser"));

                    screen.addPreference(makeautofit);
                }
            });
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }
    }
}
