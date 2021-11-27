import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Part1BreakfastProxy implements Part1LeInterface {
    private List<DayOfWeek> allowedDays = new ArrayList<>(
            Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
    );
    private int earliestHour = 9; //9am
    private int latestHour = 21; //9pm
    private Part1Breakfast breakfast = new Part1Breakfast();

    @Override
    public void makePancakes() {
        if (isValid()) {
            breakfast.makePancakes();
        }
    }

    @Override
    public void cookBacon() {
        if (isValid()) {
            breakfast.cookBacon();
        }
    }

    @Override
    public void toastBread() {
        if (isValid()) {
            breakfast.toastBread();
        }
    }

    public Boolean isValid() {
        LocalDateTime ldt = LocalDateTime.now();
        if (allowedDays.contains(ldt.getDayOfWeek()) &&
                ldt.getHour() >= earliestHour &&
                ldt.getHour() <= latestHour) {
            return true;
        }
        throw new RuntimeException("You know that I have the day off, Master Wayne.");
    }
}
