package com.wupgig.chess;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import com.wupgig.common.Global;
import com.wupgig.common.JdbcUtils;
import com.wupgig.common.Logic;
import com.wupgig.common.NetUtils;
import com.wupgig.message.ChessMessage;
import com.wupgig.message.CountdownMessage;
import com.wupgig.message.EscapeMessage;
import com.wupgig.message.GameRequestMeaasge;
import com.wupgig.message.GiveUpMessage;
import com.wupgig.message.Message;
import com.wupgig.message.NewGameMessage;
import com.wupgig.message.ReplayMessage;
import com.wupgig.message.ResultMessage;
import com.wupgig.message.RivalRecordMessage;
import com.wupgig.message.SelectChessColorMessage;
import com.wupgig.message.TextMessage;
import com.wupgig.pojo.Record;
import com.wupgig.pojo.Sinfo;
import com.wupgig.pojo.User;
import com.wupgig.record.MyRecord;
import com.wupgig.record.RivalRecord;
import com.wupgig.robot.RobotPlay;
import com.wupgig.service.RecordService;
import com.wupgig.service.SinfoService;
import com.wupgig.service.UserService;
import com.wupgig.service.impl.RecordServiceImpl;
import com.wupgig.service.impl.SinfoServiceImpl;
import com.wupgig.service.impl.UserServiceImpl;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.nio.ch.Net;

/**
 * 注册登录对战版本的棋盘
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月25日上午8:59:43
 *
 */
public class ChessBoard extends Stage{
	// 数据库操作
	private UserService userService;
	private RecordService recordService;
	private SinfoService sinfoService;
	// 五子棋盘的横竖线条的数量
	private static final int SIZE = 15;
	// 棋盘线之间的间隔
	private static final int LINE_SPACING = 40;
	// 是否轮到黑子下，黑先 true
	private boolean isBlack;
	// 游戏是否结束,默认结束 true
	private boolean gameOver;
	// 保存棋子的信息
	private List<Chess> chessList;
	// 棋子半径
	private static final int CHESS_RADIUS = 19;
	// 用来记录棋盘是否有棋子,false表示没有，true表示有
	private boolean[][] arr;
	// 用来记录棋盘对应位置棋子的颜色，和 arr一起判断某个位置是否有棋子和棋子的颜色时
	// 可以达到O(1)的时间复杂度
	private Color[][] colors;
	
	// 是否可以落子, 默认不可以
	private boolean isPlay;
	// 自己棋的颜色
	private Color color;
	// 自己棋的颜色,1 表示黑色，0表示白色
	private int blackOrWhite;
	
	// 用户是否可以悔棋，默认为true,可以(一个用户只能悔棋一次)
	private boolean replay;
	// 主舞台
	private Pane pane;
	
	// 显示在线玩家文本
	private Text userPlaytext;
	
	// 所有在线用户的text集合
	private List<Text> textList;
	
	// 刷新在线玩家按钮
	private Button refreshButton;
	
	// 在线玩家上一页按钮
	private Button prevPageButton;
	
	// 在线玩家下一页按钮
	private Button nextPageButton;
	
	// 发送悔棋请求、对战请求、新局请求后的提示文本
	private Text waitText;
	
	// 玩家是否发送了对战请求，默认没有
	private boolean isSend;
	
	// 玩家是否接受了对战请求，默认没有
	private boolean isAccept;
	
	// 最后一个棋子的标志对象
	private Circle redCircle;
	
	// 聊天消息展示范围
	TextArea textArea;
	// 发送消息文本框
	TextField messageText;
	
	// 分页查询起始页数，从0开始
	private int index;
	
	// 分页查询每页显示的玩家的个数
	private static final int USER_SIZE = 2;
	
	// 对局开始后 双方在棋盘上显示的文本集合
	private List<Text> startTextList;
	
	// 对局开始后，在自己账号后面显示的棋子
	private Circle myAccountCircle;
	
	// 对局开始后，在对手账号后面显示的棋子
	private Circle rivalAccountCircle;
	
	// 对局开始后，当前落子文本后面显示的棋子
	private Circle nowChess;
	
	// 时间线类，用来轮播舞台背景图
	private Timeline timeline;
	
	// 背景图集合
	private List<ImageView> imageList;
	
	// 定位当前背景图的下标，默认为0
	private int imageIndex;
	
	// 用来控制轮播按钮的暂停和开始,默认暂停
	private boolean playImages;
	
	// 背景音乐  
    private MediaPlayer mediaPlayer;
    
    // 局时文本
    private Text gameTimeText;
    
    // 局时 倒计时
    private Timeline gameTimeline;
    
    // 步时文本
    private Text stepTimeText;
    
    // 步时 倒计时
    private Timeline stepTimeline;
    
    // 控制局时的倒计时
    private int gameTimeNum;
    
    // 控制步时的倒计时
    private int stepTimeNum;
    
    // 下拉按钮
    private SplitMenuButton menuButton;
    
	// 用来控制背景音乐的暂停和开始,默认开始
	private boolean playMusic;
	
	// 背景音乐的暂停和开始按钮
	private Button musicButton;
	
	// 求助小棋仙的次数，最多可以求助3次
	private int robotTime;

	public ChessBoard() {
		// 初始化棋盘
		this.start();
	}
	/**
	 * 打开棋盘时，初始化棋盘和棋盘上的节点
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月12日上午11:32:43
	 */
	private void start() {
		// 初始化成员变量
		this.initializeVariable();
		// 播放背景音乐
        mediaPlayer.play();
        // 循环播放
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            
		// 保存棋盘对象到Global中
		Global.chessBoard = this;
		
		// 重写叉掉五子棋窗口后的方法
		this.newCloseRequest();
		
		// 将背景图放入集合中
		this.addImages();
		
		// 创建舞台场景
		this.initializeStage();
		
		// 添加时间线类控制逻辑
		this.createTimeline();
	
		// 在棋盘上划线，添加棋盘五点标志，以及显示文本
		this.addText();
		
		// 加入文本框
		this.addTextBox();

		// 鼠标点击棋盘，对应位置生成棋子
		this.mouseClikedChessboard();
		
		// 添加按钮
		this.addButton();
	}
	// 初始化成员变量
	private void initializeVariable() {
		userService = new UserServiceImpl();
		recordService = new RecordServiceImpl();
		sinfoService = new SinfoServiceImpl();
		isBlack = true;
		gameOver = true;
		chessList = new ArrayList<>();
		arr = new boolean[SIZE][SIZE];
		colors = new Color[SIZE][SIZE];
		replay = true;
		textList = new ArrayList<>();
		waitText = new Text(200,300,"");
		redCircle = new Circle(4);
		textArea = new TextArea();
		startTextList = new ArrayList<>();
		myAccountCircle = new Circle(830, 72, CHESS_RADIUS);
		rivalAccountCircle = new Circle(830, 212, CHESS_RADIUS);
		nowChess = new Circle(830, 282, CHESS_RADIUS);
		imageList = new ArrayList<>();
		mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/music/高山流水.mp3").toString()));
		playMusic = true;
		robotTime = 3;
	}
	/**
	 * 创建舞台场景
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月10日下午9:16:53
	 */
	private void initializeStage() {
		// 设置窗口大小不可改变
		this.setResizable(false);
		// 设置标题
		this.setTitle("五子棋");
		
		// 设置图标
		this.getIcons().add(new Image("images/icon-1.jpg"));
		
		// 创建主舞台
		pane = new Pane();
		
		// 创建场景对象--宽1000,高700像素
		Scene scene = new Scene(pane, 1000, 700);
		
		// 用css文件设置多行文本框的背景颜色为透明颜色、路径为该类的同一路径
		scene.getStylesheets().add(getClass().getResource("BackgroundColor.css").toExternalForm());
		// 把场景加入舞台
		this.setScene(scene);
		
		// 设置初始背景图
		ImageView imageView = new ImageView("images/白连衣裙樱花.jpg");
		pane.getChildren().add(imageView);
	}
	/**
	 * 鼠标点击棋盘后的逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月10日下午9:13:23
	 */
	private void mouseClikedChessboard() {
		pane.setOnMouseClicked(e -> {
			// 游戏开始且轮到你落子
			if (!gameOver && isPlay) {
				double x = e.getX(); 
				double y = e.getY();

				// 当 点击的x 或 y 坐标超出棋盘范围时，落子无效，设置10的偏移量
				if (x < 40 || x > 630 || y < 40 || y > 620) {
					return;
				}
				
				// 给棋盘上的横轴交叉的点定义坐标
				int xIndex = (int)Math.round((x - 50) / 40);
				int yIndex = (int)Math.round((y - 50) / 40);

				// 把棋子加入到棋盘中
				this.piece(xIndex, yIndex);
			}

		});
	}
	/**
	 * 重写叉掉五子棋窗口后的方法
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月10日下午9:06:02
	 */
	private void newCloseRequest() {
		this.setOnCloseRequest(e -> {
			if (Global.temporaryOppoIP != null) {

				// 发送逃跑消息，己方逃跑，对方显示胜利
				NetUtils.sendMessage(new EscapeMessage(), Global.temporaryOppoIP);
				
				if (!gameOver) {
					// 游戏未结束逃跑，给对手发送消息，对手将更新数据库
					ResultMessage resultMessage = new ResultMessage();
					resultMessage.setAccount(Global.account);
					resultMessage.setResult(Color.BLACK.equals(this.color) ? 
							ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
					
		        	// 发送对战结果消息
		        	NetUtils.sendMessage(resultMessage, Global.oppoIP);
				}
			}
			// 改为离线状态
			sinfoService.updateStatusByAccount(Global.account, 0);
			// 退出游戏按钮
			System.exit(0);
		});
	}
	/**
	 * 用于控制局时、步时倒计时和棋盘背景图轮播
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日上午9:23:10
	 */
	private void createTimeline() {
		// 动态轮播棋盘背景图
		this.changeImagesTimeline();
		
		// 控制局时倒计时
		this.controlGameTimeline();
		
		// 控制步时倒计时
		this.controlStepTimeline();
	}
	/**
	 * 控制局时倒计时
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日上午9:28:45
	 */
	private void controlGameTimeline() {
		this.gameTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> gameTimeText()));
		gameTimeline.setCycleCount(Timeline.INDEFINITE);
		// 使棋盘打开时倒计时暂停
		gameTimeline.play();
		gameTimeline.pause();
		
		this.gameTimeText = new Text(170, 30, "局时 10:00");
		gameTimeText.setFill(Color.RED);
		gameTimeText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));
		pane.getChildren().add(gameTimeText);
		
		// 初始化局时
		this.gameTimeNum = 600;
	}
	/**
	 * 局时文本，倒计时逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日上午9:32:21
	 */
	private void gameTimeText() {
		if (gameTimeNum == 0) {
			gameTimeline.pause();
			NetUtils.sendMessage(new CountdownMessage(), Global.oppoIP);
			
			// 认输，给对手发送消息，对手将更新数据库
			ResultMessage resultMessage = new ResultMessage();
			resultMessage.setAccount(Global.account);
			resultMessage.setResult(Color.BLACK.equals(this.color) ? 
					ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
			
        	// 发送对战结果消息
        	NetUtils.sendMessage(resultMessage, Global.oppoIP);
			
			// 认输后处理逻辑
			this.specialLose();
			return;
		}
		int minute = --gameTimeNum / 60;
		int second = gameTimeNum % 60;
		this.gameTimeText.setText("局时 " + (minute < 10 ? ("0" + minute) : minute) + ":" + (second < 10 ? ("0" + second) : second));
	}
	
	/**
	 * 控制步时倒计时
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日上午9:29:09
	 */
	private void controlStepTimeline() {
		this.stepTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> stepTimeText()));
		stepTimeline.setCycleCount(Timeline.INDEFINITE);
		// 使棋盘打开时倒计时暂停
		stepTimeline.play();
		stepTimeline.pause();
		
		
		this.stepTimeText = new Text(330, 30, "步时 60");
		stepTimeText.setFill(Color.RED);
		stepTimeText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));
		pane.getChildren().add(stepTimeText);
		
		// 初始化步时
		this.stepTimeNum = 60;
		
	}
	
	/**
	 * 步时文本，倒计时逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日上午9:34:51
	 */
	private void stepTimeText() {
		if (stepTimeNum == 0) {
			stepTimeline.pause();
			NetUtils.sendMessage(new CountdownMessage(), Global.oppoIP);
			
			// 认输，给对手发送消息，对手将更新数据库
			ResultMessage resultMessage = new ResultMessage();
			resultMessage.setAccount(Global.account);
			resultMessage.setResult(Color.BLACK.equals(this.color) ? 
					ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
			
        	// 发送对战结果消息
        	NetUtils.sendMessage(resultMessage, Global.oppoIP);
			
			// 认输后处理逻辑
			this.specialLose();
			return;
		}
		this.stepTimeText.setText("步时 " + --stepTimeNum);
	}
	
	/**
	 * 控制棋盘背景图动态轮播
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日上午9:25:22
	 */
	private void changeImagesTimeline() {
		// 背景图动态轮播， 1秒一次
		timeline = new Timeline(new KeyFrame
				(Duration.millis(1000), e -> changeImages()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		// 使窗口打开时背景图暂停轮放
		timeline.play();
		timeline.pause();
	}
	
	/**
	 * 棋盘背景图轮播
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午5:47:31
	 */
	private void changeImages() {
		if (imageIndex == imageList.size()) {
			imageIndex = 0;
		}
		pane.getChildren().set(0, imageList.get(imageIndex++));
	}
	
	/**
	 * 将背景图放入集合中,并将大小改为主窗口一样大
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午5:49:46
	 */
	private void addImages() {
		// 获取主窗口的宽高
		Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
		double width = screenRectangle.getWidth();
		double height = screenRectangle.getHeight();
		
		ImageView imageView1 = new ImageView("images/李白.jpg");
		ImageView imageView2 = new ImageView("images/貂蝉.jpg");
		ImageView imageView3 = new ImageView("images/鹿.jpg");
		ImageView imageView4 = new ImageView("images/古风美少女伞.jpg");
		ImageView imageView5 = new ImageView("images/静态山水.jpg");
		ImageView imageView6 = new ImageView("images/长头发女孩.jpg");
		ImageView imageView7 = new ImageView("images/雪竹宝剑单马尾.jpg");
		ImageView imageView8 = new ImageView("images/二次元女孩竹排.jpg");
		ImageView imageView9 = new ImageView("images/白连衣裙樱花.jpg");
		imageList.add(imageView1);
		imageList.add(imageView2);
		imageList.add(imageView3);
		imageList.add(imageView4);
		imageList.add(imageView5);
		imageList.add(imageView6);
		imageList.add(imageView7);
		imageList.add(imageView8);
		imageList.add(imageView9);
	}
	/**
	 * 消息发送文本框，消息显示框
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日下午3:02:32
	 */
	private void addTextBox() {
		// 消息发送文本框
		messageText = new TextField();
		messageText.setPrefSize(150, 20);
		messageText.setLayoutX(660);
		messageText.setLayoutY(600);
		
		// 消息显示框
		textArea.setLayoutX(660);
		textArea.setLayoutY(370);
		textArea.setFont(Font.font(15));

		// 关闭手动输入
		textArea.setEditable(false);
		// 设置自动换行
		textArea.setWrapText(true);

		// 设置多行文本框里面的文字颜色为红色
		textArea.setStyle("-fx-text-fill:red");
		
      //初始化设置行数
		textArea.setPrefRowCount(10);
		
      //设置宽高
		textArea.setPrefWidth(300);
		textArea.setPrefHeight(200);
		
		pane.getChildren().addAll(messageText, textArea);
	}
	/**
	 * 在棋盘上划线，添加棋盘五点标志，以及显示文本
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午4:43:31
	 */
	private void addText() {
		// 在棋盘上画线
		this.drawLine();
		// 在棋盘上画5个定位点
		this.drawFivePoint();
		// 显示在线玩家文本在棋盘上
		this.addPlay();
		// 显示在线玩家的用户名在棋盘上
		this.queryAllAccountShowSinfo(index * USER_SIZE, USER_SIZE);
		// 请求对战，悔棋、新局提示
		this.waitText();
	}
	/**
	 * 请求对战，悔棋、新局提示
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日下午1:35:41
	 */
	private void waitText() {
		this.waitText.setFill(Color.RED);
		this.waitText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 40));
		pane.getChildren().add(this.waitText);
	}
	/**
	 * 查询所有在线玩家账号名，并显示在棋盘上
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日上午11:07:17
	* @param index 分页查询起始索引
	* @param size 每页的数量
	 */
	private void queryAllAccountShowSinfo(int index, int size) {
		// 分页查询所有在线用户,一页显示两个在线用户
		List<Sinfo> list = sinfoService.queryAllByLimit(index, size);
		// 移除棋盘上的在线玩家
		if (!textList.isEmpty()) {
			pane.getChildren().removeAll(this.textList);
			textList.clear();
		}
		this.showSinfo(list);
	}
	
	/**
	 * 显示在线玩家的账号名在棋盘上
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午8:22:13
	* @param list 所有在线玩家的集合
	 */
	private void showSinfo(List<Sinfo> list) {
		
		int count = 0;
		for (Sinfo sinfo : list) {		
			Text text = new Text(770, 160 + (count++ * 40), 
					sinfo.getAccount() + "(" + (sinfo.getStatus() == 1 ? "空闲" : "忙碌") +  ")");
			text.setFill(Color.MAGENTA);
			text.setFont(Font.font("宋体", 
					 FontPosture.REGULAR, 25));
			// 加入文本集合
			this.textList.add(text);
			pane.getChildren().add(text);
			
			//给每个显示的玩家名绑定点击发送对战请求事件
			text.setOnMouseClicked(e -> {
				// 游戏已经开始
				if (!gameOver) {
					System.out.println("游戏已经开始，无法发送对战请求！");
					return;
				}
				// 鼠标点击玩家账号名后，从数据库中重新查询玩家当前在线状态，
				// 防止别的玩家棋盘展示的是之前的在线玩家，状态可能不对
				Sinfo nowSinfo = sinfoService.queryIPByAccount(sinfo.getAccount());
				// 已经发送过对战请求
				if (isSend) {
					Alert alert = new Alert(AlertType.INFORMATION,"已经发送过对战请求，请耐心等待！");
					alert.initOwner(this);
					alert.show();
					return;
				}
				// 对方正在对战中
				if (nowSinfo.getStatus() == 2) {
					Alert alert = new Alert(AlertType.INFORMATION, sinfo.getAccount() + "正在激战中，请换个对手！");
					alert.initOwner(this);
					alert.show();
					return;
				}
				// 对方离线
				if (nowSinfo.getStatus() == 0) {
					Alert alert = new Alert(AlertType.INFORMATION, sinfo.getAccount() + "已经离线，请换个对手！");
					alert.initOwner(this);
					alert.show();
					return;
				}
				// 不能和自己对战
				if (sinfo.getAccount().equals(Global.account)) {
					Alert alert = new Alert(AlertType.INFORMATION, "你个憨憨，点自己干吉尔！");
					alert.initOwner(this);
					alert.show();
					return;
				}
				
				// 获取对手ip
				Global.oppoIP = sinfoService.queryIPByAccount(sinfo.getAccount()).getAddress();
				// 取反
				this.isSend = !isSend;
				// 给对手发送对战请求消息
				GameRequestMeaasge gameRequestMeaasge = new GameRequestMeaasge();
				gameRequestMeaasge.setAccount(Global.account);
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_REQUEST);
				this.waitText.setText("已给" +  sinfo.getAccount() + "发送对战请求，请耐心等待……");
				
				NetUtils.sendMessage(gameRequestMeaasge, Global.oppoIP);
				// 请求对战后由于未知原因会停止背景音乐，需要继续播放背景音乐
				mediaPlayer.play();
				musicButton.setText("暂停音乐");
				playMusic = !playMusic;
			});
		}
		
	}
	/**
	 * 显示在线玩家文本
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午4:47:29
	 */
	private void addPlay() {
		userPlaytext = new Text(670, 70, "在线玩家,点击名字请求对战");
		userPlaytext.setFill(Color.AQUA);
		userPlaytext.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));
		pane.getChildren().add(userPlaytext);
		
	}
	
	/**
	 * 棋盘划线
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午11:03:39
	 */
	private void drawLine() {
		// 在棋盘上绘制出15条横线
		for (int i = 0; i < SIZE; i++) {
			// 创建线对象，起始横线的左右坐标为(50, 50) (610, 50)
			Line line = new Line(50, 50 + LINE_SPACING * i , 610, 
					50 + LINE_SPACING * i);
			// 设置线段的颜色
			line.setStroke(Color.RED);
			pane.getChildren().add(line);
		}
		
		// 在棋盘上绘制出15条纵线
		for (int i = 0; i < SIZE; i++) {
			// 创建线对象，起始纵线的左右坐标为(50, 50) (50, 610)
			Line line = new Line(50  + LINE_SPACING * i, 50 ,
					50  + LINE_SPACING * i, 610);
			// 设置线段的颜色
			line.setStroke(Color.RED);
			pane.getChildren().add(line);
		}
		
	}
	/**
	 * 在棋盘上画出五个定位点
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午11:04:10
	 */
	private void drawFivePoint() {
		Circle circle1 = new Circle(170, 170, 4);
		circle1.setFill(Color.RED);
		Circle circle2 = new Circle(490, 170, 4);
		circle2.setFill(Color.RED);
		Circle circle3 = new Circle(330, 330, 4);
		circle3.setFill(Color.RED);
		Circle circle4 = new Circle(170, 490, 4);
		circle4.setFill(Color.RED);
		Circle circle5 = new Circle(490, 490, 4);
		circle5.setFill(Color.RED);
		pane.getChildren().addAll(circle1, circle2, circle3, circle4, circle5);
	}
	/**
	 * 在自己的棋盘上显示对手下的棋子
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:49:38
	* @param message
	 */
	private void chessMessage(Message message) {
		// 播放落子声
		this.soundMoveLater();
		// 当前线程睡眠20毫秒
		this.stopThread();
		
		// 局时倒计时开始
		this.gameTimeline.play();
		// 步时倒计时开始
		this.stepTimeline.play();
		// 去除上一个红色的标志
		if (!chessList.isEmpty()) {
			pane.getChildren().remove(redCircle);
			
		}
		// 设置当前落子后文本面棋子的颜色
		nowChess.setFill(isBlack ? Color.ALICEBLUE: Color.BLACK);
		
		// 对手下完棋后，自己可以下棋了
		this.isPlay = true;
		ChessMessage chessMessage = (ChessMessage)message;
		// 获取对手棋子的坐标和颜色
		int x = chessMessage.getX();
		int y = chessMessage.getY();
		Color nowColor = chessMessage.getBlackOrWhite() == 1 ? Color.BLACK : Color.ALICEBLUE;

		Circle circle = new Circle(x * 40 + 50,
				y * 40 + 50,CHESS_RADIUS);

		circle.setFill(nowColor);
		// 如果对手是黑棋，那么自己就是白棋
		if (chessMessage.getBlackOrWhite() == 1) {
			this.blackOrWhite = 0;
			this.color = Color.ALICEBLUE;
		}

		// 更新当前棋子信息
		isBlack = !isBlack;

		// 记录对手下的棋的信息
		arr[x][y] = true;
		colors[x][y] = nowColor;
		Chess chess = new Chess(x, y, nowColor);
		chessList.add(chess);
		pane.getChildren().add(circle);

		// 标志落点的x坐标
		redCircle.setCenterX(x * 40 + 50);
		// 标志落点的y坐标
		redCircle.setCenterY(y * 40 + 50);
		// 设置为红色
		redCircle.setFill(Color.RED);
		// 把标志加入到棋盘中
		pane.getChildren().add(redCircle);
		
		// 对手五连，结束游戏
		if (this.isWin(chess)) {
    		// 局时倒计时停止
    		gameTimeline.pause();
    		// 步时倒计时停止
    		stepTimeline.pause();
			
			// 清除对手ip
			Global.oppoIP = null;
			// 清除棋盘上双方VS的文字和后面的棋子
			// 重新添加刷新、上一页和下一页按钮
			this.removeEndTextAndCircle();
			
			this.gameOverReminder("失败");
		}
		// 棋盘已满
		if (chessList.size() == SIZE * SIZE) {
			// 提示框
			this.chessFullReminder();
		}
	}
	
	/**
	 * 处理悔棋消息 
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:51:57
	* @param message
	 */
	private void replayMessage(Message message) {
		ReplayMessage replayMessage = (ReplayMessage)message;
		// 悔棋请求
		if (replayMessage.getFlag() == ReplayMessage.REPLAY_REQUEST) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "对方请求悔棋，是否同意？",
					new ButtonType("拒绝",  ButtonData.NO), 
					new ButtonType("同意",  ButtonData.YES));
			alert.initOwner(this);
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonData.YES) {
	    		// 局时倒计时停止
	    		gameTimeline.pause();
	    		// 暂停并重置步时
	    		stepTimeline.pause();
	    		this.stepTimeText.setText("步时 60");
	    		this.stepTimeNum = 60;
	    		
				// 我方同意悔棋，将ReplayMessage中的flag改成响应的结果
				replayMessage.setFlag(ReplayMessage.REPLAY_AGRRE);
				// 处理我方同意后自己棋盘的悔棋逻辑
				// 悔棋后，将其在arr中对应位置的值置为0,color中的颜色置空
				int lastIndex = chessList.size() - 1;
				Chess chess = chessList.get(lastIndex);
				arr[chess.getX()][chess.getY()] = false;
				colors[chess.getX()][chess.getY()] = null;
				// 删除棋盘上最后加上的一个棋子和颜色标志
				pane.getChildren().remove(redCircle);
				// 移除最后一个棋子
				for (int i = pane.getChildren().size() - 1; i >= 0; i--) {
					if (pane.getChildren().get(i) instanceof Circle) {
						pane.getChildren().remove(i);
						break;
					}
				}
				chessList.remove(lastIndex);
				isBlack = !isBlack;
				isPlay = !isPlay;
				
				
				if (!chessList.isEmpty()) {
					// 悔棋后，最后一个棋子再加上红色标志
					Chess chess1 = chessList.get(chessList.size() - 1);

					// 标志落点的x坐标
					redCircle.setCenterX(chess1.getX() * 40 + 50);
					// 标志落点的y坐标
					redCircle.setCenterY(chess1.getY() * 40 + 50);
					// 设置为红色
					redCircle.setFill(Color.RED);
					// 把标志加入到棋盘中
					pane.getChildren().add(redCircle);
					
				}
				
			} else {
				// 拒绝悔棋，将ReplayMessage中的flag改成响应的结果
				replayMessage.setFlag(ReplayMessage.REPLAY_REFUSE);
			}
        	// 发送消息给对手
        	NetUtils.sendMessage(replayMessage, Global.oppoIP);
		
		// 对手同意悔棋
		} else if (replayMessage.getFlag() == ReplayMessage.REPLAY_AGRRE) {
    		// 局时倒计时开始
    		gameTimeline.play();
    		// 步时倒计时开始
    		stepTimeline.play();;
			// 移除悔棋请求提示
			this.waitText.setText("");
			// 将replay 设置为false 即下次不能再悔棋了
			replay = false;
			
			// 处理对方同意后自己棋盘的悔棋逻辑
			// 悔棋后，将其在arr中对应位置的值置为0,color中的颜色置空
			int lastIndex = chessList.size() - 1;
			Chess chess = chessList.get(lastIndex);
			arr[chess.getX()][chess.getY()] = false;
			colors[chess.getX()][chess.getY()] = null;
			// 删除棋盘上最后加上的一个棋子和颜色标志
			pane.getChildren().remove(redCircle);
			// 移除最后一个棋子
			for (int i = pane.getChildren().size() - 1; i >= 0; i--) {
				if (pane.getChildren().get(i) instanceof Circle) {
					pane.getChildren().remove(i);
					break;
				}
			}
			chessList.remove(lastIndex);
			isBlack = !isBlack;
			isPlay = !isPlay;
			
			
			if (!chessList.isEmpty()) {
				// 悔棋后，最后一个棋子再加上红色标志
				Chess chess1 = chessList.get(chessList.size() - 1);
				// 标志落点的x坐标
				redCircle.setCenterX(chess1.getX() * 40 + 50);
				// 标志落点的y坐标
				redCircle.setCenterY(chess1.getY() * 40 + 50);
				// 设置为红色
				redCircle.setFill(Color.RED);
				// 把标志加入到棋盘中
				pane.getChildren().add(redCircle);
				
			}
			
		// 对方拒绝悔棋
		} else if (replayMessage.getFlag() == ReplayMessage.REPLAY_REFUSE) {
			// 清除悔棋请求、对战请求、新局请求后的提示文本
			waitText.setText("");
			// 移除悔棋请求提示
			this.waitText.setText("");
			Alert alert = new Alert(AlertType.INFORMATION, "对方不同意悔棋！");
			alert.initOwner(this);
			alert.show();
		}
	}
	
	/**
	 * 处理新局消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:53:40
	* @param message
	 */
	private void newGameMessage(Message message) {
		NewGameMessage newGameMessage = (NewGameMessage)message;
		if (newGameMessage.getState() == NewGameMessage.REQUEST) {
			if (this.isAccept) {
				// 我方正在接受对战请求，拒绝
				newGameMessage.setState(NewGameMessage.NO);
				newGameMessage.setAccount(Global.account);
            	// 发送消息给对手
            	NetUtils.sendMessage(newGameMessage, sinfoService.queryIPByAccount(newGameMessage.getAccount()).getAddress());
				return;
			}
			this.newGame(newGameMessage);
		// 对手同意新局
		} else if (newGameMessage.getState() == NewGameMessage.OK){
			
			this.startNew(newGameMessage.getAccount());
		// 对手拒绝新局
		} else if (newGameMessage.getState() == NewGameMessage.NO) {
			// 清除悔棋请求、对战请求、新局请求后的提示文本
			waitText.setText("");
			Alert alert = new Alert(AlertType.INFORMATION, newGameMessage.getAccount() + "不想和你再来一局！");
			alert.initOwner(this);
			alert.show();
		}
	}
	
	/**
	 * 获取对手战绩的消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:54:58
	* @param message
	 */
	private void rivalRecordMessage(Message message) {
		RivalRecordMessage recordMessage = (RivalRecordMessage) message;
		
		// 请求消息
		if (recordMessage.getRivalRecord() == RivalRecordMessage.REQUEST) {
			recordMessage.setRivalRecord(RivalRecordMessage.RESPONSE);
			// 从数据库中获取用户信息
		    User user =  userService.queryUserByAccount(Global.account);
		    // 将用户信息保存到用户消息类中
		    recordMessage.setUser(user);
		    
		    // 将用户信息响应给请求端
		    NetUtils.sendMessage(recordMessage, Global.temporaryOppoIP);
		// 响应消息
		} else if (recordMessage.getRivalRecord() == RivalRecordMessage.RESPONSE) {
			// 将对手的响应的用户消息保存到对手战绩的页面类中，方便展示对手战绩时使用
			RivalRecord.recordMessage = recordMessage;
			// 显示对手战绩的界面
			RivalRecord record = new RivalRecord();
			record.show();
		}
	}
	
	/**
	 * 对局结束消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:55:56
	* @param message
	 */
	private void resultMessage(Message message) {
		ResultMessage resultMessage = (ResultMessage)message;
		// 清除对手ip
		Global.oppoIP = null;
		
		Record record = new Record();
		// isLose()为true表示是认输后的消息，为false表示是正常五连后的消息
		// 黑胜或黑平
		if (resultMessage.getResult() == ResultMessage.BLACK_WIN || 
				resultMessage.getResult() == ResultMessage.BLACK_DRAW) {
			record.setBlack(resultMessage.isLose() ? Global.account : resultMessage.getAccount());
			record.setWhite(resultMessage.isLose() ? resultMessage.getAccount() : Global.account);
		// 白胜或白平	
		} else {
			record.setWhite(resultMessage.isLose() ? Global.account : resultMessage.getAccount());
			record.setBlack(resultMessage.isLose() ? resultMessage.getAccount() : Global.account);
		}

		// 记录结果
		record.setResult(resultMessage.getResult() == 4 ? 3 : resultMessage.getResult());
		// 记录对局结束时间
		record.setChessTime(new Timestamp(System.currentTimeMillis()));
		
		int result = record.getResult();
		Connection conn = null;
		try {
			// 获取连接
			conn = JdbcUtils.getConnection();
			// 设置事务手动提交
			JdbcUtils.disableAutocommit(conn);
			// 将对战记录保存到数据库
			recordService.saveRecord(record);
			// 更新对战双方的战绩
			// 黑胜
			if (result == ResultMessage.BLACK_WIN) {
				// 加一分 、总局数加一局、胜局加一局
				String blackAccount = resultMessage.isLose() ? Global.account : resultMessage.getAccount();
				userService.saveWinChessByAccount(blackAccount);
				// 减一分、总局数加一句、败局加一局
				String whiteAccount = resultMessage.isLose() ? resultMessage.getAccount() : Global.account;
				userService.saveLostChessByAccount(whiteAccount);
				
			// 白胜
			} else if (result == ResultMessage.WHITE_WIN) {
				// 加一分 、总局数加一局、胜局加一局
				String whiteAccount = resultMessage.isLose() ? Global.account : resultMessage.getAccount();
				userService.saveWinChessByAccount(whiteAccount);
				
				// 减一分、总局数加一句、败局加一局
				String blackAccount = resultMessage.isLose() ? resultMessage.getAccount() : Global.account;
				
				userService.saveLostChessByAccount(blackAccount);
			// 平局
			} else {
				// 总局数加一局、和棋数加一局
				String whiteAccount = resultMessage.isLose() ? Global.account : resultMessage.getAccount();
				userService.saveDrawChessByAccount(whiteAccount);
				
				String blackAccount = resultMessage.isLose() ? resultMessage.getAccount() : Global.account;
				userService.saveDrawChessByAccount(blackAccount);
			}
			// 提交事务
			JdbcUtils.commit(conn);
		} catch (Exception e) {
			// 回滚事务
			JdbcUtils.rollback(conn);
		} finally {
			if (conn != null) {
				// 关闭连接
				JdbcUtils.close(conn);
			}
		}
	}
	
	/**
	 * 对战请求消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:57:23
	* @param message
	 */
	private void gameRequestMeaasge(Message message) {
		GameRequestMeaasge gameRequestMeaasge = (GameRequestMeaasge)message;

		// 如果是请求消息
		if (gameRequestMeaasge.getRequestType() == GameRequestMeaasge.GAME_REQUEST) {
			if (this.isAccept) {
				// 我方正在接受对战请求
				// 不同意
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_REFUSE);
			    // 发送消息
            	NetUtils.sendMessage(gameRequestMeaasge, sinfoService.queryIPByAccount(gameRequestMeaasge.getAccount()).getAddress());
				return;
			}
			// 已接受对战请求
			this.isAccept = true;
			// 更新对手ip
			Global.oppoIP = sinfoService.queryIPByAccount(gameRequestMeaasge.getAccount()).getAddress();
			Alert alert = new Alert(AlertType.CONFIRMATION, gameRequestMeaasge.getAccount() + "请求一战，是否给个面子？",
					new ButtonType("拒绝",  ButtonData.NO), 
					new ButtonType("同意",  ButtonData.YES));
			alert.initOwner(this);
			
			Optional<ButtonType> button = alert.showAndWait();
			// 如果同意
			if (button.get().getButtonData() == ButtonData.YES) {
				this.stopThread();
				// 告诉原先对手(刚刚下过棋的对手)，让他死心(清除临时对手ip)，
				if (Global.temporaryOppoIP != null) {
				    // 发送消息
	            	NetUtils.sendMessage(new EscapeMessage(), Global.temporaryOppoIP);
				}
				// 更新临时对手ip
				Global.temporaryOppoIP = Global.oppoIP;
				
				// 随机选择棋子颜色
				this.selectColor();
				
				// 游戏初始化
				this.startNew(gameRequestMeaasge.getAccount());
				
				// 将自己的账号放入消息类中
				gameRequestMeaasge.setAccount(Global.account);
				// 发送消息
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_AGRRE);
            	NetUtils.sendMessage(gameRequestMeaasge, Global.oppoIP);
				
			} else {
				// 更新对手ip
				Global.oppoIP = sinfoService.queryIPByAccount(gameRequestMeaasge.getAccount()).getAddress();
				// 拒绝后变为没接受对战请求
				this.isAccept = false;
				// 如果不同意
				gameRequestMeaasge.setRequestType(GameRequestMeaasge.GAME_REFUSE);
				
			    // 发送消息
            	NetUtils.sendMessage(gameRequestMeaasge, Global.oppoIP);
				// 移除对手ip
				Global.oppoIP = null;
			}
			
		// 同意对战请求
		} else if (gameRequestMeaasge.getRequestType() == GameRequestMeaasge.GAME_AGRRE) {
			this.stopThread();
			// 告诉原先对手(刚刚下过棋的对手)，让他死心(清除临时对手ip)
			if (Global.temporaryOppoIP != null) {
			    // 发送消息
            	NetUtils.sendMessage(new EscapeMessage(), Global.temporaryOppoIP);
			}
			// 更新临时对手ip
			Global.temporaryOppoIP = Global.oppoIP;

			// 初始化数据
			this.startNew(gameRequestMeaasge.getAccount());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(gameRequestMeaasge.getAccount() + "同意对战，开始游戏！");
			alert.initOwner(this);
			alert.show();
		
		// 拒绝对战请求
		} else if (gameRequestMeaasge.getRequestType() == GameRequestMeaasge.GAME_REFUSE) {
			
			// 拒绝后，改回请求对战状态
			this.isSend = !isSend;
			// 清除对手ip
			Global.oppoIP = null;
			// 移除
			this.waitText.setText("");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("对方不给面子，拒绝了你的请求！");
			alert.initOwner(this);
			alert.show();
		}
	}
	
	/**
	 * 逃跑消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午9:59:18
	* @param message
	 */
	private void escapeMessage(Message message) {
		// 对手逃跑，可以接受新的对战请求了
		this.isAccept = false;
		// 如果对局已经结束
		if (this.gameOver) {
			// 清除临时对手ip
			Global.temporaryOppoIP = null;
		} else {
			// 清除临时对手ip
			Global.temporaryOppoIP = null;
			// 对手对局中逃跑后的处理逻辑
			this.specialWin("对手逃跑了");
		}
	}
	
	/**
	 * 选择棋子颜色消息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月13日上午10:00:45
	* @param message
	 */
	private void selectChessColorMessage(Message message) {
		SelectChessColorMessage selectChessColor = (SelectChessColorMessage)message;
		
		if (selectChessColor.getColor() == SelectChessColorMessage.WHITE) {
			// 我方执白棋
			this.blackOrWhite = 0;
			this.color = Color.ALICEBLUE;
			this.isPlay = false;
		} else if (selectChessColor.getColor() == SelectChessColorMessage.BLACK){
    		// 局时倒计时开始
    		gameTimeline.play();
    		// 步时倒计时开始
    		stepTimeline.play();
			// 我方执黑棋
			this.blackOrWhite = 1;
			this.color = Color.BLACK;
			this.isPlay = true;
		}
	}
	/**
	 * 处理网络通信的消息，实现棋盘上棋子的同步改变
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午10:46:04
	* @param message
	 */
	public void upDateUI(Message message) {
		// 在自己的棋盘上显示对手下的棋子
		if (message instanceof ChessMessage) {
			this.chessMessage(message);
		}
		// 如果消息是悔棋消息
		else if (message instanceof ReplayMessage) {
			this.replayMessage(message);
		}
		// 如果是新局消息
		else if (message instanceof NewGameMessage) {
			this.newGameMessage(message);
		}
		
		// 如果是获取对手战绩的消息
		else if (message instanceof RivalRecordMessage) {
			this.rivalRecordMessage(message);
		}
		
		// 如果是对局结束的消息
		else if (message instanceof ResultMessage) {
			this.resultMessage(message);
		}
		// 如果是对战请求消息
		else if (message instanceof GameRequestMeaasge) {
			this.gameRequestMeaasge(message);
		}
		
		// 如果是逃跑消息
		else if (message instanceof EscapeMessage) {
			this.escapeMessage(message);
		}
		
		// 如果是聊天消息
		else if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			textArea.appendText(textMessage.getAccount() + "：" + textMessage.getTextMessage() + "\n");
		}
		
		// 如果是选择棋子颜色消息
		else if (message instanceof SelectChessColorMessage) {
			this.selectChessColorMessage(message);
		}
		
		// 如果是认输消息
		else if (message instanceof GiveUpMessage) {
			// 对手认输后的处理逻辑
			this.specialWin("对手认输了");
		}
		
		// 如果是倒计时归 0 的消息
		else if (message instanceof CountdownMessage) {
			// 对手倒计时归 0 后的处理逻辑
			this.specialWin("对手超时了");
		}
	}
	/**
	 * 特殊的胜利后的处理逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日下午5:58:54
	 */
	private void specialWin(String winMenthod) {
		// 局时倒计时暂停
		this.gameTimeline.pause();
		// 步时倒计时暂停
		this.stepTimeline.pause();
		// 改为空闲状态
		sinfoService.updateStatusByAccount(Global.account, 1);
		// 清除棋盘上双方VS的文字和后面的棋子
		// 重新添加刷新、上一页和下一页按钮
		this.removeEndTextAndCircle();
		// 游戏结束
		this.gameOver = true;
		// 对手认输，可以发送新的对战请求了
		this.isSend = false;
		// 对手认输，可以接受新的对战请求了
		this.isAccept = false;
		// 清除对手ip
		Global.oppoIP = null;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(winMenthod + "，恭喜你赢了！积分 + 1");
		alert.initOwner(this);
		alert.show();
	}
	/**
	 * 特殊的失败后的处理逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月7日下午6:38:07
	 */
	private void specialLose() {
		// 局时倒计时暂停
		this.gameTimeline.pause();
		// 步时倒计时暂停
		this.stepTimeline.pause();
		// 改为空闲状态
		sinfoService.updateStatusByAccount(Global.account, 1);
		// 清除棋盘上双方VS的文字和后面的棋子
		// 重新添加刷新、上一页和下一页按钮
		this.removeEndTextAndCircle();
		// 游戏结束
		this.gameOver = true;
		// 对手认输，可以发送新的对战请求了
		this.isSend = false;
		// 对手认输，可以接受新的对战请求了
		this.isAccept = false;
		// 清除对手ip
		Global.oppoIP = null;
		Alert loseAlert = new Alert(AlertType.INFORMATION);
		loseAlert.setContentText("失败！积分 - 1");
		loseAlert.initOwner(this);
		loseAlert.show();
	}
	
	/**
	 * 清除棋盘上双方VS的文字和后面的棋子
	 * 重新添加刷新、上一页和下一页按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月1日下午2:36:50
	 */
	private void removeEndTextAndCircle() {
		// 清除棋盘上双方VS的文字和后面的棋子
		pane.getChildren().removeAll(startTextList);
		
		pane.getChildren().remove(myAccountCircle);
		pane.getChildren().remove(rivalAccountCircle);
		pane.getChildren().remove(nowChess);
		
		// 显示在线玩家文本在棋盘上
		this.addPlay();
		
		// 重新添加刷新、上一页和下一页按钮
		// 刷新在线玩家按钮
		this.refreshButton();
		
		// 在线玩家上一页按钮
		this.prevPageButton();
		
		// 在线玩家下一页按钮
		this.nextPageButton();
	}
	// 去除在线棋盘上玩家信息，以及相关标题按钮
	private void removeUserInfo() {
		// 移除棋盘上的在线玩家
		if (!textList.isEmpty()) {
			pane.getChildren().removeAll(this.textList);
			textList.clear();
		}
		// 移除在线玩家提示文本
		pane.getChildren().remove(userPlaytext);
		
		// 移除 刷新、上一页、下一页按钮
		pane.getChildren().remove(refreshButton);
		pane.getChildren().remove(prevPageButton);
		pane.getChildren().remove(nextPageButton);
		
	}
	// 新局
	private void newGame(NewGameMessage newGameMessage) {
		// 已接受对战请求
		this.isAccept = true;
		Alert alert = new Alert(AlertType.CONFIRMATION, "对方请求再战一盘，是否同意？",
				new ButtonType("拒绝",  ButtonData.NO), 
				new ButtonType("同意",  ButtonData.YES));
		alert.initOwner(this);
		Optional<ButtonType> button = alert.showAndWait();
		if (button.get().getButtonData() == ButtonData.YES) {
			
			// 随机选择棋子颜色
			this.selectColor();
			// 新局初始化
			this.startNew(newGameMessage.getAccount());
			// 确认新局后，发送消息通知对手初始化
			newGameMessage.setState(NewGameMessage.OK);
			newGameMessage.setAccount(Global.account);
		    // 发送消息
        	NetUtils.sendMessage(newGameMessage, Global.temporaryOppoIP);
		} else {
			// 拒绝后变为没接受对战请求
			this.isAccept = false;
			// 不同意后，发送消息通知对手
			newGameMessage.setState(NewGameMessage.NO);
			newGameMessage.setAccount(Global.account);
		    // 发送消息
        	NetUtils.sendMessage(newGameMessage, Global.temporaryOppoIP);
		}
	}
	// 随机选择棋子颜色
	public void selectColor() {
		// 随机0 或  1
		int randomNum = (int)(Math.random() * 2);
		// 1就是黑棋, 发送消息通知对手：你是白棋
		if (randomNum == 1) {
    		// 局时倒计时开始
    		gameTimeline.play();
    		// 步时倒计时开始
    		stepTimeline.play();
			this.blackOrWhite = 1;
			this.color = Color.BLACK;
			this.isPlay = true;
			SelectChessColorMessage selectChessColor = new SelectChessColorMessage();
			selectChessColor.setColor(SelectChessColorMessage.WHITE);
		    // 发送消息
        	NetUtils.sendMessage(selectChessColor, Global.oppoIP);
		// 0就是白棋, 发送消息通知对手：你是黑棋
		} else {
			this.blackOrWhite = 0;
			this.color = Color.ALICEBLUE;
			this.isPlay = false;
			SelectChessColorMessage selectChessColor = new SelectChessColorMessage();
			selectChessColor.setColor(SelectChessColorMessage.BLACK);
		    // 发送消息
        	NetUtils.sendMessage(selectChessColor, Global.oppoIP);
			
		}
	}
	
	/**
	 * 落子
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午11:08:15
	* @param x
	* @param y
	 */
	private void piece(int x, int y) {
		if (chessList.size() == SIZE * SIZE) {
			System.out.println("棋盘已满，游戏结束");
			// 平局
			// 给对手发送消息，让他更新数据库信息
			ResultMessage resultMessage = new ResultMessage();
			// 根据当前用户的棋子的颜色设置消息类结果属性
			resultMessage.setResult(Color.BLACK.equals(this.color) ?
					ResultMessage.BLACK_DRAW : ResultMessage.WHITE_DRAW);
			
			// 显示提示框
			chessFullReminder();
			return;
		}
		
		// 判断下子是否重复
		if (arr[x][y]) {
			System.out.println("同一坐标重复落子，无效！");
			return;
		}
		// 播放落子声
		this.soundMoveLater();
		// 当前线程睡眠20毫秒
		this.stopThread();
		
		// 局时倒计时暂停
		gameTimeline.pause();
		// 暂停并重置步时
		stepTimeline.pause();
		this.stepTimeText.setText("步时 60");
		this.stepTimeNum = 60;
		
		// 去除上一个红色的标志
		if (!chessList.isEmpty()) {
			pane.getChildren().remove(redCircle);
			
		}
		
		// 当前落子文本后面棋子的颜色
		nowChess.setFill(isBlack ? Color.ALICEBLUE: Color.BLACK);
		

		// 落完一子后，要先等对手落子，才能继续落子
		isPlay = !isPlay;
		arr[x][y] = true;
		int tempX = x * LINE_SPACING + 50;
		int tempY = y * LINE_SPACING + 50;
		// 绘制棋子
		Circle circle = new Circle();
		// 棋子落点的x坐标
		circle.setCenterX(tempX);
		// 棋子落点的y坐标
		circle.setCenterY(tempY);
		
		// 设置棋子的颜色
		circle.setFill(this.color);
		// 将棋子的颜色记录到数组colors 中
		colors[x][y] = this.color;
		
		// 设置棋子的半径
		circle.setRadius(CHESS_RADIUS);
		
		// 把棋子加入到棋盘中
		pane.getChildren().add(circle);

		
		// 标志落点的x坐标
		redCircle.setCenterX(tempX);
		// 标志落点的y坐标
		redCircle.setCenterY(tempY);
		// 设置为红色
		redCircle.setFill(Color.RED);
		// 把标志加入到棋盘中
		pane.getChildren().add(redCircle);
		
		// 将棋子的信息保存到数组中
		Chess chess = new Chess(x, y, this.color);
		chessList.add(chess);
		// 更换棋子颜色
		isBlack = !isBlack;
    	// 给服务器发送消息
    	NetUtils.sendMessage(new ChessMessage(x, y,this.blackOrWhite), Global.oppoIP);
		// 出现五连子，结束游戏
		if (isWin(chess)) {
    		// 局时倒计时停止
    		gameTimeline.pause();
    		// 步时倒计时停止
    		stepTimeline.pause();
			
			// 出现五连，给对手发送消息，对手将更新数据库
			ResultMessage resultMessage = new ResultMessage();
			// 设置为不是认输
			resultMessage.setLose(false);
			resultMessage.setAccount(Global.account);
			resultMessage.setResult(Color.BLACK.equals(this.color) ? 
					ResultMessage.BLACK_WIN : ResultMessage.WHITE_WIN);
			
        	// 发送对战结果消息
        	NetUtils.sendMessage(resultMessage, Global.oppoIP);
			
			// 清除对手ip
			Global.oppoIP = null;

			// 清除棋盘上双方VS的文字和后面的棋子
			// 重新添加刷新、上一页和下一页按钮
			this.removeEndTextAndCircle();
			
			// 显示对局结束弹窗
			this.gameOverReminder("胜利");
			
		}
	}
	
	// 棋盘下满棋子的提示框
	private void chessFullReminder() {
		gameOver = true;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(this);
		alert.setTitle("对战结果");
		alert.setContentText("平局");
		alert.show();
	}
	
	// 游戏结束提示框
	private void gameOverReminder(String result) {
		// 改为离线状态
		sinfoService.updateStatusByAccount(Global.account, 1);
		// 游戏结束，可以重新向任意对手发起对战请求
		this.isSend = false;
		this.gameOver = true;
		// 游戏结束，可以被玩家申请对战
		this.isAccept = false;
		// 刷新在线玩家
		this.refresh();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(this);
		alert.setTitle("对战结果");
		alert.setContentText("胜利".equals(result) ? "胜利，积分 + 1" : "失败，积分 - 1");
		alert.show();
	}
	/**
	 * 是否出现五连子
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午11:30:27
	* @param chess
	* @return
	 */
	private boolean isWin(Chess chess) {
		return Logic.isWin(chessList, chess, arr, colors);
	}
	/**
	 * 添加按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月22日下午11:46:33
	 */
	private void addButton() {
		// 新局按钮
		this.againButton();
		
		// 悔棋按钮
		this.withDrawButton();
		
		// 保存棋谱按钮
		this.saveChessBookButton();
		
		// 打开棋谱按钮
		this.openChessBookButton();
		// 退出按钮
		this.exitButton();
		
		// 查看自己的信息
		this.myRecordButton();
		
		// 查看对手信息
		this.rivalRecordButton();
		
		// 刷新在线玩家按钮
		this.refreshButton();
		
		// 发送消息按钮
		this.messageButton();
		
		// 在线玩家上一页按钮
		this.prevPageButton();
		
		// 在线玩家下一页按钮
		this.nextPageButton();
		
		// 其它下拉按钮
		this.otherButton();
			
	}
	
	/**
	 * 下拉按钮，包含了一些其它按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月6日下午10:20:20
	 */
	private void otherButton() {
		// 创建下拉按钮
		menuButton = new SplitMenuButton();
		menuButton.setText("其它按钮");
		menuButton.setPrefSize(90, 40);
		menuButton.setLayoutX(890);
		menuButton.setLayoutY(650);
		
		// 创建下拉按钮中的轮播按钮
		MenuItem menuItemPlayImages = new MenuItem(null, playImagesButton());
		
		// 创建下拉按钮中的认输按钮
		MenuItem menuItemgiveUp= new MenuItem(null, giveUpButton());
		
		// 创建暂停播放背景音乐的按钮
		MenuItem menuMusic= new MenuItem(null, musicButton());
		
		// 创建求助小棋仙按钮
		MenuItem menuRobot= new MenuItem(null, helpRobotButton());
		
		// 将按钮放入下拉按钮中
		menuButton.getItems().addAll(menuRobot, menuItemPlayImages, menuMusic, menuItemgiveUp);
		pane.getChildren().add(menuButton);
		
	}
	/**
	 * 求助小棋仙按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月12日下午12:10:08
	 */
	private Button helpRobotButton() {
		Button helpRobotButton = new Button("求助小棋仙");
		helpRobotButton.setPrefSize(80, 40);
		helpRobotButton.setOnAction(e -> {
			// 求助小棋仙按钮逻辑实现
			this.helpRobot();
		});
		
		return helpRobotButton;
	}
	
	// 求助小棋仙按钮逻辑实现
	private void helpRobot() {
		if (gameOver || !isPlay) {
			return;
		}
		if (robotTime == 0) {
			Alert alert1 = new Alert(AlertType.INFORMATION, "三次已过，莫挨老子！");
			alert1.initOwner(this);
			alert1.show();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION,"你还有 "+ robotTime-- + " 次求助机会，" + "确定求助小棋仙吗？",
				new ButtonType("取消",  ButtonData.NO), 
				new ButtonType("确定",  ButtonData.YES));
		alert.initOwner(this);
		Optional<ButtonType> button = alert.showAndWait();
		if (button.get().getButtonData() == ButtonData.YES) {
			if (chessList.isEmpty()) {
				this.piece(7, 7);
			} else {
				Chess chess = RobotPlay.getChess(colors, arr, color, SIZE);
				// 将机器人的落子画到棋盘上
				this.piece(chess.getX(), chess.getY());
			}
		}
	}
	// 控制背景音乐的播放和暂停
	private Button musicButton() {
		musicButton = new Button("暂停音乐");
		musicButton.setPrefSize(80, 40);
		
		// 暂停播放背景音乐的逻辑实现
		musicButton.setOnAction(e -> {
			if (playMusic) {
				this.mediaPlayer.pause();
				playMusic = !playMusic;
				musicButton.setText("播放音乐");
			} else {
				this.mediaPlayer.play();
				playMusic = !playMusic;
				musicButton.setText("暂停音乐");
			}
		});
		return musicButton;
	}
	/**
	 * 认输按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月6日下午11:00:28
	* @return
	 */
	private Button giveUpButton() {
		Button giveUpButton = new Button("认     输");
		giveUpButton.setPrefSize(80, 40);
		
		// 认输按钮逻辑实现
		giveUpButton.setOnAction(e -> {
			if (gameOver) {
				return;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION, "你确定认输吗？",
					new ButtonType("取消",  ButtonData.NO), 
					new ButtonType("确定",  ButtonData.YES));
			alert.initOwner(this);
			Optional<ButtonType> button = alert.showAndWait();
			if (button.get().getButtonData() == ButtonData.YES) {
				NetUtils.sendMessage(new GiveUpMessage(), Global.oppoIP);
				
				// 认输，给对手发送消息，对手将更新数据库
				ResultMessage resultMessage = new ResultMessage();
				resultMessage.setAccount(Global.account);
				resultMessage.setResult(Color.BLACK.equals(this.color) ? 
						ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
				
	        	// 发送对战结果消息
	        	NetUtils.sendMessage(resultMessage, Global.oppoIP);
				
				// 认输后处理逻辑
				this.specialLose();
			}

		});
		
		return giveUpButton;
	}
	/**
	 * 图片轮播按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午6:54:35
	* @param 

	 */
	private Button playImagesButton() {
		Button playImagesButton = new Button("开始轮播");
		playImagesButton.setPrefSize(80, 40);
		
		// 轮播背景图按钮逻辑实现
		playImagesButton.setOnAction(e -> {
			if (playImages) {
				timeline.stop();
				playImagesButton.setText("开始轮播");
			} else {
				timeline.play();
				playImagesButton.setText("暂停轮播");
			}
			playImages = !playImages;
		});
		
		return playImagesButton;
		
	}
	/**
	 * 在线玩家上一页按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午3:33:03
	 */
	private void prevPageButton() {
		prevPageButton = new Button("上一页");
		prevPageButton.setPrefSize(60, 40);
		prevPageButton.setLayoutX(680);
		prevPageButton.setLayoutY(320);
		
		// 将按钮添加到棋盘
		pane.getChildren().add(prevPageButton);
		
		// 上一页按钮逻辑实现
		prevPageButton.setOnAction(e -> {
			this.prevPage();
		});
	}
	
	/**
	 * 上一页按钮逻辑实现
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午3:45:01
	 */
	private void prevPage() {
		if (index == 0) {
			Alert alert = new Alert(AlertType.INFORMATION, "没有上一页了！");
			alert.initOwner(this);
			alert.show();
			return;
		} 
		// 显示第二页的在线玩家
		this.queryAllAccountShowSinfo(--index * USER_SIZE, USER_SIZE);
	}
	
	/**
	 * 在线玩家下一页按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午3:33:37
	 */
	private void nextPageButton() {
		nextPageButton = new Button("下一页");
		nextPageButton.setPrefSize(60, 40);
		nextPageButton.setLayoutX(880);
		nextPageButton.setLayoutY(320);
		
		// 将按钮添加到棋盘
		pane.getChildren().add(nextPageButton);
		
		// 下一页按钮逻辑实现
		nextPageButton.setOnAction(e -> {
			this.nextPage();
		});
	}
	/**
	 * 下一页按钮逻辑实现
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午3:46:38
	 */
	private void nextPage() {
		// 当前在线玩家总个数
		long totalPage = sinfoService.queryAllCount();
		// 最多能分页显示的索引
		int nowIndex = (int)(totalPage - 1) / USER_SIZE;
		if (index == nowIndex) {
			Alert alert = new Alert(AlertType.INFORMATION, "没有下一页了！");
			alert.initOwner(this);
			alert.show();
			return;
		} 
		// 显示第二页的在线玩家
		this.queryAllAccountShowSinfo(++index * USER_SIZE, USER_SIZE);
	}
	
	/**
	 * 点击发送文本消息给对手
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日下午3:12:34
	 */
	private void messageButton() {
		Button messageButton = new Button("发 送");
		messageButton.setLayoutX(850);
		messageButton.setLayoutY(598);
		messageButton.setPrefSize(50, 25);
		// 按钮关联回车键
		messageButton.setDefaultButton(true);
		pane.getChildren().add(messageButton);
		
		/**
		 * 按钮实现逻辑
		 */
		messageButton.setOnAction(e -> {
			this.sendMessage();
		});
	}
	/**
	 * 发送消息实现逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日下午3:44:55
	 */
	private void sendMessage() {
		String message = messageText.getText().trim();
		// 发送后清空输入框
		messageText.setText("");
		// 取出首尾空格，长度大于0 小于等于30 的字符串才能成功发送
		if (message.length() > 0 && message.length() <= 30) {
			
			if ("习近平".equals(message)) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "小比崽子，开门，查水表！！！");
				alert.initOwner(this);
				alert.show();
				return;
			}
			textArea.appendText(Global.account + "：" + message + "\n");
			if (Global.temporaryOppoIP == null) {
				return;
			}
			TextMessage textMessage = new TextMessage();
			textMessage.setTextMessage(message);
			textMessage.setAccount(Global.account);
			
			// 给对手发送消息
			NetUtils.sendMessage(textMessage, Global.temporaryOppoIP);
		} else if (message.length() <= 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "小比崽子，别乱发！！！");
			alert.initOwner(this);
			alert.show();
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION, "小比崽子，发那么多干吉尔！！！");
			alert.initOwner(this);
			alert.show();
		}	
	}
	/**
	 * 刷新在线玩家按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日上午8:31:41
	 */
	private void refreshButton() {
		refreshButton = new Button("刷     新");
		refreshButton.setPrefSize(80, 40);
		refreshButton.setLayoutX(770);
		refreshButton.setLayoutY(90);
		pane.getChildren().add(refreshButton);
		
		// 刷新逻辑
		refreshButton.setOnAction(e -> {
			this.refresh();
		});
	}
	/**
	 * 刷新棋盘上显示的在线玩家
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午4:00:33
	 */
	private void refresh() {
		// 重置分页索引
		index = 0;
		this.queryAllAccountShowSinfo(index * USER_SIZE, USER_SIZE);
	}
	
	/**
	 * 新局按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:06:47
	 */
	private void againButton() {
		Button button = new Button("新      局");
		button.setPrefSize(80, 40);
		button.setLayoutX(50);
		button.setLayoutY(650);
		
		// 将按钮添加到棋盘
		pane.getChildren().add(button);
		// 棋盘初始化
		button.setOnAction(e -> {
			if (gameOver) {
				if (Global.temporaryOppoIP == null) {
					Alert alert = new Alert(AlertType.INFORMATION, "没有对手，无法新局！");
					alert.initOwner(this);
					alert.show();
					return;
				}
				this.waitText.setText("对手正在思考，请耐心等待……");
				NewGameMessage newGameMessage = new NewGameMessage();
				newGameMessage.setState(NewGameMessage.REQUEST);
				newGameMessage.setAccount(Global.account);
				
				// 给服务器发送消息
				NetUtils.sendMessage(newGameMessage, Global.temporaryOppoIP);
			} else {
				Alert alert = new Alert(AlertType.INFORMATION,"游戏未结束，不能重新开始");
				alert.initOwner(this);
				alert.show();
			}
		});
	}
	/**
	 * 初始化新局数据
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:07:13
	 */
	private void startNew(String rivalAccount) {
		this.isBlack = true;
		// 更改游戏状态
		this.sinfoService.updateStatusByAccount(Global.account, 2);
		// 清除悔棋请求、对战请求、新局请求后的提示文本
		this.waitText.setText("");
		// 去除在线棋盘上玩家信息，以及相关标题按钮
		this.removeUserInfo();
		 
		// 清空棋子
		this.pane.getChildren().removeIf(e -> e instanceof Circle);
		
		// 在棋盘上添加 玩家账号  VS 玩家账号、账号后面显示玩家执的黑白棋子
		// 添加当前落子 后面跟黑或白棋子
		this.addStartText(rivalAccount);
		
		// 删除棋子的信息
		this.chessList = new ArrayList<>();
		// 可以悔棋
		this.replay = true;
		this.arr = new boolean[SIZE][SIZE];
		this.colors = new Color[SIZE][SIZE];

		// 添加五点标记
		this.drawFivePoint();
		this.gameOver = false;
		// 游戏开始，不能在发送对战请求了
		this.isSend = true;
		// 重置局时
		this.gameTimeText.setText("局时 10:00");
		this.gameTimeNum = 600;
		// 重置步时
		this.stepTimeText.setText("步时 60");
		this.stepTimeNum = 60;
		// 重置求助小棋仙次数
		this.robotTime = 3;
	}
	
	/**
	 *  在棋盘上添加 玩家账号  VS 玩家账号、账号后面显示玩家执的黑白棋子(逻辑不在这)
	 * 添加当前落子 后面跟黑或白棋子(逻辑不在这)
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月1日上午11:28:00
	* @param rivalAccount 对手账号
	 */
	private void addStartText(String rivalAccount)  {
		// 我方账号
		Text MyAccountText = new Text(750, 80, Global.account);
		MyAccountText.setFill(Color.RED);
		MyAccountText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));
		
		// 账号后方的棋子
		myAccountCircle.setFill(color);
		
		// VS
		Text vsText = new Text(750, 150, "VS");
		vsText.setFill(Color.BLACK);
		vsText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));

		// 对手账号
		Text rivalAccountText = new Text(750, 220, rivalAccount);
		rivalAccountText.setFill(Color.RED);
		rivalAccountText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));
		
		// 对手账号后方的棋子
		rivalAccountCircle.setFill(blackOrWhite == 1 ? Color.ALICEBLUE: Color.BLACK);
		
		// 当前落子
		Text nowChessText = new Text(705, 290, "当前落子");
		nowChessText.setFill(Color.RED);
		nowChessText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 25));
		
		// 当前落子的棋子
		nowChess.setFill(Color.BLACK);
		
		
		pane.getChildren().addAll(MyAccountText, vsText, rivalAccountText, nowChessText,
				myAccountCircle, rivalAccountCircle, nowChess);
		startTextList.add(MyAccountText);
		startTextList.add(vsText);
		startTextList.add(rivalAccountText);
		startTextList.add(nowChessText);
	}

	/**
	 * 退出游戏按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:13:15
	 */
	private void exitButton() {
		Button button = new Button("退     出");
		button.setPrefSize(80, 40);
		button.setLayoutX(530);
		button.setLayoutY(650);
		// 将按钮添加到棋盘
		pane.getChildren().add(button);
		// 退出逻辑
		button.setOnAction(e -> {
			this.exit();
		});
	}
	// 退出提示框
	private void exit() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "你确定退出吗？",
				new ButtonType("取消",  ButtonData.NO), 
				new ButtonType("确定",  ButtonData.YES));
		alert.initOwner(this);
		Optional<ButtonType> button = alert.showAndWait();
		if (button.get().getButtonData() == ButtonData.YES) {
			if (Global.temporaryOppoIP != null) {
				
				// 发送逃跑消息，己方逃跑，对方显示胜利
				NetUtils.sendMessage(new EscapeMessage(), Global.temporaryOppoIP);
				
				if (!gameOver) {
					// 游戏未结束逃跑，给对手发送消息，对手将更新数据库
					ResultMessage resultMessage = new ResultMessage();
					resultMessage.setAccount(Global.account);
					resultMessage.setResult(Color.BLACK.equals(this.color) ? 
							ResultMessage.WHITE_WIN : ResultMessage.BLACK_WIN);
					
		        	// 发送对战结果消息
		        	NetUtils.sendMessage(resultMessage, Global.oppoIP);
				}
			}
			// 更改为离线状态
			sinfoService.updateStatusByAccount(Global.account, 0);
			// 退出游戏按钮
			System.exit(0);
		}
	}
	/**
	 * 保存棋谱按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:19:48
	 */
	private void saveChessBookButton() {
		Button button = new Button("保存棋谱");
		button.setPrefSize(80, 40);
		button.setLayoutX(290);
		button.setLayoutY(650);

		// 将按钮添加到棋盘
		pane.getChildren().add(button);
		// 将棋局信息写入文件保存到本地
		button.setOnAction(e -> {
			this.saveChessBook();
		});
	}
	
	/**
	 * 将本局棋谱信息写入文件中
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:20:22
	 */
	private void saveChessBook() {
		if (!gameOver) {
			Alert alert = new Alert(AlertType.INFORMATION,"棋局未结束，不能保存棋谱！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		if (chessList.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION,"棋盘为空，不能保存棋谱！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		
		FileChooser fChooser = new FileChooser();
		// 设置保存文件对话框的标题
		fChooser.setTitle("保存棋谱");
		// 设置默认目录,打开后可自己选择目录
		fChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		// 设置文件后缀
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv", "*.csv"));
		
		// 打开文件保存对话框
		File file = fChooser.showSaveDialog(this);
		// 点击取消，取消保存
		if (file == null) {
			return;
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			// 将chessList中棋子的信息遍历写入到文件中
			for (Chess chess : chessList) {
				// 一个棋子的信息写到一行
				writer.write(chess.getX() + "," +  chess.getY() + "," + chess.getColor());
				// 换行
				writer.newLine();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 打开棋谱按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:23:33
	 */
	private void openChessBookButton() {
		Button button = new Button("打开棋谱");
		button.setPrefSize(80, 40);
		button.setLayoutX(410);
		button.setLayoutY(650);
		
		// 将按钮添加到棋盘
		pane.getChildren().add(button);
		// 从文件中读取欢迎棋局的信息
		button.setOnAction(e -> {
			this.openChessBook();
		});
	}
	
	/**
	 * 从文件中还原棋局
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:24:01
	 */
	private void openChessBook() {
		if (!gameOver) {
			Alert alert = new Alert(AlertType.INFORMATION,"棋局未结束，不能打开棋谱！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 更改游戏状态,对战中
		sinfoService.updateStatusByAccount(Global.account, 2);
		FileChooser fileChooser = new FileChooser();
		// 设置保存文件对话框的标题
		fileChooser.setTitle("打开棋谱");
		// 设置默认目录,打开后可自己选择目录
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		// 设置文件后缀
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv", "*.csv"));
		// 打开文件保存对话框
		File file = fileChooser.showSaveDialog(this);
		// 点击取消，取消选择
		if (file == null) {
			// 更改游戏状态，空闲
			sinfoService.updateStatusByAccount(Global.account, 1);
			return;
		}

		// 存放棋谱信息的集合
		List<Chess> chessList = new ArrayList<>();
		// 移除下拉按钮
		pane.getChildren().remove(menuButton);
		// 清空棋子，保证打谱的正确
		pane.getChildren().removeIf(e -> e instanceof Circle);
		// 清空所有按钮
		pane.getChildren().removeIf(e -> e instanceof Button);
		// 清空所有文本
		pane.getChildren().removeIf(e -> e instanceof Text);
		// 清空所有单行文本框
		pane.getChildren().removeIf(e -> e instanceof TextField);
		// 清空多行文本框
		pane.getChildren().removeIf(e -> e instanceof TextArea);
		
		// 添加退出按钮
		this.exitButton();
		// 添加五点标记
		this.drawFivePoint();
		// 记录已打谱的棋子的个数
		int[] count = new int[1];
		// 设置不能落子
		gameOver = true;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String data;
			while ((data = reader.readLine()) != null) {
				// 将文件每行数据以逗号为分隔符存入数组，即x y 颜色
				String[] chessData= data.split(",");
				Chess chess = new Chess(Integer.parseInt(chessData[0]),
						Integer.parseInt(chessData[1]), Color.web(chessData[2]));
				chessList.add(chess);
				
			}
			// 下一步按钮
			nextStepButton(count, chessList);
			
			// 上一步按钮
			previousStepButton(count, chessList);
			
			// 结束棋谱按钮
			exitChessBook();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 棋谱下一步按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:24:50
	* @param count
	* @param chessList
	 */
	private void nextStepButton(int[] count, List<Chess> chessList) {

		Button button = new Button("下一步");
		button.setPrefSize(60, 40);
		button.setLayoutX(635);
		button.setLayoutY(210);
		pane.getChildren().add(button);
		// 下一步按钮逻辑
		button.setOnAction(e -> {
			this.nextStep(count, chessList);
		});
	}
	/**
	 * 棋谱下一步逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:25:39
	* @param count
	* @param chessList
	 */
	private void nextStep(int[] count, List<Chess> chessList) {
		if (count[0] == chessList.size()) {
			System.out.println("已经是最后一步棋了，没有下一步了！");
			return;
		}
		Circle circle = new Circle();
		Chess chess = chessList.get(count[0]);
		circle.setCenterX(chess.getX() * LINE_SPACING + 50);
		circle.setCenterY(chess.getY() * LINE_SPACING + 50);
		circle.setRadius(CHESS_RADIUS);
		circle.setFill(chess.getColor());
		pane.getChildren().add(circle);
		count[0]++;
	}
	
	/**
	 * 棋谱上一步按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:26:11
	* @param count
	* @param chessList
	 */
	private void previousStepButton(int[] count, List<Chess> chessList) {
		Button button = new Button("上一步");
		button.setPrefSize(60, 40);
		button.setLayoutX(635);
		button.setLayoutY(310);
		pane.getChildren().add(button);
		// 上一步按钮逻辑
		button.setOnAction(e -> {
			this.previousStep(count, chessList);
		});
	}
	
	/**
	 * 棋谱上一步逻辑
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:26:29
	* @param count
	* @param chessList
	 */
	private void previousStep(int[] count, List<Chess> chessList) {
		if (count[0] == 0) {
			System.out.println("已经是第一步棋了，没有上一步了！");
			return;
		}
		// 移除最后一步棋子
		pane.getChildren().remove(pane.getChildren().size() - 1);
		count[0]--;
	}
	
	/**
	 * 结束棋谱按钮 + 逻辑 
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日上午8:26:58
	 */
	private void exitChessBook() {
		Button button = new Button("退谱");
		button.setPrefSize(60, 40);
		button.setLayoutX(635);
		button.setLayoutY(410);
		pane.getChildren().add(button);
		// 退出棋谱按钮逻辑
		button.setOnAction(e -> {
			// 更改游戏状态,空闲
			sinfoService.updateStatusByAccount(Global.account, 1);
			// 移除所有棋子
			pane.getChildren().removeIf(c -> c instanceof Circle);
			// 移除退出按钮
			pane.getChildren().removeIf(b -> b instanceof Button);
			// 移除文本
			pane.getChildren().removeIf(t -> t instanceof Text);
			// 显示在线玩家
			this.refresh();
			// 在棋盘上划线，添加棋盘五点标志，以及显示文本
			this.addText();
			// 加入文本框
			this.addTextBox();
			// 加入按钮
			this.addButton();
			// 置空棋子信息
			chessList = new ArrayList<Chess>();
			arr = new boolean[SIZE][SIZE];
			colors = new Color[SIZE][SIZE];
			// 重置黑先
			isBlack = true;
			// 默认棋子颜色重置为黑色
			color = Color.BLACK;
			// 不可以落子
			isPlay = false;
			gameOver = true;
			
			// 可以悔棋
			replay = true;
			// 棋子初始颜色为黑
			blackOrWhite = 1;
			return;
		});
	}
	/**
	 * 悔棋按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日下午1:55:02
	 */
	private void withDrawButton() {
		Button button = new Button("悔      棋");
		button.setPrefSize(80, 40);
		button.setLayoutX(170);
		button.setLayoutY(650);
		
		// 将按钮添加到棋盘
		pane.getChildren().add(button);
		// 悔棋逻辑
		button.setOnAction(e -> {
			this.withDraw();
			
		});
	}
	/**
	 * 悔棋逻辑判断
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月23日下午7:28:29
	 */
	private void withDraw() {
		// 游戏已经结束
		if (gameOver) {
			Alert alert = new Alert(AlertType.INFORMATION,"游戏已经结束，不能悔棋！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 棋盘为空
		if (chessList.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION,"棋盘为空，不能悔棋！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 用户已经悔过棋
		if (!replay) {
			Alert alert = new Alert(AlertType.INFORMATION,"已经悔过棋，不能悔棋！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		// 轮到自己落子
		if (isPlay) {
			Alert alert = new Alert(AlertType.INFORMATION,"自己落子，不能悔棋！");
			alert.initOwner(this);
			alert.show();
			return;
		}
		
		this.waitText.setText("已给对手发送悔棋请求，请耐心等待……");
		// 发送悔棋请求
		ReplayMessage replayMessage = new ReplayMessage();
		replayMessage.setFlag(ReplayMessage.REPLAY_REQUEST);
		NetUtils.sendMessage(replayMessage, Global.oppoIP);
		
	}
	
	/**
	 * 查看自己的战绩
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月25日上午10:30:47
	 */
	private void myRecordButton() {
		Button myRecordButton = new Button("我的战绩");
		myRecordButton.setPrefSize(80, 40);
		myRecordButton.setLayoutX(650);
		myRecordButton.setLayoutY(650);
		pane.getChildren().add(myRecordButton);
		
		myRecordButton.setOnAction(e -> {
			// 显示我的战绩的页面
			MyRecord myRecord = new MyRecord();
			myRecord.show();
		});
	}
	
	/**
	 * 查看对手的战绩
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月25日上午10:31:23
	 */
	private void rivalRecordButton() {
		Button rivalRecordButton = new Button("对手战绩");
		rivalRecordButton.setPrefSize(80, 40);
		rivalRecordButton.setLayoutX(770);
		rivalRecordButton.setLayoutY(650);
		pane.getChildren().add(rivalRecordButton);
		
		rivalRecordButton.setOnAction(e -> {
			if (Global.temporaryOppoIP == null) {
				Alert alert = new Alert(AlertType.INFORMATION, "没有连接对手，无法查看！");
				alert.initOwner(this);
				alert.show();
				return;
			}
			// 给对手发送查看战绩的消息
			RivalRecordMessage recordMessage = new RivalRecordMessage();
			recordMessage.setRivalRecord(RivalRecordMessage.REQUEST);
			
        	NetUtils.sendMessage(recordMessage, Global.temporaryOppoIP);
		});
	}
	
	/**
	 * 落子声
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月2日下午1:37:09
	 */
	private void soundMoveLater() {
        Media media = new Media(getClass().getResource("/music/落子声.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
	}
	/**
	 * 当前线程睡眠20毫秒，用于修复棋子落子后偶尔没有落子声的未知bug
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月6日下午1:30:10
	 */
	private void stopThread() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}









