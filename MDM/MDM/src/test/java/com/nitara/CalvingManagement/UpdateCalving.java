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

public class UpdateCalving extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateCalving() throws Exception {

		System.out.println("Update Calving");
		String abstractname = prop.getProperty("UpdateCalving");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");

		// Login with admin credentials
		Login admin = new Login();
		String Admintoken = admin.userToken("1234567890","gormal@123456");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readData("UpdateCalving",filepath);

		
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
		Assert.assertEquals(message,"Calving data updated Successfully.");


		//Assert the updated changes
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		JSONObject lactation = data.readRowField("GeneralData","Lactation",filepath);
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),lactation.getInt("Lactation"),token); 

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		JSONObject activity = breedingActivities.getJSONObject(breedingActivities.length()-1);

		Assert.assertEquals(activity.getString("activity"),"Calving");
		Assert.assertEquals(activity.getString("calvingResult"),requestParams.getString("calfResult"));
		String date = requestParams.getString("calvingDate");
		Assert.assertEquals(activity.getString("date").contains(date),true);
		System.out.println("Status: Updated values relected");
	}
}




