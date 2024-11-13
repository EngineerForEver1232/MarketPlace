package com.pedpo.pedporent.utills;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;



public class NumberGrouping {

    private static NumberGrouping numberGrouping = new NumberGrouping();
    public static NumberGrouping getNewInstance()
    {
        return numberGrouping ;
    }
    TextWatcher textWatcher = null;
    public void convertNumberToPersian(EditText editText) {
        textWatcher = new TextWatcher() {
            private int beforeTextChanged ;
            private int onTextChanged ;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeTextChanged = charSequence.toString().length();
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                onTextChanged = s.toString().length() ;

                if (beforeTextChanged < onTextChanged) {
//                    for (String chr : new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}) {
//                        if (s.toString().contains("" + chr)) {
                    String formatNumber =
                            NumberFormatPersian.getNewInstance().convertToPersian(
                                    NumberFormatPersian.getNewInstance().splitDigits(editText.getText().toString())
                            );
                    editText.setSelection(editText.getText().length());
                    editText.removeTextChangedListener(textWatcher);
                    editText.setText(formatNumber);
                    editText.addTextChangedListener(textWatcher);
//                        }
                }

//                }
//                editText.removeTextChangedListener(this);
//                editText.setText(NumberFormatPersian.getNewInstance().convertToPersian(editText.getText().toString()));
//                editText.setSelection(editText.getText().length());
//                editText.addTextChangedListener(this);
            }
            @Override
            public void afterTextChanged(Editable editable) {

                //                if (editable.toString().contains("،"))
                if (beforeTextChanged > onTextChanged )
                {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            String formatNumber =
                                    NumberFormatPersian.getNewInstance().convertToPersian(
                                            NumberFormatPersian.getNewInstance().splitDigits(editText.getText().toString())
                                    );
                            editText.setText(formatNumber);
                            editText.setSelection(editText.getText().length());
                        }
                    };
                    handler.removeCallbacks(runnable);
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(runnable,100);


                }
            }
        };
        editText.addTextChangedListener(textWatcher);
    }
    private Handler handler = new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            return false;
        }
    });


//    public void changeLanguage(EditText eText)
//    {
//        eText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (charSequence.toString().length()<5) {
//                    LanguageIdentifier languageIdentifier =
//                            LanguageIdentification.getClient();
//                    languageIdentifier.identifyLanguage(charSequence.toString())
//                            .addOnSuccessListener(
//                                    new OnSuccessListener<String>() {
//                                        @Override
//                                        public void onSuccess(@Nullable String languageCode) {
//                                            if (languageCode.equals("und")) {
//                                            } else if (languageCode.equals("ar") || languageCode.equals("fa")) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////                                                    textInputLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                                                    eText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
////                                                    textInputLayout.setTextDirection(View.LAYOUT_DIRECTION_RTL);
//                                                    eText.setTextDirection(View.LAYOUT_DIRECTION_RTL);
//                                                }
//                                            } else {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////                                                    textInputLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                                                    eText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
////                                                    textInputLayout.setTextDirection(View.LAYOUT_DIRECTION_LTR);
//                                                    eText.setTextDirection(View.LAYOUT_DIRECTION_LTR);
//                                                }
//                                            }
//                                        }
//                                    })
//                            .addOnFailureListener(
//                                    new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Model couldn’t be loaded or other internal error.
//                                            Log.e("InputMethodManager", "Language: " + e.getMessage());
//
//                                        }
//                                    });
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }




}
