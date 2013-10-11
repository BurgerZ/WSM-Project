package pro.burgerz.wsm.mods.smstommsdisabler;

import pro.burgerz.wsm.IWSMHookLoadPackage;
import pro.burgerz.wsm.WSMHelpers;
import pro.burgerz.wsm.XC_MethodHook;
import pro.burgerz.wsm.XC_MethodReplacement;
import pro.burgerz.wsm.callbacks.XC_LoadPackage.LoadPackageParam;

public class SMStoMMSDisabler implements IWSMHookLoadPackage {
	public static final String MODULE = SMStoMMSDisabler.class.getSimpleName();

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals("com.android.mms")) {
			WSMHelpers.log(MODULE, "Start hook in: " + lpparam.packageName);
			ClassLoader classLoader = lpparam.classLoader;
			XC_MethodReplacement methodreplacer = new XC_MethodReplacement() {
				protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
					return Integer.valueOf(255);
				}
			};
			try {
				Class.forName("com.android.mms.g", false, classLoader);
				WSMHelpers.log(MODULE, "Hooking: com.android.mms.g.kn()");
				WSMHelpers.findAndHookMethod("com.android.mms.g", classLoader, "kn", methodreplacer);
				WSMHelpers.log(MODULE, "Done!");
			} catch (ClassNotFoundException ignored) {
				WSMHelpers.log(MODULE, "Class not found! Skipping...");
            } catch (NoSuchMethodError ignored) {
            	WSMHelpers.log(MODULE, "Method not found! Skipping...");
            } catch (Exception e) {
            	WSMHelpers.log(MODULE, "Error (" + e.toString() + ")! Skipping...");
            }
			try {
				Class.forName("com.android.mms.MmsConfig", false, classLoader);
				WSMHelpers.log(MODULE, "Hooking: com.android.mms.MmsConfig.getSmsToMmsTextThreshold()");
				WSMHelpers.findAndHookMethod("com.android.mms.MmsConfig", classLoader, "getSmsToMmsTextThreshold", methodreplacer);
				WSMHelpers.log(MODULE, "Done!");
			} catch (ClassNotFoundException ignored) {
				WSMHelpers.log(MODULE, "Class not found! Skipping...");
            } catch (NoSuchMethodError ignored) {
            	WSMHelpers.log(MODULE, "Method not found! Skipping...");
            } catch (Exception e) {
            	WSMHelpers.log(MODULE, "Error (" + e.toString() + ")! Skipping...");
            }
			
		}

	}

}
