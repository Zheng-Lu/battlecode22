package MyPlayer;

import battlecode.common.*;
import java.util.PriorityQueue;

interface NavSafetyPolicy {
    public boolean isSafeToMoveTo(MapLocation loc) throws GameActionException;
}

class SafetyPolicyCrunch extends Bot implements NavSafetyPolicy {

    public SafetyPolicyCrunch(RobotController r) throws GameActionException {
        super(r);
    }

    public boolean isSafeToMoveTo(MapLocation loc) throws GameActionException {
        if(rc.canSenseLocation(loc) && rc.senseRubble(loc) > 80) {
            return false;
        }
        return true;
    }
}

public class Navigation extends Bot {

    public Navigation(RobotController r) throws GameActionException {
        super(r);
    }

    // TODO: Move to specific given destination
    public static void moveTo(MapLocation destination) throws  GameActionException{
        Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
        };
        MapLocation curLoc = rc.getLocation();
        if(!rc.isMovementReady()){
            // Actions needed when rc can't move
        }
        else if(curLoc.isAdjacentTo(destination)){
            rc.move(curloc.directionTo(destination));
        }
        else{
        //Minor bug
        //     PriorityQueue<MapLocation, Integer> pQueue = new PriorityQueue<>();
        //     pQueue.add(curLoc, rc.senseRubble(curLoc));
        //     Hashtable<MapLocation, Integer> visited = new Hashtable<MapLocation, Integer>();
        //     Hashtable<MapLocation, MapLocation> path = new Hashtable<>();
        //     MapLocation[] allDirections = new MapLocation;
        //     //Store all the directions needed to reach the destination
        //     visited.put(curLoc, rc.senseRubble(curLoc));
        //     path.put(null, curLoc);

        //     while (!pQueue.isEmpty()){
        //         MapLocation current = pQueue.poll();

        //         if(current.equals(destination)){
        //             break;
        //         }

        //         for(Direction direction : directions){
        //             MapLocation adjLoc = current.add(direction);
        //             Integer new_cost = rc.senseRubble(adjLoc) + visited.get(current);
        //             if(!visited.containsKey(adjLoc)){
        //                 visited.put(adjLoc, new_cost);    
        //                 Integer priority = new_cost + adjLoc.distanceSquaredTo(destination);
        //                 pQueue.put(adjLoc, priority);
        //                 path.put(current, adjLoc);
        //                 allDirections.add(direction);
        //             }
        //         }
            
        //     }

        //     rc.move(allDirections[1]);
        // }
            


    }

    // TODO: Exploring map strategy
    public static void explore() throws GameActionException{
        Direction[] dirs = new Direction[2];
        for (Direction dir : dirs){

        }

    }
}

