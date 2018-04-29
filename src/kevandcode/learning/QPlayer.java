package kevandcode.learning;

import kevandcode.Offer;
import kevandcode.Player;
import kevandcode.Utils;

public class QPlayer extends Player
{
	private static final int NUM_PLAYER_STATES = 32;
	private static final int NUM_ACTIONS = 25;
	
	public float[][][] mQTable;

	public float mGreedyEpsilon = .8f;
	public float mLearningRate = .5f;
	public float mRewardDiscount = .9f;
	public int mRewardMultiplier = 100;
	
	private int mLastVictoryPoints = 0;
	private int mLastOtherVictoryPoints = 0;
	private int mLastPlayerState = 0;
	private int mLastOtherState = 0;
	private int mLastAction = 0;
	
	public QPlayer()
	{
		// Player
		// Taking into account the current player, other player, and the possible offers/actions
		mQTable = new float[NUM_PLAYER_STATES][NUM_PLAYER_STATES][NUM_ACTIONS];
	}
	
	public Offer PracticeMakeOffer(Player other)
	{
		int actionIndex = 0;
		
		int playerState = ConvertPlayerStateToIndex(this);
		int otherState = ConvertPlayerStateToIndex(other);
		
		if (Utils.sRandom.nextFloat() < mGreedyEpsilon)
		{
			// Random Action
			actionIndex = Utils.sRandom.nextInt(NUM_ACTIONS);
		}
		else
		{
			// Greedy Action
			actionIndex = GetHighestQAction(playerState, otherState);
		}
		
		// Store the state of the last action for updating purposes
		mLastVictoryPoints = mVictoryPoints;
		mLastOtherVictoryPoints = other.mVictoryPoints;
		mLastPlayerState = playerState;
		mLastOtherState = otherState;
		mLastAction = actionIndex;
		
		return ConvertActionToOffer(actionIndex);
	}
	
	public void Learn(Player other)
	{
		int playerState = ConvertPlayerStateToIndex(this);
		int otherState = ConvertPlayerStateToIndex(other);
		int highestQAction = GetHighestQAction(playerState, otherState);
		int victoryPointReward = mRewardMultiplier * (mVictoryPoints - mLastVictoryPoints)
			- 2 * mRewardMultiplier * (other.mVictoryPoints - mLastOtherVictoryPoints);
		
		mQTable[mLastPlayerState][mLastOtherState][mLastAction] = 
				mQTable[mLastPlayerState][mLastOtherState][mLastAction] * (1 - mLearningRate) + 
				mLearningRate * (victoryPointReward + mRewardDiscount * mQTable[playerState][otherState][highestQAction]);
	}
	
	public int GetHighestQAction(int playerState, int otherState)
	{
		int actionIndex = 0;
		
		// Iterate over QTable for the given states and find the highest Q value
		for (int i = 1; i < NUM_ACTIONS; ++i)
		{
			if (mQTable[playerState][otherState][i] > mQTable[playerState][otherState][actionIndex])
			{
				actionIndex = i;
			}
		}
		
		// If the highest Q value is 0, all values are 0. Choose random Action.
		if (mQTable[playerState][otherState][actionIndex] == 0)
		{
			actionIndex = Utils.sRandom.nextInt(NUM_ACTIONS);
		}
		
		return actionIndex;
	}

	public Offer MakeOffer(Player other)
	{
		int actionIndex = 0;
		
		int playerState = ConvertPlayerStateToIndex(this);
		int otherState = ConvertPlayerStateToIndex(other);
		
		// Greedy Action
		actionIndex = GetHighestQAction(playerState, otherState);
		
		return ConvertActionToOffer(actionIndex);
	}
	
	public void SetLearningParameters(float greedyEpsilon, float learningRate, float rewardDiscount, int rewardMultiplier)
	{
		mGreedyEpsilon = greedyEpsilon;
		mLearningRate = learningRate;
		mRewardDiscount = rewardDiscount;
		mRewardMultiplier = rewardMultiplier;
	}
	
	public void SaveBrain(String brainFileName)
	{
	    TableUtils.SaveTable(mQTable, brainFileName);
	}
	
	public void LoadBrain(String brainFileName)
	{
	    mQTable = TableUtils.LoadTable(brainFileName);
	}
	
	public static int ConvertPlayerStateToIndex(Player player)
	{
		// Resource State: 2^5 = 32
		// Limiting resource states to 0 (no resource) or 1 (has > 0 resources)
		int[] state = Utils.Clamp(player.mCurrentResources, 0, 1);
		
		// Interpret the combined state as a binary number and use the converted decimal number as the index
		int combinedStateIndex = Utils.ToDecimal(state, 2);
		
		return combinedStateIndex;
	}
	
	public static int ConvertOfferActionToIndex(Offer offer)
	{
		// 5 Resources
		// Resource x Resource = 5 x 5 = 25 actions
		int[] actionState = new int[] {offer.mOffererGiveResourceType, offer.mOffererGetResourceType};
		
		return Utils.ToDecimal(actionState, 5);
	}
	
	public static Offer ConvertActionToOffer(int action)
	{
		// Base 5 - 5 Resources
		int[] actionState = Utils.FromDecimal(action, 2, 5);
		
		return new Offer(actionState[0], actionState[1]);
	}
	
	public String GetName()
	{
		return "LearnedPriority";
	}
}
