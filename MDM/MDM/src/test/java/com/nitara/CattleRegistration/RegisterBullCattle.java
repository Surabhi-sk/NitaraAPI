package com.nitara.CattleRegistration;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.nitara.FarmManagement.ViewFarmCattleList;
import com.nitara.UserLogin.Login;
import com.nitara.base.GenericBase;
import com.nitara.utilities.ExcelUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*User Story :Cattle registration 1872
@Author : Ravi Teja - Regression TestCases
 */
public class RegisterBullCattle extends GenericBase{
	//NitaraService results = new NitaraService();

	@Test(groups= {"Smoke"})
	public void registerBullCattle() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");


		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		
		
		//Update tag numbers in Registration.xlsx
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		exceldata.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		exceldata.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readCase("RegBullCattle","RegisterBullCattle",filepath,external);
		
		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {

				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));
				}
				else {
					request.formParam(key, dataObject.get(key));
				}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(res.getStatusCode(), 200);


		//Validate success message
		String jsonString = res.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattle Registered successfully.", message);
		String cattleId = JsonPath.from(jsonString).get("cattleId");

		/*//Get cattleId of newly registered bull
		ViewFarmCattleList cattle = new ViewFarmCattleList();
		String response = cattle.viewFarmCattleList(dataObject.getString("farmId"));

		JSONObject cattleList = new JSONObject(response);
		JSONArray farmCattle = cattleList.getJSONArray("farmCattle");

		String cattleId = "";
		String tagNumber = dataObject.getString("tagNumber");
		System.out.println(farmCattle.length());
		for(int i =0 ;i<farmCattle.length();i++) {
			JSONObject details = farmCattle.getJSONObject(i);
			if(tagNumber.equals(details.getString("tagNumber"))){
				cattleId = details.getString("cattleId");
			}

		}
		System.out.println(cattleId);

		if(cattleId.equals("")) {
			System.out.println("Newly registered cattle not found in the farmlist");

			//Assert.assertEquals(cattleId, "");
			Assert.assertEquals(cattleId.isEmpty(),false);


			Assert.assertEquals(cattleId.isEmpty(),false);	

		}*/



		//Write CattleId to excel
		filepath = prop.getProperty("CattleOtherActivities");
		exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
		System.out.println("Written");
	}



	public void registerBullCattle(String token) throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");


		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();

		//Update tag numbers in Registration.xlsx
		ExcelUtils var = new ExcelUtils();
		String TagNo = var.generateNo(8);
		exceldata.writeStringData("GeneralData","TagNumber",TagNo, filepath);
		String CoopNo = var.generateNo(12);
		exceldata.writeStringData("GeneralData","CooperativeTagNumber",CoopNo, filepath);

		JSONObject dataObject = exceldata.readCase("RegBullCattle","RegisterBullCattle",filepath,external);

		request.header("Authorization","Bearer " + token);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) {

				if((dataObject.getString(key)).matches("([^\\s]+(\\.(?i)(jpe?g|png|pdf))$)")) {
					request.multiPart(key, new File(dataObject.getString(key)));
				}
				else {
					request.formParam(key, dataObject.get(key));
				}
			}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}

		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(200, res.getStatusCode());



		//Validate status code
		Assert.assertEquals(res.getStatusCode(), 200);


		//Validate success message
		String jsonString = res.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Cattle Registered successfully.", message);
		String cattleId = JsonPath.from(jsonString).get("cattleId");
		
		//Write CattleId to excel
		filepath = prop.getProperty("CattleOtherActivities");
		exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
		System.out.println("Written");
	}


	@Test(groups= {"Regression"})
	public void Registerwithduplicatetagnumber() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String external = prop.getProperty("AccountManagement");
		String filepath = prop.getProperty("RegressionData");

		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithduplicatetagnumber",filepath);

		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpeg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}
		}		
		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Tag Number already exists.");

	}

	@Test(groups= {"Regression"})
	public void registerwithemptytagnumbers() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		String external = prop.getProperty("AccountManagement");
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithemptytagnumbers",filepath);

		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpeg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}

		}

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		//res.prettyPeek();

		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		JSONObject res = new JSONObject(jsonString);
		JSONObject error = res.getJSONObject("errors");


		JSONArray tagNumber = error.getJSONArray( "TagNumber");
		Assert.assertEquals(tagNumber.getString(0),"The TagNumber field is required."); 
	}			

	@Test(groups= {"Regression"})
	public void registerwithoutyearofbirth() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		//String external = prop.getProperty("AccountManagement");
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		String external = prop.getProperty("AccountManagement");
		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithoutyearofbirth",filepath);

		request.header("Authorization","Bearer " +usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpeg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}

		}
		System.out.println(dataObject);
		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, res.getStatusCode());

		//Assert error message
		String jsonString = res.asString(); 
		JSONObject response = new JSONObject(jsonString);
		JSONObject error = response.getJSONObject("errors");

		//Assert.assertEquals(error.length(),4);

		JSONArray tag = error.getJSONArray("YearOfBirth");
		//	Assert.assertEquals("Cattle aged 2020 years and 10 months should not exceed the maximum age of 25 years.", tag.getString(0));


	}

	/*@Test
				public void Registerwithoutphotos() throws Exception {

					String abstractname = prop.getProperty("RegisterBullCattle");
					RestAssured.baseURI = prop.getProperty("baseurl");
					//String filepath = prop.getProperty("Registration");
					String external = prop.getProperty("AccountManagement");
					String filepath = prop.getProperty("RegressionData");


					RequestSpecification request = RestAssured.given();

					ExcelUtils exceldata = new ExcelUtils();
					//Update tags
				//	exceldata.updateField("RegBullCattle",filepath,"tagNumber"); 
				//	exceldata.updateField("RegBullCattle",filepath,"cooperativeTagNumber"); 
					JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithoutphotos",filepath);

					request.header("Authorization","Bearer " + token);
					for (String key: dataObject.keySet()){
						if((dataObject.get(key) instanceof String)) 
							if((dataObject.getString(key)).contains(".jpeg")) {
								request.multiPart(key, new File(dataObject.getString(key)));}

							else {
								request.formParam(key, dataObject.get(key));
							}
						else {
							request.formParam(key, dataObject.get(key));
						}

					}
					System.out.println(dataObject);
					Response res = request.post(abstractname).then().extract().response();

					//Print response
					res.prettyPeek();

					//Validate status code
					Assert.assertEquals(400, res.getStatusCode());

				/*	//Assert error message
					String jsonString = res.asString(); 
					JSONObject response = new JSONObject(jsonString);
					JSONObject error = response.getJSONObject("errors");

					Assert.assertEquals(error.length(),1);

					JSONArray tag = error.getJSONArray("YearOfBirth");
					Assert.assertEquals("The TagNumber field is required.", tag.getString(0));
					}*/

	//}*/

	@Test
	public void Registerwithmorethan25yeardsofage() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		//String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithmorethan25yeardsofage",filepath);
		System.out.print(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpeg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}

		}

		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, res.getStatusCode());


		//Assert error message
		String jsonString = res.asString(); 
		JSONObject response = new JSONObject(jsonString);
		JSONObject error = response.getJSONObject("errors");


		JSONArray tag = error.getJSONArray("YearOfBirth");
		//	Assert.assertEquals("Cattle aged 30 years and 10 months should not exceed the maximum age of 25 years.", tag.getString(0));
	}	
	@Test(groups= {"Regression"})
	public void Registerwithlessthan6monthsofage() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		//String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		
		//Update tags
		//	exceldata.updateField("RegBullCattle",filepath,"tagNumber"); 
		//	exceldata.updateField("RegBullCattle",filepath,"cooperativeTagNumber"); 
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithlessthan6monthsofage",filepath);
		System.out.print(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpeg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}

		}

		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, res.getStatusCode());

		//Validate success message
		//Assert error message
		String jsonString = res.asString(); 
		JSONObject response = new JSONObject(jsonString);
		JSONObject error = response.getJSONObject("errors");

		//Assert.assertEquals(error.length(),4);

		JSONArray tag = error.getJSONArray("MinimumAge");
		Assert.assertEquals(tag.getString(0),"Bull cannot be less than 6 months of age.");				
	}	

	@Test(groups= {"Regression"})
	public void Registerwithduplicatecotagnumber() throws Exception {

		String abstractname = prop.getProperty("RegisterBullCattle");
		RestAssured.baseURI = prop.getProperty("baseurl");
		//String filepath = prop.getProperty("Registration");
		String external = prop.getProperty("AccountManagement");
		String filepath = prop.getProperty("RegressionData");


		RequestSpecification request = RestAssured.given();

		ExcelUtils exceldata = new ExcelUtils();
		
		JSONObject username = exceldata.readRowField("GeneralData", "username", external);
		JSONObject password = exceldata.readRowField("GeneralData", "Password", external);

		Login user = new Login();
		String usertoken = user.userToken(username.getString("username"),password.getString("Password"));
		//Update tags
		//	exceldata.updateField("RegBullCattle",filepath,"tagNumber"); 
		//	exceldata.updateField("RegBullCattle",filepath,"cooperativeTagNumber"); 
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithduplicatecotagnumber",filepath);
		System.out.print(dataObject);
		request.header("Authorization","Bearer " + usertoken);
		for (String key: dataObject.keySet()){
			if((dataObject.get(key) instanceof String)) 
				if((dataObject.getString(key)).contains(".jpeg")) {
					request.multiPart(key, new File(dataObject.getString(key)));}

				else {
					request.formParam(key, dataObject.get(key));
				}
			else {
				request.formParam(key, dataObject.get(key));
			}

		}

		Response res = request.post(abstractname).then().extract().response();

		Response response = request.post(abstractname).then().extract().response();

		//Print response
		response.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, response.getStatusCode());

		//Assert error message
		String jsonString = response.asString(); 
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals(message,"Cooperative Number already exists.");
	}
}
