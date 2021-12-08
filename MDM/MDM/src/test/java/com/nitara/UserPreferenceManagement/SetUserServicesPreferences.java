package com.nitara.UserPreferenceManagement;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SetUserServicesPreferences extends GenericBase{

	@Test(groups= {"Smoke"})
	public void SetServicesPreferences() {

		String abstractname = prop.getProperty("SetUserServicesPreferences");
		RestAssured.baseURI = prop.getProperty("baseurl"); 

		RequestSpecification request = RestAssured.given();

		ArrayList<Object> listdata = new ArrayList<Object>();  

		// Vet
		JSONObject arrayOne = new JSONObject();
		arrayOne.put("service", "Vet"); // "AI" or "Vet"
		arrayOne.put("isServiceOpted",false); // Boolean

		JSONObject arrayTwo = new JSONObject();
		arrayTwo.put("service", "AI"); // "AI" or "Vet"
		arrayTwo.put("isServiceOpted",true); // Boolean

		listdata.add(arrayOne);
		listdata.add(arrayTwo);
		request.body(listdata.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("External Service Preferences Updated", message);

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
		Assert.assertEquals(200, response.getStatusCode());

		//Assert the set details
		GetUserServicesPreferences pref = new GetUserServicesPreferences();
		String res = pref.getUserServicesPreferences(token);
		
		JSONObject obj = new JSONObject(res);
		JSONArray services = obj.getJSONArray("externalServicePreferences");
		
		for(int i=0;i<services.length();i++) {
			JSONObject activityObj = services.getJSONObject(i);
			if(activityObj.get("service").equals("Vet")) {
				Assert.assertEquals(false,activityObj.get("isServiceOpted"));
				continue;
			}

			if(activityObj.get("service").equals("Deworming")) {
				Assert.assertEquals(true,activityObj.get("isServiceOpted"));
				continue;
			}

		}

		
		System.out.println("User Service Preferences set.");




	}


}
