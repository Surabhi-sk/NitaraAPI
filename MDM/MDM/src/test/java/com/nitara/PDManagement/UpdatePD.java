package com.nitara.PDManagement;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.CattleRegistration.RegisterMilkingOrDryCattle;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdatePD extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updatePD() throws IOException {

		String abstractname = prop.getProperty("UpdatePD");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdatePD", "UpdatePDwithValidData",filepath);

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
		Assert.assertEquals("Pd data updated Successfully.", message);



		//Assert the updated changes
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		JSONObject activity = breedingActivities.getJSONObject(breedingActivities.length()-1);

		Assert.assertEquals("PD",activity.getString("activity"));
		if(requestParams.get("isCattlePregnant").equals(true)) {
			Assert.assertEquals("Pregnant",activity.getString("pdResult"));}
		else {
			Assert.assertEquals("Not Pregnant",activity.getString("pdResult"));
		}
		String date = requestParams.getString("pregnancyTestDate");
		Assert.assertEquals(activity.getString("date").contains(date),true);

		System.out.println("Status: Updated values relected");
	}
	
	@Test(groups= {"Regression"})
	public void updatePDwithFutureDate() throws IOException {

		String abstractname = prop.getProperty("UpdatePD");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdatePD","UpdatePD",filepath);

		Helper date = new Helper();

		requestParams.put("pregnancyTestDate", date.getDate(2));

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);


		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray( "PregnancyTestDate");
		Assert.assertEquals( obj.getString(0).contains("must not be greater than today"),true);
	}
	
	
	@Test(groups= {"Regression"})
	public void updatePDwhenNoPDavailable() throws Exception {

		String abstractname = prop.getProperty("UpdatePD");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdatePD","UpdatePDwithValidData",filepath);


		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);


		//Validate error message
				String jsonString = response.asString();
				String  message = JsonPath.from(jsonString).get("message");
				Assert.assertEquals("Cannot update Pd data. Delete all breeding activities added after Pd.", message);

	}

}
