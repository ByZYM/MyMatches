package MAP;


import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

public interface gameConfig {//��Ϸ��̬����
	//���ڱ���
	String title="��ͼ�༭��";
	String path = "./data/map/map.map";  
	String blockPath= "./data/blocks/";  
	String enityPath= "./data/enity/";
	String recipePath= "./data/recipe/";  

	
	//��Ϸ�����С
	int blockWidth=30;
	int blockHeight=30;
	//���ڴ�С
	int panelWidth=blockWidth*1024;
	int panelHeight=blockWidth*256;
	
	//NPC��С
	int playerWidth=30;
	int playerHeight=60;
	
	Image ic[]=tools.getResImg();

	Image Rect=tools.makeColorTransparent(new ImageIcon("./images/display/Rect.png").getImage(),Color.white);
}