package com.nitara.UserRegistration;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class VerifyOTP extends GenericBase{

	@Test(groups= {"Smoke"})
	public void verifyOTP() {

/*String abstractname = prop.getProperty("VerifyOTP");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("otp", "1111"); // Cast	
		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Otp is verified.", message);

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
		Assert.assertEquals(200, response.getStatusCode());*/

	}

	public void verifyOTP(String otp, String Regtoken) {

		String abstractname = prop.getProperty("VerifyOTP");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("otp", otp); // Cast	
		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + Regtoken);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("OTP is verified.", message);

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

	}
}
