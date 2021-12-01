import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;


public class ClientViewInit extends JFrame{
	private Panel contentPane;
	private String UserName;

	
	public ClientViewInit() {
		makeContentPane();
	}
	
	private void makeContentPane() {
	
        JScrollPane scrollPane = new JScrollPane();
		JTextPane textArea = new JTextPane();

		scrollPane.setBounds(242, 467, 303, 95);
    	contentPane.add(scrollPane);
        scrollPane.setViewportView(textArea);
        textArea.setEditable(true);
        textArea.setFont(new Font("", Font.PLAIN, 14));	   
        
        JTextField txtInput = new JTextField();
        txtInput.setBounds(244, 566, 301, 21);
        txtInput.setColumns(10);
        contentPane.add(txtInput);
        
        JButton btnSend = new JButton("Send");
		btnSend.setBounds(549, 555, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);

		JButton imgBtn = new JButton("+");
        imgBtn.setBounds(176, 555, 50, 40);
        contentPane.add(imgBtn);
		
	}


	 	
	

	
}
