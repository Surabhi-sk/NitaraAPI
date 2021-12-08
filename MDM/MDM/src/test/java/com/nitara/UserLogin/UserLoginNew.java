package com.nitara.UserLogin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class UserLoginNew extends Properties{


	public Properties prop;

	@Test(groups= {"Regression","Smoke"})
	public void userLoginwithValidData() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		RestAssured.baseURI = "http://test.nitara.co.in";
		String filepath = prop.getProperty("AccountManagement");
		String abstractname = prop.getProperty("UserLoginV2");

		//Excel read
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		String usrname = user.getString("SPusername");
		String pssword = password.getString("SPpassword");

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", "2110349288");
		requestParams.put("Password","password@123");
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");
		requestParams.put("key",  "com.nitara.serviceprovider");
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals(message,"Logged in successfully.");


	}
	
	@Test(groups= {"Regression"})
	public void SPLoginWithFarmerkey() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		RestAssured.baseURI = "http://test.nitara.co.in";
		String filepath = prop.getProperty("AccountManagement");
		String abstractname = prop.getProperty("UserLoginV2");

		//Excel read
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		String username = user.getString("SPusername");
		String pssword = password.getString("SPpassword");

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", username);
		requestParams.put("Password", pssword);
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");
		requestParams.put("key",  "com.nitara.farmer");
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),401);


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals(message,"User cannot login as a Farmer.");


	}
	
	
	@Test(groups= {"Regression"})
	public void farmerLoginWithSPkey() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		RestAssured.baseURI = "http://test.nitara.co.in";
		String filepath = prop.getProperty("AccountManagement");
		String abstractname = prop.getProperty("UserLoginV2");

		//Excel read
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", filepath);

		String username = user.getString("username");
		String pssword = password.getString("Password");

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", username);
		requestParams.put("Password", pssword);
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");
		requestParams.put("key",  "com.nitara.serviceprovider");
		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),401);


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals(message,"User cannot login as a Service Provider.");


	}

	@Test(groups= {"Regression"})
	public void loginwithInvalidPhone() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis); 

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("UserLoginNew");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserLoginNew","LoginwithInvalidPhone",filepath);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),401);

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Wrong Username or Password.", message);	
	}


	@Test(groups= {"Regression"})
	public void loginwithInvalidPassword() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("UserLoginNew");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserLoginNew","LoginwithInvalidPassword",filepath);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),401);

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Wrong Username or Password.");

	}


	@Test(groups= {"Regression"})
	public void loginwithBlankCredentials() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("UserLoginNew");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserLoginNew","LoginwithBlankCredentials",filepath);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),2);

		JSONArray phone = error.getJSONArray("Phone");
		Assert.assertEquals(phone.getString(0),"The Phone field is required.");

		JSONArray password = error.getJSONArray("Password");
		Assert.assertEquals( password.getString(0),"The Password field is required.");

	}

}
