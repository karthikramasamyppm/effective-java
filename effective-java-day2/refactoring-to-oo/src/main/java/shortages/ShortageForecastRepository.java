package shortages;

public interface ShortageForecastRepository {
    ShortageForecast get(String productRefNo, int daysAhead);
}
