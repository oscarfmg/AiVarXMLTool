package converter;

import java.util.regex.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CSVParser{
	
	public CSVParser(){
		errorLog="";		
	}

	public static boolean isNumber(String input){
		try{
			if( input==null || new Float(input).isNaN())
				return false;
		} catch (NumberFormatException e){
			return false;
		}

		return true;
	}

	public CSVSheet parse(String input, boolean headersIncluded, String delimiterType, boolean downcaseHeaders, boolean upcaseHeaders){
		//errors=[]
		// dataArray=[]
		int numCommas, numTabs;

		// test for delimiter
		// count the number of commas
		Pattern RE = Pattern.compile("[^,]");
		numCommas = RE.matcher(input).replaceAll("").length();

		// count the number of tabs
		RE = Pattern.compile("[^\t]");
		numTabs = RE.matcher(input).replaceAll("").length();

		String rowDelimiter = "\n";
		// set delimiter
		String columnDelimiter = ",";
		if (numTabs > numCommas)
			columnDelimiter = "\t";

		if(delimiterType.equals("comma"))
			columnDelimiter = ",";
		else if(delimiterType.equals("tab"))
			columnDelimiter = "\t";

		// kill extra empty lines
		RE = Pattern.compile("^"+rowDelimiter+"+");
		input = RE.matcher(input).replaceAll("");
		RE = Pattern.compile(rowDelimiter+"+$");
		input = RE.matcher(input).replaceAll("");

		ArrayList<ArrayList<String>> dataArray = CSVToArray(input, columnDelimiter);
		// escape out any tabs or returns or new lines
		for(int i=dataArray.size()-1;i>=0;i--){
			for(int j=dataArray.get(i).size()-1;j>=0;j--){
				dataArray.get(i).set(j,dataArray.get(i).get(j).replace("\t","\\t"));
				dataArray.get(i).set(j,dataArray.get(i).get(j).replace("\n","\\n"));
				dataArray.get(i).set(j,dataArray.get(i).get(j).replace("\r","\\r"));
			}
		}

		ArrayList<String> headerNames;
		ArrayList<String> headerTypes = new ArrayList<String>();
		int numColumns = dataArray.get(0).size();
		int numRows = dataArray.size();

		if(headersIncluded){
			// remove header row
			headerNames = dataArray.remove(0);
			numRows = dataArray.size();
		} else { //if no headerNames provided
			// create generic property names
			headerNames = new ArrayList<String>();
			for(int i=0;i<numColumns;i++){
				headerNames.add("val"+i);
				// headerTypes.add("");
			}
		}

		if(upcaseHeaders){
			for(int i=headerNames.size()-1;i>=0;i--){
				headerNames.set(i,headerNames.get(i).toUpperCase());
			}
		}
		if(downcaseHeaders){
			for(int i=headerNames.size()-1;i>=0;i--){
				headerNames.set(i,headerNames.get(i).toLowerCase());
			}
		}

		// test all the rows for proper number of columns.
		for(int i=0;i<dataArray.size();i++){
			int numValues = dataArray.get(i).size();
			if(numValues != numColumns)
				this.log("Error parsing row "+i+". Wrong number of columns.");
		}

		// test columns for number data type
		int numRowsToTest = numColumns;
		float threshold = 0.5f;
		for(int i=0;i<headerNames.size();i++){
			int numFloats=0;
			int numInts=0;
			for(int r=0;r<numRowsToTest&&r<dataArray.size();r++){
				if(dataArray.get(r)!=null){
					if(CSVParser.isNumber(dataArray.get(r).get(i))){
						numInts++;
						if(dataArray.get(r).get(i).indexOf(".")>0){
							numFloats++;
						}
					}
				}
			}

			if( ((float)numInts / (float)numRowsToTest) > threshold){
				if(numFloats > 0){
					headerTypes.add("float");
				} else {
					headerTypes.add("int");
				}
			} else {
				headerTypes.add("string");
			}
		}

		return new CSVSheet(dataArray,headerNames,headerTypes,this.getLog());
	}

//--------------------------------------
// ERROR LOGGING
//--------------------------------------
	private String errorLog;

	public void resetLog(){
		errorLog="";
	}

	public void log(String l){
		errorLog+=l+" !!\n";
	}

	public String getLog(){
		return errorLog;
	}

//--------------------------------------
// UTIL
//--------------------------------------
	public ArrayList<ArrayList<String>> CSVToArray(String strData, String strDelimiter){ //CSVToArrayList
		// This Function from Ben Nadel, http://
		// This will parse a delimited string into an array of
		// arrays. The default delimiter is the comma, but this
		// can be overriden in the second argument.

		if(strDelimiter==null || strDelimiter.equals(""))
			strDelimiter = ",";

		// Create a regular expression to parse the CSV values.
		Pattern objPattern = Pattern.compile(
			// Delimiters.
			"(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
			// Quoted fields.
			"(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
			// Standad fields.
			"([^\"\\" + strDelimiter + "\\r\\n]*))"
		);

		// Create an array to hold our data. Give the array
		// a default empty first row.
		ArrayList<ArrayList<String>> arrData = new ArrayList<ArrayList<String>>();
		arrData.add(new ArrayList<String>());

		// Create an array to hold our individual pattern
		// matching groups.
		Matcher valMatcher = objPattern.matcher(strData);

		// Keep looping over the regular expression matches
		// until we can no longer find a match.
		while(valMatcher.find()){
			// Get the delimiter that was found.
			String strMatchedDelimiter = valMatcher.group(1);

			// Check to see if the given delimiter has a length
			// (is not the start of string) and if it matches
			// field delimiter. If id does not, then we know
			// that this delimiter is a row delimiter.
			if(	strMatchedDelimiter.length() > 0 &&
			    !strMatchedDelimiter.equals(strDelimiter) ){
				// Since we have reached a new row of data,
				// add an empty row to our data array.
				arrData.add(new ArrayList<String>());
			}

			// Now that we have our delimiter out of the way,
			// let's check to see which kind of value we
			// captured (quoted or unquoted).
			String strMatchedValue="";
			if( valMatcher.group(2) != null ){
				// We found a quoted value. When we capture
				// this value, unescape any double quotes.
				strMatchedValue = Pattern.compile("\"\"").matcher(valMatcher.group(2)).replaceAll("\"");
			} else {
				// We found a non-quoted value.
				strMatchedValue = valMatcher.group(3);
			}

			// Now that we have our value string, let's add
			// it to the data array.
			arrData.get(arrData.size()-1).add(strMatchedValue);
		}

		// Return the parsed data.
		return arrData;
	}

	public static void main(String[] args){
		try{
			String file = new Scanner(new File("pb.csv")).useDelimiter("\\Z").next();

			CSVParser csvParser = new CSVParser();
			CSVSheet csvSheet = csvParser.parse(file,true,"",false,false);
			System.out.println(csvSheet);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}