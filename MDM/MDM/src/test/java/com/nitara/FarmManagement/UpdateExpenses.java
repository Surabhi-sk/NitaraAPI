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


	public class UpdateExpenses extends GenericBase
	{
		NitaraService results = new NitaraService();

		@Test(groups= {"Smoke"})
		public void updateExpense() throws Exception 
		{

			String abstractname = prop.getProperty("UpdateExpense");
			RestAssured.baseURI = prop.getProperty("baseurl");
			String filepath = prop.getProperty("AccountManagement");

			//read user name and password
			ExcelUtils exceldata = new ExcelUtils();
			JSONObject requestParams  = exceldata.readRowField("GeneralData", "farmId", filepath);


			RequestSpecification request = RestAssured.given();
			requestParams.put("expenseCategory", "Labor"); 
			requestParams.put("amount",200);
			requestParams.put( "remarks", "Expense");
			requestParams.put( "Date", "2021-03-15");

			Response response = request.body(requestParams.toString())
					.header("Content-Type", "application/json")
					.header("Authorization","Bearer " + token)
					.post(abstractname);

			//Print response
			response.prettyPeek();

			//Validate status code
			Assert.assertEquals(200, response.getStatusCode());

			String jsonString = response.asString();
			String  message = JsonPath.from(jsonString).get("message");
			Assert.assertEquals("Expense Data updated successfully.", message);
		}			
	
}
