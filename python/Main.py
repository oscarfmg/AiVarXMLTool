import Tkinter as tk

from ui.ConverterFrame import ConverterFrame
from converter.converter import DataConverter

if __name__ == "__main__":
    root = tk.Tk()

    converter = DataConverter()
    app = ConverterFrame(converter,root)
    root.protocol("WM_DELETE_WINDOW",app.quit)
    root.grid_rowconfigure(0,weight=1)
    root.grid_columnconfigure(0, weight=1)
    root.geometry("400x240")

    app.mainloop()

    root.destroy()
