package luk;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.*;

public class SimpleAnimation {

	public static void main(String[] args) {
		
		WindowFrame ventana = new WindowFrame(350,300, "Main Window");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		

	}

}

class WindowFrame extends JFrame{
	
	String title;
	WindowPanel panel = new WindowPanel();
	int objectsOnFrame = 0;
	
	RectanguloMov rect1 = new RectanguloMov(20,20,30,30,0);
	RectanguloMov rect2 = new RectanguloMov(313,290,30,30,1);

	
	Keys keys1 = new Keys(rect1.id);
	Keys keys2 = new Keys(rect2.id);
	
	public WindowFrame(int x, int y, String title) {
		
	setBounds(x,y,400,400);
	setTitle(title);
	this.title = title;
	
	addObjetoAnimado(rect1);
	addObjetoAnimado(rect2);
	
	setVisible(true);
	
	
	add(panel);
	addKeyListener(keys1);
	addKeyListener(keys2);
	
	
	Thread thread = new Thread(new Animation(this));	
	thread.start();
	
	}
	
	public void actualizar(WindowPanel panel, Keys keysMoving, Keys keysQuiet) {		
		
		panel.setRect(panel.r.get(keysMoving.player).movimiento(panel.getBounds(), keysMoving.Direction)); 
		panel.setRect(panel.r.get(keysQuiet.player)); 

		panel.repaint();		
		
		
	}
	
	public void addObjetoAnimado(RectanguloMov re) {
		
		panel.setRect(re);
		panel.createdObjects++;
	
	}
	
}

class WindowPanel extends JPanel{
	
	public ArrayList<RectanguloMov> r = new ArrayList<>();
	public ArrayList<Rectangle2D> r2d = new ArrayList<>();
	int createdObjects = 0;
	
	
	public void setRect(RectanguloMov rect) {
		r.add(rect);
		r2d.add(new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height));
	} 
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		Rectangle2D rect;
		Rectangle2D ojoIzq, ojoDer, ojoIzqP, ojoDerP, boca;


		for (int i=0; i< createdObjects; i++) {
		rect = r2d.get(r2d.size()-i-1);
		
		g2.setColor(new Color(30,30,200));
		g2.fill(rect);
		g2.setColor(new Color(100,255,255));
		g2.draw(rect);
		
		
		
		ojoIzq = new Rectangle2D.Double(
				rect.getWidth()/5 + rect.getMinX(), 
				rect.getHeight()/5 + rect.getMinY(), 
				rect.getWidth()/5, 
				rect.getHeight()/5);
		
		ojoIzqP = new Rectangle2D.Double(
				3*rect.getWidth()/10 + rect.getMinX(), 
				3*rect.getHeight()/10 + rect.getMinY(), 
				rect.getWidth()/10, 
				rect.getHeight()/10);
		
		ojoDer = new Rectangle2D.Double(
				3*rect.getWidth()/5 + rect.getMinX(), 
				rect.getHeight()/5 + rect.getMinY(), 
				rect.getWidth()/5, 
				rect.getHeight()/5);
		
		ojoDerP = new Rectangle2D.Double(
				7*rect.getWidth()/10 + rect.getMinX(), 
				3*rect.getHeight()/10 + rect.getMinY(), 
				rect.getWidth()/10, 
				rect.getHeight()/10);
		
		boca = new Rectangle2D.Double(
				rect.getWidth()/5 + rect.getMinX(),
				32*rect.getHeight()/50 + rect.getMinY(),
				3*rect.getWidth()/5 ,
				rect.getHeight()/7);
		
		
		
		g2.setColor(new Color(255,255,255));
		g2.draw(ojoIzq);
		g2.fill(ojoIzq);
		
		g2.draw(ojoDer);
		g2.fill(ojoDer);
		
		g2.draw(boca);
		g2.fill(boca);
		
		g2.setColor(new Color(0,0,0));
		
		g2.draw(ojoIzqP);
		g2.fill(ojoIzqP);
		g2.draw(ojoDerP);
		g2.fill(ojoDerP);
		
		
		}
		
		
	}
	
}

class RectanguloMov {
	
	int x;
	int y;
	int width;
	int height;
	
	public int id;
	
	private int dx = 30;
	private int dy = 30;
	
	private final int default_dx = 30;
	private final int default_dy = 30;
	
	private boolean noLeft;
	private boolean noRight;
	private boolean noUp;
	private boolean noDown;
	
	public RectanguloMov(int x, int y, int width, int height, int id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
	}

	public RectanguloMov movimiento(Rectangle2D limits, String Direction) {
				
		if(x+dx+width > limits.getMaxX()  && Direction == "Right") { //Checkea el borde derecho
			
			dx = (int)(limits.getMaxX() - (x+width) - 1);
			
			movimiento2(limits, Direction);
			
			noRight = true;
			
			dx = default_dx;
			
		} else { noRight = false;}
		
		if(x-dx < 0 && Direction == "Left") { //Checkea el borde izquierdo
			
			dx = x;
			
			movimiento2(limits, Direction);
			
			noLeft = true;
			
			dx = default_dx;
			
		} else { noLeft = false;}
		
		if(y-dy < 0  && Direction == "Up") { //Checkea el borde superior
			
			dy = y;
			
			movimiento2(limits, Direction);
			
			noUp = true;
			
			dy = default_dy;
			
		} else { noUp = false;}
		
		if(y+dy+height> limits.getMaxY()  && Direction == "Down") { //Checkea el borde inferior
			
			dy = (int)(limits.getMaxY() - (y+height) - 1);
			
			movimiento2(limits, Direction);
			
			noDown = true;
			
			dy = default_dy;
			
		} else { noDown = false;}
		
		movimiento2(limits, Direction);
		
		return this;
		
	}
	
	public void movimiento2(Rectangle2D limits, String Direction) {
		
		if (Direction == "Right" && noRight != true) {
			x+=dx;
		}
		if (Direction == "Left" && noLeft != true) {
			x-=dx;
		}
		if (Direction == "Down" && noDown != true) {
			y+=dy;
		}
		if (Direction == "Up" && noUp != true) {
			y-=dy;
		}
		
	}
	
	
	
}


class Animation implements Runnable{

	private WindowFrame frame;
	
	public Animation(WindowFrame frame) {
		this.frame = frame;
	}
	
	
	@Override
	public void run() {
		
		while (true) {

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(frame.keys1.isMoving) {
		frame.actualizar(frame.panel, frame.keys1, frame.keys2);
		frame.keys1.isMoving = false;
		} else if(frame.keys2.isMoving) {
			frame.actualizar(frame.panel, frame.keys2, frame.keys1);
			frame.keys2.isMoving = false;
			}
		}
		
	}

}

class Keys implements KeyListener {

	public KeyEvent event;
	public boolean isMoving = false;
	public String Direction;
	public int player;
	
	public Keys(int player) {
		this.player = player;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (this.player==0) {
				
		if(e.getKeyCode() == KeyEvent.VK_D) {
			Direction = "Right";
			isMoving = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_A) {
			Direction = "Left";
			isMoving = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			Direction = "Up";
			isMoving = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			Direction = "Down";
			isMoving = true;
		}
		} 
		if (this.player == 1) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Direction = "Right";
			isMoving = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			Direction = "Left";
			isMoving = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			Direction = "Up";
			isMoving = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			Direction = "Down";
			isMoving = true;
		}
		}
		
	}
	
	public String direction() {
		return Direction;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}