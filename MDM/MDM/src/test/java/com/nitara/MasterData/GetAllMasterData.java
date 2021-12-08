/*User Story : Fetch all Master Data - 2782
@Author : Surabhi
*/

package com.nitara.MasterData;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetAllMasterData extends GenericBase{

	NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void getAllMasterData() throws IOException 

	{ 

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetAllMasterData"),token);

		rs.getBody().prettyPeek();
		
		// Comparing expected result with actual result
		Assert.assertEquals(200, rs.getStatusCode());
		
		String jsonString = rs.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Master Data.");
		
		JSONObject mData = new JSONObject(jsonString);
		JSONObject data = mData.getJSONObject("data");
		Assert.assertEquals(data.length(),22);

	}

}
