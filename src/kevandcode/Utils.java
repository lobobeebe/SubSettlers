package kevandcode;

import java.util.Random;

public class Utils
{
	public static final Random sRandom = new Random();
	
	public static boolean CanAfford(int[] start, int[] takeaway)
	{
		boolean canAfford = true;
		int[] difference = Utils.SubtractVectors(start, takeaway);
		
		for (int i = 0; canAfford && i < difference.length; ++i)
		{
			if (difference[i] < 0) 
			{
				canAfford = false;
			}
		}
		
		return canAfford;
	}
	
	public static int[] SubtractVectors(int[] start, int[] takeaway)
	{
		int[] returnVector = start.clone();
		
		int size = Math.min(start.length, takeaway.length);
		
		for (int i = 0; i < size; ++i)
		{
			returnVector[i] -= takeaway[i];
		}
		
		return returnVector;
	}
	
	public static int[] AddVectors(int[] start, int[] add)
	{
		int[] returnVector = start.clone();
		
		int size = Math.min(start.length, add.length);
		
		for (int i = 0; i < size; ++i)
		{
			returnVector[i] += add[i];
		}
		
		return returnVector;
	}
	
	public static int[] Clamp(int[] vector, int min, int max)
	{
		int[] returnVector = vector.clone();
		
		for (int i = 0; i < vector.length; ++i)
		{
			returnVector[i] = Math.min(Math.max(returnVector[i], min), max);
		}
		
		return returnVector;
	}
	
	public static int Sum(int[] vector)
	{
		int sum = 0;
		
		for (int i = 0; i < vector.length; ++i)
		{
			sum += vector[i];
		}
		
		return sum;
	}
	
	public static int[] Concatenate(int[] start, int[] end)
	{
		int length = start.length + end.length;
		int[] returnVector = new int[length];
		
		for (int i = 0; i < length; ++i)
		{
			if (i < start.length)
			{
				returnVector[i] = start[i];
			}
			else
			{
				returnVector[i] = end[i - start.length];
			}
		}
		
		return returnVector;
	}
	
	public static int ToDecimal(int[] valueVector, int base)
	{
		int decimal = 0;
		
		int multiplier = 1;
		
		for (int i = 0; i < valueVector.length; ++i)
		{
			decimal += multiplier * valueVector[i];
			multiplier *= base;
		}
		
		return decimal;
	}
	
	public static int[] FromDecimal(int decimal, int length, int base)
	{
		int[] binary = new int[length];
		
		for (int i = 0; decimal > 0 && i < length; ++i)
		{
			binary[i] = decimal % base;
			decimal /= base;
		}
		
		return binary;
	}
	
	public static String ToString(int[] vector)
	{
		String str = "{";
		
		for (int i = 0; i < vector.length; ++i)
		{
			str += vector[i];
			
			if (i != vector.length - 1)
			{
				str += " ";
			}
		}
		
		str += "}";
		
		return str;
	}
}
