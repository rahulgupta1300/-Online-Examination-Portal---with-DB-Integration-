package com.github.tangyi.common.utils;

public class EnvUtils {

	public static String getValue(String key, String defaultVal) {
		try {
			String value = getValue(key);
			if (value == null) {
				return defaultVal;
			} else {
				return value;
			}
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static String getValue(String key) {
		// 系统-D
		String val = System.getProperty(key);
		if (val != null) {
			return val;
		}
		// 环境变量
		return System.getenv(key);
	}

	public static int getInt(String key, int defaultVal) {
		String val = getValue(key);
		return val == null ? defaultVal : Integer.parseInt(val);
	}

	public static long getLong(String key, long defaultVal) {
		String val = getValue(key);
		return val == null ? defaultVal : Long.parseLong(val);
	}
}
