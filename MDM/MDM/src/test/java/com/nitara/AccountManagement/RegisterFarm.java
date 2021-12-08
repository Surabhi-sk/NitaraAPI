/*User Story : Enhancement_Service Provider should be able to register a Farm - 1871
@Author : Surabhi
RegisterNewFarm : Login with SP credentials,
 				  Register Farm,
 				  Write ownerId, NitaraFarmId to AccountManagement.xlsx  
 				  Get farmId,shedId, groupId from GetUserDetails API and write to excel

 */
package com.nitara.AccountManagement;

import java.io.File;

import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.nitara.PermissionManagement.GetUserDetails;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;



import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RegisterFarm extends GenericBase{

	@Test(groups= {"Smoke","Regression"})
	public void RegisterNewFarmSP() throws Exception {
		
		System.out.println("Register Farm");
		String abstractname = prop.getProperty("RegisterNewFarm");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		RequestSpecification request = RestAssured.given();
		ExcelUtils exceldata = new ExcelUtils();
		//Update phone number in AccountManagement.xlsx
		ExcelUtils var = new ExcelUtils();
		String phoneNo = var.generateNo(10);
		exceldata.writeStringData("GeneralData","phone",phoneNo, filepath);



		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		JSONObject dataObject= exceldata.readData("RegisterFarm",filepath);
		System.out.println(dataObject);


		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response response = request.post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Validate Status code
		Assert.assertEquals(response.getStatusCode(),200 );

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals(message,"Farm Created Successfully.");

		JSONObject res = new JSONObject(jsonString);
		Assert.assertEquals(res.get("farmOwnerPhoneNumber"),dataObject.get("phone"));
		Assert.assertEquals(res.get("farmOwnerName"),dataObject.get("name"));
		Assert.assertEquals(res.getString("farmOwnerProfilePicPath").isEmpty(),false);

		// write ownerId and NitaraFarmId to AccountManagement file
		String NitaraFarmId = res.getString("nitaraFarmId");
		exceldata.writeStringData("GeneralData","NitaraFarmId", NitaraFarmId, filepath);

		String ownerId = res.getString("farmOwnerId");
		exceldata.writeStringData("GeneralData","farmOwnerId", ownerId, filepath);


		//Get farmId,shedId, groupId from GetUserDetails API
		GetUserDetails userDetails = new GetUserDetails();
		String userData = userDetails.getUserDetails(dataObject.getString("phone"),usertoken);

		Map<String, String> detail= JsonPath.from(userData).getMap("userDetail");


		String regressionFilepath = prop.getProperty("RegressionData");

		// write phone,farm,shed,group to AccountManagement & RegressionData file
		String usr = detail.get("phone");
		exceldata.writeStringData("GeneralData","username", usr, filepath);
		exceldata.writeStringData("GeneralData","farmerUsername", usr, regressionFilepath);
		String farmId = detail.get("farm");
		exceldata.writeStringData("GeneralData","farmId", farmId, filepath);
		exceldata.writeStringData("GeneralData","farmId", farmId, regressionFilepath);
		String shedId = detail.get("shed");
		exceldata.writeStringData("GeneralData","shedId", shedId, filepath);
		exceldata.writeStringData("GeneralData","shedId", shedId, regressionFilepath);
		String groupId = detail.get("group");
		exceldata.writeStringData("GeneralData","groupId", groupId, filepath);
		exceldata.writeStringData("GeneralData","groupId", groupId, regressionFilepath);
	}

	

	public void RegisterFarmSP() throws Exception {

		System.out.println("Register New Farm");
		String abstractname = prop.getProperty("RegisterFarm");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();	
		//Update phone number in AccountManagement.xlsx
		ExcelUtils var = new ExcelUtils();
		String phoneNo = var.generateNo(10);
		data.writeStringData("GeneralData","phone",phoneNo, filepath);

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		JSONObject dataObject= data.readData("RegisterFarm",filepath);
		System.out.println(dataObject);


		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response response = request.post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Validate Status code
		Assert.assertEquals(response.getStatusCode(),200 );

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");

		//Validate success message
		Assert.assertEquals(message,"User Added Successfully.");

		/*//SetPassword
		SetPasswordForUser us = new SetPasswordForUser();
		us.setPasswordForUser(dataObject.getString("phone"),usertoken);

		//Assert user details
		GetUserDetails userDetails = new GetUserDetails();
		String res = userDetails.getUserDetails(dataObject.getString("phone"),token);

		Map<String, String> detail= JsonPath.from(res).getMap("userDetail");
		//Assert.assertEquals(dataObject.getString("name"), detail.get("fullName"));
		//Assert.assertEquals(detail.get("userImagePath").isEmpty(),false);
		Assert.assertEquals(detail.get("farmRole"),"Owner");*/



	}
}
