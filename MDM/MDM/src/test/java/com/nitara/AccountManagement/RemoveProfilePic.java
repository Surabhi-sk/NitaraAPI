package com.nitara.AccountManagement;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RemoveProfilePic extends GenericBase{

	NitaraService results = new NitaraService();


	@Test(groups= {"Smoke"})
	public void removeProfilePic() {

		System.out.println("Remove Profile Picture");
		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("RemoveProfilePic"),token);

		System.out.println("Response code:" + rs.getStatusCode());
		//System response
		System.out.println("Response:" + rs.getBody().prettyPrint().toString());

		// Comparing expected result with actual result
		Assert.assertEquals(rs.getStatusCode(),200);

		//Validate success message
		String jsonString = rs.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Profile pic removed.");

		//Assert if picture is removed
		ViewProfilePic cattle = new ViewProfilePic();
		String res = cattle.viewProfilePic(token);
		String  profilePicPath = JsonPath.from(res).get("profilePicPath");
		Assert.assertEquals(profilePicPath,null);
	}
}
