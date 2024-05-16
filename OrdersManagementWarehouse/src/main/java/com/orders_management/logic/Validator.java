package com.orders_management.logic;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
public class Validator{

    public static boolean isPositiveNumber(BigDecimal number) {
        return number.compareTo(new BigDecimal("0")) > 0;
    }

    public static boolean isNonNegative(int number) {
        return number >= 0;
    }

    public static boolean containsOnlyLetters(String str) {
        return str.matches("[a-zA-Z]+");
    }

    public static boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static boolean isAge(int number)
    {
        return number>17 &&number<100;
    }

    public static boolean isDateBeforeToday(LocalDateTime date) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return date.compareTo(today) >= 0;
    }

    public static boolean containsOnlyLettersAndSpaces(String str) {
        return str.matches("[a-zA-Z ]+");
    }




}
