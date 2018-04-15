import math, re
import DataGridRender

class CSVParser:
	def __init__(self):
		self.errorLog = []

	@staticmethod
	def isNumber(string):
		try:
			if not string or math.isnan(float(string)):
				return False
		except ValueError:
			return False
		return True
				  #string  #bool		    #string        #bool			#bool
	def parse(self, input, headersIncluded, delimiterType, downcaseHeaders, upcaseHeaders):
		dataArray=[]
		errors=[]

		#test for delimiter
		#count the number of commas
		RE = r"[,]"
		numCommas = len(re.findall(RE,input))

		#count the number of tabs
		RE = r"[\t]"
		numTabs = len(re.findall(RE,input))

		rowDelimiter = "\n"
		#set delimiter
		columnDelimiter = ","
		if numTabs > numCommas:
			columnDelimiter = "\t"

		if delimiterType == "comma":
			columnDelimiter = ","
		elif delimiterType == "tab":
			columnDelimiter = "\t"

		#kill extra empty lines
		RE = r'^'+rowDelimiter+'+'
		input = re.sub(RE,"",input)
		RE = rowDelimiter+"+$"
		input = re.sub(RE,"",input)

		dataArray = self.CSVToArray(input,columnDelimiter)

		#escape out any tabs or returns or new lines
		for i in range(len(dataArray))[::-1]:
			for j in range(len(dataArray[i]))[::-1]:
				dataArray[i][j] = dataArray[i][j].replace("\t","\\t")
				dataArray[i][j] = dataArray[i][j].replace("\n","\\n")
				dataArray[i][j] = dataArray[i][j].replace("\r","\\r")

		headerNames=[]
		headerTypes=[]
		numColumns = len(dataArray[0])
		numRows = len(dataArray)

		if headersIncluded:
			#remove header row
			headerNames = dataArray[0]
			dataArray = dataArray[1:]
			numRows = len(dataArray)
			headerTypes = ["" for i in range(numRows)]
		else: #if no headerNames provided
			#create generic property names
			for i in range(numColumns):
				headerNames.append("val"+str(i))
				headerTypes.append("")

		if upcaseHeaders:
			headerNames = [header.upper() for header in headerNames]
		if downcaseHeaders:
			headerNames = [header.lower() for header in headerNames]

		#Test all the rows for proper number of columns.
		for i in range(len(dataArray)):
			numValues = len(dataArray[i])
			if numValues != numColumns:
				self.log("Error parsing row "+str(i)+". Wrong number of columns.")

		#Test columns for number data type
		numRowsToTest = numColumns
		threshold = 0.5
		for i in range(len(headerNames)):
			numFloats=0
			numInts=0
			for r in range(numRowsToTest):
				if dataArray[r]:
					if CSVParser.isNumber(str(dataArray[r][i])):
						numInts=numInts+1
						if str(dataArray[r][i]).find(".") > 0:
							numFloats=numFloats+1
			
			if numInts/numRowsToTest > threshold:
				if numFloats > 0:
					headerTypes[i] = "float"
				else:
					headerTypes[i] = "int"
			else:
				headerTypes[i] = "string"

		#return [dataArray,headerNames,headerTypes,self.getLog()]
		return {'dataGrid':dataArray, 'headerNames':headerNames, 'headerTypes':headerTypes, 'errors':self.getLog()}


#---------------------------------------
# ERROR LOGGING
#---------------------------------------
	errorLog=[]

	def resetLog(self):
		self.errorLog = []

	def log(self,l):
		self.errorLog.append(l)

	def getLog(self):
		return ' !!\n'.join(self.errorLog)

#---------------------------------------
# UTIL
#---------------------------------------
	def CSVToArray(self,strData,strDelimiter): #CSVToList :P
		#This Function from Ben Nadel, http://www.bennadel.com/blog/1504-Ask-Ben-Parsing-CSV-Strings-With-Javascript-Exec-Regular-Expression-Command.htm
		#This will parse a delimited string into an array of
		#arrays. The default delimiter is the comma, but this
		#can be overriden in the second argument.
		if not strDelimiter:
			strDelimiter=","

		#Create a regular expression to parse the CSV values.
		objPattern = (
			# Delimiters.
			"(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
			# Quoted fields.
			"(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
			# Standard fields.
			"([^\"\\" + strDelimiter + "\\r\\n]*))"
		)

		#Create an array to hold our data. Give the array
		#a default empty first row.
		arrData = [[]]

		#Create an array to hold our individual pattern
		#matching groups.
		arrMatches = None

		#Keep looping over the regular expression matches
		#until we can no longer find a match.
		for arrMatches in re.finditer(objPattern,strData):
			#Get the delimiter that was found.
			strMatchedDelimiter = arrMatches.group(1)

			#Check to see if the given delimiter has a length
			#(is not the start of string) and if it matches
			#field delimiter. If id does not, then we know
			#that this delimiter is a row delimiter.
			if len(strMatchedDelimiter) > 0 and strMatchedDelimiter != strDelimiter:
				#Since we have reached a new row of data,
				#add an empty row to our data array.
				arrData.append([])

			#Now that we have our delimiter out of the way,
			#let's check to see which kind of value we
			#captured (quoted or unquoted).
			if arrMatches.group(2):
				#We found a quoted value. When we capture
				#this value, unescape any double quotes.
				strMatchedValue = re.sub("\"\"","\"",arrMatches.group(2))
			else:
				#We found a non-quoted value.
				strMatchedValue = arrMatches.group(3)
			
			#Now that we have our value string, let's add
			#it to the data array.
			#arrData[len(arrData)-1].append(strMatchedValue)
			arrData[-1].append(strMatchedValue)
		return arrData