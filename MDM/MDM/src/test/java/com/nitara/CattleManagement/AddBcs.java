package com.nitara.CattleManagement;

import java.io.IOException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleProfile;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddBcs extends GenericBase{

	@Test(groups= {"Smoke"})
	public void addBcsDetails() throws IOException {

		String abstractname = prop.getProperty("AddBcs");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readData("AddBcs",filepath);

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
		Assert.assertEquals( message,"BCS Data added successfully.");

		
		//Assert the added changes
		ViewCattleProfile cattleprofile = new ViewCattleProfile();
		String viewDetails = cattleprofile.viewCattleProfile(requestParams.getString("CattleId"),token);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleProfileData");

		Assert.assertEquals((cattleMilkingData.getInt("currentBcs")),requestParams.getInt("BcsScore"));
		String date = requestParams.getString("DateOfBcs");
		Assert.assertEquals(cattleMilkingData.getString("currentBcsRecordedDate").contains(date), true);

		System.out.println("Bcs data added.");

	}
}
