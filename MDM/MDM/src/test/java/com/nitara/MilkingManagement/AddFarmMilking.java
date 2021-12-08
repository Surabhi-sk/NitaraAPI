package com.nitara.MilkingManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.UserPreferenceManagement.SetUserMilkingPreferences;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class AddFarmMilking extends GenericBase
{
	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void addUserFarmMilking() throws Exception 
	{

		String abstractname = prop.getProperty("AddFarmMilking");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", filepath);
		JSONObject farmId = exceldata.readRowField("GeneralData", "farmId", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));

		SetUserMilkingPreferences userPref = new SetUserMilkingPreferences();
		userPref.setUserMilkingPreferences("Farm",usertoken);

		RequestSpecification request = RestAssured.given();
		farmId.put("MilkingDate", "2020-12-13");
		farmId.put("session", "morning");
		farmId.put("yield", 500); // Cast
		farmId.put("Fat", 5.5);
		farmId.put("snf", 8.5);
		farmId.put("priceOfMilk", 30);


		Response response = request.body(farmId.toString())
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
		Assert.assertEquals("Farm Milking added successfully.", message);

	}			
}
