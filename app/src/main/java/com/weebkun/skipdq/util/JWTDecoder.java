package com.weebkun.skipdq.util;

import java.util.Base64;

public class JWTDecoder {

    public static String decode(String token) {
        // split into parts
        String[] parts = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(parts[1]));
    }
}
