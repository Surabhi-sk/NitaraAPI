package com.nitara.AccountManagement;


import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ViewFarmerHomePage extends GenericBase{

	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void viewFarmerHomePage() throws Exception {

		System.out.println("");
		String filepath = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("ViewFarmerHomePage"),usertoken);

		rs.prettyPeek();

		//System response
		System.out.println("Response:" + rs.asString());

		// Validate status code
		Assert.assertEquals(rs.getStatusCode(),200);

		String responseString = rs.asString();
		String  message = JsonPath.from(responseString).get("message");
		//Validate success message
		Assert.assertEquals(message,"Farmer Home Page");

	}
}
