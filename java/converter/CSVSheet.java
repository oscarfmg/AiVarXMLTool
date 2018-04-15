package converter;

import java.util.ArrayList;

public class CSVSheet{
	private String[][] dataGrid;
	private String[] headerNames;
	private String[] headerTypes;
	private String errors;

	public CSVSheet(ArrayList<ArrayList<String>>dataArray,
	 ArrayList<String> headerNames,
	 ArrayList<String> headerTypes,
	 String errors){
		dataGrid = new String[dataArray.size()][];
		for(int r=0;r<dataArray.size();r++)
			dataGrid[r] = dataArray.get(r).toArray(new String[0]);
		this.headerNames = headerNames.toArray(new String[0]);
		this.headerTypes = headerTypes.toArray(new String[0]);
		this.errors = errors;
	}

	public String[][] getDataGrid(){
		return dataGrid;
	}

	public String[] getHeaderNames(){
		return headerNames;
	}

	public String[] getHeaderTypes(){
		return headerTypes;
	}

	public String getErrors(){
		return errors;
	}

	@Override
	public String toString(){
		StringBuilder strBldr = new StringBuilder();
		strBldr.append("{ dataGrid: ");
		strBldr.append("[");
		for(int i=0;i<dataGrid.length;i++){	
			printArray(strBldr, dataGrid[i]);
			if(i!=dataGrid.length-1)
				strBldr.append(", ");
		}
		strBldr.append("]");

		strBldr.append(",\nheaderNames: ");
		printArray(strBldr,headerNames);
		strBldr.append(",\nheaderTypes: ");
		printArray(strBldr,headerTypes);
		strBldr.append(",\nerrors: ");
		strBldr.append(errors);
		strBldr.append(" }");
		return strBldr.toString();
	}

	private void printArray(StringBuilder strBldr, String[] array){
		strBldr.append("[");
		for(int i=0;i<array.length;i++){
			strBldr.append(array[i]);
			if(i!=array.length-1)
				strBldr.append(", ");
		}
		strBldr.append("]");
	}
}