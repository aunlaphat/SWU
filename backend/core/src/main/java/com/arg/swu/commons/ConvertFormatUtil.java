package com.arg.swu.commons;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * 
 * 
 * @author sitthichaim
 *
 */
public class ConvertFormatUtil {
	
    protected final static String DEFAULT_NUMBER_FORMAT = "###0";
    protected final static String DEFAULT_DOUBLE_FORMAT = "#.######";
    public final static String DEFAULT_DECIMAL_0DIGIT_FORMAT = "#,##0";
    public final static String DEFAULT_DECIMAL_2DIGIT_FORMAT = "#,##0.00";
    protected final static String DEFAULT_NO_DATA = "";

    public static String convertFormat(String str) {
        return (str != null && str.trim().length() > 0) ? str.trim() : DEFAULT_NO_DATA;
    }

    public static String convertFormat(Number number, String format) {
        if (number == null) {
            return convertFormat((String) null);
        }

        DecimalFormat numberFormat = new DecimalFormat();
        try {
            numberFormat.applyPattern(format);
            return numberFormat.format(number);
        } catch (Exception e) {
            return convertFormat(number);
        }

    }

    public static String convertFormat(Long value) {
        if (null == value) {
            return DEFAULT_NO_DATA;
        }

        String valuetemp = value.toString();
        return valuetemp;
    }

    public static String convertFormat(Integer edition, String description) {
        String desc = convertFormat(description);
        return edition.toString() + " : " + desc;
    }

    public static String convertFormat(Number number) {
        return convertFormat(number, DEFAULT_NUMBER_FORMAT);
    }

    public static String convertFormat(Double number) {
        return convertFormat(number, DEFAULT_DOUBLE_FORMAT);
    }

    public static String convertFormatCommaDecimal(Double val) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df.format(val);
    }

    public static String convertFormatCommaDecimal(BigDecimal val, String format) {
        DecimalFormat df;
        if (format != null && format.length() > 0) {
            df = new DecimalFormat(format);
        } else {
            df = new DecimalFormat();
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setGroupingSeparator(',');
            df.setDecimalFormatSymbols(dfs);
        }
        return df.format(val);
    }
}
