package MyPlayer;

import battlecode.common.*;

// TODO: Design a better building strategy
public class Builder extends RobotPlayer{
    RobotController rc;
    Direction[] dirs = Direction.values();

    // TODO:
    void tryBuilding(){

    }


    // TODO:
    void build(RobotType type){
        MapLocation myLoc = rc.getLocation();
        Direction dir = Direction.NORTH;
        for (int i = 0; i < 8; ++i) {
            if(rc.canBuildRobot(type, dir)) {
                MapLocation newLoc = myLoc.add(dir);
                if (rc.canSenseLocation(newLoc)){

                }
            }
        }

    }

    public static void run(RobotController rc) throws GameActionException {
        Direction dir = directions[rng.nextInt(directions.length)];
        Direction dir2 = directions[rng.nextInt(directions.length)];
        
        if (rc.isActionReady()) {
            // Build a laboratory
            if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
                rc.buildRobot(RobotType.LABORATORY, dir);
            }

            // Build a Watchtotwer
            if (rc.canBuildRobot(RobotType.WATCHTOWER, dir2)) {
                rc.buildRobot(RobotType.WATCHTOWER, dir2);
            }
        }

        // Also try to move randomly.
        Direction dir3 = directions[rng.nextInt(directions.length)];
        if (rc.canMove(dir3)) {
            rc.move(dir3);
            System.out.println("I moved!");
        }

    }



}
