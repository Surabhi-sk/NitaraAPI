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


public class ViewServiceProviderHomePage extends GenericBase{

	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void viewServiceProviderHomePage() throws Exception {
		
		String filepath = prop.getProperty("AccountManagement");
		
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("ViewServiceProviderHomePage"),usertoken);

		//Print Response
		rs.prettyPeek();

		//Validate Status code
		Assert.assertEquals(rs.getStatusCode(),200);

		String responseString = rs.asString();
		String  message = JsonPath.from(responseString).get("message");
		//Validate success message
		Assert.assertEquals(message,"Service Provider Home Page" );	
	}

}
