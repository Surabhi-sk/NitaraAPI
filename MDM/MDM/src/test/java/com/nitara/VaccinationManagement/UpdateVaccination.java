package com.nitara.VaccinationManagement;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import com.nitara.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateVaccination extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateVaccination() throws IOException {


		String abstractname = prop.getProperty("UpdateVaccination");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateVaccination","UpdateVaccination",filepath);


		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).contains(".jpg")) {
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
		Assert.assertEquals("Vaccination Data Updated Successfully.", message);
		

		//Assert the added details
		ViewVaccinationData cattle = new ViewVaccinationData();
		String res = cattle.viewVaccinationData(dataObject.getString("CattleId"), token);

		//Parse JSON response
		JSONObject cattleDetails = new JSONObject(res);
		JSONObject vaccinationData = cattleDetails.getJSONObject("vaccinationData");
		JSONArray vaccinations = vaccinationData.getJSONArray("vaccinations");

		JSONObject vaccine = vaccinations.getJSONObject(vaccinations.length()-1);
		Assert.assertEquals(dataObject.getString("VaccinationTypes"),vaccine.getString("vaccineType"));
		Assert.assertEquals(dataObject.getString("VaccinationBrand"),vaccine.getString("vaccineBrand"));
		Assert.assertEquals(dataObject.get("DosageInMl"),vaccine.getDouble("dosage"));
		String date = dataObject.getString("VaccinationDateTime");
		Assert.assertEquals(vaccine.getString("dateTime").contains(date),true);
		Assert.assertEquals(vaccine.getString("vaccinationProofFilePath").isEmpty(),false);


		System.out.println("Vaccination data updated.");

	}
	
	
	@Test(groups= {"Regression"})
	public void updateVaccinationFutureDate() throws IOException {
		

		String abstractname = prop.getProperty("UpdateVaccination");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");


		RequestSpecification request = RestAssured.given();

		
		
		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateVaccination","UpdateVaccinationFutureDate",filepath);


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
		
		Helper date = new Helper();
		request.formParam("VaccinationDateTime", date.getDate(1));

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());
		
		
		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		//Assert.assertEquals(error.length(),2);

		JSONArray obj = error.getJSONArray("VaccinationDateTime");
		Assert.assertEquals(obj.getString(0).contains("must not be greater than today"), true);

	}
	
	
	@Test(groups= {"Regression"})
	public void updateVaccinationMandatoryFields() throws IOException {

		String abstractname = prop.getProperty("AddVaccination");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");


		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateVaccination","UpdateVaccinationMandatoryFields",filepath);




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
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		//Assert.assertEquals(error.length(),2);

		JSONArray obj = error.getJSONArray("CattleIds");
		Assert.assertEquals(obj.getString(0),"The CattleIds field is required.");

		JSONArray obj1 = error.getJSONArray("VaccinationBrand");
		Assert.assertEquals(obj1 .getString(0),"The VaccinationBrand field is required.");

		
		JSONArray obj2 = error.getJSONArray("VaccinationTypes");
		Assert.assertEquals(obj2 .getString(0),"The VaccinationTypes field is required.");

	}

}


