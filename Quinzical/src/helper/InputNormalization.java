package helper;

import java.util.Arrays;

/**
 * Helper class to normalize user input 
 */
public class InputNormalization {

	/**
	 * Perform input normalization on the users answer so that all non alphabetic 
	 * characters are removed and leading a/the/an are also removed
	 * @param text the user answer to normalize
	 * @return the normalized text
	 */
	public static String refineString(String text) {
		
		// Remove all symbols except /
		if (text.contains("/")) {
			;
		}else {
			text = text.replaceAll("\\p{P}", "");
		}

		//  Remove leading a/the/an
		String leading = text.split(" ")[0].trim();
		//Make sure removing the leading wont cause empty String
		if (text.split(" ").length>1 && (leading.equalsIgnoreCase("the") || leading.equalsIgnoreCase("a") || leading.equalsIgnoreCase("an"))) {
			text=text.replaceFirst(leading+" ", "").trim();
		}
		return text;
	}

	/**
	 * Replace macrons in the users answer with normal letters. This normalizes
	 * the user input so it can be evaluated correctly taking into account macrons
	 * @param text the user answer to normalize
	 * @return the normalized text
	 */
	public static String normal(String text) {
		
		String[] macrons = new String[] {"ā","ē","ī","ō","ū","Ā","Ē","Ī","Ō","Ū"};
		String[] normalLetter = new String[] {"a","e","i","o","u","a","e","i","o","u"};

		// replace macrons
		for(String i :  macrons){
			if (text.contains(i)) {
				text=text.replace(i, normalLetter[Arrays.asList(macrons).indexOf(i)]);
			}
		}
		return text.toLowerCase();
	};
}