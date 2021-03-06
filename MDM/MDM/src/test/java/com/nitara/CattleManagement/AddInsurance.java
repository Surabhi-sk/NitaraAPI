package com.nitara.CattleManagement;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddInsurance extends GenericBase{

	@Test(groups= {"Smoke"})
	public void addInsurance() throws IOException {

		String abstractname = prop.getProperty("AddInsurance");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readData("AddInsurance",filepath);


		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {
				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf|zip))$)")) {
					System.out.println(dataObject.getString(key));
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
		Assert.assertEquals(response.getStatusCode(),200);


		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Insurance Data added successfully.");


		//Verify the added details
		ViewInsuranceData details = new ViewInsuranceData();
		String res = details.viewInsuranceData(dataObject.getString("CattleId"), token);

		//Parse json
		JSONObject cattleDetails = new JSONObject(res);
		JSONObject cattleInsuranceData = cattleDetails.getJSONObject("insuranceData");
		JSONArray insurances = cattleInsuranceData.getJSONArray("insurances");
		JSONObject obj = insurances.getJSONObject(insurances.length()-1);


		String date = dataObject.getString("StartDate");
		Assert.assertEquals(obj.getString("insuranceStartDate").contains(date), true);
		int tenureInMonths = dataObject.getInt("Tenure") * 12;
		Assert.assertEquals(tenureInMonths,(obj.getInt("tenureInMonths")));
		Assert.assertEquals(dataObject.getInt("InsurancePremium"),(obj.getInt("insurancePremium")));
		Assert.assertEquals(obj.getString("insuranceTagPicturePath").isEmpty(),false);
		Assert.assertEquals(obj.getString("insuranceDocumentFilePath").isEmpty(),false);

	}



	@Test(groups= {"Regression"})
	public void addInsuranceMandatoryFields() throws IOException {

		String abstractname = prop.getProperty("AddInsurance");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();


		JSONObject dataObject = new JSONObject();


		request.header("Authorization","Bearer " + token);
		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),400);


		//Validate success message
		String jsonString = response.asString();
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");

		JSONArray obj = error.getJSONArray("Tenure");
		Assert.assertEquals(obj.getString(0),"The field Tenure must be between 1 and 4.");

		obj = error.getJSONArray("CattleId");
		Assert.assertEquals(obj.getString(0),"The CattleId field is required.");

		obj = error.getJSONArray("InsuranceDocumentFile");
		Assert.assertEquals(obj.getString(0),"The InsuranceDocumentFile field is required.");

		obj = error.getJSONArray("InsuranceTagPictureFile");
		Assert.assertEquals(obj.getString(0),"The InsuranceTagPictureFile field is required.");

	}
}
