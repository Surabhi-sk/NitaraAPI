package com.nitara.CattleManagement;

import java.io.IOException;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SearchMilkingCattle extends GenericBase{


	@Test(groups= {"Smoke"})
	public void SearchMilkingCattleDetails( ) throws IOException {
		String abstractname = prop.getProperty("SearchMilkingCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");

		RequestSpecification request = RestAssured.given();

		///Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject= data.readData("RegMilkingDryCattle",filepath,external);
		

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("farmId", dataObject.getString("farmId")); // Cast
		requestParams.put("searchString", dataObject.getString("tagNumber"));

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());
	}


}
