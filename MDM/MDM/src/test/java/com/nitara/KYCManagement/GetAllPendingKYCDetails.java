package com.nitara.KYCManagement;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetAllPendingKYCDetails extends GenericBase{

	
	@Test(groups= {"Smoke"})
	public void getAllPendingKYCDetails() {
		
		String abstractname = prop.getProperty("GetAllPendingKYCDetails");
		RestAssured.baseURI = prop.getProperty("baseurl");
		
		Login admin = new Login();
		String Admintoken = admin.userToken("1234567890","gormal@123456");
		
		Response response = RestAssured.given().
				header("Authorization","Bearer " + Admintoken).get(abstractname);
		
		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("KYC Unverified users", message);

	}
}
