// JavaObjClientView.java ObjecStram 湲곕컲 Client
// 떎吏덉쟻 씤 梨꾪똿 李 
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.SliderUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mysql.cj.TransactionEventHandler;

import dao.UserDao;
import model.User;
import mybatis.Mybatis_User;

import java.awt.*;

public class JavaGameClientView extends JFrame {
	/**
	* 
	*/
	
	private static final long serialVersionUID = 1L;

	private JTextField txtInput;
	private JSlider slider;

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
	private JTextPane textArea;
	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;
	public JButton startbtn, rectanglebtn, circlebtn;
//   Point first_point, last_point, old_point;

	private int ox, oy, nx, ny;
	// 유저 이름
	private JLabel user_label_01;
	private JLabel user_label_02;
	private JLabel user_label_03;
	private JLabel user_label_04;


	public JButton bluebtn, redbtn, yellowbtn, blackbtn, pinkbtn, eraserbtn, clearbtn;

	// 그리기관련 툴박스
	private JPanel toolBox;
	private Graphics gc;
	private Graphics gc2 = null;
	private Graphics gc3 = null;
	private Color color;
	JPanel panel;
	private JLabel lblMouseEvent;
	private int thickness, pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null;
	private Image panelImage2 = null;
	public int stroke =3;
	JButton pen_size_plus, pen_size_minus;
	private JLabel lblNewLabel;
	Point start = null, end = null;
	String fg = "free";
	Figure figure = new Figure();

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
		// -------------------------------------------------

		// userImg
		UserDao userDao = new UserDao(Mybatis_User.getSqlSessionFactory());
		User user = userDao.selectByUserIMG(username);

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
		contentPane.add(user_panel_01);

		user_panel_03.setBounds(638, 32, 135, 125);
		contentPane.add(user_panel_03);

		user_panel_02.setBounds(22, 238, 135, 125);
		contentPane.add(user_panel_02);

		user_panel_04.setBounds(638, 238, 135, 125);
		contentPane.add(user_panel_04);

		// -------------------------------------------------

		// 채팅창
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(242, 467, 303, 95);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);

		txtInput = new JTextField();
		txtInput.setBounds(244, 566, 301, 21);
		contentPane.add(txtInput);
		txtInput.setColumns(10);
		
		// -------------------------------------------------
		

		btnSend = new JButton("Send");
		btnSend.setBounds(549, 555, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		imgBtn = new JButton("+");
		imgBtn.setBounds(176, 555, 50, 40);
		contentPane.add(imgBtn);

		JButton EXITBtn = new JButton("EXIT");
		EXITBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		EXITBtn.setBounds(703, 555, 69, 40);
		contentPane.add(EXITBtn);

		panel = new JPanel(); // 캔버스 붙어있음
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(176, 32, 442, 342);
		contentPane.add(panel);
		panel.setLayout(null);
		gc = panel.getGraphics();
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(373, 317, 57, 15);
		panel.add(lblNewLabel); //ㅇㅁㅇ?
		
		// ==============================================================
		// 그림이 그려지는 패널
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = (Graphics2D) panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		gc2.setColor(Color.blue);
		gc2.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);
		
		panelImage2 = createImage(panel.getWidth(), panel.getHeight());
		gc3 = (Graphics2D) panelImage.getGraphics();
		gc3.setColor(panel.getBackground());
		gc3.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		gc3.setColor(Color.blue);
		gc3.drawRect(0, 0, panel.getWidth() - 1, panel.getHeight() - 1);

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
		// --------------------------------------------------------------

//====================================================== 색 버튼, 모양 버튼 모임
		toolBox = new JPanel();
		toolBox.setBounds(176, 393, 442, 64);
		toolBox.setBackground(Color.white);
		contentPane.add(toolBox);
		toolBox.setLayout(null);

		redbtn = new JButton("");
		redbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBtColor("red");
			}
		});
		redbtn.setBounds(8, 8, 50, 23);
		redbtn.setBackground(Color.red);
		toolBox.add(redbtn);

		bluebtn = new JButton("");
		bluebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				getBtColor("blue");
			}
		});
		bluebtn.setBounds(63, 8, 50, 23);
		bluebtn.setBackground(Color.blue);
		toolBox.add(bluebtn);

		yellowbtn = new JButton("");
		yellowbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				getBtColor("yellow");
			}
		});
		yellowbtn.setBounds(8, 33, 50, 23);
		yellowbtn.setBackground(Color.yellow);
		toolBox.add(yellowbtn);

		blackbtn = new JButton("");
		blackbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				getBtColor("black");
			}
		});
		blackbtn.setBounds(63, 33, 50, 23);
		blackbtn.setBackground(Color.black);
		toolBox.add(blackbtn);

		pinkbtn = new JButton("");
		pinkbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				getBtColor("pink");
			}
		});
		pinkbtn.setBackground(Color.pink);
		pinkbtn.setBounds(118, 33, 50, 23);
		toolBox.add(pinkbtn);

		eraserbtn = new JButton("");
		eraserbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				getBtColor("white");
			}
		});
		eraserbtn.setBackground(Color.white);
		eraserbtn.setBounds(118, 8, 50, 23);
		toolBox.add(eraserbtn);

		clearbtn = new JButton("clear");
		clearbtn.setBackground(Color.CYAN);
		clearbtn.setBounds(198, 8, 50, 23);

		clearbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				clear(str);
			}
		});
		toolBox.add(clearbtn);
//========================================================================시작버튼
		startbtn = new JButton("Game Start");
		startbtn.setBounds(638, 398, 128, 40);
		startbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "800", "GameStart");
				SendObject(cm);
			}
		});
		contentPane.add(startbtn);
//========================================================================도형
		
		circlebtn = new JButton("○");
		circlebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("circle");
			}
		});
		circlebtn.setBounds(286, 8, 50, 23);
		toolBox.add(circlebtn);
		
		rectanglebtn = new JButton("□");
		rectanglebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("square");
			}
		});
		rectanglebtn.setBounds(286, 33, 50, 23);
		toolBox.add(rectanglebtn);
		
		JButton trianglebtn = new JButton("△");
		trianglebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("triangle");
			}
		});
		trianglebtn.setBounds(348, 8, 50, 23);
		toolBox.add(trianglebtn);
		
		JButton freebtn = new JButton("~");
		freebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				figure.setFigure("free");
			}
		});
		freebtn.setBounds(348, 33, 50, 23);
		toolBox.add(freebtn);
		
		pen_size_plus = new JButton("+");
		pen_size_plus.setFont(new Font("굴림", Font.PLAIN, 15));
		pen_size_plus.setBounds(180, 33, 45, 23);
		pen_size_plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				ChatMsg cm = new ChatMsg(UserName, "900", "stroke_szie++");
				SendObject(cm);
			}
		});
		toolBox.add(pen_size_plus);
		
		pen_size_minus = new JButton("-");
		pen_size_minus.setFont(new Font("굴림", Font.PLAIN, 15));
		pen_size_minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg cm = new ChatMsg(UserName, "900", "stroke_szie--");
				SendObject(cm);
			}
		});
		pen_size_minus.setBounds(226, 33, 45, 23);
		toolBox.add(pen_size_minus);
		
		
		// -------------------------------------------------

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//         is = socket.getInputStream();
//         dis = new DataInputStream(is);
//         os = socket.getOutputStream();
//         dos = new DataOutputStream(os);

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
			bluebtn.addActionListener(action);
			redbtn.addActionListener(action);
			yellowbtn.addActionListener(action);
			blackbtn.addActionListener(action);
			pinkbtn.addActionListener(action);
			clearbtn.addActionListener(action);
			eraserbtn.addActionListener(action);
			startbtn.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			imgBtn.addActionListener(action2);
			MyMouseEvent mouse = new MyMouseEvent();
			panel.addMouseMotionListener(mouse);
			panel.addMouseListener(mouse);
//			MyMouseWheelEvent wheel = new MyMouseWheelEvent();
//			panel.addMouseWheelListener(wheel);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	public void paint(Graphics g) {
		super.paint(g);
		// Image 영역이 가려졌다 다시 나타날 때 그려준다.
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
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						if (cm.UserName.equals(UserName))
							AppendTextR(msg); // 궡 硫붿꽭吏 뒗 슦痢≪뿉
						else
							AppendText(msg);
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
						Start();
					case "900":
						if(cm.data.equals("stroke_szie++"))
							if(stroke<12)
						((Graphics2D) gc2).setStroke(new BasicStroke(stroke++));
						if(cm.data.equals("stroke_szie--")) {
							if(stroke>3)
							((Graphics2D) gc2).setStroke(new BasicStroke(stroke--));
						}
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//                  dos.close();
//                  dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch臾 걹
				} // 諛붽묑 catch臾몃걹

			}
		}
	}
	
	public void drawFigure(String Figure) {
		
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
			ChatMsg cm = new ChatMsg(UserName, "700", "clear");
			SendObject(cm);
		}
	}

	public void Start() {
	}


	public void clear() {
		Graphics gc = panel.getGraphics();
		gc.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		panel.repaint();
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 =  panelImage.getGraphics();
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
		} else if (color.equals("pink")) {
			gc2.setColor(Color.pink);
		} else if (color.equals("white")) {
			gc2.setColor(Color.white);
		}
		ChatMsg cm = new ChatMsg(UserName, "600", color);
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
		} else if (color.equals("pink")) {
			gc2.setColor(Color.pink);
		} else if (color.equals("white")) {
			gc2.setColor(Color.white);
		}
	}

//=============================================== 
	// Mouse Event 수신 처리
	public void DoMouseEvent(ChatMsg cm) {
		if (cm.UserName.matches(UserName)) // 본인 것은 이미 Local 로 그렸다.
			return;
		((Graphics2D) gc2).setStroke(new BasicStroke(stroke));
		
		if (cm.mouse_e.getID() == MouseEvent.MOUSE_PRESSED) {
			ox = cm.mouse_e.getX();
			oy = cm.mouse_e.getY();
		}
		nx = cm.mouse_e.getX();
		ny = cm.mouse_e.getY();
		gc2.drawLine(ox, oy, nx, ny);
		ox = nx;
		oy = ny;

		gc.drawImage(panelImage, 0, 0, panel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(UserName, "500", "MOUSE");
		cm.mouse_e = e;
		SendObject(cm);
	}

	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {

	      public void mouseDragged(MouseEvent e) {
	          lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," + e.getY());
	          ((Graphics2D) gc2).setStroke(new BasicStroke(stroke));
	          nx = e.getX();
	          ny = e.getY();
	          gc2.drawLine(ox, oy, nx, ny);
	          ox = nx;
	          oy = ny;
	          // panelImnage는 paint()에서 이용한다.
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
			ox = e.getX();
			oy = e.getY();
			lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," + e.getY());
			SendMouseEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," + e.getY());
			end = e.getPoint();
			((Graphics2D) gc3).setStroke(new BasicStroke(stroke));
			int x = Math.min(start.x, end.x);
			int y = Math.min(start.y, end.y);
			int width = Math.abs(start.x - end.x);
			int height = Math.abs(start.y - end.y);
			
			String fg = figure.getFigure();
			
			if(!fg.equals("free")) {
				if(fg.equals("circle")) {
					gc3.drawOval(x, y, width, height); 
				}else if(fg.equals("square")) {
					gc3.drawRect(x, y, width, height); 
				}else if(fg.equals("triangle")) {
					
				}
			gc3.drawImage(panelImage2, 0, 0, panel);
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
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
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