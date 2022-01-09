package MyPlayer;

import battlecode.common.*;

import static MyPlayer.Communication.*;
import static MyPlayer.Navigation.moveTo;

// TODO: Design a better attack and move strategy
public class Soldier extends RobotPlayer{
    /**
     * Run a single turn for a Soldier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static MapLocation parentArchon;
    //patrol
    static void patrol(RobotController rc) throws  GameActionException {
        MapLocation curr = rc.getLocation();
        Direction dir = parentArchon.directionTo(curr);
        int patrolRadiusSq = 10;
        if (curr.distanceSquaredTo(parentArchon) < patrolRadiusSq) {
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    break;
                }
                dir = dir.rotateLeft();
            }
        } else if (curr.distanceSquaredTo(parentArchon) > (patrolRadiusSq * 2)) {
            dir = dir.opposite();
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    break;
                }
                dir = dir.rotateRight();
            }
        } else {
            dir = dir.rotateLeft().rotateLeft();
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    break;
                }
                dir = dir.rotateLeft();
            }
        }
    }

    public static void run(RobotController rc) throws GameActionException {

        if (turnCount <= 1) {
            for (RobotInfo info : rc.senseNearbyRobots(2)){
                if (info.getType().equals(RobotType.ARCHON)){
                    parentArchon = info.getLocation();
                    break;
                }
            }
        }

        // patrol
        if (turnCount < 20) {
            // perception
            boolean nearEnemy = false;
            MapLocation enemyLocation = null;
            int enemyDistance = 9999;
            for (RobotInfo info : rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam().opponent())) {
                if (info.getLocation().distanceSquaredTo(parentArchon) < enemyDistance) {
                    nearEnemy = true;
                    enemyLocation = info.getLocation();
                }
            }
            if (nearEnemy) {
                Direction dir = rc.getLocation().directionTo(enemyLocation);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                }
                if (rc.canAttack(enemyLocation)) {
                    rc.attack(enemyLocation);
                }
            } else{
                patrol(rc);
            }
        }else {
            // attack enemy archon
            MapLocation nearestTarget = null;
            int shortestDistance = 9999;
            for (int i = 0; i < 4; i++) {
                if (haveInfo(rc, i) && !ifArchonAnnihilated(rc, i)) {
                    MapLocation target = readLocation(rc, i);
                    if (target.distanceSquaredTo(rc.getLocation()) < shortestDistance) {
                        shortestDistance = target.distanceSquaredTo(rc.getLocation());
                        nearestTarget = target;
                    }
                }
                if (ifArchonAnnihilated(rc,i)) {
                    System.out.println(i + " Annihilated!!!");
                }
            }

            if (nearestTarget != null) {
                //check if enemy archon annihilated
                annihilatedArchon(rc, nearestTarget);
                // attack
                if (rc.canAttack(nearestTarget)) {
                    rc.attack(nearestTarget);
                }
                // move towards target
                if (shortestDistance < rc.getType().actionRadiusSquared*2/3) {
                    Direction dir = rc.getLocation().directionTo(nearestTarget);
                    for (int i = 0; i < 8; i++) {
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                            break;
                        }
                        dir = dir.rotateLeft();
                    }
                }else {
                    moveTo(rc, nearestTarget);
                }
            }
        }
    }


}
