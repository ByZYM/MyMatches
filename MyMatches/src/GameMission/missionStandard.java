package GameMission;

import GameGraphics.mainFrame;
import GameLogic.NPC;

public abstract class missionStandard {
	String publicDialog[] = { "��ã���ĵ�һ����������һ����ľ��", "�����˽�����Ŷ" };// ���񷢲��ĶԻ��ı�
	String finishDialog[] = { "лл�����ľ��" };// ������ɵĶԻ��ı�
	NPC n = null;
	String npc_Public=null;
	String npc_Finish=null;
	public boolean finish=false;//�Ƿ�������ɹ�һ��
	int doAmount = -1;// �����ִ�д��� -1Ϊ�ظ� Ϊ0�Ͳ�����
	public boolean doThing = false;// ��������
	public abstract void doMission();
	
	public void setFinish(boolean b){
		finish=b;
	}
	public boolean getFinish(){
		return finish;
	}
	public void setDoAmount(int i){//�����������ɴ��� -1Ϊ�ظ����
		doAmount=i;
	}
	public void setPublicNPC(String n){//�������񷢲�NPC
		npc_Public=n;
	}
	public void setFinishNPC(String n){//�����������NPC
		npc_Finish=n;
	}
	public void setPublicDialog(String s[]){
		publicDialog=s;
	}
	public void setFinishDialog(String s[]){
		finishDialog=s;
	}
	public void saySomething(String a[]) throws InterruptedException {
		for (int i = 0; i < a.length; i++) {
			mainFrame.newChatBubble(n, 2000, a[i]);
			Thread.sleep(2000);
		}
	}
	public NPC getNPC() {
		return n;
	}
	public void setNPC(NPC nn) {
		n = nn;
	}
	public void doThing(NPC n) {//�������
		if (doThing == false) {
			doThing = true;
			setNPC(n);
			doMission();
		}
	}
}
