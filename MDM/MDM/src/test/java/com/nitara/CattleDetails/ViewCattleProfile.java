package com.nitara.CattleDetails;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewCattleProfile extends GenericBase{
	
	@Test(groups= {"Smoke"})
	public void viewCattleProfile() throws Exception {
		
		System.out.println("View Cattle Profile");
		String abstractname = prop.getProperty("ViewCattleProfile");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject 79fc2ee3-968b-49e8-87d6-023579ab68fa

		ExcelUtils data = new ExcelUtils();
		/*JSONObject requestParams = data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);*/
		
	
		JSONObject requestParams = new JSONObject();
		requestParams.put("CattleId", "d2c71699-8e27-44d4-968b-7ad496c22ba6");
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
		Assert.assertEquals(message,"Cattle Profile Details");

}
	
	public String viewCattleProfile(String cattleId,String token ) {
		
		String abstractname = prop.getProperty("ViewCattleProfile");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("CattleId", cattleId);
		request.body(requestParams.toString());
		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);
 
		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");	
		Assert.assertEquals( message,"Cattle Profile Details");

		return jsonString;
	}


}
