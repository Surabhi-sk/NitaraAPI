package com.nitara.CattleManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SearchCattle extends GenericBase{

	@Test(groups= {"Smoke"})
	public void searchCattle( ) {
		String abstractname = prop.getProperty("SearchCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");


		RequestSpecification request = RestAssured.given();

		//JSONObject
		JSONObject requestParams = new JSONObject(); 
		requestParams.put("searchString", "");
		requestParams.put("farmId", "fdd6c649-f446-4761-a8cc-b05638fc1f5a");
		request.body(requestParams.toString());

		Response response = request.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());

	}
}
