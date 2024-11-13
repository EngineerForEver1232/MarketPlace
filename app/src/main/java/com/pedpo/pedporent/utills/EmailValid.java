package com.pedpo.pedporent.utills;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValid {

    public static boolean isEmailValid(String email)
    {
//        String regExpn =
//                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

         String EMAIL_PATTERN_MK =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

          String regExpn = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(EMAIL_PATTERN_MK);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean isValidEmailPatterns(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static String[] faNumbers = new String[]{"۰","۱","۲","۳","۴","۵","۶","۷","۸","۹"};
    private static String[] enNumbers = new String[]{"0","1","2","3","4","5","6","7","8","9","+"};


    public static boolean isValidPhone(String contentInput) {

        String content = NumberFormatPersian.getNewInstance().toNumberEnlish(contentInput);

        char[] chars = content.toCharArray();
        ArrayList<String> arrayListChar = new ArrayList<>();

        for (char c :  chars)
        {
            arrayListChar.add(c+"");
        }

        boolean valid = false ;
        for (String number : arrayListChar) {
            Log.e("login", "login: " + number);
            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(enNumbers));

            if (arrayList.contains(number)){
                valid = true ;
            }else{ valid = false;
                Log.e("loginCode", "login: " + number);

                return false ;
            }
        }

        return valid ;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
//        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";
//        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[~!@#$%^&*-+=:;'<>,_.?])(?=\\S+$).{4,}$";

//        String PASSWORD_MK = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        String PASSWORD_MKYOUNG = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

        pattern = Pattern.compile(PASSWORD_MKYOUNG);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
