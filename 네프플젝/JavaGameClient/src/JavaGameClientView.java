
// JavaObjClientView.java ObjecStram 湲곕컲 Client
// 떎吏덉쟻 씤 梨꾪똿 李 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;

public class JavaGameClientView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField txtInput;

	private JPanel contentPane;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JTextPane textArea;
	private Frame frame;
	private FileDialog fd;
	public JButton startbtn, rectanglebtn, circlebtn, trianglebtn, freebtn;

	private int ox, oy, nx, ny;
	// 유저 이름
	private JLabel user_label_01;
	private JLabel user_label_02;
	private JLabel user_label_03;
	private JLabel user_label_04;

	// 그리기관련 툴박스
	private JPanel toolBox;
	public JButton bluebtn, redbtn, yellowbtn, blackbtn, greenbtn, eraserbtn, clearbtn;

	
	private ImageIcon red = new ImageIcon("src/image/red.png");
	private ImageIcon blue = new ImageIcon("src/image/blue.png");
	private ImageIcon green= new ImageIcon("src/image/green.png");
	private ImageIcon black = new ImageIcon("src/image/black.png");
	private ImageIcon yellow = new ImageIcon("src/image/yellow.png");
	private ImageIcon viewclear = new ImageIcon("src/image/clear.png");
	private ImageIcon eraser = new ImageIcon("src/image/eraser.png");
	
	private ImageIcon sendbtn = new ImageIcon("src/image/send.png");
	private ImageIcon stbtn = new ImageIcon("src/image/start.png");
	private ImageIcon exbtn = new ImageIcon("src/image/exit.png");
	
	Image exitimg = exbtn.getImage();
	Image exitimage = exitimg.getScaledInstance(50, 30, Image.SCALE_SMOOTH);
	ImageIcon exiticon = new ImageIcon(exitimage);
	
	Image sendimg = sendbtn.getImage();
	Image sendimage = sendimg.getScaledInstance(50, 30, Image.SCALE_SMOOTH);
	ImageIcon sendicon = new ImageIcon(sendimage);
	
	Image startimg = stbtn.getImage();
	Image startimage = startimg.getScaledInstance(60, 45, Image.SCALE_SMOOTH);
	ImageIcon starticon = new ImageIcon(startimage);
	


	private Graphics gc;
	private Graphics gc2 = null;
	private Color color;
	JPanel panel;
	private JLabel lblMouseEvent;
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null;
	public int stroke = 3;
	JButton pen_size_plus, pen_size_minus;
	private JLabel lblNewLabel;
	Point start = null, end = null;
	String fg = "free";
	Figure figure = new Figure();
	GameInfo gInfo = new GameInfo();

	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public JavaGameClientView(String username, String password, String ip_addr, String port_no) {

		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 634);
		// -------------------------------------------------
		// 메인 패널 배경이미지 들어있음
		JPanel contentPane = new JPanel() {
			public void paint(Graphics g) {
				Image MainScreen = new ImageIcon("src/image/bacimg.png").getImage();
				Dimension d = getSize();
				g.drawImage(MainScreen, 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		// -------------------------------------------------


		JPanel user_panel_01 = new JPanel() {
			public void paint(Graphics g) {
				Image user1 = new ImageIcon("src/image/egg.jpg").getImage();
				Dimension d = getSize();
				g.drawImage(user1, 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};

		JPanel user_panel_02 = new JPanel() {
			public void paint(Graphics g) {
				Image user1 = new ImageIcon("src/image/mushroom.jpg").getImage();
				Dimension d = getSize();
				g.drawImage(user1, 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};

		JPanel user_panel_03 = new JPanel() {
			public void paint(Graphics g) {
				Image user1 = new ImageIcon("src/image/pumpkin.jpg").getImage();
				Dimension d = getSize();
				g.drawImage(user1, 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};

		JPanel user_panel_04 = new JPanel() {
			public void paint(Graphics g) {
				Image user1 = new ImageIcon("src/image/rockrice.jpg").getImage();
				Dimension d = getSize();
				g.drawImage(user1, 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paint(g);
			}
		};

		// 유저패널
		user_panel_01.setBounds(22, 32, 135, 125);
		user_panel_01.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		contentPane.add(user_panel_01);

		user_panel_03.setBounds(638, 32, 135, 125);
		user_panel_03.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		contentPane.add(user_panel_03);

		user_panel_02.setBounds(22, 238, 135, 125);
		user_panel_02.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		contentPane.add(user_panel_02);

		user_panel_04.setBounds(638, 238, 135, 125);
		user_panel_04.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		contentPane.add(user_panel_04);

		// -------------------------------------------------

		// 채팅창
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(242, 467, 303, 95);
		scrollPane.setBorder(new TitledBorder(new LineBorder(color.black, 2)));

		contentPane.add(scrollPane);

		textArea = new JTextPane();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);

		txtInput = new JTextField();
		txtInput.setBounds(244, 566, 301, 21);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		// -------------------------------------------------

		btnSend = new JButton(sendicon);
		btnSend.setBorderPainted(false);
		btnSend.setFocusPainted(false);
		btnSend.setContentAreaFilled(false);
		btnSend.setOpaque(false);
		btnSend.setForeground(new Color(100, 209, 190));
		btnSend.setBounds(549, 555, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;

		JButton EXITBtn = new JButton(exiticon);
		EXITBtn.setContentAreaFilled(false);
		EXITBtn.setOpaque(false);
		EXITBtn.setBorderPainted(false);
		EXITBtn.setFocusPainted(false);
		EXITBtn.setForeground(new Color(100, 209, 190));
		EXITBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye", gInfo.getGameStatus(), gInfo.getGameUserName());
				SendObject(msg);
				System.exit(0);
			}
		});
		EXITBtn.setBounds(703, 555, 69, 40);
		contentPane.add(EXITBtn);

		panel = new JPanel(); // 캔버스 붙어있음
		panel.setBorder(new LineBorder(new Color(0, 0, 255), 1, true));
		panel.setBackground(Color.WHITE);
		panel.setBounds(176, 32, 442, 342);
		contentPane.add(panel);
		panel.setLayout(null);
		gc = panel.getGraphics();

		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(373, 317, 57, 15);
		panel.add(lblNewLabel); // ㅇㅁㅇ?

		// ==============================================================
		// 그림이 그려지는 패널
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = (Graphics2D) panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		gc2.setColor(Color.blue);
		gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);

		lblMouseEvent = new JLabel("<dynamic>");
		lblMouseEvent.setHorizontalAlignment(SwingConstants.CENTER);
		lblMouseEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblMouseEvent.setBackground(Color.WHITE);
		lblMouseEvent.setBounds(22, 505, 208, 40);
		contentPane.add(lblMouseEvent);

		// -------------------------------------------------
		user_label_01 = new JLabel();
		user_label_01.setBounds(22, 171, 135, 15);
		contentPane.add(user_label_01);
		user_label_01.setText("a");

		user_label_02 = new JLabel();
		user_label_02.setBounds(22, 373, 135, 15);
		contentPane.add(user_label_02);
		user_label_02.setText("b");

		user_label_03 = new JLabel();
		user_label_03.setBounds(638, 171, 135, 15);
		contentPane.add(user_label_03);
		user_label_03.setText("c");
		
		user_label_04 = new JLabel();
		user_label_04.setBounds(638, 373, 135, 15);
		contentPane.add(user_label_04);
		user_label_04.setText("d");
		// --------------------------------------------------------------

//====================================================== 색 버튼, 모양 버튼 모임
		toolBox = new JPanel();
		toolBox.setBounds(176, 393, 442, 64);
		toolBox.setBackground(new Color(255, 0, 0, 0));
		contentPane.add(toolBox);
		toolBox.setLayout(null);

		redbtn = new JButton(red);
		redbtn.setBorderPainted(false);
		redbtn.setBackground(new Color(100,209,190));
		redbtn.setFocusPainted(false);
		redbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("red");
			}
		});
		redbtn.setBounds(8, 8, 50, 23);
		toolBox.add(redbtn);

		bluebtn = new JButton(blue);
		bluebtn.setBackground(new Color(100,209,190));
		bluebtn.setBorderPainted(false);
		bluebtn.setFocusPainted(false);
		bluebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("blue");
			}
		});
		bluebtn.setBounds(63, 8, 50, 23);
		toolBox.add(bluebtn);

		yellowbtn = new JButton(yellow);
		yellowbtn.setBorderPainted(false);
		yellowbtn.setFocusPainted(false);
		yellowbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("yellow");
			}
		});
		yellowbtn.setBounds(8, 33, 50, 23);
		yellowbtn.setBackground(new Color(100,209,190));
		toolBox.add(yellowbtn);

		blackbtn = new JButton(black);
		blackbtn.setBorderPainted(false);
		blackbtn.setFocusPainted(false);
		blackbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("black");
			}
		});
		blackbtn.setBounds(63, 33, 50, 23);
		blackbtn.setBackground(new Color(100,209,190));
		toolBox.add(blackbtn);

		greenbtn = new JButton(green);
		greenbtn.setBorderPainted(false);
		greenbtn.setFocusPainted(false);
		greenbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("green");
			}
		});
		greenbtn.setBackground(new Color(100,209,190));
		greenbtn.setBounds(118, 33, 50, 23);
		toolBox.add(greenbtn);

		eraserbtn = new JButton(eraser);
		eraserbtn.setBorderPainted(false);
		eraserbtn.setFocusPainted(false);
		eraserbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("white");
			}
		});
		eraserbtn.setBackground(new Color(100,209,190));
		eraserbtn.setBounds(118, 8, 50, 23);
		toolBox.add(eraserbtn);

		clearbtn = new JButton(viewclear);
		clearbtn.setFocusPainted(false);
		clearbtn.setBorderPainted(false);
		clearbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = "clear";
				clear(str);
			}
		});
		clearbtn.setBackground(new Color(100,209,190));
		clearbtn.setBounds(198, 8, 50, 23);
		toolBox.add(clearbtn);
		
//========================================================================시작버튼
		startbtn = new JButton(starticon);
		startbtn.setBounds(703, 398, 63, 59);
		startbtn.setBorderPainted(false);
		startbtn.setContentAreaFilled(false);
		startbtn.setFocusPainted(false);
		startbtn.setOpaque(false);
		startbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gInfo.setGameStatus("t");
				gInfo.setGameUserName(UserName);
				ChatMsg cm = new ChatMsg(UserName, "800", figure.getFigure(), gInfo.getGameStatus(),
						gInfo.getGameUserName());
				SendObject(cm);
			}
		});
		contentPane.add(startbtn);
//========================================================================도형

		circlebtn = new JButton("○");
		circlebtn.setFont(new Font("굴림", Font.BOLD, 15));
		circlebtn.setFocusPainted(false);
		circlebtn.setBorderPainted(false);
		circlebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("circle");
			}
		});
		circlebtn.setBounds(286, 8, 50, 23);
		circlebtn.setBackground(new Color(100,209,190));
		toolBox.add(circlebtn);

		rectanglebtn = new JButton("□");
		rectanglebtn.setFont(new Font("굴림", Font.BOLD, 15));
		rectanglebtn.setFocusPainted(false);
		rectanglebtn.setBorderPainted(false);
		rectanglebtn.setBackground(new Color(100,209,190));
		rectanglebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("square");
			}
		});
		rectanglebtn.setBounds(286, 33, 50, 23);
		toolBox.add(rectanglebtn);

		trianglebtn = new JButton("/");
		trianglebtn.setFont(new Font("굴림", Font.BOLD, 15));
		trianglebtn.setFocusPainted(false);
		trianglebtn.setBorderPainted(false);
		trianglebtn.setBackground(new Color(100,209,190));
		trianglebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("line");
			}
		});
		trianglebtn.setBounds(348, 8, 50, 23);
		toolBox.add(trianglebtn);

		freebtn = new JButton("~");
		freebtn.setFont(new Font("굴림", Font.BOLD, 15));
		freebtn.setFocusPainted(false);
		freebtn.setBorderPainted(false);
		freebtn.setBackground(new Color(100,209,190));
		freebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("free");
			}
		});
		freebtn.setBounds(348, 33, 50, 23);
		toolBox.add(freebtn);

		pen_size_plus = new JButton("+");
		pen_size_plus.setFocusPainted(false);
		pen_size_plus.setBorderPainted(false);
		pen_size_plus.setBackground(new Color(100,209,190));
		pen_size_plus.setFont(new Font("굴림", Font.BOLD, 15));
		pen_size_plus.setBounds(180, 33, 45, 23);
		pen_size_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "900", "stroke_szie++", gInfo.getGameStatus(),
						gInfo.getGameUserName());
				SendObject(cm);
			}
		});
		toolBox.add(pen_size_plus);

		pen_size_minus = new JButton("ㅡ");
		pen_size_minus.setFocusPainted(false);
		pen_size_minus.setBorderPainted(false);
		pen_size_minus.setFont(new Font("굴림", Font.BOLD, 11));
		pen_size_minus.setBackground(new Color(100,209,190));
		pen_size_minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "900", "stroke_szie--", gInfo.getGameStatus(),
						gInfo.getGameUserName());
				SendObject(cm);
			}
		});
		pen_size_minus.setBounds(226, 33, 45, 23);
		toolBox.add(pen_size_minus);

		// -------------------------------------------------

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello", gInfo.getGameStatus(), gInfo.getGameUserName());
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			bluebtn.addActionListener(action);
			redbtn.addActionListener(action);
			yellowbtn.addActionListener(action);
			blackbtn.addActionListener(action);
			greenbtn.addActionListener(action);
			clearbtn.addActionListener(action);
			eraserbtn.addActionListener(action);
			startbtn.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			MyMouseEvent mouse = new MyMouseEvent();
			panel.addMouseMotionListener(mouse);
			panel.addMouseListener(mouse);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	public void paint(Graphics g) {
		super.paint(g);
		gc.drawImage(panelImage, 0, 0, this);
	}

	// Server Message瑜 닔 떊 빐 꽌 솕硫댁뿉 몴 떆
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
						msg = String.format("[%s]\n%s", cm.UserName, cm.data, cm.gStatus, cm.gGameUserName);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						if (cm.UserName.equals(UserName)) {
							AppendTextR(msg); // 궡 硫붿꽭吏 뒗 슦痢≪뿉
						} else {
							AppendText(msg);
						}
						break;
					case "300": // Image 泥⑤
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						break;
					case "500": // Mouse Event 닔 떊
						DoMouseEvent(cm);
						break;
					case "600":
						setColor(cm.data);
						break;
					case "700":
						clear();
						break;
					case "800":
						if (cm.gGameUserName.equals(UserName)) {
							setgInfo(cm);
							clear("clear");
							setStartDisable(cm);
							btnBooleanT(cm);
						} else {
							setgInfo(cm);
							setStartDisable(cm);
							btnBooleanF(cm);
							clear("clear");
						}
						break;
					case "900":
						if (cm.data.equals("stroke_szie++"))
							if (stroke < 12)
								((Graphics2D) gc2).setStroke(new BasicStroke(stroke++));
						if (cm.data.equals("stroke_szie--")) {
							if (stroke > 3)
								((Graphics2D) gc2).setStroke(new BasicStroke(stroke--));
						}
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} 
				} 

			}
		}
	}

	public void setStartDisable(ChatMsg cm) {
		if (cm.gStatus.equals("t")) {
			startbtn.setEnabled(false);
		} else {
			startbtn.setEnabled(true);
		}
	}

	public void btnBooleanF(ChatMsg cm) {
		if (cm.gStatus.equals("t")) {
			startbtn.setEnabled(false);
			eraserbtn.setEnabled(false);
			redbtn.setEnabled(false);
			blackbtn.setEnabled(false);
			bluebtn.setEnabled(false);
			yellowbtn.setEnabled(false);
			greenbtn.setEnabled(false);
			clearbtn.setEnabled(false);
			pen_size_plus.setEnabled(false);
			pen_size_minus.setEnabled(false);
			circlebtn.setEnabled(false);
			rectanglebtn.setEnabled(false);
			trianglebtn.setEnabled(false);
			freebtn.setEnabled(false);
		} else {
			btnBooleanT(cm);
		}
	}

	public void btnBooleanT(ChatMsg cm) {
		eraserbtn.setEnabled(true);
		redbtn.setEnabled(true);
		blackbtn.setEnabled(true);
		bluebtn.setEnabled(true);
		yellowbtn.setEnabled(true);
		greenbtn.setEnabled(true);
		clearbtn.setEnabled(true);
		pen_size_plus.setEnabled(true);
		pen_size_minus.setEnabled(true);
		circlebtn.setEnabled(true);
		rectanglebtn.setEnabled(true);
		trianglebtn.setEnabled(true);
		freebtn.setEnabled(true);
	}

	public void clear(String str) {
		if (str.equalsIgnoreCase("clear")) {
			Graphics gc = panel.getGraphics();
			gc.clearRect(0, 0, panel.getWidth(), panel.getHeight());
			panel.repaint();
			panelImage = createImage(panel.getWidth(), panel.getHeight());
			gc2 = panelImage.getGraphics();
			gc2.setColor(panel.getBackground());
			gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
			gc2.setColor(Color.blue);
			gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);

			ChatMsg cm = new ChatMsg(UserName, "700", "clear", gInfo.getGameStatus(), gInfo.getGameUserName());
			SendObject(cm);
		}
	}

	public void Start() {
		ChatMsg cm = new ChatMsg(UserName, "800", "gameStart", gInfo.getGameStatus(), gInfo.getGameUserName());
		SendObject(cm);
	}

	public void setgInfo(ChatMsg cm) {
		gInfo.setGameStatus(cm.gStatus);
		gInfo.setGameUserName(cm.gGameUserName);
	}

	public void clear() {
		Graphics gc = panel.getGraphics();
		gc.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		panel.repaint();
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		gc2.setColor(Color.blue);
		gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);
	}

	public void getBtColor(String color) {
		if (color.equals("red")) {
			gc2.setColor(Color.red);
		} else if (color.equals("blue")) {
			gc2.setColor(Color.blue);
		} else if (color.equals("yellow")) {
			gc2.setColor(Color.yellow);
		} else if (color.equals("black")) {
			gc2.setColor(Color.black);
		} else if (color.equals("green")) {
			gc2.setColor(Color.green);
		} else if (color.equals("white")) {
			gc2.setColor(Color.white);
		}
		ChatMsg cm = new ChatMsg(UserName, "600", color, gInfo.getGameStatus(), gInfo.getGameUserName());
		SendObject(cm);
	}

	public void setColor(String color) {
		if (color.equals("red")) {
			gc2.setColor(Color.red);
		} else if (color.equals("blue")) {
			gc2.setColor(Color.blue);
		} else if (color.equals("yellow")) {
			gc2.setColor(Color.yellow);
		} else if (color.equals("black")) {
			gc2.setColor(Color.black);
		} else if (color.equals("green")) {
			gc2.setColor(Color.green);
		} else if (color.equals("white")) {
			gc2.setColor(Color.white);
		}
	}

//=============================================== 
	// Mouse Event 수신 처리
	public void DoMouseEvent(ChatMsg cm) {
		if (cm.UserName.matches(UserName)) 
			return;
		((Graphics2D) gc2).setStroke(new BasicStroke(stroke));
		if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
			ox = cm.mouse_e.getX();
			oy = cm.mouse_e.getY();
		}
		nx = cm.mouse_e.getX();
		ny = cm.mouse_e.getY();
		int x = Math.min(ox, nx);
		int y = Math.min(oy, ny);
		int width = Math.abs(ox - nx);
		int height = Math.abs(oy - ny);

		if (cm.data.equals("circle")) {
			gc2.drawOval(x, y, width, height);
		} else if (cm.data.equals("square")) {
			gc2.drawRect(x, y, width, height);
		} else if (cm.data.equals("line")) {
			gc2.drawLine(ox, oy, nx, ny);
		} else if (cm.data.equals("free")) {
			gc2.drawLine(ox, oy, nx, ny);
			ox = nx;
			oy = ny;
		}
		gc.drawImage(panelImage, 0, 0, panel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(UserName, "500", figure.getFigure(), gInfo.getGameStatus(), gInfo.getGameUserName());
		cm.mouse_e = e;
		SendObject(cm);
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		if (start == null) // 타원이 만들어지지 않았음
			return;
		g.setColor(Color.BLUE); // 파란색 선택
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		g.drawOval(x, y, width, height); // 타원 그리기
	}

	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			System.out.println("start = " + start);
			lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," + e.getY());
			((Graphics2D) gc2).setStroke(new BasicStroke(stroke));
			String fg = figure.getFigure();
			nx = e.getX();
			ny = e.getY();
			int x = Math.min(ox, nx);
			int y = Math.min(oy, ny);
			int width = Math.abs(ox - nx);
			int height = Math.abs(oy - ny);

			if (gInfo.getGameStatus().equals("t")) {
				if (gInfo.getGameUserName().equals(UserName)) {
					if (fg.equals("free")) {
						gc2.drawLine(ox, oy, nx, ny);
						ox = nx;
						oy = ny;
						// panelImnage는 paint()에서 이용한다.
						gc.drawImage(panelImage, 0, 0, panel);
						SendMouseEvent(e);
						// panelImnage는 paint()에서 이용한다.
					} else if (fg.equals("square")) {
						gc.setColor(gc2.getColor());
						gc.drawImage(panelImage, 0, 0, panel);
						gc.drawRect(x, y, width, height);
					} else if (fg.equals("circle")) {
						gc.setColor(gc2.getColor());
						gc.drawImage(panelImage, 0, 0, panel);
						gc.drawOval(x, y, width, height);
					} else if (fg.equals("line")) {
						gc.setColor(gc2.getColor());
						gc.drawImage(panelImage, 0, 0, panel);
						gc.drawLine(ox, oy, nx, ny);
					}
				} else {
					return;
				}
			} else {
				if (fg.equals("free")) {
					nx = e.getX();
					ny = e.getY();
					gc2.drawLine(ox, oy, nx, ny);
					ox = nx;
					oy = ny;
					// panelImnage는 paint()에서 이용한다.
					gc.drawImage(panelImage, 0, 0, panel);
					SendMouseEvent(e);
					// panelImnage는 paint()에서 이용한다.
				} else if (fg.equals("square")) {
					gc.setColor(gc2.getColor());
					gc.drawImage(panelImage, 0, 0, panel);
					gc.drawRect(x, y, width, height);
				} else if (fg.equals("circle")) {
					gc.setColor(gc2.getColor());
					gc.drawImage(panelImage, 0, 0, panel);
					gc.drawOval(x, y, width, height);
				} else if (fg.equals("line")) {
					gc.setColor(gc2.getColor());
					gc.drawImage(panelImage, 0, 0, panel);
					gc.drawLine(ox, oy, nx, ny);

				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," + e.getY());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," + e.getY());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseEntered " + e.getX() + "," + e.getY());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseExited " + e.getX() + "," + e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			start = e.getPoint();
			System.out.println("start = " + start);
			ox = e.getX();
			oy = e.getY();
			lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," + e.getY());
			if (gInfo.getGameStatus().equals("t")) {
				if (gInfo.getGameUserName().equals(UserName)) {
				} else {
					return;
				}
			} else {
			}
			SendMouseEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," + e.getY());
			nx = e.getX();
			ny = e.getY();
			((Graphics2D) gc2).setStroke(new BasicStroke(stroke));

			int x = Math.min(ox, nx);
			int y = Math.min(oy, ny);
			int width = Math.abs(ox - nx);
			int height = Math.abs(oy - ny);
			String fg = figure.getFigure();

			if (gInfo.getGameStatus().equals("t")) {
				if (gInfo.getGameUserName().equals(UserName)) {
					if (!fg.equals("free")) {
						if (fg.equals("circle")) {
							gc2.drawOval(x, y, width, height);
						} else if (fg.equals("square")) {
							gc2.drawRect(x, y, width, height);
						} else if (fg.equals("line")) {
							gc2.drawLine(ox, oy, nx, ny);
						}
						gc2.drawImage(panelImage, 0, 0, panel);
						repaint();
					}
				} else {
					return;
				}
			} else {
				if (!fg.equals("free")) {
					if (fg.equals("circle")) {
						gc2.drawOval(x, y, width, height);
					} else if (fg.equals("square")) {
						gc2.drawRect(x, y, width, height);
					} else if (fg.equals("line")) {
						gc2.drawLine(ox, oy, nx, ny);
					}
					gc2.drawImage(panelImage, 0, 0, panel);
					repaint();
				}
			}
			SendMouseEvent(e);
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

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.

		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", left);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		// textArea.replaceSelection("\n");

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
			doc.insertString(doc.getLength(), msg + "\n", right);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		// textArea.replaceSelection("\n");

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

		gc2.drawImage(ori_img, 0, 0, panel.getWidth(), panel.getHeight(), panel);
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
//         byte[] bb;
//         bb = MakePacket(msg);
//         dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg, gInfo.getGameStatus(), gInfo.getGameUserName());
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//            dos.close();
//            dis.close();
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