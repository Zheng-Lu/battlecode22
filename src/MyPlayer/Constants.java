package MyPlayer;

import battlecode.common.RobotType;

public class Constants {
    public static int FAST_SECRET_NUM = 194;
    public static int SLOW_SECRET_NUM = 155252936;
    public static int BUG_PATIENCE = 20; //how many turns before we give up bugging
    public static int MAX_CLUSTER_DIST = RobotType.MINER.actionRadiusSquared;
    public static int GIVE_UP_CLUSTER_DIST = 2;
    public static int EXPLORE_FOOBAR = 25;
}
