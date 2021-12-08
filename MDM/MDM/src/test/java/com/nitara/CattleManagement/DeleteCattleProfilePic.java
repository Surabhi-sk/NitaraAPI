package com.nitara.CattleManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.CattleDetails.ViewAllCattlePhotos;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteCattleProfilePic extends GenericBase{

	@Test(groups= {"Smoke"})
	public void deleteCattleProfilePic() throws Exception {

		String abstractname = prop.getProperty("DeleteCattleProfilePic");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("CattleOtherActivities");

		ExcelUtils data = new ExcelUtils();
		JSONObject requestParams = data.readRowField("GeneralData","CattleId",filepath);

		ViewAllCattlePhotos cattle = new ViewAllCattlePhotos();
		String cattleDetails = cattle.viewAllCattlePhotos(requestParams.getString("CattleId"),token);


		JSONObject pictureDetails = new JSONObject(cattleDetails);
		JSONObject profilePics = pictureDetails.getJSONObject("cattleProfilePics");
		JSONArray bodyPics = profilePics.getJSONArray("bodyCattleProfilePics");
		JSONObject photo = bodyPics.getJSONObject(0);
		String photoId = photo.getString("photoId");

		RequestSpecification request = RestAssured.given();
		requestParams.put("ProfilePicId", photoId); 

		Response response = request.body(requestParams.toString())
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer " + token)
				.post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Profile Picture Removed.", message);


		//CHECK WHETHER THE PICTURE IS REMOVED
		cattleDetails = cattle.viewAllCattlePhotos(requestParams.getString("CattleId"),token);

		pictureDetails = new JSONObject(cattleDetails);
		profilePics = pictureDetails.getJSONObject("cattleProfilePics");
		bodyPics = profilePics.getJSONArray("bodyCattleProfilePics");
		Assert.assertEquals(bodyPics.length(),0);


	}			

}
