import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class test2 extends JFrame implements MouseListener, ActionListener {
	private JButton buttonCls,buttonRed,buttonGreen,buttonBlue,buttonExit;	//버튼 만들기
	private Point p1,p2;   //마우스의 좌표를 얻기위한 변수
	private Graphics g;  //그리기수행하는 변수
	private JLabel labelclick, labelstart, labelend; //좌표를 표시하기 위한 라벨
	private JPanel pic;	//그림판

	private static Color color;

	public test2() {
		Container cp = getContentPane();
		cp.setLayout(null);						//버튼을 원하는 위치에 놓기위해
		buttonCls = new JButton("Clear");		//지우기버튼
		buttonCls.setBounds(10, 10, 80, 30);	//지우기 버튼의 위치
		buttonCls.addActionListener(this);

		buttonRed = new JButton("Red");			//빨간버튼
		buttonRed.setBounds(100, 10, 90, 30);	//빨간버튼위치
		buttonRed.setBackground(Color.red);		//버튼색깔빨간색으로..
		buttonRed.addActionListener(this);
		buttonGreen = new JButton("Green");		//녹색버튼
		buttonGreen.setBounds(200, 10, 90, 30);	//녹생버튼위치
		buttonGreen.setBackground(Color.green);	//버튼색깔녹색으로..
		buttonGreen.addActionListener(this);
		buttonBlue = new JButton("Blue");		//파란버튼
		buttonBlue.setBounds(300, 10, 90, 30);	//파란버튼위치
		buttonBlue.setBackground(Color.blue);	//버튼색깔파랑으로..
		buttonBlue.addActionListener(this);
		buttonExit = new JButton("Exit");		//종료버튼
		buttonExit.setBounds(400, 10, 90, 30);	//종료버튼위치
		buttonExit.addActionListener(this);	
		
		cp.add(buttonCls);		//지우기버튼 그리기
		cp.add(buttonRed);		//빨간색버튼 그리기
		cp.add(buttonGreen);	//녹색버튼 그리기
		cp.add(buttonBlue);		//파랑버튼 그리기
		cp.add(buttonExit);		//종료버튼 그리기
		
		pic = new JPanel();				//마우스로 그릴 그림판
		pic.setBounds(0,60,500,500);	//그림판의 범위 0,60 에서 500,500까지

		pic.addMouseListener(this);  
		cp.add(pic);	//그림판 구현

		labelclick=new JLabel();	//클릭한 좌표
		labelclick.setBounds(10,40,150,20);	//표시할 위치
		cp.add(labelclick);
		setVisible(true);

		labelstart=new JLabel();
		labelstart.setBounds(180,40,200,20);
		cp.add(labelstart);
		setVisible(true);

		labelend=new JLabel();
		labelend.setBounds(320,40,150,20);
		cp.add(labelend);
		setVisible(true);
	}
	public void mouseClicked(MouseEvent e) {
		labelclick.setText("클릭(x : "+e.getX()+", y : "+e.getY()+")");
		} //마우스 클릭시 좌표 보여줌
	public void mouseEntered(MouseEvent e) {} //마우스가 pic 위에 가도 이벤트 없음
	public void mouseExited(MouseEvent e) {	} //pic 위에서 이동해도 이벤트 없음
	public void mousePressed(MouseEvent e) { //마우스 버튼눌르고 드래그 할때
		p1=e.getPoint();					//이벤트 발생. 마우스를 클릭한지점의 좌표를 얻음
		labelstart.setText("시작(x : "+e.getX()+", y : "+e.getY()+") →→");
		}
	public void mouseReleased(MouseEvent e) { 
		p2=e.getPoint();					//이벤튼 발생. 마우스를 뗀지점 좌표를 얻음
		labelend.setText("끝(x : "+e.getX()+", y : "+e.getY()+")");
		g = pic.getGraphics();
		g.setColor(color);
		
		if((int)p1.getX()<(int)p2.getX() && (int)p1.getY()<(int)p2.getY())	//왼쪽위에서 오른쪽 아래로 사각형 그리기
			g.drawRect((int)p1.getX(),(int)p1.getY(),(int)p2.getX()-(int)p1.getX(),(int)p2.getY()-(int)p1.getY());
		else if ((int)p1.getX()>(int)p2.getX() && (int)p1.getY()>(int)p2.getY())//오른쪽 아래에서 왼쪽위로 사각형 그리기
			g.drawRect((int)p2.getX(),(int)p2.getY(),(int)p1.getX()-(int)p2.getX(),(int)p1.getY()-(int)p2.getY());
		else if ((int)p1.getX()>(int)p2.getX() && (int)p1.getY()<(int)p2.getY())//오른쪽 위에서 왼쪽아래로 사각형 그리기
			g.drawRect((int)p2.getX(),(int)p1.getY(),(int)p1.getX()-(int)p2.getX(),(int)p2.getY()-(int)p1.getY());
		else																	//왼쪽아래에서 오른쪽위로 사각형 그리기
			g.drawRect((int)p1.getX(),(int)p2.getY(),(int)p2.getX()-(int)p1.getX(),(int)p1.getY()-(int)p2.getY());	
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Red")) {
			color = Color.red;	//빨강
		} else if(e.getActionCommand().equals("Green")) {
			color = Color.green;	//녹색
		} else if(e.getActionCommand().equals("Blue")) {
			color = Color.blue;		//파랑
		} else if(e.getActionCommand().equals("Exit"))	{
			System.exit(0);	//버튼 클릭시 종료
		} else if(e.getActionCommand().equals("Clear")){
			labelclick.setText(" ");
			labelstart.setText(" ");
			labelend.setText(" ");
			g.clearRect(0,0,500,500);
		}
	}
	
	public static void main(String[] args)
	{
		test2 f = new test2();
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);	}	}	);
		f.setTitle("사각형 그리기");
		f.setSize(500, 500);
		f.setVisible(true);
	}
}