package lubao_5;

import battlecode.common.*;


strictfp class Laboratory {
    static void run(RobotController rc) throws GameActionException {
        if (rc.canTransmute()) {
            rc.transmute();
        }
    }
}
