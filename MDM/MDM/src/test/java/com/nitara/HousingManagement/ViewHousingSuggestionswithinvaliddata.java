package com.nitara.HousingManagement;

import java.io.IOException;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewHousingSuggestionswithinvaliddata extends GenericBase
{
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void ViewHousingSuggestions() throws IOException 
	{
		
		String abstractname = prop.getProperty("ViewHousingSuggestions");
		RestAssured.baseURI = prop.getProperty("baseurl");
		
		
		RequestSpecification request = RestAssured.given();
		 
		 JSONObject requestParams = new JSONObject();
		 requestParams.put("farmId", "d4ebdeeb-8b42-4ab8-9380-eda28177c182e"); // Cast
		 request.body(requestParams.toString());
		 request.header("Content-Type", "application/json");
	     request.header("Authorization","Bearer " + token);
		 Response response = request.post(abstractname);
			 
			 System.out.println("Response body: " + response.body().prettyPeek().asString());

			
			
				
				// Printing system response code
				System.out.println("Response code:" + response.getStatusCode());

			
				if (response.getStatusCode() == 400) {
								// Printing test case result
					System.out.println("Status: Pass");
				} 
				else
				{
					// Printing test case result
					System.out.println("Status: Fail");
				}
				// Comparing expected result with actual result
				Assert.assertEquals(400, response.getStatusCode());
				
	}			}
