package com.wupgig.login;



import java.sql.Connection;
import java.sql.Timestamp;
import java.util.regex.Pattern;

import com.wupgig.common.Global;
import com.wupgig.common.JdbcUtils;
import com.wupgig.common.MD5Util;
import com.wupgig.pojo.Sinfo;
import com.wupgig.pojo.User;
import com.wupgig.service.SinfoService;
import com.wupgig.service.UserService;
import com.wupgig.service.impl.SinfoServiceImpl;
import com.wupgig.service.impl.UserServiceImpl;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 用户注册界面
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月24日上午8:51:59
 *
 */
public class UserRegister extends Stage{
	// 数据库操作
	private UserService userService = new UserServiceImpl();
	private SinfoService sinfoService = new SinfoServiceImpl();
	// 输入账号
	private TextField account;
	// 输入密码
	private PasswordField  password;
	// 输入确定密码
	private PasswordField confirmPassword;
	
	public UserRegister () {
		// 设置窗口大小不可改变
		this.setResizable(false);
		// 设置图标
		this.getIcons().add(new Image("images/icon-1.jpg"));
		// 设置窗口标题
		this.setTitle("用户注册");
		// 创建主舞台
		Pane pane = new Pane();
		// 设置舞台背景-蓝色
		pane.setBackground(new Background(new BackgroundFill(Color.AZURE,null,null)));
		// 创建场景对象 长为400像素，宽为400像素
		Scene scene = new Scene(pane, 400, 300);
		// 将场景放入舞台
		this.setScene(scene);
		
		// 加入文本
		this.addText(pane);
		
		// 加入文本框
		this.addTextBox(pane);
		
		// 加入按钮
		this.addButton(pane);
	}
	/**
	 * 账号、密码、确认密码文本
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:21:11
	* @param pane
	 */
	private void addText(Pane pane) {
		// 账号文本
		Text accountText = new Text(40, 50, "账号：");
		accountText.setFill(Color.BLACK);
		accountText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 密码文本
		Text passwordText = new Text(40, 120, "密码：");
		passwordText.setFill(Color.BLACK);
		passwordText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 确认密码文本
		Text confirmPassword = new Text(40, 190, "确认密码：");
		confirmPassword.setFill(Color.BLACK);
		confirmPassword.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		pane.getChildren().addAll(accountText, passwordText, confirmPassword);
	}
	
	/**
	 * 账号、密码、确认密码输入框
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:21:33
	* @param pane
	 */
	private void addTextBox(Pane pane) {
		// 账号输入框
		account = new TextField();
		account.setPrefSize(150, 15);
		account.setLayoutX(130);
		account.setLayoutY(33);
		
		// 密码输入框
		password = new PasswordField();
		password.setPrefSize(150, 15);
		password.setLayoutX(130);
		password.setLayoutY(103);
		
		// 确认密码输入框
		confirmPassword = new PasswordField();
		confirmPassword.setPrefSize(150, 15);
		confirmPassword.setLayoutX(130);
		confirmPassword.setLayoutY(173);
		
		pane.getChildren().addAll(account, password, confirmPassword);
	}
	
	/**
	 * 按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:22:54
	* @param pane
	 */
	private void addButton(Pane pane) {
		// 确认按钮
		this.registerButton(pane);
		
		// 取消按钮
		this.cancelButton(pane);
		
	}
	/**
	 * 确认按钮逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:23:00
	* @param pane
	 */
	private void registerButton(Pane pane) {
		Button registerButton = new Button("注册");
		registerButton.setPrefSize(40, 40);
		registerButton.setLayoutX(130);
		registerButton.setLayoutY(240);
		// 按钮关联回车键
		registerButton.setDefaultButton(true);
		pane.getChildren().add(registerButton);
		// 点击按钮实现用户注册
		registerButton.setOnAction(e -> {
			this.register(pane);
		});
	}
	/**
	 * 点击注册
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月25日上午7:44:40
	* @param pane
	 */
	private void register(Pane pane) {
		// 输入框不能为空
		if ("".equals(account.getText()) || "".equals(password.getText()) 
				|| "".equals(confirmPassword.getText())) {
				Alert alert = new Alert(AlertType.INFORMATION,"账号或密码不能为空！");
				alert.initOwner(this);
				alert.show();
				return;
		}
		// 密码和确认密码不一致
		if (!(password.getText().equals(confirmPassword.getText()))) {
			Alert alert = new Alert(AlertType.INFORMATION,"输入的两次密码不一致！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 正则表达式规范账号密码 
		String patternAccount = "[\u4e00-\u9fa5_a-zA-Z0-9_]{1,15}";
		String patternPassword = "[a-zA-Z0-9_]{6,15}";
		boolean isPassword = Pattern.matches(patternPassword, password.getText());
		boolean isAccount = Pattern.matches(patternAccount, account.getText());
		if (!isAccount) {
			Alert alert = new Alert(AlertType.INFORMATION,"账号需要为1-15位的中文，英文字母和数字及下划线");
			alert.initOwner(this);
			alert.show();
			return;
		}
		if (!isPassword) {
			Alert alert = new Alert(AlertType.INFORMATION,"密码需要为6-15位的英文字母和数字及下划线");
			alert.initOwner(this);
			alert.show();
			return;
		}
		
		// 账号已经存在
		String accountString = account.getText();
		User user = userService.queryUserByAccount(accountString);
		if (user != null) {
			Alert alert = new Alert(AlertType.INFORMATION,"账号已存在！！！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 将用户信息保存到数据库中
		User nowUser = new User();
		nowUser.setAccount(accountString);
		// md5加密密码
		nowUser.setPassword(MD5Util.digest(confirmPassword.getText()));
		nowUser.setRegTime(new Timestamp(System.currentTimeMillis()));
		Connection conn = null;
		try {
			conn = JdbcUtils.getConnection();
			JdbcUtils.disableAutocommit(conn);
			userService.saveUser(nowUser);
			// 保存离线用户到数据库
			Sinfo sinfo = new Sinfo();
			sinfo.setAccount(accountString);
			sinfo.setAddress(Global.myIP);
			sinfoService.saveSinfo(sinfo);
			JdbcUtils.commit(conn);
		} catch (Exception e) {
			JdbcUtils.rollback(conn);
		} finally {
			if (conn != null) {
				JdbcUtils.close(conn);
			}
		}

		// 显示登录界面
		UserLogin userLogin = new UserLogin();
		userLogin.show();
		// 关闭注册界面
		this.close();
	}
	
	/**
	 * 取消按钮逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:23:08
	* @param pane
	 */
	private void cancelButton(Pane pane) {
		Button cancelButton = new Button("取消");
		cancelButton.setPrefSize(40, 40);
		cancelButton.setLayoutX(230);
		cancelButton.setLayoutY(240);
		// 按钮关联Esc按键
		cancelButton.setCancelButton(true);
		pane.getChildren().add(cancelButton);
		
		// 点击按钮取消连接
		cancelButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(this);
			alert.setTitle("取消连接");
			alert.setHeaderText("点击确认按钮会取消注册");
			alert.setContentText("你确认取消吗？");
			// 等待用户操作
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				// 关闭当前页面
				this.close();
			}
		});
	}

}
