package pro.burgerz.wsm.mods.holidaysmodule;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HolidaysModule implements IXposedHookLoadPackage, IXposedHookZygoteInit, Preference.OnPreferenceChangeListener {

    private static final String MODULE = "HolidaysModule";
	private static final String PACKAGE = HolidaysModule.class.getPackage().getName();
	private static XSharedPreferences prefs;

    private static List<Integer> workdays2013_ru = Arrays.asList();
    private static List<Integer> freedays2013_ru = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 54, 67, 121, 122, 123, 129, 130, 163, 308);
    private static List<Integer> workdays2014_ru = Arrays.asList();
    private static List<Integer> freedays2014_ru = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 54, 67, 69, 121, 122, 129, 130, 163, 164, 307, 308);
    
    private static List<Integer> workdays2013_uk = Arrays.asList(117, 138);
    private static List<Integer> freedays2013_uk = Arrays.asList(1, 7, 67, 121, 122, 123, 126, 129, 130, 175, 179, 238);
    private static List<Integer> workdays2014_uk = Arrays.asList();
    private static List<Integer> freedays2014_uk = Arrays.asList(1, 7, 8, 69, 111, 121, 122, 129, 130, 160, 181, 237);

    private static List<Integer> workdays2013_sk = Arrays.asList();
    private static List<Integer> freedays2013_sk = Arrays.asList(305,358,359,360);
    private static List<Integer> workdays2014_sk = Arrays.asList();//need to update
    private static List<Integer> freedays2014_sk = Arrays.asList(305,358,359,360);//need to update

    private static List<Integer> workdays2013_cz = Arrays.asList();
    private static List<Integer> freedays2013_cz = Arrays.asList(301,358,359,360);
    private static List<Integer> workdays2014_cz = Arrays.asList();//need to update
    private static List<Integer> freedays2014_cz = Arrays.asList(301,358,359,360); //need to update

    
	private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}
    
    private static void replaceHolidayUpdate() {
        log(MODULE, "Hooking: miui.util.DataUpdateUtils.HOLIDAY_URL");
        try {
        	prefs.reload();
        	String server_miuisu = "http://miui.su/bu_festival/calendar";
        	String server_romz = "http://cloud.romz.bz/holidays/";
        	String original  = "http://api.comm.miui.com/holiday/holiday.jsp";
        	String server_slovak = "http://calendar.miui.cz/holidays-sk.jsp";
        	String server_czech = "http://calendar.miui.cz/holidays-cz.jsp";
        	String HOLIDAY_URL = original;
        	if (prefs.getString(Settings.PREF_KEY_UPDATE_SERVER, "server_original").equals("server_romz")) {
        		HOLIDAY_URL = server_romz;
        	} else if (prefs.getString(Settings.PREF_KEY_UPDATE_SERVER, "server_original").equals("server_miuisu")) {
        		HOLIDAY_URL = server_miuisu;
        	} else if (prefs.getString(Settings.PREF_KEY_UPDATE_SERVER, "server_original").equals("server_cz_slovak")) {
        		HOLIDAY_URL = server_slovak;
        	} else if (prefs.getString(Settings.PREF_KEY_UPDATE_SERVER, "server_original").equals("server_cz_czech")) {
        		HOLIDAY_URL = server_czech;
        	}
            XposedHelpers.setStaticObjectField(Class.forName("miui.util.DataUpdateUtils"), "HOLIDAY_URL", HOLIDAY_URL);
            log(MODULE, "Done!");
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchFieldError ignored) {
            log(MODULE, "Field not found! Skipping...");
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
    	
        if (loadPackageParam.packageName.equals("com.android.calendar")) {
            log(MODULE, "Start hook in: " + loadPackageParam.packageName);
            final ClassLoader classLoader = loadPackageParam.classLoader;
            try {
                Class.forName("com.android.calendar.CalendarUtils", false, classLoader);
            	
                XposedHelpers.findAndHookMethod("com.android.calendar.CalendarUtils", classLoader, "getDaysOffType", Context.class, int.class, int.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        Context context = (Context) methodHookParam.args[0];
                        int year = (Integer) methodHookParam.args[1];
                        int day = (Integer) methodHookParam.args[2];

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        if (sp.getString("holidays_selector", "original").equals("russian")) {
                            if (year == 2013) {
                                if (workdays2013_ru.contains(day)) return 2;
                                if (freedays2013_ru.contains(day)) return 1;
                            }
                            if (year == 2014) {
                                if (workdays2014_ru.contains(day)) return 2;
                                if (freedays2014_ru.contains(day)) return 1;
                            }
                            return 0;
                        }
                        if (sp.getString("holidays_selector", "original").equals("ukrainian")) {
                            if (year == 2013) {
                                if (workdays2013_uk.contains(day)) return 2;
                                if (freedays2013_uk.contains(day)) return 1;
                            }
                            if (year == 2014) {
                                if (workdays2014_uk.contains(day)) return 2;
                                if (freedays2014_uk.contains(day)) return 1;
                            }
                            return 0;
                        }
                        if (sp.getString("holidays_selector", "original").equals("slovak")) {
                            if (year == 2013) {
                                if (workdays2013_sk.contains(day)) return 2;
                                if (freedays2013_sk.contains(day)) return 1;
                            }
                            if (year == 2014) {
                                if (workdays2014_sk.contains(day)) return 2;
                                if (freedays2014_sk.contains(day)) return 1;
                            }
                            return 0;
                        }
                        if (sp.getString("holidays_selector", "original").equals("czech")) {
                            if (year == 2013) {
                                if (workdays2013_cz.contains(day)) return 2;
                                if (freedays2013_cz.contains(day)) return 1;
                            }
                            if (year == 2014) {
                                if (workdays2014_cz.contains(day)) return 2;
                                if (freedays2014_cz.contains(day)) return 1;
                            }
                            return 0;
                        }
                        return XposedBridge.invokeOriginalMethod(methodHookParam.method, methodHookParam.thisObject, methodHookParam.args);
                    }
                });
                log(MODULE, "Done!");
            } catch (ClassNotFoundException ignored) {
                log(MODULE, "Class not found! Skipping...");
            } catch (NoSuchMethodError ignored) {
                log(MODULE, "Method not found! Skipping...");
            } catch (Exception e) {
                log(MODULE, "Error (" + e.toString() + ")! Skipping...");
            }

            try {
                Class.forName("com.android.calendar.GeneralPreferences", false, classLoader);
            	
                XposedHelpers.findAndHookMethod("com.android.calendar.GeneralPreferences", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    	PreferenceScreen screen = ((PreferenceFragment) param.thisObject).getPreferenceScreen();
                        Context context = ((PreferenceFragment) param.thisObject).getActivity();
                        Context localContext = context.createPackageContext("pro.burgerz.wsm.mods.holidaysmodule", 2);

                        PreferenceCategory preferenceCategory = new PreferenceCategory(context);
                        preferenceCategory.setTitle(localContext.getString(R.string.holidays));

                        ListPreference holidays = new ListPreference(context);
                        holidays.setKey("holidays_selector");
                        holidays.setEntryValues(new String[]{"original", "russian", "ukrainian", "slovak", "czech"});
                        holidays.setEntries(new String[]{localContext.getString(R.string.holiday_original), 
                        		localContext.getString(R.string.holiday_russian), 
                        		localContext.getString(R.string.holiday_ukrainian),
                        		localContext.getString(R.string.holiday_slovak),
                        		localContext.getString(R.string.holiday_czech)});
                        holidays.setTitle(localContext.getString(R.string.holiday_select));
                        holidays.setDialogTitle(localContext.getString(R.string.holiday_select));
                        holidays.setSummary(localContext.getString(R.string.holiday_select_desc));

                        XSharedPreferences xsp = new XSharedPreferences("com.android.calendar");
                        xsp.makeWorldReadable();

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        holidays.setValue(prefs.getString("holidays_selector", "original"));
                        holidays.setOnPreferenceChangeListener(HolidaysModule.this);

                        screen.addPreference(preferenceCategory);
                        screen.addPreference(holidays);

                    }
                });
                log(MODULE, "Done!");
            } catch (ClassNotFoundException ignored) {
                log(MODULE, "Class not found! Skipping...");
            } catch (NoSuchMethodError ignored) {
                log(MODULE, "Method not found! Skipping...");
            } catch (Exception e) {
                log(MODULE, "Error (" + e.toString() + ")! Skipping...");
            }
        }

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
		prefs = new XSharedPreferences(PACKAGE);
		prefs.makeWorldReadable();
    	replaceHolidayUpdate();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals("holidays_selector")) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            SharedPreferences.Editor edt = prefs.edit();
            edt.putString("holidays_selector", (String) o);
            edt.commit();
            return true;
        }
        return false;
    }
}
