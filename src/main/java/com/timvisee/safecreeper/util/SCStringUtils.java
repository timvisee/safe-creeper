package com.timvisee.safecreeper.util;

public class SCStringUtils {

	/**
	 * Trim whitespaces on the left of a string.
	 *
	 * @param s String to trim.
	 *
	 * @return Result string.
     */
	public static String ltrim(String s) {
	    int i = 0;
	    
	    while (i < s.length() && Character.isWhitespace(s.charAt(i)))
	        i++;
	    
	    return s.substring(i);
	}

	/**
	 * Trim whitespaces on the left of a string.
	 *
	 * @param s String to trim.
	 *
	 * @return Result string.
	 */
	public static String rtrim(String s) {
	    int i = s.length()-1;
	    
	    while (i >= 0 && Character.isWhitespace(s.charAt(i)))
	        i--;
	    
	    return s.substring(0,i+1);
	}
}