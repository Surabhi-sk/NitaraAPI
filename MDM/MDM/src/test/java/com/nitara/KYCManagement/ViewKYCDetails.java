package com.nitara.KYCManagement;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewKYCDetails extends GenericBase{

	@Test
	public void viewKYCDetails() throws Exception {
		String abstractname = prop.getProperty("ViewKYCDetails");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);

		Response response = request.body(username.toString())
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
		Assert.assertEquals("KYC Details.", message);

	}
}
