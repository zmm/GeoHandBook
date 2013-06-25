/**
 * 
 */
package org.geo2tag.geohandbook.utils;

import org.geo2tag.geohandbook.GeoHandBookMidlet;


/**
 * @author Mark Zaslavskiy
 *
 */
public  class GHBState {
	private static String m_authToken = null;
	private static GeoHandBookMidlet m_midlet = null;
	
	
	public static String getAuthToken() {
		return m_authToken;
	}


	public static void setAuthToken(String authToken) {
		GHBState.m_authToken = authToken;
	}

	public static GeoHandBookMidlet getMidlet() {
		return m_midlet;
	}

	public static void setMidlet(GeoHandBookMidlet midlet) {
		GHBState.m_midlet = midlet;
	}
	
	
	
}
