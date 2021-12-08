package com.nitara.WeightManagement;


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


public class AddWeightByGirth extends GenericBase{

	public String filepath = prop.getProperty("CattleOtherActivities");

	@Test(groups= {"Smoke"})
	public void AddWeight_ByGirth() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_ByGirth",filepath);
		

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);

		//Validate  message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Weight Data added successfully.", message);

		int girth,length,weight;
		Assert.assertEquals( response.getStatusCode(),200);
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
	public void AddWeight_ByGirthMinimumWeightCheck() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_ByGirthMinimumWeightCheck",filepath);

		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
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
	public void AddWeight_ByGirthMaximumWeightCheck() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_ByGirthMaximumWeightCheck",filepath);
		
		requestParams.remove("Method");
		System.out.println(requestParams);

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		//Validate success message
		String jsonString = response.asString();
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

	
		JSONArray obj = error.getJSONArray("CalculatedWeight");
		Assert.assertEquals(obj.getString(0),"Weight must be between 15 and 2000 kg.");

	}
	
	

	@Test(groups= {"Regression"})
	public void AddWeight_TwiceOnSameDay() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_ByGirth",filepath);
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();
		
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
		//Validate  message
		Assert.assertEquals(message,"Cannot add more than one weight entry on one date.");

	}
	

	@Test(groups= {"Regression"})
	public void AddWeight_BeforePreviouslyAddedDate() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_BeforePreviouslyAddedDate",filepath);
		
		
		Helper date = new Helper();
		requestParams.put("dateOfWeight", date.getDate(-1));
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();
		
		requestParams = data.readCase("AddWeight","AddWeight_BeforePreviouslyAddedDate",filepath);
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
		//Validate message
		Assert.assertEquals(message,"Cannot add a weight entry on a date before previously added weight entry. Please delete previous weight entry and then add new weight data.");

	}
	
	

	@Test(groups= {"Regression"})
	public void AddWeight_ByGirthonConsecutiveDates() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeight_ByGirthonConsecutiveDates",filepath);
		
		requestParams.remove("Method");
		System.out.println(requestParams);
		
		Helper date = new Helper();
		
		// First Request - AddWeight - Previous day's date(-1)
		requestParams.put("dateOfWeight", date.getDate(-2));
		requestParams.put("girth", 35);
		requestParams.put("length", 100);
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();
		
		// Second Request - AddWeight - Current date(0) - Weight more than 1.5kg/Day Difference
		requestParams = data.readCase("AddWeight","AddWeight_ByGirthonConsecutiveDates",filepath);
		
	
		requestParams.put("dateOfWeight", date.getDate(-1));
		requestParams.put("girth", 45);
		requestParams.put("length", 100);
		
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
		Assert.assertEquals(message,"Difference between weight entry cannot be more than 1.5kgs/day.");

	}
	
	
	/*@Test(groups= {"Regression"})
	public void addWeightByGirthMismatch() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("AddWeight","AddWeightByGirthMismatch",filepath);


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
		
		obj = error.getJSONArray("Length");
		Assert.assertEquals(obj.getString(0),"Length is required due to CalculateBy being equal to Girth");
	


	}
	
	@Test(groups= {"Regression"})
	public void addWeightByGirthMandatoryFields() throws Exception {
		String abstractname = prop.getProperty("AddWeight");
		RestAssured.baseURI = prop.getProperty("baseurl");
	

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
		
		obj = error.getJSONArray( "CattleId");
		Assert.assertEquals(obj.getString(0),"The CattleId field is required.");
		
		obj = error.getJSONArray( "DateOfWeight");
		Assert.assertEquals(obj.getString(0),"The DateOfWeight field is required.");
	
	}*/
	



}
