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

public class LoginWithOtp extends GenericBase{

	@Test(groups= {"Smoke"})
	public void loginWithOtp() throws Exception {


		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("LoginWithOtp");
		String filepath = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);


		GenerateOtpForLogin otp = new GenerateOtpForLogin();
		String OTP = otp.generateOtpForLogin(username.getString("username"));

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("OTP",OTP); // Cast
		requestParams.put("userName", username.getString("username"));
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());


		String jsonString = response.asString();
		String message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Logged in successfully.", message);

	}

}
