package com.nitara.UserRegistration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserRegistration extends GenericBase{


	@Test(groups= {"Regression"})
	public void registerUserAlreadyRegistered() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterUser");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserRegistration","RegisterUserAlreadyregistered",filepath);
		System.out.println(requestParams);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User is already registered as Farmer.", message);	
	}

	@Test(groups= {"Regression"})
	public void registerUserMandatoryFields() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterUser");
		

		RequestSpecification request = RestAssured.given();

		
		JSONObject requestParams = new JSONObject();
		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),6);

		JSONArray arr = error.getJSONArray("Phone");
		Assert.assertEquals("The Phone field is required.", arr.getString(0));

		arr = error.getJSONArray("Role");
		Assert.assertEquals("The Role field is required.", arr.getString(0));

		arr = error.getJSONArray("Location");
		Assert.assertEquals("The Location field is required.", arr.getString(0));

		arr = error.getJSONArray("DeviceName");
		Assert.assertEquals("The DeviceName field is required.", arr.getString(0));

		arr = error.getJSONArray("CountryCode");
		Assert.assertEquals("The CountryCode field is required.", arr.getString(0));

		arr = error.getJSONArray("Name");
		Assert.assertEquals("The Name field is required.", arr.getString(0));

	}
	

	@Test(groups= {"Regression"})
	public void registerUserExceedLengthPhonenName() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterUser");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserRegistration","RegisterUserExceedLengthPhone&Name",filepath);
		System.out.println(requestParams);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),2);

		JSONArray arr = error.getJSONArray("Phone");
		Assert.assertEquals("The field Phone must be a string or array type with a maximum length of '10'.", arr.getString(0));

		arr = error.getJSONArray("Name");
		Assert.assertEquals("The field Name must be a string or array type with a maximum length of '10'.", arr.getString(0));
	}


	@Test(groups= {"Regression"})
	public void setPasswordExceedLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("SetPasswordForUser");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserRegistration","SetPasswordExceedLength",filepath);

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + token)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());


		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		JSONArray password = error.getJSONArray("Password");
		Assert.assertEquals("Maximum 15 characters allowed.", password.getString(0));

	}

	@Test(groups= {"Regression"})
	public void setPasswordMandatoryFields() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("SetPasswordForUser");


		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + token)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());


		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),3);

		JSONArray name = error.getJSONArray("UserName");
		Assert.assertEquals("The UserName field is required.", name.getString(0));

		JSONArray password = error.getJSONArray("Password");
		Assert.assertEquals("The Password field is required.", password.getString(0));

		JSONArray cpassword = error.getJSONArray("ConfirmPassword");
		Assert.assertEquals("The ConfirmPassword field is required.", cpassword.getString(0));


	}



	@Test(groups= {"Regression"})
	public void setPasswordMinimumLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("SetPasswordForUser");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserRegistration","SetPasswordMinimumLength",filepath);

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + token)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());


		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		JSONArray password = error.getJSONArray("Password");
		Assert.assertEquals("Minimum 8 characters allowed.", password.getString(0));
	}


	@Test(groups= {"Regression"})
	public void passwordnConfirmPasswordDoNotMatch() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("SetPasswordForUser");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("UserRegistration","ConfirmPassword&PasswordDoNotMatch",filepath);

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + token)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());


		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		JSONArray password = error.getJSONArray("ConfirmPassword");
		Assert.assertEquals("'ConfirmPassword' and 'Password' do not match.", password.getString(0));
	}



}
