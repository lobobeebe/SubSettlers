package kevandcode.learning;

import java.io.FileInputStream;
import java.util.Properties;

import kevandcode.Offer;
import kevandcode.Player;
import kevandcode.Resources;

public class LearningManager
{	
	public static void main(String[] args)
	{		
		Properties properties = new Properties();
		
		try
		{
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			properties.load(new FileInputStream(rootPath + "learning.properties"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Episodic Parameters
		int numEpisodes = Integer.parseInt(properties.getProperty("numEpisodes", "100"));
		int numActionsPerEpisode = Integer.parseInt(properties.getProperty("numActionsPerEpisode", "100"));
		
		// Learning Parameters
		float greedyEpsilon = Float.parseFloat(properties.getProperty("greedyEpsilon", ".8"));
		float learningRate = Float.parseFloat(properties.getProperty("learningRate", ".5"));
		float rewardDiscount = Float.parseFloat(properties.getProperty("rewardDiscount", ".9"));
		int rewardMultiplier = Integer.parseInt(properties.getProperty("rewardMultiplier", "100"));
		String brainName = properties.getProperty("brainName", "brain.txt");

		// Initialize players
		QPlayer player = new QPlayer();
		player.SetLearningParameters(greedyEpsilon, learningRate, rewardDiscount, rewardMultiplier);
		
		Player other = new Player();
		
		// Learning Episodes
		for (int episodeNum = 0; episodeNum < numEpisodes; ++episodeNum)
		{
			player.ResetItems();
			other.ResetItems();
			
			boolean gameOver = false;
			
			for (int actionNum = 0; !gameOver && actionNum < numActionsPerEpisode; ++actionNum)
			{			
				// Provide all players a random number of resources
				player.AddResources(Resources.GetRandomlyGeneratedResources(player.GetMaxResourceGain()));
				other.AddResources(Resources.GetRandomlyGeneratedResources(other.GetMaxResourceGain()));
				
				// Practice an offer to explore the results
				Offer offer = player.PracticeMakeOffer(other);
				
				// Resolve offer
				Player.ResolveOfferStrict(player, other, offer);
				
				// Build to determine reward
				player.Build();
				other.Build();
				
				// Update the Brain with result from trade
				player.Learn(other);
				
				if (player.mVictoryPoints > 10 || other.mVictoryPoints > 10)
				{
					gameOver = true;
				}
			}
		}
		
		player.SaveBrain(brainName);
		System.out.println("Done");
	}
}
