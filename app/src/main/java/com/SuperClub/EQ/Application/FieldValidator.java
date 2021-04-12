package com.SuperClub.EQ.Application;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.SuperClub.EQ.Application.ValidationResult.ResultType.OK;

public class FieldValidator {

    private static final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static int countSymbols(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            Character temp = str.charAt(i);

            if (Character.isLetter(temp))
                sum++;
        }
        return sum;
    }


    public static ValidationResult validateStartEnd(Date start, Date end) {
        ValidationResult result = new ValidationResult();
        if (end.getTime() <= start.getTime()) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "End date must be greater than start date";
            return result;
        }
        result.type = OK;
        return result;
    }


    public static ValidationResult validateQueueTitle(String title) {
        ValidationResult result = new ValidationResult();
        if (countSymbols(title) < 5) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Title must contain at least 5 symbols";
            return result;
        }

        if (title.length() > 30) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Title is too long";
            return result;
        }

        result.type = OK;
        return result;
    }



    public static ValidationResult validateQueueDescription(String description) {
        ValidationResult result = new ValidationResult();
        if (countSymbols(description) < 20) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Description must contain at least 20 symbols";
            return result;
        }

        if (description.length() > 150) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Description is too long";
            return result;
        }

        result.type = OK;
        return result;
    }

    public static ValidationResult validatePassword(String password) {
        ValidationResult result = new ValidationResult();
        if (password.length() < 4) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Password must contain at least 4 symbols";
            return result;
        }

        if (password.length() > 14) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Password is too long";
            return result;
        }

        result.type = OK;
        return result;
    }

    public static ValidationResult validateName(String password) {
        ValidationResult result = new ValidationResult();
        if (password.length() < 3) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Name must contain at least 3 symbols";
            return result;
        }

        if (password.length() > 20) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Name is too long";
            return result;
        }

        result.type = OK;
        return result;
    }

    public static ValidationResult validateEmail(String email) {
        ValidationResult result = new ValidationResult();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            result.type = ValidationResult.ResultType.ERROR;
            result.message = "Invalid email";
            return result;
        }

        result.type = OK;
        return result;
    }


}
