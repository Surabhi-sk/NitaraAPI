package com.nitara.CattleManagement;


import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleRegistration.RegisterCalfCattle;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RemoveCattle extends GenericBase{


	@Test(groups= {"Smoke"})
	public void RemoveCattleDetails() throws Exception {

		String abstractname = prop.getProperty("RemoveCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		RegisterCalfCattle reg = new RegisterCalfCattle();
		String cattleId = reg.registerCalf(token);
		
		JSONObject requestParams = new JSONObject();
		requestParams.put("CattleId", cattleId); 
		requestParams.put("ReasonForDeletion", "Sold");
		requestParams.put("Remarks", "CattleSold");
		requestParams.put("Price", 5000); 
		request.body(requestParams.toString());


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());


		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		System.out.println("message");
		Assert.assertEquals("Cattle Removed successfully.", message);

	}
}
