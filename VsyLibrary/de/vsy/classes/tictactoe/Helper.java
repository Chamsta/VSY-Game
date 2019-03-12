package de.vsy.classes.tictactoe;

import java.io.File;

/**
 * This class contains all static tools methods.
 */
public class Helper {
	/**
	 * Converts a file path to a local OS format.
	 * @param res The file path to convert.
	 * @return The correct file path for the current OS.
	 */
	public static String separatorsToSystem(String res) {
	    if (res==null) return null;
	    if (File.separatorChar=='\\') {
	        // From Windows to Linux/Mac
	        return res.replace('/', File.separatorChar);
	    } else {
	        // From Linux/Mac to Windows
	        return res.replace('\\', File.separatorChar);
	    }
	}
}
