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
public class GetAllDiseases extends GenericBase{
	
	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void GetAllDiseases() {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetAllDiseases"),token);

		System.out.println("Response code:" + rs.getStatusCode());
		
		System.out.println("Response:" + rs.getBody().prettyPrint().toString());
		
	
		
		String jsonString = rs.asString();
		String  message = JsonPath.from(jsonString).get("message");

		// Printing test case result
		if (rs.getStatusCode() == 200) {			
			System.out.println("Status: Pass");
		} else {
			System.out.println("Status: Fail");
		}

		// Comparing expected result with actual result
		Assert.assertEquals(message , "Disease Data.");
		
	}

}
