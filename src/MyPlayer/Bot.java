package MyPlayer;

import java.util.*;

import battlecode.common.*;

public class Bot {
    public static RobotController rc;
    public static RobotType type;
    public static Team enemy;
    public static Team us;
    public static MapLocation ArchonLoc;
    public static MapLocation here;
    public static int mapHeight;
    public static int mapWidth;
    public static MapLocation[] PossibleEnemyArchonLoc;
    public static MapLocation[] Laboratories;
    public static int numLaboratories;
    public static MapLocation[] Watchtowers;
    public static int numWatchtotwers;
    public static Random rand;
    public static Direction lastExploreDir;
    public static int[] unitCounts = {0,0,0,0,0,0,0,0,0,0};

    public Bot() {
        return;
    }

    public static enum Symmetry {
        VERTICAL,
        HORIZONTAL,
        ROTATIONAL,
    }

    public static Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST
    };

    public static RobotType[] builtByBuilder = {RobotType.LABORATORY, RobotType.WATCHTOWER, RobotType.ARCHON};
    public static RobotInfo[] nearbyEnemy = {};

    public static int turnCount = 0;
    public static int numMiners = 0;
    public static int numBuilders = 0;
    public static int round = 0;


    public Bot(RobotController r) throws GameActionException {
        rc = r;
        type = rc.getType();
        us = rc.getTeam();
        enemy = rc.getTeam().opponent();
        mapHeight = rc.getMapHeight();
        mapWidth = rc.getMapWidth();
        round = rc.getRoundNum();
        Laboratories = new MapLocation[100];
        Watchtowers = new MapLocation[100];
        rc.senseNearbyRobots(50, enemy);
    }

    public void run() throws GameActionException {
        turnCount++;
        if(turnCount>1)
            round++;
    }

    static boolean nearbyRobot(RobotType target) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots();
        for(RobotInfo r : robots) {
            if(r.getType() == target) {
                return true;
            }
        }
        return false;
    }

    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    static boolean tryBuild(RobotType type, Direction dir) throws GameActionException {
        if (rc.isActionReady() && rc.canBuildRobot(type, dir)) {
            rc.buildRobot(type, dir);
            return true;
        } else return false;
    }

}
