package MyPlayer;

import battlecode.common.*;
import java.util.*;


interface NavSafetyPolicy {
    public boolean isSafeToMoveTo(MapLocation loc) throws GameActionException;
}

class SafetyPolicyAvoidAllUnits extends Bot implements NavSafetyPolicy {

    public SafetyPolicyAvoidAllUnits() throws GameActionException {
        super();
    }

    public boolean isSafeToMoveTo(MapLocation loc) throws GameActionException {
        // TODO: Rubble threshold value need to be determined (current : 80)
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

    private static MapLocation dest;
    private static NavSafetyPolicy safety;

    private enum BugState {
        DIRECT, BUG
    }

    public enum WallSide {
        LEFT, RIGHT
    }

    private static BugState bugState;
    public static WallSide bugWallSide = null;
    private static int bugStartDistSq;
    private static Direction bugLastMoveDir;
    private static Direction bugLookStartDir;
    private static int bugRotationCount;
    private static int bugMovesSinceSeenObstacle = 0;
    private static int foobar = 0;

    private static boolean move(Direction dir) throws GameActionException {
        rc.move(dir);
        return true;
    }

    private static boolean canMove(Direction dir) throws GameActionException {
        return rc.canMove(dir) && safety.isSafeToMoveTo(here.add(dir));
    }

    // Determines whether can move in the direct way
    private static boolean tryMoveDirect() throws GameActionException {
        Direction toDest = here.directionTo(dest);

        if (canMove(toDest)) {
            move(toDest);
            return true;
        }

        // Possible turning directions
        Direction[] dirs = new Direction[2];
        Direction dirLeft = toDest.rotateLeft();
        Direction dirRight = toDest.rotateRight();
        if (here.add(dirLeft).distanceSquaredTo(dest) < here.add(dirRight).distanceSquaredTo(dest)) {
            dirs[0] = dirLeft;
            dirs[1] = dirRight;
        } else {
            dirs[0] = dirRight;
            dirs[1] = dirLeft;
        }

        for (Direction dir : dirs) {
            if (canMove(dir)) {
                move(dir);
                return true;
            }
        }
        return false;
    }

    private static void startBug() throws GameActionException {
        bugStartDistSq = here.distanceSquaredTo(dest);
        bugLastMoveDir = here.directionTo(dest);
        bugLookStartDir = here.directionTo(dest);
        bugRotationCount = 0;
        bugMovesSinceSeenObstacle = 0;
        if (bugWallSide == null) {
            // try to intelligently choose on which side we will keep the wall
            Direction leftTryDir = bugLastMoveDir.rotateLeft();
            for (int i = 0; i < 3; i++) {
                if (!canMove(leftTryDir))
                    leftTryDir = leftTryDir.rotateLeft();
                else
                    break;
            }
            Direction rightTryDir = bugLastMoveDir.rotateRight();
            for (int i = 0; i < 3; i++) {
                if (!canMove(rightTryDir))
                    rightTryDir = rightTryDir.rotateRight();
                else
                    break;
            }
            if (dest.distanceSquaredTo(here.add(leftTryDir)) < dest.distanceSquaredTo(here.add(rightTryDir))) {
                bugWallSide = WallSide.RIGHT;
            } else {
                bugWallSide = WallSide.LEFT;
            }
        }
    }

    private static Direction findBugMoveDir() throws GameActionException {
        bugMovesSinceSeenObstacle++;
        Direction dir = bugLookStartDir;
        for (int i = 8; i-- > 0;) {
            if (canMove(dir))
                return dir;
            dir = (bugWallSide == WallSide.LEFT ? dir.rotateRight() : dir.rotateLeft());
            bugMovesSinceSeenObstacle = 0;
        }
        return null;
    }

    private static int numRightRotations(Direction start, Direction end) {
        return (end.ordinal() - start.ordinal() + 8) % 8;
    }

    private static int numLeftRotations(Direction start, Direction end) {
        return (-end.ordinal() + start.ordinal() + 8) % 8;
    }

    private static int calculateBugRotation(Direction moveDir) {
        if (bugWallSide == WallSide.LEFT) {
            return numRightRotations(bugLookStartDir, moveDir) - numRightRotations(bugLookStartDir, bugLastMoveDir);
        } else {
            return numLeftRotations(bugLookStartDir, moveDir) - numLeftRotations(bugLookStartDir, bugLastMoveDir);
        }
    }

    private static void bugMove(Direction dir) throws GameActionException {
        if (move(dir)) {
            bugRotationCount += calculateBugRotation(dir);
            bugLastMoveDir = dir;
            if (bugWallSide == WallSide.LEFT)
                bugLookStartDir = dir.rotateLeft().rotateLeft();
            else
                bugLookStartDir = dir.rotateRight().rotateRight();
        }
    }

    private static boolean detectBugIntoEdge() throws GameActionException {
        if (bugWallSide == WallSide.LEFT) {
            return !rc.onTheMap(here.add(bugLastMoveDir.rotateLeft()));
        } else {
            return !rc.onTheMap(here.add(bugLastMoveDir.rotateRight()));
        }
    }

    private static void reverseBugWallFollowDir() throws GameActionException {
        bugWallSide = (bugWallSide == WallSide.LEFT ? WallSide.RIGHT : WallSide.LEFT);
        startBug();
    }

    private static void bugTurn() throws GameActionException {
        if (detectBugIntoEdge()) {
            reverseBugWallFollowDir();
        }
        Direction dir = findBugMoveDir();
        if (dir != null) {
            bugMove(dir);
        }
    }

    private static boolean canEndBug() {
        if (bugMovesSinceSeenObstacle >= 4)
            return true;
        return (bugRotationCount <= 0 || bugRotationCount >= 8) && here.distanceSquaredTo(dest) <= bugStartDistSq;
    }

    private static void bugMove() throws GameActionException {
        // Check if we can stop bugging at the *beginning* of the turn
        if (bugState == BugState.BUG) {
            if (canEndBug()) {
                bugState = BugState.DIRECT;
            }
        }

        // If DIRECT mode, try to go directly to target
        if (bugState == BugState.DIRECT) {
            if (!tryMoveDirect()) {
                bugState = BugState.BUG;
                startBug();
            }
        }
        // If that failed, or if bugging, bug
        if (bugState == BugState.BUG) {
            bugTurn();
        }
    }


    static class Tuple implements Comparable<Tuple>{
        MapLocation loc;
        Integer priority;

        public Tuple(MapLocation loc, Integer priority){
            this.loc = loc;
            this.priority = priority;
        }

        public MapLocation getLoc(){
            return this.loc;
        }

        // public Integer getPriority(){
        //     return this.priority;
        // }

        @Override
        public int compareTo(Tuple tup) {
            return this.priority - tup.priority;
        }
    }


//    // TODO: Move to specific given destination
//    public static void moveTo(MapLocation destination) throws  GameActionException{
//        Direction[] directions = {
//                Direction.NORTH,
//                Direction.NORTHEAST,
//                Direction.EAST,
//                Direction.SOUTHEAST,
//                Direction.SOUTH,
//                Direction.SOUTHWEST,
//                Direction.WEST,
//                Direction.NORTHWEST,
//        };
//        MapLocation curLoc = rc.getLocation();
//
//        if(!rc.isMovementReady()){
//            // Actions needed when rc can't move
//        }
//        else if(curLoc.isAdjacentTo(destination)){
//            rc.move(curLoc.directionTo(destination));
//        }
//        else{
//            PriorityQueue<Tuple> pQueue = new PriorityQueue<Tuple>();
//
//            pQueue.add(new Tuple(curLoc, rc.senseRubble(curLoc)));
//            Hashtable<MapLocation, Integer> visited = new Hashtable<MapLocation, Integer>();
//            Hashtable<MapLocation, MapLocation> path = new Hashtable<>();
//            ArrayList<Direction> allDirections = new ArrayList<Direction>();
//            //Store all the directions needed to reach the destination
//            visited.put(curLoc, rc.senseRubble(curLoc));
//            path.put(null, curLoc);
//
//            while (!pQueue.isEmpty()){
//                MapLocation current = pQueue.poll().getLoc();
//
//                if(current.equals(destination)){
//                    break;
//                }
//
//                for(Direction direction : directions){
//                    MapLocation adjLoc = current.add(direction);
//                    Integer new_cost = rc.senseRubble(adjLoc) + visited.get(current);
//                    if(!visited.containsKey(adjLoc)){
//                        visited.put(adjLoc, new_cost);
//                        Integer priority = new_cost + adjLoc.distanceSquaredTo(destination);
//                        pQueue.add(new Tuple(adjLoc, priority));
//                        path.put(current, adjLoc);
//                        allDirections.add(direction);
//                    }
//                }
//
//            }
//            rc.move(allDirections.get(1));
//        }
//    }
private static class Edge implements Comparable<Edge>{
    private final MapLocation from;
    private final MapLocation to;
    private  final int weight;
    private  int priority = 10000;

    public Edge(MapLocation f, MapLocation t, int w) {
        from = f;
        to = t;
        weight = w;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public MapLocation getFrom() {
        return from;
    }
    public MapLocation getTo() {
        return to;
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return this.to.equals(edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to);
    }


    @Override
    public int compareTo(Edge o) {
        if (this == o) return 0;
        return Integer.compare(this.priority, o.priority);
    }

}
    //heuristic function
    static int heuristic(MapLocation start, MapLocation destination) {
        int dx = Math.abs(start.x - destination.x);
        int dy = Math.abs(start.y - destination.y);
        return 2*(dx + dy) - Math.min(dx, dy);
    }

    // A* moveTo
    static void moveTo(RobotController rc, MapLocation destination) throws GameActionException {
        PriorityQueue<Edge> open = new PriorityQueue<>();
        PriorityQueue<Edge> closed = new PriorityQueue<>();
        MapLocation start = rc.getLocation();
        Edge startEdge = new Edge(start, start, 0);
        if (start.equals(destination)) return;
        closed.add(startEdge);
        double c = 0.1;
        int coolDown = rc.getMovementCooldownTurns();
        System.out.println(coolDown);
        for (Direction d : directions) {
            MapLocation adj = start.add(d);
            if(!rc.onTheMap(adj) || rc.canSenseRobotAtLocation(adj)) {
                continue;
            }
            int weight = (int) Math.round(c * rc.senseRubble(adj));
            Edge edge = new Edge(adj, adj, weight);
            edge.setPriority(edge.getWeight() + heuristic(adj, destination));
            open.add(edge);
            if (coolDown < 30) {
                closed.add(edge);
            }
        }

        if (coolDown > 30) {
            int r = Math.min(5 + coolDown/10, rc.getType().visionRadiusSquared / 3);
            while (!open.isEmpty()) {
                Edge edge1 = open.poll();
                closed.add(edge1);
                MapLocation midPoint = edge1.getTo();
                for (Direction d : directions) {
                    MapLocation adj = midPoint.add(d);
                    if (!start.isWithinDistanceSquared(adj, r) || rc.canSenseRobotAtLocation(adj) || !rc.onTheMap(adj)) {
                        continue;
                    }
                    int weight = (int) Math.round(c * rc.senseRubble(adj));
                    Edge edge = new Edge(edge1.getFrom(), adj, edge1.getWeight() + weight);
                    if (!open.contains(edge) && !closed.contains(edge)) {
                        edge.setPriority(edge.getWeight() + heuristic(adj, destination));
                        open.add(edge);
                    }
                }
            }
        }

        Edge edge = closed.poll();
        Direction direction = start.directionTo(edge.from);
        if (rc.canMove(direction)) {
            rc.move(direction);
        }
    }


    public static boolean goTo(MapLocation theDest, NavSafetyPolicy theSafety) throws GameActionException {
        if (!theDest.equals(dest)) {
            dest = theDest;
            bugState = BugState.DIRECT;
        }

        if (here.equals(dest))
            return false;

        safety = theSafety;

        bugMove();
        return true;
    }

    // TODO: Exploring map strategy
    public static void explore() throws GameActionException{
        if(lastExploreDir == null) {
            lastExploreDir = randomDirection();
            foobar = 0;
        }
        if(foobar >= Constants.EXPLORE_FOOBAR) {
            foobar = 0;
            lastExploreDir = (new Direction[] {
                    lastExploreDir.rotateLeft().rotateLeft(),
                    lastExploreDir.rotateLeft(),
                    lastExploreDir,
                    lastExploreDir.rotateRight(),
                    lastExploreDir.rotateRight().rotateRight()})[rand.nextInt(5)];
        }
        foobar++;

        if(!rc.onTheMap(here.add(lastExploreDir))) {
            lastExploreDir = lastExploreDir.opposite();
        }

        if (canMove(lastExploreDir)) {
            move(lastExploreDir);
            return;
        }

        Direction[] dirs = new Direction[4];
        Direction dirLeft = lastExploreDir.rotateLeft();
        Direction dirRight = lastExploreDir.rotateRight();
        Direction dirLeftLeft = dirLeft.rotateLeft();
        Direction dirRightRight = dirRight.rotateRight();
        dirs[0] = dirLeft;
        dirs[1] = dirRight;
        dirs[2] = dirLeftLeft;
        dirs[3] = dirRightRight;
        for (Direction dir : dirs) {
            if (canMove(dir)) {
                move(dir);
                return;
            }
        }
        return;
    }
}

