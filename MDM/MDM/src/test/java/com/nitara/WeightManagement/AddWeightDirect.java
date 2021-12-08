package com.nitara.WeightManagement;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleProfile;
import com.nitara.CattleRegistration.RegisterBullCattle;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class AddWeightDirect extends GenericBase{


	@Test(groups= {"Smoke"})
	public void addWeightDirect() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given(); 
		
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(usertoken);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_Direct",filepath);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Weight Data added successfully.", message);

		int girth,length,weight;
		Assert.assertEquals(200, response.getStatusCode());
		if(requestParams.getString("calculateBy").equals("Girth")) {
			girth = requestParams.getInt("girth");
			length = requestParams.getInt("length");
			weight = (girth*girth*length)/300 ;}
		else {
			weight = requestParams.getInt("weight");
		}

		//Assert added details
		ViewCattleProfile cattleprofile = new ViewCattleProfile();
		String viewDetails = cattleprofile.viewCattleProfile(requestParams.getString("CattleId"),usertoken);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleProfileData");

		Assert.assertEquals(weight,(cattleMilkingData.getInt("currentWeight")));
		String date = requestParams.getString("dateOfWeight");
		Assert.assertEquals(cattleMilkingData.getString("currentWeightRecordedDate").contains(date), true);

		System.out.println("Weight data added.");

	}
	
	
	public void addWeightDirect(String token) throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

RequestSpecification request = RestAssured.given(); 
		
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(usertoken);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_Direct",filepath);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals( message,"Weight Data added successfully.");

		int girth,length,weight;
		Assert.assertEquals(200, response.getStatusCode());
		if(requestParams.getString("calculateBy").equals("Girth")) {
			girth = requestParams.getInt("girth");
			length = requestParams.getInt("length");
			weight = (girth*girth*length)/300 ;}
		else {
			weight = requestParams.getInt("weight");
		}

		//Assert added details
		ViewCattleProfile cattleprofile = new ViewCattleProfile();
		String viewDetails = cattleprofile.viewCattleProfile(requestParams.getString("CattleId"),token);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleProfileData");

		Assert.assertEquals(weight,(cattleMilkingData.getInt("currentWeight")));
		String date = requestParams.getString("dateOfWeight");
		Assert.assertEquals(cattleMilkingData.getString("currentWeightRecordedDate").contains(date), true);

		System.out.println("Weight data added.");

	}
	
	
	@Test(groups= {"Regression"})
	public void AddWeightDirectMinWeight() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

RequestSpecification request = RestAssured.given(); 
		
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		
		

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightDirectMinimumWeight",filepath);




		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		//Validate  message
		String jsonString = response.asString();
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

	
		JSONArray obj = error.getJSONArray("CalculatedWeight");
		Assert.assertEquals(obj.getString(0),"Weight must be between 15 and 2000 kg.");

	}
	
	
	@Test(groups= {"Regression"})
	public void AddWeightDirectMaxiWeight() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given(); 
		
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightDirectMaximumWeight",filepath);




		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
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
	public void AddTwoWeightsOnSameDay() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");
		String external = prop.getProperty("AccountManagement");
		
		RequestSpecification request = RestAssured.given(); 
		
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(usertoken);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightDirect",filepath);
		
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();
		
		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate message
		Assert.assertEquals(message,"Cannot add more than one weight entry on one date.");

	}
	
	@Test(groups= {"Regression"})
	public void AddWeightDirectBeforePreviouslyAddedDate() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

RequestSpecification request = RestAssured.given(); 
		
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(usertoken);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightBeforePreviouslyAddedDate",filepath);
		
		Helper date = new Helper();
		requestParams.put("dateOfWeight", date.getDate(-1));
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();
		
		requestParams = data.readCase("AddWeight","AddWeightBeforePreviouslyAddedDate",filepath);
		requestParams.put("dateOfWeight", date.getDate(-2));
		
		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate  message
		Assert.assertEquals(message,"Cannot add a weight entry on a date before previously added weight entry. Please delete previous weight entry and then add new weight data.");
	}
	
	
	
	
	
	@Test(groups= {"Regression"})
	public void AddWeightDirectConsecutiveDatesError() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

RequestSpecification request = RestAssured.given(); 
		
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(usertoken);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightDirectConsecutiveDatesError",filepath);
		
		Helper date = new Helper();
		
		// First Request - AddWeight - Previous day's date(-1)
		requestParams.put("dateOfWeight", date.getDate(-2));
		requestParams.put("Weight", 410);
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();
		
		// Second Request - AddWeight - Current date(0) - Weight more than 1.5kg/Day Difference
		requestParams = data.readCase("AddWeight","AddWeightBeforePreviouslyAddedDate",filepath);
		requestParams.put("dateOfWeight", date.getDate(-1));
		requestParams.put("Weight", 412);
		
		
		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate  message
		Assert.assertEquals(message,"Difference between weight entry cannot be more than 1.5kgs/day.");

	}
	
	
	
	
	/*@Test(groups= {"Regression"})
	public void addWeightDirectMismatch() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightDirectMismatch",filepath);


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
		
		
		JSONArray obj = error.getJSONArray( "Weight");
		Assert.assertEquals(obj.getString(0),"Weight is required due to CalculateBy being equal to Direct");
		

	}*/




}
