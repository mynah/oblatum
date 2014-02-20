package org.mynah.oblatum.util;

import static org.mynah.oblatum.util.Constants.UNDERLINE;

public class CamelCaseUtils {


    /**
     * Convert a column name with underscores to the corresponding property name using "camel case".  A name
     * like "customer_number" would match a "customerNumber" property name.
     *
     * @param name the column name to be converted
     * @return the name using "camel case"
     */
    public static String convertUnderscoreNameToPropertyName(String name) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && name.length() > 0) {
            for (int i = 0; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals(UNDERLINE)) {
                    nextIsUpper = true;
                } else {
                    if (nextIsUpper) {
                        result.append(s.toUpperCase());
                        nextIsUpper = false;
                    } else {
                        result.append(s.toLowerCase());
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding underscore.
     *
     * @param name the string containing original name
     * @return the converted name
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                String slc = s.toLowerCase();
                if (!s.equals(slc)) {
                    result.append(UNDERLINE).append(slc);
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }
}
