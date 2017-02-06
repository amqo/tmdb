package amqo.com.privaliatmdb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MovieParameterCreatorTest.class,
        MoviesActivityPresenterTest.class,
        ScreenHelperTest.class
})
public class unitTestSuite { }
