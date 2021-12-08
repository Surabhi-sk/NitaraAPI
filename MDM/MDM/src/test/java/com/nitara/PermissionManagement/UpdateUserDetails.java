package com.nitara.PermissionManagement;

import java.io.File;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UpdateUserDetails extends GenericBase{

	@Test(groups= {"Smoke"})
	public void updateUserDetails() throws Exception {
		String abstractname = prop.getProperty("UpdateUserDetails");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", filepath);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", filepath);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));

		RequestSpecification request = RestAssured.given();
		JSONObject farmhelpDetails = exceldata.readData("UpdateUserDetails",filepath);

		request.header("Authorization","Bearer " + usertoken);
		for (String key: farmhelpDetails.keySet()){
			if((farmhelpDetails.get(key) instanceof String)) 
				if((farmhelpDetails.getString(key)).contains(".jpg")) {
					request.multiPart(key, new File(farmhelpDetails.getString(key)));}
				else {
					request.formParam(key, farmhelpDetails.get(key));
				}
			else {
				request.formParam(key, farmhelpDetails.get(key));
			}

		}
		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, response.getStatusCode());

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("User Updated Successfully.", message);

		//Assert updated changes
		/*GetUserDetails user = new GetUserDetails();
		String res = user.getUserDetails("888888888",token);

		Map<String, String> detail= JsonPath.from(res).getMap("userDetail");
		String role = detail.get("farmRole");
		Assert.assertEquals("Milking", role);
		//Validate success message
		Assert.assertEquals(true,detail.get("canViewBreedingModule"));
		Assert.assertEquals(false,detail.get("canViewMilkingModule"));


		JSONObject cattleDetails = new JSONObject(res);
		JSONObject cDetails = cattleDetails.getJSONObject("userDetail");
		System.out.println(cDetails.getString("farmRole"));*/

	}
}
