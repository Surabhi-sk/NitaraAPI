package com.nitara.PermissionManagement;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetAllUsersAndRoles extends GenericBase {

	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void getAllUsersAndRoles() throws Throwable { 

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetAllUsersAndRoles"),token);


		// Printing system response code
		System.out.println("Response code:" + rs.getStatusCode());

		// Printing system response
		System.out.println("Response:" + rs.getBody().prettyPeek().asString());
		
		String jsonString = rs.asString();
		String  message = JsonPath.from(jsonString).get("message");
		

	
		
		/*JSONObject cattleDetails = new JSONObject(jsonString);
		
		
	//	JSONObject cattleTreatmentData = cattleDetails.getJSONObject("allUsers");
		JSONArray treatment = cattleDetails.getJSONArray("allUsers");
				
		JSONObject object = treatment.getJSONObject(0);
		
		String key="allUsers";
		List<HashMap<String,Object>>details=rs.jsonPath().getList(key);
		//Now parse value from List
		HashMap<String,Object> allUsers=details.get(0);// for first index
		String shed=(String)allUsers.get("shed");;
		String group=(String)allUsers.get("group");;
		String farm=(String)allUsers.get("farm");;
		
		System.out.println("shed =" +"  " +  shed);
	
		System.out.println("group =" + "  " +  group);
		
		System.out.println("farm  ="+"  "+  farm);
		
		String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("RegressionData");
		
		ExcelUtils eu = new ExcelUtils();
		eu.writeStringData("Sheet1","farmId", farm, external);
		eu.writeStringData("Sheet1","shedId", shed, external);
		eu.writeStringData("Sheet1","shedId", group, external);
		//Validate success message
		Assert.assertEquals("All Users.", message);

		if (rs.getStatusCode() == 200) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals(200, rs.getStatusCode());*/
		
		



	}

}




























