package com.nitara.MasterData;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.response.Response;

public class GetMilkingSessionMasterData extends GenericBase {

	NitaraService results = new NitaraService();
	
	@Test(groups= {"Smoke"})
	public void GetMilkingSessionMasterDataInEnglish() throws IOException 
	
	{ 
				
		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetMilkingSessionMasterData"),token);
		
		
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
		Assert.assertEquals(200, rs.getStatusCode());
		
		

	}

	
	
	
}
