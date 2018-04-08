package GameLogic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;

import GameGraphics.mainFrame;
import GameResource.gameConfig;

public class Enity implements gameConfig {
	block bk;
	public int i = 0;
	public int j = 0;
	int x = 0;
	int y = 0;
	int aX = 0;// ˮƽƫ��
	int aY = 0;// ��ֱƫ��
	Image im;
	private int ID=-1;
	private String Name="��";// ����
	private int Type=-1;// ���� 0"��ͨ"1"ľ"2"ʯ"3"��"4"��"5"��"6"�޵�"

	private int Atk=-1;// ������
	private int Break;// �;ö�
	private int StackAmount;// �ѵ�����
	private int CanPut;// 0�� 1��
	private int CanEat;// 0�� 1��
	private int Tool;// �������� 0"��ͨ"1"��"2"��"3"��"4"��"
	private int HungerHeal;// �����Ȼָ�ֵ

	public Enity(){
		
	}
	public Enity(int id, int i, int j) {// ��ʼ����Ʒ
		FileInputStream fis;
		try {
			fis = new FileInputStream(enityPath + id + ".enity");

			DataInputStream dis = new DataInputStream(fis);
			this.ID = id;
			this.Name = new String(dis.readUTF());
			this.Type = dis.readInt();
			this.Atk = dis.readInt();
			this.Break = dis.readInt();
			this.StackAmount = dis.readInt();
			this.CanPut = dis.readInt();
			this.Tool = dis.readInt();
			this.CanEat = dis.readInt();
			this.HungerHeal = dis.readInt();
			// Image Rect = tools.makeColorTransparent(
			// new ImageIcon(tools.class.getClassLoader().getResource(
			// "images/display/Rect.png")).getImage(), Color.white);

			im = new ImageIcon("./images/blocks/" + id + ".png").getImage();
			this.i = i;
			this.j = j;
			dis.close();
			fis.close();
		} catch (FileNotFoundException e1) {// û�и��ļ�
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(enityPath + id + ".enity");

				DataOutputStream dos = new DataOutputStream(fos);

				dos.flush();
				dos.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public Enity(int id, int i, int j, int mX, int mY) {// ��ʼ����Ʒ
		this(id, i, j);
		aX = mX;
		aY = mY;
	}

	public Enity(block b) {// ��ʼ��������Ʒ
		this(b.getID(), 0, 0);
		this.bk = b;
		this.i = bk.getI();
		this.j = bk.getJ();
		b.getID();
		im = b.getImg();
	}

	public int getID() {
		return ID;
	}

	public String getName() {// ��ȡName
		return this.Name;
	}

	public int getType() {// ��ȡType
		return this.Type;
	}

	public int getAtk() {// ��ȡAtk
		return this.Atk;
	}

	public void setI(int a) {// ����i
		this.i = a;
	}

	public void setJ(int a) {// ����j
		this.j = a;
	}

	public void setBreak(int a) {// �����;ö�
		this.Break = a;
	}

	public int getBreak() {// ��ȡBreak
		return this.Break;
	}

	public int getStackAmount() {// ��ȡ��Ʒ�ѵ�����
		return this.StackAmount;
	}

	public boolean getCanPut() {
		return this.CanPut == 1 ? true : false;
	}

	public boolean getCanEat() {
		return this.CanEat == 1 ? true : false;
	}

	public int getToolType() {// ��ȡToolType
		return this.Tool;
	}

	public int getHungerHeal() {// ��ȡ�����Ȼָ�ֵ
		return this.HungerHeal;
	}

	public Image getImg() {// ��ȡ��ƷͼƬ
		return im;
	}

	public Rectangle getRect() {// ��ȡ������ײ����
		return new Rectangle(x, y, mainFrame.blockWidth / 2, mainFrame.blockHeight / 2);
	}

	public void drawDrop(Graphics g) {
		x = mainFrame.p.aX + (i - mainFrame.p.I) * mainFrame.blockWidth - mainFrame.p.mX + mainFrame.blockWidth / 2 + aX;
		y = mainFrame.p.aY + (j - mainFrame.p.J) * mainFrame.blockHeight - mainFrame.p.mY + mainFrame.blockHeight / 2 + aY;
		g.drawImage(im, x, y, mainFrame.blockWidth / 2, mainFrame.blockHeight / 2, null);
		g.setColor(Color.red);
		// g.drawRect(x, y, blockWidth/2, blockHeight/2);
	}
}