package com.nitara.AccountManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateUserProfile extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateUserProfile() {
		
		System.out.println("Update User Profile");
		String abstractname = prop.getProperty("UpdateUserProfile");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("name", "Farmer02");

		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				header("Authorization","Bearer " + token).post(abstractname);

		response.prettyPeek();

		// Comparing expected result with actual result
		Assert.assertEquals(response.getStatusCode(),200);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals("Profile updated successfully.", message);
	}
}
