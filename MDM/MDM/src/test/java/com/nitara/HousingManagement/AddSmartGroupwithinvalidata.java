package com.nitara.HousingManagement;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddSmartGroupwithinvalidata extends GenericBase
{
	NitaraService results = new NitaraService();
	
	@Test(groups= {"Smoke"})
	public void AddSmartGroup() throws IOException 
	{
		
		String abstractname = prop.getProperty("AddSmartGroup");
		RestAssured.baseURI = prop.getProperty("baseurl");
		
					
		
		RequestSpecification request = RestAssured.given();
		 HashMap<String, Object> queryParam = new HashMap<>();
		 queryParam.put("farmid", "d4ebdeeb-8b42-4ab8-9380-eda28177c182");
		 queryParam.put("shedName", "Ravishedscs");		 
		 queryParam.put("Groups", Arrays.asList("SmartGroup1s", "SmartGroup2s"));
		 
		 request.body(queryParam);
		 request.header("Content-Type", "application/json");
	     request.header("Authorization","Bearer " + token);
		 Response response = request.post(abstractname);
			 
			 System.out.println("Response body: " + response.body().prettyPeek().asString());
			
			
				// Printing system response code
				System.out.println("Response code:" + response.getStatusCode());

			
				if (response.getStatusCode() == 400) {
								// Printing test case result
					System.out.println("Status: Pass");
				} else {
					// Printing test case result
					System.out.println("Status: Fail");
				}
				// Comparing expected result with actual result
				Assert.assertEquals(400, response.getStatusCode());
				
	}			}

