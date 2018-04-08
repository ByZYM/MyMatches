package GameGraphics;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import GameLogic.Enity;
import GameLogic.NPC;
import GameLogic.block;
import GameLogic.chatBubbles;
import GameLogic.closeDoor;
import GameMenu.mainMenu;
import GameMenu.optionMenu;
import GameMission.mission;
import GameResource.*;
import GameTool.tools;
import PLAYER.HP;
import PLAYER.backpack;
import PLAYER.chestBar;
import PLAYER.player;
import PLAYER.progress;
import PLAYER.recipeBar;

/*������*/
public class mainFrame extends JFrame
		implements gameConfig, KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

	// ���ڴ�С
	public static int panelWidth = 1000;
	public static int panelHeight = 600;

	// ��Ϸ�����С
	public static int blockWidth = 30;
	public static int blockHeight = blockWidth;
	public static boolean firstInit = false;
	// NPC��С
	public static int playerWidth = blockWidth;
	public static int playerHeight = playerWidth * 2 - 5;

	public static int mouseX;
	public static int mouseY;
	static int view = panelWidth / 15 / 2 + 2;

	public static player p;

	static NPC npc;
	Image iBuffer = null;
	Graphics gBuffer = null;
	public static display d;
	backGround bg;

	Image s = null;
	// Light light;
	// �Ƿ������ƻ�����
	public static boolean pause = true;
	// �Ƿ������ƻ�����
	boolean destroy = false;
	// �Ƿ����ڳ�
	boolean eating = false;
	// ͼƬ���
	int destroyAmount = 0;

	// �����ƻ��ķ���
	int bI;
	int bJ;
	// ��������ֵ
	int block_ID = 1;
	int block_HP = 0;
	// ʳ������ֵ
	int eatingPiece = 0;
	// �Ƿ���Debugģʽ
	public static boolean Debug = false;
	// �Ƿ����ӽǱ任ģʽ
	public static boolean ViewMod = false;

	public static ArrayList<block> bk = new ArrayList<block>();// ��ǰ��Ļ�ڵ�block
	public static ArrayList<Enity> drop_Enity = new ArrayList<Enity>();// dropEnity
	public static ArrayList<NPC> npcs = new ArrayList<NPC>();// npc
	public static ArrayList<chatBubbles> cb = new ArrayList<chatBubbles>();// chatBubbles
	public static mission ms = new mission();
	public static backpack bp;// ����
	public static chestBar chest;// ����
	public static recipeBar rb;// �ϳɱ�
	public static HP hp;// Ѫ��������
	public static mainMenu mm = new mainMenu();// ���˵�
	public static optionMenu om = new optionMenu();// ���ò˵�
	public static progress Progress;// ������

	public static int lastPlayerLoc[] = { 0, 0 };// ��ҵ���һ����
	public static int locDif[] = { 0, 0 };// ��ҵ�ǰ��������һ�����ֵ
	public static int select = 1;// װ����ѡ���� 1-10
	// ��ʼ������
	public static boolean initing = true;

	public mainFrame() {
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addMouseWheelListener(this);
		d = new display();
		init();
	}

	// �µ�����
	public static void newDropEnity(int id, int i, int j, int amount) {
		for (int ii = 0; ii < amount; ii++) {
			drop_Enity.add(new Enity(id, i, j));
		}
	}

	/* ���������� */
	public static void newChatBubble(NPC n, long delay, String line) {
		cb.add(new chatBubbles(n, delay, line));
	}

	public void init() {
		updateThread ut = new updateThread(this);
		new Thread(ut).start();
		this.setTitle(title);
		this.setSize(panelWidth, panelHeight);
		this.setBounds(100, 0, panelWidth, panelHeight);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new FlowLayout());
		Container c = this.getContentPane();
		c.setBackground(Color.white);
		c.setLayout(null);
		setResizable(false);
		setVisible(true);
	}

	public static void startGame() {
		bp = new backpack(p, mainFrame.panelHeight / 2 - 80, 45, 5, 10);// ����

		tools.readMap();
		tools.readDropEnity();
		tools.readBackPack();

		// ���������ƶ��߳�
		for (int i = 0; i < 1; i++) {
			npc = new NPC(482, 112);
			npc.name = "hahaha";
			// npc.setWalkAround(true);
			npc.canDown = false;
			npcs.add(npc);
		}
		npc = new NPC(483, 112);
		npc.name = "NPC";
		npc.canDown = false;
		npcs.add(npc);

		p = new player(476, 117);
		// light=new Light();
		// light.X=panelWidth-100;
		// light.Y=100;
		new Thread(p).start();

		chest = new chestBar(p, mainFrame.panelHeight / 2 - 250, 45, 3, 10);// ����
		rb = new recipeBar(p, mainFrame.panelHeight / 2 - 250, 45, 10);// �ϳɱ�
		rb.setSize(2);

		hp = new HP(p);// Ѫ��
		Progress = new progress();// ������

		tools.readPlayerData();
		// bg=new backGround();
		// ����ˢ�½����߳�

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					for (int i = 0; i < drop_Enity.size(); i++) {
						if (drop_Enity.get(i).getRect().intersects(p.getRect())) {// �񵽵�����
							if (bp.addItem(drop_Enity.get(i)) == true) {// �ɹ��񵽵�����
								drop_Enity.remove(i);
							}
						}
					}
					int view1 = panelWidth / 30 / 2 + 2;
					for (int i = 0; i < npcs.size(); i++) {
						if (npcs.get(i).canIntersect == true && npcs.get(i).getRect().intersects(p.getRect())) {// ������
							p.player_HP -= npcs.get(i).atk;
							p.knockBack(npcs.get(i).getRect());
						}
						if ((npcs.get(i).I < p.I - view1 || npcs.get(i).I > p.I + view1 || npcs.get(i).J < p.J - view1
								|| npcs.get(i).J > p.J + view1) && npcs.get(i).canDown == true) {
							npcs.remove(i);
							i--;
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					for (int i = 0; i < npcs.size(); i++) {
						if (pause == false) {
							NPC n = npcs.get(i);
							if (n.folowPlayer == true) {
								n.AX = mainFrame.p.I;
								n.AY = mainFrame.p.J;
							}
							if (i == n.AX && n.J == n.AY && n.folowPlayer == false) {
								n.complete = true;
							}
							n.move();
							if (tools.intersectJudge(n.getNextRect(2)) == false && n.isJump == false
									&& n.canDown == true) {
								n.down = true;
							}
							if (n.canIntersect == true) {
								if (Math.sqrt((p.I - n.I) * (p.I - n.I) + (p.J - n.J) * (p.J - n.J)) < 6) {// NPC�������
									n.setWalkAround(false);
									n.complete = false;
									if (n.folowPlayer == false) {
										System.out.println("��" + n.getName() + "����");
										n.folowPlayer();
									}
								} else {
									if (n.folowPlayer == true) {
										System.out.println(n.getName() + "ʧȥ����");
										n.folowPlayer();
									}
									n.setWalkAround(true);
								}
							}
							if (n.left == false && n.right == false) {
								n.towards = 0;
							}
						}
					}
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// initBlock();
		// newDropEnity(102, 479, 117, 1);
		// newDropEnity(101, 480, 117, 1);
		// newDropEnity(100, 481, 117, 1);
		// newDropEnity(200, 482, 117, 1);
		// newDropEnity(200, 482, 117, 1);
		// newDropEnity(200, 482, 117, 1);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public static void initBlock(Graphics g) {
		initing = true;
		Image ima = null;
		bk = new ArrayList<block>();
		for (int i = p.I - view; i <= p.I + view; i++) {
			for (int j = p.J - view; j <= p.J + view; j++) {
				if (i >= 0 && j >= 0 && i < gameMap.map0[0].length && j < gameMap.map0.length) {
					ima = gameMap.getMapIcon(gameMap.map0[j][i]);
					if (gameMap.map0[j][i] == 21 || gameMap.map0[j][i] == 22) {// closeDoor
						bk.add(new closeDoor(i, j, ima, gameMap.map0[j][i]));
					} else {
						bk.add(new block(i, j, ima, gameMap.map0[j][i]));
					}
					if (bk.get(bk.size() - 1).getImg() != null) {
						if (bk.get(bk.size() - 1).getID() == 14) {// ˢ����
							tools.produceMob(bk.get(bk.size() - 1));
						} else if (bk.get(bk.size() - 1).getID() >= 15 && bk.get(bk.size() - 1).getID() <= 17) {// �ܲ���
							tools.growSeed(bk.get(bk.size() - 1));
						} 
						else if (bk.get(bk.size() - 1).getID() ==24) {// ����
							tools.growTree(bk.get(bk.size() - 1));
						}
						bk.get(bk.size() - 1).draw(g);
					}

				}
			}
		}
		firstInit = true;
		initing = false;

	}

	// public void refreshBlock(Graphics g){
	// Image ima = null;
	// for(int i=0;i<bk.size();i++){
	// if (bk.get(i).getImg() != null) {
	// if (bk.get(i).getID() == 14) {// ˢ����
	// tools.produceMob(bk.get(i));
	// } else if (bk.get(i).getID() >= 15 && bk.get(i).getID() <= 17) {// �ܲ���
	// tools.grow(bk.get(i));
	// }
	// bk.get(i).draw(g);
	// }
	// }
	// if(p.I!=lastPlayerLoc[0]||p.J!=lastPlayerLoc[1]){
	// locDif[0]=p.I-lastPlayerLoc[0];
	// locDif[1]=p.J-lastPlayerLoc[1];
	// lastPlayerLoc[0]=p.I;
	// lastPlayerLoc[1]=p.J;
	// //p.I/J - view������СI��J
	// //p.I/J + view�������I��J
	// if(locDif[0]>0){
	// for(int i=0;i<bk.size();i++){
	// if(bk.get(i).getI()==p.I - view){
	// bk.remove(bk.get(i));
	// }
	// }
	// }
	// if(locDif[0]<0){
	// for(int i=0;i<bk.size();i++){
	// if(bk.get(i).getI()==p.I + view){
	// bk.remove(bk.get(i));
	// }
	// }
	//
	//// ima=gameMap.getMapIcon(gameMap.map0[j][i]);
	//// if (gameMap.map0[j][i] == 21 || gameMap.map0[j][i] == 22) {// closeDoor
	//// bk.add(new closeDoor(i, j, ima, gameMap.map0[j][i]));
	//// } else {
	//// bk.add(new block(i, j, ima, gameMap.map0[j][i]));
	//// }
	// }
	// if(locDif[1]>0){
	// for(int i=0;i<bk.size();i++){
	// if(bk.get(i).getJ()==p.J - view){
	// bk.remove(bk.get(i));
	// }
	// }
	// }
	// if(locDif[1]<0){
	// for(int i=0;i<bk.size();i++){
	// if(bk.get(i).getJ()==p.J + view){
	// bk.remove(bk.get(i));
	// }
	// }
	// }
	// }
	// }
	public void drawDropEnity(Graphics g) {
		for (int i = 0; i < drop_Enity.size(); i++) {

			drop_Enity.get(i).drawDrop(g);

		}
	}

	public void drawNPCs(Graphics g) {
		for (int i = 0; i < npcs.size(); i++) {
			npcs.get(i).draw(g);
		}
	}

	public void drawChatBubbles(Graphics g) {
		int amount = cb.size();
		for (int i = 0; i < amount; i++) {
			cb.get(i).draw(g);
		}
		for (int i = 0; i < amount; i++) {
			if (cb.get(i).drawBubble == false) {
				cb.remove(i);
				amount--;
				i--;
			}
		}
	}

	public void paint(Graphics g) {

		if (iBuffer == null) {
			iBuffer = createImage(this.getWidth(), this.getHeight());
			gBuffer = iBuffer.getGraphics();
		}

		gBuffer.setColor(Color.white);
		gBuffer.fillRect(0, 0, panelWidth, panelHeight);
		// gBuffer.drawImage(dayNight, -100, -100,
		// panelWidth+200,panelHeight*2+200,null);

		// bg.draw(gBuffer);

		if (firstInit == true) {
			gBuffer.setFont(new Font("΢���ź�", Font.PLAIN, 13));

			initBlock(gBuffer);// ������
			if (destroy == true) {
				block b = tools.getPosBlock(bI, bJ);
				int x = b.getX();
				int y = b.getY();
				gBuffer.drawImage(pic_destroyImg[destroyAmount], x, y, blockWidth, blockHeight, null);
			}

			gBuffer.setColor(Color.red);
			block b = tools.getPixBlock(mouseX, mouseY);
			if (Math.sqrt((p.I - b.getI()) * (p.I - b.getI()) + (p.J - b.getJ()) * (p.J - b.getJ())) <= 5) {
				gBuffer.drawRect(b.getX(), b.getY(), blockWidth, blockHeight);
			}

			drawDropEnity(gBuffer);// ��������Ʒ
			drawNPCs(gBuffer);

			// light.draw(gBuffer);
			p.draw(gBuffer);// ������
			drawChatBubbles(gBuffer);

		}
		d.draw(gBuffer);
		if (firstInit == true && Debug) {
			gBuffer.setColor(Color.red);
			gBuffer.drawString("block:" + bk.size(), 20, 50);
			gBuffer.drawString("isJumping:" + p.isJump, 20, 70);
			gBuffer.drawString("I:" + p.I + " J:" + p.J + " mx:" + p.mX + " my:" + p.mY, 20, 90);
			gBuffer.drawString("block_NAME:" + block.getName(block_ID), 20, 110);
			gBuffer.drawString("blockHP:" + block_HP, 20, 130);
			gBuffer.drawString("eatingPiece:" + eatingPiece, 20, 150);
			gBuffer.drawString("Select:" + select, 20, 170);
			gBuffer.drawString("PlayerHP:" + p.player_HP + "/" + p.player_MaxHP, 20, 190);
			gBuffer.drawString("ViewMod:" + ViewMod, 20, 210);
			gBuffer.drawString("CannotDothing:" + d.getCannotDothing(), 20, 230);
			gBuffer.drawString("MOUSE X:" + mouseX + " MOUSE Y:" + mouseY, 20, 250);
			int world[] = tools.getPixWorldXY(mouseX, mouseY);
			gBuffer.drawString("WORLD I:" + world[0] + " J:" + world[1] + " mX:" + world[2] + " mY:" + world[3], 20,
					270);
			gBuffer.drawString("TimeBetweenFrame:" + tools.timeResult + " ms", 20, 290);
			gBuffer.drawString("FPS:" + (int) tools.getFPS(), 20, 310);
		}
		if (firstInit) {
			chest.doThingOnMouseMoved(gBuffer);
			rb.doThingOnMouseMoved(gBuffer);
			bp.doThingOnMouseMoved(gBuffer);
		}
		g.drawImage(iBuffer, 0, 0, this);

	}

	/* ��������� */
	public void keyPressed(KeyEvent e) {
		// System.out.println("1");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			if (d.getCannotDothing() == false && !pause)
				p.jump(300);
			break;
		case KeyEvent.VK_A:
			if (d.getCannotDothing() == false && !pause)
				p.left = true;
			break;
		case KeyEvent.VK_D:
			if (d.getCannotDothing() == false && !pause)
				p.right = true;
			break;
		case KeyEvent.VK_E:// �򿪱���
			if (!pause)
				d.setDrawBackPack(!d.getDrawBackPack());
			break;
		case KeyEvent.VK_P:// ��ͣ
			tools.setPauseGame(!pause);
			break;
		case KeyEvent.VK_B:// Debug
			Debug = !Debug;
			if (ViewMod == true) {
				ViewMod = false;
				if (d.getDrawBackPack() == false) {
					d.setCannotDothing(false);
				}
				blockWidth = 30;
				blockHeight = blockWidth;
				playerWidth = blockWidth;
				playerHeight = playerWidth * 2;
			}
			break;
		case KeyEvent.VK_V:// �ӽ�ģʽ
			if (!pause && Debug) {
				if (ViewMod == false) {
					ViewMod = true;
					d.setCannotDothing(true);
				} else {
					ViewMod = false;
					if (d.getDrawBackPack() == false) {
						d.setCannotDothing(false);
					}
					blockWidth = 30;
					blockHeight = blockWidth;
					playerWidth = blockWidth;
					playerHeight = playerWidth * 2;
				}
			}
			break;
		case KeyEvent.VK_Q:// ������Ʒ
			if (!pause) {
				if (bp.bp_enity.size() >= select && bp.stackAmount.get(select - 1) >= 1) {
					bp.stackAmount.set(select - 1, bp.stackAmount.get(select - 1) - 1);// ����Ʒ
					// ��Ʒ���ڵ���
					drop_Enity.add(new Enity(bp.bp_enity.get(select - 1).getID(), p.I + 2, p.J));
				}
			}
			break;

		case KeyEvent.VK_1:
			if (!pause)
				select = 1;
			break;
		case KeyEvent.VK_2:
			if (!pause)
				select = 2;
			break;
		case KeyEvent.VK_3:
			if (!pause)
				select = 3;
			break;
		case KeyEvent.VK_4:
			if (!pause)
				select = 4;
			break;
		case KeyEvent.VK_6:
			if (!pause)
				select = 5;
			break;
		case KeyEvent.VK_7:
			if (!pause)
				select = 7;
			break;
		case KeyEvent.VK_8:
			if (!pause)
				select = 8;
			break;
		case KeyEvent.VK_9:
			if (!pause)
				select = 9;
			break;
		case KeyEvent.VK_0:
			if (!pause)
				select = 10;
			break;
		case KeyEvent.VK_ESCAPE:
			if (d.getDrawMainMenu() == false && d.getDrawChest() == false && d.getDrawBackPack() == false
					&& d.getCannotDothing() == false) {
				pause = !pause;
				d.setDrawOptionMenu(!d.getDrawOptionMenu());
			}
			d.setDrawChest(false);
			d.setDrawBackPack(false);
			d.setCannotDothing(false);
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			if (d.getCannotDothing() == false && !pause) {
				p.left = false;
				p.towards = 0;
			}
			break;
		case KeyEvent.VK_D:
			if (d.getCannotDothing() == false && !pause) {
				p.right = false;
				p.towards = 0;
			}
			break;
		}
		// System.out.println(player.X + " " + player.Y);
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (d.getDrawMainMenu()) {
			mm.doThingOnMouseMove(e);
		}
		if (d.getDrawOptionMenu()) {
			om.doThingOnMouseMove(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// ���
		if (e.getButton() == 1) {
			// gBuffer.drawRect(b.getX(), b.getY(), blockWidth, blockHeight);

			block bb = tools.getPixBlock(e.getX(), e.getY());
			if (d.getDrawMainMenu() == true) {
				mm.doThingOnMousePressed(e);
			} else if (d.getDrawOptionMenu() == true) {
				om.doThingOnMousePressed(e);
			} else if (d.getCannotDothing() == false && destroy == false && bb.getImg() != null && bb != null
					&& Math.sqrt((p.I - bb.getI()) * (p.I - bb.getI()) + (p.J - bb.getJ()) * (p.J - bb.getJ())) <= 5) {// can���ƻ�����
				block_ID = bb.getID();
				System.out.println("�ƻ���Ʒ");
				destroyBlock(bb);
			} else if (d.getDrawBackPack() == true) {// ������״̬
				bp.doThingOnMousePressed(e);
				rb.doThingOnMousePressed(e);
			} else if (d.getDrawChest() == true) {// ���Ӵ�״̬
				bp.doThingOnMousePressed(e);
				chest.doThingOnMousePressed(e);
			} else if (tools.getPixNPC(mouseX, mouseY) != null
					&& Math.sqrt((p.I - bb.getI()) * (p.I - bb.getI()) + (p.J - bb.getJ()) * (p.J - bb.getJ())) <= 2) { // ��������
				NPC n = tools.getPixNPC(mouseX, mouseY);
				if (n.canIntersect) {// �ܱ�����
					if (bp.stackAmount.get(select - 1) > 0) {
						Enity en = bp.bp_enity.get(select - 1);

						System.out.println("�����������˺�" + en.getAtk() + " ����" + n.hp);
						n.hp -= en.getAtk();
					} else {
						System.out.println("�����������˺�" + 1 + " ����" + n.hp);
						n.hp -= 1;
					}
					n.knockBack(p.getRect());
					if (n.hp <= 0)
						npcs.remove(n);
				}
			}

		}
		// �Ҽ�
		else if (e.getButton() == 3) {
			if (d.getCannotDothing() == false) {// ���������ڴ�״̬
				block bb = tools.getPixBlock(e.getX(), e.getY());
				if (bb != null && bb.getImg() != null && Math
						.sqrt((p.I - bb.getI()) * (p.I - bb.getI()) + (p.J - bb.getJ()) * (p.J - bb.getJ())) <= 5) {// �Ҽ������Ʒ����
					int skill = block.getSkill(bb.getID());
					switch (skill) {
					case 0:
						System.out.println("����_��ͨ��Ʒ");
						if (bb.getID() == 1) {// ���� ������
							Enity en = bp.bp_enity.get(select - 1);
							if (bp.stackAmount.get(select - 1) > 0 && en.getID() == 200
									&& tools.getPosBlock(bb.getI(), bb.getJ() - 1) == null) {// �ܲ�
								bp.stackAmount.set(select - 1, bp.stackAmount.get(select - 1).intValue() - 1);// ʹ��
								gameMap.map0[bb.getJ() - 1][bb.getI()] = 15;
							}
							else if (bp.stackAmount.get(select - 1) > 0 && en.getID() == 24
									&& tools.getPosBlock(bb.getI(), bb.getJ() - 1) == null) {// �ܲ�
								bp.stackAmount.set(select - 1, bp.stackAmount.get(select - 1).intValue() - 1);// ʹ��
								gameMap.map0[bb.getJ() - 1][bb.getI()] = 24;
							}
						} else if (bb.getID() == 19) {// ���²���
							gameMap.map0[bb.getJ()][bb.getI()] = 21;
							gameMap.map0[bb.getJ() - 1][bb.getI()] = 22;
						} else if (bb.getID() == 20) {// ���ϲ���
							gameMap.map0[bb.getJ()][bb.getI()] = 22;
							gameMap.map0[bb.getJ() + 1][bb.getI()] = 21;
						} else if (bb.getID() == 21) {// ���²���
							gameMap.map0[bb.getJ()][bb.getI()] = 19;
							gameMap.map0[bb.getJ() - 1][bb.getI()] = 20;
						} else if (bb.getID() == 22) {// ���ϲ���
							gameMap.map0[bb.getJ()][bb.getI()] = 20;
							gameMap.map0[bb.getJ() + 1][bb.getI()] = 19;
						}

						break;
					case 1:
						System.out.println("����_����");
						backpack.bI = bb.getI();
						backpack.bJ = bb.getJ();
						tools.readChest(bb.getI(), bb.getJ());
						d.setDrawChest(!d.getDrawChest());
						break;
					case 2:
						System.out.println("����_��¯");
						break;
					case 3:
						System.out.println("����_�ϳ�̨");
						rb.setSize(3);
						d.setDrawBackPack(!d.getDrawBackPack());
						break;
					case 4:
						System.out.println("����_��ħ̨");
						break;
					}
				} else if (tools.getPixNPC(mouseX, mouseY) != null && tools.getPixNPC(mouseX, mouseY).canInteract) {// ������NPC
					NPC n = tools.getPixNPC(mouseX, mouseY);
					ms.doThing(n);
					System.out.println("����_NPC");
				} else if (bp.stackAmount.get(select - 1) > 0) {// ������� ������Ʒ
					Enity en = bp.bp_enity.get(select - 1);
					for (int i = 0; i < npcs.size(); i++) {
						if (bb.getRect().intersects(npcs.get(i).getRect()) && npcs.get(i).canInteract == true) {
							return;
						}
					}
					if (en.getCanPut()) {// ����Ʒ�Ƿ���Է���

						if (bb != null && bb.getImg() == null && !bb.getRect().intersects(p.getRect()) && Math.sqrt(
								(p.I - bb.getI()) * (p.I - bb.getI()) + (p.J - bb.getJ()) * (p.J - bb.getJ())) <= 5) {// �ж��Ƿ���Է��÷���
							System.out.println("������Ʒ");
							if (en.getID() == 23) {// ������
								if (gameMap.map0[bb.getJ() - 1][bb.getI()] != 0) {
									return;
								} else {
									bp.stackAmount.set(select - 1, bp.stackAmount.get(select - 1).intValue() - 1);// ʹ��
									gameMap.map0[bb.getJ()][bb.getI()] = 19;
									gameMap.map0[bb.getJ() - 1][bb.getI()] = 20;
								}
							}else {
								bp.stackAmount.set(select - 1, bp.stackAmount.get(select - 1).intValue() - 1);// ʹ��
								gameMap.map0[bb.getJ()][bb.getI()] = en.getID();
							}
						}
					} else if (en.getCanEat()) {// ����Ʒ�Ƿ���Գ�
						System.out.println("��ʳ��");
						eatEnity(en);
					} else {// ������Ʒʹ�÷���
						// block b=tools.getPixBlock(mouseX, mouseY);
						// npc=new shoot(p.I, p.J, b.getI(), b.getJ());
						// npcs.add(npc);
					}
				}

			} else if (d.getDrawBackPack() == true) {// ������״̬ʹ���Ҽ�
				bp.doThingOnMousePressed(e);
				rb.doThingOnMousePressed(e);
			} else if (d.getDrawChest() == true) {// ���Ӵ�״̬
				bp.doThingOnMousePressed(e);
				chest.doThingOnMousePressed(e);
			}
		}
	}

	public void eatEnity(final Enity en) {
		if (eating == false && (float) hp.getHunger() / hp.getMaxHunger() <= 0.95) {
			new Thread(new Runnable() {
				public void run() {
					eating = true;
					eatingPiece = 20;
					d.setDrawProgress(true);
					Progress.setMax(eatingPiece);
					Progress.setNow(eatingPiece);
					while (eatingPiece > 0 && eating == true) {
						eatingPiece -= 1;
						Progress.setNow(eatingPiece);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					eating = false;
					d.setDrawProgress(false);
					if (eatingPiece <= 0) {

						bp.stackAmount.set(select - 1, bp.stackAmount.get(select - 1).intValue() - 1);// ʹ��
						hp.setHunger(hp.getHunger() + en.getHungerHeal());// ���Ӽ�����
						if (hp.getHunger() > hp.getMaxHunger()) {
							hp.setHunger(hp.getMaxHunger());
						}
						eatingPiece = 20;
					}
				}
			}).start();
		}
	}

	public static void destroyBlockDirect(int i, int j) {// ֱ���ƻ�����
		int id = gameMap.map0[j][i];
		gameMap.map0[j][i] = 0;
		newDropEnity(block.getDropID(id), i, j, block.getDropAmount(id));
		if (block.getName(id).contains("����")) {// �����е���Ʒ����
			tools.readChest(i, j);
			for (int ii = 0; ii < chest.bp_enity.size(); ii++) {
				for (int jj = 0; jj < chest.stackAmount.get(ii); jj++) {
					Enity e = new Enity(chest.bp_enity.get(ii).getID(), i, j);
					e.setBreak(chest.bp_enity.get(ii).getBreak());
					drop_Enity.add(e);
				}
				chest.stackAmount.set(ii, 0);
			}
			File f = new File(path_chest + i + "-" + j + ".dat");
			if (f.exists())
				f.delete();
		}
	}

	public void destroyBlock(final block bb) {
		bI = bb.getI();
		bJ = bb.getJ();
		int id = bb.getID();
		double percent = 0.0;
		int HP = block.getHP(bb.getID());
		Enity en = bp.bp_enity.get(select - 1);
		if (block.getHP(bb.getID()) == -1)
			return;// �޷��ݻ�
		if (bp.stackAmount.get(select - 1) > 0) {

			int amount = bp.stackAmount.get(select - 1);
			if (amount > 0) {
				percent = tools.getPercent(block.getType(bb.getID()), en.getType(), en.getToolType());
			} else {
				percent = 1.0;
			}
		} else {// ����
			percent = 1.0;
		}
		block_HP = (int) (percent * HP);
		d.setDrawProgress(true);
		Progress.setMax(block_HP);
		Progress.setNow(block_HP);
		final int t_hp = HP;
		final double pp = percent;// percent�����������ת
		if (destroy == false) {
			new Thread(new Runnable() {
				public void run() {
					destroy = true;

					while (block_HP > 0 && destroy == true) {
						block_HP -= 20;
						Progress.setNow(block_HP);
						double rr = (double) block_HP / (pp * t_hp);
						destroyAmount = (int) (rr * 10);// destroyͼƬ�±�
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					destroy = false;
					d.setDrawProgress(false);
					if (block_HP <= 0) {
						gameMap.map0[bJ][bI] = 0;
						if (bb.getID() == 19 || bb.getID() == 21) {// ���²�
							gameMap.map0[bJ - 1][bI] = 0;
						}
						if (bb.getID() == 20 || bb.getID() == 22) {// ���ϲ�
							gameMap.map0[bJ + 1][bI] = 0;
						}
						if (en.getBreak() >= 1) {// �����;ö�
							en.setBreak(en.getBreak() - 1);
						} else if (en.getBreak() < 1 && en.getBreak() != -1) {// ���;��������;öȵĹ���
							bp.stackAmount.set(select - 1, 0);
						}
						newDropEnity(block.getDropID(bb.getID()), bb.getI(), bb.getJ(),
								block.getDropAmount(bb.getID()));
						if (block.getName(id).contains("����")) {// �����е���Ʒ����
							tools.readChest(bI, bJ);
							for (int ii = 0; ii < chest.bp_enity.size(); ii++) {
								for (int jj = 0; jj < chest.stackAmount.get(ii); jj++) {
									Enity e = new Enity(chest.bp_enity.get(ii).getID(), bI, bJ);
									e.setBreak(chest.bp_enity.get(ii).getBreak());
									drop_Enity.add(e);
								}
								chest.stackAmount.set(ii, 0);
							}
							File f = new File(path_chest + bb.getI() + "-" + bb.getJ() + ".dat");
							if (f.exists())
								f.delete();
						}
						block_HP = 0;
					}
				}
			}).start();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1 && destroy == true) {
			destroy = false;
		}
		if (e.getButton() == 3 && eating == true) {
			eating = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if (pause == false) {
			if (ViewMod == true) {
				if (e.getWheelRotation() == -1) {
					if (blockWidth <= 60) {
						blockWidth += 2;
						blockHeight = blockWidth;
						playerWidth = blockWidth;
						playerHeight = playerWidth * 2;
					}
				}
				if (e.getWheelRotation() == 1) {
					if (blockWidth >= 10) {
						p.mX = 0;
						blockWidth -= 2;
						blockHeight = blockWidth;
						playerWidth = blockWidth;
						playerHeight = playerWidth * 2;
					}
				}
			} else if (ViewMod == false) {
				destroy = false;
				eating = false;
				if (e.getWheelRotation() == 1) {

					if (select == 10) {
						select = 1;
					} else {
						select += 1;
					}
				}
				if (e.getWheelRotation() == -1) {
					if (select == 1) {
						select = 10;
					} else {
						select -= 1;
					}
				}
			}
		}
	}
}
