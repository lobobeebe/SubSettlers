package kevandcode.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

import kevandcode.Offer;
import kevandcode.Player;
import kevandcode.PriorityPlayer;
import kevandcode.Resources;
import kevandcode.Utils;
import kevandcode.learning.QPlayer;

public class GameManager
{
	public static final int NUM_ROUNDS = 1000000;
	public static final int NUM_ITERATIONS = 200;
	
	public static final int NUM_PLAYERS = 4;
	
	public static final int CONFIG_1NOTRADE_3PRIORITY = 0;
	public static final int CONFIG_1NOTRADE_3LEARNED = 1;
	public static final int CONFIG_1PRIORITY_3NOTRADE = 2;
	public static final int CONFIG_1PRIORITY_3LEARNED = 3;
	public static final int CONFIG_1LEARNED_3NOTRADE = 4;
	public static final int CONFIG_1LEARNED_3PRIORITY = 5;
	public static final int CONFIG_4NOTRADE = 6;
	public static final int CONFIG_4PRIORITY = 7;
	public static final int CONFIG_4LEARNED = 8;
	public static final int NUM_CONFIGURATIONS = 9;
	
	public Player[] mPlayers;
	private int mConfig;
	
	public GameManager(int config)
	{
		mPlayers = new Player[NUM_PLAYERS];
		mConfig = config;

		switch (config)
		{
			case CONFIG_1NOTRADE_3PRIORITY:
				mPlayers[0] = new Player();
				mPlayers[1] = new PriorityPlayer();
				mPlayers[2] = new PriorityPlayer();
				mPlayers[3] = new PriorityPlayer();
				break;
				
			case CONFIG_1PRIORITY_3NOTRADE:
				mPlayers[0] = new PriorityPlayer();
				mPlayers[1] = new Player();
				mPlayers[2] = new Player();
				mPlayers[3] = new Player();
				break;

			case CONFIG_1NOTRADE_3LEARNED:
				mPlayers[0] = new Player();
				mPlayers[1] = new QPlayer();
				((QPlayer)mPlayers[1]).LoadBrain("brain.txt");
				mPlayers[2] = new QPlayer();
				((QPlayer)mPlayers[2]).LoadBrain("brain.txt");
				mPlayers[3] = new QPlayer();
				((QPlayer)mPlayers[3]).LoadBrain("brain.txt");
				break;

			case CONFIG_1LEARNED_3NOTRADE:
				mPlayers[0] = new QPlayer();
				((QPlayer)mPlayers[0]).LoadBrain("brain.txt");
				mPlayers[1] = new Player();
				mPlayers[2] = new Player();
				mPlayers[3] = new Player();
				break;

			case CONFIG_1LEARNED_3PRIORITY:
				mPlayers[0] = new QPlayer();
				((QPlayer)mPlayers[0]).LoadBrain("brain.txt");
				mPlayers[1] = new PriorityPlayer();
				mPlayers[2] = new PriorityPlayer();
				mPlayers[3] = new PriorityPlayer();
				break;

			case CONFIG_1PRIORITY_3LEARNED:
				mPlayers[0] = new PriorityPlayer();
				mPlayers[1] = new QPlayer();
				((QPlayer)mPlayers[1]).LoadBrain("brain.txt");
				mPlayers[2] = new QPlayer();
				((QPlayer)mPlayers[2]).LoadBrain("brain.txt");
				mPlayers[3] = new QPlayer();
				((QPlayer)mPlayers[3]).LoadBrain("brain.txt");
				break;
				
			case CONFIG_4NOTRADE:
				mPlayers[0] = new Player();
				mPlayers[1] = new Player();
				mPlayers[2] = new Player();
				mPlayers[3] = new Player();
				break;
				
			case CONFIG_4PRIORITY:
				mPlayers[0] = new PriorityPlayer();
				mPlayers[1] = new PriorityPlayer();
				mPlayers[2] = new PriorityPlayer();
				mPlayers[3] = new PriorityPlayer();
				break;
				
			case CONFIG_4LEARNED:
				mPlayers[0] = new QPlayer();
				((QPlayer)mPlayers[0]).LoadBrain("brain.txt");
				mPlayers[1] = new QPlayer();
				((QPlayer)mPlayers[1]).LoadBrain("brain.txt");
				mPlayers[2] = new QPlayer();
				((QPlayer)mPlayers[2]).LoadBrain("brain.txt");
				mPlayers[3] = new QPlayer();
				((QPlayer)mPlayers[3]).LoadBrain("brain.txt");
				break;
		}
	}
	
	public int SimulateIteration()
	{
		int winningPlayer = -1;

		// Provide all players a random number of resources
		RandomlyGenerateResourcesForAllPlayers();
		
		int numPlayersGone = 0;
		boolean[] hasPlayerGone = new boolean[NUM_PLAYERS];
		
		for (int i = 0; winningPlayer < 0 && i < NUM_PLAYERS; ++i)
		{
			// Get a random player order
			int turn = Utils.sRandom.nextInt(NUM_PLAYERS - numPlayersGone);
			int playerNum = 0;
			
			while(hasPlayerGone[playerNum] || turn > 0)
			{
				if (!hasPlayerGone[playerNum])
				{
					--turn;
				}
					
				++playerNum;
			}
			hasPlayerGone[playerNum] = true;
			++numPlayersGone;
			
			Player player = mPlayers[playerNum];
			
			// Simulate building right now for benchmark
			int[] preTradePossibleBuilds = player.SimulateBuild(player.mCurrentResources, player.mCurrentBuilds);
			
			// Offer a trade to each other player
			Trade(playerNum);
			
			// Allow current player to build
			int[] newBuilds = player.Build();
			player.mNumBuildsFromTrades = Utils.AddVectors(player.mNumBuildsFromTrades,
					Utils.SubtractVectors(newBuilds, preTradePossibleBuilds));
			
			// End game at 10 Victory Points
			if (mPlayers[playerNum].mVictoryPoints >= 10)
			{
				winningPlayer = playerNum;
			}
		}
		
		return winningPlayer;
	}
	
	public void Trade(int playerNum)
	{
		Player player = mPlayers[playerNum];
		
		// Offer a trade to each other player
		int numTrades = 0;
		boolean[] hasTraded = new boolean[NUM_PLAYERS];
		
		while (numTrades < NUM_PLAYERS)
		{
			// Get a random player order
			int turn = Utils.sRandom.nextInt(NUM_PLAYERS - numTrades);
			int traderNum = 0;
			
			while(hasTraded[traderNum] || turn > 0)
			{
				if (!hasTraded[traderNum])
				{
					--turn;
				}
					
				++traderNum;
			}
			
			if (playerNum != traderNum)
			{
				// Get the offer for the other player
				Offer offer = player.MakeOffer(mPlayers[traderNum]);
				
				// Resolve the offer
				Player.ResolveOfferStrict(player, mPlayers[traderNum], offer);
			}
			
			hasTraded[traderNum] = true;
			++numTrades;
		}
		
		
		
		
		// BELOW IS THE CONSTANT TRADE ORDER (STORING IN CASE CONSTANT ORDER SHOULD BE TESTED)
		// Offer a trade to each other player
//		for (int otherPlayerNum = 0; otherPlayerNum < NUM_PLAYERS; ++otherPlayerNum)
//		{
//			if (otherPlayerNum != playerNum)
//			{
//				// Get the offer for the other player
//				Offer offer = player.MakeOffer(mPlayers[otherPlayerNum]);
//				
//				// Resolve the offer
//				Player.ResolveOfferStrict(player, mPlayers[otherPlayerNum], offer);
//			}
//		}
	}
	
	public void RandomlyGenerateResourcesForAllPlayers()
	{
		for (int playerNum = 0; playerNum < NUM_PLAYERS; ++playerNum)
		{
			Player player = mPlayers[playerNum];
		
			// Add random resources
			player.AddResources(Resources.GetRandomlyGeneratedResources(player.GetMaxResourceGain()));
		}
	}
	
	public void PlayGames(int numGames)
	{
		for (int roundNum = 0; roundNum < numGames; ++roundNum)
		{
			// Reset Player's holdings
			for (int i = 0; i < NUM_PLAYERS; ++i)
			{
				mPlayers[i].ResetItems();
			}
			
			// If winning player is >= 0, a player has won
			int winningPlayer = -1;
			
			for (int iterationNum = 0; winningPlayer < 0 && iterationNum < NUM_ITERATIONS; ++iterationNum)
			{
				winningPlayer = SimulateIteration();
				
				if (winningPlayer >= 0)
				{
					// Add a game to each player. Add a win to the winner.
					++mPlayers[winningPlayer].mNumWins;
					
					for (int playerNum = 0; playerNum < NUM_PLAYERS; ++playerNum)
					{
						++mPlayers[playerNum].mNumGames;
					}
				}
			}
		}
		
		// Write results
		SaveResults(GetConfigName() + NUM_ROUNDS + ".csv");
	}
	
	public void SaveResults(String fileName)
	{
		try
		{
		    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		    
		    // Header line
		    writer.write(Player.GetStringFormat());
		    writer.newLine();
		    
		    for (int i = 0; i < NUM_PLAYERS; ++i)
		    {
		    	writer.write(mPlayers[i].ToString());
		    	writer.newLine();
		    }
		     
		    writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String GetConfigName()
	{
		switch (mConfig)
		{
			case CONFIG_1NOTRADE_3PRIORITY:
				return "NoTradeVPriority";
			case CONFIG_1NOTRADE_3LEARNED:
				return "NoTradeVLearned";
			case CONFIG_1PRIORITY_3NOTRADE:
				return "PriorityVNoTrade";
			case CONFIG_1PRIORITY_3LEARNED:
				return "PriorityVLearned";
			case CONFIG_1LEARNED_3NOTRADE:
				return "LearnedVNoTrade";
			case CONFIG_1LEARNED_3PRIORITY:
				return "LearnedVPriority";
			case CONFIG_4NOTRADE:
				return "NoTradeVNoTrade";
			case CONFIG_4PRIORITY:
				return "PriorityVPriority";
			case CONFIG_4LEARNED:
				return "LearnedVLearned";
		}
		
		return "";
	}
	
	public static void main(String args[])
	{
		System.out.println("Start Time:\t" + LocalTime.now());
		for (int i = 0; i < NUM_CONFIGURATIONS; ++i)
		{
			GameManager game = new GameManager(i);
			game.PlayGames(NUM_ROUNDS);
		}
		System.out.println("End Time:\t" + LocalTime.now());
	}
}
