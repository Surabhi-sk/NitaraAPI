package com.nitara.WeightManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleRegistration.RegisterBullCattle;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteWeight extends GenericBase{

	@Test(groups= {"Smoke"})
	public void deleteWeight() throws Exception {
		String abstractname = prop.getProperty("DeleteWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		

		AddWeightDirect weightData = new AddWeightDirect();
		weightData.addWeightDirect(token); 
		
		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Weight Data removed successfully." );


	}
	
	@Test(groups= {"Regression"})
	public void deleteWeightNoWeightLogs() throws Exception {
		String abstractname = prop.getProperty("DeleteWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);
		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		//Validate message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"No Weight logs available.");
	}
}