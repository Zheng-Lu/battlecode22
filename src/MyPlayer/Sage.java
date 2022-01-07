package MyPlayer;

import battlecode.common.*;

// TODO: TBD
public class Sage extends RobotPlayer{
    public static void run(RobotController rc) throws GameActionException {
        if (rc.isActionReady()){
            if (rc.canEnvision(AnomalyType.CHARGE)){
                rc.envision(AnomalyType.CHARGE);
            }
        }
    }
}
