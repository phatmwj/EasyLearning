package tpp.easy.learning.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class NumberInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(source.charAt(i))) {
                return ""; // Chặn ký tự không phải số
            }
        }
        return null; // Cho phép ký tự là số
    }
}
