package pro.burgerz.wsm.mods.carrierlabel;

import java.util.List;

import miui.preference.BasePreferenceActivity;
import miui.preference.ValuePreference;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class CarrierLabel implements IXposedHookLoadPackage {

    private static final String MODULE = "CarrierLabel";

    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
	@Override
	public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.android.systemui")) {
            log(MODULE, "Start hook in: " + loadPackageParam.packageName);
            ClassLoader classLoader = loadPackageParam.classLoader;

            try {
                Class.forName("com.android.systemui.settings.StyleSettings", false, classLoader);
                //Class.forName("com.android.settings.MiuiDeviceInfoSettings", false, classLoader);
                XposedHelpers.findAndHookMethod("com.android.systemui.settings.StyleSettings", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        @SuppressWarnings("deprecation")
						PreferenceScreen screen = ((BasePreferenceActivity) param.thisObject).getPreferenceScreen();
                        Context context = screen.getContext();//(Activity) ((BasePreferenceActivity) param.thisObject).get;
                        
                        PreferenceScreen screen0 = (PreferenceScreen) screen.getPreferenceManager().findPreference("parent");
                        //PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                        //preferenceCategory.setTitle("CARRIER");
                        
                        Context localContext = context.createPackageContext("pro.burgerz.wsm.mods.carrierlabel", 2);
                        
                        TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService("phone");
                        String mOperator = mTelephonyManager.getNetworkOperatorName();
                        String mOperatorCode = mTelephonyManager.getSimOperator();
                        String name = System.getString(localContext.getContentResolver(), 
                        		"MOBILE_OPERATOR_NAME_" + mOperatorCode);
                        
                        log(MODULE, "Operator: " + mOperator);
                        log(MODULE, "Alias: " + name);
                        
                        ValuePreference carrierLabel = new ValuePreference(context);
                        carrierLabel.setKey("wsm_carrier_label");
                        carrierLabel.setTitle(localContext.getString(R.string.pref_carrier_title));
                        carrierLabel.setValue(TextUtils.isEmpty(name) ? mOperator : name);
                        carrierLabel.setShowRightArrow(true);
                        Intent i = getLaunchIntent(context, localContext.getPackageName());
                        carrierLabel.setIntent(i);
                        //carrierLabel.setOnPreferenceChangeListener(CarrierLabel.this);
                        //carrierLabel.setIntent(new Intent(context, SpnConfig.class));

                        //String date = null;
                        //try {
                        //    Class<?> c = Class.forName("android.os.SystemProperties");
                        //    Method get = c.getMethod("get", String.class);
                        //    date = (String) get.invoke(c, "ro.build.date");
                        //} catch (Exception ignored) {
                        //}
                        //screen0.addPreference(preferenceCategory);
                        screen0.addPreference(carrierLabel);

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
	
	private Intent getLaunchIntent(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory("pro.burgerz.wsm.category.CL_EDIT");
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, 0);
        if (ris == null || ris.size() <= 0) {
            return pm.getLaunchIntentForPackage(packageName);
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get(0).activityInfo.packageName, ris.get(0).activityInfo.name);
        return intent;
    }

}
