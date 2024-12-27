/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ledin
 */
public class Validated {

    public static Boolean isEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static Boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        String repex = "^\\w+@\\w+(\\.\\w+){1,2}$";
        return email.matches(repex);
    }

    public static Boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }

        String repex = "^[0-9]{10}$";
        return phoneNumber.matches(repex);
    }

    public static Boolean isPassword(char[] pass) {
        String passString = new String(pass).trim();
        return passString.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{6,15}$");
    }

    public static boolean checkEmpty(String... str) {
        for (String string : str) {
            if (!isEmpty(string)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericDouble(String numeric) {
        if (numeric == null) {
            return false;
        }
        String regex = "^[+]?(?!0$)[0-9]*\\.?[0-9]+$";
        return numeric.matches(regex);
    }

    public static boolean isNumericInt(String numeric) {
        if (numeric == null) {
            return false;
        }
        String regex = "^[+]?[0-9]+$";
        return numeric.matches(regex);
    }

    public static boolean validateUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{4,13}$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
