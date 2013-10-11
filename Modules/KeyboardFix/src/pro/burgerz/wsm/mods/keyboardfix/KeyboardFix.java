package pro.burgerz.wsm.mods.keyboardfix;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class KeyboardFix implements IXposedHookZygoteInit {
	
	private String MODULE = "KeyboardFix";

	private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
	
    public static final int convertValueToInt(CharSequence var0, int var1) {
        if(var0 == null) {
            return var1;
        } else {
            String str = var0.toString().replaceAll("[^0-9\\.]","");
            return (int) Double.parseDouble(str);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    	log(MODULE, "Hooking: com.android.internal.util.XmlUtils.convertValueToInt(CharSequence, int)");
        try {
        	XposedHelpers.findAndHookMethod(Class.forName("com.android.internal.util.XmlUtils"), "convertValueToInt", CharSequence.class, int.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return convertValueToInt((CharSequence) methodHookParam.args[0], ((Integer) methodHookParam.args[1]).intValue());
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
