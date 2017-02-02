package amqo.com.privaliatmdb;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Map;

import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.views.popular.MoviesPresenter;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MoviesActivityPresenterTest {

    @Mock
    private MoviesEndpoint mMoviesEndpointMock;

    @Mock
    private Observable<Movies> mObservableMoviesMock;

    @Mock
    private MoviesContract.View mMoviesViewMock;

    @Mock
    SharedPreferences mSharedPreferencesMock;

    private MoviesPresenter mMoviesActivityPresenter;

    private final int DEFAULT_PAGE = 1;

    @Before
    public void setUp() {
        mMoviesActivityPresenter = new MoviesPresenter(
                mMoviesEndpointMock,
                mMoviesViewMock,
                mSharedPreferencesMock);
    }

    @Test
    public void getMovies_callCorrectMethods() {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mObservableMoviesMock;
            }
        }).when(mMoviesEndpointMock).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(DEFAULT_PAGE);

        verify(mMoviesEndpointMock).getMovies(any(Integer.class), any(Map.class));
        verify(mMoviesViewMock).setLoading(true);
    }

    @Test
    public void getMovies_UsesCorrectParameters() {

        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {

                         assertEquals(invocation.getArguments()[0], MoviesEndpoint.API_VERSION);

                         Map<String, String> parameters = (Map<String, String>) invocation.getArguments()[1];
                         assertEquals(parameters.size(), 4);
                         assertEquals(parameters.get("page"), Integer.toString(DEFAULT_PAGE));
                         assertEquals(parameters.get("sort_by"), "popularity.desc");
                         assertFalse(parameters.get("language").isEmpty());

                         return mObservableMoviesMock;
                     }
                 }
        ).when(mMoviesEndpointMock).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(DEFAULT_PAGE);
    }

    @Test
    public void getMovies_CreatesCorrectPagination() {

        final int secondPage = 2;

        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {

                         Map<String, String> parameters = (Map<String, String>) invocation.getArguments()[1];
                         assertEquals(parameters.get("page"), Integer.toString(secondPage));
                         return mObservableMoviesMock;
                     }
                 }
        ).when(mMoviesEndpointMock).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(secondPage);
    }

}
