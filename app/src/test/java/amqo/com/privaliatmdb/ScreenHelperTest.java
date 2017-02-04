package amqo.com.privaliatmdb;

import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import amqo.com.privaliatmdb.model.MoviesConfiguration;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScreenHelperTest {

    private final int DEFAULT_DENSITY = 100;

    @Mock Activity mActivityMock;
    @Mock WindowManager mWindowManagerMock;
    @Mock Display mDisplayMock;

    @Mock MoviesConfiguration mMoviesConfigurationMock;

    ScreenHelper mScreenHelper;
    ScreenHelper mScreenHelperSpy;
    MoviesConfiguration mMoviesConfiguration;

    @Before
    public void setUp() {
        mMoviesConfiguration = new MoviesConfiguration();
        mMoviesConfiguration.setSizes(getSizes());

        mScreenHelper = new ScreenHelper(mActivityMock);
        mScreenHelperSpy = spy(mScreenHelper);
    }

    @Test
    public void getCorrectImageSize_callVerify() {

        when(mActivityMock.getWindowManager()).thenReturn(mWindowManagerMock);
        when(mWindowManagerMock.getDefaultDisplay()).thenReturn(mDisplayMock);

        mScreenHelper.getCorrectImageSize(mMoviesConfigurationMock);

        verify(mMoviesConfigurationMock).getSizes();
        verify(mActivityMock).getWindowManager();
        verify(mWindowManagerMock).getDefaultDisplay();
    }

    @Test
    public void getCorrectImageSize_getFirstSize() {

        doReturn(DEFAULT_DENSITY).when(mScreenHelperSpy).getScreenDensity();
        String size = mScreenHelperSpy.getCorrectImageSize(mMoviesConfiguration);
        assertThat(size, is(getSizes().get(0)));
    }

    @Test
    public void getCorrectImageSize_getSecondSize() {

        doReturn(200).when(mScreenHelperSpy).getScreenDensity();
        String size = mScreenHelperSpy.getCorrectImageSize(mMoviesConfiguration);
        assertThat(size, is(getSizes().get(1)));
    }

    @Test
    public void getCorrectImageSize_getLastSize() {

        doReturn(1300).when(mScreenHelperSpy).getScreenDensity();
        String size = mScreenHelperSpy.getCorrectImageSize(mMoviesConfiguration);
        List<String> sizes = getSizes();
        assertThat(size, is(getSizes().get(sizes.size() - 1)));
    }

    @Test
    public void getCorrectImageSize_getFistWhenDifferent() {

        doReturn(1300).when(mScreenHelperSpy).getScreenDensity();
        List<String> sizes = getDifferentSizes();
        mMoviesConfiguration.setSizes(sizes);

        String size = mScreenHelperSpy.getCorrectImageSize(mMoviesConfiguration);
        assertThat(size, is(getSizes().get(sizes.size() - 1)));
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
