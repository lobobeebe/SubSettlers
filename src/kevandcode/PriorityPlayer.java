package kevandcode;

public class PriorityPlayer extends Player 
{
	public Offer MakeOffer(Player other)
	{
		// Get the current build plan
		int buildTypePlan = GetBuildPlan();
		
		// Get a random needed resource needed
		int resourceNeeded;
		do
		{
			resourceNeeded = Utils.sRandom.nextInt(Resources.NUM_RESOURCES);
		}
		while(Resources.RESOURCE_COSTS[buildTypePlan][resourceNeeded] == 0);
		
		// Get a random resource not needed
		int resourceUneeded;
		do
		{
			resourceUneeded = Utils.sRandom.nextInt(Resources.NUM_RESOURCES);
		}
		while(Resources.RESOURCE_COSTS[buildTypePlan][resourceUneeded] > 0);
		
		return new Offer(resourceUneeded, resourceNeeded);
	}
	
	public int GetBuildPlan()
	{
		for (int buildType = 0; buildType < Resources.NUM_BUILDS - 1; ++buildType)
		{
			if (Resources.CanAffordBuildsOnly(mCurrentBuilds, buildType))
			{
				return buildType;
			}	
		}
		
		// Should never happen since roads do not pre-require builds
		return Resources.NUM_BUILDS - 1; 
	}
	
	public String GetName()
	{
		return "RandomPriority";
	}
}
