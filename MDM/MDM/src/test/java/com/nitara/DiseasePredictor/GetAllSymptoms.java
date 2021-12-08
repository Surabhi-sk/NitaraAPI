package com.nitara.DiseasePredictor;




import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*User Story :Disease Predictor 678
@Author : Ravi Teja
*/

public class GetAllSymptoms extends GenericBase {
	
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	
	public void GetAllSymptoms() {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetAllSymptoms"),token);

		System.out.println("Response code:" + rs.getStatusCode());
		
		System.out.println("Response body: " + rs.body().prettyPeek().asString());
		
		String jsonString = rs.asString();
		String  message = JsonPath.from(jsonString).get("message");

		// Validate Status code
		Assert.assertEquals(200, rs.getStatusCode());

		// Printing test case result
		if (rs.getStatusCode() == 200) {			
			System.out.println("Status: Pass");
		} else {
			System.out.println("Status: Fail");
			
		}
		// Comparing expected result with actual result
		Assert.assertEquals(message , "Symptoms Data.");
}
	
	
}