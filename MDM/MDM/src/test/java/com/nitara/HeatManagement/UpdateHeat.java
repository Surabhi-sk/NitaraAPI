package com.nitara.HeatManagement;



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

public class UpdateHeat extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateHeat() throws Exception {

		String abstractname = prop.getProperty("UpdateHeat");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateHeat","UpdateHeatwithValidData",filepath);
		
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
		Assert.assertEquals("Heat data updated Successfully.", message);

		//Assert the updated changes
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		JSONObject lactation = data.readRowField("GeneralData","Lactation",filepath);
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),lactation.getInt("Lactation"),token); 

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);

		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");

		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");

		JSONObject activity = breedingActivities.getJSONObject(breedingActivities.length()-1);
		Assert.assertEquals("Heat",activity.getString("activity"));
		Assert.assertEquals(requestParams.getString("heatType"),activity.getString("heatType"));
		String date = requestParams.getString("heatDateTime");
		Assert.assertEquals(activity.getString("date").contains(date),true);

		System.out.println("Status: Updated values relected");

	}
	
	@Test(groups= {"Regression"})
	public void noHeatActivity() throws Exception {

		String abstractname = prop.getProperty("UpdateHeat");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();
		
		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();
		
		

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateHeat","UpdateHeatwithValidData",filepath);


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);


		//Validate error message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cannot update heat data. Delete all breeding activities added after Heat.", message);



	}
	
	@Test(groups= {"Regression"})
	public void updateHeatwithFutureDate() throws Exception {

		String abstractname = prop.getProperty("UpdateHeat");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();
		
		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();
		
		AddHeat heat = new AddHeat();
		heat.addHeatData(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateHeat","UpdateHeat",filepath);
		
		Helper date = new Helper();
		requestParams.put("heatDateTime", date.getDate(2));


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


				JSONArray obj = error.getJSONArray( "HeatDateTime");
				Assert.assertEquals( obj.getString(0).contains("must not be greater than today"),true);



	}
	
	//@Test(groups= {"Regression"})
	/*public void updateHeatwithFutureDate() throws Exception {

		String abstractname = prop.getProperty("UpdateHeat");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();
		
		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();
		
		AddHeat heat = new AddHeat();
		heat.addHeatData(token);

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("UpdateHeat","UpdateHeat",filepath);
		
		Helper date = new Helper();
		requestParams.put("heatDateTime", date.getDate(2));


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


				JSONArray obj = error.getJSONArray( "HeatDateTime");
				Assert.assertEquals( obj.getString(0).contains("must not be greater than today"),true);



	}*/
}
