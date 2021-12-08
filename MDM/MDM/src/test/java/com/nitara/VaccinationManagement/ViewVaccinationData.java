package com.nitara.VaccinationManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewVaccinationData extends GenericBase{

	@Test(groups= {"Smoke"})
	public void viewVaccinationData() throws Exception {
		String abstractname = prop.getProperty("ViewVaccinationData");
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
		Assert.assertEquals("Vaccination Details", message);
	}


	public String viewVaccinationData(String cattleId,String token) {
		String abstractname = prop.getProperty("ViewVaccinationData");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("CattleId", cattleId); // Cast 
		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");
		request.header("Authorization","Bearer " + token);

		Response response = request.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());
		String jsonString = response.asString();

		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());

		return jsonString;


	}

}
