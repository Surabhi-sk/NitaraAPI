package com.nitara.UserPreferenceManagement;

import java.util.Map;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SetUserInsurancePreferences extends GenericBase{
	@Test(groups= {"Smoke"})
	public void setInsurancePreference() {
		String abstractname = prop.getProperty("SetUserInsurancePreferences");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();


		//JSON Parameters 
		JSONObject requestParams = new JSONObject();
		requestParams.put("IsNotificationOpted", true); // Bool
		//requestParams.put( "NotificationReceiver","NotificationReceiver"); 

		request.body(requestParams.toString());

		Response response = request.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		// Response
		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Success Message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals("Insurance Preferences Updated", message);

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());
		
		//Assert the set details
		GetUserInsurancePreferences pref = new GetUserInsurancePreferences();
		String res = pref.getUserInsurancePreferences(token);
		
		Map<String, String> detail= JsonPath.from(res).getMap("insurancePreferences");
		//Validate success message
		Assert.assertEquals("true",String.valueOf(detail.get("isNotificationOpted")));
		Assert.assertEquals(true,detail.get("isNotificationOpted"));
		//Assert.assertEquals("NReceiver", detail.get("notificationReceiver"));

	}
}
