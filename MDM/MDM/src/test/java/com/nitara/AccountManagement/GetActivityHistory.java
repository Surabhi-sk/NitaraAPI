package com.nitara.AccountManagement;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.response.Response;

public class GetActivityHistory extends GenericBase{

	NitaraService results = new NitaraService();
	
	
	@Test(groups= {"Smoke"})
	public void getActivityHistory() {
		
		System.out.println("Get Activity History");
		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetActivityHistory"),token);

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
		Assert.assertEquals(rs.getStatusCode(),200);

	}

}
