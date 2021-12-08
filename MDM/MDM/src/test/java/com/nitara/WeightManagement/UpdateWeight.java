/*
@Author : Surabhi
*/
package com.nitara.WeightManagement;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleProfile;
import com.nitara.CattleRegistration.RegisterBullCattle;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateWeight extends GenericBase{


	@Test(groups= {"Smoke"})
	public void updateWeight() throws Exception {
		String abstractname = prop.getProperty("UpdateWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		AddWeightDirect weightData = new AddWeightDirect();
		weightData.addWeightDirect(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateWeight","UpdateWeight",filepath);


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
		Assert.assertEquals(message,"Weight Data updated successfully.");

		int girth,length,weight;
		if(requestParams.getString("calculateBy").equals("Girth")) {
			girth = requestParams.getInt("girth");
			length = requestParams.getInt("length");
			weight = (girth*girth*length)/300 ;}
		else {
			weight = requestParams.getInt("weight");
		}


		//Assert the updated changes
		ViewCattleProfile cattleprofile = new ViewCattleProfile();
		String viewDetails = cattleprofile.viewCattleProfile(requestParams.getString("CattleId"),token);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleProfileData");

		Assert.assertEquals((cattleMilkingData.getInt("currentWeight")),weight);
		String date = requestParams.getString("dateOfWeight");
		Assert.assertEquals(cattleMilkingData.getString("currentWeightRecordedDate").contains(date), true);

		System.out.println("Weight data updated.");

	}


	@Test(groups= {"Regression"})
	public void updateWeightNoWeightLogs() throws Exception {
		String abstractname = prop.getProperty("UpdateWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateWeight","UpdateWeight",filepath);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		//Validate  message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"No Weight logs available.");

	}


	@Test(groups= {"Regression"})
	public void updateWeightMinWeight() throws Exception {
		String abstractname = prop.getProperty("UpdateWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		AddWeightDirect weightData = new AddWeightDirect();
		weightData.addWeightDirect(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateWeight","UpdateWeightMinimumWeight",filepath);

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
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray("CalculatedWeight");
		Assert.assertEquals(obj.getString(0),"Weight must be between 15 and 2000 kg.");

	}


	@Test(groups= {"Regression"})
	public void updateWeightMaxiWeight() throws Exception {
		String abstractname = prop.getProperty("UpdateWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		AddWeightDirect weightData = new AddWeightDirect();
		weightData.addWeightDirect(token);


		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateWeight","UpdateWeightMaximumWeight",filepath);

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
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray("CalculatedWeight");
		Assert.assertEquals(obj.getString(0),"Weight must be between 15 and 2000 kg.");

	}




	@Test(groups= {"Regression"})
	public void updateWeightBeforePreviouslyAddedDate() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateWeight","UpdateWeightBeforePreviouslyAddedDate",filepath);


		// First Request - AddWeight - Two days before current date(-2)
		Helper date = new Helper();
		requestParams.put("dateOfWeight", date.getDate(-2));

		String abstractname = prop.getProperty("AddWeight");
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		Assert.assertEquals(response.getStatusCode(),200);
		//Print response
		response.prettyPeek();
		
		//Second Request - AddWeight - One day before current date(-1)
		requestParams = data.readCase("UpdateWeight","UpdateWeightBeforePreviouslyAddedDate",filepath);
		requestParams.put("dateOfWeight", date.getDate(-1));

	
		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);
		
		Assert.assertEquals(response.getStatusCode(),200);
		//Print response
		response.prettyPeek();

		////Third Request - UpdateWeight - Three days before current date(-3)
		abstractname = prop.getProperty("UpdateWeight");
		requestParams = data.readCase("UpdateWeight","UpdateWeightBeforePreviouslyAddedDate",filepath);
		requestParams.put("dateOfWeight", date.getDate(-3));

		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate message
		Assert.assertEquals(message,"Cannot add a weight entry on a date before previously added weight entry. Please delete previous weight entry and then add new weight data.");

	}
	
	
	@Test(groups= {"Regression"})
	public void updateWeightToPreviouslyAddedDate() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateWeight","UpdateWeightToPreviouslyAddedDate",filepath);


		// First Request - AddWeight - Two days before current date(-2)
		Helper date = new Helper();
		requestParams.put("dateOfWeight", date.getDate(-2));

		String abstractname = prop.getProperty("AddWeight");
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		Assert.assertEquals(response.getStatusCode(),200);
		//Print response
		response.prettyPeek();
		
		//Second Request - AddWeight - One day before current date(-1)
		requestParams = data.readCase("UpdateWeight","UpdateWeightToPreviouslyAddedDate",filepath);
		requestParams.put("dateOfWeight", date.getDate(-1));

		
		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);
		
		Assert.assertEquals(response.getStatusCode(),200);
		//Print response
		response.prettyPeek();

		//Third Request - AddWeight - Two days before current date(-2)
		abstractname = prop.getProperty("UpdateWeight");
		requestParams = data.readCase("UpdateWeight","UpdateWeightToPreviouslyAddedDate",filepath);
		requestParams.put("dateOfWeight", date.getDate(-2));

		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validatemessage
		Assert.assertEquals(message,"Cannot add more than one weight entry on one date.");
	}


	/*@Test(groups= {"Regression"})
	public void updateWeightMandatoryFields() throws Exception {
		String abstractname = prop.getProperty("UpdateWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();


		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);
		JSONObject requestParams = new JSONObject();

		requestParams.put("calculateBy","Girth");


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(), 400);

		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray( "Girth");
		Assert.assertEquals(obj.getString(0),"Girth is required due to CalculateBy being equal to Girth");

		JSONArray obj1 = error.getJSONArray("Length");
		Assert.assertEquals(obj1.getString(0),"Length is required due to CalculateBy being equal to Girth");

	}*/







}
