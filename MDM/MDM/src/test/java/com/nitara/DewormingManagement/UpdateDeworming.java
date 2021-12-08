package com.nitara.DewormingManagement;

import java.io.File;


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

public class UpdateDeworming extends GenericBase{


	@Test(groups= {"Smoke"})
	public void updateDeworming() throws Exception {

		String abstractname = prop.getProperty("UpdateDeworming");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		JSONObject dataObject = data.readData("UpdateDeworming",filepath);

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

		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, res.getStatusCode());

		//Validate success message
		String jsonString = res.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Deworming Data Updated Successfully.", message);


		//Verify the updated details
		ViewDewormingData details = new ViewDewormingData();
		String response = details.viewDewormingData(dataObject.getString("CattleId"), token);

		//Parse json
		JSONObject cattleDetails = new JSONObject(response);
		JSONObject cattleDewormingData = cattleDetails.getJSONObject("dewormingData");
		JSONArray deworming = cattleDewormingData.getJSONArray("dewormings");
		JSONObject obj = deworming.getJSONObject(0);

		String date = dataObject.getString("DewormingDate");
		Assert.assertEquals(obj.getString("dateTime").contains(date), true);
		Assert.assertEquals(obj.getString("dewormingProofFilePath").isEmpty(),false);
	}


}