package MyPlayer;

import battlecode.common.*;

// TODO: TBD
public class Laboratory extends RobotPlayer{
    public static void run(RobotController rc) throws GameActionException {
        if (rc.isActionReady()){
            if (rc.canTransmute()) {
                rc.transmute();
            }
        }
    }
}
