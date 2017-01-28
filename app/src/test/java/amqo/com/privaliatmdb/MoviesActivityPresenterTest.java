package amqo.com.privaliatmdb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Map;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.network.IMoviesController.IMoviesListener;
import amqo.com.privaliatmdb.network.IMoviesEndpoint;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.PopularMoviesParametersCreator;
import retrofit2.Call;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MoviesActivityPresenterTest {

    private MoviesActivityPresenter mMoviesActivityPresenter;

    @Mock
    private IMoviesEndpoint mMoviesEndpointInterface;

    @Mock
    private Call<Movies> mCallMovies;

    private MovieParameterCreator mMovieParameterCreator;

    private final int DEFAULT_PAGE = 1;

    @Before
    public void setUp() {
        mMovieParameterCreator = new PopularMoviesParametersCreator();
        mMoviesActivityPresenter = new MoviesActivityPresenter(
                mMovieParameterCreator, mMoviesEndpointInterface);
    }

    @Test
    public void verifyMovieCalls() {

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return mCallMovies;
                    }
                }
        ).when(mMoviesEndpointInterface).getMovies(any(Integer.class), any(Map.class));

        mMoviesActivityPresenter.getMovies(DEFAULT_PAGE, new IMoviesListener() {
            @Override
            public void onMoviesLoaded(Movies movies) {}
        });

        verify(mCallMovies).enqueue(mMoviesActivityPresenter);
        verify(mMoviesEndpointInterface).getMovies(any(Integer.class), any(Map.class));


    }

    @Test
    public void verifyMovieCallParameters() {

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {

                        assertEquals(invocation.getArguments()[0], IMoviesEndpoint.API_VERSION);

                        Map<String, String> parameters = (Map<String, String>) invocation.getArguments()[1];
                        assertEquals(parameters.size(), 4);
                        assertEquals(parameters.get("page"), Integer.toString(DEFAULT_PAGE));
                        assertEquals(parameters.get("sort_by"), "popularity.desc");

                        return mCallMovies;
                    }
                }
        ).when(mMoviesEndpointInterface).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(DEFAULT_PAGE, new IMoviesListener() {
            @Override
            public void onMoviesLoaded(Movies movies) { }
        });
    }

    @Test
    public void verifyMovieCallPagination() {

        final int secondPage = 2;

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {

                        Map<String, String> parameters = (Map<String, String>) invocation.getArguments()[1];
                        assertEquals(parameters.get("page"), Integer.toString(secondPage));
                        return mCallMovies;
                    }
                }
        ).when(mMoviesEndpointInterface).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(secondPage, new IMoviesListener() {
            @Override
            public void onMoviesLoaded(Movies movies) { }
        });
    }

}
