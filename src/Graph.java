import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;

enum GraphType {
	DOT_GRAPH,
	LINE_GRAPH,
	BAR_GRAPH
}

enum GraphColor {
	RED_GRAPH,
	GREEN_GRAPH,
	BLUE_GRAPH
}

public class Graph extends Panel implements ComponentListener{
	
	private int width;
	private int height;
	private int x;
	private int y;
	private int rows;
	private int cols;
	
	private double gDataX[] = null;
	private double gDataY[] = null; 
	private double[] data_x;
	private double[] data_y;
	
	private Data gData = null;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	
	public GraphType gType;
	public GraphColor gColor;

	private int isInitialized = 0;
	private final int OFFSET = 50;
	private final int S_OFFSET = 50; //drawing start offset
	
	/**
	 * Konstruktor domyœlny klasy Graph.
	 * Inicjuje odczyt danych
	 */
	public Graph()
	{
		width = this.getWidth(); //Zmienna offset u¿ywana jest po to aby zostawic miejsce na osie X, Y oraz napisy
		height = this.getHeight() - OFFSET;
			
		x = this.getX();
		y = this.getY();
		
		this.rows = 10;
		this.cols = 10;
		
		gData = new Data();
		
		try
		{
			gData.ReadDataFromFile();
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
		}
		
		InitGData();
		addComponentListener(this);
		
		this.gType = GraphType.DOT_GRAPH;
		this.gColor = GraphColor.RED_GRAPH;
	}
	
	/**
	 * Zdarzenie wywo³ywane gdy rozmiar komponentu zostanie zmieniony
	 */
	public void componentResized(ComponentEvent e)
	{
		width = this.getWidth();
		height = this.getHeight() - OFFSET;
		
		x = this.getX();
		y = this.getY();
		
		isInitialized = 0;
		InitGData();
	}
	
	/**
	 * Rysowanie wykresu danych z pliku
	 * @param filePath œcie¿ka do pliku
	 */
	public void printGraphFromFile(String filePath)
	{
		try 
		{
			gData = new Data(filePath);
		}
		catch(Exception e)
		{
			e.toString();
			filePath = "data.txt";
		}
		
		InitGData();
		addComponentListener(this);
		
		this.repaint();
	}

	//metoda wywowy³ana podczas rysowania komponentu lub przy jego odœwie¿eniu
	public void paint(Graphics g)
	{
		if(isInitialized == 0) return;
		
		Graphics2D g2 = (Graphics2D) g;
		paintGrid(g2);
		paintXaxis(g2);
		paintYaxis(g2);
		paintXaxisLabel(g);
		paintYaxisLabel(g);
		
		setPaintColor(g2, gColor);
	
		AffineTransform at = new AffineTransform();
		at.translate(0, height);
		at.scale(1.0, -1.0);
		g2.setTransform(at);
		
		switch (gType) {
		case BAR_GRAPH:
			paintBar(g2);
			break;
		case DOT_GRAPH:
			paintDot(g2);
			break;
		case LINE_GRAPH:
			paintLine(g2);
			break;
		default:
			break;
		}
		
		at.translate(0, 0);
		at.scale(1.0, 1.0);
		g2.setTransform(at);
	}
	
	public double getXmin()
	{
		return this.xMin;
	}
	
	public double getXmax()
	{
		return this.xMax;
	}
	
	public double getYmin()
	{
		return this.yMin;
	}
	
	public double getYmax()
	{
		return this.yMax;
	}
	
	public void setXmin(double x)
	{
		xMin = x;
		scaleChanged();
	}
	
	public void setXmax(double x)
	{
		xMax = x;
		scaleChanged();
	}
	
	public void setYmin(double y)
	{
		yMin = y;
		scaleChanged();
	}
	
	public void setYmax(double y)
	{
		yMax = y;
		scaleChanged();
	}

	
	
	
	private void scaleChanged()
	{
		double yRatio;
		double xRatio;
		
		yRatio = yMax/(height);
		xRatio = xMax/(width);
		
		for(int i = 0; i < data_x.length; i++)
		{
			gDataX[i] = (data_x[i] - xMin)/xRatio + OFFSET;
			gDataY[i] = (data_y[i] - yMin)/yRatio;
		}
		
		isInitialized = 1;
		this.repaint();
	}
	
	private double findXmin()
	{
		double tmp = data_x[0];
		int i;
		for(i = 1; i < data_x.length; i++)
		{
			if(data_x[i] < tmp) tmp = data_x[i];
		}
		return xMin = tmp;
	}
	
	private double findXmax()
	{
		double tmp = data_x[0];
		int i;
		
		for(i = 1; i < data_x.length; i++)
		{
			if(data_x[i] > tmp) 
			{
				tmp = data_x[i];
			}
		}
		return xMax = tmp;
	}
	
	private double findYmin()
	{
		double tmp = data_x[0];
		int i;
		for(i = 1; i < data_x.length; i++)
		{
			if(data_y[i] < tmp) tmp = data_y[i];
		}
		return yMin = tmp;
	}
	
	private double findYmax()
	{
		double tmp = data_x[0];
		int i;
		for(i = 1; i < data_x.length; i++)
		{
			if(data_y[i] > tmp) tmp = data_y[i];
		} 
		return yMax = tmp;
	}
	
	private void InitGData()
	{		
		data_x = gData.GetXdata();
		data_y = gData.GetYdata();
		
		double yRatio;
		double xRatio;
		
		gDataX = new double[data_x.length];
		gDataY = new double[data_y.length];
		
		//Szukanie wartoœci minimalnych i maksymalnych.
		findXmin();
		findXmax();
		findYmin();
		findYmax();
		
		//dopasowanie skali
		yRatio = yMax/(height);
		xRatio = xMax/(width);
		
		//Aby wykres zaczynal sie od 0.0 i miescil na ekranie trzeba dokonac transformacji wpolrzednych
		//Aby wykres zaczynal sie od punktu 0.0 trzeba odjac od wszystkich wartosci y wartosc ymin i analogicznie dla x. 
		//Aby wykres konczyk sie w punkcie height
		
		for(int i = 0; i < data_x.length; i++)
		{
			gDataX[i] = (data_x[i] - xMin)/xRatio + OFFSET;
			gDataY[i] = (data_y[i] - yMin)/yRatio;
		}
		
		isInitialized = 1;
	}

	private void paintLine(Graphics2D g2)
	{		
		int l = gData.GetDataLength();
		
		x = this.getX();
		y = this.getY();
		
		for(int i = 0; i < l - 1; i++)
		{	
			if(gDataX[i] >= 50 && gDataY[i] >= 0.0) //jesli punkt miesci sie na obszarze wykresu
				g2.draw(new  Line2D.Double(gDataX[i], gDataY[i], gDataX[i+1], gDataY[i+1]));
		}
	}
	
	private void paintBar(Graphics2D g2)
	{
//
	}
	
	private void paintDot(Graphics2D g2)
	{
		int l = gData.GetDataLength();
		
		for(int i = 0; i < l; i++)
		{
			if(gDataX[i] >= 50 && gDataY[i] >= 0.0) //jesli punkt miesci sie na obszarze wykresu
				g2.draw(new Ellipse2D.Double(gDataX[i], gDataY[i], 5, 5));
		}
	}
	
	private void paintGrid(Graphics2D g2)
	{
		int i;
		
		int rowHt = height / (rows);
		for(i = 0; i < rows; i++)
			g2.drawLine(S_OFFSET, i * rowHt, width, i * rowHt);
		
		int rowWid = (width) / (cols);
		for(i = 0 ; i < cols; i++) 
			g2.drawLine(i * rowWid + S_OFFSET, 0, i*rowWid + S_OFFSET, height);
	}
	
	private void paintXaxis(Graphics2D g2)
	{
		g2.fillRect(S_OFFSET, 0, 2, height);
	}
	
	private void paintYaxis(Graphics2D g2)
	{
		g2.fillRect(S_OFFSET, height, width, 2);
	}
	
	private void paintXaxisLabel(Graphics g)
	{
		int colWid = width / cols;
				
		Double diff = new Double(Math.abs(xMax - xMin));
		Double step = new Double(diff/cols);
		Double xmin = xMin;

		for(int i = 0; i <= cols; i++)
		{
			String l = String.format("%-2.2f", xmin);
			xmin+= step;
			
			g.drawString(l, i*colWid+OFFSET, height+20);
		}
	}
 	
	private void paintYaxisLabel(Graphics g)
	{
		int rowHt = height / rows;
		
		Double diff = new Double(Math.abs(yMax - yMin));
		Double step = new Double(diff/rows);
		Double ymin = yMin;
		
		for(int i = cols; i >= 0; i--)
		{
			String l = String.format("%-2.2f", ymin);
			ymin += step;
			
			g.drawString(l, 10, i*rowHt);
		}
	}
	
	private void setPaintColor(Graphics2D g2, GraphColor color)
	{
		if(g2 == null || color == null) return;
		
		switch (color) {
		case RED_GRAPH:
			g2.setColor(new Color(255, 0, 0));
			break;
		case BLUE_GRAPH:
			g2.setColor(new Color(0, 0, 255));
			break;
		case GREEN_GRAPH:
			g2.setColor(new Color(0, 255, 0));
			break;
		default:
			break;
		}
	}
	
	
	public void componentHidden(ComponentEvent arg0) {
	}
	public void componentMoved(ComponentEvent arg0) {
		x = this.getX();
		y = this.getY();
	}
	public void componentShown(ComponentEvent arg0) {
	}
}