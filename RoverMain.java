import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

public class RoverMain extends JFrame implements Runnable, KeyListener {
	private BufferedImage bi = null;
	
	private ArrayList rvList = null;
	private int LSize;
	
	private boolean 	flag_start = false;
	private int 		window_w = 400, window_h = 400;
	
	private double		CurrentTime = 0.0, LastTime = 0.0, TimeElapsed = 0.0;

	// obstacle objects 변수 선언
	int NumObs = 4;
	VectorArray obVts[];
	 
	RoverMain() {
		bi = new BufferedImage(window_w, window_h, BufferedImage.TYPE_INT_RGB); 
		rvList = new ArrayList();
		LSize  = 2;

		this.addKeyListener(this);
		this.setSize(window_w, window_h);
		this.setTitle("Rover");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		// obstacle objects 초기화
		obVts = new VectorArray[NumObs];
		
		obVts[0] = new VectorArray();
		obVts[0].Size = 8;
		obVts[0].TheVec = new Vector<Point>();
		obVts[0].TheVec.add(new Point(12,15));obVts[0].TheVec.add(new Point(380,15)); 		obVts[0].TheVec.add(new Point(380,15));obVts[0].TheVec.add(new Point(380,360)); 	
		obVts[0].TheVec.add(new Point(380,360));obVts[0].TheVec.add(new Point(12,360));		obVts[0].TheVec.add(new Point(12,360)); obVts[0].TheVec.add(new Point(12,15)); 

		obVts[1] = new VectorArray();
		obVts[1].Size = 12;
		obVts[1].TheVec = new Vector<Point>();
		obVts[1].TheVec.add(new Point(80, 60));obVts[1].TheVec.add(new Point(200,60)); 		obVts[1].TheVec.add(new Point(200,60));obVts[1].TheVec.add(new Point(200,100)); 	
		obVts[1].TheVec.add(new Point(200,100));obVts[1].TheVec.add(new Point(160,100));	obVts[1].TheVec.add(new Point(160,100));obVts[1].TheVec.add(new Point(160,200)); 	
		obVts[1].TheVec.add(new Point(160,200));obVts[1].TheVec.add(new Point(80,200)); 	obVts[1].TheVec.add(new Point(80,200));obVts[1].TheVec.add(new Point(80,60)); 


		obVts[2] = new VectorArray();
		obVts[2].Size = 8;
		obVts[2].TheVec = new Vector<Point>();
		obVts[2].TheVec.add(new Point(220,180));obVts[2].TheVec.add(new Point(320,180)); 	obVts[2].TheVec.add(new Point(320,180));obVts[2].TheVec.add(new Point(320,300)); 	
		obVts[2].TheVec.add(new Point(320,300));obVts[2].TheVec.add(new Point(220,300));	obVts[2].TheVec.add(new Point(220,300));obVts[2].TheVec.add(new Point(220,180));

		obVts[3] = new VectorArray();
		obVts[3].Size = 8;
		obVts[3].TheVec = new Vector<Point>();
		obVts[3].TheVec.add(new Point(250,60)); obVts[3].TheVec.add(new Point(320,60)); 	obVts[3].TheVec.add(new Point(320,60));obVts[3].TheVec.add(new Point(320,100)); 	
		obVts[3].TheVec.add(new Point(320,100));obVts[3].TheVec.add(new Point(250, 100));	obVts[3].TheVec.add(new Point(250, 100));obVts[3].TheVec.add(new Point(250,60));
					
					
	}

	public void run() {
		try {
			int Cnt = 0;
			while(true) {
				Thread.sleep(10);
				CurrentTime = (System.nanoTime() / 1000000000.0);
				TimeElapsed = CurrentTime - LastTime;
				LastTime = CurrentTime;
				
				if(Cnt >= 100) { 
					createRover();	
					Cnt = 0;
				}

				Cnt += 10;

				draw(TimeElapsed); 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createRover() {
		if(flag_start) {
			for(int i=0; i < LSize; i++){
				Point StartPos = new Point(175+35*i, 200);
				// StartPos를 매개변수로 하여 Rover 객체를 생성
				// 만든 객체를 ArrayList rvList에 추가
				Rover m = new Rover(StartPos);
				rvList.add(m);
			}
		}
	}
	
	public void draw(double elapsedtime) {
		Graphics getgs1 = bi.getGraphics();

		getgs1.setColor(Color.WHITE);
		getgs1.fillRect(0, 0, window_w, window_h);
		
		// draw obstacle objects
		getgs1.setColor(Color.BLACK);
		int[] x1 = new int[obVts[0].Size];
		int[] y1 = new int[obVts[0].Size];
		for(int j=0; j < obVts[0].Size; ++j){
			x1[j] = (int)obVts[0].TheVec.get(j).x;	y1[j] = (int)obVts[0].TheVec.get(j).y;  
		}
		getgs1.drawPolygon(x1, y1, obVts[0].Size);
		
		getgs1.setColor(Color.BLACK);
		int[] x2 = new int[obVts[1].Size];
		int[] y2 = new int[obVts[1].Size];
		for(int j=0; j < obVts[1].Size; ++j){
			x2[j] = (int)obVts[1].TheVec.get(j).x;	y2[j] = (int)obVts[1].TheVec.get(j).y;  
		}
		getgs1.drawPolygon(x2, y2, obVts[1].Size);
		
		getgs1.setColor(Color.BLACK);
		int[] x3 = new int[obVts[2].Size];
		int[] y3 = new int[obVts[2].Size];
		for(int j=0; j < obVts[2].Size; ++j){
			x3[j] = (int)obVts[2].TheVec.get(j).x;	y3[j] = (int)obVts[2].TheVec.get(j).y;  
		}
		getgs1.drawPolygon(x3, y3, obVts[2].Size);
		
		getgs1.setColor(Color.BLACK);
		int[] x4 = new int[obVts[3].Size];
		int[] y4 = new int[obVts[3].Size];
		for(int j=0; j < obVts[3].Size; ++j){
			x4[j] = (int)obVts[3].TheVec.get(j).x;	y4[j] = (int)obVts[3].TheVec.get(j).y;  
		}
		getgs1.drawPolygon(x4, y4, obVts[3].Size);

		for(int i = 0; i < rvList.size(); i++) {
			//ArrayList에 저장된 각 Rover에 대해서
			// 꼭지점들로 DrawPolygon 메소드를 적용하여 Rover를 그린다
			Rover rv = (Rover)rvList.get(i);
			getgs1.setColor(Color.RED);
	
			int[] x = new int[rv.NumRoverVt];
			int[] y = new int[rv.NumRoverVt]; 
			for(int j=0; j < rv.NumRoverVt; ++j){
				x[j] = (int)rv.RoverTransVB.get(j).x;		y[j] = (int)rv.RoverTransVB.get(j).y;
			}
			getgs1.drawPolygon(x, y, rv.NumRoverVt);

			getgs1.setColor(Color.BLACK);
			int[] sx = new int[rv.NumSensorVt];
			int[] sy = new int[rv.NumSensorVt]; 
			for(int j=0; j < rv.NumSensorVt; ++j){
				sx[j] = (int)rv.SensorTransVB.get(j).x;		sy[j] = (int)rv.SensorTransVB.get(j).y;
			}
			getgs1.drawPolygon(sx, sy, rv.NumSensorVt);
			
			rv.moveRover(elapsedtime, obVts);
		}

		getgs1 = this.getGraphics();
		getgs1.drawImage(bi, 0, 0, window_w, window_h, this);		
	}

	public void keyPressed(KeyEvent ke) {
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_ENTER: 
			flag_start = true;
			break;
		}
	}

	public void keyReleased(KeyEvent ke) {	
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_ENTER: 
			flag_start = false;
			break;
		}
	}

	public void keyTyped(KeyEvent ke) {}

	public static void main(String[] args) {		
		Thread t = new Thread(new RoverMain());	 
		t.start();
	}
}