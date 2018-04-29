package kevandcode;

public class Player
{
	public int mNumWins;
	public int mNumGames;
	public int mVictoryPoints;
	
	public int[] mCurrentResources;
	public int[] mCurrentBuilds;
	
	public int[] mTotalResources;
	public int[] mTotalBuilds;
	
	public int[] mNumResourcesFromTrades;
	public int[] mNumBuildsFromTrades;
	
	public Player()
	{
		mNumResourcesFromTrades = new int[Resources.NUM_RESOURCES];
		mNumBuildsFromTrades = new int[Resources.NUM_BUILDS];
		
		mTotalResources = new int[Resources.NUM_RESOURCES];
		mTotalBuilds = new int[Resources.NUM_BUILDS];
		
		mNumWins = 0;
		
		ResetItems();
	}
	
	public void AddResources(int[] resources)
	{
		int[] add = Utils.Clamp(resources, 0, Integer.MAX_VALUE);
		
		mCurrentResources = Utils.AddVectors(mCurrentResources, add);
		mTotalResources = Utils.AddVectors(mTotalResources, add);
	}
	
	public void SubtractResources(int[] resources)
	{
		int[] sub = Utils.Clamp(resources, 0, Integer.MAX_VALUE);
		
		mCurrentResources = Utils.SubtractVectors(mCurrentResources, sub);
	}
	
	public int[] SimulateBuild(int[] currentResources, int[] currentBuilds)
	{
		int[] resources = currentResources.clone();
		int[] builds = currentBuilds.clone();
		
		int[] newBuilds = new int[Resources.NUM_BUILDS];
		
		// Determine what we can build in prioritized order
		for (int build = 0; build < Resources.NUM_BUILDS - 1; ++build)
		{
			if (build != Resources.ROAD || builds[Resources.ROAD] < 5)
			{
				if (Resources.CanAfford(resources, builds, build))
				{
					resources = Resources.SubtractResourcesForBuild(resources, build);
					builds = Resources.SubtractBuildsForBuild(builds, build);
					
					++newBuilds[build];
					
					// Subtract build to see if we can build multiple of the same 
					--build;
				}
			}
		}
		
		return newBuilds;
	}
	
	public int[] Build()
	{		
		int[] newBuilds = SimulateBuild(mCurrentResources, mCurrentBuilds);
		
		for (int buildType = newBuilds.length - 1; buildType >= 0; --buildType)
		{
			for (int numBuilds = 0; numBuilds < newBuilds[buildType]; ++numBuilds)
			{
				mCurrentResources = Resources.SubtractResourcesForBuild(mCurrentResources, buildType);
				mCurrentBuilds = Resources.SubtractBuildsForBuild(mCurrentBuilds, buildType);
			}
		}
		
		// Add builds to stats
		mCurrentBuilds = Utils.AddVectors(mCurrentBuilds, newBuilds);
		mTotalBuilds = Utils.AddVectors(mTotalBuilds, newBuilds);
		
		// Cities and settlements are one point
		mVictoryPoints += newBuilds[Resources.CITY];
		mVictoryPoints += newBuilds[Resources.SETTLEMENT];
		
		return newBuilds;
	}
	
	public void ResetItems()
	{
		mVictoryPoints = 0;
		
		mCurrentResources = new int[Resources.NUM_RESOURCES];
		mCurrentBuilds = new int[Resources.NUM_BUILDS];
	}
	
	public static boolean ResolveOfferStrict(Player offerer, Player other, Offer offer)
	{
		boolean success = false;
		
		if (offer != null && Utils.CanAfford(offerer.mCurrentResources, offer.getOffererGiveResources()) &&
				Utils.CanAfford(other.mCurrentResources, offer.getOffererGetResources()))
		{
			ResolveOffer(offerer, other, offer);
			
			success = true;
		}
		
		return success;
	}
	
	public static void ResolveOffer(Player offerer, Player other, Offer offer)
	{
		if (offer != null)
		{
			offerer.AddResources(offer.getOffererGetResources());
			offerer.SubtractResources(offer.getOffererGiveResources());
			offerer.mNumResourcesFromTrades = Utils.AddVectors(offerer.mNumResourcesFromTrades,
					offer.getOffererGetResources());
			offerer.mNumResourcesFromTrades = Utils.SubtractVectors(offerer.mNumResourcesFromTrades,
					offer.getOffererGiveResources());
			
			other.AddResources(offer.getOffererGiveResources());
			other.SubtractResources(offer.getOffererGetResources());
		}
	}
	
	public int GetMaxResourceGain()
	{
		return 1 + mCurrentBuilds[Resources.CITY] * 2 + mCurrentBuilds[Resources.SETTLEMENT];
	}
	
	public Offer MakeOffer(Player player)
	{
		return null;
	}
	
	public static String GetStringFormat()
	{
		return "Player,Win %,Avg Brick RBT,Avg Wood RBT,Avg Wool RBT,Avg Wheat RBT,Avg Ore RBT," +
				"Avg City Total,Avg City BBT,Avg Settlement Total,Avg Settlement BBT,Avg Road Total," +
				"Avg Road BBT,Avg Card Total,Avg Card BBT";
	}
	
	public String ToString()
	{
		String str = GetName() + "," + ((float)mNumWins / mNumGames);
		
		for (int i = 0; i < Resources.NUM_RESOURCES; ++i)
		{
			str += "," + ((float)mNumResourcesFromTrades[i] / mNumGames);
		}
		
		for (int i = 0; i < Resources.NUM_BUILDS; ++i)
		{
			str += "," + ((float)mTotalBuilds[i] / mNumGames);
			str += "," + ((float)mNumBuildsFromTrades[i] / mNumGames);
		}
		
		return str;
	}
	
	public String GetName()
	{
		return "No-Trade";
	}
}
