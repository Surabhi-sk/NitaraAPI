package com.nitara.UserRegistration;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SetPasswordForUser extends GenericBase{

	@Test(groups= {"Smoke"})
	public void setPasswordForUser() {

		String abstractname = prop.getProperty("SetPasswordForUser");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("UserName", "9"); // Cast	
		requestParams.put("Password", "password@123");
		requestParams.put("ConfirmPassword", "password@123");
		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Password created successfully.", message);

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

	public void setPasswordForUser(String username, String token) throws Exception {

		String abstractname = prop.getProperty("SetPasswordForUser");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("UserName", username); // Cast	
		requestParams.put("Password", "password@123");
		requestParams.put("ConfirmPassword", "password@123");
		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Password created successfully.", message);

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

		//update password in excel
		ExcelUtils exceldata = new ExcelUtils();
		String filepath= ".\\src\\main\\java\\com\\nitara\\testdata\\AccountManagement.xlsx";
		

	}
}