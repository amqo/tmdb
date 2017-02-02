package amqo.com.privaliatmdb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScreenHelperTest {

    private final int DEFAULT_DENSITY = 100;

    @Mock
    MoviesContract.View mViewMock;
    @Mock
    MoviesConfiguration mMoviesConfigurationMock;

    MoviesConfiguration mMoviesConfiguration;

    @Before
    public void setUp() {
        mMoviesConfiguration = new MoviesConfiguration();
        mMoviesConfiguration.setSizes(getSizes());
    }

    @Test
    public void getCorrectImageSize_callVerify() {
        when(mViewMock.getScreenDensity()).thenReturn(DEFAULT_DENSITY);
        ScreenHelper.getCorrectImageSize(mViewMock, mMoviesConfigurationMock);
        verify(mViewMock).getScreenDensity();
        verify(mMoviesConfigurationMock).getSizes();
    }

    @Test
    public void getCorrectImageSize_getFirstSize() {

        when(mViewMock.getScreenDensity()).thenReturn(DEFAULT_DENSITY);
        String size = ScreenHelper.getCorrectImageSize(mViewMock, mMoviesConfiguration);
        assertThat(size, is(getSizes().get(0)));
    }

    @Test
    public void getCorrectImageSize_getSecondSize() {

        when(mViewMock.getScreenDensity()).thenReturn(200);
        String size = ScreenHelper.getCorrectImageSize(mViewMock, mMoviesConfiguration);
        assertThat(size, is(getSizes().get(1)));
    }

    @Test
    public void getCorrectImageSize_getLastSize() {

        when(mViewMock.getScreenDensity()).thenReturn(1300);
        String size = ScreenHelper.getCorrectImageSize(mViewMock, mMoviesConfiguration);
        List<String> sizes = getSizes();
        assertThat(size, is(sizes.get(sizes.size() - 1)));
    }

    @Test
    public void getCorrectImageSize_getFistWhenDifferent() {

        when(mViewMock.getScreenDensity()).thenReturn(1300);
        List<String> sizes = getDifferentSizes();
        mMoviesConfiguration.setSizes(sizes);

        String size = ScreenHelper.getCorrectImageSize(mViewMock, mMoviesConfiguration);
        assertThat(size, is(sizes.get(0)));
    }

    private List<String> getSizes() {
        List<String> sizes = new ArrayList<>();
        sizes.add("w92");
        sizes.add("w154");
        sizes.add("w185");
        sizes.add("w342");
        sizes.add("w500");
        sizes.add("w780");
        sizes.add("original");
        return sizes;
    }

    private List<String> getDifferentSizes() {
        List<String> sizes = new ArrayList<>();
        sizes.add("aa");
        sizes.add("bb");
        sizes.add("cc");
        sizes.add("dd");
        sizes.add("ee");
        sizes.add("ff");
        sizes.add("original");
        return sizes;
    }
}
