package com.arg.swu.commons;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 
 * @author sitthichaim
 *
 */
public class BahtTextUtil {
	private static final String[] NUMBER_SCALES = {
	        "ล้าน", "สิบ", "ร้อย",
	        "พัน", "หมื่น", "แสน", ""
    };
    private static final String[] DIGIT_WORDS = {
        "ศูนย์", "หนึ่ง", "สอง",
        "สาม", "สี่", "ห้า", "หก",
        "เจ็ด", "แปด", "เก้า"
    };
    //~ Instance fields ························································
    private BigDecimal amount;
    private String amountText;

    //~ Constructors ···························································
    public BahtTextUtil(double amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtTextUtil(float amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtTextUtil(int amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtTextUtil(long amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtTextUtil(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtTextUtil(Number amount) {
        this.amount = new BigDecimal(String.valueOf(amount));
    }

    //~ Methods ································································
    /**
     * Returns baht text. This method triggers initialization in
     * concurrent/thread safe fashion.
     *
     * @return Baht text.
     */
    public String toString() {
        if (null == amountText) {
            synchronized (this) {
                if (null == amountText) {
                    this.amountText = getBathText(this.amount);
                    if (BigDecimal.ZERO.equals(amount)) {
                        this.amountText = this.DIGIT_WORDS[0] + this.amountText;
                    }
                }
            }
        }

        return this.amountText;
    }

    public static String getBathText(BigDecimal amount) {
        if(amount == null){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        BigDecimal absolute = amount.abs();
        int precision = absolute.precision();
        int scale = absolute.scale();
        int rounded_precision = ((precision - scale) + 2);
        MathContext mc = new MathContext(rounded_precision, RoundingMode.HALF_UP);
        BigDecimal rounded = absolute.round(mc);
        BigDecimal[] compound = rounded.divideAndRemainder(BigDecimal.ONE);
        boolean negative_amount = (-1 == amount.compareTo(BigDecimal.ZERO));

        compound[0] = compound[0].setScale(0);
        compound[1] = compound[1].movePointRight(2);

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            buffer.append("ศูนย์บาท");
        } else {

            if (negative_amount) {
                buffer.append("ลบ");
            }

            buffer.append(getNumberText(compound[0].toBigIntegerExact()));
            buffer.append("บาท");

            if (0 == compound[1].compareTo(BigDecimal.ZERO)) {
                buffer.append("ถ้วน");
            } else {
                buffer.append(getNumberText(compound[1].toBigIntegerExact()));
                buffer.append("สตางค์");
            }
        }

        return buffer.toString();
    }

    private static String getNumberText(BigInteger number) {
        StringBuffer buffer = new StringBuffer();
        char[] digits = number.toString().toCharArray();

        for (int index = digits.length; index > 0; --index) {
            int digit = Integer.parseInt(String.valueOf(digits[digits.length
                    - index]));
            String digit_text = DIGIT_WORDS[digit];
            int scale_idx = ((1 < index)
                    ? ((index - 1) % 6)
                    : 6);

            if ((1 == scale_idx) && (2 == digit)) {
                digit_text = "ยี่";
            }

            if (1 == digit) {
                switch (scale_idx) {
                    case 0:
                    case 6:
                        buffer.append((index < digits.length)
                                ? "เอ็ด"
                                : digit_text);

                        break;

                    case 1:
                        break;

                    default:
                        buffer.append(digit_text);

                        break;
                }
            } else if (0 == digit) {
                if (0 == scale_idx) {
                    buffer.append(NUMBER_SCALES[scale_idx]);
                }

                continue;
            } else {
                buffer.append(digit_text);
            }

            buffer.append(NUMBER_SCALES[scale_idx]);
        }

        return buffer.toString();
    }

    public boolean equals(Object that) {
        return this.amount.equals(((BahtTextUtil) that).amount);
    }

    //--------------------------------------------------------------------------
    //English Language
    final private static String[] units = {"Zero", "One", "Two", "Three", "Four",
        "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
        "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    final private static String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty",
        "Sixty", "Seventy", "Eighty", "Ninety"};

    public static String convert(Integer i) {
        //
        if (i < 20) {
            return units[i];
        }
        if (i < 100) {
            return tens[i / 10] + ((i % 10 > 0) ? " " + convert(i % 10) : "");
        }
        if (i < 1000) {
            return units[i / 100] + " Hundred" + ((i % 100 > 0) ? " and " + convert(i % 100) : "");
        }
        if (i < 1000000) {
            return convert(i / 1000) + " Thousand " + ((i % 1000 > 0) ? " " + convert(i % 1000) : "");
        }
        return convert(i / 1000000) + " Million " + ((i % 1000000 > 0) ? " " + convert(i % 1000000) : "");
    }
}
