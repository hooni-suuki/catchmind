
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
						// 600: 색상바꾸기 700: 화면 클리어 800: 게임시작 및 게임관련 메시지 900: 펜사이즈 증감
	public String UserName;
	public String data;
	public String gStatus;
	public String gGameUserName;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size; // pen size
	
	public ChatMsg(String UserName, String code, String msg, String gameStatus, String gameUserName) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
		this.gStatus = gameStatus;
		this.gGameUserName = gameUserName;
	}
}