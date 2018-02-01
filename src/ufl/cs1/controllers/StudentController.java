package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;


import java.util.List;

// MICHAEL O'CONNELL // JACK BRENNAN // BRANDON ROSENTHAL // STEVEN SIEGEL //

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();

        //GHOSTS
		Defender red = enemies.get(0); //CHASER
		Defender orange = enemies.get(1); //PROTECTS POWER PILLS
		Defender pink = enemies.get(2); // PROTECTS PILLS
		Defender blue = enemies.get(3); //GETS IN FRONT OF ATTACKER

		if (!red.isVulnerable()) { // RED WILL TARGET THE ATTACKER
			actions[0] = red.getNextDir(game.getAttacker().getLocation(), !red.isVulnerable());
		}
		if(!orange.isVulnerable()) { // ORANGE WILL TARGET POWER PILLS
			actions[1] = orange.getNextDir(orange.getTargetNode(game.getCurMaze().getPowerPillNodes(), true), true);
		}
		if(!pink.isVulnerable()) { // PINK WILL TARGET PILLS
			actions[2] = pink.getNextDir(pink.getTargetNode(game.getPillList(), true), true);
		}
		// BLUE WILL TRY TO GET IN FRONT OF MS. PAC MAN.
		if(!blue.isVulnerable()){ // BLUE WILL REVERSE THE ATTACKERS MOVES
            actions[3] = game.getAttacker().getReverse();
            if(blue.getLocation().isJunction()){ // WHEN BLUE IS AT A JUNCTION, IT WILL TARGET THE ATTACKER
                actions[3] = blue.getNextDir(game.getAttacker().getLocation(),true);
            }
        }
        // IF BLUE IS CLOSE TO RED, IT WILL REVERSE DIRECTION
        if ((red.getLocation().getPathDistance(blue.getLocation()) <= 35) && (!blue.isVulnerable())){
		    actions[3] = blue.getReverse();
    }


        // TRY CATCH FOR IF THERE ARE NO MORE POWER PILLS LEFT
		    try { // IF ATTACKER IS 35 PIXELS AWAY FROM A POWER PILL ALL GHOSTS EXCEPT RED WILL RUN AWAY
                if ((game.getAttacker().getLocation().getPathDistance(game.getAttacker().getTargetNode(game.getPowerPillList(), true)) <= 35)) {
                    actions[1] = orange.getNextDir(game.getAttacker().getLocation(), false);
                    actions[2] = pink.getNextDir(game.getAttacker().getLocation(), false);
                    actions[3] = blue.getNextDir(game.getAttacker().getLocation(), false);
                }
            } catch (Exception e){ // IF THERE ARE NO MORE POWER PILLS, ORANGE WILL TARGET RED
            if (!orange.isVulnerable()) {
                actions[1] = orange.getNextDir(red.getLocation(),true);
                actions[1] = orange.getReverse();
            }
            }


            //VULNERABLE PHASE: IF A GHOST IS VULNERABLE, THAT GHOST WILL RUN AWAY.
		if(red.isVulnerable()) {
			actions[0] = red.getNextDir(game.getAttacker().getLocation(), false);
		}
		if(orange.isVulnerable()) {
			actions[1] = orange.getNextDir(game.getAttacker().getLocation(), false);
		}
		if(pink.isVulnerable()) {
			actions[2] = pink.getNextDir(game.getAttacker().getLocation(), false);
		}
		if(blue.isVulnerable()) {
			actions[3] = blue.getNextDir(game.getAttacker().getLocation(), false);
		}

		return actions;
	}

}