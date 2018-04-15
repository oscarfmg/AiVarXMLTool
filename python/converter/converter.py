import DataGridRender
import CSVParser
import sys,traceback

class DataConverter:
	def __init__(self):
		self.columnDelimiter	= '\t' 
		self.rowDelimiter		= '\n'
		self.newLine			= '\n'
		self.delimiter			= None
		self.indent				= '  '
		self.commentLine		= '//'
		self.useUnderscores		= True;
		self.headersProvided	= True;
		self.downcaseHeaders	= False;
		self.upcaseHeaders		= False;
		self.includeWhiteSpace	= True;
		self.useTabsForIndent	= False;
		self.inputText			= ""
		self.outputText			= ""
		self.CsvParser			= CSVParser.CSVParser()

	def convert(self,input):
		self.inputText = input

		#make sure there is input data before converting...
		if len(self.inputText) > 0:
			if self.includeWhiteSpace:
				self.newLine = '\n'
			else:
				self.indent=''
				self.newLine=''

		self.CsvParser.resetLog()
		parseOutput = self.CsvParser.parse(self.inputText,self.headersProvided,self.delimiter, self.downcaseHeaders, self.upcaseHeaders)
		self.outputText = DataGridRender.DataGridRender(parseOutput["dataGrid"],parseOutput["headerNames"],parseOutput["headerTypes"],self.indent,self.newLine)

	def save(self,fileName):
		try:
			outputFile = open(fileName,'w')
			outputFile.write(self.outputText)
			outputFile.close()
		except:
			traceback.print_exc(file=sys.stdout)
			return False
		return True

if __name__ == "__main__":
	x = DataConverter()
	archivo = open('pb.csv').read()
	x.convert(archivo)
	x.save("pb.xml")
	input("Presiona Enter para continuar...")
