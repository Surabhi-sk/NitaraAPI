package com.nitara.AccountManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*public class ReactivateAccount extends GenericBase{

	@Test(groups= {"Smoke"})
	public void reactivateAccount() throws Exception {

		String abstractname = prop.getProperty("ReactivateAccount");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "password", filepath);
		
		/*DeactivateAccount deactivate = new DeactivateAccount();
		deactivate.deactivateAccount(username.getString("username"),password.getString("password"));

		Login user = new Login();
		String Admintoken =user.userToken("1234567890","gormal@123456");

		RequestSpecification request = RestAssured.given();
		request.body(username.toString());

		Response response = request.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + Admintoken)
				.post(abstractname);

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		// Printing system response
		System.out.println("Response:" + response.getBody().prettyPeek().asString());

		// Comparing expected result with actual result
		Assert.assertEquals(response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Account Reactivated." );
		
		//Check if reactivated user is able to login
		String res = user.userLogin(username.getString("username"),password.getString("password"));
		String  loginMessage = JsonPath.from(res).get("message");
		Assert.assertEquals(loginMessage,"Logged in successfully.");

	}
}*/
