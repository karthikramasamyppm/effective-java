package delivery.planning.plan;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.Value;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClosingPlanSteps {

    private final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    private final LocalDate date = LocalDate.now(clock);
    private DeliveryEvents events = Mockito.mock(DeliveryEvents.class);
    private DemandForecasting forecasting = Mockito.mock(DemandForecasting.class);

    @When("^plan is closing$")
    public void planIsClosing() throws Throwable {
        PlanCompleteness completeness = new PlanCompleteness(date);
        DeliveryPlan subject = new DeliveryPlan(
                date,
                completeness,
                events,
                forecasting
        );

        subject.close(new ClosePlan(Set.of(), Set.of()));
    }

    @Then("^planning is completed$")
    public void planningIsCompleted() throws Throwable {
        Mockito.verify(events)
                .emit(Mockito.eq(new PlanningCompleted()));
    }

    @Then("^planning is NOT completed$")
    public void planningIsNOTCompleted() throws Throwable {
        Mockito.verify(events, Mockito.never())
                .emit(Mockito.any(PlanningCompleted.class));
    }

    @Then("^there was no need for adjusting demands$")
    public void thereWasNoNeedForAdjustingDemands() throws Throwable {
        Mockito.verifyZeroInteractions(forecasting);
    }

    @Then("^there was no need for reminder for next day$")
    public void thereWasNoNeedForReminderForNextDay() throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Given("^customers demands:$")
    public void customersDemands(List<ProductAmount> demands) throws Throwable {

    }

    @Given("^reminders from previous day:$")
    public void remindersFromPreviousDay(List<Map<String, Object>> reminders) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Given("^amounts delivered according to plan$")
    public void amountsDeliveredAccordingToPlan(List<ProductAmount> planned) throws Throwable {

    }

    @When("^customer decided to adjust demands for: \"([^\"]*)\"$")
    public void customerDecidedToAdjustDemandsFor(String refNos) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @When("^customer decided to deliver missing pieces for next day: \"([^\"]*)\"$")
    public void customerDecidedToDeliverMissingPiecesForNextDay(String refNos) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Then("^demand for \"([^\"]*)\" was adjusted to (\\d+)$")
    public void demandForWasAdjustedTo(String refNo, long amount) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Then("^reminder of (\\d+) for \"([^\"]*)\" was saved$")
    public void reminderOfForWasSaved(long amount, String refNo) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Value
    static class ProductAmount {
        String product;
        long amount;
    }
}
