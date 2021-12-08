package com.nitara.FarmManagement;



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

public class ViewFarmCattleList extends GenericBase
{
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void viewFarmCattleList() throws Exception 
	{

		String abstractname = prop.getProperty("ViewFarmCattleList");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		//JSONObject requestParams = exceldata.readRowField("GeneralData", "farmId", filepath);

		JSONObject requestParams = new JSONObject();
		requestParams.put("farmId","2f34e39a-f573-40b2-b6e0-6c50fb2a5af2");
		RequestSpecification request = RestAssured.given();
		
	
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattle List", message);

	}

	public String viewFarmCattleList(String farmId) {
		// TODO Auto-generated method stub
		String abstractname = prop.getProperty("ViewFarmCattleList");
		RestAssured.baseURI = prop.getProperty("baseurl");


		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("farmId", farmId); // Cast 

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);
		System.out.println("Response body: " + response.body().prettyPeek().asString());

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattle List", message);


		return jsonString;
	}			

}
