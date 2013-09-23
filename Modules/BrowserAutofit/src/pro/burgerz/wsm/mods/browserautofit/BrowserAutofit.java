package pro.burgerz.wsm.mods.browserautofit;

import pro.burgerz.wsm.IWSMHookLoadPackage;
import pro.burgerz.wsm.WSMBridge;
import pro.burgerz.wsm.WSMHelpers;
import pro.burgerz.wsm.XC_MethodHook;
import pro.burgerz.wsm.callbacks.XC_LoadPackage;
import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class BrowserAutofit implements IWSMHookLoadPackage {

    private static final String MODULE = "BrowserAutofit";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.android.browser")) return;

        WSMHelpers.log(MODULE, "Starting hook in: " + loadPackageParam.packageName);
        ClassLoader classLoader = loadPackageParam.classLoader;

        WSMHelpers.log(MODULE, "com.android.browser.preferences.GeneralPreferencesFragment.onCreate");
        try {
            Class.forName("com.android.browser.preferences.GeneralPreferencesFragment", false, classLoader);
            WSMHelpers.findAndHookMethod("com.android.browser.preferences.GeneralPreferencesFragment", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
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
            WSMHelpers.log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            WSMHelpers.log(MODULE, "Method not found! Skipping...");
        }
    }
}
