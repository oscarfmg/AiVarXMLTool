# coding=utf-8

import Tkinter as tk
from WorkPanel import WorkPanel

class RawTextPane(tk.Frame,WorkPanel):
    def __init__(self, master):
        tk.Frame.__init__(self, master, name="raw-text-pane",width=500,height=200,bg="blue")

        self.grid(row=0, sticky=(tk.N+tk.E+tk.W+tk.S))
        self.grid_columnconfigure(0, weight=2)
        self.grid_rowconfigure(0, weight=1)

        self.textRawText = tk.Text(self)
        self.textRawText.grid(row=0, column=0, sticky=(tk.N+tk.E+tk.W+tk.S))
        self.textRawText.config(undo=True, wrap='word')
        self.scrollbar = tk.Scrollbar(self,command=self.textRawText.yview)
        self.scrollbar.grid(row=0, column=1, sticky=(tk.N+tk.E+tk.W+tk.S))
        self.textRawText["yscrollcommand"] = self.scrollbar.set

    def getText(self):
        return self.textRawText.get("1.0",tk.END).strip()