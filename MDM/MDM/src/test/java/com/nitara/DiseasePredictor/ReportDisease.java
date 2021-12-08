package com.nitara.DiseasePredictor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleRegistration.RegisterBullCattle;
import com.nitara.CattleRegistration.RegisterHeiferCattle;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*User Story :Disease Predictor 678
@Author : Ravi Teja
*/

public class ReportDisease extends GenericBase{
	
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void ReportDisease() throws Exception 
	{
	
		
		String abstractname = prop.getProperty("ReportDisease");
		RestAssured.baseURI = prop.getProperty("baseurl"); 
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		
	/*	JSONObject requestParams = new JSONObject();
		requestParams.put("CattleId", "08f773f8-b9e9-46b6-8202-80c7d2cacf12");
		requestParams.put("Symptoms","Severe Inflammation of Udder");
		requestParams.put("Disease", "Disease_LumpySkinDisease");
		requestParams.put("HasFever", true);
		requestParams.put("HasStoppedEating", true);*/
		
		RegisterBullCattle hc= new RegisterBullCattle();
		
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readRowField("GeneralData","CattleId",filepath);
		
		HashMap<String, Object> queryParam = new HashMap<>();	
	
		queryParam.put("CattleId", requestParams.get("CattleId"));
		
		queryParam.put("Disease", "Disease_LumpySkinDisease");
		queryParam.put("HasFever", true);
		queryParam.put("HasStoppedEating", true);
		queryParam.put("Symptoms",Arrays.asList("Persistent Infection of Udder", "Hard Lumps in Udder"));
			
		

		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);;

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());
		if (response.getStatusCode() == 200) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals("Disease reported successfully.", message);

	}
	@Test(groups= {"Regression"})
	public void ReportDiseaseforinvalidcattle() 
	{
	
		
		String abstractname = prop.getProperty("ReportDisease");
		RestAssured.baseURI = prop.getProperty("baseurl"); 

		RequestSpecification request = RestAssured.given();
		
		
		
		
		HashMap<String, Object> queryParam = new HashMap<>();	
	
		queryParam.put("CattleId", "08f773f8-b9e9-46b6-8202-80c7d2cacf");
		
		queryParam.put("Disease", "Disease_LumpySkinDisease");
		queryParam.put("HasFever", true);
		queryParam.put("HasStoppedEating", true);
		queryParam.put("Symptoms",Arrays.asList("Persistent Infection of Udder", "Hard Lumps in Udder"));
			
		

		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);;

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());
		if (response.getStatusCode() == 400) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals("Invalid cattle.", message);

	}
	
	
	

}
