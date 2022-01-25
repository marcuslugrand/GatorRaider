package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.controllers.example.OriginalDefenders;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
import game.models.Node;
import game.view.GameView;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public final class StudentAttackerController implements AttackerController
{
	private Node bottomleftpill;
	public void init(Game game)
	{
		bottomleftpill = game.getPowerPillList().get(3);
	}
	public void shutdown(Game game)
	{
		//Nothing done at the end of the game
	}
	public int update(Game game, long timeDue)
	{
		//This line codes for the first level as level one can be completed by having the gator simply eat the available normal pills
		if (game.getLevel() == 0)
		{
			return
					game.getAttacker().getNextDir(game.getAttacker().getTargetNode(game.getPillList(
					), true), true);
		}
		//This code codes for the second level and above
		if (game.getLevel() >= 1)
		{
			//This try catch block is used to catch the NullPointerException that will be eventually be thrown
			try
			{
				//This code calculates the nearest power pill nearby
				Node target;
				List<Node> pills = game.getPowerPillList();
				target = game.getAttacker().getTargetNode(pills, true);
				//These variables calculate the distance from each defender the attacker
				int defender1distance =
						game.getAttacker().getLocation().getPathDistance(game.getDefender(0).getLocation
								());
				int defender2distance =
						game.getAttacker().getLocation().getPathDistance(game.getDefender(1).getLocation
								());
				int defender3distance =
						game.getAttacker().getLocation().getPathDistance(game.getDefender(2).getLocation
								());
				int defender4distance =
						game.getAttacker().getLocation().getPathDistance(game.getDefender(3).getLocation());
				//This code ensures the gator will go the bottomleft pill each time
				if (game.checkPowerPill(bottomleftpill) &&
						game.getAttacker().getLocation().getPathDistance(bottomleftpill) < 10)
				{
					//Ensures the gator will eat the first power pill as soon as an enemy leaves the lair
					if ((defender1distance < 20 && defender1distance > -1) ||
							(defender2distance < 20 && defender2distance > -1) || (defender3distance < 20 &&
							defender3distance > -1) || (defender4distance < 20 && defender4distance > -1))
					{
						return game.getAttacker().getNextDir(bottomleftpill,
								true);
					}
					return game.getAttacker().getReverse();
				}
				//This code ensures the gator will go towards the nearest vulnerable defender if there is one near by
 else if (game.getDefender(0).isVulnerable() ||
					game.getDefender(1).isVulnerable() || game.getDefender(2).isVulnerable() ||
					game.getDefender(3).isVulnerable())
			{
				//Calculates the closest defender
				int index = 0;
				int minimium = 10000;
				for (int i = 0; i < 4; i++)
				{
					//Ensures the gator won't accidentally eat a power pill to get a vulnerable defender
					if ((game.getDefender(0).isVulnerable() ||
							game.getDefender(1).isVulnerable() || game.getDefender(2).isVulnerable() ||
							game.getDefender(3).isVulnerable()) &&
							(game.getAttacker().getLocation().getPathDistance(target) < 4))
					{
						return game.getAttacker().getReverse();
					}
					//Gets the closest vulnerable defender
					if
					((game.getDefender(i).getLocation().getPathDistance(game.getAttacker().getLocation()) < minimium) && game.getDefender(i).isVulnerable())
					{
						index = i;
						minimium =
								game.getDefender(i).getLocation().getPathDistance(game.getAttacker().getLocation
										());
					}
				}
				return
						game.getAttacker().getNextDir(game.getDefender(index).getLocation(), true);
			}
				//If the gator is close to a power pill and the defenders aren't nearby the gator will oscillate
 else if
			((game.getAttacker().getLocation().getPathDistance(target) < 5 &&
							defender1distance > 10 && defender2distance > 10 && defender3distance > 10 && defender4distance > 10))
			{
				return game.getAttacker().getReverse();
			}
				return game.getAttacker().getNextDir(target, true);
			}
			//Catches the eventual NullPointerException
			catch (NullPointerException ee)
			{
				//If a defender is vulnerable, get the closest vulnerable defender
				if (game.getDefender(0).isVulnerable() ||
						game.getDefender(1).isVulnerable() || game.getDefender(2).isVulnerable() ||
						game.getDefender(3).isVulnerable())
				{
					int index = 0;
					int minimium = 10000;
					for (int i = 0; i < 4; i++)
					{
						if ((game.getDefender(i).isVulnerable() &&
								game.getDefender(i).getLocation().getPathDistance(game.getAttacker().getLocation
										()) < minimium))
						{
							index = i;
							minimium =
									game.getDefender(index).getLocation().getPathDistance(game.getAttacker().getLocation());
						}
					}
					return
							game.getAttacker().getNextDir(game.getDefender(index).getLocation(), true);
				}
				//If all avaliable power pills are gone, get the closest pellet while not reversing direction at any time
				if
				(game.getAttacker().getNextDir(game.getAttacker().getTargetNode(game.getPillList
						(), true), true) == game.getAttacker().getReverse())
				{
					if (game.getAttacker().getLocation().isJunction())
					{
						return game.getAttacker().getPossibleDirs(false).get(0);
					}
					return game.getAttacker().getDirection();
				}
				return
						game.getAttacker().getNextDir(game.getAttacker().getTargetNode(game.getPillList(
						), true), true);
			}
		}
		return -1;
	}
}