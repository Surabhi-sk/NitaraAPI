package com.nitara.CattleRegistration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.FarmManagement.ViewFarmCattleList;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RegisterMilkingOrDryCattle extends GenericBase{

	NitaraService results = new NitaraService();
	public static Properties prop;

	@Test(groups= {"Smoke","Regression"})
	public void registerMilkingOrDryCattle() throws Exception {
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle"); //
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		//Update tag numbers in Registration.xlsx
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		exceldata.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		exceldata.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","RegisterMilkingDryCattle",filepath,external);

		request.header("Authorization","Bearer " + usertoken);
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
		String cattleId = JsonPath.from(jsonString).get("cattleId");

		//Write CattleId 
		filepath = prop.getProperty("CattleBreedingData");
		exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
		System.out.println(dataObject.getInt("currentLactation"));
		exceldata.writeIntegerData("GeneralData", "Lactation", dataObject.getInt("currentLactation"), filepath);
		System.out.println("Written");
	}
	
	

	@Test(groups= {"Regression"})
	public void milkingCattleTagNumbersExceedLength() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","TagNumbersExceedLength",filepath);

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
	public void milkingCattleTagNumbersAlreadyExist() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","TagNumberAlreadyExist",filepath);
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

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Tag Number already exists.");

	}


	@Test(groups= {"Regression"})
	public void milkingCattleCoopTagNumbersAlreadyExist() throws Exception {

		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);
		

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();


		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","CoopTagNumberAlreadyExist",filepath);

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
	public void milkingCattleYearOfBirthFutureDate() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","YoBFutureDate",filepath);
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

		JSONArray MinAge = error.getJSONArray("LastCalvingDate");
		Assert.assertEquals(MinAge.getString(0),"Cattle age is less than minimum age.");
		Assert.assertEquals(MinAge.getString(1),"Date must not be less than Date of Birth");

		JSONArray YearofBirth = error.getJSONArray("YearOfBirth");
		Assert.assertEquals(YearofBirth.getString(0),"Year and Month of Birth can not be a future date.");

		JSONArray InsemMinAge = error.getJSONArray( "CurrentLactation");
		Assert.assertEquals(InsemMinAge.getString(0),"Cattle age is less than minimum age.");
	}

	@Test(groups= {"Regression"})
	public void inseminationWithin15DaysofCalving() throws Exception {

		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis); 
		
		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");
		
		

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","InseminationWithin15DaysofCalving",filepath);
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

		JSONArray inseminationDate = error.getJSONArray("InseminationDate");
		Assert.assertEquals(inseminationDate.getString(0).contains("must not be less than Last Calving Date + 15 days."), true);
	}



	@Test(groups= {"Regression"})
	public void milkingCattlePDActivity() throws Exception {

		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		//Update tag numbers in Registration.xlsx
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		//ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","PDActivityEntry",filepath);
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

		//Assert error message
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

		}

		System.out.println(cattleId);
		ViewCattleBreedingActivity cattleDet = new ViewCattleBreedingActivity();
		String viewDetails = cattleDet.viewCattleBreedingActivity(cattleId,1,usertoken); 

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");

		String defaultPD = "No default PD entry";

		for(int i=0;i<breedingActivities.length();i++) {

			JSONObject activity = breedingActivities.getJSONObject(i);

			if(activity.getString("activity").equals("PD")) {
				defaultPD ="Default PD entry added";
			}

		}

		Assert.assertEquals(defaultPD,"Default PD entry added");


	}


	@Test(groups= {"Regression"})
	public void milkingCattleDryActivity() throws Exception {

		
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);
		
		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		//Update tag numbers in Registration.xlsx
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		//ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","DryPeriodActivityEntry",filepath);
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

		//Assert error message
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

		}

		System.out.println(cattleId);
		ViewCattleBreedingActivity cattleDet = new ViewCattleBreedingActivity();
		String viewDetails = cattleDet.viewCattleBreedingActivity(cattleId,1,usertoken); 

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");

		String defaultDry = "No default DryPeriod entry";

		for(int i=0;i<breedingActivities.length();i++) {

			JSONObject activity = breedingActivities.getJSONObject(i);

			if(activity.getString("activity").equals("Dry Period")) {
				defaultDry ="Default DryPeriod entry added";
				String date = dataObject.getString("DryPeriodDate");
				Assert.assertEquals(activity.getString("date").contains(date),true);
			}

		}

		Assert.assertEquals(defaultDry,"Default DryPeriod entry added");


	}


	@Test(groups= {"Regression"})
	public void milkingCattleDryPeriodBeforeCalving() throws Exception {

		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);

		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle"); //
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","DryPeriodBeforeCalving",filepath);
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

		JSONArray dryPeriodDate = error.getJSONArray("DryPeriodDate");
		Assert.assertEquals(dryPeriodDate.getString(0).contains("must not be less than Last Calving Date"), true);
	}

	@Test(groups= {"Regression"})
	public void milkingCattleCalvingFutureDate() throws Exception {

		/*RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","InseminationFutureDate",filepath);
		System.out.println(dataObject);
		request.header("Authorization","Bearer " + token);
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

		Assert.assertEquals(error.length(),3);*/



	}



	@Test(groups= {"Regression"})
	public void milkingCattleMandatoryFields() throws Exception {

		
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);
		
		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();


		//ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","MandatoryFields",filepath);
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

		if(response.getStatusCode()==200) {

			exceldata.updateField("GeneralData",filepath,"TagNumber"); 
			exceldata.updateField("GeneralData",filepath,"CooperativeTagNumber");

		}


		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		//Assert.assertEquals(error.length(),9);

		JSONArray breed = error.getJSONArray("Breed");
		Assert.assertEquals(breed.getString(0),"The Breed field is required.");

		JSONArray farmId = error.getJSONArray("FarmId");
		Assert.assertEquals(farmId.getString(0),"The FarmId field is required.");

		JSONArray tagNumber = error.getJSONArray( "TagNumber");
		Assert.assertEquals(tagNumber.getString(0),"The TagNumber field is required."); 

		JSONArray cattleType = error.getJSONArray( "CattleType");
		Assert.assertEquals(cattleType.getString(0),"The CattleType field is required."); 

		//JSONArray InsemMinAge = error.getJSONArray( "YearOfBirth");
		//Assert.assertEquals(InsemMinAge.getString(0),"Cattle aged 2020 years and 11 months should not exceed the maximum age of 25 years."); 


		JSONArray lastCalvingDate = error.getJSONArray( "LastCalvingDate");
		Assert.assertEquals(lastCalvingDate.getString(0),"The LastCalvingDate field is required.");

		/*JSONArray isCattleInseminated = error.getJSONArray( "IsCattleInseminated");
		Assert.assertEquals(isCattleInseminated.getString(0),"IsCattleInseminated is required due to IsCattlePregnant being equal to False");*/
	}


	@Test(groups= {"Regression"})
	public void milkingCattleShedGroupDataNotCorrect() throws Exception {
		
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);
		
		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();


		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		//ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","ShedGroupDataNotCorrect",filepath);
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
	public void milkingCattleMinimumAge() throws Exception {

		
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);
		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		//ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","MilkingCattleMinimumAge",filepath);
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

		Assert.assertEquals(error.length(),2);

		JSONArray MinAge = error.getJSONArray("LastCalvingDate");
		Assert.assertEquals(MinAge.getString(0),"Cattle age is less than minimum age.");

		JSONArray InsemMinAge = error.getJSONArray( "CurrentLactation");
		Assert.assertEquals(InsemMinAge.getString(0),"Cattle age is less than minimum age.");

	}

	@Test(groups= {"Regression"})
	public void milkingCattleMaximumAge() throws Exception {

		
		
		prop=new Properties();
		FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
		prop.load(fis);
		
		RestAssured.baseURI = prop.getProperty("baseurl");
		String abstractname = prop.getProperty("RegisterMilkingOrDryCattle");
		String filepath = prop.getProperty("RegressionData");
		String external = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		RequestSpecification request = RestAssured.given();

		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		var.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		var.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		//ExcelUtils exceldata = new ExcelUtils();
		JSONObject dataObject = exceldata.readCase("RegMilkingDryCattle","MilkingCattleMaximumAge",filepath);
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
}
