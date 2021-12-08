package com.nitara.WeightManagement;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewWeightData extends GenericBase{

	@Test(groups= {"Smoke"})
	public void viewWeightData() throws IOException {

		String abstractname = prop.getProperty("ViewWeightData");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");


		RequestSpecification request = RestAssured.given();
		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readData("ViewWeightData",filepath);

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

		Assert.assertEquals("Weight Details", message);
	}

	@Test(groups= {"Regression"})
	public void viewWeightManadatoryFields() throws IOException {

		String abstractname = prop.getProperty("ViewWeightData");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");


		RequestSpecification request = RestAssured.given();


		JSONObject requestParams = new JSONObject();

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray( "EndDate");
		Assert.assertEquals(obj.getString(0),"The EndDate field is required.");

		obj = error.getJSONArray( "CattleId");
		Assert.assertEquals(obj.getString(0),"The CattleId field is required.");

		obj = error.getJSONArray( "StartDate");
		Assert.assertEquals(obj.getString(0),"The StartDate field is required.");
	}

}
