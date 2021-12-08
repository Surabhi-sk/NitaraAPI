package com.nitara.InseminationManagement;

import java.io.File;


import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CalvingManagement.AddCalving;
import com.nitara.CattleDetails.ViewCattleBreedingActivity;
import com.nitara.CattleRegistration.RegisterMilkingOrDryCattle;
import com.nitara.HeatManagement.AddHeat;
import com.nitara.PDManagement.AddPD;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddInseminationByServiceProvider extends GenericBase{

	@Test(groups= {"Smoke"})
	public void addInseminationByServiceProvider() throws Exception {

		String abstractname = prop.getProperty("AddInseminationByServiceProvider");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");
		

		RequestSpecification request = RestAssured.given();
		
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "SPusername", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "SPpassword", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("SPusername"),password.getString("SPpassword"));

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		filepath = prop.getProperty("CattleBreedingData");
		JSONObject dataObject = data.readCase("AddInseminationBySP","AddInseminationbySPwithValidData",filepath);

	request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
					System.out.println( dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
				System.out.println( dataObject.get(key));
			}

		}
		Response response = request.post(abstractname).then().extract().response();


		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());


		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("Insemination data added Successfully.", message);


	
		///Assert the updated changes
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		JSONObject lactation = data.readRowField("GeneralData","Lactation",filepath);
		String viewDetails = cattle.viewCattleBreedingActivity(dataObject.getString("CattleId"),lactation.getInt("Lactation"),token); 

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		JSONObject activity = breedingActivities.getJSONObject(breedingActivities.length()-1);

		Assert.assertEquals("Insemination",activity.getString("activity"));
		Assert.assertEquals("Artificial",activity.getString("inseminationType"));
		Assert.assertEquals(dataObject.getString("semenBrand"),activity.getString("semenBrand"));
		Assert.assertEquals(dataObject.getString("bullId"),activity.getString("bullId"));
		Assert.assertEquals(dataObject.getString("breedCode"),activity.getString("breedCode"));
		Assert.assertEquals(dataObject.getString("semenStation"),activity.getString("semenStation"));
		Assert.assertEquals(dataObject.getInt("dayOfYear"),activity.getInt("dayOfYear"));
		Assert.assertEquals(dataObject.getInt("dateOfProduction"),activity.getInt("dateOfProduction"));
		Assert.assertEquals(dataObject.getString("ejaculationNumber"),activity.getString("ejaculationNumber"));
		
		String date = dataObject.getString("inseminationDate");
		Assert.assertEquals(activity.getString("date").contains(date),true);
		
	}
	
	
	@Test(groups= {"Regression"})
	public void AddInseminationwithFutureDate() throws Exception {

		String abstractname = prop.getProperty("AddInseminationByServiceProvider");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParam = data.readCase("AddInseminationBySP","AddInseminationbySP",filepath);
		Helper date = new Helper();
		
		requestParam.put("inseminationDate",date.getDate(5));


		Response response = request.body(requestParam.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray( "InseminationDate");
		Assert.assertEquals( obj.getString(0).contains("must not be greater than today"),true);
	}
	
	
	@Test(groups= {"Regression"})
	public void AddInseminationBeforeLastHeat() throws Exception {

		String abstractname = prop.getProperty("AddInseminationByServiceProvider");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();
		
		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();
		Helper date = new Helper();
		
		AddHeat heat = new AddHeat();
		String heatDate = date.getDate(-16);
		heat.addHeatData(heatDate, token);
		
		//Read from excel to JSONArray
		ExcelUtils data = new ExcelUtils();
		JSONObject requestParam = data.readCase("AddInseminationBySP","AddInseminationbySP",filepath);
		
		
		requestParam.put("inseminationDate",date.getDate(-17));

		System.out.println(requestParam);
		Response response = request.body(requestParam.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray( "InseminationDate");
		Assert.assertEquals( obj.getString(0).contains("must not be greater than today"),true);
		
	}
	
	
	
	@Test(groups= {"Regression"})
	public void addInseminationDataWithin15DaysofCalving() throws Exception {

		String abstractname = prop.getProperty("AddInseminationByServiceProvider"); //
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();


		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();

		

		Helper date = new Helper();

		AddInsemination in = new AddInsemination();
		String InsemDate =  date.getDate(-280);
		in.AddInseminationData(InsemDate,token);

		AddPD pd = new AddPD();
		String PDdate = date.getDate(-250);
		pd.addPD(true, PDdate, token);

		AddCalving calving = new AddCalving();
		String calvingdate = date.getDate(-10);
		calving.addCalvingData(token, calvingdate);
		
		JSONObject requestParams = data.readCase("AddInseminationBySP","AddInseminationbySP",filepath);
		System.out.println(requestParams);

		requestParams.put("inseminationDate", date.getDate(-1));

		
		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);


		//Validate error message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Insemination Date cannot be within 15 days of Last Calving Date.", message);


	}

	
	@Test(groups= {"Regression"})
	public void addNewInseminationDataAfterPregnantPDResult() throws Exception {

		String abstractname = prop.getProperty("AddInseminationByServiceProvider"); //
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleBreedingData");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		

		RegisterMilkingOrDryCattle cat = new RegisterMilkingOrDryCattle();
		cat.registerMilkingOrDryCattle();

		Helper date = new Helper();

		AddInsemination in = new AddInsemination();
		String InsemDate =  date.getDate(-23);
		in.AddInseminationData(InsemDate, token);

		AddPD pd = new AddPD();
		String PDdate = date.getDate(-2);
		pd.addPD(true, PDdate, token);

		
		JSONObject requestParams = data.readCase("AddInseminationBySP","AddInseminationbySP",filepath);
		System.out.println(requestParams);

		requestParams.put("inseminationDate", date.getDate(-1));


		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Insemination data added Successfully.");

		//Assert the updated changes
		ViewCattleBreedingActivity cattle = new ViewCattleBreedingActivity();
		String viewDetails = cattle.viewCattleBreedingActivity(requestParams.getString("CattleId"),1,token);

		JSONObject cattleDetails = new JSONObject(viewDetails);
		JSONObject cattleMilkingData = cattleDetails.getJSONObject("cattleMilkingData");
		JSONArray breedingActivities = cattleMilkingData.getJSONArray("breedingActivities");
		JSONObject activity = breedingActivities.getJSONObject(breedingActivities.length()-3);

		Assert.assertEquals("Not Pregnant",activity.getString("pdResult"));


	}


}
