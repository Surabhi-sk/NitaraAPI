package com.nitara.KYCManagement;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RejectKYCDetails extends GenericBase{

	@Test(groups= {"Smoke"})
	public void rejectKYCDetails() throws Exception {

		String abstractname = prop.getProperty("RejectKYCDetails");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		

		Login admin = new Login();
		String Admintoken = admin.userToken("1234567890","gormal@123456");

		RequestSpecification request = RestAssured.given();

		username.put("Reason", "Documents Required");

		Response response = request.body(username.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + Admintoken)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());


		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());

		// Printing system response
		System.out.println("Response:" + response.getBody().prettyPeek().asString());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("KYC Details rejected.", message);
		
		//Check Verification status
		
		GetKycVerificationStatus kycStatus = new GetKycVerificationStatus();
		String status = kycStatus.getKycVerificationStatus(usertoken);
		String  verificationStatus = JsonPath.from(status).get("verificationStatus");
		Assert.assertEquals("KYC verification is Rejected", verificationStatus);

	}

}
