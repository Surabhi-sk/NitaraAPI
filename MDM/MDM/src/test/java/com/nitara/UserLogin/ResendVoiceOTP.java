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

public class ResendVoiceOTP extends GenericBase{

	@Test(groups= {"Smoke"})
	public void resendVoiceOTP() throws Exception {


		String abstractname = prop.getProperty("ResendVoiceOTP");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);


		GenerateOtpForLogin otp = new GenerateOtpForLogin();
		otp.generateOtpForLogin(username.getString("username"));


		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone",username.getString("username"));

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());


		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		Assert.assertEquals("Voice OTP Resent.", message);

	}

}
