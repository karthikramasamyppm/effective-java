package delivery.planning.plan;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import delivery.planning.ProductRefNo;
import lombok.Value;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClosingPlanSteps {

    private final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    private final LocalDate date = LocalDate.now(clock);
    private DeliveryEvents events = Mockito.mock(DeliveryEvents.class);
    private DemandForecasting forecasting = Mockito.mock(DemandForecasting.class);
    private List<ProductAmount> planned = List.of();
    private List<ProductAmount> demands = List.of();

    ArgumentCaptor<PlanningCompleted> event = ArgumentCaptor.forClass(PlanningCompleted.class);
    ArgumentCaptor<AdjustDemands> outgoingCommand = ArgumentCaptor.forClass(AdjustDemands.class);
    private Set<ProductRefNo> reminderForRefNos = Set.of();
    private Set<ProductRefNo> adjustDemandForRefNos = Set.of();

    @When("^plan is closing$")
    public void planIsClosing() throws Throwable {

        PlanCompleteness completeness = new PlanCompleteness(
                date,
                ProductAmount.toAmounts(planned),
                ProductAmount.toAmounts(demands)
        );
        DeliveryPlan subject = new DeliveryPlan(
                date,
                completeness,
                events,
                forecasting
        );
        subject.close(new ClosePlan(reminderForRefNos, adjustDemandForRefNos));
    }

    @Then("^planning is completed$")
    public void planningIsCompleted() throws Throwable {
        Mockito.verify(events).emit(event.capture());
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
        Assertions.assertThat(event.getValue().getReminder())
                .isEqualTo(ProductAmounts.empty());
    }

    @Then("^reminder of (\\d+) for \"([^\"]*)\" was saved$")
    public void reminderOfForWasSaved(long amount, String refNo) throws Throwable {
        ProductAmounts reminder = event.getValue().getReminder();

        Assertions.assertThat(reminder.get(new ProductRefNo(refNo)))
                .isEqualTo(amount);
    }

    @Given("^customers demands:$")
    public void customersDemands(List<ProductAmount> demands) throws Throwable {
        this.demands = demands;
    }

    @Given("^reminders from previous day:$")
    public void remindersFromPreviousDay(List<Map<String, Object>> reminders) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Given("^amounts delivered according to plan$")
    public void amountsDeliveredAccordingToPlan(List<ProductAmount> planned) throws Throwable {
        this.planned = planned;
    }

    @When("^customer decided to adjust demands for: \"([^\"]*)\"$")
    public void customerDecidedToAdjustDemandsFor(String refNos) throws Throwable {
        adjustDemandForRefNos = Stream.of(refNos.split(", *"))
                .map(ProductRefNo::new)
                .collect(Collectors.toSet());
    }

    @When("^customer decided to deliver missing pieces for next day: \"([^\"]*)\"$")
    public void customerDecidedToDeliverMissingPiecesForNextDay(String refNos) throws Throwable {
        reminderForRefNos = Stream.of(refNos.split(", *"))
                .map(ProductRefNo::new)
                .collect(Collectors.toSet());
    }

    @Then("^demand for \"([^\"]*)\" was adjusted to (\\d+)$")
    public void demandForWasAdjustedTo(String refNo, long amount) throws Throwable {
        Mockito.verify(forecasting).adjustDemand(outgoingCommand.capture());

        Assertions.assertThat(outgoingCommand.getValue().getDate()).isEqualTo(date);
        Assertions.assertThat(outgoingCommand.getValue().getAmounts().get(new ProductRefNo(refNo)))
                .isEqualTo(amount);
    }

    @Value
    static class ProductAmount {
        String product;
        long amount;

        static ProductAmounts toAmounts(List<ProductAmount> amounts) {
            return ProductAmounts.of(amounts.stream()
                    .collect(Collectors.toMap(
                            pro -> new ProductRefNo(pro.getProduct()),
                            ProductAmount::getAmount
                    )));
        }
    }
}
