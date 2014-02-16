package com.timvisee.safecreeper.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.map.MinecraftFont;

public class SCChatUtils {
	
	/** @var CHAT_BOX_WIDTH Default width in pixels of the client chat box */
	private static final int CHAT_BOX_WIDTH = 320;
	/** @var CHAR_DEFAULT_WIDTH Default width to use for an unknown character */
	private static final int CHAR_DEFAULT_WIDTH = 4;
	
	/**
	 * Get the height of a Minecraft font character in pixels
	 * @return Character height
	 */
	public static int getFontHeight() {
		return MinecraftFont.Font.getHeight();
	}
	
	/**
	 * Get the width of a Minecraft font character in pixels
	 * @param c Character to get the width from
	 * @return Character width. Might vary a bit when an unknown character is applied.
	 */
	public static int getCharWidth(char c) {
		try {
			return getStringWidth(String.valueOf(c));
		} catch(Exception ex) { }
		
		return 0;
	}
	
	/**
	 * Get the width of a string using the Minecraft font in pixels
	 * @param s String to get the width from
	 * @return String width. Might vary a bit when unknown characters are applied.
	 */
	public static int getStringWidth(String s) {
		return getStringWidth(s, true);
	}
	
	/**
	 * Get the width of a string using the Minecraft font in pixels
	 * @param s String to get the width from
	 * @param multiline True to calculate the width while taking multiline strings in account,
	 * False to calculate the width as if the whole string would fit on a single line
	 * @return String width. Might vary a bit when unknown characters are applied.
	 */
	public static int getStringWidth(String s, boolean multiline) {
		return getStringWidth(s, multiline, getChatBoxWidth());
	}
	
	/**
	 * Get the width of a string using the Minecraft font in pixels
	 * @param s String to get the width from
	 * @param multiline True to calculate the width while taking multiline strings in account,
	 * False to calculate the width as if the whole string would fit on a single line
	 * @param maxWidth Maximum width of each line
	 * @return String width. Might vary a bit when unknown characters are applied.
	 */
	public static int getStringWidth(String s, boolean multiline, int maxWidth) {
		// Filter color characters from the string (due to NPE's)
		s = ChatColor.stripColor(s);
		
		// Split the string into multiple lines if the multiline param is set to true,
		// put the whole string on a single line if this feature is disabled
		List<String> lines = new ArrayList<String>();
		if(multiline) {
			lines = splitStringIntoLines(s, maxWidth);
		} else
			lines.add(s);
		
		// Get the whole last line
		String lastLine = lines.get(lines.size() - 1);
		
		// Make sure this method won't cause errors due to unknown characters
		try {
			// Get the string width for each character
			int w = MinecraftFont.Font.getWidth(lastLine);
			
			// Add the space between each character
			if(lastLine.length() > 1)
				w += lastLine.length();
			
			// Handle wrong space width
			w += StringUtils.countMatches(lastLine, " ");
			
			// Add the other lines to the sum if the string was multiline
			if(multiline)
				w += (lines.size() - 1) * getChatBoxWidth();
			
			// Return the result
			return w;
			
		} catch(Exception ex) {
			// TODO: Remove this line!
			ex.printStackTrace();
		}
		
		// Above caused an error due to unknown characters, find the width manually
		// String width
		int w = 0;
		
		// Try to gather the width for each char
		for(int i = 0; i < lastLine.length(); i++) {
			try {
				w += MinecraftFont.Font.getWidth(lastLine.substring(i, i + 1));
			} catch(Exception ex) {
				// Unable to get char width, use default width
				w += CHAR_DEFAULT_WIDTH;
			}
		}
		
		// Add the space between each character
		if(lastLine.length() > 1)
			w += lastLine.length();
		
		// Handle wrong space width
		w += StringUtils.countMatches(lastLine, " ");
		
		// Add the other lines to the sum if the string was multiline
		if(multiline)
			w += (lines.size() - 1) * getChatBoxWidth();
		
		// Return the calculated string width
		return w;
	}
	
	/**
	 * Get the amount of lines a string would consume
	 * @param s String to check for
	 * @param maxWidth Maximum width of each line
	 * @return Amount of lines a string would consume
	 */
	public static int getStringHeight(String s) {
		return getStringHeight(s, getChatBoxWidth());
	}
	
	/**
	 * Get the amount of lines a string would consume
	 * @param s String to check for
	 * @param maxWidth Maximum width of each line
	 * @return Amount of lines a string would consume
	 */
	public static int getStringHeight(String s, int maxWidth) {
		return (int) Math.ceil(((double) getStringWidth(s, true)) / ((double) maxWidth));
	}
	
	/**
	 * Split a string into different lines
	 * @param s String to split into lines
	 * @param maxWidth Maximum width of each line
	 * @return List of lines the string was splitted into
	 */
	public static List<String> splitStringIntoLines(String s, int maxWidth) {
		List<String> lines = new ArrayList<String>();
		
		int lastChar = -1;
		while(lastChar < s.length() - 1) {
			for(int i = s.length() - 1; i >= lastChar + 1; i--) {
				// Split the long word if no spaces are placed on a line
				if(i == lastChar + 1) {
					for(int j = s.length() - 1; j >= lastChar; j--) {
						// Get the current substring
						String subStr = s.substring(lastChar + 1, j + 1);
						
						// Check whether this substring is compatible
						if(getStringWidth(subStr, false) <= maxWidth) {
							lastChar = j;
							lines.add(subStr);
							break;
						}
					}
				}
				
				// Skip to each last character of a word, 
				if(i + 1 < s.length())
					if(!Character.isWhitespace(s.charAt(i + 1)))
						continue;
				
				// Get the current substring
				String subStr = s.substring(lastChar + 1, i + 1);
				
				// Check whether this substring is compatible
				if(getStringWidth(subStr, false) <= maxWidth) {
					lines.add(SCStringUtils.rtrim(subStr));
					lastChar = i;
				}
			}
		}
		
		// Return all lines
		return lines;
	}
	
	/**
	 * Get the default client chat box width
	 * @return Default chat box width
	 */
	public static int getChatBoxWidth() {
		return CHAT_BOX_WIDTH;
	}
	
	/**
	 * Fit a string into the default chat box space
	 * @param s String to fit
	 * @return Fitted string
	 */
	public static String fitString(String s) {
		return fitString(s, "...");
	}
	
	/**
	 * Fit a string into the default chat box space
	 * @param s String to fit
	 * @param append Append string
	 * @return Fitted string
	 */
	public static String fitString(String s, String append) {
		return fitString(s, append, getChatBoxWidth());
	}
	
	/**
	 * Fit a string into a specified space
	 * @param s String to fit
	 * @param append Append string
	 * @param maxWidth Maximum width of the string
	 * @return Fitted string
	 */
	public static String fitString(String s, String append, int maxWidth) {
		// Return the string when the width is already bellow the max width
		if(getStringWidth(s) <= maxWidth)
			return s;
		
		// Fit the string and append the param string
		int appendWidth = getStringWidth(append);
		for(int i = s.length() - 1; i >= 0; i--) {
			// Skip white spaces
			if(s.substring(i, i + 1).trim().length() == 0)
				continue;
			
			// Check whether this substring had a width that fits
			if(getStringWidth(s.substring(0, i + 1)) <= maxWidth - appendWidth)
				return s.substring(0, i + 1) + append;
		}
		
		// Return the fitted string
		return s;
	}
	
	/**
	 * Fill up a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param append String to use to fill up
	 * @return Filled string
	 */
	public static String fillLine(String s, String append) {
		return fillLine(s, append, getChatBoxWidth());
	}
	
	/**
	 * Fill up a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param append String to use to fill up
	 * @param maxWidth Desired filled string width
	 * @return Filled string
	 */
	public static String fillLine(String s, String append, int maxWidth) {
		return fillLine(s, append, append, maxWidth);
	}

	/**
	 * Fill up a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param prepend String to use to fill up the beginning
	 * @param append String to use to fill up to end
	 * @return Filled string
	 */
	public static String fillLine(String s, String prepend, String append) {
		return fillLine(s, prepend, append, getChatBoxWidth());
	}

	/**
	 * Fill up a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param prepend String to use to fill up the beginning
	 * @param append String to use to fill up to end
	 * @param maxWidth Desired filled string width
	 * @return Filled string
	 */
	public static String fillLine(String s, String prepend, String append, int maxWidth) {
		// Return the string when the width is already on or over the max width
		if(getStringWidth(s) >= maxWidth)
			return s;
		
		// Fill up the string
		while(true) {
			// Append one char
			if(getStringWidth(s + append) >= maxWidth)
				return s;
			else
				s = s + append;
			
			// Prepend one char
			if(getStringWidth(prepend + s) >= maxWidth)
				return s;
			else
				s = prepend + s;
		}
	}

	/**
	 * Fill up the end of a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param append String to use to fill up to end
	 * @return Filled string
	 */
	public static String fillLineEnd(String s, String append) {
		return fillLineEnd(s, append, getChatBoxWidth());
	}

	/**
	 * Fill up the end of a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param append String to use to fill up to end
	 * @param maxWidth Desired filled string width
	 * @return Filled string
	 */
	public static String fillLineEnd(String s, String append, int maxWidth) {
		return fillLine(s, "", append, maxWidth);
	}

	/**
	 * Fill up the beginning of a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param prepend String to use to fill up the beginning
	 * @return Filled string
	 */
	public static String fillLineBegin(String s, String prepend) {
		return fillLine(s, prepend, "", getChatBoxWidth());
	}

	/**
	 * Fill up the beginning of a string to give it the same width as the client chat box
	 * @param s String to fill
	 * @param prepend String to use to fill up the beginning
	 * @param maxWidth Desired filled string width
	 * @return Filled string
	 */
	public static String fillLineBegin(String s, String prepend, int maxWidth) {
		return fillLine(s, prepend, "", maxWidth);
	}
	
	/**
	 * Get the string that allows a blank line to be send
	 */
	public static String getBlankLineString() {
		return ChatColor.RESET + " ";
	}
}
