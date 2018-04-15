package converter;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;
import ui.ConverterFrame;

public class Converter{
	private String columnDelimiter		= "\t";
	private String rowDelimiter			= "\n";
	private String newLine				= "\n";
	private String delimiter			= "  ";
	private String indent				= "\t";
	private boolean useUnderscores		= true;
	private boolean headersProvided		= true;
	private boolean downcaseHeaders		= false;
	private boolean upcaseHeaders		= false;
	private boolean includeWhiteSpace	= true;
	private boolean useTabsForIndent	= false;
	private String inputText;
	private String outputText;
	private CSVParser csvParser;

	public Converter(){
		inputText = outputText = "";
		csvParser = new CSVParser();
	}

	public void convert(String input){
		inputText = input;

		// make sure there is input data before converting...
		if(inputText.length()>0){
			if(includeWhiteSpace)
				newLine="\n";
			else{
				indent="";
				newLine="";
			}
		}

		csvParser.resetLog();
		CSVSheet parsedCSV = csvParser.parse(inputText,headersProvided,delimiter,downcaseHeaders,upcaseHeaders);
		renderToAiXML(parsedCSV.getDataGrid(), parsedCSV.getHeaderNames(), parsedCSV.getHeaderTypes(), indent, newLine);
		// System.out.println(outputText);
	}

	public boolean save(String filename){
		File file = new File(filename);
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(outputText);
			writer.close();
		} catch( Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean save(File file){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(outputText);
			writer.close();
		} catch( Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String renderToAiXML(String[][] dataGrid, String[] headerNames, String[] headerTypes, String indent, String newLine){
		// inits
		int numColumns = headerNames.length;
		ArrayList<String> list = new ArrayList<String>();
		try{
			Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/template.xml"));
			while(scanner.hasNext()){
				list.add(scanner.nextLine());
			}
		} catch( Exception e){
			e.printStackTrace();
		}
		String[] template = list.toArray(new String[0]);

		ArrayList<String> output = new ArrayList<String>();

		// begin render loop (HardCodeMaster)
		int lvlIndent=0;
		for(int i=0; i<template.length;i++){
			if( (i>=2 && i<=7) || (i==11) || (i==22) )
				lvlIndent=1;
			else if( (i==12) || (i>=14 && i<=15) || (i==21) )
				lvlIndent=2;
			else
				lvlIndent=0;

			// if( (i==13) || (i>=16 && i<=20) ) //Skips for template´s lines already used
			// continue;

			output.add(makeIndent(lvlIndent,indent)+template[i]+newLine); //Append a simple line of template

			if(i==12){
				i++;
				lvlIndent=3;
				for(String header : headerNames)
					output.add(makeIndent(lvlIndent,indent)+template[i].replace("%VALUE%",header)+newLine);
			} else if(i==15){
				i++;
				for(String[] row : dataGrid){
					lvlIndent=3;
					output.add(makeIndent(lvlIndent,indent)+template[i].replace("%VALUE%",row[0])+newLine);
					lvlIndent=4;
					for(int j=0;j<numColumns;j++){
						output.add(makeIndent(lvlIndent,indent)+template[i+1].replace("%VALUE%",headerNames[j])+newLine);
						output.add(makeIndent(lvlIndent+1,indent)+template[i+2].replace("%VALUE%",row[j])+newLine);
						output.add(makeIndent(lvlIndent,indent)+template[i+3].replace("%VALUE%",headerNames[j])+newLine);
					}
					lvlIndent=3;
					output.add(makeIndent(lvlIndent,indent)+template[i+4]+newLine);
				}
				i+=4;
			}
		}

		StringBuilder sb = new StringBuilder();
		for(String s : output)
			sb.append(s);
		outputText = sb.toString();
		return outputText;
	}

	private String makeIndent(int lvl, String indent){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<lvl;i++)
			sb.append(indent);
		return sb.toString();
	}

	public String getText(){
		return outputText;
	}

	public static void main(String[] args){
		try{
			String file = new Scanner(new File("pb.csv")).useDelimiter("\\Z").next();

			Converter converter = new Converter();
			converter.convert(file);
			if(converter.save("pb.xml"))
				System.out.println("Test completed. File pb.xml saved.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}