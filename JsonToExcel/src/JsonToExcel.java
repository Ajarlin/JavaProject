import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JsonToExcel{
	
	private static String[] columns = {"first_name", "last_name", "date_of_birth"};
	private static List<Employees> employeesList = new ArrayList<Employees>();
	
	
	public static void main(String args[]){
	System.out.println("========================================");	
	
	
	System.out.println("Give a Json directory\n\tExample: C:\\Users\\Soul\\Desktop\\people.json");	
	Map<String, List<String>> employeesMap = new HashMap<>();
	
	
	while(true){
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		input = input.trim();
		if(input.equals("exit")){
			break;
		}
		if(input.endsWith(".json")){
			try{
				File fileName =  new File(input);
				String cleanFileName = fileName.getName().replaceFirst("[.][^.]+$", "");
				String contents = new String((Files.readAllBytes(Paths.get(input))));
				JSONObject jsonObject = new JSONObject(contents);
				//System.out.println(jsonObject);
				JSONArray jsonArray = jsonObject.getJSONArray("user_info");
				
				List<String> firstNames = new ArrayList<String>();
				List<String> lastNames = new ArrayList<String>();
				List<String> dob = new ArrayList<String>();
	
	
				for(int i = 0 ; i < jsonArray.length(); i++) {
			    	  firstNames.add(valueNameChecker(jsonArray, i, "first_name"));
			    	  lastNames.add(valueNameChecker(jsonArray, i, "last_name"));
			    	  dob.add(valueDateChecker(jsonArray, i));
			          //System.out.println(jsonArray.getJSONObject(i)); // display usernames
			      }
				
				setUpEmployeesMap(cleanFileName, employeesMap, firstNames, lastNames, dob);
			} catch (IOException e){
				e.printStackTrace();
			}
		} else{
			System.out.println("The file you inserted was not a json file\n");
		}
	
	} 
	
	
	
	
	
	
	
	
	System.out.println("========================================");	
	System.out.println("Program has been terminated.");

	}
	
	//Checks if the date is properly formatted, else failed validation
	private static String valueDateChecker(JSONArray jArr, int j) {
		if(jArr.optJSONObject(j)!= null && jArr.optJSONObject(j).length()!=0){
			//System.out.println("Is a String/not empty");
			if(jArr.optJSONObject(j).isNull("date_of_birth")){
				return "N/A";
			}
				if(isValidDate(jArr.getJSONObject(j).getString("date_of_birth"))){
					return jArr.getJSONObject(j).getString("date_of_birth");
				}
				else{
					return "Invalid: Improper Date Format";
				}
		} else{
			//System.out.println("Is Null");
			return "Nothing Found";
		}
	}

	//Takes in a string, returns the String if its all alphabetical, else failed validation
	public static String valueNameChecker(JSONArray jArr, int j, String key){
		if(jArr.optJSONObject(j)!= null && jArr.optJSONObject(j).length()!=0){
			//System.out.println("Is a String/not empty");
			if(jArr.optJSONObject(j).isNull(key)){
				return "N/A";
			}
				if(isAlpha(jArr.getJSONObject(j).getString(key))){
					return jArr.getJSONObject(j).getString(key);
				} else{
					return "Invalid: Not Alpha";
				}
			
		} else{
			return "Nothing Found";
		}
	}
	
	
	//adds all the lists of values into a hashmap
	public static void setUpEmployeesMap(String fileName, Map<String, List<String>> employeesMap, List<String> firstNames, List<String> lastNames, List<String> dob) throws IOException{
		//System.out.println(firstNames + "\nsize of list "+firstNames.size());
		//System.out.println(lastNames + "\nsize of list "+lastNames.size());
		//System.out.println(dob + "\nsize of list "+dob.size());
		employeesMap.put("first_name", firstNames);
		employeesMap.put("last_name", lastNames);
		employeesMap.put("date_of_birth", dob);
		printEmployees(employeesMap);
		
		
		int empMapSize = employeesMap.values().stream().mapToInt(Collection::size).sum()/3;
		System.out.println(empMapSize);
		for(int i=0; i<empMapSize; i++){
			employeesList.add(new Employees(employeesMap.get("first_name").get(i), employeesMap.get("last_name").get(i), employeesMap.get("date_of_birth").get(i)));
		}
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(fileName);
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 16);
		
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		Row headerRow = sheet.createRow(0);
		
		for(int i = 0; i<columns.length; i++){
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		
		int rowNum = 1;
		
		for(Employees emp: employeesList){
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(emp.firstName);
			row.createCell(1).setCellValue(emp.lastName);
			row.createCell(2).setCellValue(emp.dateOfBirth);
		}
		
		for(int j = 0; j < columns.length; j++){
			sheet.autoSizeColumn(j);
		}
		
		FileOutputStream fileOut = new FileOutputStream(fileName +".xls");
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		//System.out.println(sheet.getPhysicalNumberOfRows()+" "+sheet.getFirstRowNum()+" "+sheet.getLastRowNum());
		System.out.println("xls file created");
		delimitedPrinter(employeesMap, fileName);
	}
	
	private static void delimitedPrinter(Map<String, List<String>> employeesMap, String fileName) {
		int empMapSize = employeesMap.values().stream().mapToInt(Collection::size).sum()/employeesMap.values().size();
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(fileName + "_output" + ".txt");
			String output = "";
			String lineSeparator = System.getProperty("line.separator");
			for(int i=0; i<empMapSize; i++){
				if(i!=0){
					output = employeesMap.get("first_name").get(i) + "|" + employeesMap.get("last_name").get(i) + "|" +employeesMap.get("date_of_birth").get(i)+lineSeparator;
				} else{
					output = "first_name" + "|" + "last_name"+ "|" +"date_of_birth" +lineSeparator;
				}
				writer.write(output);
			}
		} catch(FileNotFoundException e){
			System.out.println("Delimited file not found");
		} finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
		System.out.println("Output file created");
	}

	// Checks if a string is alpha only using Regex
	public static boolean isAlpha(String name){
		return name.matches("[a-zA-Z]+");
	}
	
	//Checks if the date is properly formated to MM/DD/YYYY format
	public static boolean isValidDate(String date){
		date = date.trim();
		if(date.equals("")){
			return false;
		}
		else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			dateFormat.setLenient(false);
			try{
				//Will check if the date is a valid date format
				Date jDate = dateFormat.parse(date);
			} catch (ParseException e){
				return false;
			}
		}
		
		return true;	
	}
	
	//Print outs the value of employees map keys, values
	public static void printEmployees(Map<String, List<String>> employeesMap){
		employeesMap.forEach((key, value) -> System.out.println(key + ": " + value));
	}
	
}