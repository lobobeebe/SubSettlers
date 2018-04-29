package kevandcode;

public class Resources
{
	public static final int BRICK = 0;
	public static final int WOOD = 1;
	public static final int WOOL = 2;
	public static final int WHEAT = 3;
	public static final int ORE = 4;
	public static final int NUM_RESOURCES = 5;

	public static final int CITY = 0;
	public static final int SETTLEMENT = 1;
	public static final int ROAD = 2;
	public static final int CARD = 3;
	public static final int NUM_BUILDS = 4;
	
	public static final int[][] RESOURCE_COSTS = new int[][] 
	{
		{0, 0, 0, 2, 3}, // CITY
		{1, 1, 1, 1, 0}, // SETTLEMENT
		{1, 1, 0, 0, 0}, // ROAD
		{0, 0, 1, 1, 1}, // CARD
	};
	
	public static final int[][] BUILD_COSTS = new int[][] 
	{
		{0, 1, 0, 0}, // CITY
		{0, 0, 2, 0}, // SETTLEMENT
		{0, 0, 0, 0}, // ROAD
		{0, 0, 0, 0}, // CARD
	};
	
	public static boolean CanAfford(int[] resources, int[] builds, int buildType)
	{
		return CanAffordResourcesOnly(resources, buildType) && CanAffordBuildsOnly(builds, buildType);
	}
	
	public static boolean CanAffordResourcesOnly(int[] resources, int buildType)
	{
		return Utils.CanAfford(resources,  RESOURCE_COSTS[buildType]);
	}
	
	public static boolean CanAffordBuildsOnly(int[] builds, int buildType)
	{
		return Utils.CanAfford(builds,  BUILD_COSTS[buildType]);
	}
	
	public static int[] SubtractResourcesForBuild(int[] currentResources, int buildType)
	{
		return Utils.SubtractVectors(currentResources, RESOURCE_COSTS[buildType]);
	}
	
	public static int[] SubtractBuildsForBuild(int[] currentBuilds, int buildType)
	{
		return Utils.SubtractVectors(currentBuilds, BUILD_COSTS[buildType]);
	}
	
	public static int[] GetRandomlyGeneratedResources(int maxResourceGain)
	{		
		int[] totalResourcesGained = new int[Resources.NUM_RESOURCES];

		// Get a random number of resources uniformly distributed from 0 (inclusive) to the given max (exclusive)
		// Adding 1 to the max resource gain to add the possibility of actually getting the max
		int resourceGain = Utils.sRandom.nextInt(maxResourceGain + 1);
		
		for (int i = 0; i < resourceGain; ++i)
		{
			// Add a random resource
			int resourceType = Utils.sRandom.nextInt(Resources.NUM_RESOURCES);
			++totalResourcesGained[resourceType];
		}
		
		return totalResourcesGained;
	}
}
