package MyPlayer;

import battlecode.common.*;

import static MyPlayer.Navigation.explore;

// TODO: Design a better building strategy
public class Builder extends Unit{
    RobotController rc;
    Direction[] dirs = Direction.values();

    public Builder(RobotController rc) throws GameActionException {
        super(rc);
    }

    private MapLocation tryBuildWithin(RobotType type, MapLocation loc, int dist) throws GameActionException {
        Direction dirL = here.directionTo(loc);
        Direction dirR = dirL.rotateRight();
        for(int i=0;i<4;i++) {
            MapLocation locL = here.add(dirL);
            MapLocation locR = here.add(dirR);
            if(rc.canBuildRobot(type, dirL) && locL.distanceSquaredTo(loc) <= dist) {
                rc.buildRobot(type, dirL);
                return locL;
            }
            if(rc.canBuildRobot(type, dirR) && locR.distanceSquaredTo(loc) <= dist) {
                rc.buildRobot(type, dirR);
                return locR;
            }
            dirL = dirL.rotateLeft();
            dirR = dirR.rotateRight();
        }
        return null;
    }

    private MapLocation chooseLabLoc() {
        MapLocation bestLoc = ArchonLoc;
        int minDist = here.distanceSquaredTo(bestLoc);
        RobotType.LABORATORY.ordinal();
        for(int i=0; i<unitCounts[RobotType.LABORATORY.ordinal()]; i++){
            int dist = here.distanceSquaredTo(Laboratories[i]);
            if(dist < minDist){
                bestLoc = Laboratories[i];
                minDist = dist;
            }
        }
        return bestLoc;
    }

    private MapLocation chooseWatchtowerLoc() {
        MapLocation bestLoc = ArchonLoc;
        int minDist = here.distanceSquaredTo(bestLoc);
        RobotType.WATCHTOWER.ordinal();
        for(int i=0; i<unitCounts[RobotType.WATCHTOWER.ordinal()]; i++){
            int dist = here.distanceSquaredTo(Watchtowers[i]);
            if(dist < minDist){
                bestLoc = Watchtowers[i];
                minDist = dist;
            }
        }
        return bestLoc;
    }

    private boolean buildWatchtowerIfShould() throws GameActionException {
        RobotType rt = BuilderStrategy.determineBuildingNeeded();
        if(rt != null && tryBuild(rt, randomDirection())){
            return true;
        }

        // TODO: Watchtower building location optimization
        // radiusSquared = sqrt(34)
        if(nearbyRobot(RobotType.ARCHON) && rc.senseNearbyRobots(6).length > 30){
            if(tryBuild(RobotType.WATCHTOWER, randomDirection())){
                System.out.println("Created a Watchtower");
                return true;
            }
        }
        return false;
    }

    private boolean buildLabIfShould() throws GameActionException {
        RobotType rt = BuilderStrategy.determineBuildingNeeded();
        if(rt != null && tryBuild(rt, randomDirection())){
            return true;
        }

        // TODO: Laboratory building location optimization
        // Transmuting formula: 1 Au = floor(20 - 15e^(-0.02n)) Pb
        // radiusSquared = sqrt(53)
        if(rc.senseNearbyRobots(7, us).length < 30
            && rc.senseNearbyRobots(7, enemy).length < 20)
        {
            if(tryBuild(RobotType.LABORATORY, randomDirection())){
                System.out.println("Created a Laboratory");
                return true;
            }
        }
        return false;
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


    // TODO: Mutating building Strategy

    // TODO: Repairing building Strategy


    public void run() throws GameActionException {
        if(buildLabIfShould()){

        }else if (buildWatchtowerIfShould()){

        }
        else{
            explore();
        }

    }



}
