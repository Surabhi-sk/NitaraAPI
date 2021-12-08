package com.nitara.AccountManagement;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.response.Response;

public class GetAllActiveDevices extends GenericBase 
{
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void getAllActiveDevices() throws IOException 
	{ 
		System.out.println("Get All Active Devices");
		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetAllActiveDevices"),token);


		// Printing system response code
		System.out.println("Response code:" + rs.getStatusCode());

		// Printing system response
		System.out.println("Response:" + rs.getBody().prettyPeek().asString());


		if (rs.getStatusCode() == 200) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals( rs.getStatusCode(),200);


	}
}
