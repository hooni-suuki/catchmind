
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLayeredPane;

public class StartView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel ViewPane;
	private JButton LoginBtn;
	static StartView frame = new StartView();
	private ImageIcon StartBtn = new ImageIcon("src/image/startbtn.jpg");
	
	private ImageIcon exbtn = new ImageIcon("src/image/exit.png");
	
	Image exitimg = exbtn.getImage();
	Image exitimage = exitimg.getScaledInstance(45, 30, Image.SCALE_SMOOTH);
	ImageIcon exiticon = new ImageIcon(exitimage);
	
	private Audio backAudio;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
					frame.setTitle("CatchMind");
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public StartView() {
		
		try {
			backAudio = new Audio("src/Audio/InitBGM.wav", true); //배경음악넣었움
			backAudio.soundStart();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "재생이 안돼..?");
			System.exit(0);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 891, 579);

		JPanel ViewPane = new JPanel() {
		public void paint(Graphics g) {
			Image MainScreen = new ImageIcon("src/image/mainimg.jpg").getImage();
			Dimension d = getSize();
			g.drawImage(MainScreen, 0, 0,d.width,d.height, null);
			setOpaque(false);
			super.paint(g);
		}
		};
		
		ViewPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(ViewPane);
		ViewPane.setLayout(null);
		
		//컴퍼넌트엮어놓은 패패널
		JPanel panel = new JPanel();
		panel.setBounds(109, 114, 704, 373);
		panel.setOpaque(false);
		ViewPane.add(panel);
		panel.setLayout(null);
		
		LoginBtn = new JButton(StartBtn);
		LoginBtn.setBounds(250, 292, 213, 71);
		LoginBtn.setContentAreaFilled(false);//踰꾪듉 �궡�슜 �븞梨꾩�
		LoginBtn.setOpaque(false);
		LoginBtn.setBorderPainted(false);
		LoginBtn.setContentAreaFilled(false);
		panel.add(LoginBtn);
		
		//종료
		JButton ExitBtn = new JButton(exiticon);
		ExitBtn.setContentAreaFilled(false);
		ExitBtn.setOpaque(false);
		ExitBtn.setBorderPainted(false);
		ExitBtn.setFocusPainted(false);
		ExitBtn.setBounds(789, 509, 76, 23);
		ExitBtn.setForeground(new Color(100, 209, 190));
		ExitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ViewPane.add(ExitBtn);
		
		Myaction action = new Myaction(); 
		LoginBtn.addActionListener(action); //로그인화면으로

	}
	

	public class Myaction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JavaGameClientMain login =new JavaGameClientMain();
			login.setVisible(true);//JavaGameClientMain 실행
			frame.dispose();//프레임없애고 넘겨줌
		}
	}
}
