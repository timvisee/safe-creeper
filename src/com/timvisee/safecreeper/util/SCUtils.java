package com.timvisee.safecreeper.util;

public class SCUtils {
	
	/**
	 * Check if a string could be cast to an integer
	 * @param str String to check
	 * @return False if not
	 */
	public static boolean isInt(String str) {
		try {
			@SuppressWarnings("unused")
			int i = Integer.parseInt(str);
	        return true;
        } catch (NumberFormatException ex) { }
		return false;
	}
}
