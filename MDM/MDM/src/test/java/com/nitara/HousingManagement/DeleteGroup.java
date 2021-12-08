package com.nitara.HousingManagement;


import java.util.HashMap;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteGroup extends GenericBase
{
	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void deleteGroup() throws Exception 
	{

		String abstractname = prop.getProperty("DeleteGroup");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");


		//read user name and password
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject farmId = exceldata.readRowField("GeneralData", "farmId", filepath);


		RequestSpecification request = RestAssured.given();
		HashMap<String, Object> queryParam = new HashMap<>();
		queryParam.put("farmid", farmId.getString("farmId"));
		queryParam.put("shedName", "UserNewShed1");		 
		queryParam.put("GroupName", "UserGroup1");


		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);


		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());


		String jsonString = response.asString();				
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Group Deleted successfully.", message);



	}			
}
