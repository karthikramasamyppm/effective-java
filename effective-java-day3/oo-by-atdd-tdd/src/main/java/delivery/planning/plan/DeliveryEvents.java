package delivery.planning.plan;

public interface DeliveryEvents {
    void emit(PlanningCompleted event);
}
