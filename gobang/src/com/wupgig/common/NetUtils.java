package com.wupgig.common;

import java.io.ObjectOutputStream;
import java.net.Socket;

import com.wupgig.message.Message;

import javafx.scene.control.Alert;
/**
 * 发送网络信息的工具类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月22日下午9:30:45
 *
 */
public class NetUtils {
	/**
	 * 客户端给服务端发送消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午9:31:41
	* @param message
	 */
	public static void sendMessage(Message message, String oppoIP) {
		try (Socket socket = new Socket(oppoIP, Global.oppoPort);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
			
			oos.writeObject(message);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR, "连接对手出错！请稍后再试！");
			alert.showAndWait();
		}
	}

}
