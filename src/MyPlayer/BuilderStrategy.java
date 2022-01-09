package MyPlayer;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class BuilderStrategy {
    static Bot bot;
    static RobotController rc;
    static int[] buildingIndices = {
            RobotType.LABORATORY.ordinal(),
            RobotType.WATCHTOWER.ordinal(),
            RobotType.ARCHON.ordinal()
    };
    static int[] leadPriorities;

    public BuilderStrategy(Bot bot) {
        rc = bot.rc;
        // represents the min soup we would need to build. lower is higher priority
        leadPriorities = new int[]{
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
        };
    }

    public static RobotType determineBuildingNeeded() {
        updatePriorities(bot.unitCounts);
        RobotType ret = null;
        int minPri = rc.getTeamLeadAmount(rc.getTeam()) + 1;
        for(int i=0; i<buildingIndices.length; i++){
            int idx = buildingIndices[i];
            if(leadPriorities[idx] < minPri){
                minPri = leadPriorities[idx];
                ret = RobotType.values()[idx];
            }
        }
        return ret;
    }

    public boolean shouldBuildUnit(RobotType rt) {
        updatePriorities(bot.unitCounts);
        return leadPriorities[rt.ordinal()] <= rc.getTeamLeadAmount(rc.getTeam());
    }

    public static void updatePriorities(int[] unitCounts) {
        return;
    }
}
