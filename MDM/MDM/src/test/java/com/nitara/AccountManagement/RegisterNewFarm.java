package com.nitara.AccountManagement;

import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.PermissionManagement.GetUserDetails;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RegisterNewFarm  extends GenericBase
{
	@Test
	public void registerNewFarm() throws Throwable
	{
		String abstractname= prop.getProperty("RegisterNewFarm");
		
		RestAssured.baseURI=prop.getProperty("baseurl");
		
		String filepath = prop.getProperty("AccountManagement");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		data.updateField("GeneralData",filepath,"phone"); 
		JSONObject dataObject= data.readData("RegisterFarm",filepath);
		System.out.println(dataObject);


		request.header("Authorization","Bearer " + token);
		
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String))
			{
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) 
				{
					request.multiPart(key, new File(dataObject.getString(key)));
				}
				else
				{
					request.formParam(key, dataObject.get(key));
				}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response response = request.post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());
		// Validate Status code
		Assert.assertEquals(response.getStatusCode(),200);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals(message,"Farm Created Successfully.");

		//Assert user details
		GetUserDetails userDetails = new GetUserDetails();
		String res = userDetails.getUserDetails(dataObject.getString("phone"),token);

		Map<String, String> detail= JsonPath.from(res).getMap("userDetail");
		//Assert.assertEquals(dataObject.getString("name"), detail.get("fullName"));
		//Assert.assertEquals(detail.get("userImagePath").isEmpty(),false);
		Assert.assertEquals(detail.get("farmRole"),"Owner");
	}
	
	
	
	@Test(groups= {"Regression"})
	public void RegisterFarmwithoutphonenumber() throws Throwable
	{
		String abstractname= prop.getProperty("RegisterNewFarm");
		
		RestAssured.baseURI=prop.getProperty("baseurl");
		
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils exceldata = new ExcelUtils();
	//	data.updateField("GeneralData",filepath,"phone"); 
		JSONObject dataObject = exceldata.readCase("RegisterNewFarm","RegisterFarmwithoutphonenumber",filepath);
		System.out.println(dataObject);


		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response response = request.post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Validate Status code
		Assert.assertEquals(400, response.getStatusCode());

		
		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals( error.length(),1);

		JSONArray tag = error.getJSONArray("Phone");
		Assert.assertEquals( "The Phone field is required.", tag.getString(0));
	}
	
	@Test(groups= {"Regression"})
	public void RegisterFarmwithoutname() throws Throwable
	{
		String abstractname= prop.getProperty("RegisterNewFarm");
		
		RestAssured.baseURI=prop.getProperty("baseurl");
		
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils exceldata = new ExcelUtils();
	//	exceldata.updateField("GeneralData",filepath,"phone"); 
		JSONObject dataObject = exceldata.readCase("RegisterNewFarm","RegisterFarmwithoutname",filepath);
		System.out.println(dataObject);


		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response response = request.post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Validate Status code
		Assert.assertEquals(400, response.getStatusCode());

		
		//Assert error message
				String jsonString = response.asString(); 
				JSONObject res = new JSONObject(jsonString);
				JSONObject error = res.getJSONObject("errors");

				Assert.assertEquals(error.length(),1);

				JSONArray tag = error.getJSONArray("Name");
				Assert.assertEquals( "The Name field is required.", tag.getString(0));
			
	}
	
	@Test(groups= {"Regression"})
	public void RegisterFarmwithduplicatephonenumber() throws Throwable
	{
		String abstractname= prop.getProperty("RegisterNewFarm");
		
		RestAssured.baseURI=prop.getProperty("baseurl");
		
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils exceldata = new ExcelUtils();
	//	data.updateField("GeneralData",filepath,"phone"); 
		JSONObject dataObject = exceldata.readCase("RegisterNewFarm","RegisterFarmwithduplicatephonenumber",filepath);
		System.out.println(dataObject);


		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response response = request.post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Validate Status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals("User already registered.", message);

		
	}
	
	
}
