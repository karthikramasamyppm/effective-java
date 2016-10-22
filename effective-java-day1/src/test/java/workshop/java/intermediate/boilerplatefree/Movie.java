package workshop.java.intermediate.boilerplatefree;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.Builder;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.EnumSet;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode(of = "imdbID")
public class Movie {

    private final String imdbID;
    private final String title;
    private final Year year;
    private final String rated;
    private final LocalDate released;
    private final Duration runtime;
    private final EnumSet<Genre> genre;
    private final List<Person> director;
    private final List<Person> writer;
    private final List<Person> actors;
    private final String plot;
    private final String language;
    private final String country;
    private final String awards;
    private final URI poster;
    private final Double metascore;
    private final Double imdbRating;
    private final Double imdbVotes;

    public enum Genre {
        Action,
        Biography,
        Animated,
        Adventure,
        ChickFlicks,
        Comedy,
        Detective,
        Mystery,
        Children,
        Animation,
        Family,
        Gangster,
        Disaster,
        Classic,
        Drama,
        Fantasy,
        Cult,
        Epics,
        History,
        FilmNoir,
        Documentary,
        Horror,
        Guy,
        Serial,
        Musicals,
        Dance,
        Melodramas,
        Sexual,
        Erotic,
        ScienceFiction,
        Road,
        Silent,
        WarAntiWar,
        Romance,
        Westerns,
        Sports,
        Supernatural,
        Thriller
    }
}
