import java.awt.Button;
import java.awt.Choice;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window extends Frame implements WindowListener, ActionListener{
	private int width;
	private int height;
	
	private Graph graph;
	private TextField tfXaxisMin;
	private TextField tfXaxisMax;
	private TextField tfYaxisMin;
	private TextField tfYaxisMax;
	
	private Button btOpenFile;
	private Button btSetXmax;
	private Button btSetXmin;
	private Button btSetYmax;
	private Button btSetYmin;
	
	private double HeightRatio = 1.0/9.0; //zmienna u¿ywana do okreœlenia zawartoœci GridBagLayout
	private double WeightRatio = 1.0/16.0;
	
	/**
	 * Konstruktor okna g³ównego aplikacji
	 */
	public Window() {
		width = 1220;
		height = 720;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		createLayout(c);
	
		pack();
		setSize(width, height);
		setVisible(true);
		addWindowListener(this);
	}
	
	/**
	 * Obs³uga buttona "Open File"
	 */
	public void actionPerformed(ActionEvent e)
	{	
		Object source = e.getSource();
		
		if(source == btOpenFile)
		{
			FileDialog fd = new FileDialog(this);
			fd.setTitle("Open Data File");
			fd.setVisible(true);
			
			String fileName = fd.getFile();
			graph.printGraphFromFile(fileName);
			
			reloadTextFields();
		}
		else if(source == btSetXmax)
		{
			try {
				System.out.println("btSetXmax");
				Double d = new Double(tfXaxisMax.getText());
				
				if(d < graph.getXmin())
				{
					Exception ex = new Exception("Invalid argument");
					throw ex;
				}
				
				graph.setXmax(d);
			} catch (Exception e1) {
				Double tmp = new Double(graph.getXmax());		
				tfXaxisMax.setText(tmp.toString());
			}
		}
		else if(source == btSetXmin)
		{
			try {
				System.out.println("btSetXmin");
				Double d = new Double(tfXaxisMin.getText());
				
				if(d > graph.getXmax())
				{
					Exception ex = new Exception("Invalid argument");
					throw ex;
				}
				
				graph.setXmin(d);
			} catch (Exception e1) {
				
				Double tmp = new Double(graph.getXmin());		
				tfXaxisMin.setText(tmp.toString());
			}
		}
		else if(source == btSetYmin)
		{
			try {
				System.out.println("btSetYmin");
				Double d = new Double(tfYaxisMin.getText());
				
				System.out.println(d + "  " + graph.getYmax());
				if(d > graph.getYmax())
				{
					Exception ex = new Exception("Invalid argument");
					throw ex;
				}
				graph.setYmin(d);
			} catch (Exception e1) {
				Double tmp = new Double(graph.getYmin());		
				tfYaxisMin.setText(tmp.toString());
			}
		}
		else if(source == btSetYmax)
		{
			try {
				System.out.println("btsetYmax");
				Double d = new Double(tfYaxisMax.getText());
				
				if(d < graph.getYmin())
				{
					Exception ex = new Exception("Invalid argument");
					throw ex;
				}
				graph.setYmax(d);
			} catch (Exception e1) {
				Double tmp = new Double(graph.getYmax());		
				tfYaxisMax.setText(tmp.toString());
			}
		}
	}
	
	private void createLayout(GridBagConstraints c)
	{
		insertGraph(c);
		insertOpenButton(c);
		insertColorLabel(c);
		insertColorChoice(c);
		insertTypeLabel(c);
		insertTypeChoice(c);
		
		//Zakres osi
		insertXaxisMinLabel(c);
		insertXaxisMin(c);
		insertXaxisMaxLabel(c);
		insertXaxisMax(c);
		
		insertYaxisMaxLabel(c);
		insertYaxisMax(c);
		insertYaxisMinLabel(c);
		insertYaxisMin(c);
		
		insertXmaxSetButton(c);
		insertXminSetButton(c);
		insertYmaxSetButton(c);
		insertYminSetButton(c);
	}
	
	private void insertGraph(GridBagConstraints c)
	{
		graph = new Graph();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 14;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(20, 20, 20, 0);
		add(graph, c);
	}
	
	private void insertOpenButton(GridBagConstraints c)
	{
		btOpenFile = new Button("Open File");
		c.gridx = 15;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.01;
		c.weighty = 0.01;
		add(btOpenFile, c);
		
		btOpenFile.addActionListener(this);
	}
	 
	private void insertColorLabel(GridBagConstraints c)
	{
		Label lbSetGraphColor = new Label("Set Graph Color");
		c.gridx = 15;
		c.gridy = 1;
		add(lbSetGraphColor, c);
	}
	
	private void insertTypeLabel(GridBagConstraints c)
	{
		Label lbSetGraphType = new Label("Set Graph Type");
		c.gridx = 15;
		c.gridy = 2;
		add(lbSetGraphType, c);
	}
	
	private void insertColorChoice(GridBagConstraints c)
	{
		Choice chColor = new Choice();
		chColor.add("Red");
		chColor.add("Green");
		chColor.add("Blue");
		c.gridx = 16;
		c.gridy = 1;
		add(chColor, c);
		
		chColor.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				Choice ce =(Choice) e.getSource();
 
				if(ce.getSelectedItem().equals("Red"))
				{
					graph.gColor = GraphColor.RED_GRAPH;
				}
				else if(ce.getSelectedItem().equals("Blue"))
				{
					graph.gColor = GraphColor.BLUE_GRAPH;
				}
				else if(ce.getSelectedItem().equals("Green"))
				{
					graph.gColor = GraphColor.GREEN_GRAPH;
				}
				
				graph.repaint();
			}
		});
	}
	
	private void insertTypeChoice(GridBagConstraints c)
	{
		Choice chType = new Choice();
		chType.add("Point");
		chType.add("Line");
		chType.add("Bar");
		c.gridx = 16;
		c.gridy = 2;
		add(chType, c);
		
		chType.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				Choice ce = (Choice) e.getSource();
				
				if(ce.getSelectedItem().equals("Point"))
				{
					graph.gType = GraphType.DOT_GRAPH;
				}
				else if(ce.getSelectedItem().equals("Line"))
				{
					graph.gType = GraphType.LINE_GRAPH;
				}
				else if(ce.getSelectedItem().equals("Bar"))
				{
					graph.gType = GraphType.BAR_GRAPH;
				}
				
				graph.repaint();
			}
		});
		
	}

	private void insertXaxisMinLabel(GridBagConstraints c)
	{
		Label lbXaxisMin = new Label("Set X min value");
		c.gridx = 15;
		c.gridy = 3;
		add(lbXaxisMin, c);
	}
	
	private void insertXaxisMin(GridBagConstraints c)
	{
		tfXaxisMin = new TextField("");
		c.gridx = 15;
		c.gridy = 4;
		
		Double xmin = new Double(graph.getXmin());
		tfXaxisMin.setText(xmin.toString());
		
		add(tfXaxisMin, c);
	}
	
	private void insertXaxisMaxLabel(GridBagConstraints c)
	{		
		Label lbXaxisMax = new Label("X max");
		c.gridx = 15;
		c.gridy = 5;
		add(lbXaxisMax, c);
	}
	
	private void insertXaxisMax(GridBagConstraints c)
	{
		tfXaxisMax = new TextField("X max");
		c.gridx = 15;
		c.gridy = 6;
		
		Double xmax = new Double(graph.getXmax());
		tfXaxisMax.setText(xmax.toString());
		
		add(tfXaxisMax, c);
	}
	
	private void insertYaxisMinLabel(GridBagConstraints c)
	{
		Label lbYaxisMin = new Label("Y min");
		c.gridx = 15;
		c.gridy = 7;
		
		add(lbYaxisMin, c);
	}
	
	private void insertYaxisMin(GridBagConstraints c)
	{
		tfYaxisMin = new TextField("Y min");
		c.gridx = 15;
		c.gridy = 8;
		
		Double ymin = new Double(graph.getYmin());
		tfYaxisMin.setText(ymin.toString());
		
		add(tfYaxisMin, c);
	}
	
	private void insertYaxisMaxLabel(GridBagConstraints c)
	{
		Label lbYaxisMax = new Label("Y max");
		c.gridx = 15;
		c.gridy = 9;
		add(lbYaxisMax, c);
	}
	
	private void insertYaxisMax(GridBagConstraints c)
	{
		tfYaxisMax = new TextField("X max");
		c.gridx = 15;
		c.gridy = 10;
		
		Double ymax = new Double(graph.getYmax());
		tfYaxisMax.setText(ymax.toString());
		
		add(tfYaxisMax, c);
	}
	
	private void reloadTextFields()
	{
		Double tmp = new Double(graph.getXmax());		
		tfXaxisMax.setText(tmp.toString());
		
		tmp = new Double(graph.getXmin());
		tfXaxisMin.setText(tmp.toString());
		
		tmp = new Double(graph.getYmax());
		tfYaxisMax.setText(tmp.toString());

		tmp = new Double(graph.getYmin());
		tfYaxisMin.setText(tmp.toString());
	}
	
	private void insertXmaxSetButton(GridBagConstraints c)
	{		
		btSetXmax = new Button("Set");
		c.gridx = 16;
		c.gridy = 6;
		add(btSetXmax, c);
		btSetXmax.addActionListener(this);
	}
	
	private void insertXminSetButton(GridBagConstraints c)
	{
		btSetXmin = new Button("Set");
		c.gridx = 16;
		c.gridy = 4;
		add(btSetXmin, c);
		btSetXmin.addActionListener(this);
	}
	
	private void insertYmaxSetButton(GridBagConstraints c)
	{
		btSetYmax = new Button("Set");
		c.gridx = 16;
		c.gridy = 10;
		add(btSetYmax, c);
		btSetYmax.addActionListener(this);
	}
	
	private void insertYminSetButton(GridBagConstraints c)
	{
		btSetYmin = new Button("Set");
		c.gridx = 16;
		c.gridy = 8;
		add(btSetYmin, c);
		btSetYmin.addActionListener(this);
	}
	
	
	
	//Window Listener virtual functions implemenetation.
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}




