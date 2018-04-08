package GameGraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import GameLogic.Enity;
import GameResource.gameConfig;

public class display implements gameConfig {
	private boolean drawEquipColumn = false;// ��װ����
	private boolean cannotDothing = false;// ��ĳЩ���治������
	private boolean drawBackPack = false;// ������
	private boolean drawChest = false;// ������
	private boolean drawHP = false;// ��HP
	private boolean drawProgress = false;// ��������
	private boolean drawMainMenu = true;// ����Ϸ����˵�
	private boolean drawOptionMenu = false;// ����Ϸ���ò˵�

	public boolean getCannotDothing() {
		return cannotDothing;
	}

	public void setCannotDothing(boolean b) {
		mainFrame.p.up = false;
		mainFrame.p.left = false;
		mainFrame.p.right = false;
		cannotDothing = b;
	}

	public boolean getDrawHP() {
		return drawHP;
	}

	public boolean getDrawEquipColumn() {
		return drawEquipColumn;
	}

	public boolean getDrawBackPack() {
		return drawBackPack;
	}

	public boolean getDrawChest() {
		return drawChest;
	}

	public boolean getDrawProgress() {
		return drawProgress;
	}

	public boolean getDrawMainMenu() {
		return drawMainMenu;
	}

	public boolean getDrawOptionMenu() {
		return drawOptionMenu;
	}

	public void setDrawMainMenu(boolean b) {
		drawMainMenu = b;
	}

	public void setDrawOptionMenu(boolean b) {
		drawOptionMenu = b;
	}

	public void setDrawHP(boolean b) {
		drawHP = b;
	}

	public void setDrawProgress(boolean b) {
		drawProgress = b;
	}

	public void setDrawEquipColumn(boolean b) {
		drawEquipColumn = b;

	}

	public void setDrawBackPack(boolean b) {
		drawBackPack = b;
		if (b == false) {

			for (int i = 0; i < mainFrame.rb.bp_enity.size(); i++) {// ����ϳɱ�����Ʒ
				if ((i + 1) % mainFrame.rb.line != 0) {
					Enity enity = mainFrame.rb.bp_enity.get(i);
					int amount = mainFrame.rb.stackAmount.get(i);
					mainFrame.newDropEnity(enity.getID(), mainFrame.p.I + 2, mainFrame.p.J, amount);
					mainFrame.rb.stackAmount.set(i, 0);
				}
			}
			mainFrame.rb.setSize(2);
			mainFrame.rb.judge();
		}
		if (mainFrame.ViewMod == false)
			setCannotDothing(b);
	}

	public void setDrawChest(boolean b) {
		drawChest = b;
		if (mainFrame.ViewMod == false)
			setCannotDothing(b);
	}

	public void draw(Graphics g) {
		if (drawEquipColumn) {// ��װ����
			int a = 0;
			for (int i = 0; i < 10; i++) {
				g.drawImage(pic_Rect, mainFrame.panelWidth / 2 - 250 + i * 50, mainFrame.panelHeight - 100, 50, 50,
						null);
				g.drawImage(pic_Select, mainFrame.panelWidth / 2 - 250 + (mainFrame.select - 1) * 50,
						mainFrame.panelHeight - 100, 50, 50, null);
				
				if (a < mainFrame.bp.bp_enity.size()) {
					if (mainFrame.bp.stackAmount.get(a) > 0) {
						Enity e = mainFrame.bp.bp_enity.get(a);
						g.drawImage(e.getImg(), mainFrame.panelWidth / 2 - 250 + i * 50 + 10,
								mainFrame.panelHeight - 100 + 10, 50 - 20, 50 - 20, null);

						g.setColor(Color.BLACK);
						if (e.getBreak() != -1) {// ����ѵ�����Ϊ1 �� ӵ���;öȵ���Ʒ
							g.drawString(String.valueOf(e.getBreak()),
									mainFrame.panelWidth / 2 - 250 + (i + 1) * 50 - 30,
									mainFrame.panelHeight - 100 + (1) * 50 - 10 - 20 - 4);
						} else {// ��ͨ��Ʒ�ͻ����ѵ�����
							g.drawString(String.valueOf(mainFrame.bp.stackAmount.get(a)),
									mainFrame.panelWidth / 2 - 250 + (i + 1) * 50 - 23,
									mainFrame.panelHeight - 100 + (1) * 50 - 10);
						}
					}
					a++;
				}
			}
		}
		if (drawBackPack) {// ������
			mainFrame.bp.draw(g);
			mainFrame.rb.draw(g);
		}
		if (drawChest) {// ������
			mainFrame.chest.draw(g);
			mainFrame.bp.draw(g);
		}
		if (drawHP) {// ��HP
			mainFrame.hp.draw(g);
		}
		if (drawProgress) {// ��������
			mainFrame.Progress.draw(g);
		}
		if (drawMainMenu) {// �����˵�
			mainFrame.mm.draw(g);
		}
		if (drawOptionMenu) {// �����ò˵�
			mainFrame.om.draw(g);
		}
	}
}
