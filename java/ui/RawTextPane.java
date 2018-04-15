package ui;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RawTextPane extends JPanel implements WorkPanel{
	private JTextArea txtRawText;

	public RawTextPane(){
		super();

		txtRawText = new JTextArea(10,45);
		txtRawText.setEditable(true);
		add(txtRawText);
	}

	@Override
	public String getText(){
		return txtRawText.getText();
	}
}