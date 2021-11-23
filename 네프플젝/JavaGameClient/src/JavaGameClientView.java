
// JavaObjClientView.java ObjecStram 湲곕컲 Client
//�떎吏덉쟻�씤 梨꾪똿 李�
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JRadioButton;

 

public class JavaGameClientView extends JFrame {
	/**
	 * 
	 */
	
	private ArrayList<String> UserList = new ArrayList<String>();  

	private static final long serialVersionUID = 1L;
	

	
	private JTextField txtInput;

	private JPanel contentPane;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	// private JTextArea textArea;
	private JTextPane textArea;
	private JButton draw, newDraw;  //"그리기"버튼과 "모두 지우기"버튼

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;
	
	//유저 이름 
	private JLabel user_label_01;
	private JLabel user_label_02;
	private JLabel user_label_03;
	private JLabel user_label_04;

	//그리기관련 툴박스
	private JPanel toolBox;
	private JRadioButton redButton;
	private JRadioButton greenButton;
	private JRadioButton blueButton;
	private JRadioButton yellowButton;
	
	JPanel panel;
	private JLabel lblMouseEvent;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null; 
	private Graphics gc2 = null;
	private Audio backAudio;
	


	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientView(String username, String password, String ip_addr, String port_no)  {
		
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 634);
		//-------------------------------------------------	
		//메인 패널 배경이미지 들어있음
		JPanel contentPane = new JPanel() {
			public void paint(Graphics g) {
				Image MainScreen = new ImageIcon("src/image/mainimg.jpeg").getImage();
				Dimension d = getSize();
				g.drawImage(MainScreen, 0, 0,d.width,d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//-------------------------------------------------
		//채팅창 
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(242, 455, 303, 107);
		contentPane.add(scrollPane);
		
		textArea = new JTextPane();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);
		textArea.setFont(new Font("援대┝泥�", Font.PLAIN, 14));
		
		//-------------------------------------------------	

		btnSend = new JButton("Send");
		btnSend.setFont(new Font("援대┝", Font.PLAIN, 14));
		btnSend.setBounds(549, 555, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		imgBtn = new JButton("+");
		imgBtn.setFont(new Font("援대┝", Font.PLAIN, 16));
		imgBtn.setBounds(176, 555, 50, 40);
		contentPane.add(imgBtn);

		JButton btnNewButton = new JButton("EXIT");
		btnNewButton.setFont(new Font("援대┝", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(703, 555, 69, 40);
		contentPane.add(btnNewButton);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(176, 32, 442, 381);
		contentPane.add(panel);
		gc = panel.getGraphics();
		
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0,0, panel.getWidth(),  panel.getHeight());
		gc2.setColor(Color.BLACK);
		gc2.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);
		
		lblMouseEvent = new JLabel("<dynamic>");
		lblMouseEvent.setHorizontalAlignment(SwingConstants.CENTER);
		lblMouseEvent.setFont(new Font("援대┝", Font.BOLD, 14));
		lblMouseEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblMouseEvent.setBackground(Color.WHITE);
		lblMouseEvent.setBounds(176, 505, 34, 40);
		contentPane.add(lblMouseEvent);
		
		//-------------------------------------------------	
		user_label_01 = new JLabel();
		user_label_01.setBounds(22, 171, 135, 15);
		contentPane.add(user_label_01);
		user_label_01.setText(username);

		user_label_02 = new JLabel();
		user_label_02.setBounds(22, 373, 135, 15);
		contentPane.add(user_label_02);
		user_label_02.setText(username);

		user_label_03 = new JLabel();
		user_label_03.setBounds(638, 171, 135, 15);
		contentPane.add(user_label_03);
		
		user_label_04 = new JLabel();
		user_label_04.setBounds(638, 373, 135, 15);
		contentPane.add(user_label_04);
		JPanel user_panel_01 = new JPanel();
		user_panel_01.setBounds(22, 32, 135, 125);
		contentPane.add(user_panel_01);
		
		JPanel user_panel_03 = new JPanel();
		user_panel_03.setBounds(638, 32, 135, 125);
		contentPane.add(user_panel_03);
		
		JPanel user_panel_02 = new JPanel();
		user_panel_02.setBounds(22, 238, 135, 125);
		contentPane.add(user_panel_02);
		
		JPanel user_panel_04 = new JPanel();
		user_panel_04.setBounds(638, 238, 135, 125);
		contentPane.add(user_panel_04);
	//-------------------------------------------------	
		
		toolBox = new JPanel();
		toolBox.setBounds(176, 419, 442, 40);
		contentPane.add(toolBox);
		toolBox.setLayout(null);
		JButton [] setbtn = new JButton[4];
		setbtn[0] = new JButton("지우기");
		redButton = new JRadioButton("red");
		redButton.setBounds(8, 6, 61, 23);
		toolBox.add(redButton);

		greenButton = new JRadioButton("green");
		greenButton.setBounds(203, 6, 61, 23);
		toolBox.add(greenButton);
		
		blueButton = new JRadioButton("blue");
		blueButton.setBounds(73, 6, 61, 23);
		toolBox.add(blueButton);
		
		yellowButton = new JRadioButton("yellow");
		yellowButton.setBounds(136, 6, 61, 23);
		toolBox.add(yellowButton);
		
		ButtonGroup  group = new ButtonGroup();
		group.add(blueButton);
		group.add(redButton);
		group.add(greenButton);
		group.add(yellowButton);
		//-------------------------------------------------	

		
				txtInput = new JTextField();
				txtInput.setBounds(244, 566, 301, 21);
				contentPane.add(txtInput);
				txtInput.setColumns(10);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			imgBtn.addActionListener(action2);
			MyMouseEvent mouse = new MyMouseEvent();
			panel.addMouseMotionListener(mouse);
			panel.addMouseListener(mouse);
			MyMouseWheelEvent wheel = new MyMouseWheelEvent();
			panel.addMouseWheelListener(wheel);
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	public void paint(Graphics g) {
		super.paint(g);
		// Image �쁺�뿭�씠 媛��젮議뚮떎 �떎�떆 �굹���궇 �븣 洹몃젮以��떎.
		gc.drawImage(panelImage, 0, 0, this);
	}
	
	// Server Message瑜� �닔�떊�빐�꽌 �솕硫댁뿉 �몴�떆
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						if (cm.UserName.equals(UserName))
							AppendTextR(msg); // �궡 硫붿꽭吏��뒗 �슦痢≪뿉
						else
							AppendText(msg);
						break;
					case "300": // Image 泥⑤�
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						break;
					case "500": // Mouse Event �닔�떊
						DoMouseEvent(cm);
						break;
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch臾� �걹
				} // 諛붽묑 catch臾몃걹

			}
		}
	}

	// Mouse Event �닔�떊 泥섎━
	public void DoMouseEvent(ChatMsg cm) {
		
		gc2.fillOval(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);
		gc.drawImage(panelImage, 0, 0, panel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(UserName, "500", "MOUSE");
		cm.mouse_e = e;
		cm.pen_size = pen_size;
		SendObject(cm);
	}

	class MyMouseWheelEvent implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if (e.getWheelRotation() < 0) { // �쐞濡� �삱由щ뒗 寃쎌슦 pen_size 利앷�
				if (pen_size < 20)
					pen_size++;
			} else {
				if (pen_size > 2)
					pen_size--;
			}
			lblMouseEvent.setText("mouseWheelMoved Rotation=" + e.getWheelRotation() 
				+ " pen_size = " + pen_size + " " + e.getX() + "," + e.getY());

		}
		
	}
	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," + e.getY());// 醫뚰몴異쒕젰媛��뒫
			gc2.setColor(Color.black);
			
			if(redButton.isSelected()) {
				gc2.setColor(Color.red);
				
			}else if(yellowButton.isSelected()) {
				gc2.setColor(Color.yellow);
			}
			else if(blueButton.isSelected()) {
				gc2.setColor(Color.blue);
			}else if(greenButton.isSelected()) {
				gc2.setColor(Color.green);
			}
				
			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
			// panelImnage�뒗 paint()�뿉�꽌 �씠�슜�븳�떎.
			gc.drawImage(panelImage, 0, 0, panel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," + e.getY());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," + e.getY());
			Color c = new Color(0,0,255);
			gc2.setColor(c);
			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
			gc.drawImage(panelImage, 0, 0, panel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseEntered " + e.getX() + "," + e.getY());
			// panel.setBackground(Color.YELLOW);

		}

		@Override
		public void mouseExited(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseExited " + e.getX() + "," + e.getY());
			// panel.setBackground(Color.CYAN);

		}

		@Override
		public void mousePressed(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," + e.getY());

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," + e.getY());
			// �뱶�옒洹몄쨷 硫덉텧�떆 蹂댁엫

		}
	}
	
	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				// frame.setVisible(true);
				// fd.setDirectory(".\\");
				fd.setVisible(true);
				// System.out.println(fd.getDirectory() + fd.getFile());
				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.img = img;
					SendObject(obcm);
				}
			}
		}
	}



	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		//textArea.setCaretPosition(len);
		//textArea.replaceSelection(msg + "\n");
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");

	}
	
	// 화면 우측에 출력
	public void AppendTextR(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.	
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);	
	    doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(),msg+"\n", right );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");

	}
	
	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else {
			textArea.insertIcon(ori_icon);
			new_img = ori_img;
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
		// ImageViewAction viewaction = new ImageViewAction();
		// new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
		// panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

		gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
		gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}
