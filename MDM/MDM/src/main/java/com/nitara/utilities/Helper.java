

package com.nitara.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;






public class Helper {

	
	/* To get a date which x days after the current date.
	@Author : Surabhi
	*/

	@Test
	public String getDate(int x) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, x);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateValue = format.format(cal.getTime());
		return dateValue;
	}
	
	public String getDatex(int x) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, x);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateValue = format.format(cal.getTime());
		return dateValue;
	}


}
