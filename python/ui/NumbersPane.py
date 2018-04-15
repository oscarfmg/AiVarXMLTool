# coding=utf-8

import Tkinter as tk
from WorkPanel import WorkPanel

class NumbersPane(tk.Frame):
    def __init__(self,master):
        tk.Frame.__init__(self,master,name="numbers-pane")

        self.grid(row=0, sticky=(tk.N+tk.E+tk.W+tk.S))
        for col in xrange(2):
            self.grid_columnconfigure(col, weight=1)
        for row in xrange(4):
            self.grid_rowconfigure(row, weight=1)

        self.lblInit = tk.Label(self,text="Inicio:")
        self.lblInit.grid(row=0,column=0,sticky=tk.W)

        self.txtInit = tk.StringVar(self, "")
        self.entryInit = tk.Entry(self,textvariable=self.txtInit,width=3)
        self.entryInit.grid(row=0,column=1,sticky=tk.E+tk.W)

        self.lblEnd = tk.Label(self,text="Fin:")
        self.lblEnd.grid(row=1,column=0,sticky=tk.W)

        self.txtEnd = tk.StringVar(self, "")
        self.entryEnd = tk.Entry(self,textvariable=self.txtEnd,width=3)
        self.entryEnd.grid(row=1, column=1, sticky=tk.E+tk.W)

        self.lblNumbersXSheet = tk.Label(self,text="Números X Hoja:")
        self.lblNumbersXSheet.grid(row=2,column=0,sticky=tk.W)

        self.txtNumbersXSheet = tk.StringVar(self,"")
        self.entryNumbersXSheet = tk.Entry(
            self, textvariable=self.txtNumbersXSheet, width=3)
        self.entryNumbersXSheet.grid(row=2, column=1, sticky=tk.E+tk.W)

        self.lblUseZero = tk.Label(self, text="Agregar ceros:")
        self.lblUseZero.grid(row=3, column=0,sticky=tk.W)

        self.blnUseZero = tk.BooleanVar(self,False)
        self.chkUseZero = tk.Checkbutton(self,variable=self.blnUseZero,onvalue=True,offvalue=False)
        self.chkUseZero.grid(row=3,column=1,sticky=tk.W)

    def getText(self):
        if self.txtInit.get() == "":
            self.txtInit.set("0")
        if self.txtEnd.get() == "":
            self.txtEnd.set("0")
        if self.txtNumbersXSheet.get() == "":
            self.txtNumbersXSheet.set("1")
        
        init = int(self.txtInit.get())
        end  = int(self.txtEnd.get())
        nXs  = int(self.txtNumbersXSheet.get())
        if nXs <= 0:
            nXs = 1
        total = end - init + 1
        numSheets = total / nXs

        if total%nXs!=0:
            numSheets += 1
        
        header_list = ["c{0}".format(cell) for cell in xrange(1,nXs+1)]
        header = ",".join(header_list)

        leading_zero_format = ""
        if self.blnUseZero.get():
            leading_zero_format = ":0{LEN}".format(LEN=len(self.txtEnd.get()))
        
        number_format = "{{0{LEADING_FORMAT}}}".format(LEADING_FORMAT=leading_zero_format)
        
        numbers_list = [",".join([number_format.format((init+row)+(numSheets*cell)) for cell in xrange(nXs)]) for row in xrange(numSheets)]
        numbers_string = "\n".join(numbers_list)

        return header+"\n"+numbers_string
