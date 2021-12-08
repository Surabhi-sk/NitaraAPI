package com.nitara.UserLogin;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Logout extends GenericBase{


	NitaraService results = new NitaraService();
	
	@Test(groups= {"Smoke"})
	public void logout() {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("Logout"),token);

		System.out.println("Response code:" + rs.getStatusCode());
		//System response
		System.out.println("Response:" + rs.body().asString());

		// Printing test case result
		if (rs.getStatusCode() == 200) {			
			System.out.println("Status: Pass");
		} else {
			System.out.println("Status: Fail");
		}

		// Comparing expected result with actual result
		Assert.assertEquals(200, rs.getStatusCode());

		String responseString = rs.asString();
		String  message = JsonPath.from(responseString).get("message");
		//Validate success message
		Assert.assertEquals("Logged out successfully.", message);

	}





}
