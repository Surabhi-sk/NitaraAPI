package com.nitara.AccountManagement;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateProfilePic extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateProfilePic() {

		System.out.println("Update Profile Picture");
		String abstractname = prop.getProperty("UpdateProfilePic");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String image = prop.getProperty("image");

		Response response = RestAssured.given().header("Authorization","Bearer " + token).
				multiPart("ProfilePicFile", new File(image)).
				post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		// Comparing expected result with actual result
		Assert.assertEquals(response.getStatusCode(),200);

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals(message,"Profile Pic updated.");

		//Assert if picture is updated
		ViewProfilePic cattle = new ViewProfilePic();
		String res = cattle.viewProfilePic(token);
		String  profilePicPath = JsonPath.from(res).get("profilePicPath");
		Assert.assertEquals(profilePicPath.isEmpty(),false);

	}
}
