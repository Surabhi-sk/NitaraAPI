package com.nitara.UserPreferenceManagement;


import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class SetUserMilkingPreferences extends GenericBase{

	@Test(groups= {"Smoke"})
	public void setUserMilkingPreferences() {

		String abstractname = prop.getProperty("SetUserMilkingPreferences");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();


		//JSON Parameters 
		JSONObject requestParams = new JSONObject();
		requestParams.put("TypeOfMilking", "Cattle"); // "Cattle" or "Farm"
		requestParams.put( "IsAfternoonSessionOpted",true); // boolean

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
		Assert.assertEquals("Milking Preferences Updated", message);

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		
		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());
		
		//Assert the set changes
	/*	GetUserMilkingPreferences pref = new GetUserMilkingPreferences();
		String res = pref.getMilkingPreferences(token);
		
		Map<String, String> detail= JsonPath.from(res).getMap("milkingPreferences");
		String type = detail.get("typeOfMilking");
		Assert.assertEquals("Cattle", type);
		//Validate success message
		//Assert.assertEquals("true",String.valueOf(detail.get("isAfternoonSessionOpted")));
		Assert.assertEquals(true,detail.get("isAfternoonSessionOpted"));*/
		

}

	
	
	public void setUserMilkingPreferences(String type,String usertoken) {

		String abstractname = prop.getProperty("SetUserMilkingPreferences");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();


		//JSON Parameters 
		JSONObject requestParams = new JSONObject();
		requestParams.put("TypeOfMilking", type); // "Cattle" or "Farm"
		requestParams.put( "IsAfternoonSessionOpted",true); // boolean

		request.body(requestParams.toString());

		Response response = request.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		// Response
		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Success Message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals("Milking Preferences Updated", message);

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		
		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());
		

}

}
