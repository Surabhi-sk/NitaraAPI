package com.nitara.HousingManagement;


import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddSmartShed extends GenericBase
{
	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void addSmartShed() throws Exception 
	{

		String abstractname = prop.getProperty("AddSmartShed");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject farmId = exceldata.readRowField("GeneralData", "farmId", filepath);


		RequestSpecification request = RestAssured.given();

		HashMap<String, Object> queryParam = new HashMap<>();
		queryParam.put("farmid", farmId.getString("farmId"));
		queryParam.put("shedname", "SmartNewShed1");
		queryParam.put("Groups", Arrays.asList("SmartGroup3", "SmartGroup4"));

		
		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);


		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();				
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Smart Shed added successfully.", message);
	}			
}

