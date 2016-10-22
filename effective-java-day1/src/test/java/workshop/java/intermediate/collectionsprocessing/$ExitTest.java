package workshop.java.intermediate.collectionsprocessing;

import lombok.*;
import org.junit.jupiter.api.Test;
import workshop.java.intermediate.boilerplatefree.ExampleMovies;
import workshop.java.intermediate.boilerplatefree.Movie;

import java.time.Duration;
import java.util.function.BinaryOperator;

public class $ExitTest {
    public static final BinaryOperator<Aggregator> SINGLE_THREADED_AGGREGATION = (aggregator1, aggregator2) -> {
        throw new UnsupportedOperationException();
    };

    // using ExampleMovies ...

    // Task 1.
    // find titles of movies with French language

    // Task 2.
    // find any movie from Drama genre
    // find any movie from Silent genre

    // Task 3.
    // calculate
    // over all movies duration
    // average imdbRating
    // total number of imdbVotes
    // largest crew number

    @Value
    public static class ImmutableValue {
        private final Duration totalDuration;
        private final double ratingSum;
        private final int ratingCount;
        private final double totalVotes;
        private final int largestCrew;
    }


    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Aggregator {
        public Duration totalDuration = Duration.ZERO;
        public double ratingSum = 0.0;
        public int ratingCount = 0;
        public double totalVotes = 0.0;
        public int largestCrew = 0;
    }

    @Test
    public void aggregations() throws Exception {

        ExampleMovies.allMovies()
                .parallelStream()
                .map(Movie.MovieBuilder::build)
                .map(movie -> new ImmutableValue(
                        movie.getRuntime(),
                        movie.getImdbRating(),
                        1,
                        movie.getImdbVotes(),
                        movie.getActors().size()
                                + movie.getDirector().size()
                                + movie.getWriter().size()
                ));
        // finish it please
    }


    // Task 4.
    // list distinctive all persons from database ordered by name


    // Task 5.
    // group by genre



    // Task 6.
    // calculate average duration by genre

    // Task 7.
    // custom spliterator - sliding window with parametrised window size

}
