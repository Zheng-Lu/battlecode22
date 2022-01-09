package MyPlayer;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Communication {

    // check if shared array have information
    static boolean  haveInfo(RobotController rc, int index) throws  GameActionException {
        return  rc.readSharedArray(index) != 0;
    }

    // save location from shared array
    static void saveLocation(RobotController rc, MapLocation location, int index) throws GameActionException {
        int num;
        num = location.x * 64 + location.y;
        rc.writeSharedArray(index, num);
    }

    // read location from shared array
    static MapLocation readLocation(RobotController rc, int index) throws GameActionException {
        int num = rc.readSharedArray(index);
        int y = num % 64;
        num = num / 64;
        int x = num % 64;
        return new MapLocation(x, y);
    }

    static boolean ifArchonAnnihilated(RobotController rc, int index) throws GameActionException{
        return rc.readSharedArray(index) / 32768 > 0;
    }

    static void annihilatedArchon(RobotController rc, MapLocation location) throws  GameActionException{
        if (rc.getLocation().distanceSquaredTo(location) < rc.getType().visionRadiusSquared && (!rc.canSenseRobotAtLocation(location) || (rc.canSenseRobotAtLocation(location) && !rc.senseRobotAtLocation(location).getType().equals(RobotType.ARCHON)))) {
            for (int i=0; i < 4; i++) {
                if (readLocation(rc, i).equals(location)) {
                    rc.writeSharedArray(i, rc.readSharedArray(i) + 32678);
                    break;
                }
            }
        }
    }

}
