package kevandcode.learning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TableUtils 
{
	public static void SaveTable(float[][][] table, String fileName)
	{
		try
		{
		    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		    
		    writer.write(table.length + " " + table[0].length + " " + table[0][0].length);
		    writer.newLine();
		    
		    for (int i = 0; i < table.length; ++i)
		    {			    
		    	for (int j = 0; j < table[i].length; ++j)
		    	{				    
		    		for (int k = 0; k < table[i][j].length; ++k)
		    		{
		    		    writer.write(table[i][j][k] + " ");
		    		}
		    		writer.newLine();
		    	}
		    	writer.newLine();
		    }
		     
		    writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static float[][][] LoadTable(String fileName)
	{
		float[][][] table = new float[0][0][0];
		
		try
		{
		    BufferedReader reader = new BufferedReader(new FileReader(fileName));
		    
		    String line = reader.readLine();
		    
		    int[] tableSize = tokenizeInt(line);
		    table = new float[tableSize[0]][tableSize[1]][tableSize[2]];
		    
		    for (int i = 0; i < table.length; ++i)
		    {			    
		    	for (int j = 0; j < table[i].length; ++j)
		    	{
		    		line = reader.readLine();
	    			table[i][j] = tokenizeFloat(line);
		    	}
		    	
	    		line = reader.readLine();
		    }
		    
		    reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return table;
	}
	
	public static boolean Equal(float[][][] lhs, float[][][] rhs)
	{		
		if (lhs.length != rhs.length && 
				lhs[0].length != rhs[0].length &&
				lhs[0][0].length != rhs[0][0].length)
		{
			return false;
		}
		
		for (int i = 0; i < lhs.length; ++i)
	    {			    
	    	for (int j = 0; j < lhs[i].length; ++j)
	    	{				    
	    		for (int k = 0; k < lhs[i][j].length; ++k)
	    		{
	    			if (lhs[i][j][k] != rhs[i][j][k])
	    			{
	    				return false;
	    			}
	    		}
	    	}
	    }
			
		return true;
	}
	
	public static int[] tokenizeInt(String str)
	{
	    String[] tokens = str.split(" ");
	    int[] intTokens = new int[tokens.length];
	    
	    for (int i = 0; i < tokens.length; ++i)
	    {
	    	intTokens[i] = Integer.parseInt(tokens[i]);
	    }
	    
	    return intTokens;
	}
	
	public static float[] tokenizeFloat(String str)
	{
	    String[] tokens = str.split(" ");
	    float[] floatTokens = new float[tokens.length];
	    
	    for (int i = 0; i < tokens.length; ++i)
	    {
	    	floatTokens[i] = Float.parseFloat(tokens[i]);
	    }
	    
	    return floatTokens;
	}
}
