package com.nitara.MasterData;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nitara.base.GenericBase;
import com.nitara.service.NitaraService;
import io.restassured.response.Response;

public class GetA1A2TypeMasterData extends GenericBase {

	NitaraService results = new NitaraService();
	
	@Test(groups= {"Smoke"})
	public void GetAllA1A2TypeMasterDataInEnglish() throws IOException 
	
	{ 
				
		Response rs = results.GetAll(prop.getProperty("baseurl"), prop.getProperty("GetA1A2TypeMasterData"),token);
		
		
		// Printing system response code
		System.out.println("Response code:" + rs.getStatusCode());

		// Printing system response
		System.out.println("Response:" + rs.getBody().prettyPeek().asString());

		if (rs.getStatusCode() == 200) {
						// Printing test case result
			System.out.println("Status: Pass");
		} else {
			// Printing test case result
			System.out.println("Status: Fail");
		}
		// Comparing expected result with actual result
		Assert.assertEquals( rs.getStatusCode(),200);

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Get All GetAllAdmissionSourceCodesBySearchString
	/*
	 * @Test public void GetAllAdmissionSourceCodesBySearchString() throws
	 * IOException {
	 * 
	 * ExcelConfig excelread = new ExcelConfig(prop.getProperty("excelpath"));
	 * 
	 * // Fetching input parameters from excel and storing into the variables int
	 * SearchStringCount =
	 * excelread.getRowCountBasedOnColumn("GetAllAdmissionSourceCodes",
	 * "SearchString");
	 * 
	 * // Printing row count System.out.println("SearchStringCount:" +
	 * SearchStringCount);
	 * 
	 * for (int i = 0; i <= SearchStringCount; i++) {
	 * 
	 * System.out.println("\nTest Case: " + i); System.out.println(
	 * "************************************************************************************"
	 * );
	 * 
	 * // Fetching input parameters from excel and storing into variables String
	 * SearchString = excelread.readData("GetAllAdmissionSourceCodes",
	 * "SearchString", SearchStringCount);
	 * 
	 * // Printing input parameters System.out.println("SearchString:" +
	 * SearchString);
	 * 
	 * Response rs = results.GetAllByCustomParams(prop.getProperty("baseurl"),
	 * prop.getProperty("GetAllAdmissionSourceCodes"), "SearchString",
	 * SearchString);
	 * 
	 * // Printing system response code System.out.println("Response code:" +
	 * rs.getStatusCode());
	 * 
	 * // Printing system response //System.out.println("Response:" +
	 * rs.getBody().asString());
	 * 
	 * if (rs.getStatusCode() == 200) { // Printing test case result
	 * System.out.println("Status: Pass"); } else { // Printing test case result
	 * System.out.println("Status: Fail"); } // Comparing expected result with
	 * actual result Assert.assertEquals(200, rs.getStatusCode()); } }
	 */

}
