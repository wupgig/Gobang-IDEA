package com.wupgig.record;

import java.text.DecimalFormat;

import com.wupgig.message.RivalRecordMessage;
import com.wupgig.pojo.User;

import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * 对手战绩显示界面
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月25日下午12:00:09
 *
 */
public class RivalRecord extends Stage{
	//用来保存对手发送过来的用户信息，即User类
	public static  RivalRecordMessage recordMessage;
	public RivalRecord() {
		// 设置窗口大小不可改变
		this.setResizable(false);
		// 设置图标
		this.getIcons().add(new Image("images/icon-1.jpg"));
		// 设置窗口标题
		this.setTitle("我的战绩");
		// 创建主舞台
		Pane pane = new Pane();
		// 设置舞台背景-蓝色
		pane.setBackground(new Background(new BackgroundFill(Color.AZURE,null,null)));
		// 创建场景对象 长为400像素，宽为400像素
		Scene scene = new Scene(pane, 400, 400);
		// 将场景放入舞台
		this.setScene(scene);
		
		// 加入文本
		this.addText(pane);
		
		// 加入按钮
		this.addButton(pane);
	}

	private void addText(Pane pane) {
		
		User user = recordMessage.getUser();
		// 账号
		Text accountText = new Text(160, 20, "账号：" + user.getAccount());
		accountText.setFill(Color.BLACK);
		accountText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 积分	
		Text scoreText = new Text(160, 50, "积分：" + user.getScore());
		scoreText.setFill(Color.BLACK);
		scoreText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 总局数
		Text totalText = new Text(160, 100, "总局数：" + user.getTotalNums());
		totalText.setFill(Color.BLACK);
		totalText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 胜局数
		Text winText = new Text(160, 150, "胜局数：" + user.getWinNums());
		winText.setFill(Color.BLACK);
		winText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 败局数
		Text lostText = new Text(160, 200, "败局数：" + user.getLostNums());
		lostText.setFill(Color.BLACK);
		lostText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		// 平局数
		Text drawText = new Text(160, 250, "平局数：" + user.getDrawNums());
		drawText.setFill(Color.BLACK);
		drawText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		

		
		
		// 胜率,保留两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		Text oddsText = new Text(160, 300, "胜率：" + 
				df.format(user.getWinNums() * 100.0 / (user.getTotalNums() == 0 ? 1 : user.getTotalNums())) + "%");
		oddsText.setFill(Color.BLACK);
		oddsText.setFont(Font.font("宋体", 
				 FontPosture.REGULAR, 15));
		
		
		// 将文字加入场景
		pane.getChildren().addAll(accountText, scoreText, totalText, 
				winText, lostText, drawText, oddsText);
		
	}
	
	/**
	 * 添加按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月25日上午10:47:53
	* @param pane
	 */
	private void addButton(Pane pane) {
		// 添加关闭按钮
		this.closeButton(pane);

	}
	/**
	 * 关闭战绩页面按钮
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月25日上午10:52:10
	* @param pane
	 */
	private void closeButton(Pane pane) {
		Button closeButton = new Button("关闭");
		closeButton.setPrefSize(40, 40);
		closeButton.setLayoutX(180);
		closeButton.setLayoutY(350);
		pane.getChildren().add(closeButton);
		
		closeButton.setOnAction(e -> {
			// 关闭战绩页面
			this.close();
		});
	}
}
