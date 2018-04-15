def DataGridRender(dataGrid, headerNames, headerTypes, indent, newLine):
	#inits...
	commentLine="<!--"
	commentLineEnd="-->"
	outputText=""
	numRows=len(dataGrid)
	numColumns=len(headerNames)

	template = open('template.xml').read().split(newLine)

	outputText = []	
	#begin render loop (HardCodeMaster)
	for i in range(len(template)):
		if (i >= 2 and i<=7) or (i==11) or (i==22):
			lvlIndent=1
		elif (i==12) or (i>=14 and i<=15) or (i==21):
			lvlIndent=2
		else:
			lvlIndent=0

		if (i==13) or (i>=16 and i<=20): #Skips for template's lines already used
			continue

		outputText.append(indent*lvlIndent+template[i]) #Append a simple line of template

		if i==12:
			i=i+1
			lvlIndent=3
			for header in headerNames:
				outputText.append(indent*lvlIndent+template[i].replace("%VALUE%",header))
		elif i==15:
			i=i+1
			for row in dataGrid:
				lvlIndent=3
				outputText.append(indent*lvlIndent+template[i].replace("%VALUE%",row[0]))
				lvlIndent=4
				for j in range(numColumns):
					outputText.append(indent*lvlIndent+template[i+1].replace("%VALUE%",headerNames[j]))
					outputText.append(indent*(lvlIndent+1)+template[i+2].replace("%VALUE%",row[j]))
					outputText.append(indent*lvlIndent+template[i+3].replace("%VALUE%",headerNames[j]))
				lvlIndent=3
				outputText.append(indent*lvlIndent+template[i+4])

	return newLine.join(outputText)
