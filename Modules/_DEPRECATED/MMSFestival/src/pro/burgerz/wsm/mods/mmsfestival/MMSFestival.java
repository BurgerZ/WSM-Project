package pro.burgerz.wsm.mods.mmsfestival;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MMSFestival implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    private static final String MODULE = "MMSFestival";
    private static final String PACKAGE = MMSFestival.class.getPackage().getName();
    private static XSharedPreferences prefs;
    
    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
		prefs = new XSharedPreferences(PACKAGE);
		prefs.makeWorldReadable();
    }
    
    public static Method[] findMethods(Class<?> clazz, Class<?> returnType, Class<?>... parameterTypes) {
    	List<Method> result = new LinkedList<Method>();
        for (Method method : clazz.getDeclaredMethods()) {
                if (returnType != null && returnType != method.getReturnType())
                        continue;
                Class<?>[] methodParameterTypes = method.getParameterTypes();
                if (parameterTypes.length != methodParameterTypes.length)
                        continue;
                boolean match = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                        if (parameterTypes[i] != methodParameterTypes[i]) {
                                match = false;
                                break;
                        }
                }
                if (!match)
                        continue;
                method.setAccessible(true);
                result.add(method);
        }
        return result.toArray(new Method[result.size()]);
}
    
	@Override
	public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.android.mms")) {
            log(MODULE, "Start hook in: " + loadPackageParam.packageName);
            ClassLoader classLoader = loadPackageParam.classLoader;
            
            try {
                Class<?> class0 = Class.forName("com.android.mms.data.FestivalUpdater", false, classLoader);
                    log(MODULE, "Class: com.android.mms.data.FestivalUpdater");
                    Method[] methods = findMethods(class0, JSONObject.class, new Class<?>[] {String.class});
                    for (Method m : methods) {
                    	XposedHelpers.findAndHookMethod("com.android.mms.data.FestivalUpdater", classLoader, m.getName(), String.class, new XC_MethodReplacement() {
                            @Override
                            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                                log(MODULE, "Calling: " + methodHookParam.method.getName() + "(String)");
                                String paramString = (String) methodHookParam.args[0];
                                prefs.reload();
                                String server_categories_miuisu = "http://miui.su/miuisms.php";
                                String server_messages_miuisu = "http://ota.miui.su/miuisms/res/messages.php";
                                String server_categories_romz = "http://cloud.romz.bz/miuisms/res/categories.php";
                                String server_messages_romz = "http://cloud.romz.bz/miuisms/res/messages.php";
                            	String server_categories_original  = "http://api.comm.miui.com/miuisms/res/categories";
                            	String server_messages_original  = "http://api.comm.miui.com/miuisms/res/messages";
                            	
                            	String CATEGORIES_URL = server_categories_original;
                            	String MESSAGES_URL = server_messages_original;
                            	if (prefs.getString(Settings.PREF_KEY_UPDATE_SERVER, "server_original").equals("server_romz")) {
                            		CATEGORIES_URL = server_categories_romz;
                            		MESSAGES_URL = server_messages_romz;
                            		log(MODULE, "Server: romz.bz");
                            	} else if (prefs.getString(Settings.PREF_KEY_UPDATE_SERVER, "server_original").equals("server_miuisu")) {
                            		CATEGORIES_URL = server_categories_miuisu;
                            		MESSAGES_URL = server_messages_miuisu;
                            		log(MODULE, "Server: miui.su");
                            	} else {
                            		log(MODULE, "Server: miui.com");
                            	}
                                paramString = paramString.replace("http://api.comm.miui.com/miuisms/res/categories", CATEGORIES_URL);
                                paramString = paramString.replace("http://api.comm.miui.com/miuisms/res/messages", MESSAGES_URL);
                                
                                Object[] args = new Object[]{paramString};
                                return XposedBridge.invokeOriginalMethod(methodHookParam.method, methodHookParam.thisObject, args);
                            }
                        });
                    }
            } catch (ClassNotFoundException ignored) {
                log(MODULE, "Class not found! Skipping...");
            } catch (NoSuchMethodError ignored) {
                log(MODULE, "Method not found! Skipping...");
            }
        }
		
	}

}
