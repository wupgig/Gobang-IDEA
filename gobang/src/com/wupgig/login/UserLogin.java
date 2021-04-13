package com.wupgig.login;

import java.util.Optional;

import com.wupgig.chess.ChessBoard;
import com.wupgig.common.Global;
import com.wupgig.common.MD5Util;
import com.wupgig.common.ServerThread;
import com.wupgig.pojo.Address;
import com.wupgig.pojo.Sinfo;
import com.wupgig.pojo.User;
import com.wupgig.service.AddressService;
import com.wupgig.service.SinfoService;
import com.wupgig.service.UserService;
import com.wupgig.service.impl.AddressServiceImpl;
import com.wupgig.service.impl.SinfoServiceImpl;
import com.wupgig.service.impl.UserServiceImpl;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 用户登录界面
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月24日上午8:52:12
 *
 */
public class UserLogin extends Stage{
	// 主界面的stage，用于登录后关闭主界面
	public static Stage stage;
	// 数据库操作
	private UserService userService = new UserServiceImpl();
	private AddressService addressService = new AddressServiceImpl();
	private SinfoService sinfoService = new SinfoServiceImpl();
	// 输入账号
	private TextField account;
	// 输入密码
	private PasswordField  passwordField;
	// 记住密码复选框
	CheckBox check;
	
	public UserLogin () {
		// 设置窗口大小不可改变
		this.setResizable(false);
		// 设置图标
		this.getIcons().add(new Image("images/icon-1.jpg"));
		// 设置窗口标题
		this.setTitle("用户登录");
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
		
		// 记住密码复选框
		this.rememberPassword(pane);
		
		// 记住账号
		this.rememberAccount();

		// 是否记住密码
		this.isRememberPassword();
		
		// 加入按钮
		this.addButton(pane);
	}
	/**
	 * 记住密码功能复选框
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日上午10:04:49
	* @param pane
	 */
	public void rememberPassword(Pane pane) {
		// 加入记住密码复选框
		check = new CheckBox();
		check.setLayoutX(160);
		check.setLayoutY(145);
		Text text = new Text(185, 157,"记住密码");
		text.setFill(Color.BLACK);
		text.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 10));
		pane.getChildren().addAll(check, text);
	}
	/**
	 * 记住密码功能逻辑实现
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日上午10:14:15
	 */
	public void isRememberPassword() {
		Address address = addressService.queryAccountByIP(Global.myIP);
		// 数据库用户地址表中有该账号和ip地址
		if (address != null) {
			boolean isRemember = address.getRemember() == 1 ? true : false;
			check.setSelected(isRemember);
			// 如果用户选择了记住密码
			if (isRemember) {			
				// 记住密码到密码框
				passwordField.setText(userService.queryUserByAccount(address.getAccount()).getPassword());
			}
		}
	}
	/**
	 * 记住账号功能实现
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日上午9:03:14
	 */
	public void rememberAccount() {
		if (Global.myIP != null) {
			// 通过ip查询账号
			Address address = addressService.queryAccountByIP(Global.myIP);
			// 如果数据库中有这个账号，则直接将这个账号写入账号框
			if (address != null) {
				this.account.setText(address.getAccount());
			}
		}
	}
	/**
	 * 添加账号、密码的文本
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午8:57:46
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
		
		
		pane.getChildren().addAll(accountText, passwordText);
	}
	
	/**
	 * 添加输入账号、密码的输入框
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午8:58:17
	* @param pane
	 */
	private void addTextBox(Pane pane) {
		// 账号输入框
		account = new TextField();
		account.setPrefSize(150, 15);
		account.setLayoutX(130);
		account.setLayoutY(33);
		
		// 密码输入框
		passwordField = new PasswordField();
		passwordField.setPrefSize(150, 15);
		passwordField.setLayoutX(130);
		passwordField.setLayoutY(103);
		
		pane.getChildren().addAll(account, passwordField);
	}
	
	/**
	 * 添加登录、取消、注册的按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午8:58:51
	* @param pane
	 */
	private void addButton(Pane pane) {
		// 登录按钮
		this.loginButton(pane);
		
		// 取消按钮
		this.cancelButton(pane);
		
		// 注册按钮
		this.registerButton(pane);
	}
	/**
	 * 登录按钮逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:10:08
	* @param pane
	 */
	private void loginButton(Pane pane) {
		Button oKButton = new Button("确定");
		oKButton.setPrefSize(40, 40);
		oKButton.setLayoutX(100);
		oKButton.setLayoutY(200);
		// 按钮关联回车键
		oKButton.setDefaultButton(true);
		pane.getChildren().add(oKButton);
		// 点击按钮实现显示ip 端口配置界面
		oKButton.setOnAction(e -> {
			// 登录逻辑
			login(pane);
		});
	}
	/**
	 * 点击登录执行的逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月25日上午8:54:05
	* @param pane
	 */
	private void login(Pane pane) {
		// 账号或密码不能为空
		if ("".equals(account.getText()) || "".equals(passwordField.getText())) {
			Alert alert = new Alert(AlertType.INFORMATION,"账号或密码不能为空！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 根据输入的账号密码查询
		User user = userService.queryUserByAccountAndPassword(account.getText(), passwordField.getText());
		// 如果密码正确或加密后的密码正确，登录成功，否则登录失败
		if (user == null) {
			// md5加密
			String md5Password = MD5Util.digest(passwordField.getText());
			User md5User = userService.queryUserByAccountAndPassword(account.getText(), md5Password);
			if (md5User == null) {
				Alert alert = new Alert(AlertType.INFORMATION,"账号或密码输入错误！");
				alert.initOwner(this);
				alert.show();
				return;
			} else {
				// 判断该玩家是否在线
				Sinfo sinfo = sinfoService.queryIPByAccount(account.getText());
				if (sinfo.getStatus() != 0) {
					Alert alert = new Alert(AlertType.INFORMATION,"账号已在线，无法重复登录！");
					alert.initOwner(this);
					alert.show();
					return;
				}
			}
		} else {
			Sinfo sinfo = sinfoService.queryIPByAccount(account.getText());
			if (sinfo.getStatus() != 0) {
				Alert alert = new Alert(AlertType.INFORMATION,"账号已在线，无法重复登录！");
				alert.initOwner(this);
				alert.show();
				return;
			}
		}
		
		// 将用户账号保存到Global类中
		Global.account = account.getText();
		// 查看用户地址表中是否存在该ip和账号
		Address address =  addressService.queryAccountByIP(Global.myIP);
		// 不存在就保存到数据库
		if (address == null) {
			Address saveAddress = new Address();
			saveAddress.setAccount(Global.account);
			saveAddress.setAddress(Global.myIP);
			addressService.saveAddress(saveAddress);
		// 存在且账号不相同
		} else if (!Global.account.equals(address.getAccount())) {
			// 更新账号
			addressService.updateAccount(Global.myIP, Global.account);
		}
		
		// 将用户是否记住密码的选择更新到数据库
		if (check.isSelected()) {
			// 用户选择记住密码
			addressService.updateRemember(1, Global.account);
		} else {
			// 用户选择不记住密码
			addressService.updateRemember(0, Global.account);
		}
		
		Sinfo queryIPByAccount = sinfoService.queryIPByAccount(Global.account);
		
		// 如果对应的账号下的ip发生了改变，则更新他的ip和在线空闲状态即可
		if (!queryIPByAccount.getAddress().equals(Global.myIP)) {
			// 更新用户ip地址
			sinfoService.updateIPByAccount(Global.account, Global.myIP);
			// 更改在在线状态为空闲
			sinfoService.updateStatusByAccount(Global.account, 1);
			
		// 如果对应的账号下的ip没变，则更改为在线空闲状态即可
		} else {
			// 更改在在线状态为空闲
			sinfoService.updateStatusByAccount(Global.account, 1);
		}
		
		// 关闭登录界面
		this.close();
		// 登录后，关闭主界面
		this.stage.close();
		
		// 开启server线程监听对手客户端在棋盘打开后发送的消息
		ServerThread serverThread = new ServerThread();
		Thread boardThread = new Thread(serverThread);
		boardThread.start();
		
		// 打开棋盘界面
		ChessBoard chessBoard = new ChessBoard();
		chessBoard.show();
	}
	
	/**
	 * 取消按钮逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:10:15
	* @param pane
	 */
	private void cancelButton(Pane pane) {
		Button cancelButton = new Button("取消");
		cancelButton.setPrefSize(40, 40);
		cancelButton.setLayoutX(180);
		cancelButton.setLayoutY(200);
		// 按钮关联Esc按键
		cancelButton.setCancelButton(true);
		pane.getChildren().add(cancelButton);
		
		// 点击按钮取消连接
		cancelButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "你确认退出登录界面吗？",
					new ButtonType("取消",  ButtonData.NO), 
					new ButtonType("确定",  ButtonData.YES));
			alert.initOwner(this);
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonData.YES) {
				// 关闭登录界面
				this.close();
			}
		});
	}
	/**
	 * 注册按钮逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日上午9:10:23
	* @param pane
	 */
	private void registerButton(Pane pane) {
		Button registerButton = new Button("注册");
		registerButton.setPrefSize(40, 40);
		registerButton.setLayoutX(260);
		registerButton.setLayoutY(200);
		pane.getChildren().add(registerButton);
		// 点击按钮实现显示ip 端口配置界面
		registerButton.setOnAction(e -> {
			// 关闭登录页面
			this.close();
			UserRegister userRegister = new UserRegister();
			// 显示注册界面
			userRegister.show();
		});
	}
}
