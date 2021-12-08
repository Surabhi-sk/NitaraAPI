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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddUserDefinedShed extends GenericBase
{
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void addUserDefinedShed() throws Exception 
	{

		String abstractname = prop.getProperty("AddUserDefinedShed");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject farmId = exceldata.readRowField("GeneralData", "farmId", filepath);


		RequestSpecification request = RestAssured.given();


		HashMap<String, Object> queryParam = new HashMap<>();
		queryParam.put("farmid", farmId.getString("farmId"));
		queryParam.put("shedname", "UserNewShed1");
		queryParam.put("Groups", Arrays.asList("UserGroup3", "UserGroup4"));


		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

	}			
}

