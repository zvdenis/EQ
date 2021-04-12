package com.SuperClub.EQ.Application;

public class ValidationResult {
    public enum ResultType {
        OK,
        ERROR
    }

    public ResultType type;
    public String message;
}
