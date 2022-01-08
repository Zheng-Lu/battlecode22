package MyPlayer;

import battlecode.common.*;
import battlecode.util.*;
import java.util.*;

import static MyPlayer.Navigation.*;

public class Miner extends Unit{
    MapLocation targetMineLoc;

    public Miner(RobotController r) throws GameActionException {
        super(r);
    }

    public void run() throws GameActionException {
        super.run();
        if(targetMineLoc == null || !(targetMineLoc.equals(here) && rc.senseLead(here) > 0) && turnCount % 10 == 0)
            updateTargetMineLoc();
        if (targetMineLoc != null) {
            if(here.equals(targetMineLoc)) {
                tryMine(here);
            }
            else {
                // TODO: write a moveTo(MapLocation loc) function
                goTo(targetMineLoc);
            }
        }
        else {
            // TODO: write a explore() function to explore the map to find resources
            explore();
        }
    }

    private void updateTargetMineLoc() throws GameActionException {
        if(targetMineLoc != null) {
            if(rc.canSenseLocation(targetMineLoc) && !rc.isLocationOccupied(targetMineLoc) && rc.senseLead(targetMineLoc) > 0)
                return;
        }
        targetMineLoc = null;
        MapLocation[] candidates = rc.senseNearbyLocationsWithLead(20);
        int minDist = Integer.MAX_VALUE;
        int xsum = 0;
        int ysum = 0;
        for(MapLocation cand: candidates){
            xsum += cand.x;
            ysum += cand.y;
            int dist = rc.getLocation().distanceSquaredTo(cand);
            if(!rc.isLocationOccupied(cand) && dist < minDist){
                targetMineLoc = cand;
                minDist = dist;
            }
        }
    }

    static boolean tryMine(MapLocation dir) throws GameActionException {
        if (rc.isActionReady() && rc.canMineLead(dir)) {
            rc.mineLead(dir);
            return true;
        } else return false;
    }
}

