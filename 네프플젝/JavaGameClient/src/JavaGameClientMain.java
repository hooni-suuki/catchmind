// JavaObjClient.java
// ObjecStream �궗�슜�븯�뒗 梨꾪똿 Client

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import dao.UserDao;
import model.User;
import mybatis.Mybatis_User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class JavaGameClientMain extends JFrame {
	
	UserDao userDao = new UserDao(Mybatis_User.getSqlSessionFactory());
	User user = new User();
	
	private static final long serialVersionUID = 1L;
	private JTextField txtUserName;
	private JTextField txtPassword;
	private JFrame frame;

	private ImageIcon StartBtn = new ImageIcon("src/image/startbtn.jpg");
	private ImageIcon exbtn = new ImageIcon("src/image/exit.png");
	
	Image exitimg = exbtn.getImage();
	Image exitimage = exitimg.getScaledInstance(50, 30, Image.SCALE_SMOOTH);
	ImageIcon exiticon = new ImageIcon(exitimage);
	

	public JavaGameClientMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 891, 579);
		setResizable(false);
		setLocationRelativeTo(null);
		
		JPanel contentPane = new JPanel() {
			public void paint(Graphics g) {
				Image MainScreen = new ImageIcon("src/image/mainimg.jpg").getImage();
				Dimension d = getSize();
				g.drawImage(MainScreen, 0, 0,d.width,d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel ComponentPanel = new JPanel(); // attached all componenet 
		ComponentPanel.setBounds(109, 114, 704, 373);
		contentPane.add(ComponentPanel);
		ComponentPanel.setOpaque(false);
		ComponentPanel.setLayout(null);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(311, 189, 204, 32);
		ComponentPanel.add(txtUserName);
		txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
		txtUserName.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(311, 247, 204, 33);
		ComponentPanel.add(txtPassword);
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setColumns(10);
		
		JLabel lbUsername = new JLabel("User Name");
		lbUsername.setBounds(202, 188, 82, 33);
		ComponentPanel.add(lbUsername);
		
		JLabel lblIpAddress = new JLabel("Password");
		lblIpAddress.setBounds(217, 247, 82, 33);
		ComponentPanel.add(lblIpAddress);
		
		JButton btnConnect = new JButton(StartBtn);
		btnConnect.setContentAreaFilled(false);//踰꾪듉 �궡�슜 �븞梨꾩�
		btnConnect.setOpaque(false);
		btnConnect.setBorderPainted(false);
		btnConnect.setContentAreaFilled(false);
		btnConnect.setBounds(250, 292, 213, 71);
		ComponentPanel.add(btnConnect);
		
		JButton exitBtn = new JButton(exiticon);
		exitBtn.setBounds(759, 494, 106, 38);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setOpaque(false);
		exitBtn.setBorderPainted(false);
		exitBtn.setFocusPainted(false);
		exitBtn.setForeground(new Color(100, 209, 190));
		contentPane.add(exitBtn);
		
		exitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		Myaction action = new Myaction();
		
		btnConnect.addActionListener(action); //connect
	}
	
	
	class Myaction implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = txtUserName.getText().trim();
			String password = txtPassword.getText().trim();
			String ip_addr = "127.0.0.1";
			String port_no = "30000";
			
			User user = userDao.selectByID(username);
			try {
				if(!user.getId().equals(username)) {
					JOptionPane.showMessageDialog(null, "계정이 틀렸습니다..");
				}
				else if(!user.getPassword().equals(password)){
					JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.");
				}
				else {
					JOptionPane.showMessageDialog(null, "로그인~!~!~!");
					JavaGameClientView view = new JavaGameClientView(username, password, ip_addr, port_no);
					setVisible(false);
				}
			} catch (Exception e2) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "아이디가 없습니다.");
			}
			
		}
	}
}


