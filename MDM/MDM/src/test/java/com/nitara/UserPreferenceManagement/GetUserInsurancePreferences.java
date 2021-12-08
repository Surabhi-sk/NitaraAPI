package com.nitara.UserPreferenceManagement;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.response.Response;

public class GetUserInsurancePreferences extends GenericBase{
	NitaraService results = new NitaraService();
	
	@Test(groups= {"Smoke"})
	public void getUserInsurancePreferences() {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetUserInsurancePreferences"),token);

		System.out.println("Response code:" + rs.getStatusCode());
		//System response
		System.out.println("Response:" + rs.getBody().prettyPrint().toString());

		// Printing test case result
		if (rs.getStatusCode() == 200) {			
			System.out.println("Status: Pass");
		} else {
			System.out.println("Status: Fail");
		}

		// Comparing expected result with actual result
		Assert.assertEquals(200, rs.getStatusCode());

	}
	
	public String getUserInsurancePreferences(String token) {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetUserInsurancePreferences"),token);

		System.out.println("Response code:" + rs.getStatusCode());
		//System response
		System.out.println("Response:" + rs.getBody().prettyPrint().toString());
		
		String response = rs.asString();
		// Comparing expected result with actual result
		Assert.assertEquals(200, rs.getStatusCode());
		
		return(response);

	}



}
