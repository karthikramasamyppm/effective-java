package tools;

public interface Strategy {

    Strategy AT_DAY_START = (level, demand, produced) -> level - demand.getLevel();
    Strategy TILL_END_OF_DAY = (level, demand, produced) -> level - demand.getLevel() + produced;

    long calculateLevelOnDelivery(long level, Demands.DailyDemand demand, long produced);

}
