package com.nitara.PermissionManagement;



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


public class GetUserDetails extends GenericBase{

	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void getUserDetails() throws Exception {

		String abstractname = prop.getProperty("GetUserDetails");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();

		//requestParams.put("userName", username.get("username"));
		requestParams.put("userName", "8050495045");//8340000255
		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				header("Authorization","Bearer " + token).post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User Details.", message);



	}


	public String getUserDetails(String username, String token) {


		String abstractname = prop.getProperty("GetUserDetails");
		RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("userName", username); // Cast

		Response response= request.body(requestParams.toString()).header("Content-Type", "application/json").
				header("Authorization","Bearer " + token).post(abstractname);

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		// Printing system response
		System.out.println("Response:" );
		response.prettyPeek();
		String jsonString = response.asString();

		// Comparing expected result with actual result
		Assert.assertEquals(200,  response.getStatusCode());

		return jsonString;

	}
}
