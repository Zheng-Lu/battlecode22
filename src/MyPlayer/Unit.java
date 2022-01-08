package MyPlayer;

import battlecode.common.*;

public class Unit extends Bot {
    public Unit(RobotController r) throws GameActionException {
        super(r);
    }

    @Override
    public void run() throws GameActionException {
        super.run();
        here = rc.getLocation();
    }

    // navigate towards a particular location
    static boolean goTo(MapLocation destination) throws GameActionException {
        return Navigation.goTo(destination, new SafetyPolicyAvoidAllUnits());
    }

}


