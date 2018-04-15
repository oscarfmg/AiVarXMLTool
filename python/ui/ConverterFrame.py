import Tkinter as tk
from Tkinter import Frame
from ttk import Notebook
import tkFileDialog
import Tkconstants
import tkMessageBox

from NumbersPane import NumbersPane
from RawTextPane import RawTextPane

class ConverterFrame(Frame):
    def __init__(self,converter,master=None):
        Frame.__init__(self,master)

        master.title("Ai Variable tool")

        self.converter = converter

        self.grid(row=0, sticky=(tk.N+tk.E+tk.W+tk.S))
        self.grid_columnconfigure(0, weight=1)
        self.grid_rowconfigure(0, weight=1)

        self.tabbedPane = Notebook(self,name="tabbed-pane")
        self.tabbedPane.grid(row=0, column=0, sticky=(tk.N+tk.E+tk.W+tk.S))

        self.btnSave = tk.Button(self,text="Guardar",command=self.btnSaveCallback)
        self.btnSave.grid(row=1,column=0,sticky=(tk.N+tk.E+tk.W+tk.S))

        self.rawTextPanel = RawTextPane(self.tabbedPane)
        self.rawTextPanel.grid(row=0, column=0, sticky=(tk.N+tk.E+tk.W+tk.S))

        self.numbersPanel = NumbersPane(self.tabbedPane)
        self.numbersPanel.grid(row=0, column=0, sticky=(tk.N+tk.E+tk.W+tk.S))

        self.tabbedPane.add(self.numbersPanel,text="Series de numeros")
        self.tabbedPane.add(self.rawTextPanel, text="Texto en crudo")

        self.panels = [self.numbersPanel,self.rawTextPanel]

    def btnSaveCallback(self):        
        filename = tkFileDialog.asksaveasfilename(initialdir=".", title="Select file", filetypes=(("Xml files", "*.xml"),("All files","*.*")))

        current_idx = self.tabbedPane.index("current")
        work_text = self.panels[current_idx].getText()

        self.converter.convert(work_text)
        if not self.converter.save(filename):
            tkMessageBox.showerror("Error","El archivo no ha sido almacenado.")
        else:
            tkMessageBox.showinfo("Exito","El archivo ha sido almacenado adecuadamente.")
