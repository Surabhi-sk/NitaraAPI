/*User Story : Enhancement_Service Provider should be able to register a Farm - 1871
@Author : Surabhi
Description : Login with Service Provider credentials
			  Get farmId from AccountManagement.xlsx
			  Request FarmMetadata with farmId
*/

package com.nitara.FarmManagement;

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

public class GetFarmMetadata extends GenericBase{
	
	@Test(groups= {"Smoke"})
	public void getFarmMetadata() throws Exception 
	{

		String abstractname = prop.getProperty("GetFarmMetadata");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		//JSONObject requestParams = exceldata.readRowField("GeneralData", "farmId", filepath);
		
		
		JSONObject requestParams = new JSONObject();
		requestParams.put("farmId", "1843a7fe-b999-4e4b-97f6-c360670383be");

		RequestSpecification request = RestAssured.given();

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + usertoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		String jsonString = response.asString();
	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Farm Meta Data.");
		
		
	}	

}