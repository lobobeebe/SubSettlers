package kevandcode;

public class Offer
{
	public int mOffererGiveResourceType;
	public int mOffererGetResourceType;
	
	public Offer(int giveResourceType, int getResourceType)
	{
		mOffererGiveResourceType = giveResourceType;
		mOffererGetResourceType = getResourceType;
	}
	
	public int[] getOffererGetResources()
	{
		int[] getResources = new int[Resources.NUM_RESOURCES];
		getResources[mOffererGetResourceType] = 1;
		
		return getResources;
	}
	
	public int[] getOffererGiveResources()
	{
		int[] giveResources = new int[Resources.NUM_RESOURCES];
		giveResources[mOffererGiveResourceType] = 1;
		
		return giveResources;
	}
}
