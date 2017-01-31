package amqo.com.privaliatmdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import amqo.com.privaliatmdb.network.MovieParameterCreator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class MovieParameterCreatorTest {

    @Test
    public void createPopularMoviesParameters_FirstPage() {
        Map<String, String> parameters =
                MovieParameterCreator.createPopularMoviesParameters(1);

        assertEquals(parameters.get("page"), Integer.toString(1));
        checkCommonParameters(parameters);
    }

    @Test
    public void createPopularMoviesParameters_SecondPage() {
        Map<String, String> parameters =
                MovieParameterCreator.createPopularMoviesParameters(2);

        assertEquals(parameters.get("page"), Integer.toString(2));
        checkCommonParameters(parameters);
    }

    @Test
    public void createMoviesConfigurationParameters_Correct() {
        Map<String, String> parameters =
                MovieParameterCreator.createMoviesConfigurationParameters();
        assertEquals(parameters.size(), 1);
        assertEquals(parameters.get("api_key"), PrivateConstants.TMDB_API_KEY);
    }

    private void checkCommonParameters(Map<String, String> parameters) {
        assertEquals(parameters.size(), 4);
        assertEquals(parameters.get("sort_by"), "popularity.desc");
        assertEquals(parameters.get("api_key"), PrivateConstants.TMDB_API_KEY);
        assertFalse(parameters.get("language").isEmpty());
    }
}
