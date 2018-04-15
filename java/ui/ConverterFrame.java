package ui;

import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import converter.Converter;

public class ConverterFrame extends JFrame implements ActionListener {
	private Converter converter;

	private JButton btnSave;
	private JTabbedPane tabbedPane;
	private GridBagLayout layout;

	private RawTextPane rawTextPanel;
	private NumbersPane numbersPanel;

	private ConverterFrame(){
		super("Ai Variable tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();

		c.fill=GridBagConstraints.BOTH;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		tabbedPane = new JTabbedPane();
		rawTextPanel = new RawTextPane();
		numbersPanel = new NumbersPane();
		tabbedPane.add(numbersPanel,"Series de numeros");
		tabbedPane.add(rawTextPanel,"Texto en crudo");
		add(tabbedPane,c);
		

		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=1;
		c.anchor=GridBagConstraints.PAGE_END;
		btnSave = new JButton("Guardar");
		btnSave.addActionListener(this);
		add(btnSave,c);

		pack();
	}

	public ConverterFrame(Converter converter){
		this();
		this.converter = converter;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==btnSave){

			JFileChooser fc = new JFileChooser();
			if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();

				converter.convert(((WorkPanel)tabbedPane.getSelectedComponent()).getText());
				if(!converter.save(file)){
					JOptionPane.showMessageDialog(this,"Error","El archivo no ha sido almacenado.",JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(this,"Exito","El archivo ha sido almacenado adecuadamente.",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	public static void main(String args[]){
		ConverterFrame mainFrame = new ConverterFrame();
		mainFrame.setVisible(true);
	}
}