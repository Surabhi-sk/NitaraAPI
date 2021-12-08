package com.nitara.Treatment;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleRegistration.RegisterBullCattle;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteTreatment extends GenericBase{


	@Test(groups= {"Smoke"})
	public void deleteTreatment() throws Exception {
		String abstractname = prop.getProperty("DeleteTreatment");
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
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Treatment Data Removed Successfully.", message);

	}
	
	@Test(groups= {"Regression"})
	public void treatmentdoesntExist() throws Exception {
		String abstractname = prop.getProperty("DeleteTreatment");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

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
		Assert.assertEquals( response.getStatusCode(),400);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Treatment does not exist.", message);

	}
	
	
	@Test(groups= {"Regression"})
	public void deleteTreatmentTwice() throws Exception {
		String abstractname = prop.getProperty("DeleteTreatment");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
		AddTreatment treatmnt = new AddTreatment();
		treatmnt.addTreatment(token);
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
		Assert.assertEquals( response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Treatment Data Removed Successfully.", message);
		
		
		response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);
		
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		//Validate success message
		jsonString = response.asString();
		message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Treatment does not exist.", message);

	}
}
