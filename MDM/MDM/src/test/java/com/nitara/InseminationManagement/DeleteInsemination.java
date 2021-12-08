package com.nitara.InseminationManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteInsemination extends GenericBase{

	@Test(groups= {"Smoke"})
	public void deleteInsemination() throws Exception {

		String abstractname = prop.getProperty("DeleteInsemination");
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
		Assert.assertEquals("Insemination data removed Successfully.", message);


		//Check if the insemination activity is removed
		viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);
		cattleDetails = new JSONObject(viewDetails);
		cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		Assert.assertEquals((noOfActivities-1), breedingActivities.length());

	}
}
