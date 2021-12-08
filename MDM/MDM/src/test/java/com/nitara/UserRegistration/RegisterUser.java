package com.nitara.UserRegistration;


import java.util.Map;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.PermissionManagement.GetUserDetails;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RegisterUser extends GenericBase{

	@Test(groups= {"Smoke","Regression"})
	public void registerUser() throws Exception {

		String abstractname = prop.getProperty("RegisterUser");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("AccountManagement");

		RequestSpecification request = RestAssured.given();
		
		ExcelUtils data = new ExcelUtils();
		
		//Update phone number in AccountManagement.xlsx
		ExcelUtils var = new ExcelUtils();
		String phoneNo = var.generateNo(10);
		data.writeStringData("GeneralData","phone",phoneNo, filepath);



		//Read from excel to JSONArray
		
		JSONObject requestParams = data.readData("RegisterUser",filepath);


		Response response= request.body(requestParams.toString()).
				header("Content-Type", "application/json").
				post(abstractname);

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(response.getStatusCode(),200);

		//Validate success message
		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"User Added Successfully." );

		String RegToken = JsonPath.from(jsonString).get("token");

		//Verify OTP
		VerifyOTP otp = new VerifyOTP();
		otp.verifyOTP("1111",RegToken); //default 1111

		//SetPassword
		SetPasswordForUser user = new SetPasswordForUser();
		user.setPasswordForUser(requestParams.getString("phone"),RegToken);


		///Assert user details
		GetUserDetails userDetails = new GetUserDetails();
		String res = userDetails.getUserDetails(requestParams.getString("phone"),RegToken);

		Map<String, String> detail= JsonPath.from(res).getMap("userDetail");
		Assert.assertEquals(requestParams.getString("name"), detail.get("fullName"));


		String regressionFilepath = prop.getProperty("RegressionData");

		if(requestParams.getString("role").equalsIgnoreCase("farmer")) {
			Assert.assertEquals("Owner", detail.get("farmRole"));
		}
		else {
			Assert.assertEquals("Service Provider", detail.get("farmRole"));
			String username = detail.get("phone");
			data.writeStringData("GeneralData","SPusername", username, filepath);
			data.writeStringData("GeneralData","usernameSP", username, regressionFilepath);
		}
	}


}
