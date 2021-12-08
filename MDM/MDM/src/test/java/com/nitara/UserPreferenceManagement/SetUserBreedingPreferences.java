package com.nitara.UserPreferenceManagement;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SetUserBreedingPreferences extends GenericBase{

	@Test(groups= {"Smoke"})
	public void SetBreedingPreferences() {
		String abstractname = prop.getProperty("SetUserBreedingPreferences");
		RestAssured.baseURI = prop.getProperty("baseurl"); 

		RequestSpecification request = RestAssured.given();

		ArrayList<Object> listdata = new ArrayList<Object>();  

		// Calving
		JSONObject arrayOne = new JSONObject();
		arrayOne.put("Activity", "Calving");
		arrayOne.put("IsNotificationOpted",false); // Boolean
		arrayOne.put("NotificationReceiver","e080634c-846c-4faf-83a4-f7bc990076e1");


		// Heat
		JSONObject arrayTwo = new JSONObject();
		arrayTwo.put("Activity", "Heat");
		arrayTwo.put("IsNotificationOpted",true); // Boolean
		arrayTwo.put("NotificationReceiver","user-id");



		// PD
		JSONObject arrayThree = new JSONObject();
		arrayThree.put("Activity", "PD");
		arrayThree.put("IsNotificationOpted",true); // Boolean
		arrayThree.put("NotificationReceiver","e080634c-846c-4faf-83a4-f7bc990076e1");
		arrayThree.put("daysDifference", 64);

		// Dry
		JSONObject arrayFour = new JSONObject();
		arrayFour.put("Activity", "Dry");
		arrayFour.put("IsNotificationOpted",false); // Boolean
		arrayFour.put("NotificationReceiver","e080634c-846c-4faf-83a4-f7bc990076e1");
		arrayFour.put("daysDifference", 65);

		listdata.add(arrayOne);
		listdata.add(arrayTwo);
		listdata.add(arrayThree);
		listdata.add(arrayFour);

		request.body(listdata.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("Breeding Preferences Updated", message);

		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());


		//Assert the set details
		GetUserBreedingPreferences pref = new GetUserBreedingPreferences();
		String res = pref.getUserBreedingPreferences(token);

		JSONObject obj = new JSONObject(res);
		JSONArray preference= obj.getJSONArray("breedingPreferences");

		for(int i=0;i<preference.length();i++) {
			JSONObject activityObj = preference.getJSONObject(i);
			//FollowUp data
			if(activityObj.get("activity").equals("Heat")) {
				Assert.assertEquals(true,activityObj.get("isNotificationOpted"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}
			
			//Deworming data
			if(activityObj.get("activity").equals("PD")) {
				Assert.assertEquals(true,activityObj.get("isNotificationOpted"));
				Assert.assertEquals(64,activityObj.get("daysDifference"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}

			//Vaccination data
			if(activityObj.get("activity").equals("Calving")) {
				Assert.assertEquals(false,activityObj.get("isNotificationOpted"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}
			
			//Vaccination data
			if(activityObj.get("activity").equals("Dry")) {
				Assert.assertEquals(false,activityObj.get("isNotificationOpted"));
				Assert.assertEquals(65,activityObj.get("daysDifference"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}
		}

		System.out.println("User Breeding Preferences set.");



	}

}
