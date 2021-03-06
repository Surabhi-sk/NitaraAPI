package com.nitara.CattleManagement;


import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateCattleProfile extends GenericBase{


	@Test(groups= {"Smoke"})
	public void updateCattleProfile() throws Exception {

		String abstractname = prop.getProperty("UpdateCattleProfile");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();

		//Read from excel to JSONObject
		ExcelUtils data = new ExcelUtils();
		data.updateField("GeneralData",filepath,"TagNumber"); 
		data.updateField("GeneralData",filepath,"CooperativeTagNumber"); 
		JSONObject requestParams = data.readData("UpdateCattleProfile",filepath);
		
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(12);
		String TagNo2 = var.generateNo(12);
		
		String disease[] = new String[] {TagNo,TagNo2};
		List<String> list = Arrays.asList(disease);
		
		requestParams.put("CooperativeTagNumbers", list);
		

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("messages");
		Assert.assertEquals("Cattle Profile updated.", message);

	}
}
