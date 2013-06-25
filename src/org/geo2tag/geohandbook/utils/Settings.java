/**
 * 
 */
package org.geo2tag.geohandbook.utils;

/**
 * @author Mark Zaslavskiy
 *
 */
public class Settings {
	private static final int TAG_AGE_IN_YEARS = 5;
	private static final String SERVER_URL = "http://demo64.geo2tag.org/service";
	private static final double RADIUS = 300;
	private static final long UPDATE_INTERVAL = 30000;
	private static final boolean ENABLE_PREIODICAL_UPDATE = false;
	
	public static String getServerUrl() {
		return SERVER_URL;
	}
	
	public static double getRadius() {
		return RADIUS;
	}
	public static long getUpdateInterval() {
		return UPDATE_INTERVAL;
	}
	
	public static boolean isPeriodicalUpdateEnabled(){
		return ENABLE_PREIODICAL_UPDATE;
	}

	public static int getTagAgeInYears() {
		// TODO Auto-generated method stub
		return TAG_AGE_IN_YEARS;
	}
	
	public static String getDbName(){
		return StringConstants.HANDBOOK_NAME;
	}
}
