package tpp.profixer.customer.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import tpp.profixer.customer.constant.Constants;

public class NumberUtils {
    public static String formatCurrency(int d){
        int decimalSpace = 0;
        String pattern = "#,##0" + (decimalSpace > 0 ? "." + "0".repeat(decimalSpace) : "");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, decimalFormatSymbols);
        return decimalFormat.format(d) + " " + Constants.CURRENCY_SYMBOL;
    }
}
