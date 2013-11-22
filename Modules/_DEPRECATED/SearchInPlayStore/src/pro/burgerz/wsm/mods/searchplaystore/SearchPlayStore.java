package pro.burgerz.wsm.mods.searchplaystore;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.callbacks.XCallback;

public class SearchPlayStore implements IXposedHookLoadPackage, IXposedHookZygoteInit {

	private static final String MODULE = "SearchPlayStore";
	
	private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
	
	@Override
	public void handleLoadPackage(LoadPackageParam loadPackageParam)
			throws Throwable {

		if (loadPackageParam.packageName.equals("com.android.quicksearchbox")) {
			
			log(MODULE, "Start hook in: " + loadPackageParam.packageName);
			ClassLoader classLoader = loadPackageParam.classLoader;
			try {
				Class.forName("com.android.quicksearchbox.ui.SearchActivityView", false, classLoader);
				log(MODULE, "Hooking: " + "com.android.quicksearchbox.ui.SearchActivityView.fillFixedSuggestionView()");
                XposedHelpers.findAndHookMethod("com.android.quicksearchbox.ui.SearchActivityView", 
                		classLoader, "fillFixedSuggestionView", 
                		"com.android.quicksearchbox.ui.DefaultSuggestionView", String.class, int.class, 
                		replaceMarket);
                
                log(MODULE, "Hooking: " + "com.android.quicksearchbox.AbstractSource.createSourceSearchIntent()");
                XposedHelpers.findAndHookMethod("com.android.quicksearchbox.AbstractSource", 
                		classLoader, "createSourceSearchIntent", 
                		ComponentName.class, String.class, Bundle.class, 
                		replaceMarketActivity);
                
                log(MODULE, "Done!");
			} catch (ClassNotFoundException ignored) {
				log(MODULE, "Class not found! Skipping...");
			} catch (NoSuchMethodError ignored) {
				log(MODULE, "Method not found! Skipping...");
			}
		}

	}
	
	private static XC_MethodHook replaceMarket = new XC_MethodHook(XCallback.PRIORITY_HIGHEST) {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			prefs.reload();
			boolean useGoogle = prefs.getBoolean("use_google_market", true);
			Object[] args = param.args;
			String pack = (String) args[1];
			String packXiaomi = "com.xiaomi.market";
			String packGoogle = "com.android.vending";
			Object[] newArgs = new Object[3];
			if (pack.equals(packGoogle) || pack.equals(packXiaomi)) {
				newArgs[1] = useGoogle ? packGoogle : packXiaomi;
			}
			newArgs[0] = param.args[0];
			newArgs[2] = param.args[2];
			XposedBridge.invokeOriginalMethod(param.method, param.thisObject, newArgs);
		}
	};
	private static XC_MethodReplacement replaceMarketActivity = new XC_MethodReplacement() {
		@Override
		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
			prefs.reload();
			boolean useGoogle = prefs.getBoolean("use_google_market", true);
			ComponentName comp = (ComponentName) param.args[0];
			ComponentName compXiaomi = new ComponentName("com.xiaomi.market", "com.xiaomi.market.ui.JoinActivity");
			ComponentName compGoogle = new ComponentName("com.android.vending", "com.google.android.finsky.activities.MainActivity");
			Object[] newArgs = new Object[3];
			if (comp.equals(compGoogle) || comp.equals(compXiaomi)) {
				newArgs[0] = useGoogle ? compGoogle : compXiaomi;
			}
			newArgs[1] = param.args[1];
			newArgs[2] = param.args[2];
			Intent i;
		      if(newArgs[0] == null) {
		         i = null;
		      } else {
		         i = new Intent(Intent.ACTION_SEARCH);
		         i.setComponent((ComponentName) newArgs[0]);
		         i.addFlags(268435456);
		         i.addFlags(67108864);
		         i.putExtra("user_query", (String) newArgs[1]);
		         i.putExtra("query", (String) newArgs[1]);
		         if(newArgs[2] != null) {
		            i.putExtra("app_data", (Bundle) newArgs[2]);
		            return i;
		         }
		      }
		      return i;	
		}
	};
	
	private static XSharedPreferences prefs;
	private static final String PACKAGE = "pro.burgerz.wsm.mods.searchplaystore";
	
	@Override
	public void initZygote(StartupParam arg0) throws Throwable {
		prefs = new XSharedPreferences(PACKAGE);
	}

}
