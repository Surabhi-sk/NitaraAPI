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

public class ViewAllCattlePhotos extends GenericBase{

	@Test(groups= {"Smoke"})
	public void viewAllCattlePhotos() throws Exception 
	{
		System.out.println("View All Photos");
		String abstractname = prop.getProperty("ViewAllCattlePhotos");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");


		RequestSpecification request = RestAssured.given();

		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams= data.readRowField("GeneralData","CattleId",filepath);
		System.out.println(requestParams);
		

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200 );

		//Validate success message
		String jsonString = response.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Cattle Profile Pictures.");
		
	}			
	
	
	public String viewAllCattlePhotos(String cattleId, String utoken) throws Exception 
	{

		String abstractname = prop.getProperty("ViewAllCattlePhotos");
		RestAssured.baseURI = prop.getProperty("baseurl");


		RequestSpecification request = RestAssured.given();

		
		JSONObject requestParams= new JSONObject();
		requestParams.put("CattleId",cattleId);
		
		request.body(requestParams.toString());
		
		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + utoken);
		Response response = request.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200 );

		//Validate success message
		String jsonString = response.asString();	
		String  message = JsonPath.from(jsonString).get("message");	
		Assert.assertEquals(message,"Cattle Profile Pictures.");
		
		return(jsonString);

	}			
}
