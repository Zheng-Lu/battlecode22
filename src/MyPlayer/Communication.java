package MyPlayer;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Communication {

    // save location from shared array
    public static void saveLocation(RobotController rc, MapLocation location, int index) throws GameActionException {
        int num;
        num = location.x * 64 + location.y;
        rc.writeSharedArray(index, num);
    }

    // read location from shared array
    public static MapLocation readLocation(RobotController rc, int index) throws GameActionException {
        int num = rc.readSharedArray(index);
        int y = num % 64;
        num = num / 64;
        int x = num % 64;
        return new MapLocation(x, y);
    }

}
