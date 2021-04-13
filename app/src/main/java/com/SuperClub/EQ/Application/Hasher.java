package com.SuperClub.EQ.Application;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Hasher {
    private static int base = 36;
    private static int bitCoef = 9;
    private static int offset = 1023;
    private static int multCoef = 3;



    public static String getQueueCode(int qid) {
        qid = (qid << bitCoef) * multCoef + offset;
        return Integer.toString(qid, base);
    }

}

