package com.nitara.PDManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CalvingManagement.AddCalving;
import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.CattleRegistration.RegisterMilkingOrDryCattle;
import com.nitara.InseminationManagement.AddInsemination;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeletePD extends GenericBase{

	@Test(groups= {"Smoke"})
	public void deletePD() throws Exception {

		String abstractname = prop.getProperty("DeletePD");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);


		//store no of activities listed for the cattle
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		int noOfActivities = breedingActivities.length();

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
		Assert.assertEquals("Pd data removed Successfully.", message);

		//Check if the PD activity is removed
		viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);
		cattleDetails = new JSONObject(viewDetails);
		cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		Assert.assertEquals((noOfActivities-1), breedingActivities.length());

	}
	
	
	
	@Test(groups= {"Regression"})
	public void deletePDwhenOtherBreedingActivitiesareAdded() throws Exception {

		String abstractname = prop.getProperty("DeletePD");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();
		
		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();

		Helper date = new Helper();

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


		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cannot delete Pd data. Delete all breeding activities added after Pd.", message);

	}


}
