package GameGraphics;

import javax.swing.JFrame;

import GameTool.tools;

/*ˢ����������߳�*/
public class updateThread implements Runnable {
	JFrame j;
	public static int stdTime=0;
	updateThread(JFrame j) {
		this.j = j;
	}

	public void run() {
		while (true) {
			tools.getTimeBetween();
			j.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			stdTime++;
			if (stdTime == 10000) {//  100������
				stdTime = 1;
			}
		}
	}
}
