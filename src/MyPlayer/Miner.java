package MyPlayer;

import battlecode.common.*;
import java.util.*;

public class Miner extends RobotPlayer{
    static RobotController rc;

    MapLocation targetMineLoc;

    private void updateTargetMineLoc() throws GameActionException {
        if(targetMineLoc != null) {
            if(rc.canSenseLocation(targetMineLoc) && !rc.isLocationOccupied(targetMineLoc) && rc.senseLead(targetMineLoc) > 0)
                return;
        }
        targetMineLoc = null;
        MapLocation[] candidates = rc.getAllLocationsWithinRadiusSquared(rc.getLocation(), 20);
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

    public void run() throws GameActionException {
        MapLocation curr = rc.getLocation();
        if(targetMineLoc == null || !(targetMineLoc.equals(curr) && rc.senseLead(curr) > 0) && turnCount % 10 == 0)
            updateTargetMineLoc();
        if (targetMineLoc != null) {
            if(curr.equals(targetMineLoc)) {
                tryMine(curr);
            }
            else {
                // TODO: write a moveTo(MapLocation loc) function
                // moveTo(targetMineLoc)
            }
        }
        else {
            // TODO: write a explore() function to explore the map to find resources
            // explore();
        }
    }
}
