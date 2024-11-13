package com.pedpo.pedporent.widget.calendar.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatPersian {

    private static NumberFormatPersian numberFormatPersian = new NumberFormatPersian();

    public static NumberFormatPersian getNewInstance() {
        return numberFormatPersian;
    }

    public String getFormat(String number) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(Long.parseLong(number));
    }

    public String toPersianNumbersSample(String str) {
        return str
                .replace("0", "۰")
                .replace("1", "۱")
                .replace("2", "۲")
                .replace("3", "۳")
                .replace("4", "۴")
                .replace("5", "۵")
                .replace("6", "۶")
                .replace("7", "۷")
                .replace("8", "۸")
                .replace("9", "۹");
//                .replace("-", "/");
    }

    public String toPersianMonth(String str) {
        return str
                .replace("10", "دی")
                .replace("11", "بهمن")
                .replace("12", "اسفند")
                .replace("1", "فروردین")
                .replace("2", "اردیبهشت")
                .replace("3", "خرداد")
                .replace("4", "تیر")
                .replace("5", "مرداد")
                .replace("6", "شهریور")
                .replace("7", "مهر")
                .replace("8", "آبان")
                .replace("9", "آذر");
    }

    public String toPersianDay(int month) {
        String monthString;
        switch (month) {
            case 1:
                monthString = "فروردین";
                break;
            case 2:
                monthString = "اردیبهشت";
                break;
            case 3:
                monthString = "خرداد";
                break;
            case 4:
                monthString = "تیر";
                break;
            case 5:
                monthString = "مرداد";
                break;
            case 6:
                monthString = "شهریور";
                break;
            case 7:
                monthString = "مهر";
                break;
            case 8:
                monthString = "آبان";
                break;
            case 9:
                monthString = "آذر";
                break;
            case 10:
                monthString = "دی";
                break;
            case 11:
                monthString = "بهمن";
                break;
            case 12:
                monthString = "اسفند";
                break;
            default:
                monthString = "خطا";
                break;
        }
        return monthString;
    }


    private static String[] faNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    public String convertToPersian(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += faNumbers[number];
            } else if (c == '٫' || c == ',' || c == '٬') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }

    public String splitDigits(String numberString) {

        if (numberString.isEmpty())
            return "";

        long number = Long.parseLong(numberString.replaceAll("[^\\d]", ""));

        try {
//            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###");
//            DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
//            decimalFormatSymbol.setGroupingSeparator('،');
//            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbol);

//            return String.format("%,d",number);


            DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
            decimalFormatSymbol.setGroupingSeparator('،');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(decimalFormatSymbol);
            df.setGroupingSize(3);
            return df.format(number);
        } catch (Exception ex) {
            return String.valueOf(number);
        }
    }

    public String clearComma(String numberString) {
        if (numberString.isEmpty())
            return "";
        Log.e("splitDigitsTest", "onTextChanged2222: " + numberString);

        return numberString.replaceAll("[^\\d]", "");
    }

    public String toEnglishClearComma(String numberString) {
        return toNumberEnlish(clearComma(numberString));
    }

    public String toNumberEnlish(String str) {
        return str
                .replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9");
    }


    public String splitDigitsPersianNumber(String number) {
        return splitDigits(convertToPersian(number));
    }


}
