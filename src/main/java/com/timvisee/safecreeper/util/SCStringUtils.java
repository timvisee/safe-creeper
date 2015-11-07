package com.timvisee.safecreeper.util;

public class SCStringUtils {
	
	public static String ltrim(String s) {
	    int i = 0;
	    
	    while (i < s.length() && Character.isWhitespace(s.charAt(i)))
	        i++;
	    
	    return s.substring(i);
	}

	public static String rtrim(String s) {
	    int i = s.length()-1;
	    
	    while (i >= 0 && Character.isWhitespace(s.charAt(i)))
	        i--;
	    
	    return s.substring(0,i+1);
	}
}