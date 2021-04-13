package com.wupgig.common;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.wupgig.message.Message;

import javafx.application.Platform;
import javafx.stage.Stage;

// 接受客户端发送的消息
public class ServerThread implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		try {
			// 创建服务器端的ServerSocket，指明自己的端口号
			serverSocket = new ServerSocket(Global.myPort);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 出现异常后，终止该线程
			return;
		}
		// 一直监听客户端的消息
		while (true) {
			// 调用accept()表示接受来自客户端的socket
			try (Socket socket = serverSocket.accept()) {
				// 获取客户端的输入流
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				// 将流中的消息对象读取出来
				Message message = (Message)ois.readObject();
				// 处理消息，指定将消息传到ChessBoard类中的upDateUI方法里面
				Platform.runLater(() -> Global.chessBoard.upDateUI(message));

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
