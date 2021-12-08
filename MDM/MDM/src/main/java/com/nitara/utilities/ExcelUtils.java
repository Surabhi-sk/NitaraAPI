package com.nitara.utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.io.FileOutputStream;

import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.javafaker.Faker;



public class ExcelUtils {
	
public static List<JSONObject> getTestDetails(String filepath, String sheetname) throws IOException{
		
		List<JSONObject> list = null;
		FileInputStream fs = null;
		
		String absPath = new File(filepath).getAbsolutePath();
		
		
		try {
			fs = new FileInputStream(absPath);
			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			System.out.println(workbook);
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();	 
			evaluator.evaluateAll();
			XSSFSheet sheet = workbook.getSheet(sheetname);
			System.out.println(sheet);
			
			int lastrow = sheet.getLastRowNum();
			int lastcol = sheet.getRow(0).getLastCellNum();
			
			JSONObject obj;
			/*Map<String,String> map = null;*/
			list = new ArrayList<>();
			
			for(int i=1; i<=lastrow;i++) {
				obj = new JSONObject();
				//map = new HashMap<>();
				for(int j=0;j<lastcol; j++) {
					String key = sheet.getRow(0).getCell(j).getStringCellValue();
					System.out.println(key);
					if( sheet.getRow(i).getCell(j) == null ||sheet.getRow(i).getCell(j).getCellType() == CellType.BLANK) {
						continue;
					}
					
					if(sheet.getRow(i).getCell(j).getCellType()== CellType.FORMULA){
						CellValue cellValue = evaluator.evaluate(sheet.getRow(i).getCell(j));		
						System.out.println(cellValue.getStringValue());
						obj.put(key,cellValue.getStringValue());
						continue;
					}
					
					if(sheet.getRow(i).getCell(j).getCellType() == CellType.STRING) {
						String value = sheet.getRow(i).getCell(j).getStringCellValue();
						System.out.println(value);
						obj.put(key, value);
					}
					else if (sheet.getRow(i).getCell(j).getCellType() == CellType.BOOLEAN) {
						obj.put(key,sheet.getRow(i).getCell(j).getBooleanCellValue());
						System.out.println(sheet.getRow(i).getCell(j).getBooleanCellValue());
						continue;
					}
					else
					{
						if( DateUtil.isCellDateFormatted(sheet.getRow(i).getCell(j))){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						String dateValue = format.format(sheet.getRow(i).getCell(j).getDateCellValue());
						System.out.println(dateValue);
						obj.put(key, dateValue);
						}
						else {
						System.out.println(sheet.getRow(i).getCell(j).getNumericCellValue());
						obj.put(key, sheet.getRow(i).getCell(j).getNumericCellValue());
					
						}
				
			}
				}
			
				list.add(obj);
				System.out.println(obj);
				System.out.println(list);
				
			
			
		} 
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			try {
				if(Objects.nonNull(fs)) {
					fs.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(list);
		return list;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int findRow(XSSFSheet sheet, String cellContent) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
						return row.getRowNum();  
					}
				}
			}
		}               
		return -1;
	}


	public JSONObject readCase(String sheet, String scenario, String filepath) throws IOException {
		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();	 
		evaluator.evaluateAll();

		XSSFSheet ws=workbook.getSheet(sheet);
		XSSFCell cell ;

		int  row = findRow(ws,scenario);

		if(row==-1) {
			System.out.println("No such test data available");
			workbook.close();
			return null;
		}
		
		
		int lastrow = ws.getLastRowNum();
		int lastcol = ws.getRow(0).getLastCellNum();
		
		JSONObject obj = new JSONObject();
		
			for(int j=1;j<lastcol; j++) {
				String key = ws.getRow(0).getCell(j).getStringCellValue();
				System.out.println(key);
				if( ws.getRow(row).getCell(j) == null ||ws.getRow(row).getCell(j).getCellType() == CellType.BLANK) {
					continue;
				}
				
				if(ws.getRow(row).getCell(j).getCellType()== CellType.FORMULA){
					CellValue cellValue = evaluator.evaluate(ws.getRow(row).getCell(j));		
					System.out.println(cellValue.getStringValue());
					obj.put(key,cellValue.getStringValue());
					continue;
				}
				
				if(ws.getRow(row).getCell(j).getCellType() == CellType.STRING) {
					String value = ws.getRow(row).getCell(j).getStringCellValue();
					System.out.println(value);
					obj.put(key, value);
				}
				else if (ws.getRow(row).getCell(j).getCellType() == CellType.BOOLEAN) {
					obj.put(key,ws.getRow(row).getCell(j).getBooleanCellValue());
					System.out.println(ws.getRow(row).getCell(j).getBooleanCellValue());
					continue;
				}
				else
				{
					if( DateUtil.isCellDateFormatted(ws.getRow(row).getCell(j))){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String dateValue = format.format(ws.getRow(row).getCell(j).getDateCellValue());
					System.out.println(dateValue);
					obj.put(key, dateValue);
					}
					else {
					System.out.println(ws.getRow(row).getCell(j).getNumericCellValue());
					obj.put(key, ws.getRow(row).getCell(j).getNumericCellValue());
				
					}
			
		}
			}
		
		
		workbook.close();
		return(obj);

	/*	XSSFRow rowNumber =ws.getRow(++row);
		int colCount =rowNumber.getLastCellNum();
		String[] keyValues = new String[colCount];
		Iterator<Cell> cells = rowNumber.cellIterator();
		for (int i = 0;i<colCount;i++) {
			cell= (XSSFCell) cells.next();

			keyValues[i] = cell.getStringCellValue();
		}


		rowNumber = ws.getRow(++row);
		JSONObject obj = new JSONObject();
		for (int j = 0;j<colCount;j++) {

			if( rowNumber.getCell(j) == null ||rowNumber.getCell(j).getCellType() == CellType.BLANK) {
				continue;
			}

			if(rowNumber.getCell(j).getCellType()== CellType.FORMULA){
				CellValue cellValue = evaluator.evaluate(rowNumber.getCell(j));		
				obj.put(keyValues[j],cellValue.getStringValue());
				continue;
			}

			else if(rowNumber.getCell(j).getCellType() == CellType.STRING) {
				DataFormatter df = new DataFormatter();
				String value = df.formatCellValue(rowNumber.getCell(j));

				obj.put(keyValues[j],value);
				continue;
			}
			else if (rowNumber.getCell(j).getCellType() == CellType.BOOLEAN) {
				obj.put(keyValues[j],rowNumber.getCell(j).getBooleanCellValue());
				continue;
			}
			else {
				if( DateUtil.isCellDateFormatted(rowNumber.getCell(j))){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String dateValue = format.format(rowNumber.getCell(j).getDateCellValue());
					obj.put(keyValues[j],dateValue);
					continue;
				}
				else {
					obj.put(keyValues[j],rowNumber.getCell(j).getNumericCellValue());
					continue;
				}
			}
		}*/

		
	}
	
	
	
	public JSONObject readCase(String sheet, String scenario, String filepath, String external) throws IOException {
		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		BaseFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();	  
	
		absPath = new File(external).getAbsolutePath();
		FileInputStream input1 = new FileInputStream(absPath);
		XSSFWorkbook workbook2 = new XSSFWorkbook(input1);

		//Setup workbook environment
		BaseFormulaEvaluator evaluator2 = workbook2.getCreationHelper().createFormulaEvaluator();	 
		Map< String,FormulaEvaluator> detail= new HashMap<>();
		detail.put("w1",evaluator);
		detail.put("w2",evaluator2);
		String[] workbookNames = {"Registration.xlsx","AccountManagement.xlsx"};
		BaseFormulaEvaluator[] evaluators = {evaluator,evaluator2};
		BaseFormulaEvaluator.setupEnvironment(workbookNames, evaluators);

		evaluator2.evaluateAll();
		evaluator.evaluateAll();


		XSSFSheet ws=workbook.getSheet(sheet);
		XSSFCell cell ;

		int  row = findRow(ws,scenario);

		if(row==-1) {
			System.out.println("No such test data available");
			workbook.close();
			return null;
		}
		
		
		int lastrow = ws.getLastRowNum();
		int lastcol = ws.getRow(0).getLastCellNum();
		
		JSONObject obj = new JSONObject();
		
			for(int j=1;j<lastcol; j++) {
				String key = ws.getRow(0).getCell(j).getStringCellValue();
				System.out.println(key);
				if( ws.getRow(row).getCell(j) == null ||ws.getRow(row).getCell(j).getCellType() == CellType.BLANK) {
					continue;
				}
				
				if(ws.getRow(row).getCell(j).getCellType()== CellType.FORMULA){
					CellValue cellValue = evaluator.evaluate(ws.getRow(row).getCell(j));		
					System.out.println(cellValue.getStringValue());
					obj.put(key,cellValue.getStringValue());
					continue;
				}
				
				if(ws.getRow(row).getCell(j).getCellType() == CellType.STRING) {
					String value = ws.getRow(row).getCell(j).getStringCellValue();
					System.out.println(value);
					obj.put(key, value);
				}
				else if (ws.getRow(row).getCell(j).getCellType() == CellType.BOOLEAN) {
					obj.put(key,ws.getRow(row).getCell(j).getBooleanCellValue());
					System.out.println(ws.getRow(row).getCell(j).getBooleanCellValue());
					continue;
				}
				else
				{
					if( DateUtil.isCellDateFormatted(ws.getRow(row).getCell(j))){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String dateValue = format.format(ws.getRow(row).getCell(j).getDateCellValue());
					System.out.println(dateValue);
					obj.put(key, dateValue);
					}
					else {
					System.out.println(ws.getRow(row).getCell(j).getNumericCellValue());
					obj.put(key, ws.getRow(row).getCell(j).getNumericCellValue());
				
					}
			
		}
			}
		
		
		workbook.close();
		workbook2.close();
		return(obj);
	}



	public JSONObject readData(String sheet,String filepath) throws IOException{

		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();	 
		evaluator.evaluateAll();

		XSSFSheet ws=workbook.getSheet(sheet);
		XSSFRow row =ws.getRow(0);
		XSSFCell cell ;
		int colCount = row.getLastCellNum();


		String[] keyValues = new String[colCount];
		Iterator<Cell> cells = row.cellIterator();

		for (int i = 0;i<colCount;i++) {
			cell= (XSSFCell) cells.next();

			keyValues[i] = cell.getStringCellValue();
		}



		XSSFRow rowNo = ws.getRow(1);
		JSONObject obj = new JSONObject();
		for (int j = 0;j<colCount;j++) {

			if( rowNo.getCell(j) == null ||rowNo.getCell(j).getCellType() == CellType.BLANK) {
				continue;
			}

			if(rowNo.getCell(j).getCellType()== CellType.FORMULA){
				CellValue cellValue = evaluator.evaluate(rowNo.getCell(j));
				obj.put(keyValues[j],cellValue.getStringValue());
				continue;

			}

			else if(rowNo.getCell(j).getCellType() == CellType.STRING) {
				DataFormatter df = new DataFormatter();
				String value = df.formatCellValue(rowNo.getCell(j));
				obj.put(keyValues[j],value);
				continue;
			}
			else if (rowNo.getCell(j).getCellType() == CellType.BOOLEAN) {
				obj.put(keyValues[j],rowNo.getCell(j).getBooleanCellValue());
				continue;
			}
			else {
				if( DateUtil.isCellDateFormatted(rowNo.getCell(j))){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String dateValue = format.format(rowNo.getCell(j).getDateCellValue());
					obj.put(keyValues[j],dateValue);
					continue;
				}
				else {
					obj.put(keyValues[j],rowNo.getCell(j).getNumericCellValue());
					continue;


				}
			}



		}
		System.out.println(obj);
		workbook.close();
		return(obj);


	}


	public void updateField(String sheet, String filePath, String field) throws Exception {


		String absPath = new File(filePath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		XSSFSheet ws=workbook.getSheet(sheet);

		//find row of the required field
		int rownumber = findRow(ws, field);
		System.out.println(rownumber);
		long i = Long.parseLong(ws.getRow(rownumber).getCell(1).getStringCellValue());
		String value = String.valueOf(i+1);
		ws.getRow(rownumber).getCell(1).setCellValue(value);

		input.close();

		FileOutputStream outputStream = new FileOutputStream(absPath);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}


	public void writeStringData(String sheet, String field, String value, String filepath ) throws Exception {

		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		XSSFSheet ws=workbook.getSheet(sheet);

		int rownumber = findRow(ws, field);
		ws.getRow(rownumber).getCell(1).setCellValue(value);



		/*for (int j = 0;j<=rowCount;j++) {

			if( ws.getRow(j) == null) {
				continue;
			}

			System.out.println(ws.getRow(j).getCell(0).getStringCellValue());
			if(ws.getRow(j).getCell(0).getStringCellValue().equals(field) ) {
				ws.getRow(j).getCell(1).setCellValue(value);
				System.out.println(ws.getRow(j).getCell(0).getStringCellValue());
				break;
			}
		}*/

		input.close();

		FileOutputStream outputStream = new FileOutputStream(absPath);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();


	}

	public String generateNo(int digits) {
		Faker faker = new Faker();
		String number = faker.number().digits(digits);
		System.out.println(number);
		return number;

	}



	public void writeIntegerData(String sheet, String field, int value, String filepath ) throws Exception {

		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		XSSFSheet ws=workbook.getSheet(sheet);

		int rownumber = findRow(ws, field);
		ws.getRow(rownumber).getCell(1).setCellValue(value);

		input.close();

		FileOutputStream outputStream = new FileOutputStream(absPath);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();


	}


	public JSONObject readRowField(String sheet, String field, String filepath ) throws Exception {

		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();	 
		evaluator.evaluateAll();

		XSSFSheet ws=workbook.getSheet(sheet);

		int rowCount=ws.getPhysicalNumberOfRows();

		JSONObject obj = new JSONObject();
		for (int j = 0;j<rowCount;j++) {
			if(ws.getRow(j).getCell(0).getStringCellValue().equals(field) ) {
				if(ws.getRow(j).getCell(1).getCellType() == CellType.STRING) {
					obj.put(field,ws.getRow(j).getCell(1).getStringCellValue());
					break;
				}
				else {

					obj.put(field,ws.getRow(j).getCell(1).getNumericCellValue());

				}

			}
		}

		System.out.println(obj);

		workbook.close();

		return obj;

	}





	public JSONObject readData(String sheet,String filepath, String external) throws IOException{

		String absPath = new File(filepath).getAbsolutePath();
		FileInputStream input = new FileInputStream(absPath);
		XSSFWorkbook workbook = new XSSFWorkbook(input);

		BaseFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();	 


		absPath = new File(external).getAbsolutePath();
		FileInputStream input1 = new FileInputStream(absPath);
		XSSFWorkbook workbook2 = new XSSFWorkbook(input1);

		//Setup workbook environment
		BaseFormulaEvaluator evaluator2 = workbook2.getCreationHelper().createFormulaEvaluator();	 
		Map< String,FormulaEvaluator> detail= new HashMap<>();
		detail.put("w1",evaluator);
		detail.put("w2",evaluator2);
		String[] workbookNames = {"Registration.xlsx","AccountManagement.xlsx"};
		BaseFormulaEvaluator[] evaluators = {evaluator,evaluator2};
		BaseFormulaEvaluator.setupEnvironment(workbookNames, evaluators);

		evaluator2.evaluateAll();
		evaluator.evaluateAll();



		XSSFSheet ws=workbook.getSheet(sheet);

		XSSFRow row =ws.getRow(0);
		XSSFCell cell ;
		int colCount = row.getLastCellNum();

		String[] keyValues = new String[colCount];
		Iterator<Cell> cells = row.cellIterator();
		for (int i = 0;i<colCount;i++) {
			cell= (XSSFCell) cells.next();

			keyValues[i] = cell.getStringCellValue();
		}


		XSSFRow rowNo = ws.getRow(1);
		JSONObject obj = new JSONObject();
		for (int j = 0;j<colCount;j++) {

			if( rowNo.getCell(j) == null ||rowNo.getCell(j).getCellType() == CellType.BLANK) {
				continue;
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if(rowNo.getCell(j).getCellType()== CellType.FORMULA){
				CellValue cellValue = evaluator.evaluate(rowNo.getCell(j));
				obj.put(keyValues[j],cellValue.getStringValue());
				continue;

			}

			else if(rowNo.getCell(j).getCellType() == CellType.STRING) {
				DataFormatter df = new DataFormatter();
				String value = df.formatCellValue(rowNo.getCell(j));
				obj.put(keyValues[j],value);
				continue;
			}
			else if (rowNo.getCell(j).getCellType() == CellType.BOOLEAN) {
				obj.put(keyValues[j],rowNo.getCell(j).getBooleanCellValue());
				continue;
			}
			else {
				if( DateUtil.isCellDateFormatted(rowNo.getCell(j))){

					String dateValue = format.format(rowNo.getCell(j).getDateCellValue());
					obj.put(keyValues[j],dateValue);
					continue;
				}
				else {
					obj.put(keyValues[j],rowNo.getCell(j).getNumericCellValue());
					continue;

				}
			}
		}


		System.out.println(obj);
		workbook.close();
		workbook2.close();
		return(obj);


	}

}
