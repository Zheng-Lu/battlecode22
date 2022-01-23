package lubao_5;

import battlecode.common.*;

strictfp class Builder {
    static MapLocation backupLocation = RobotPlayer.getRandomMapLocation();
    static MapLocation parentLocation;
    static MapLocation buildLocation = new MapLocation(-1,-1);
    static RobotType myType = RobotType.BUILDER;
    static int startRound = -1;
    static int currRound;
    static boolean TryBuild = true;
    static int numEarlyLabs;
    static final int LAB_COST = 180;

    static MapLocation getBuildLocation(RobotController rc, MapLocation[] nearLocations) throws GameActionException{
        MapLocation build_Location = null;
        MapLocation myLocation = rc.getLocation();

        int minDistance = 99999;
        int minRubble = 99999;
        // find block with fewest rubble
        for (MapLocation location : nearLocations){
            if (rc.senseRubble(location) < minRubble && location.distanceSquaredTo(parentLocation) > 4 && !rc.canSenseRobotAtLocation(location)) {
                minDistance = myLocation.distanceSquaredTo(location);
                minRubble = rc.senseRubble(location);
                build_Location = location;
            }
        }
        return build_Location;
    }

    static void run(RobotController rc) throws GameActionException {
        MapLocation[] nearlocations;
        MapLocation myLocation = rc.getLocation();
        nearlocations = rc.getAllLocationsWithinRadiusSquared(myLocation, myType.visionRadiusSquared);
        ArchonLocation[] archLocs = Communications.getArchonLocations(rc);

        int leadAmount = rc.getTeamLeadAmount(rc.getTeam());
        int goldAmount = rc.getTeamGoldAmount(rc.getTeam());

        if (startRound == -1) {
            startRound = rc.getRoundNum();
        }
        currRound = rc.getRoundNum() - startRound;
        // denote parent location
        if (currRound == 0) {
            for (RobotInfo info : rc.senseNearbyRobots(2)) {
                if (info.getType() == RobotType.ARCHON) {
                    parentLocation = info.getLocation();
                    break;
                }
            }
        }
        /////////////////////////////////////
        boolean defendArchon = false;

        for (ArchonLocation archLoc : archLocs) {
            if (archLoc != null && archLoc.exists && archLoc.shouldDefend) {
                defendArchon = true;
            }
        }

        // TODO: Adjust the number of labs at early stage
        if(leadAmount > LAB_COST*2.5){
            numEarlyLabs = 2;
        }else{
            numEarlyLabs = 1;
        }

        // every 120 try build
        if (currRound % 120 == 0) {
            TryBuild = true;
        }

        // find a location to build
        if (buildLocation.equals(new MapLocation(-1,-1)) || rc.canSenseRobotAtLocation(buildLocation)){
            buildLocation = getBuildLocation(rc, nearlocations);
        }

////////////////////////////////////////////////////////////////////////////////////////////
        if (TryBuild && myLocation.distanceSquaredTo(buildLocation) <= 2) {
            Direction dir = myLocation.directionTo(buildLocation);
            if (rc.canBuildRobot(RobotType.LABORATORY, dir) && !defendArchon) {
                rc.buildRobot(RobotType.LABORATORY, dir);
                TryBuild = false;
            }

        }else{
            if (TryBuild && rc.isMovementReady()) {
                RobotPlayer.move2(rc, buildLocation, 2);
            }
        }
    //try to repair near buildings
        for (RobotInfo info : rc.senseNearbyRobots(myType.actionRadiusSquared, rc.getTeam())) {
            if (!rc.isActionReady()) break;
            if (info.getType() == RobotType.LABORATORY && info.getHealth() < RobotType.LABORATORY.health) {
                rc.setIndicatorString("repair " + info.health);
                if (rc.canRepair(info.getLocation())) {
                    rc.repair(info.getLocation());
                }
            }
        }
    }
}
