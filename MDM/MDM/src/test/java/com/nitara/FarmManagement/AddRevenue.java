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

public class AddRevenue extends GenericBase
{
	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void addRevenue() throws Exception 
	{

		String abstractname = prop.getProperty("AddRevenue");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject  requestParams = exceldata.readRowField("GeneralData", "farmId", filepath);


		RequestSpecification request = RestAssured.given();
		requestParams.put("RevenueCategory", "Milk"); 
		requestParams.put("quantity", 30); 
		requestParams.put("amount",60);
		requestParams.put( "remarks", "Revenue");
		requestParams.put( "Date", "2021-03-02T00:00:10");

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);

		String jsonString = response.asString();				
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Revenue Data added successfully.");

	}			
}
