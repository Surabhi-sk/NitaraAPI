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

public class ResendOTP extends GenericBase{

	@Test(groups= {"Smoke"})
	public void resendOTP() throws Exception {

		String abstractname = prop.getProperty("ResendOTP");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);

		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", username.getString("username"));


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("OTP Resent.", message);
	}

}
