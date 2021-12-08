package com.nitara.DewormingManagement;


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

public class DeleteDeworming extends GenericBase{

	@Test(groups= {"Smoke"})
	public void deleteDeworming() throws Exception {
		String abstractname = prop.getProperty("DeleteDeworming");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);

		ViewDewormingData details = new ViewDewormingData();
		String viewDetails = details.viewDewormingData(requestParams.getString("CattleId"), token);
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleDewormingData = cattleDetails.getJSONObject("dewormingData");
		JSONArray deworming = cattleDewormingData.getJSONArray("dewormings");
		int noOfDewormings = deworming.length();

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
		Assert.assertEquals("Deworming Data Removed Successfully.", message);


		// Check if the activity is deleted
		viewDetails = details.viewDewormingData(requestParams.getString("CattleId"), token);
		cattleDetails = new JSONObject(viewDetails);
		cattleDewormingData = cattleDetails.getJSONObject("dewormingData");
		deworming = cattleDewormingData.getJSONArray("dewormings");
		Assert.assertEquals((noOfDewormings-1), deworming.length());

	}

}
