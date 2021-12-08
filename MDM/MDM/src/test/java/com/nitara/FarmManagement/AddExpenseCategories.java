package com.nitara.FarmManagement;


import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddExpenseCategories extends GenericBase
{
	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void addExpenseCategories() throws Exception 
	{
/*
		String abstractname = prop.getProperty("AddExpenseCategories");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read username and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "password", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("password"));


		HashMap<String, Object> queryParam = new HashMap<>();
		queryParam.put("categories", Arrays.asList("Product1", "Product2"));

		RequestSpecification request = RestAssured.given();

		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Expense Categories added successfully.", message);*/


	}			
}
