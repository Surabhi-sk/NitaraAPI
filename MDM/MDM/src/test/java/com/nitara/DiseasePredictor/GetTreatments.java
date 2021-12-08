package com.nitara.DiseasePredictor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.nitara.UserPreferenceManagement.GetUserServicesPreferences;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*User Story :Disease Predictor 678
@Author : Ravi Teja
*/

public class GetTreatments extends GenericBase{
	
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void GetTreatments() 
	{
	
		
		String abstractname = prop.getProperty("GetTreatments");
		RestAssured.baseURI = prop.getProperty("baseurl"); 

		RequestSpecification request = RestAssured.given();
		
		HashMap<String, Object> queryparam = new HashMap<>();


//		ArrayList<String> list = new ArrayList<String>(); 
//		list.add("Disease_EphemeralFever");
//		list.add("Disease_FMD");
//		
//
//		JSONObject queryparams = new JSONObject();
//		queryparams.put("diseaseIdList",list); 
		
		String disease[] = new String[] {"Disease_EphemeralFever", "Disease_FMD"};
		List<String> list = Arrays.asList(disease);
		
		//queryparam.put("", Arrays.asList("Disease_EphemeralFever", "Disease_FMD"));
		
	//	listdata.add(queryparams);
//		request.body(listdata.toString());
		
		

		Response response = request.body(list)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());
		if (response.getStatusCode() == 200) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals("Treatment Data.", message);

	}

}
