package com.nitara.DiseasePredictor;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.nitara.UserPreferenceManagement.GetUserServicesPreferences;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*User Story :Disease Predictor 678
@Author : Ravi Teja
*/

public class GetCategories extends GenericBase{
	
	NitaraService results = new NitaraService();
	
	

	@Test(groups= {"Smoke"})
	public void getCategories() {

		
		
		String abstractname = prop.getProperty("GetCategories");
		RestAssured.baseURI = prop.getProperty("baseurl"); 

		RequestSpecification request = RestAssured.given();

		ArrayList<Object> listdata = new ArrayList<Object>();  

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());
		if (response.getStatusCode() == 200) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals("Category Data.", message);

	}

}
