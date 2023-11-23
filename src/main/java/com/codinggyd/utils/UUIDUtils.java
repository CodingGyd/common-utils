package com.codinggyd.utils;

import java.util.UUID;

public class UUIDUtils {
	public static String createUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	public static long createIDForLong() {
		return System.currentTimeMillis();
	}

}
