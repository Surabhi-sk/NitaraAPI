package com.nitara.AccountManagement;

import java.io.File;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BulkUpload extends GenericBase{

	@Test
	public void bulkUpload() {


		String abstractname = prop.getProperty("BulkUpload");
		RestAssured.baseURI = prop.getProperty("baseurl");


		RequestSpecification request = RestAssured.given();
		request.header("Authorization","Bearer " + token);

		request.multiPart("Zip", new File("src\\main\\java\\com\\nitara\\testdata\\Offline_RegHeifer.zip"));


	Response res = request.post(abstractname).then().extract().response();


	//Print response
	res.prettyPeek();

	//Validate status code
	Assert.assertEquals(200, res.getStatusCode());
}

}
