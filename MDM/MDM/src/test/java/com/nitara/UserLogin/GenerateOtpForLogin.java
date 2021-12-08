package com.nitara.UserLogin;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GenerateOtpForLogin extends GenericBase{

	@Test(groups= {"Smoke"})
	public void generateOtpForLogin() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("GenerateOtpForLogin");
		String filepath = prop.getProperty("AccountManagement");
		
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "username", filepath);
		
		String username = user.getString("username");
		

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("phone", username);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();
		String message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("OTP Generated.", message);

	}

	public String generateOtpForLogin(String username) {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("GenerateOtpForLogin");

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", username);
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.post(abstractname);
		int  statusCode = response.getStatusCode();

		System.out.println("The status code recieved: " + statusCode);

		System.out.println("Response body: " + response.body().prettyPeek().asString());


		String jsonString = response.asString();
		String message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("OTP Generated.", message);
		String OTP = JsonPath.from(jsonString).get("otp");


		return (OTP);
	}

}
