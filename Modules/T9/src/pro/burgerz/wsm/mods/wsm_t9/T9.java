package pro.burgerz.wsm.mods.wsm_t9;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class T9 implements IXposedHookLoadPackage {
    
    private static final String MODULE = "T9";

    private static final char[] sNumberOneMap = new char[]{'ㄳ', 'ㅋ'};
    private static final Set sNumberOneSet = createSet(sNumberOneMap);
    private static final char[] sNumberTwoMap = new char[]{'À', 'Á', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'Ā', 'ā', 'Ă', 'ă', 'Ą', 'ą', 'Ć', 'ć', 'Ĉ', 'ĉ', 'Ċ', 'ċ', 'Č', 'č', 'ƀ', 'Ɓ', 'Ƃ', 'ƃ', 'Ƅ', 'ƅ', 'Ɔ', 'Ƈ', 'ƈ', 'Ǎ', 'ǎ', 'Ά', 'Α', 'Β', 'Γ', 'ά', 'α', 'β', 'γ', 'А', 'Б', 'В', 'Г', 'а', 'б', 'в', 'г', 'Ґ', 'ґ', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', '°', '±', '²', '³', '´', 'µ', '¶', '·', 'ㄴ', 'ب', 'ة', 'ت', 'ث', 'پ', 'ד', 'ה', 'ו'};
    private static final Set sNumberTwoSet = createSet(sNumberTwoMap);
    private static final char[] sNumberThreeMap = new char[]{'È', 'É', 'Ê', 'Ë', 'è', 'é', 'ê', 'ë', 'Ď', 'ď', 'Đ', 'đ', 'Ē', 'ē', 'Ĕ', 'ĕ', 'Ė', 'ė', 'Ę', 'ę', 'Ě', 'ě', 'Ɖ', 'Ɗ', 'Ƌ', 'ƌ', 'ƍ', 'Ǝ', 'Ə', 'Ɛ', 'Ƒ', 'ƒ', 'Δ', 'Ε', 'Ζ', 'έ', 'δ', 'ε', 'ζ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'ㄷ', 'ㅌ', 'Ё', 'Ђ', 'Є', 'Д', 'Е', 'Ж', 'З', 'д', 'е', 'ж', 'з', 'ё', 'ђ', 'є', 'א', 'ב', 'ג', 'ء', 'آ', 'أ', 'إ', 'ا', 'ى'};
    private static final Set sNumberThreeSet = createSet(sNumberThreeMap);
    private static final char[] sNumberFourMap = new char[]{'Ì', 'Í', 'Ï', 'ì', 'í', 'ï', 'Ĝ', 'ĝ', 'Ğ', 'ğ', 'Ġ', 'ġ', 'Ģ', 'ģ', 'Ĥ', 'ĥ', 'Ħ', 'ħ', 'Ĩ', 'ĩ', 'Ī', 'ī', 'Ĭ', 'ĭ', 'Į', 'į', 'İ', 'ı', 'Ĳ', 'ĳ', 'Ɠ', 'Ɣ', 'ƕ', 'Ɩ', 'Ɨ', 'Ǐ', 'ǐ', 'Ζ', 'Η', 'Θ', 'Ι', 'Ϊ', 'ή', 'ί', 'η', 'θ', 'ι', 'ϊ', 'І', 'Ї', 'Ј', 'И', 'Й', 'К', 'Л', 'и', 'й', 'к', 'л', 'і', 'ї', 'ј', 'ם', 'מ', 'ן', 'נ', 'س', 'ش', 'ص', 'ض', 'Ỉ', 'ỉ', 'Ị', 'ị', 'ㄹ'};
    private static final Set sNumberFourSet = createSet(sNumberFourMap);
    private static final char[] sNumberFiveMap = new char[]{'Ĵ', 'ĵ', 'Ķ', 'ķ', 'ĸ', 'Ĺ', 'ĺ', 'Ļ', 'ļ', 'Ľ', 'ľ', 'Ŀ', 'ŀ', 'Ł', 'ł', 'Ƙ', 'ƙ', 'ƚ', 'ƛ', 'Κ', 'Λ', 'Μ', 'κ', 'λ', 'μ', 'М', 'Н', 'О', 'П', 'м', 'н', 'о', 'п', 'י', 'ך', 'כ', 'ל', 'د', 'ذ', 'ر', 'ز', 'ژ', 'ㅁ'};
    private static final Set sNumberFiveSet = createSet(sNumberFiveMap);
    private static final char[] sNumberSixMap = new char[]{'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'Ń', 'ń', 'Ņ', 'ņ', 'Ň', 'ň', 'ŉ', 'Ŋ', 'ŋ', 'Ō', 'ō', 'Ŏ', 'ŏ', 'Ő', 'ő', 'Œ', 'œ', 'Ɯ', 'Ɲ', 'ƞ', 'Ɵ', 'Ơ', 'ơ', 'Ƣ', 'ƣ', 'Ό', 'Ν', 'Ξ', 'Ο', 'ν', 'ξ', 'ο', 'ό', 'Р', 'С', 'Т', 'У', 'р', 'с', 'т', 'у', 'ז', 'ח', 'ט', 'ج', 'ح', 'خ', 'چ', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'ㅂ', 'ㅍ'};
    private static final Set sNumberSixSet = createSet(sNumberSixMap);
    private static final char[] sNumberSevenMap = new char[]{'ß', 'Ŕ', 'ŕ', 'Ŗ', 'ŗ', 'Ř', 'ř', 'Ś', 'ś', 'Ŝ', 'ŝ', 'Ş', 'ş', 'Š', 'š', 'Ƥ', 'ƥ', 'Ʀ', 'Ƨ', 'ƨ', 'Ʃ', 'ƪ', 'Π', 'Ρ', 'Σ', 'π', 'ρ', 'ς', 'σ', 'Ф', 'Х', 'Ц', 'Ч', 'ф', 'х', 'ц', 'ч', 'ר', 'ש', 'ת', 'ؤ', 'ئ', 'ن', 'ه', 'و', 'ي', 'ی', 'ㅅ'};
    private static final Set sNumberSevenSet = createSet(sNumberSevenMap);
    private static final char[] sNumberEightMap = new char[]{'Ù', 'Ú', 'Û', 'Ü', 'ù', 'ú', 'û', 'ü', 'Ţ', 'ţ', 'Ť', 'ť', 'Ŧ', 'ŧ', 'Ũ', 'ũ', 'Ū', 'ū', 'Ŭ', 'ŭ', 'Ů', 'ů', 'Ű', 'ű', 'Ų', 'ų', 'ƫ', 'Ƭ', 'ƭ', 'Ʈ', 'Ư', 'ư', 'Ʊ', 'Ʋ', 'Ǔ', 'ǔ', 'Ǖ', 'ǖ', 'Ǘ', 'ǘ', 'Ǚ', 'ǚ', 'Ǜ', 'ǜ', 'Τ', 'Υ', 'Φ', 'τ', 'υ', 'φ', 'ϋ', 'Ш', 'Щ', 'Ъ', 'Ы', 'ш', 'щ', 'ъ', 'ы', 'ץ', 'צ', 'ק', 'ف', 'ق', 'ك', 'ل', 'م', 'ک', 'گ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự', 'ㅇ'};
    private static final Set sNumberEightSet = createSet(sNumberEightMap);
    private static final char[] sNumberNineMap = new char[]{'Ý', 'ý', 'Ŵ', 'ŵ', 'Ŷ', 'ŷ', 'Ÿ', 'Ź', 'ź', 'Ż', 'ż', 'Ž', 'ž', 'Ƴ', 'ƴ', 'Ƶ', 'ƶ', 'Ʒ', 'Ƹ', 'ƹ', 'ƺ', 'ƻ', 'Ƽ', 'ƽ', 'ƾ', 'ƿ', 'Ώ', 'Χ', 'Ψ', 'Ω', 'χ', 'ψ', 'ω', 'ώ', 'Ь', 'Э', 'Ю', 'Я', 'ь', 'э', 'ю', 'я', 'ס', 'ע', 'ף', 'פ', 'ط', 'ظ', 'ع', 'غ', 'Ỳ', 'ỳ', 'Ỵ', 'ỵ', 'Ỷ', 'ỷ', 'Ỹ', 'ỹ', 'ㅈ', 'ㅊ'};
    private static final Set sNumberNineSet = createSet(sNumberNineMap);
    private static final char[] sNumberCeroMap = new char[]{'ㅎ'};
    private static final Set sNumberCeroSet = createSet(sNumberCeroMap);

    private static final char[] sPinyinT9Map = new char[]{'2', '2', '2', '3', '3', '3', '4', '4', '4', '5', '5', '5', '6', '6', '6', '7', '7', '7', '7', '8', '8', '8', '9', '9', '9', '9'};
    private static final char[] sZhuyin2T9Map = new char[]{'1', '1', '1', '1', '2', '2', '2', '2', '3', '3', '3', '4', '4', '4', '5', '5', '5', '5', '6', '6', '6', '7', '7', '7', '7', '8', '8', '8', '8', '9', '9', '9', '9', '9', '0', '0', '0'};
    private static Set createSet(char[] var0) {
        HashSet var1 = new HashSet();
        int var2 = var0.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            var1.add(Character.valueOf(var0[var3]));
        }

        return var1;
    }

    public static boolean isValidT9Key(char var0) {
        return var0 >= 48 && var0 <= 57 || var0 == 44 || var0 == 43 || var0 == 42 || var0 == 35;
    }

    public static char formatCharToT9(char var0) {

        return (char) (isValidT9Key(var0) ? var0 : (var0 >= 65 && var0 <= 90 ? sPinyinT9Map[var0 - 65] : (var0 >= 97 && var0 <= 122 ? sPinyinT9Map[var0 - 97] : (var0 >= 12549 && var0 <= 12585 ? sZhuyin2T9Map[var0 - 12549] : (sNumberCeroSet.contains(Character.valueOf(var0)) ? '0' : (sNumberOneSet.contains(Character.valueOf(var0)) ? '1' : (sNumberTwoSet.contains(Character.valueOf(var0)) ? '2' : (sNumberThreeSet.contains(Character.valueOf(var0)) ? '3' : (sNumberFourSet.contains(Character.valueOf(var0)) ? '4' : (sNumberFiveSet.contains(Character.valueOf(var0)) ? '5' : (sNumberSixSet.contains(Character.valueOf(var0)) ? '6' : (sNumberSevenSet.contains(Character.valueOf(var0)) ? '7' : (sNumberEightSet.contains(Character.valueOf(var0)) ? '8' : (sNumberNineSet.contains(Character.valueOf(var0)) ? '9' : '\u0000'))))))))))))));

    }
    
    private static void log(String module, String log) {
		XposedBridge.log("[" + module + "] " + log);
	}

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.android.providers.contacts")) return;


        log(MODULE, "Start hook in: " + loadPackageParam.packageName);
        ClassLoader classLoader = loadPackageParam.classLoader;
        log(MODULE, "Hooking: com.android.providers.contacts.t9.T9Utils.formatCharToT9");
        try {
            Class.forName("com.android.providers.contacts.t9.T9Utils", false, classLoader);
            XposedHelpers.findAndHookMethod("com.android.providers.contacts.t9.T9Utils", classLoader, "formatCharToT9", char.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return formatCharToT9((Character)methodHookParam.args[0]);
                }
            });
            log(MODULE, "Done!");
            return;
        } catch (ClassNotFoundException ignored) {
        	log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
        	log(MODULE, "Method not found! Skipping...");
        }
        log(MODULE, "Hooking: com.android.providers.contacts.t9.h.b");
        try {
            Class.forName("com.android.providers.contacts.t9.h", false, classLoader);
            XposedHelpers.findAndHookMethod("com.android.providers.contacts.t9.h", classLoader, "b", char.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return formatCharToT9((Character)methodHookParam.args[0]);
                }
            });
            log(MODULE, "Done!");
            return;
        } catch (ClassNotFoundException ignored) {
            log(MODULE, "Class not found! Skipping...");
        } catch (NoSuchMethodError ignored) {
            log(MODULE, "Method not found! Skipping...");
        }
        log(MODULE, "Hooking: com.android.providers.contacts.t9.f.b");
        try {
            Class.forName("com.android.providers.contacts.t9.f", false, classLoader);
            XposedHelpers.findAndHookMethod("com.android.providers.contacts.t9.f", classLoader, "b", char.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return formatCharToT9((Character)methodHookParam.args[0]);
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
