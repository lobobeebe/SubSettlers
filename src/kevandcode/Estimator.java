package kevandcode;

public class Estimator
{
	public static int GetEstNumTurnsForBuildRecursive(int[] currentResources, int[] currentBuilds, int maxPlayerGain, int buildType)
	{
		int numTurns = 0;
		
		// Turns is 0 if the player can currently afford the piece
		if (!Resources.CanAfford(currentResources, currentBuilds, buildType))
		{
			// If the build type has any build costs (i.e. Settlements consume 2 roads) get the estimate to those first
			int[] buildCost = Resources.BUILD_COSTS[buildType];
			
			for (int prereqBuildType = 0; prereqBuildType < buildCost.length; ++prereqBuildType)
			{
				int numBuildsOfThisType = buildCost[prereqBuildType]; 
				
				// If multiple builds of the same type, get estimate to all build them all
				for (int i = 0; i < numBuildsOfThisType; ++i)
				{
					numTurns += GetEstNumTurnsForBuildRecursive(currentResources, currentBuilds, maxPlayerGain, prereqBuildType);
				}
			}
			
			// Estimate number of turns to get the resources required for the build
			int[] resourceCost = Resources.RESOURCE_COSTS[buildType];
			int[] resourcesNeeded = Utils.SubtractVectors(resourceCost, currentResources);
			
			// Clamp at 0
			resourcesNeeded = Utils.Clamp(resourcesNeeded, 0, Integer.MAX_VALUE);
			
			// Get the total number of resources
			int numResourcesNeeded = Utils.Sum(resourcesNeeded);
			
			// (Inaccurate) Estimate of num turns = Num resources / [(average resources per turn) * (chance of getting a particular resource)]
			// = numResources * (average turns per resource) * (num resources) 
			numTurns += numResourcesNeeded * (2 / (maxPlayerGain + 1)) * Resources.NUM_RESOURCES;
		}
		
		// Reduce temporary resources and builds
		currentResources = Utils.SubtractVectors(currentResources, Resources.RESOURCE_COSTS[buildType]);
		currentBuilds = Utils.SubtractVectors(currentBuilds, Resources.BUILD_COSTS[buildType]);
		
		return numTurns;
	}
	
	
	public int RollUntilCanAffordResource(int[] currentResources, int buildType, int maxResourceGain, int maxRolls)
	{
		int numRolls = 0;
		
		int[] resources = currentResources.clone();
		
		while (numRolls < maxRolls && !Resources.CanAffordResourcesOnly(resources, buildType))
		{
			resources = Utils.AddVectors(resources, Resources.GetRandomlyGeneratedResources(maxResourceGain));
			++numRolls;
		}
		
		return numRolls;
	}
}
