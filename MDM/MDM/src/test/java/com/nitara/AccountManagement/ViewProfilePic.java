package com.nitara.AccountManagement;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ViewProfilePic extends GenericBase{

	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void viewProfilePic() {

		System.out.println("View Profile Picture");
		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("ViewProfilePic"),token);

		//Print Response
		rs.prettyPeek();

		//Validate Status code
		Assert.assertEquals(rs.getStatusCode(),200);

		//Validate success message
		String jsonString = rs.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Profile Pic." );

	}


	public String viewProfilePic(String userToken) {

		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("ViewProfilePic"),userToken);

		//Print Response
		rs.prettyPeek();

		//Validate Status code
		Assert.assertEquals(rs.getStatusCode(),200);

		//Validate success message
		String jsonString = rs.asString();

		return jsonString;
	}
}
