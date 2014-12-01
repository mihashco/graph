/**
 * @author Micha³ Szczeciñski
 *Class Data is a collection of method for handle file reading / writing. 
 *It is used for parse file data and insert it in dynamic structures. 
 *It contains also method for generating random data file for test the application.
 */

import java.util.Random;
import java.util.Scanner;
import java.awt.List;
import java.io.*;

public class Data{
	
	private FileReader fReader = null;
	private FileWriter fWriter = null;
	
	private double[] data_x;
	private double[] data_y;
	
	//Obiekt wspomagaj¹cy. U¿ywany tylko do wylosowania zmiennych.
	private static Random r = new Random();
	
	/**
	 * Tworzy obiekt typu data i deklaruje tablice data_x i data_y o sta³ym rozmiarze
	 */
	public Data() {
		data_x = new double[10000];
		data_y = new double[10000];
	}
	
	/**
	 * 
	 * @param filePath - œcie¿ka do pliku z danymi. 
	 * @throws FileNotFoundException - Wyj¹tek rzucany gdy plik nie zostanie znaleziony
	 */
	public Data(String filePath) throws FileNotFoundException
	{
		try {
			Scanner sFile = new Scanner(new FileReader(filePath));
			List x_values = new List();
			List y_values = new List();
			
			while(sFile.hasNext())
			{
				x_values.add(sFile.next());
				y_values.add(sFile.next());
			}
			
			sFile.close();
			
			data_x = new double[x_values.getItemCount()];
			data_y = new double[y_values.getItemCount()];
			
			for(int i = 0; i < x_values.getItemCount(); i++)
			{
				Double tmp = new Double(x_values.getItem(i));
				data_x[i] = tmp.doubleValue();
				tmp = new Double(y_values.getItem(i));
				data_y[i] = tmp.doubleValue();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	/**
	 * @return Zwraca tablice punktów X
	 */
	public double[] GetXdata()
	{
		return data_x;
	}
	
	/**
	 * @return Zwraca tablice punktów Y
	 */
	public double[] GetYdata()
	{
		return data_y;
	}

	/**
	 * @return Wartoœæ int bêd¹ca iloœci¹ punktów danych
	 */
	public int GetDataLength()
	{
		return data_x.length;
	}

	/**
	 * Funkcja generuje losowe punkty X i Y
	 */
	public void GenerateData(){
		GenerateX();
		GenerateY();
	}

	/**
	 * Funkcja wypisuje w konsoli wartosci X i Y
	 */
	public void PrintData()
	{
		for(int i = 0; i < data_x.length; i++)
		{
			System.out.println("Data: [" + data_x[i] + "] [" + data_y[i] + "] ");
		}
	}
	
	/**
	 * Funkcja zapisuje do pliku dane X i Y
	 * @throws IOException
	 */
	public void SaveRandomToFile() throws IOException
	{
		try
		{
			fWriter = new FileWriter("random_data.txt");
			
			fWriter.flush();
			
			for(int i = 0; i < data_x.length; i++)
			{
				Double point_x = new Double(data_x[i]);
				Double point_y = new Double(data_y[i]);
				
				fWriter.write(point_x.toString() +  " " + point_y.toString() + "\n");
			}
		}
		finally
		{
			if(fWriter != null) fWriter.close();
		}
	}
	
	/**
	 * Odczyt danych z pliku "data.txt"
	 * @throws IOException
	 */
	public void ReadDataFromFile() throws IOException
	{
		Scanner sFile = null;
		
		String x_val = null;
		String y_val = null;
		
		Double tmp = null;
		int i = 0;
		
		try
		{
			sFile = new Scanner(new FileReader("data.txt"));
			
			while(sFile.hasNext())
			{
				x_val = sFile.next();
				y_val = sFile.next();
				
				tmp = new Double(x_val);
				data_x[i] = tmp.doubleValue();
				tmp = new Double(y_val);
				data_y[i++] = tmp.doubleValue();
			}
		}
		finally //Wykonywanie zawzse niezale¿nie od tego czy wyj¹tek wyst¹pi czy nie.
		{
			if(sFile != null) sFile.close(); 
		}
	}

	//Funkcje pomocnicze.
	private void GenerateX(){
		for(int i = 0; i < this.data_x.length; i++)
		{
			this.data_x[i] = (double)i;
		}
	}
	
	private void GenerateY(){
		for(int i = 0; i < this.data_y.length; i++)
		{
			this.data_y[i] = r.nextDouble();
		}
	}

}
