package pro.burgerz.wsm.mods.mithemes;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MiThemes implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static final String MODULE = "MiThemes";
    private static XSharedPreferences mPrefs;
    private static ClassLoader mClassLoader;

	private static void log(String log) {
		XposedBridge.log("[" + MODULE + "] " + log);
	}

    private static void findAndHookMethod(String clazz, String methodName, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(clazz), methodName, parameterTypesAndCallback);
        } catch (ClassNotFoundException ignored) {
            log("[" + MODULE + "] Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log("[" + MODULE + "] Method not found! Skipping...");
        }
    }

    private static void drmManager() {
        findAndHookMethod("miui.drm.DrmManager", "isLegal",
                Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        findAndHookMethod("miui.drm.DrmManager", "isLegal",
                Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(true));
        findAndHookMethod("miui.drm.DrmManager", "isLegal",
                Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(true));
        findAndHookMethod("miui.drm.DrmManager", "isRightsFileLegal",
                File.class, XC_MethodReplacement.returnConstant(true));
        //old
        findAndHookMethod("miui.drm.DrmManager", "isLegal",
                File.class, File.class, XC_MethodReplacement.returnConstant(true));
        findAndHookMethod("miui.drm.DrmManager", "isLegal",
                String.class, File.class, XC_MethodReplacement.returnConstant(true));
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        drmManager();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        mClassLoader = loadPackageParam.classLoader;
        hook();
    }

    private static void hook() {
            paymentSystem();
            authSystem();
            notifyPurchaseSuccess();
    }

    private static void paymentSystem() {
        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    mClassLoader, "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodError ignored) {
        }
        //NEW METHODS IN NEW ROMZ
        try {
            Class.forName("miui.resourcebrowser.controller.online.NetworkHelper", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.NetworkHelper",
                    mClassLoader, "validateResponseResult", int.class, InputStream.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            InputStream is = (InputStream) param.args[1];
                            param.setResult(is);
                            return;
                        }
                    });
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodError ignored) {
        }
    }

    private static void authSystem() {
        try {
            Class.forName("miui.resourcebrowser.controller.online.DrmService", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.controller.online.DrmService",
                    mClassLoader,
                    "isLegal", "miui.resourcebrowser.model.Resource", new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                            return true;
                        }
                    });
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodError ignored) {
        }
        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    mClassLoader, "isLegal", new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                            return true;
                        }
                    });
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodError ignored) {
        }
        try {
            Class.forName("miui.resourcebrowser.view.ResourceOperationHandler", false, mClassLoader);
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",
                    mClassLoader,
                    "onCheckResourceRightEventBeforeRealApply", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            XposedHelpers.setBooleanField(param.thisObject, "mIsLegal", true);
                        }
                    });
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodError ignored) {
        }
    }

    private static void notifyPurchaseSuccess() {
        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.Resource",
                    mClassLoader,
                    "isProductBought", XC_MethodReplacement.returnConstant(true));
        } catch (Exception ignored) {
        } catch (NoSuchMethodError ignored) {
        }
        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties",
                    mClassLoader,
                    "setProductBought", boolean.class, new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param)
                                throws Throwable {
                            Object[] arg = new Object[]{true};
                            param.args = arg;
                            return XposedBridge.invokeOriginalMethod(param.method,
                                    param.thisObject, arg);
                        }
                    });
        } catch (Exception ignored) {
        } catch (NoSuchMethodError ignored) {
        }
        try {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.model.ResourceOnlineProperties",
                    mClassLoader,
                    "isProductBought", XC_MethodReplacement.returnConstant(true));
        } catch (Exception ignored) {
        } catch (NoSuchMethodError ignored) {
        }
    }

}
