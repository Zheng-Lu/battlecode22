package MyPlayer;

import battlecode.common.*;
import java.util.*;

interface NavSafetyPolicy {
    public boolean isSafeToMoveTo(MapLocation loc) throws GameActionException;
}

class SafetyPolicyCrunch extends Bot implements NavSafetyPolicy {

    public SafetyPolicyCrunch(RobotController r) throws GameActionException {
        super(r);
    }

    public boolean isSafeToMoveTo(MapLocation loc) throws GameActionException {
        if(rc.canSenseLocation(loc) && rc.senseRubble(loc) > 80) {
            return false;
        }
        return true;
    }
}

public class Navigation extends Bot {

    public Navigation(RobotController r) throws GameActionException {
        super(r);
    }

    // TODO: Move to specific given destination
    public static void moveTo(MapLocation destination) throws  GameActionException{

    }

    // TODO: Exploring map strategy
    public static void explore() throws GameActionException{
        Direction[] dirs = new Direction[2];
        for (Direction dir : dirs){

        }

    }
}

