package MyPlayer;

import battlecode.common.*;
import battlecode.util.*;
import static MyPlayer.Navigation.*;
import java.util.Hashtable;

// TODO: TBD
public class Watchtower extends RobotPlayer {
    private static MapLocation highestThreatLoc = null;
    public static Hashtable<RobotType, Integer> robotThreat = new Hashtable<RobotType, Integer>(){
        {
            put(RobotType.SOLDIER, 3);
            put(RobotType.SAGE, 45);
            put(RobotType.WATCHTOWER, 5);
            put(RobotType.MINER, 0);
            put(RobotType.LABORATORY, 0);
            put(RobotType.ARCHON, 0);
            put(RobotType.BUILDER, 0);
        }
    };
    //Indicate each robot type threat
    //ARCHON is a special unit, deal with different strategy in conditions


    public static void run(RobotController rc) throws GameActionException {
        MapLocation curLoc = rc.getLocation();
        Team friend = rc.getTeam();
        RobotInfo[] enemyWithinAction = rc.senseNearbyRobots(20, friend.opponent());
        
        if(rc.getMode().equals(RobotMode.TURRET)){
            if(enemyWithinAction.length != 0){
                //Judge threat level within aciton radius
                Integer threat = judgeSurronding(curLoc, rc, 20);
                if(threat > 5){
                    // Threat level too high
                    rc.transform();
                    // Move to safe place
                    moveTo(rc, findDirection(rc, 14, false));
                }
                else{
                    if(rc.canAttack(highestThreatLoc)){
                        rc.attack(highestThreatLoc);
                    }
                    //Fightable
                    //Attack robot with highest threat level
                }

            }
            else{
                // Find enemy
                rc.transform();
                moveTo(rc, findDirection(rc, 14, true));
                
            }
        }
        else if(rc.getMode().equals(RobotMode.PORTABLE)){
            if(enemyWithinAction.length == 0){
                // No enemy within action
                moveTo(rc, new MapLocation(21,20));
                // moveTo(rc, findDirection(rc, 14, true));
                //explore
            }
            else{
                rc.transform();
            }
        }

    }

    private static MapLocation findDirection(RobotController rc, Integer radius, boolean maxOrMin) throws GameActionException{
        MapLocation curLoc = rc.getLocation();
        Integer maxMinThreat = maxOrMin ? -99999 : 99999;
        int[][] surrondingCoor = {{0,-20},{20,0},{0,20},{-20,0}};
        MapLocation maxMinThreatLoc = null;
        for(int[] coor : surrondingCoor){
            MapLocation newLoc = curLoc.translate(coor[0], coor[1]);
            Integer threat = judgeSurronding(newLoc, rc, radius);
            if((maxOrMin && threat > maxMinThreat) || (!maxOrMin && threat < maxMinThreat)){
                maxMinThreat = threat;
                maxMinThreatLoc = newLoc;
            }

        }

        return maxMinThreatLoc;
    }


    public static Integer judgeSurronding(MapLocation center, RobotController rc, Integer radius) throws GameActionException {
        
        Integer maxThreat = -99;
        RobotInfo[] allBots = rc.senseNearbyRobots(radius);
        Team friend = rc.getTeam();
        Integer surrondingThreat = 0;
        for(RobotInfo bot : allBots){
            Integer robotThreatValue = robotThreat.get(bot.getType());

            if(radius == 20){
                //When radius equal to action radius, also find target with highest threat
                
                if(robotThreatValue > maxThreat){
                    maxThreat = robotThreatValue;
                    highestThreatLoc = bot.getLocation();
                }
                
            }
            if(bot.getType().equals(RobotType.ARCHON)){
                maxThreat = 999999999;
                highestThreatLoc = bot.getLocation();
            }

            if(bot.getTeam().equals(friend)){
                // Bot is friendly
                surrondingThreat -= robotThreatValue;
            }
            else{
                // Bot is hostile
                surrondingThreat += robotThreatValue;
            }
            
        }
        return surrondingThreat;
    }

    

}
