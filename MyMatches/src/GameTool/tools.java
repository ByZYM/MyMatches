package GameTool;

import java.awt.*;
import java.awt.image.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import GameGraphics.mainFrame;
import GameGraphics.updateThread;
import GameLogic.Enity;
import GameLogic.NPC;
import GameLogic.block;
import GameResource.gameConfig;
import GameResource.gameMap;

public class tools implements gameConfig {
	/* ��ȡ���������Ʒ */
	public static double fps = 0;
	public static int frameCount = 0;
	public static double currentTime = 0;
	public static double lastTime = 0;

	public static double currentTime1 = 10;
	public static double lastTime1 = 0;
	public static double timeResult = 10;

	public static void setPauseGame(boolean b) {
		if (b == true) {// ��ͣ
			mainFrame.d.setCannotDothing(true);
			mainFrame.pause = b;
		} else {
			mainFrame.d.setCannotDothing(false);
			mainFrame.pause = b;
		}
	}

	// ˢ������������
	public static void produceMob(block b) {
		if (mainFrame.pause == false && getPosBlockID(b.getI() - 1, b.getJ() - 2) == 0
				&& getPosBlockID(b.getI() - 1, b.getJ() - 1) == 0 && getPosBlockID(b.getI(), b.getJ() - 1) == 0
				&& getPosBlockID(b.getI() + 1, b.getJ() - 1) == 0 && getPosBlockID(b.getI() + 1, b.getJ() - 2) == 0
				&& getPosBlockID(b.getI(), b.getJ() - 2) == 0) {
			if (updateThread.stdTime * 10 % 15000 == 0) {// ÿʮ����
				int a = getNPCAmount(new Rectangle(b.getX() - 8 * mainFrame.blockWidth,
						b.getY() - 8 * mainFrame.blockHeight, 16 * mainFrame.blockWidth, 16 * mainFrame.blockHeight));
				if (a <= 6) {
					NPC n = new NPC(b.getI(), b.getJ() - 1);
					n.canIntersect = true;
					n.name = "��ʬ";
					n.setWalkAround(true);
					mainFrame.npcs.add(n);
				}
			}

		}
	}

	public static void growSeed(block b) {// ����������
		if (mainFrame.pause == false) {
			if (gameMap.map0[b.getJ() + 1][b.getI()] == 0) {// �·�û�з���
				mainFrame.destroyBlockDirect(b.getI(), b.getJ());
			} else if (updateThread.stdTime * 10 % 60000 == 0 && new Random().nextInt() % 100 <= 20) {// ÿ60��
																										// 1/5
				gameMap.map0[b.getJ()][b.getI()] = b.getID() + 1;
			}

		}
	}

	public static boolean isLocBlockNULL(int i1, int j1, int i2, int j2) {// һ���������Ƿ�û�з���
		// i1,j1���Ͻ�
		// i2,j2���½�
		for (int i = i1; i <= i2; i++) {
			for (int j = j1; j <= j2; j++) {
				if (getPosBlockID(i, j) != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public static void growTree(block b) {// ������
		if (mainFrame.pause == false) {
			if (gameMap.map0[b.getJ() + 1][b.getI()] == 0) {// �·�û�з���
				mainFrame.destroyBlockDirect(b.getI(), b.getJ());
			} else if (isLocBlockNULL(b.getI() - 2, b.getJ() - 5, b.getI() + 2, b.getJ() - 1)) {
				if (updateThread.stdTime * 10 % 60000 == 0 && new Random().nextInt() % 100 <= 15) {// ÿ60�� 15%

					for (int i = b.getI() - 2; i <= b.getI() + 2; i++) {
						for (int j = b.getJ() - 5; j <= b.getJ() - 2; j++) {
							gameMap.map0[j][i] = 4;
						}
					}
					gameMap.map0[b.getJ()-5][b.getI()-2] = 0;
					gameMap.map0[b.getJ()-5][b.getI()-1] = 0;
					gameMap.map0[b.getJ()-4][b.getI()-2] = 0;
					gameMap.map0[b.getJ()-5][b.getI()+2] = 0;
					gameMap.map0[b.getJ()-5][b.getI()+1] = 0;
					gameMap.map0[b.getJ()-4][b.getI()+2] = 0;
					gameMap.map0[b.getJ()][b.getI()] = 3;
					gameMap.map0[b.getJ() - 1][b.getI()] = 3;
					gameMap.map0[b.getJ() - 2][b.getI()] = 3;
					gameMap.map0[b.getJ() - 3][b.getI()] = 3;
					
					
					mainFrame.newDropEnity(24, b.getI()-1, b.getJ() - 1, new Random().nextInt() %2+1);
				}
			}

		}
	}

	public static Enity getEnityOnHand() {
		if (mainFrame.bp.stackAmount.get(mainFrame.select - 1) > 0) {
			return mainFrame.bp.bp_enity.get(mainFrame.select - 1);
		}
		return new Enity();
	}

	public static double getTimeBetween() {// ��֡��ʱ���� ������
		currentTime1 = System.currentTimeMillis();
		timeResult = currentTime1 - lastTime1;
		lastTime1 = currentTime1;
		return timeResult;
	}

	public static double getFPS() {// ÿ��֡��

		frameCount++;
		currentTime = System.currentTimeMillis() / 1000.0;
		if (currentTime - lastTime > 1.0) // ��ʱ�������1����
		{
			fps = frameCount / (currentTime - lastTime);// ������1���ӵ�FPSֵ
			lastTime = currentTime;
			frameCount = 0;
		}
		return fps;
	}

	// ���ݹ��� ������������
	public static double getPercent(int AimType, int type, int toolType) {
		// ���� 0"��ͨ"1"ľ"2"ʯ"3"��"4"��"5"��"6"����"
		// �������� 0"��ͨ"1"��"2"��"3"��"4"��"
		double result = 1.0;
		switch (AimType) {
		case 0:
			result = 1.0;
			break;// ��ͨ����
		case 1:// ľͷ
			if (toolType == 3) {// ��ͷ
				result = 1.0 - type * 0.1 - 0.3;
			}
			break;
		case 2:// ʯͷ
		case 3:// ��
		case 4:// ��
		case 5:// ��ʯ
			if (toolType == 2) {// ����
				result = 1.0 - type * 0.1 - 0.3;
			}
			break;
		case 6:// ����
			if (toolType == 1) {// ����
				result = 1.0 - type * 0.1 - 0.3;
			}
			break;
		}
		return result;
	}

	public static int[] getPixWorldXY(int x, int y) {
		// ����I��J��mX��mY
		int mX, mY;
		block b = getPixBlock(x, y);
		mX = mainFrame.mouseX - b.getX();
		mY = mainFrame.mouseY - b.getY();
		int result[] = { b.getI(), b.getJ(), mX, mY };
		return result;
	}

	/* ��ȡ��Ļ��ĳһ���ص��Ӧ�ķ��� */
	public static block getPixBlock(int x, int y) {
		for (int i = 0; i < mainFrame.bk.size(); i++) {
			if (mainFrame.bk.get(i).getRect().contains(x, y)) {
				return mainFrame.bk.get(i);
			}
		}
		return null;
	}

	/* ��ȡ��Ļ��ĳһ���ص��Ӧ��NPC */
	public static NPC getPixNPC(int x, int y) {
		for (int i = 0; i < mainFrame.npcs.size(); i++) {
			if (mainFrame.npcs.get(i).getRect().contains(x, y)) {
				return mainFrame.npcs.get(i);
			}
		}
		return null;
	}

	/* ��ȡ��Ļ��ĳһ��Χ������NPC���� */
	public static int getNPCAmount(Rectangle r) {
		int result;
		result = 0;
		for (int i = 0; i < mainFrame.npcs.size(); i++) {
			if (mainFrame.npcs.get(i).getRect().intersects(r)) {
				result++;
			}
		}
		return result;
	}

	/* ��ȡ��Ļ��ĳһ�����Ӧ�ķ��� */
	public static block getPosBlock(int i, int j) {
		int id = getPosBlockID(i, j);
		if (id != 0) {
			return new block(i, j, gameMap.getMapIcon(id), id);
		}
		return null;
	}

	/* ��ȡ��Ļ��ĳһ�����Ӧ�ķ���ID */
	public static int getPosBlockID(int i, int j) {
		if (i > gameMap.map0[0].length - 1 || i < 0 || j > gameMap.map0.length - 1 || j < 0) {
			return 0;
		}
		return gameMap.map0[j][i];
	}

	/* �жϷ����Ƿ������ײ */
	public static boolean canIntersect(int id) {
		// ������ײID
		int a[] = { 15, 16, 17, 18, 21, 22, 24 };
		for (int i = 0; i < a.length; i++) {
			if (a[i] == id)
				return false;
		}
		return true;
	}

	/* ��ײ��� */
	public static boolean intersectJudge(Rectangle r) {
		boolean result = false;
		while (mainFrame.initing == true) {
			if (mainFrame.initing == false)
				break;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (mainFrame.initing == false) {
			try {
				for (int ii = 0; ii < mainFrame.bk.size(); ii++) {
					block b = mainFrame.bk.get(ii);
					if (b != null && b.getImg() != null && b.getRect().intersects(r) && canIntersect(b.getID())) {
						result = true;
						break;
					}
				}
			} catch (IndexOutOfBoundsException e) {
				// System.out.println("δ֪ԭ������Խ�磬����ִ�в���");
				for (int ii = 0; ii < mainFrame.bk.size(); ii++) {
					block b = mainFrame.bk.get(ii);
					if (b != null && b.getImg() != null && b.getRect().intersects(r) && canIntersect(b.getID())) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	/* ʹͼƬ��ɫ����͸�� */
	public static Image makeColorTransparent(Image im, final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};
		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	/* ���汳�� */
	public static void writeBackPack() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(backPackPath);
			DataOutputStream dos = new DataOutputStream(fos);
			int amount = mainFrame.bp.bp_enity.size();
			dos.writeInt(amount);
			for (int i = 0; i < amount; i++) {

				dos.writeInt(mainFrame.bp.bp_enity.get(i).getID());
				dos.writeInt(mainFrame.bp.bp_enity.get(i).i);
				dos.writeInt(mainFrame.bp.bp_enity.get(i).j);
				dos.writeInt(mainFrame.bp.bp_enity.get(i).getBreak());
				dos.writeInt(mainFrame.bp.stackAmount.get(i));
			}
			dos.flush();
			dos.close();
		} catch (Exception e1) {
		}
	}

	/* ���뱳�� */
	public static void readBackPack() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(backPackPath);

			DataInputStream dis = new DataInputStream(fis);
			int amount = dis.readInt();
			for (int i = 0; i < amount; i++) {
				Enity e = new Enity(dis.readInt(), dis.readInt(), dis.readInt());
				e.setBreak(dis.readInt());
				mainFrame.bp.bp_enity.set(i, e);
				mainFrame.bp.stackAmount.set(i, dis.readInt());
			}
			dis.close();
			fis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/* �������� */
	public static void writeChest(int i, int j) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path_chest + i + "-" + j + ".dat");
			DataOutputStream dos = new DataOutputStream(fos);
			int amount = mainFrame.chest.bp_enity.size();
			dos.writeInt(amount);
			for (int ii = 0; ii < amount; ii++) {
				dos.writeInt(mainFrame.chest.bp_enity.get(ii).getID());
				dos.writeInt(mainFrame.chest.bp_enity.get(ii).i);
				dos.writeInt(mainFrame.chest.bp_enity.get(ii).j);
				dos.writeInt(mainFrame.chest.bp_enity.get(ii).getBreak());
				dos.writeInt(mainFrame.chest.stackAmount.get(ii));
			}
			dos.flush();
			dos.close();
		} catch (Exception e1) {
		}
	}

	/* �������� */
	public static void readChest(int i, int j) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(path_chest + i + "-" + j + ".dat");
			DataInputStream dis = new DataInputStream(fis);
			int amount = dis.readInt();
			for (int ii = 0; ii < amount; ii++) {
				Enity e = new Enity(dis.readInt(), dis.readInt(), dis.readInt());
				e.setBreak(dis.readInt());
				mainFrame.chest.bp_enity.set(ii, e);
				mainFrame.chest.stackAmount.set(ii, dis.readInt());
			}
			dis.close();
			fis.close();
		} catch (Exception e1) {
			FileOutputStream fos;
			System.out.println("���������ӣ��޷���ȡ��ʼ����");
			try {
				fos = new FileOutputStream(path_chest + i + "-" + j + ".dat");

				DataOutputStream dos = new DataOutputStream(fos);
				for (int ii = 0; ii < mainFrame.chest.bp_enity.size(); ii++) {
					mainFrame.chest.bp_enity.set(ii, new Enity(1, 0, 0));
					mainFrame.chest.stackAmount.set(ii, 0);
				}
				dos.flush();
				dos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* ����������� */
	public static void writePlayerData() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path_playerData);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(mainFrame.p.I);
			dos.writeInt(mainFrame.p.J);
			dos.writeInt(mainFrame.hp.getHP());
			dos.writeInt(mainFrame.hp.getMaxHP());
			dos.writeInt(mainFrame.hp.getHunger());
			dos.writeInt(mainFrame.hp.getMaxHunger());
			dos.flush();
			dos.close();
		} catch (Exception e1) {
		}
	}

	/* ����������� */
	public static void readPlayerData() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(path_playerData);

			DataInputStream dis = new DataInputStream(fis);
			mainFrame.p.I = dis.readInt();
			mainFrame.p.J = dis.readInt();
			mainFrame.hp.setHP(dis.readInt());
			mainFrame.hp.setMaxHP(dis.readInt());
			mainFrame.hp.setHunger(dis.readInt());
			mainFrame.hp.setMaxHunger(dis.readInt());
			;
			dis.close();
			fis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/* ����DropEnity */
	public static void writeDropEnity() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(dropEnityPath);

			DataOutputStream dos = new DataOutputStream(fos);
			int amount = mainFrame.drop_Enity.size();
			dos.writeInt(amount);
			for (int i = 0; i < amount; i++) {
				dos.writeInt(mainFrame.drop_Enity.get(i).getID());
				dos.writeInt(mainFrame.drop_Enity.get(i).i);
				dos.writeInt(mainFrame.drop_Enity.get(i).j);
				dos.writeInt(mainFrame.drop_Enity.get(i).getBreak());
			}
			dos.flush();
			dos.close();
		} catch (Exception e1) {
		}
	}

	/* ����DropEnity */
	public static void readDropEnity() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(dropEnityPath);

			DataInputStream dis = new DataInputStream(fis);
			int amount = dis.readInt();
			mainFrame.drop_Enity.clear();
			for (int i = 0; i < amount; i++) {
				Enity e = new Enity(dis.readInt(), dis.readInt(), dis.readInt());
				e.setBreak(dis.readInt());
				mainFrame.drop_Enity.add(e);
			}
			dis.close();
			fis.close();
		} catch (Exception e1) {
		}
	}

	/* �����ͼ */
	public static void writeMap() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(mapPath);

			DataOutputStream dos = new DataOutputStream(fos);
			int i = gameMap.map0.length;
			int j = gameMap.map0[0].length;
			dos.writeInt(i);
			dos.writeInt(j);
			for (int ii = 0; ii < i; ii++) {
				for (int jj = 0; jj < j; jj++) {
					dos.writeInt(gameMap.map0[ii][jj]);
				}
			}
			dos.flush();
			dos.close();
		} catch (Exception e1) {
		}
	}

	/* ��ȡ��ͼ */
	public static void readMap() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(mapPath);

			DataInputStream dis = new DataInputStream(fis);
			int i = 0;
			int j = 0;
			i = dis.readInt();
			j = dis.readInt();
			System.out.println(i + " " + j);
			gameMap.map0 = new int[i][j];
			for (int ii = 0; ii < i; ii++) {
				for (int jj = 0; jj < j; jj++) {
					gameMap.map0[ii][jj] = dis.readInt();
				}
			}
			dis.close();
			fis.close();
		} catch (Exception e1) {
		}
	}

	/* ��ȡ�ƻ�ͼƬ */
	public static Image[] getDestroyImg() {
		Image i[] = new Image[10];
		for (int j = 0; j < 10; j++) {
			i[j] = tools.makeColorTransparent(new ImageIcon("./images/blocks/destroy" + (j) + ".png").getImage(),
					Color.white);
		}
		return i;
	}

	/* ��ȡ����ͼƬ */
	public static Image[] getResImg() {
		Image i[] = new Image[100];
		File file = new File("./images/blocks/");
		for (int j = 0; j < file.listFiles().length; j++) {
			// System.out.println(tools.class.getClassLoader().getResource("images/blocks/"+(j+1)+".png"));
			// //��binĿ¼��
			i[j] = tools.makeColorTransparent(new ImageIcon("./images/blocks/" + (j + 1) + ".png").getImage(),
					Color.white);
		}
		return i;
	}

	/* ��ȡ���ͼƬ */
	public static Image[] getPlayerImg() {
		Image i[] = new Image[10];
		for (int j = 0; j < 5; j++) {
			i[j] = tools.makeColorTransparent(new ImageIcon("./images/player/player" + j + ".png").getImage(),
					Color.white);
		}
		for (int j = 5; j < 9; j++) {
			i[j] = tools.makeColorTransparent(new ImageIcon("./images/player/player(" + (j - 4) + ").png").getImage(),
					Color.white);
		}
		return i;
	}

	/* ��ȡ��ӰͼƬ */
	public static Image getShadowImg() {
		Graphics gBuffer;
		BufferedImage bufferImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		gBuffer = bufferImage.getGraphics();
		Image s = new ImageIcon("./images/env/shadow.png").getImage();
		gBuffer.drawImage(s, 0, 0, 128, 128, 128, 0, 256, 128, null);// ����
		gBuffer.drawImage(s, 128, 0, 256, 128, 256, 0, 384, 128, null);// ����
		gBuffer.drawImage(s, 0, 128, 128, 256, 0, 128, 128, 256, null);// ����
		gBuffer.drawImage(s, 128, 128, 256, 256, 0, 256, 128, 384, null);// ����
		return bufferImage;
	}

	/* �Ƿ����� */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/* �����ı����ȡ�����Ϊ2Ӣ��Ϊ1 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

}
