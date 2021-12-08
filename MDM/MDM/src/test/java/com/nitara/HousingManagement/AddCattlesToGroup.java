package com.nitara.HousingManagement;


import java.util.Arrays;
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

public class AddCattlesToGroup extends GenericBase
{
	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void addCattlesToGroup() throws Exception 
	{

		String abstractname = prop.getProperty("AddCattlesToGroup");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		RequestSpecification request = RestAssured.given();
		//read user name and password

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject cattleId = exceldata.readRowField("GeneralData", "CattleId", filepath);
		filepath = prop.getProperty("AccountManagement");
		JSONObject farmId = exceldata.readRowField("GeneralData", "farmId", filepath);

		HashMap<String, Object> queryParam = new HashMap<>();
		queryParam.put("farmid", farmId.getString("farmId"));
		queryParam.put("shedname", "SmartNewShed1");	 
		queryParam.put("GroupName",  "SmartGroup2");
		queryParam.put("CattleIds", Arrays.asList(cattleId.getString("CattleId")));

		Response response = request.body(queryParam)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);
		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();		
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattles added to group.", message);
	}		
}

