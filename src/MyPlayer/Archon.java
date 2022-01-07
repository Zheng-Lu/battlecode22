package MyPlayer;

import battlecode.common.*;

public class Archon extends RobotPlayer {
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    // TODO: Building Robot Strategy
    public static void run(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        Direction dir = directions[rng.nextInt(directions.length)];
        Direction dir2 = directions[rng.nextInt(directions.length)];

        if (rng.nextBoolean()) {
            // Let's try to build a miner.
            rc.setIndicatorString("Trying to build a miner");
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
            }
        } else {
            // Let's try to build a soldier.
            rc.setIndicatorString("Trying to build a soldier");
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
            }
        }

        if (rng.nextBoolean()) {
            // Build a builder.
            rc.setIndicatorString("Trying to build a Builder");
            if (rc.canBuildRobot(RobotType.BUILDER, dir2)) {
                rc.buildRobot(RobotType.BUILDER, dir2);
            }
        } else {
            // Build a miner.
            rc.setIndicatorString("Trying to build a miner");
            if (rc.canBuildRobot(RobotType.MINER, dir2)) {
                rc.buildRobot(RobotType.MINER, dir2);
            }
        }
    }
}
