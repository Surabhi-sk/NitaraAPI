package com.nitara.MilkingManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewCattleMilking extends GenericBase{

	//@Test(groups= {"Smoke"})
	public void viewCattleMilkingDetails( ) throws Exception {
		String abstractname = prop.getProperty("ViewCattleMilking");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject cattleIds = exceldata.readRowField("GeneralData", "CattleId", filepath);
		JSONObject lactation = exceldata.readRowField("GeneralData","Lactation",filepath);

		JSONObject requestParam = new JSONObject();
		RequestSpecification request = RestAssured.given();

		
		requestParam.put("cattleId", cattleIds.getString("CattleId")); // Cast
		requestParam.put("lactationNumber", lactation.getInt("Lactation"));
		requestParam.put("startDate", "2019-09-10");
		requestParam.put("endDate", "2020-09-24");
		



		Response response = request.body(requestParam.toString())
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
		Assert.assertEquals("Cattle Milking Details", message);

	}


	public String viewCattleMilkingDetails(String token, int lactation, String cattleId ) throws Exception {
		String abstractname = prop.getProperty("ViewCattleMilking");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		JSONObject RequestParams = new JSONObject();


		RequestParams.put("cattleId", cattleId);
		RequestParams.put("lactationNumber", lactation);
		RequestParams.put("startDate", "2019-03-20");
		RequestParams.put("endDate", "2020-03-08");

		System.out.println(RequestParams);

		Response response = request.body(RequestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();

		return jsonString;

	}

}
