package mkrishtop.database;

import java.util.HashMap;

public class Converter {
	public static String getWhereString(HashMap<String, String> filter) {
		if (filter.size() == 0) return "";
		String whereString = " where ";
		
		int c = 0;
		for (String key : filter.keySet()) {
			c++;
			String newKey = key.replaceFirst("<s>", "");
			String apostrophe = (newKey.equals(key) ? "" : "'"); 
			whereString += newKey + "=" + apostrophe + filter.get(key) + apostrophe + (c != filter.size() ? " and " : "");
		}
		
		return whereString;
	}
	
	public static String getSetString(HashMap<String, String> props) {
		if (props.size() == 0) return "";
		String whereString = " set ";
		
		int c = 0;
		for (String key : props.keySet()) {
			c++;
			String newKey = key.replaceFirst("<s>", "");
			String apostrophe = (newKey.equals(key) ? "" : "'"); 
			whereString += newKey + "=" + apostrophe + props.get(key) + apostrophe + (c != props.size() ? ", " : "");
		}
		
		return whereString;
	}
}
