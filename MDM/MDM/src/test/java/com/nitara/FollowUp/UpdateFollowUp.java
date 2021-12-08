package com.nitara.FollowUp;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.Treatment.ViewTreatmentData;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateFollowUp extends GenericBase{

	@Test(groups= {"Smoke"})
	public void UpdateFollowUpDetails() throws IOException {

		String abstractname = prop.getProperty("UpdateFollowUp");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readCase("UpdateFollowUp","UpdateFollowUp",filepath);

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
		Assert.assertEquals("Followup Data Updated Successfully.", message);
		
		//Verify the added details
				ViewTreatmentData details = new ViewTreatmentData();
				String res = details.viewTreatmentData(dataObject.getString("cattleId"), token);

				
				JSONObject cattleDetails = new JSONObject(res);
				JSONObject cattleTreatmentData = cattleDetails.getJSONObject("treatmentData");
				JSONArray treatment = cattleTreatmentData.getJSONArray("treatments");
				JSONObject obj = treatment.getJSONObject(0);
				JSONArray followUps = obj.getJSONArray("followUps");
				
				
				for(int i =0; i<followUps.length();i++) {
					obj = followUps.getJSONObject(i);
					if(obj.get("followupAnalysis").equals(null)) {
						continue;
					}
					else {
						Assert.assertEquals(dataObject.get("isAntibioticGiven"),(obj.get("isAntibioticGiven")));
						Assert.assertEquals(dataObject.get("isFollowupRequired"),(obj.get("nextFollowupRequired")));
						Assert.assertEquals(obj.getString("treatmentProofPath").isEmpty(),false);
						
						String date = dataObject.getString("dateOfVisit");
						Assert.assertEquals(obj.getString("dateOfVisit").contains(date), true);
					}
				}
				
				
				System.out.println("Follow Up data updated.");

	}
}