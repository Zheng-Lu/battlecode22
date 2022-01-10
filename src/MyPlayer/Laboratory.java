package MyPlayer;

import battlecode.common.*;
import static MyPlayer.Navigation.*;

public class Laboratory extends RobotPlayer{
    public static Integer maxBotsInRange = 10;

    public static void run(RobotController rc) throws GameActionException {
        //Next update, calcualte threat level of surroding hostiles and min friends, find balance between them
        RobotInfo[] nearbyFriends = rc.senseNearbyRobots(53, rc.getTeam());
        if (nearbyFriends.length > maxBotsInRange){
            MapLocation newLoc = findDirection(rc, 13, true);
            rc.transform();
            moveTo(rc, newLoc);
        }
        else{
            produceGold(rc);
        }

    }

    public static void produceGold(RobotController rc) throws GameActionException{
        if(rc.isActionReady()){
            if(rc.canTransmute()) rc.transmute();
        }
        
    }

    

    private static MapLocation findDirection(RobotController rc, Integer radius, boolean maxOrMin) throws GameActionException{
        Team friend = rc.getTeam();
        MapLocation curLoc = rc.getLocation();
        Integer minFriend = 99999;
        int[][] surrondingCoor = {{0,-26},{26,0},{0,26},{-26,0}};
        MapLocation minFriendLoc = null;
        for(int[] coor : surrondingCoor){
            MapLocation newLoc = curLoc.translate(coor[0], coor[1]);
            Integer numOfFriend = (rc.senseNearbyRobots(newLoc, radius, friend)).length;
            if(numOfFriend < minFriend){
                minFriend = numOfFriend;
                maxBotsInRange = numOfFriend;
                minFriendLoc = newLoc;
            }

        }

        return minFriendLoc;
    }



}
