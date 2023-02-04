package com.kaua.finances.domain.utils;

import java.util.UUID;

public final class UuidUtils {

    private UuidUtils() {}

    public static String unique() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
