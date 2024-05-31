/*********************************************************************************
 * The MIT License
 *
 * Copyright (c) 2008 Virasak Dungsrikaew (virasak@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package com.arg.swu.report;

public class ThaiDisplayUtils {
    public static final char SARA_U = 'ุ';
    public static final char SARA_UU = 'ู';
    public static final char PHINTHU = 'ฺ';
    public static final char SARA_U_DOWN = '\uf718';
    public static final char SARA_UU_DOWN = '\uf719';
    public static final char PHINTHU_DOWN = '\uf71a';
    public static final char MAI_HAN_AKAT = 'ั';
    public static final char SARA_AM = 'ำ';
    public static final char SARA_I = 'ิ';
    public static final char SARA_Ii = 'ี';
    public static final char SARA_Ue = 'ึ';
    public static final char SARA_Uee = 'ื';
    public static final char MAI_TAI_KHU = '็';
    public static final char MAI_HAN_AKAT_LEFT_SHIFT = '\uf710';
    public static final char SARA_I_LEFT_SHIFT = '\uf701';
    public static final char SARA_Ii_LEFT_SHIFT = '\uf702';
    public static final char SARA_Ue_LEFT_SHIFT = '\uf703';
    public static final char SARA_Uee_LEFT_SHIFT = '\uf704';
    public static final char MAI_TAI_KHU_LEFT_SHIFT = '\uf712';
    public static final char MAI_EK = '่';
    public static final char MAI_THO = '้';
    public static final char MAI_TRI = '๊';
    public static final char MAI_CHATTAWA = '๋';
    public static final char THANTHAKHAT = '์';
    public static final char NIKHAHIT = 'ํ';
    public static final char MAI_EK_DOWN = '\uf70a';
    public static final char MAI_THO_DOWN = '\uf70b';
    public static final char MAI_TRI_DOWN = '\uf70c';
    public static final char MAI_CHATTAWA_DOWN = '\uf70d';
    public static final char THANTHAKHAT_DOWN = '\uf70e';
    public static final char MAI_EK_PULL_DOWN_AND_LEFT_SHIFT = '\uf705';
    public static final char MAI_THO_PULL_DOWN_AND_LEFT_SHIFT = '\uf706';
    public static final char MAI_TRI_PULL_DOWN_AND_LEFT_SHIFT = '\uf707';
    public static final char MAI_CHATTAWA_PULL_DOWN_AND_LEFT_SHIFT = '\uf708';
    public static final char THANTHAKHAT_PULL_DOWN_AND_LEFT_SHIFT = '\uf709';
    public static final char MAI_EK_LEFT_SHIFT = '\uf713';
    public static final char MAI_THO_LEFT_SHIFT = '\uf714';
    public static final char MAI_TRI_LEFT_SHIFT = '\uf715';
    public static final char MAI_CHATTAWA_LEFT_SHIFT = '\uf716';
    public static final char THANTHAKHAT_LEFT_SHIFT = '\uf717';
    public static final char NIKHAHIT_LEFT_SHIFT = '\uf711';
    public static final char PO_PLA = 'ป';
    public static final char FO_FA = 'ฝ';
    public static final char FO_FAN = 'ฟ';
    public static final char LO_CHULA = 'ฬ';
    public static final char THO_THAN = 'ฐ';
    public static final char YO_YING = 'ญ';
    public static final char DO_CHADA = 'ฎ';
    public static final char TO_PATAK = 'ฏ';
    public static final char RU = 'ฤ';
    public static final char LU = 'ฦ';
    public static final char THO_THAN_CUT_TAIL = '\uf700';
    public static final char YO_YING_CUT_TAIL = '\uf70f';
    public static final char SARA_AA = 'า';

    public ThaiDisplayUtils() {
    }

    public static String toDisplayString(String value) {
        return new String(toDisplayString(value.toCharArray()));
    }

    public static void toDisplayString(StringBuffer value) {
        char[] content = new char[value.length()];
        value.getChars(0, value.length(), content, 0);
        content = toDisplayString(content);
        value.setLength(0);
        value.append(content);
    }

    public static char[] toDisplayString(char[] content) {
        content = explodeSaraAm(content);
        int length = content.length;
        char pch = 'a';

        for (int i = 0; i < length; ++i) {
            char ch = content[i];
            if (isUpperLevel1(ch) && isUpTail(pch)) {
                content[i] = shiftLeft(ch);
            } else if (isUpperLevel2(ch)) {
                if (isLowerLevel(pch)) {
                    pch = content[i - 2];
                }

                if (isUpTail(pch)) {
                    content[i] = pullDownAndShiftLeft(ch);
                } else if (isLeftShiftUpperLevel1(pch)) {
                    content[i] = shiftLeft(ch);
                } else if (!isUpperLevel1(pch)) {
                    content[i] = pullDown(ch);
                }
            } else if (isLowerLevel(ch) && isDownTail(pch)) {
                char cutch = cutTail(pch);
                if (pch != cutch) {
                    content[i - 1] = cutch;
                } else {
                    content[i] = pullDown(ch);
                }
            }

            pch = content[i];
        }

        return content;
    }

    private static char[] explodeSaraAm(char[] content) {
        int count = countSaraAm(content);
        if (count == 0) {
            return (char[]) content.clone();
        } else {
            char[] newContent = new char[content.length + count];
            int j = 0;

            for (int i = 0; i < content.length; ++i) {
                char ch = content[i];
                if (i < content.length - 1 && content[i + 1] == 3635) {
                    if (isUpperLevel2(ch)) {
                        newContent[j++] = 3661;
                        newContent[j++] = ch;
                    } else {
                        newContent[j++] = ch;
                        newContent[j++] = 3661;
                    }
                } else if (ch == 3635) {
                    newContent[j++] = 3634;
                } else {
                    newContent[j++] = ch;
                }
            }

            return newContent;
        }
    }

    private static int countSaraAm(char[] content) {
        int count = 0;

        for (int i = 0; i < content.length; ++i) {
            if (content[i] == 3635) {
                ++count;
            }
        }

        return count;
    }

    private static boolean isUpTail(char ch) {
        return ch == 3611 || ch == 3613 || ch == 3615 || ch == 3628;
    }

    private static boolean isDownTail(char ch) {
        return ch == 3600 || ch == 3597 || ch == 3598 || ch == 3599 || ch == 3620 || ch == 3622;
    }

    private static boolean isUpperLevel1(char ch) {
        return ch == 3633 || ch == 3636 || ch == 3637 || ch == 3638 || ch == 3639 || ch == 3655 || ch == 3661;
    }

    private static boolean isLeftShiftUpperLevel1(char ch) {
        return ch == '\uf710' || ch == '\uf701' || ch == '\uf702' || ch == '\uf703' || ch == '\uf704' || ch == '\uf712' || ch == '\uf711';
    }

    private static boolean isUpperLevel2(char ch) {
        return ch == 3656 || ch == 3657 || ch == 3658 || ch == 3659 || ch == 3660;
    }

    public static boolean isLowerLevel(char ch) {
        return ch == 3640 || ch == 3641 || ch == 3642;
    }

    public static char pullDownAndShiftLeft(char ch) {
        switch (ch) {
            case 'ั':
                return '\uf710';
            case '่':
                return '\uf705';
            case '้':
                return '\uf706';
            case '๊':
                return '\uf707';
            case '๋':
                return '\uf708';
            case '์':
                return '\uf709';
            default:
                return ch;
        }
    }

    public static char shiftLeft(char ch) {
        switch (ch) {
            case 'ั':
                return '\uf710';
            case 'า':
            case 'ำ':
            case 'ุ':
            case 'ู':
            case 'ฺ':
            case '\u0e3b':
            case '\u0e3c':
            case '\u0e3d':
            case '\u0e3e':
            case '฿':
            case 'เ':
            case 'แ':
            case 'โ':
            case 'ใ':
            case 'ไ':
            case 'ๅ':
            case 'ๆ':
            case '์':
            default:
                return ch;
            case 'ิ':
                return '\uf701';
            case 'ี':
                return '\uf702';
            case 'ึ':
                return '\uf703';
            case 'ื':
                return '\uf704';
            case '็':
                return '\uf712';
            case '่':
                return '\uf713';
            case '้':
                return '\uf714';
            case '๊':
                return '\uf715';
            case '๋':
                return '\uf716';
            case 'ํ':
                return '\uf711';
        }
    }

    private static char pullDown(char ch) {
        switch (ch) {
            case 'ุ':
                return '\uf718';
            case 'ู':
                return '\uf719';
            case 'ฺ':
                return '\uf71a';
            case '\u0e3b':
            case '\u0e3c':
            case '\u0e3d':
            case '\u0e3e':
            case '฿':
            case 'เ':
            case 'แ':
            case 'โ':
            case 'ใ':
            case 'ไ':
            case 'ๅ':
            case 'ๆ':
            case '็':
            default:
                return ch;
            case '่':
                return '\uf70a';
            case '้':
                return '\uf70b';
            case '๊':
                return '\uf70c';
            case '๋':
                return '\uf70d';
            case '์':
                return '\uf70e';
        }
    }

    private static char cutTail(char ch) {
        switch (ch) {
            case 'ญ':
                return '\uf70f';
            case 'ฐ':
                return '\uf700';
            default:
                return ch;
        }
    }

}
