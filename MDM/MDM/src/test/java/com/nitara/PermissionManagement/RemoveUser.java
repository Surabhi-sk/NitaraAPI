package com.nitara.PermissionManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RemoveUser extends GenericBase{

	@Test(groups= {"Smoke"})
	public void removeUser() throws Exception {
		/*String abstractname = prop.getProperty("RemoveUser");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "password", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("password"));

		JSONObject userPhone = exceldata.readRowField("GeneralData", "FarmHelpUsername", filepath);
		System.out.println(userPhone);

		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("userName", userPhone.getString("FarmHelpUsername"));
		
		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				header("Authorization","Bearer " + usertoken).post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());*/
	}
}
