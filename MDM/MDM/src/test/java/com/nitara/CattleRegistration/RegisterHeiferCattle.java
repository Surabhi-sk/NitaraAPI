package com.nitara.CattleRegistration;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.FarmManagement.ViewFarmCattleList;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class RegisterHeiferCattle extends GenericBase{


	@Test(groups= {"Smoke","Regression"})
	public void registerHeiferCattle() throws Exception {

		String abstractname = prop.getProperty("RegisterHeiferCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("Registration");
		


		RequestSpecification request = RestAssured.given();

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
	
		Login user = new Login();
	//	String usertoken = user.userToken(username.getString("username"),password.getString("Password"));

		//Update tag numbers in Registration.xlsx
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		exceldata.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		exceldata.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readData("RegHeiferCattle",filepath,external);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}

		}

		Response res = request.post(abstractname).then().extract().response();


		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals( res.getStatusCode(),200);



		//Validate success message
		String jsonString = res.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message, "Cattle Registered successfully." );

		/*//Get cattleId of newly registered bull
		ViewFarmCattleList cattle = new ViewFarmCattleList();
		String response = cattle.viewFarmCattleList(dataObject.getString("farmId"));

		JSONObject cattleList = new JSONObject(response);
		JSONArray farmCattle = cattleList.getJSONArray("farmCattle");

		String cattleId = "";
		String tagNumber = dataObject.getString("tagNumber");
		System.out.println(farmCattle.length());
		for(int i =0 ;i<farmCattle.length();i++) {
			JSONObject details = farmCattle.getJSONObject(i);
			if(tagNumber.equals(details.getString("tagNumber"))){
				cattleId = details.getString("cattleId");
			}

		}
		System.out.println(cattleId);

		if(cattleId.equals("")) {
			System.out.println("Newly registered cattle not found in the farmlist");
			Assert.assertEquals(cattleId.isEmpty(),false);	
		}*/

	}

	@Test(groups= {"Regression"})
	public void heiferMaximumAge() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","HeiferMaximumAge",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		if(response.getStatusCode()==200) {

			exceldata.updateField("GeneralData",filepath,"TagNumber"); 
			exceldata.updateField("GeneralData",filepath,"CooperativeTagNumber");

		}

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),1);

		JSONArray yearOfBirth = error.getJSONArray("YearOfBirth");
		Assert.assertEquals(yearOfBirth.getString(0).contains("should not exceed the maximum age of 25 years."), true);

	}

	@Test(groups= {"Regression"})
	public void heiferTagNumbersExceedLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","TagNumbersExceedLength",filepath);

		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),2);

		JSONArray tag = error.getJSONArray("TagNumber");
		Assert.assertEquals(tag.getString(0),"Ear Tag Number/Name must be between 1-10 characters.");

		JSONArray coopTag = error.getJSONArray("CooperativeTagNumber");
		Assert.assertEquals(coopTag.getString(0),"Cooperative Number should only consist of 12 digits.");
	}


	@Test(groups= {"Regression"})
	public void heiferCoopTagNumbersAlreadyExist() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","CoopTagNumberAlreadyExist",filepath);

		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Cooperative Number already exists.");
	}


	@Test(groups= {"Regression"})
	public void heiferYearOfBirthFutureDate() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","YoBFutureDate",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		if(response.getStatusCode()==200) {

			exceldata.updateField("GeneralData",filepath,"TagNumber"); 
			exceldata.updateField("GeneralData",filepath,"CooperativeTagNumber");

		}

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),3);

		JSONArray MinAge = error.getJSONArray("MinimumAge");
		Assert.assertEquals(MinAge.getString(0),"Heifer cannot be less than 6 months of age.");

		JSONArray YearofBirth = error.getJSONArray("YearOfBirth");
		Assert.assertEquals(YearofBirth.getString(0),"Year and Month of Birth can not be a future date.");

		JSONArray InsemMinAge = error.getJSONArray( "MinimumAgeForInsemination");
		Assert.assertEquals(InsemMinAge.getString(0),"Heifer cannot be less than 6 months of age.");
	}


	@Test(groups= {"Regression"})
	public void heiferMandatoryFields() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","MandatoryFields",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		if(response.getStatusCode()==200) {

			exceldata.updateField("GeneralData",filepath,"TagNumber"); 
			exceldata.updateField("GeneralData",filepath,"CooperativeTagNumber");

		}


		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		//Assert.assertEquals(error.length(),7);

		JSONArray breed = error.getJSONArray("Breed");
		Assert.assertEquals(breed.getString(0),"The Breed field is required.");

		JSONArray farmId = error.getJSONArray("FarmId");
		Assert.assertEquals(farmId.getString(0),"The FarmId field is required.");

		JSONArray tagNumber = error.getJSONArray( "TagNumber");
		Assert.assertEquals(tagNumber.getString(0),"The TagNumber field is required."); 

		JSONArray cattleType = error.getJSONArray( "CattleType");
		Assert.assertEquals(cattleType.getString(0),"The CattleType field is required."); 

		JSONArray YearOfBirth = error.getJSONArray( "YearOfBirth");
		Assert.assertEquals(YearOfBirth.getString(0).contains("should not exceed the maximum age of 25 years."), true);

		JSONArray obj = error.getJSONArray( "InseminationType");
		Assert.assertEquals(obj.getString(0),"Insemination Type is required if insemination date is provided.");

		/*obj = error.getJSONArray("IsCattleInseminated");
		Assert.assertEquals(obj.getString(0),"IsCattleInseminated is required if cattle is not pregnant and if Age > 9");*/

	}

	@Test(groups= {"Regression"})
	public void heiferTagNumberAlreadyExists() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","TagNumberAlreadyExist",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Tag Number already exists.");

	}



	@Test(groups= {"Regression"})
	public void heiferShedGroupDataNotCorrect() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","ShedGroupDataNotCorrect",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		if(response.getStatusCode()==200) {

			exceldata.updateField("GeneralData",filepath,"TagNumber"); 
			exceldata.updateField("GeneralData",filepath,"CooperativeTagNumber");

		}


		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Shed Or Group Data incorrect.");

	}


	@Test(groups= {"Regression"})
	public void heiferInseminationMinimumAge() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","InseminationMinimumAge",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		if(response.getStatusCode()==200) {

			exceldata.updateField("GeneralData",filepath,"TagNumber"); 
			exceldata.updateField("GeneralData",filepath,"CooperativeTagNumber");

		}

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		Assert.assertEquals(error.length(),1);

		JSONArray MinAge = error.getJSONArray("MinimumAgeForInsemination");
		Assert.assertEquals(MinAge.getString(0),"Heifer cannot be less than 6 months of age.");

	}




	@Test(groups= {"Regression"})
	public void heiferPDActivity() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterHeiferCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterHeifer","PDActivityEntry",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));}
				else {
					request.formParam(key, dataObject.get(key));	}
			else {
				request.formParam(key, dataObject.get(key));}
		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattle Registered successfully.", message);
		String cattleId = JsonPath.from(jsonString).get("cattleId");


		/*//Assert error message
		ViewFarmCattleList cattle = new ViewFarmCattleList();
		String res = cattle.viewFarmCattleList(dataObject.getString("farmId"));

		JSONObject cattleList = new JSONObject(res);
		JSONArray farmCattle = cattleList.getJSONArray("farmCattle");

		String cattleId = "";
		String tagNumber = dataObject.getString("tagNumber");
		System.out.println(farmCattle.length());
		for(int i =0 ;i<farmCattle.length();i++) {
			JSONObject details = farmCattle.getJSONObject(i);
			if(tagNumber.equals(details.getString("tagNumber"))){
				cattleId = details.getString("cattleId");
			}

		}*/

		ViewCattleBreedingActivity cattleDet = new ViewCattleBreedingActivity();
		String viewDetails = cattleDet.viewCattleBreedingActivity(cattleId,0,usertoken); 

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");

		String defaultPD = "No default PD entry";

		for(int i=0;i<breedingActivities.length();i++) {

			JSONObject activity = breedingActivities.getJSONObject(i);

			if(activity.getString("activity").equals("PD")) {
				defaultPD ="Default PD entry added";

				//Assert.assertEquals(activity.getString("date").contains(date),true);
			}

		}

		Assert.assertEquals(defaultPD,"Default PD entry added");


	}

}
