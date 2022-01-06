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
        if(rc.canSenseLocation(loc) && rc.senseRubble(loc) > 50) {
            return false;
        }
        return true;
    }
}

public class Navigation extends Bot {

    public Navigation(RobotController r) throws GameActionException {
        super(r);
    }

    public static void moveTo(MapLocation destination) throws  GameActionException{

    }

    public static void explore() throws GameActionException{
        for (Direction dir : )

    }
}

