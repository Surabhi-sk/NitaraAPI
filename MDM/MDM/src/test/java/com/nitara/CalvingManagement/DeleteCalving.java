package com.nitara.CalvingManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteCalving extends GenericBase{

	@Test(groups= {"Smoke"})
	public void deleteCalving() throws Exception {

		System.out.println("Delete Calving");
		String abstractname = prop.getProperty("DeleteCalving");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");

		// Login with admin credentials
		Login admin = new Login();
		String Admintoken = admin.userToken("1234567890","gormal@123456");

		RequestSpecification request = RestAssured.given();

		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);

		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		int noOfActivities = breedingActivities.length();


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + Admintoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Calving data removed Successfully.");


		// Check if the activity is deleted
		viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);
		cattleDetails = new JSONObject(viewDetails);
		cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		Assert.assertEquals((noOfActivities-1), breedingActivities.length());
	}
}
