package com.nitara.DryPeriodManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddDryPeriod extends GenericBase{

	@Test(groups= {"Smoke"})
	public void addDryPeriod() throws IOException {

		String abstractname = prop.getProperty("AddDryPeriod");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readData("AddDryPeriod",filepath);
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
		Assert.assertEquals("Dry Period data added Successfully.", message);

		//Assert the updated changes
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		JSONObject activity = breedingActivities.getJSONObject(breedingActivities.length()-1);


		Assert.assertEquals("Dry Period",activity.getString("activity"));
		String date = requestParams.getString("DryPeriodDate");
		Assert.assertEquals(activity.getString("date").contains(date),true);

	}



}