package com.nitara.UserLogin;


import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.PermissionManagement.GetUserDetails;
import com.nitara.UserRegistration.SetPasswordForUser;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class LoginReg extends GenericBase{

	

	@Test(groups= {"Regression"})
	public void loginwithInvalidPassword() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("Login");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","LoginwithInvalidPassword",filepath);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Invalid Password.", message);

	}

	@Test(groups= {"Regression"})
	public void loginwithBlankCredentials() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("Login");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","LoginwithBlankCredentials",filepath);

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

		JSONArray phone = error.getJSONArray("Phone");
		Assert.assertEquals("The Phone field is required.", phone.getString(0));

		JSONArray password = error.getJSONArray("Password");
		Assert.assertEquals("The Password field is required.", password.getString(0));

	}

	@Test(groups= {"Regression"})
	public void otpLoginwithInvalidPhone() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("LoginWithOtp");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject data = exceldata.readCase("Login","OtpLoginwithInvalidPhone",filepath);

		JSONObject requestParams = new JSONObject();
		requestParams.put("userName",data.getString("phone"));
		requestParams.put("OTP","1111");

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User not found in Database. Please go to New User & register your account.", message);	
	}
	
	
	@Test(groups= {"Regression"})
	public void otpLoginwithInvalidOtp() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("LoginWithOtp");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject data = exceldata.readCase("Login","OtpLoginwithInvalidOtp",filepath);

		JSONObject requestParams = new JSONObject();
		requestParams.put("userName",data.getString("phone"));
		requestParams.put("OTP","87654");

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("OTP is invalid.", message);	
	}


	@Test(groups= {"Regression"})
	public void generateOtpwithInvalidUsername() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("GenerateOtpForLogin");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","GenerateOtpwithInvalidPhone",filepath);
		System.out.println(requestParams);

		Response response = request.body(requestParams.toString()).header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User not found in Database. Please go to New User & register your account.", message);	
	}


	@Test(groups= {"Regression"})
	public void forgotPasswordInvalidPhone() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ForgotPassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","ForgotPasswordInvalidPhone",filepath);

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + token)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());


		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User does not exist.", message);	
	}

	@Test(groups= {"Regression"})
	public void forgotPasswordExceedLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ForgotPassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","ForgotPasswordExceedLength",filepath);

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
	public void forgotPasswordBlankFields() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ForgotPassword");
		//String filepath = prop.getProperty("RegressionData");

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
	public void forgotPasswordMinimumLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ForgotPassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","ForgotPasswordMinimumLength",filepath);

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
		String abstractname = prop.getProperty("ForgotPassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject requestParams = exceldata.readCase("Login","ConfirmPassword&PasswordDoNotMatch",filepath);

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


	@Test(groups= {"Regression"})
	public void changePasswordExceedLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ChangePassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readCase("Login","ChangePasswordExceedLength",filepath);
		System.out.println(user);

		String username = user.getString("username");
		String oldPassword = user.getString("password");

		Login userlogin = new Login();
		String usertoken = userlogin.userToken(username,oldPassword);

		JSONObject requestParams = new JSONObject();
		requestParams.put("OldPassword", oldPassword);
		requestParams.put("NewPassword", user.getString("newPassword"));
		requestParams.put("ConfirmNewPassword", user.getString("confirmNewPassword"));

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + usertoken)
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

		JSONArray password = error.getJSONArray("NewPassword");
		Assert.assertEquals("Maximum 15 characters allowed.", password.getString(0));

	}


	@Test(groups= {"Regression"})
	public void changePasswordMinimumLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ChangePassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readCase("Login","ChangePasswordMinimumLength",filepath);

		String username = user.getString("username");
		String oldPassword = user.getString("password");

		Login userlogin = new Login();
		String usertoken = userlogin.userToken(username,oldPassword);

		JSONObject requestParams = new JSONObject();
		requestParams.put("OldPassword", oldPassword);
		requestParams.put("NewPassword", user.getString("newPassword"));
		requestParams.put("ConfirmNewPassword", user.getString("confirmNewPassword"));

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + usertoken)
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

		JSONArray password = error.getJSONArray("NewPassword");
		Assert.assertEquals("Minimum 8 characters allowed.", password.getString(0));
	}


	@Test(groups= {"Regression"})
	public void newPasswordnConfirmNewPasswordDoNotMatch() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ChangePassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readCase("Login","ConfirmNewPassword&NewPasswordDoNotMatch",filepath);


		String username = user.getString("username");
		String oldPassword = user.getString("password");

		Login userlogin = new Login();
		String usertoken = userlogin.userToken(username,oldPassword);

		JSONObject requestParams = new JSONObject();
		requestParams.put("OldPassword", oldPassword);
		requestParams.put("NewPassword", user.getString("newPassword"));
		requestParams.put("ConfirmNewPassword", user.getString("confirmNewPassword"));

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + usertoken)
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

		JSONArray password = error.getJSONArray("ConfirmNewPassword");
		Assert.assertEquals("'ConfirmNewPassword' and 'NewPassword' do not match.", password.getString(0));
	}


	@Test(groups= {"Regression"})
	public void changePasswordIncorrectOldPassword() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ChangePassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readCase("Login","ChangePasswordIncorrectOldPassword",filepath);


		String username = user.getString("username");
		String oldPassword = "password";

		Login userlogin = new Login();
		String usertoken = userlogin.userToken(username,oldPassword);

		JSONObject requestParams = new JSONObject();
		requestParams.put("OldPassword", "fgg");
		requestParams.put("NewPassword", user.getString("newPassword"));
		requestParams.put("ConfirmNewPassword", user.getString("confirmNewPassword"));
		System.out.println(requestParams);

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + usertoken)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Incorrect password. ", message);	
	}


	@Test(groups= {"Regression"})
	public void newPasswordSameAsExisting() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("ChangePassword");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readCase("Login","NewPasswordSameAsExisting",filepath);


		String username = user.getString("username");
		String oldPassword = "password";

		Login userlogin = new Login();
		String usertoken = userlogin.userToken(username,oldPassword);

		JSONObject requestParams = new JSONObject();
		requestParams.put("OldPassword", user.getString("password"));
		requestParams.put("NewPassword", user.getString("newPassword"));
		requestParams.put("ConfirmNewPassword", user.getString("confirmNewPassword"));
		System.out.println(requestParams);

		Response response = request.body(requestParams.toString())
				.header("Authorization","Bearer " + usertoken)
				.header("Content-Type", "application/json")
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("New Password cannot be same as the existing password.", message);	
	}
	
	

}
