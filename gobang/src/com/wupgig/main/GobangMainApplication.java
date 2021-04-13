package com.wupgig.main;

import java.net.InetAddress;

import com.wupgig.common.Global;
import com.wupgig.login.UserLogin;

import javafx.application.Application;
import javafx.stage.Stage;

public class GobangMainApplication extends Application{
	public static void main(String[] args) {
		// 启动程序
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		// 获取主机ip
		InetAddress inetAddress = InetAddress.getLocalHost();
		Global.myIP = inetAddress.getHostAddress();

		UserLogin userLogin = new UserLogin();
		UserLogin.stage = stage;
		// 显示用户登录界面
		userLogin.show();
	}

}
