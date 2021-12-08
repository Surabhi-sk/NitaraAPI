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

public class ChangePassword extends GenericBase{

	@Test(groups= {"Smoke"})
	public void changePassword() throws Exception {

		/*String abstractname = prop.getProperty("ChangePassword");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//Excel read
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject user = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);


		String username = user.getString("SPusername");
		String oldPassword = password.getString("SPpassword");

		Login userlogin = new Login();
		String usertoken = userlogin.userToken(username,oldPassword);


		String newPassword = "Password@234" ;

		RequestSpecification request = RestAssured.given();

		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("OldPassword",oldPassword);
		requestParams.put("NewPassword", newPassword);
		requestParams.put("ConfirmNewPassword", newPassword);

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Password Changed", message);


		//login with newPassword
		userlogin.userToken(username, newPassword);

		//update password in excel
		exceldata.writeStringData("GeneralData","SPpassword", newPassword,filepath); 
		System.out.println("Login with new password successful");*/
		
	}


}
