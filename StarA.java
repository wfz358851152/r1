package com.map;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

import com.sun.javafx.scene.text.HitInfo;

public class StarA extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8586644509356186675L;
	private final char wallKey='w';
    private final char targetKey='t';
    private final char WallKey='W';
    private final char TargetKey='T';
    private final Color wallColor=Color.red;
    private final Color targetColor=Color.blue;
    private final Color antColor=Color.yellow;
	private Font wallFont = new Font("ﾎ｢ﾈ樰ﾅｺﾚ",Font.BOLD,15);
    private Font targetFont = new Font("ﾎ｢ﾈ樰ﾅｺﾚ",Font.PLAIN,15);
    private Font[] Fonts = new Font[] {
    		new Font("ﾎ｢ﾈ樰ﾅｺﾚ",Font.PLAIN,15),
    		new Font("ﾎ｢ﾈ樰ﾅｺﾚ",Font.PLAIN,18),
    		new Font("ﾎ｢ﾈ樰ﾅｺﾚ",Font.PLAIN,21),};
    private int width=800;
    private int height=600;
    private int realwidth=800;
    private int realheight=600;
    private boolean isNotQ=false;
    private int blocksize=30;
    private int strokesize=4;
    private int linesize=1;
    private int localx=strokesize+2,localy=strokesize+2;
    private OrderedPoint[] orderedPoints=new OrderedPoint[20000];
    private OrderedPoint headPoint=new OrderedPoint(new Point(0,0), 0);
    private Ant[] ants=new Ant[20000];
    private int xlength=0,ylength=0;
    private int maxxlength=400,maxylength=400;
    private Color[][] map=new Color[maxxlength][maxylength];
    private double[][] value=new double[maxxlength][maxylength];
    private static Set<Color> cleaned=new HashSet<Color>();
    private boolean started=false;
    private boolean finished=false;
    private final Ant firstAnt=new Ant(0,0);
    public void initRoad() {
    	value=new double[maxxlength][maxylength];
    	ants[0].initSelf();
    	started=false;
    	orderedPoints=new OrderedPoint[20000];
    	headPoint=new OrderedPoint(firstAnt.site, 0);
    }
    public void initAll() {	
    	initRoad();
    	map=new Color[maxxlength][maxylength];
    	cleaned=new HashSet<Color>();
    	ants[0]=new Ant(firstAnt);
    	xlength=0;
    	ylength=0;	
    	realwidth=800;
        realheight=600; 
        repaint();
	}
	public StarA(){
        String lookAndFeel =UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e1) {
            // TODO ﾗﾔｶｯﾉ嵭ﾉｵﾄ catch ｿ�
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO ﾗﾔｶｯﾉ嵭ﾉｵﾄ catch ｿ�
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO ﾗﾔｶｯﾉ嵭ﾉｵﾄ catch ｿ�
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            // TODO ﾗﾔｶｯﾉ嵭ﾉｵﾄ catch ｿ�
            e1.printStackTrace();
        }
//        addMouseWheelListener(new MouseWheelListener() {
//			
//			@Override
//			public void mouseWheelMoved(MouseWheelEvent e) {
//				switch (e.getWheelRotation()) {
//				case 1:
//					System.out.println("back");
//					if (blocksize>10) blocksize-=1;
//					
//					break;
//
//				case -1:
//					System.out.println("forward");
//					blocksize+=1;
//					break;
//				default:
//					break;
//				};
//				repaint();
//			}
//		});
        addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
        			search(StarA.this.ants[0]);
        			return;
        		}
        		if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
        			initAll();
        			return;
        		}
        		switch (e.getKeyChar()) {
				case wallKey:
					System.out.println("!!!");
					cleaned.add(wallColor);
					break;
				case WallKey:
					cleaned.add(wallColor);
					break;
				case TargetKey:
					System.out.println("------");
					cleaned.add(targetColor);
					break;
				case targetKey:
					System.out.println("-------");
					cleaned.add(targetColor);
					break;
				default:
					break;
				}
        	}
     
        	@Override
        	public void keyReleased(KeyEvent e) {
        		switch (e.getKeyChar()) {
				case wallKey:
					cleaned.remove(wallColor);
					break;
				case WallKey:
					cleaned.remove(wallColor);
					break;
				case TargetKey:
					cleaned.remove(targetColor);
					break;
				case targetKey:
					cleaned.remove(targetColor);
					break;
				default:
					break;
				}
        	}
		});
        addMouseListener(new MouseAdapter() {
        	boolean isdblclick=false;	
			@Override
			public void mouseClicked(MouseEvent e) {
				int x=e.getX()-(localx+linesize+strokesize);
				int y=e.getY()-(localy+linesize+strokesize);
				if (e.getClickCount()==2) {
					isdblclick =true;
				}
				if (e.getClickCount()==1) {
					Thread thread=new Thread() {
						public void run() {
							try {
								Thread.sleep(325);
								if (isdblclick) {
									dblclick(x,y);
									isdblclick=false;
								}else {
									click(x,y);
								}
								repaint();
							} catch (InterruptedException e) {
			
								e.printStackTrace();
							}
						}
					};
					thread.start();
				}
				
			}
		});
        addMouseMotionListener(new MouseMotionListener() {	
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
		
			@Override
			public void mouseDragged(MouseEvent e) {
				int x=e.getX()-(localx+linesize+strokesize);
				int y=e.getY()-(localy+linesize+strokesize);
				x/=blocksize+linesize;
				y/=blocksize+linesize;		
				System.out.println("mouse="+x+","+y);
				if (insideBorder(new Ant(x,y))) {
				map[x][y]=turnColor(map[x][y], wallColor);
				repaint();
				}
			}
			
				
			
		});
        setFocusable(true);
    }
    public static Color turnColor(Color colora,Color colorb) {
    	if (colora==null) {
    		return checkColor(colorb);
    	}
		return checkColor(colora);
    }
    public static Color checkColor(Color color) {
    	if (cleaned.contains(color)){
    		return null;
    	}
    	return color;
    }
    public boolean resizeMap(int xlength,int ylength) {
    	if (maxxlength>xlength || maxylength>ylength) return false;
    	
    	maxxlength=xlength;
    	maxylength=ylength;
    	return true;
    }
    public void initPaint(Graphics2D g) {
        g.setPaint(new GradientPaint(height/2,width/2,Color.CYAN,height,width,Color.RED,true));
    	g.setStroke( new BasicStroke(linesize,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
        int x1=0,x2=0,y1=0,y2=0;
        int i=0,j=0;
        int h=localy+height-strokesize;
        int w=localx+width-strokesize;
        ylength=0;
        xlength=0;
        for(i = localx+strokesize;i <w;i+=blocksize+linesize){       
            
    	}
        w=i;
        for(j = localy+strokesize;j < h;j+=blocksize+linesize){ 
        	y1=j;
        	y2=j;
        	x1=localx+strokesize;
        	x2=w;
        	ylength++;
        	g.drawLine(x1,y1,x2,y2);
        }
        w=localx+width-strokesize;
        h=j;
        for(i = localx+strokesize;i <w;i+=blocksize+linesize){       
            x1=i;
            x2=i;
            y1=localy+strokesize;
            y2=h;
            xlength++;
        	g.drawLine(x1,y1,x2,y2);
    	}
        g.setStroke( new BasicStroke(strokesize*2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
        
        g.drawRect(localx, localy, i+strokesize-localx, j+strokesize-localy);
	
        realwidth=i+strokesize-localx;
        realheight=j+strokesize-localy;
    }
    public void drawPoint(Point a,Graphics2D g) {
//    	g.setPaint(new GradientPaint(height/2,width/2,Color.CYAN,height,width,Color.RED,true));
    	g.setPaint(new GradientPaint(0,0, a.color, 0,0,a.color));
//    	g.setBackground(Color.red);
    	g.fillRoundRect(localx+linesize+strokesize+a.blockx*(blocksize+linesize)
    			, localy+linesize+strokesize+a.blocky*(blocksize+linesize)
    			, blocksize-linesize
    			, blocksize-linesize
    			,(blocksize-linesize)/2
    			,(blocksize-linesize)/2);
    }
    public void drawString(String a,int i,int j,Graphics2D g) {
    	g.setFont(targetFont);
    	g.setPaint(new GradientPaint(0,0, Color.BLACK, 0,0,Color.BLACK));
    	g.drawString(a
    			,localx+linesize+strokesize+i*(blocksize+linesize)
    			, localy+linesize+strokesize+j*(blocksize+linesize)+20);
    }
    public void drawAllPoint(Color[][] map,Graphics2D g) {
    	for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j]==null) continue;
				drawPoint(new Point(i, j, map[i][j]), g);
				drawString(((double)Math.round(value[i][j]*100))/100+"",i,j, g);
//				drawString(value[i][j]+"",i,j, g);
			}
		}
    }
    public void drawAnt(Ant a,Graphics2D g) {
    	drawPoint(a.site, g);
    	
    }
    public void drawSolution(Quaque solution,Graphics2D g) {
    	Point point=null;
    	point=solution.next();
    	while (point!=null) {
    		System.out.println("solution="+point.color);
    		drawPoint(point, g);
    		point=solution.next();
    	}; 
    }
    public void drawAllAnt(Graphics2D g) {
    	for (int i = 0; i < ants.length; i++) {
    		if (ants[i]==null) continue;
			drawAnt(ants[i], g);
    	}
    }
    
    
    public void paintComponent(Graphics g1){
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
        initPaint(g);
        drawAllPoint(this.map,g);
        if (ants[0]!=null && started) {
        	drawAllPoint(ants[0].searchedmap,g);
//        	drawAllAnt(g);
        }         
        if (ants[0]!=null && finished) {
        	Point temp=ants[0].solution.pop();
        	while (temp!=null) {
        		drawPoint(temp, g);
        		temp=ants[0].solution.pop();
        	}
        }
//        drawSolution(ants[0].solution, g);
    }
    
    public void startGame() {
    	JFrame game = new JFrame();
//    	game.setLayout(new FlowLayout());
    	String information="click once:set a wall(red)\n"
    						+"click twice:set target(blue)\n"
    						+"drag:set walls(red)\n"
    						+"pressEnter:start searching\n"
    						+"pressEsc:clear all\n"
    						+"pressW:clear walls\n"
    						+"pressT:clear targets\n";
    	JTextArea jtr=new JTextArea(information);
    	jtr.setFont(Fonts[2]);
    	jtr.setBackground(this.getBackground());
    	jtr.setBounds(0, 0, 500, 500);
    	game.add(jtr,BorderLayout.EAST);
    	game.add(this,BorderLayout.CENTER);
//    	game.add(new JButton("start"));
    	Image img=Toolkit.getDefaultToolkit().getImage("title.png");//ｴｰｿﾚﾍｼｱ�
        game.setIconImage(img);
        game.setTitle("test by wfz");
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        game.setSize(502, 507);
        game.setSize(realwidth+300,realheight+300);
//        game.setResizable(false);
        game.setLocationRelativeTo(null);
        game.setBackground(Color.black);
        game.setAlwaysOnTop(true);
        game.setVisible(true);
        
    }
    public void click(int x,int y) {
    	x/=blocksize+linesize;
		y/=blocksize+linesize;
		if (insideBorder(new Ant(x, y))) {
			map[x][y]=turnColor(map[x][y],wallColor);
		}
    }
    public void dblclick(int x,int y) {
    	x/=blocksize+linesize;
		y/=blocksize+linesize;
		if (insideBorder(new Ant(x, y))) {
			map[x][y]=turnColor(map[x][y],targetColor);
			if (ants[0]!=null) {
				ants[0].targets.push(new Point(x, y,map[x][y]));
			}
		}
		
    }
    public void cleanValue() {
    	for (int i = 0; i < xlength; i++) {
			for (int j = 0; j < ylength; j++) {
				value[i][j]=0;
			}
		}
    }
    public double max(double a,double b) {
    	return a>b?a:b;
    }
    public void search(Ant a) {
    	if (!started) a.nextTarget();
    	if (a.target==null) return;
    	if (!started) {
    		System.out.println("started");
    		ants[0].initAnswer();
    		started=true;
    		finished=false;
    	}
		if (insideBorder(a)) {
			a.step();
			caculateRoad(a);
		}		
		if (a.arraived()) {
			finished=true;			
			ants[0].result=ants[0].target;
			ants[0].setSolution();
			initRoad();
			repaint();
			return;
		}
		Point prepoint=new Point(a.site);
		for (int i = 0; i < Ant.directions.length; i++) {
			a.move(i);
			if (!insideBorder(a)) {
				a.site.reducePoint(Ant.directions[i]);
				continue;
			}
			if (a.searchedmap[a.site.blockx][a.site.blocky]==Color.MAGENTA) {
				a.site.reducePoint(Ant.directions[i]);
				continue;
			}
			if (a.searchedmap[a.site.blockx][a.site.blocky]==Color.GREEN) {
				a.site.reducePoint(Ant.directions[i]);
				continue;
			}
			if (a.searchedmap[a.site.blockx][a.site.blocky]==Color.RED) {
				a.site.reducePoint(Ant.directions[i]);
				continue;
			}
			if (map[a.site.blockx][a.site.blocky]==wallColor) {
				a.site.reducePoint(Ant.directions[i]);
				continue;
			}
			a.search();
			value[a.site.blockx][a.site.blocky]=caculateRoad(a);
			OrderedPoint valuepoint=new OrderedPoint(new Point(prepoint), new Point(a.site),value[a.site.blockx][a.site.blocky]);
			OrderedPoint temp=null;
			
			temp=headPoint;
			while (temp.nextOne!=null && valuepoint.biggerThan(temp.nextOne)) {
				temp.age++;
				temp=temp.nextOne;
			}
			System.out.println("add"+valuepoint+"after"+temp);
			temp.add(valuepoint);
			temp=headPoint;
			System.out.println("=========================");
			while (temp.nextOne!=null) {
				System.out.println(temp.nextOne);
				temp=temp.nextOne;
			}
			
			System.out.println("==========================");
			a.site=new Point(prepoint);
		}
		if (headPoint.nextOne==null) {
			System.out.println("error");
			a.leave();
			Point point=a.answermap[a.site.blockx][a.site.blocky];
			if (point==null) {started=false;}
			else {a.site=new Point(point);}
			return;
		}
		repaint();
		headPoint=headPoint.nextOne;
		a.site=new Point(headPoint.point);
		System.out.println("nextto"+a.site);
		a.answermap[a.site.blockx][a.site.blocky]=new Point(headPoint.prepoint);
		System.out.println(a+"=prepoint="+a.answermap[a.site.blockx][a.site.blocky]);
    }
    
    public double caculateRoad(Ant a) {
    	double h=Point.reducePoints(a.site,a.home).doublelength();//dij
		double g=Point.reducePoints(a.site,a.target).doublelength();//bfs
		return mergy(h,g);
    }
    
    public boolean insideBorder(Ant a) {
		return a.site.biggerThan(new Point(-1, -1))
				&&a.site.smallerThan(new Point(xlength, ylength));
	}
    
    private double mergy(double dij, double bfs) {
		
		return 2*dij+bfs;
	}
	public static void main(String[] args) {
        
        StarA starA=new StarA();
        starA.startGame();
        starA.ants[0]=new Ant(starA.firstAnt);
        
        Thread thread1=new Thread() {
        	public void run() {
        		while (!starA.finished) {
        			starA.search(starA.ants[0]);
        		}
        	}
        };
        Thread thread2=new Thread() {
        	public void run() {
        		int i=0;
        		while (true) {
        			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					
						e.printStackTrace();
					}
        			
        			starA.repaint();
        		}
        	}
        };
//        thread1.start();
//        thread2.start();
    }
}
class Point{
	int blockx,blocky;
	Color color=null;//white
	public Point(Point a,Color color) {
		if (a==null) return;
		this.blockx=a.blockx;
		this.blocky=a.blocky;
		this.color=color;
	}
	public Point(Point a) {
		if (a==null) return;
		this.blockx=a.blockx;
		this.blocky=a.blocky;
		this.color=a.color;
	}
	public Point(int blockx,int blocky) {
		this.blockx=blockx;
		this.blocky=blocky;
	}
	public Point(int blockx,int blocky,Color color) {
		this.blockx=blockx;
		this.blocky=blocky;
		this.color=color;
	}
	public void addPoint(Point a) {
		if (a==null) return ;
		this.blockx+=a.blockx;
		this.blocky+=a.blocky;
	}
	public static Point addPoints(Point a,Point b) {
		Point c=new Point(a);
		if (a==null || b==null) return null;
		c.blockx=a.blockx+b.blockx;
		c.blocky=a.blocky+b.blocky;
		return c;
	}
	public void reducePoint(Point a) {
		if (a==null) return ;
		this.blockx-=a.blockx;
		this.blocky-=a.blocky;
	}
	public static Point reducePoints(Point a,Point b) {
		Point c=new Point(a);
		if (a==null || b==null) return null;
		c.blockx=a.blockx-b.blockx;
		c.blocky=a.blocky-b.blocky;
		return c;
	}
	public int length() {
		return Math.abs(this.blockx)+Math.abs(this.blocky);
		
	}
	public double doublelength() {
		return Math.sqrt(this.blockx*this.blockx+this.blocky*this.blocky);
	}
	public boolean hit(Point a) {
		
		return a!=null&&this.blockx==a.blockx&& this.blocky==a.blocky;
	}
	public boolean biggerThan(Point a) {
		return a!=null&&this.blockx>a.blockx&& this.blocky>a.blocky;
	}
	public boolean smallerThan(Point a) {
		return a!=null&&this.blockx<a.blockx&& this.blocky<a.blocky;
	}	
	public static Point[] beClosed(Point a) {
		if (a.blockx==0) {
			return new Point[] {
					new Point(a.blockx+1,a.blocky),
					new Point(a.blockx-1,a.blocky),};
		}
		if (a.blocky==0) {
			return new Point[] {
					new Point(a.blockx,a.blocky+1),
					new Point(a.blockx,a.blocky-1),};
		}
		return new Point[] {
				new Point(a.blockx,0),
				new Point(0,a.blocky),};
	}
	@Override
	public String toString() {
		return "*********"+blockx+":"+blocky+"***************";
	}
}
class OrderedPoint{
	OrderedPoint nextOne=null;
	Point point=null;
	Point prepoint=null;
	double value=0;
	boolean isHead=false;
	int age=-1;
	public OrderedPoint(Point prepoint,Point point,double value) {
		this.prepoint=new Point(prepoint);
		this.point=new Point(point);
		this.value=value;
	}
	public OrderedPoint(Point point,double value) {
		this.point=new Point(point);
		this.value=value;
	}
	public boolean biggerThan(OrderedPoint a) {
		System.out.println("check"+point+(!(a==null)&&this.value>a.value)+a.point);
		return !(a==null)&&this.value>a.value;
	}
	public boolean add(OrderedPoint a) {
		if (a==null) return false;
 		OrderedPoint temp=this.nextOne;
		this.nextOne=a;
		a.nextOne=temp;
		System.out.println("succeed to add"+this.nextOne+"after"+this);
		return true;
	}
	@Override
	public String toString() {
		return point+"";
	}
}
class Quaque{
	int maxtarget=10000;
	Point[] points=null;
	int head=0,tail=0;
	public Quaque() {
		this.points=new Point[maxtarget];
	}
	public Quaque(Point[] points) {
		this.points=points;
	}
	public Point[] refresh(Point[] a) {
		if (tail<a.length) return a;
		Point[] b=new Point[a.length*2];
		for (int i=0;i<tail;i++) {
			b[i]=new Point(a[i+head]);
		}
		return b;
	}
	public void push(Point a) {
		if (tail>=points.length) return;
		points[tail++]=new Point(a);
	}
	
	public Point pop() {
		if (head>=tail || 0>=tail) return null;
		return new Point(this.points[--tail]);
	}
	public Point next() {
		if (head>=tail || head>=points.length) return null;
		return new Point(points[head++]);
	}
	
}
class Ant{
	Color[][] searchedmap=new Color[400][400];
	Point[][] answermap=new Point[400][400];
	Quaque targets=new Quaque();
	Quaque solution=new Quaque();
	
	Point site=new Point(0, 0, Color.yellow);
	Point target=new Point(0, 0, null);
	Point result=new Point(0, 0, null);
	Point home=new Point(0, 0, Color.yellow);
	//TODO big porblem
	static final Point[] directions=new Point[] {
			new Point(0, 1, null),
			new Point(1, 0, null),
			new Point(0, -1, null),
			new Point(-1, 0, null),
//			new Point(1, 1, null),
//			new Point(-1, -1, null),
//			new Point(1, -1, null),
//			new Point(-1, 1, null),
	};
	
	public void initAnswer() {
		result=new Point(0, 0, null);
		answermap=new Point[400][400];
		solution=new Quaque();
	}
	public void initSelf() {
		site=new Point(0, 0, Color.yellow);
		searchedmap=new Color[400][400];
//		target=null;
	}
	public Ant(Ant a) {
		site=new Point(a.site);
		
	}
	public Ant(int x,int y) {
		site.blockx=x;
		site.blocky=y;
	}
	public Ant(Point a) {
		site=new Point(a);
	}
	public void setSite(Point a) {
		site=new Point(a);
	}
	public boolean arraived() {
		return site.hit(target);
	}
	public void nextTarget() {
		target=targets.next();
//		home=new Point(site);
//		TODO this site's site;
	}
	public void setSolution() {
		System.out.println("Finished Map"+this.result);
		Point temp=new Point(this.answermap[this.result.blockx][this.result.blocky],Color.yellow);
		
		while (temp!=null && !temp.hit(this.home)) {
			System.out.println(temp+""+temp.hit(this.home)+this.home);
			solution.push(temp);
			temp=new Point(this.answermap[temp.blockx][temp.blocky]);
		}
		solution.push(home);
	}
	public void move() {
		site=solution.next();
	}
	public void move(int i) {
		site.addPoint(directions[i]);
	}
	public void search() {	
		searchedmap[site.blockx][site.blocky]=Color.GREEN;
	}
	public void step() {	
		searchedmap[site.blockx][site.blocky]=Color.MAGENTA;
	}
	public void leave() {	
		searchedmap[site.blockx][site.blocky]=Color.RED;
	}
	@Override
	public String toString() {

		return this.site+"";
	}
}
