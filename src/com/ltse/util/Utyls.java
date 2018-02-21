/**
 * 
 */
package com.ltse.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author SWATI KHARKAR
 *
 */
public class Utyls {

	/**
	 * 
	 */
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy HH:mm:ss");
	
	public static Date convertStringToDate(String strDate) {
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static boolean isDateWithinRange (String startDate, String endDate) {
		Date start = convertStringToDate(startDate);
		Date end = convertStringToDate(endDate);
		long diffInMillies = Math.abs(end.getTime() - start.getTime());
		//System.out.println(startDate + " : " + diffInMillies + " : " + endDate);
	    return (TimeUnit.MILLISECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS) <= Constants.DIFF) ?
	    		Boolean.TRUE : Boolean.FALSE ;
	    	
	}

}
