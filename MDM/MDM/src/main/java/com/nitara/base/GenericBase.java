package com.nitara.base;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;
import org.junit.Rule;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.nitara.Reports.Report;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GenericBase {

	public String token;

	public static Properties prop;

	@Rule
	public Report screenShootRule = new Report();

	public GenericBase()
	{
		try 
		{
			prop=new Properties();
			FileInputStream fis=new FileInputStream("src\\main\\java\\com\\nitara\\config\\Config.properties");
			prop.load(fis);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}



}
