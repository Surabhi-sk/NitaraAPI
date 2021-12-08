package com.nitara.KYCManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DownloadFile extends GenericBase{


	@Test(groups= {"Smoke"})
	public void downloadFile() {

		String abstractname = prop.getProperty("DownloadFile");
		RestAssured.baseURI = prop.getProperty("baseurl");
		

		RequestSpecification request = RestAssured.given();
		String filepath = "C:\\\\NitaraApp\\\\File Storage\\\\AM\\\\KYC\\\\20210511085017605_11c9376c-4ae5-489c-8ec9-15115b15e5a0.jpg"
;		//Create 
		JSONObject requestParams = new JSONObject();
		requestParams.put("filepath", filepath); // Cast

		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				header("Authorization","Bearer " + token).post(abstractname);

		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		// Printing system response
		System.out.println("Response:" );
		response.prettyPeek();

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User Added Successfully.", message);


		// Comparing expected result with actual result
		Assert.assertEquals(200,  response.getStatusCode());

	}

}
