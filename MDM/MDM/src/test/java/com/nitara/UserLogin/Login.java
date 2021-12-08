package com.nitara.UserLogin;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Login{

	public String userToken( String username, String password) {

		//RestAssured.baseURI = prop.getProperty("baseurl");

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", username);
		requestParams.put("password", password);
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");

		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");

		Response response = request.post("/AM/Login");

		int  statusCode = response.getStatusCode();

		System.out.println("The status code recieved: " + statusCode);

		System.out.println("Response body: " + response.body().prettyPeek().asString());


		String jsonString = response.asString();
		String token = JsonPath.from(jsonString).get("token");
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("Logged in successfully.", message);

		return (token);
	}


	public String userLogin(String username, String password) {

		RestAssured.baseURI = "http://test.nitara.co.in";


		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", username);
		requestParams.put("Password", password);
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");

		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");

		Response response = request.post("/AM/Login");

		int  statusCode = response.getStatusCode();

		System.out.println("The status code recieved: " + statusCode);

		System.out.println("Response body: " + response.body().prettyPeek().asString());


		String jsonString = response.asString();


		return (jsonString);
	}


	@Test
	public void userLogin() throws Exception {

		RestAssured.baseURI = "http://test.nitara.co.in";
		String filepath = "src\\\\main\\\\java\\\\com\\\\nitara\\\\testdata\\\\AccountManagement.xlsx";

		//Excel read
		/*ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "password", filepath);


		String username = user.getString("username");
		String pssword = password.getString("password");*/

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("countryCode", "+91"); // Cast
		requestParams.put("phone", "8885468816");
		requestParams.put("Password", "password@123");
		requestParams.put("deviceName", "DELL_PC"); 		 
		requestParams.put("deviceType",  "LAPTOP");

		request.body(requestParams.toString());

		request.header("Content-Type", "application/json");

		Response response = request.post("/AM/Login");

		int  statusCode = response.getStatusCode();

		System.out.println("The status code recieved: " + statusCode);

		System.out.println("Response body: " + response.body().prettyPeek().asString());


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("Logged in successfully.", message);
		
		



	}
}
