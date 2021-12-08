/*User Story : Enhancement_Service Provider should be able to register a Farm - 1871
@Author : Surabhi
Description : Search for farm using NitaraFarmId stored in AccountManagement.xlx using Service Provider credentials
*/

package com.nitara.FarmManagement;


import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SearchFarm extends GenericBase
{
	
	
	@Test(groups= {"Smoke"})
	public void searchFarm() throws Exception 
	{

		String abstractname = prop.getProperty("SeachRegisteredFarm");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		JSONObject params = exceldata.readRowField("GeneralData", "NitaraFarmId", filepath);
		
		JSONObject requestParams = new JSONObject();
		requestParams.put("searchString", params.get("NitaraFarmId")); 

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
		JSONArray res = new JSONArray(jsonString);
		JSONObject farm = res.getJSONObject(0);
		System.out.println(farm);
		Assert.assertEquals(farm.get("nitaraFarmId"),params.get("NitaraFarmId"));
		
		
	}	
}
