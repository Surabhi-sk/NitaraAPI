package com.nitara.KYCManagement;


import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.response.Response;

public class GetKycVerificationStatus extends GenericBase{

	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void getKycVerificationStatus() {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetKycVerificationStatus"),token);


		// Printing system response code
		System.out.println("Response code:" + rs.getStatusCode());

		// Printing system response
		System.out.println("Response:" + rs.getBody().prettyPeek().asString());

	}


	public String getKycVerificationStatus(String token) {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetKycVerificationStatus"),token);


		rs.prettyPeek();

		//Validate success message
		String jsonString = rs.asString();

		return jsonString;
	}
}
