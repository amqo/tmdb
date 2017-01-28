package amqo.com.privaliatmdb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.network.IMoviesController;
import amqo.com.privaliatmdb.network.IMoviesController.IMoviesListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @Inject
    IMoviesController mMoviesController;

    @BindView(R.id.fab)
    FloatingActionButton mToolbarFAB;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParentApplication.getInstance().getMainActivityComponent(this).inject(this);

        mUnbinder = ButterKnife.bind(this);

        mToolbarFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = 1;
                Snackbar.make(view, "Receiving popular movies in page " + page, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mMoviesController.getMovies(page,
                        new IMoviesListener() {
                            @Override
                            public void onMoviesLoaded(Movies movies) {
                                List<Movie> resultMovies = movies.getMovies();
                                for (Movie movie : resultMovies) {
                                    Log.i("", "");
                                }
                            }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        ParentApplication.getInstance().releaseMainActivityComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
