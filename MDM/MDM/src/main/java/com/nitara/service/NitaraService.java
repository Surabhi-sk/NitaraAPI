package com.nitara.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class NitaraService {

	// This method is for GetAllCodesFromCodesList
	public Response GetAllCodesFromCodesList(String baseurl, String abstractionname, String CodesList, String Version) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		RequestSpecification request = RestAssured.given().body(CodesList).queryParam("Version", Version)
				.header("Content-Type", "application/json");

		Response response = request.post(abstractionname);

		return response;

	}

	// This method is for GetAllCodesFromCodesList
	public Response GetAllCodesFromCodesListwithNoParam(String baseurl, String abstractionname, String CodesList) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		RequestSpecification request = RestAssured.given().body(CodesList).header("Content-Type", "application/json");

		Response response = request.post(abstractionname);

		return response;

	}

	// GetAll
	// This method is for GetAll without any parameter
	public Response GetAll(String baseurl, String abstractionname, String token) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;
		
				// Passing parameters to the request
		Response res = RestAssured.given().header("Authorization",
	              "Bearer " + token).get(abstractionname).then().extract()
				.response();
		System.out.println(res);
		return res;

	}

	// Get
	// This method is for Get with one parameter
	public Response GetAllByCustomParams(String baseurl, String abstractionname, String param, String paramValue, String token) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		// Passing parameters to the request
		Response res = RestAssured.given().header("Authorization",
	              "Bearer " + token).param(param, paramValue).get(abstractionname).then()
				.contentType(ContentType.JSON).extract().response();
		return res;

	}

	// Get
	// This method is for Get with two parameter
	public Response GetAllByCustomParams(String baseurl, String abstractionname, String param1, String param1Value,
			String param2, String param2Value) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		// Passing parameters to the request
		Response res = RestAssured.given().param(param1, param1Value).param(param2, param2Value).get(abstractionname)
				.then().contentType(ContentType.JSON).extract().response();
		return res;

	}

	// Get
	// This method is for Get with three parameter
	public Response GetAllByCustomParams(String baseurl, String abstractionname, String param1, String param1Value,
			String param2, String param2Value, String param3, String param3Value) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		// Passing parameters to the request
		Response res = RestAssured.given().param(param1, param1Value).param(param2, param2Value)
				.param(param3, param3Value).get(abstractionname).then().contentType(ContentType.JSON).extract()
				.response();
		return res;
	}
	// Get
	// This method is for Get with four parameter

	public Response GetAllByCustomParams(String baseurl, String abstractionname, String param1, String param1Value,
			String param2, String param2Value, String param3, String param3Value, String param4, String param4Value) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		// Passing parameters to the request
		Response res = RestAssured.given().param(param1, param1Value).param(param2, param2Value)
				.param(param3, param3Value).param(param4, param4Value).get(abstractionname).then()
				.contentType(ContentType.JSON).extract().response();
		return res;

	}

	// Get
	// This method is for Get with five parameter

	public Response GetAllByCustomParams(String baseurl, String abstractionname, String param1, String param1Value,
			String param2, String param2Value, String param3, String param3Value, String param4, String param4Value,
			String param5, String param5Value) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		// Passing parameters to the request
		Response res = RestAssured.given().param(param1, param1Value).param(param2, param2Value)
				.param(param3, param3Value).param(param4, param4Value).param(param5, param5Value).get(abstractionname)
				.then().contentType(ContentType.JSON).extract().response();
		return res;
	}

	// Post
	// This method is for Post Custom Params
	public Response PostByCustomParams(String baseurl, String abstractionname, String PostObject) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		RequestSpecification request = RestAssured.given().body(PostObject).header("Content-Type", "application/json");

		Response response = request.post(abstractionname);

		return response;
	}
	
	public Response PostByCustomParams(String baseurl, String abstractionname, String PostObject, String Param, String ParamValue) {
		// Assigning base Url to the request
		RestAssured.baseURI = baseurl;

		RequestSpecification request = RestAssured.given().body(PostObject).queryParam(Param, ParamValue)
				.header("Content-Type", "application/json");

		Response response = request.post(abstractionname);

		return response;
	}

}