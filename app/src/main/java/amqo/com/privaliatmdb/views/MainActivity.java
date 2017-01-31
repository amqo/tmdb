package amqo.com.privaliatmdb.views;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.views.popular.MoviesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab)
    FloatingActionButton mToolbarFAB;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;

    private boolean mHideSearch = false;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        MoviesFragment moviesFragment = (MoviesFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (moviesFragment == null) {
            moviesFragment = MoviesFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, moviesFragment);
            transaction.commit();
        }

        MoviesApplication.getInstance().createMainActivityComponent(moviesFragment);

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        MoviesApplication.getInstance().releaseMainActivityComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(!mHideSearch);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        mToolbarFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Use this to make some action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mToolbarFAB.getVisibility() == View.VISIBLE) {
                    mHideSearch = true;
                    mCollapsingToolbar.setTitle("");
                    invalidateOptionsMenu();
                } else if (mToolbarFAB.getVisibility() == View.INVISIBLE) {
                    mHideSearch = false;
                    mCollapsingToolbar.setTitle(getString(R.string.title));
                    invalidateOptionsMenu();
                }
            }
        });
    }
}
