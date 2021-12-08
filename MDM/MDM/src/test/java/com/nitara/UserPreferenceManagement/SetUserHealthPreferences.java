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

public class SetUserHealthPreferences extends GenericBase{

	@Test(groups= {"Smoke"})
	public void SetHealthPreferences() {

		String abstractname = prop.getProperty("SetUserHealthPreferences");
		RestAssured.baseURI = prop.getProperty("baseurl"); 

		RequestSpecification request = RestAssured.given();

		ArrayList<Object> listdata = new ArrayList<Object>();  

		// Follow up
		JSONObject arrayOne = new JSONObject();
		arrayOne.put("Activity", "Followup");
		arrayOne.put("IsNotificationOpted",false); // Boolean
		arrayOne.put("NotificationReceiver","e080634c-846c-4faf-83a4-f7bc990076e1");

		// Deworming
		JSONObject arrayTwo = new JSONObject();
		arrayTwo.put("Activity", "Deworming");
		arrayTwo.put("IsNotificationOpted",true);
		arrayTwo.put("intervalInMonths",5);
		arrayTwo.put("NotificationReceiver","e080634c-846c-4faf-83a4-f7bc990076e1");

		JSONArray arrayTwoElements = new JSONArray();
		JSONObject arrayTwoElementOneArray = new JSONObject();
		arrayTwoElementOneArray.put("DayAfterBirth", 17 );

		JSONObject arrayTwoElementSecondArray = new JSONObject();
		arrayTwoElementSecondArray.put("DayAfterBirth", 85 );

		arrayTwoElements.put(arrayTwoElementOneArray);
		arrayTwoElements.put(arrayTwoElementSecondArray);
		arrayTwo.put("CalfHealthPreferences", arrayTwoElements);

		// Vaccination
		JSONObject arrayThree = new JSONObject();
		arrayThree.put("Activity", "Vaccination");
		arrayThree.put("IsNotificationOpted",true); // Boolean
		arrayThree.put("NotificationReceiver","e080634c-846c-4faf-83a4-f7bc990076e1");

		listdata.add(arrayOne);
		listdata.add(arrayTwo);
		listdata.add(arrayThree);

		request.body(listdata.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("Health Preferences Updated", message);


		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());

		//Assert the set details
		GetUserHealthPreferences pref = new GetUserHealthPreferences();
		String res = pref.getUserHealthPreferences(token);

		JSONObject obj = new JSONObject(res);
		JSONArray preference= obj.getJSONArray("healthPreferences");

		for(int i=0;i<preference.length();i++) {
			JSONObject activityObj = preference.getJSONObject(i);
			if(activityObj.get("activity").equals("FollowUp")) {
				Assert.assertEquals(false,activityObj.get("isNotificationOpted"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}

			if(activityObj.get("activity").equals("Deworming")) {
				Assert.assertEquals(true,activityObj.get("isNotificationOpted"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}

			if(activityObj.get("activity").equals("Vaccination")) {
				Assert.assertEquals(true,activityObj.get("isNotificationOpted"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				//Assert.assertEquals("NReceiver", activityObj.get("notificationReceiver"));
				continue;
			}
		}

		System.out.println("User Health Preferences set.");




	}

}
