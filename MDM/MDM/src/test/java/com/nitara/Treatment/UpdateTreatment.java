package com.nitara.Treatment;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.CattleRegistration.RegisterBullCattle;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateTreatment extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateTreatment() throws Exception  {

		String abstractname = prop.getProperty("UpdateTreatment");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		/*AddTreatment treatmnt = new AddTreatment();
		treatmnt.addTreatment(token);*/

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateTreatment","UpdateTreatment",filepath);


		request.header("Authorization","Bearer " + token);
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

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Treatment Data Updated Successfully.", message);


		//Verify the added details
		ViewTreatmentData details = new ViewTreatmentData();
		String res = details.viewTreatmentData(dataObject.getString("cattleId"), token);

		//Parse json
		JSONObject cattleDetails = new JSONObject(res);
		JSONObject cattleTreatmentData = cattleDetails.getJSONObject("treatmentData");
		JSONArray treatment = cattleTreatmentData.getJSONArray("treatments");
		JSONObject obj = treatment.getJSONObject(0);
		
		String date = dataObject.getString("dateOfVisit");
		Assert.assertEquals(obj.getString("dateTime").contains(date), true);
		Assert.assertEquals(dataObject.getString("disease"), obj.getString("disease"));
		Assert.assertEquals(obj.getString("treatmentProofPath").isEmpty(),false);

		// Calculate next followUp date and assert
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate = format.parse(date);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DATE, dataObject.getInt("followupDays"));
        System.out.println(format.format((cal.getTime())));
        String followUpDate =  format.format((cal.getTime()));
        JSONArray followUps = obj.getJSONArray("followUps");
		obj = followUps.getJSONObject(0);

		Assert.assertEquals(obj.getString("followupDate").contains(followUpDate), true);
	}
	
	
	@Test(groups= {"Regression"})
	public void treatmentDoesNotExist() throws Exception {

		String abstractname = prop.getProperty("UpdateTreatment");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RegisterBullCattle cattle = new RegisterBullCattle();
		cattle.registerBullCattle(token);

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateTreatment","UpdateTreatmentwithValidData",filepath);


		request.header("Authorization","Bearer " + token);
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

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Treatment does not exist.");

	}
	
	@Test(groups= {"Regression"})
	public void addMedicineswithoutDosage() throws Exception {

		String abstractname = prop.getProperty("UpdateTreatment");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		AddTreatment treatmnt = new AddTreatment();
		treatmnt.addTreatment(token);

		RequestSpecification request = RestAssured.given();
		

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateTreatment","AddMedicineswithoutDosage",filepath);

		request.header("Authorization","Bearer " + token);
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

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray obj = error.getJSONArray("Medicines[1].Unit");
		Assert.assertEquals(obj.getString(0),"The Unit field is required.");

		obj = error.getJSONArray("Medicines[1].Dosage");
		Assert.assertEquals(obj.getString(0),"The Dosage field is required.");
	}
	
	@Test(groups= {"Regression"})
	public void followUpAfterRequired() throws Exception {

		String abstractname = prop.getProperty("UpdateTreatment");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");
		
		AddTreatment treatmnt = new AddTreatment();
		treatmnt.addTreatment(token);

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateTreatment","FollowUpAfterRequired",filepath);

		request.header("Authorization","Bearer " + token);
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

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals( response.getStatusCode(),400);

		///Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		JSONArray obj = error.getJSONArray("FollowupDays");
		//Assert.assertEquals(obj.getString(0),"FollowupDays is required due to IsFollowupRequired being equal to True");

	}
	
	
	
			
			@Test(groups= {"Regression"})
			public void updateTreatmentFutureDate() throws Exception {

				String abstractname = prop.getProperty("UpdateTreatment");
				RestAssured.baseURI = prop.getProperty("baseurl");
				String filepath = prop.getProperty("CattleOtherActivities");
				

				AddTreatment treatmnt = new AddTreatment();
				treatmnt.addTreatment(token);


				RequestSpecification request = RestAssured.given();

				//Read from excel to JSONObject
				ExcelUtils data = new ExcelUtils();
				JSONObject dataObject = data.readCase("UpdateTreatment","UpdateTreatmentFutureDate",filepath);
				
				Helper date = new Helper();
				request.formParam("dateOfVisit", date.getDate(1));


				request.header("Authorization","Bearer " + token);
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

				//Print response
				response.prettyPeek();

				//Validate status code
				Assert.assertEquals( response.getStatusCode(),400);

				///Assert error message
				String jsonString = response.asString(); 
				JSONObject res = new JSONObject(jsonString);
				JSONObject error = res.getJSONObject("errors");

				JSONArray obj = error.getJSONArray("DateOfVisit");
				//Assert.assertEquals(obj.getString(0),"FollowupDays is required due to IsFollowupRequired being equal to True");
				Assert.assertEquals(obj.getString(0).contains("must not be greater than today"), true);
			}
}
