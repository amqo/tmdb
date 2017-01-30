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
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.network.MoviesEndpoint;
import amqo.com.privaliatmdb.network.MovieParameterCreator;
import amqo.com.privaliatmdb.network.PopularMoviesParametersCreator;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MoviesActivityPresenterTest {

    @Mock
    private MoviesEndpoint mMoviesEndpointInterfaceMock;

    @Mock
    private Observable<Movies> mObservableMoviesMock;

    @Mock
    private MovieParameterCreator mMovieParameterCreatorMock;

    @Mock
    private MoviesContract.View mMoviesViewMock;

    private MovieParameterCreator mMovieParameterCreator;

    private MoviesPresenter mMoviesActivityPresenter;

    private final int DEFAULT_PAGE = 1;

    @Before
    public void setUp() {
        mMovieParameterCreator = new PopularMoviesParametersCreator();
        mMoviesActivityPresenter = new MoviesPresenter(
                mMovieParameterCreator,
                mMoviesEndpointInterfaceMock,
                mMoviesViewMock);
    }

    @Test
    public void getMovies_callCorrectMethods() {

        mMoviesActivityPresenter = new MoviesPresenter(
                mMovieParameterCreatorMock, mMoviesEndpointInterfaceMock, mMoviesViewMock);

        Consumer<Movies> consumer = new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception { }
        };

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mObservableMoviesMock;
            }
        }).when(mMoviesEndpointInterfaceMock).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(DEFAULT_PAGE, consumer);

        verify(mMoviesEndpointInterfaceMock).getMovies(any(Integer.class), any(Map.class));
        verify(mMovieParameterCreatorMock).createParameters(DEFAULT_PAGE);
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

                         return mObservableMoviesMock;
                     }
                 }
        ).when(mMoviesEndpointInterfaceMock).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(DEFAULT_PAGE, new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception { }
        });
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
        ).when(mMoviesEndpointInterfaceMock).getMovies(any(Integer.class), any(Map.class));
        mMoviesActivityPresenter.getMovies(secondPage, new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) throws Exception { }
        });
    }

}
