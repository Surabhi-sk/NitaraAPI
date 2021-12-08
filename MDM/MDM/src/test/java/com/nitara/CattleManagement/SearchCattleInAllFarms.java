package com.nitara.CattleManagement;


import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SearchCattleInAllFarms extends GenericBase{

	@Test(groups= {"Smoke"})
	public void searchCattleInAllFarms( ) {
		
		String abstractname = prop.getProperty("SearchCattleInAllFarms");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject(); 
		requestParams.put("searchString", "M");

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
