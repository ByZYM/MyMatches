package GameMission;

import GameGraphics.mainFrame;
import GameTool.tools;
//�ռ�����
public class missionCollection extends missionStandard {
	boolean accept = false;// �Ƿ��Ѿ���������
	int id;

	public missionCollection(){
	}

	public void setEnityID(int i){
		id=i;
	}
	public void doMission() {
		try {
			if (doThing == true && (doAmount == -1 || doAmount > 0)) {
				if (accept == false && n.getName().equals(npc_Public)) {// ���ִ�Сд����
					accept = true;
					saySomething(publicDialog);
				} else if (accept == true && n.getName().equals(npc_Finish) && tools.getEnityOnHand().getID() == id) {// ���ִ�Сд����
					mainFrame.bp.stackAmount.set(mainFrame.select-1,mainFrame.bp.stackAmount.get(mainFrame.select-1)-1);
					saySomething(finishDialog);
					if (doAmount != -1) {
						doAmount--;
					}
					setFinish(true);//�������������һ��
					accept = false;
				}
				doThing = false;
			}
		} catch (Exception e) {
		}
	}
}
