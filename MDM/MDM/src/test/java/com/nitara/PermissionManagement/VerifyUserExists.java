package com.nitara.PermissionManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class VerifyUserExists extends GenericBase{


	@Test(groups= {"Smoke"})
	public void verifyUserExists() throws Exception {

		String abstractname = prop.getProperty("VerifyUserExists");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);

		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("CountryCode", "+91"); 
		//requestParams.put("Phone", username.getString("username"));// Cast
		requestParams.put("Phone", username.getString("username"));

		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				header("Authorization","Bearer " + token).post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User Exists.", message);

	}

	public void verifyUserExists(String countryCode, String phone, String token) {

		String abstractname = prop.getProperty("VerifyUserExists");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("CountryCode", countryCode); 
		requestParams.put("Phone", phone);// Cast

		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				header("Authorization","Bearer " + token).post(abstractname);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User Exists.", message);


	}


}
