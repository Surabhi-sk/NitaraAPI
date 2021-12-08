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

public class ForgotPassword extends GenericBase{

	@Test(groups= {"Smoke"})
	public void forgotPassword() throws Exception {

		/*String abstractname = prop.getProperty("ForgotPassword");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		RequestSpecification request = RestAssured.given();


		//Excel read
		/*ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "SPusername", filepath);


		String username = user.getString("SPusername");
		String newPassword = "password@123";


		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("username", "8885468816"); // Cast	
		requestParams.put("password", "password@123");
		requestParams.put("confirmPassword", "password@123");


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Password created successfully.", message);*/

		//login with newPassword
		/*Login userlogin = new Login();
		userlogin.userToken(username, newPassword);

		//update password in excel
		exceldata.writeStringData("GeneralData","password", newPassword,filepath); */

	}

}
