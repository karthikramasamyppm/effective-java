package delivery.planning.plan;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;
import java.util.Map;

public class ClosingPlanSteps {

    @Given("^customers demands:$")
    public void customersDemands(List<Map<String, Object>> demands) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Given("^reminders from previous day:$")
    public void remindersFromPreviousDay(List<Map<String, Object>> reminders) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Given("^amounts delivered according to plan$")
    public void amountsDeliveredAccordingToPlan(List<Map<String, Object>> planned) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @When("^customer decided to adjust demands for: \"([^\"]*)\"$")
    public void customerDecidedToAdjustDemandsFor(String refNos) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @When("^customer decided to deliver missing pieces for next day: \"([^\"]*)\"$")
    public void customerDecidedToDeliverMissingPiecesForNextDay(String refNos) throws Throwable {
        throw new PendingException("step not implemented");
    }

    @When("^plan is closing$")
    public void planIsClosing() throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Then("^planning is completed$")
    public void planningIsCompleted() throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Then("^planning is NOT completed$")
    public void planningIsNOTCompleted() throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Then("^there was no need for adjusting demands$")
    public void thereWasNoNeedForAdjustingDemands() throws Throwable {
        throw new PendingException("step not implemented");
    }

    @Then("^there was no need for reminder for next day$")
    public void thereWasNoNeedForReminderForNextDay() throws Throwable {
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
}
