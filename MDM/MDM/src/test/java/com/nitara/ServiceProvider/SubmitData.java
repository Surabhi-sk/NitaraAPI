package com.nitara.ServiceProvider;
import com.nitara.base.GenericBase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SubmitData extends GenericBase{

	@Test(groups= {"Smoke"})
	public void submitData() {


		String abstractname = prop.getProperty("SubmitData");
		RestAssured.baseURI = prop.getProperty("baseurl");


		Response response = RestAssured.given().header("Authorization","Bearer " + token).
				formParam("FarmId", "4b66f170-e2b8-4a32-8b63-b422ea88858f").
				formParam("Otp","1111"). //bool
				formParam("Amount",2000). //bool
				formParam("ExpenseOrDue","Due").
				formParam("Inseminations.CattleId", "a80622fe-6795-48ba-af06-1a620a6aede8").
				formParam("Inseminations.SemenBrand", "SAG").
				formParam("Inseminations.BullId", "BullId01").
				/*formParam("Inseminations[0].BreedCode", "222").
				formParam("Inseminations[0].SemenStation", "semenStat").
				formParam("Inseminations[0].DayOfYear", 35).
				formParam("Inseminations[0].DateOfProduction", 4).
				formParam("Inseminations[0].EjaculationNumber", 3).
				formParam("Inseminations[0].GeoLocationOfService", "Location").
				formParam("Inseminations[0].InseminationDate",  "2021-03-25").
				multiPart("Inseminations[0].SemenStrawFile", new File(image)).*/
				post(abstractname).then().extract().response();

		System.out.println("Response body: " + response.body().prettyPeek().asString());

		String jsonString = response.asString();
		String  message = JsonPath.from(jsonString).get("message");
		//Validate success message
		Assert.assertEquals("Followup Data Added Successfully.", message);


		// Printing system response code
		System.out.println("Response code:" + response.getStatusCode());


		if (response.getStatusCode() == 200) {
			// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals(200, response.getStatusCode());



	}

}
