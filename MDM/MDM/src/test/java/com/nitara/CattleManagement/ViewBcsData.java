package com.nitara.CattleManagement;


import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ViewBcsData extends GenericBase{

	@Test(groups= {"Smoke"})
	public void viewBcsData() throws Exception {

		String abstractname = prop.getProperty("ViewBcsData");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readCase("ViewBcsData","ViewBcsData",filepath);
		
		System.out.println(requestParams);

		Helper date = new Helper();

		requestParams.put("EndDate", date.getDate(0));

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
