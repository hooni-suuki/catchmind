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
	//JavaGameClientView view = new JavaGameClientView("username", "password", "ip_addr", "port_no");
	
	public void makeClientView() {
		setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 634);
        makeContentPane();
       // makeExitBtn("UserName");
        
        JavaGameClientView view = new JavaGameClientView("username", "password", "ip_addr", "port_no");
	}
	   
	private void makeContentPane() {
		JPanel contentPane = new JPanel() {
	         public void paint(Graphics g) {
	            Image MainScreen = new ImageIcon("src/image/mainimg.jpg").getImage();
	            Dimension d = getSize();
	            g.drawImage(MainScreen, 0, 0, d.width, d.height, null);
	            setOpaque(false);
	            super.paint(g);
	         }
	      };
	      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	      setContentPane(contentPane);
	      contentPane.setLayout(null);
	
        JScrollPane scrollPane = new JScrollPane();
		JTextPane textArea = new JTextPane();

		scrollPane.setBounds(242, 467, 303, 95);
    	contentPane.add(scrollPane);
        scrollPane.setViewportView(textArea);
        textArea.setEditable(true);
        textArea.setFont(new Font("援대┝泥 ", Font.PLAIN, 14));	   
        
        JTextField txtInput = new JTextField();
        txtInput.setBounds(244, 566, 301, 21);
        txtInput.setColumns(10);
        contentPane.add(txtInput);
        
        JButton btnSend = new JButton("Send");
		btnSend.setBounds(549, 555, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);
	}

	 	
	

	
}
