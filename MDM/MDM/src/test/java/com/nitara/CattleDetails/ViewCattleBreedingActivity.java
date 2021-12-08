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

public class ViewCattleBreedingActivity extends GenericBase{

	@Test(groups= {"Smoke"})
	public void viewCattleBreedingActivity() throws Exception {

		System.out.println("View Cattle Breeding Activity");
		String abstractname = prop.getProperty("ViewCattleBreedingActivity");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");
		
		RequestSpecification request = RestAssured.given();

		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
	JSONObject requestParams = data.readRowField("GeneralData","CattleId",filepath);
		JSONObject lactation = data.readRowField("GeneralData", "Lactation", filepath);
		
		//JSONObject requestParams = new JSONObject();
		
		//requestParams.put("CattleId", "4d2b17b8-421f-4f04-93ae-0ffdf257fa90");
		//requestParams.put("LactationNumber", 1);
		
		requestParams.put("LactationNumber", lactation.get("Lactation"));
		requestParams.put("startDate", "2019-03-20");
		requestParams.put("endDate", "2020-03-08");

		System.out.println(requestParams);
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
		Assert.assertEquals(message,"Cattle Milking Details");
		
}



	public String viewCattleBreedingActivity(String cattleid, int lactation,String token) {

		String abstractname = prop.getProperty("ViewCattleBreedingActivity");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("cattleId", cattleid); // Cast	
		requestParams.put("LactationNumber", lactation);
		request.body(requestParams.toString());
		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname).then().extract().response(); 


		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattle Milking Details", message);
		System.out.println("Response body: " + response.body().prettyPeek().asString());
		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		
		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());
		
		String responseString = response.body().asString();

		return responseString;

	}

}
