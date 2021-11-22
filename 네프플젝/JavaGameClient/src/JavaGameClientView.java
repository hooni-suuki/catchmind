
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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
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


public class JavaGameClientView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 泥섎읆 BUF_LEN �쓣 �젙�쓽
	private Socket socket; // �뿰寃곗냼耳�
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	// private JTextArea textArea;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;

	JPanel panel;
	private JLabel lblMouseEvent;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 洹몃젮吏� Image瑜� 蹂닿��븯�뒗 �슜�룄, paint() �븿�닔�뿉�꽌 �씠�슜�븳�떎.
	private Image panelImage = null; 
	private Graphics gc2 = null;
	private Audio backAudio;

	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientView(String username,  String ip_addr, String port_no)  {
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 634);
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

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(238, 488, 303, 107);
		contentPane.add(scrollPane);
		
				textArea = new JTextPane();
				scrollPane.setViewportView(textArea);
				textArea.setEditable(true);
				textArea.setFont(new Font("援대┝泥�", Font.PLAIN, 14));

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

		JButton btnNewButton = new JButton("醫� 猷�");
		btnNewButton.setFont(new Font("援대┝", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(549, 488, 69, 40);
		contentPane.add(btnNewButton);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(176, 32, 442, 381);
		contentPane.add(panel);
		gc = panel.getGraphics();
		
		// Image �쁺�뿭 蹂닿��슜. paint() �뿉�꽌 �씠�슜�븳�떎.
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
		lblMouseEvent.setBounds(176, 438, 442, 40);
		contentPane.add(lblMouseEvent);
		
		JLabel user_label_01 = new JLabel("user_name_01");
		user_label_01.setBounds(22, 171, 135, 15);
		contentPane.add(user_label_01);
		
		JLabel user_label_02 = new JLabel("user_name_02");
		user_label_02.setBounds(22, 373, 135, 15);
		contentPane.add(user_label_02);
		
		JLabel user_label_03 = new JLabel("user_name_03");
		user_label_03.setBounds(638, 171, 135, 15);
		contentPane.add(user_label_03);
		
		JLabel user_label_04 = new JLabel("user_name_04");
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
		
		JButton exit_btn = new JButton("EXIT");
		exit_btn.setBounds(685, 555, 97, 38);
		contentPane.add(exit_btn);

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
			ImageSendAction action = new ImageSendAction();
			btnSend.addActionListener(action);
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
		Color c;
		if (cm.UserName.matches(UserName)) // 蹂몄씤 寃껋� �씠誘� Local 濡� 洹몃졇�떎.
			return;
		c = new Color(255, 0, 0); // �떎瑜� �궗�엺 寃껋� Red
		gc2.setColor(c);
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
			Color c = new Color(0,0,255);
			gc2.setColor(c);
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

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// �븸�뀡 �씠踰ㅽ듃媛� sendBtn�씪�븣 �삉�뒗 textField �뿉�꽭 Enter key 移섎㈃
			if (e.getSource() == imgBtn) {
				frame = new Frame("�씠誘몄�泥⑤�");
				fd = new FileDialog(frame, "�씠誘몄� �꽑�깮", FileDialog.LOAD);
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

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// �걹�쑝濡� �씠�룞
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// �솕硫댁뿉 異쒕젰
	public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // �븵�뮘 blank�� \n�쓣 �젣嫄고븳�떎.
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
	// �솕硫� �슦痢≪뿉 異쒕젰
	public void AppendTextR(String msg) {
		msg = msg.trim(); // �븵�뮘 blank�� \n�쓣 �젣嫄고븳�떎.	
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
		// Image媛� �꼫臾� �겕硫� 理쒕� 媛�濡� �삉�뒗 �꽭濡� 200 湲곗��쑝濡� 異뺤냼�떆�궓�떎.
		if (width > 200 || height > 200) {
			if (width > height) { // 媛�濡� �궗吏�
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // �꽭濡� �궗吏�
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
		// new_icon.addActionListener(viewaction); // �궡遺��겢�옒�뒪濡� �븸�뀡 由ъ뒪�꼫瑜� �긽�냽諛쏆� �겢�옒�뒪濡�
		// panelImage = ori_img.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_DEFAULT);

		gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
		gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
	}

	// Windows 泥섎읆 message �젣�쇅�븳 �굹癒몄� 遺�遺꾩� NULL 濡� 留뚮뱾湲� �쐞�븳 �븿�닔
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

	// Server�뿉寃� network�쑝濡� �쟾�넚
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

	public void SendObject(Object ob) { // �꽌踰꾨줈 硫붿꽭吏�瑜� 蹂대궡�뒗 硫붿냼�뱶
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("硫붿꽭吏� �넚�떊 �뿉�윭!!\n");
			AppendText("SendObject Error");
		}
	}
}
