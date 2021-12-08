/*User Story : Enhancement_Service Provider should be able to register a Farm - 1871
@Author : Surabhi
Description : Login with Service Provider credentials
			  Get farmId from AccountManagement.xlsx
			  Request FarmHousing data with farmId
*/

package com.nitara.HousingManagement;


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

public class GetFarmHousing extends GenericBase{
	
	@Test(groups= {"Smoke"})
	public void getFarmHousing() throws Exception 
	{

		String abstractname = prop.getProperty("GetFarmHousing");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		JSONObject requestParams = exceldata.readRowField("GeneralData", "farmId", filepath);

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
		Assert.assertEquals(message,"Farm Sheds");
		
		
	}	

}
