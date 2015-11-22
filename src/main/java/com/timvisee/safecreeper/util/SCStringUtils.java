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
		// The base index, from the beginning
	    int i = 0;

		// Increase the index until a non-whitespace character is reached
	    while(i < s.length() && Character.isWhitespace(s.charAt(i)))
	        i++;

		// Get the substring from the non-whitespace character
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
		// The base index, from the end
	    int i = s.length() - 1;

		// Decrease the index until a non-whitespace character is reached
	    while(i >= 0 && Character.isWhitespace(s.charAt(i)))
	        i--;

		// Get the substring until the non-whitespace character
	    return s.substring(0, i + 1);
	}
}