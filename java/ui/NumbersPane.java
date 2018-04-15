package ui;

import java.util.Formatter;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class NumbersPane extends JPanel implements WorkPanel{
	private JTextField	txtInit;
	private JTextField	txtEnd;
	private JTextField	txtNumbersXSheet;
	private JCheckBox	chkUseZero;
	private JLabel		lblInit;
	private JLabel		lblEnd;
	private JLabel		lblNumbersXSheet;
	private JLabel		lblUseZero;

	public NumbersPane(){
		super();

		setLayout(new GridLayout(4,2));
		lblInit = new JLabel("Inicio:");
		lblEnd = new JLabel("Fin:");
		lblNumbersXSheet = new JLabel("Numeros X Hoja:");
		lblUseZero = new JLabel("Agregar ceros:");
		txtInit = new JTextField(3);
		txtEnd = new JTextField(3);
		txtNumbersXSheet = new JTextField(3);
		chkUseZero = new JCheckBox();

		add(lblInit);
		add(txtInit);
		add(lblEnd);
		add(txtEnd);
		add(lblNumbersXSheet);
		add(txtNumbersXSheet);
		add(lblUseZero);
		add(chkUseZero);
	}

	@Override
	public String getText(){
		int init = Integer.parseInt(txtInit.getText());
		int end  = Integer.parseInt(txtEnd.getText());
		int nXs  = Integer.parseInt(txtNumbersXSheet.getText());
		int total = end - init + 1;
		int numSheets = total / nXs;
		if(total%nXs!=0)
			numSheets++;

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		for(int cell=1;cell<=nXs;cell++){
			formatter.format("c%d",cell);
			if(cell<nXs)
				sb.append(",");
		}

		sb.append("\n");
		String numberFormat = "%"+(chkUseZero.isSelected()?"0":"")+txtEnd.getText().length()+"d";

		for(int row=0;row<numSheets;row++){
			for(int cell=0;cell<nXs;cell++){
				formatter.format(numberFormat,(init+row)+(numSheets*cell));
				if(cell<nXs-1)
					sb.append(",");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}