package com.nitara.CattleRegistration;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.nitara.FarmManagement.ViewFarmCattleList;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import com.nitara.utilities.ExcelUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RegisterBullRegCases extends GenericBase

{
	NitaraService results = new NitaraService();


	@Test
	public void registerBullCattlewithduplicatetagnumber() throws Exception {

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
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithduplicatecotagnumber",filepath);

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
		Response res = request.post(abstractname).then().extract().response();

		//Print response
		res.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, res.getStatusCode());

		/*//Validate success message
		String jsonString = res.asString();	
		String  message = JsonPath.from(jsonString).get("message");
		Assert.assertEquals("Tag Number already exists.", message);*/
	
	}
	
	@Test
	public void registerwithemptytagnumbers() throws Exception {

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
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithemptytagnumbers",filepath);

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
		
		Response res = request.post(abstractname).then().extract().response();

		//Print response
		//res.prettyPeek();

		//Validate status code
		Assert.assertEquals(400, res.getStatusCode());

		
		/*//Assert error message
				String jsonString = res.asString(); 
				JSONObject response = new JSONObject(jsonString);
				JSONObject error = response.getJSONObject("errors");

				Assert.assertEquals(error.length(),1);

				JSONArray tag = error.getJSONArray("TagNumber");
				Assert.assertEquals("The TagNumber field is required.", tag.getString(0));

//				JSONArray coopTag = error.getJSONArray("CooperativeTagNumber");
//				Assert.assertEquals("Cooperative Number should only consist of 12 digits.", coopTag.getString(0));*/
//		String jsonString = res.asString();	
//		String  message = JsonPath.from(jsonString).get("message");
//		Assert.assertEquals("The TagNumber field is required.", message);	


		//Write CattleId to excel
	//	filepath = prop.getProperty("CattleOtherActivities");
	//	exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
	
	}			
				@Test
	public void registerwithoutyearofbirth() throws Exception {

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
		JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithoutyearofbirth",filepath);

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
		Assert.assertEquals("Cattle aged 2020 years and 10 months should not exceed the maximum age of 25 years.", tag.getString(0));*/

	
	}
	
				@Test
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
					Assert.assertEquals("The TagNumber field is required.", tag.getString(0));*/
}
				@Test
				public void Registerwithinvalidfileformat() throws Exception {

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
					JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithinvalidfileformat",filepath);
					System.out.print(dataObject);
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
					
					Response res = request.post(abstractname).then().extract().response();

					//Print response
					res.prettyPeek();

					//Validate status code
					Assert.assertEquals(400, res.getStatusCode());

				/*	//Validate success message
					//Assert error message
							String jsonString = res.asString(); 
							JSONObject response = new JSONObject(jsonString);
							JSONObject error = response.getJSONObject("errors");

							Assert.assertEquals(error.length(),1);

							JSONArray tag = error.getJSONArray("TagNumber");
							Assert.assertEquals("The TagNumber field is required.", tag.getString(0));

//							JSONArray coopTag = error.getJSONArray("CooperativeTagNumber");
//							Assert.assertEquals("Cooperative Number should only consist of 12 digits.", coopTag.getString(0));*/
//					String jsonString = res.asString();	
//					String  message = JsonPath.from(jsonString).get("message");
//					Assert.assertEquals("The TagNumber field is required.", message);	


					//Write CattleId to excel
				//	filepath = prop.getProperty("CattleOtherActivities");
				//	exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
				
				}
				
				@Test
				public void Registerwithmorethan25yeardsofage() throws Exception {

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
					JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithmorethan25yeardsofage",filepath);
					System.out.print(dataObject);
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
					
					Response res = request.post(abstractname).then().extract().response();

					//Print response
					res.prettyPeek();

					//Validate status code
					Assert.assertEquals(400, res.getStatusCode());

					/*//Validate success message
					//Assert error message
							String jsonString = res.asString(); 
							JSONObject response = new JSONObject(jsonString);
							JSONObject error = response.getJSONObject("errors");

							Assert.assertEquals(error.length(),1);

							JSONArray tag = error.getJSONArray("YearOfBirth");
							Assert.assertEquals("Cattle aged 30 years and 10 months should not exceed the maximum age of 25 years.", tag.getString(0));

//							JSONArray coopTag = error.getJSONArray("CooperativeTagNumber");
//							Assert.assertEquals("Cooperative Number should only consist of 12 digits.", coopTag.getString(0));*/
//					String jsonString = res.asString();	
//					String  message = JsonPath.from(jsonString).get("message");
//					Assert.assertEquals("The TagNumber field is required.", message);	


					//Write CattleId to excel
				//	filepath = prop.getProperty("CattleOtherActivities");
				//	exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
				
				}	
				@Test
				public void Registerwithlessthan6monthsofage() throws Exception {

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
					JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithlessthan6monthsofage",filepath);
					System.out.print(dataObject);
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

							Assert.assertEquals(error.length(),1);

							JSONArray tag = error.getJSONArray("MinimumAge");
							Assert.assertEquals("Bull cannot be less than 9 months of age.", tag.getString(0));

//							JSONArray coopTag = error.getJSONArray("CooperativeTagNumber");
//							Assert.assertEquals("Cooperative Number should only consist of 12 digits.", coopTag.getString(0));*/
//					String jsonString = res.asString();	
//					String  message = JsonPath.from(jsonString).get("message");
//					Assert.assertEquals("The TagNumber field is required.", message);	


					//Write CattleId to excel
				//	filepath = prop.getProperty("CattleOtherActivities");
				//	exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
				
				}	
				
				@Test
			public void Registerwithduplicatecotagnumber() throws Exception {

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
					JSONObject dataObject = exceldata.readCase("RegisterBull","Registerwithduplicatecotagnumber",filepath);
					System.out.print(dataObject);
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

							Assert.assertEquals(error.length(),1);

							JSONArray tag = error.getJSONArray("MinimumAge");
							Assert.assertEquals("Bull cannot be less than 9 months of age.", tag.getString(0));

//							JSONArray coopTag = error.getJSONArray("CooperativeTagNumber");
//							Assert.assertEquals("Cooperative Number should only consist of 12 digits.", coopTag.getString(0));*/
//					String jsonString = res.asString();	
//					String  message = JsonPath.from(jsonString).get("message");
//					Assert.assertEquals("The TagNumber field is required.", message);	


					//Write CattleId to excel
				//	filepath = prop.getProperty("CattleOtherActivities");
				//	exceldata.writeStringData("GeneralData", "CattleId", cattleId, filepath);
				
				}	
}