package amqo.com.privaliatmdb.views.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import amqo.com.privaliatmdb.MoviesApplication;
import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.Movie;
import amqo.com.privaliatmdb.model.Movies;
import amqo.com.privaliatmdb.model.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.MoviesContract;
import amqo.com.privaliatmdb.model.MoviesScrollContract;
import amqo.com.privaliatmdb.views.BaseScrollListener;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

import static amqo.com.privaliatmdb.R.id.search;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchMoviesFragment extends Fragment implements MoviesContract.ViewSearch, MoviesScrollContract.View {

    @Inject MoviesContract.PresenterSearch mMoviesPresenter;
    @Inject RecyclerView.LayoutManager mLayoutManager;
    @Inject MoviesAdapterContract.View mMoviesAdapter;

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject BaseScrollListener mScrollListener;
    @Inject SearchQueryListener mSearchQueryListener;

    @BindView(R.id.list_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    private final String CURRENT_SEARCH = "CURRENT_SEARCH";

    private boolean mIsLoading = false;
    private boolean mIsRefreshing = false;

    private SearchView mSearchView;
    private String mCurrentSearchTerm;

    public static SearchMoviesFragment newInstance() {
        return new SearchMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_SEARCH)) {
            mCurrentSearchTerm = savedInstanceState.getString(CURRENT_SEARCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        MoviesApplication.getInstance().getSearchMoviesComponent().inject(this);

        initRecyclerView();

        initOtherViews();

        if(!TextUtils.isEmpty(mCurrentSearchTerm)) {
            mMoviesPresenter.searchMovies(1, mCurrentSearchTerm);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_SEARCH, mCurrentSearchTerm);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(search);

        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(
                getActivity().getComponentName()));

        searchItem.expandActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        if(!TextUtils.isEmpty(mCurrentSearchTerm)) {
            mSearchView.setQuery(mCurrentSearchTerm, false);
        }

        mSearchView.setOnQueryTextListener(mSearchQueryListener);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getActivity().finish();
                return false;
            }
        });
    }

    @Override
    public void refreshMovies() {
        setLoading(true);
        mIsRefreshing = true;
        if (TextUtils.isEmpty(mCurrentSearchTerm)) {
            mMoviesAdapter.refreshMovies(new Movies());
            return;
        }
        mMoviesPresenter.searchMovies(1, mCurrentSearchTerm);
    }

    @Override
    public void refreshMovies(String query) {
        mCurrentSearchTerm = query;
        refreshMovies();
    }

    @Override
    public void setLoading(boolean loading) {
        mIsLoading = loading;
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(mIsLoading);
            }
        });

    }

    @Override
    public void onMovieInteraction(Movie movie) {

    }

    @Override
    public int getScreenDensity() {
        return ScreenHelper.getScreenDensity(getActivity());
    }

    @Override
    public void onMoviesLoaded(Movies movies) {
        if (mIsRefreshing) {
            mIsRefreshing = false;
            mMoviesAdapter.refreshMovies(movies);
        } else mMoviesAdapter.addMovies(movies);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void loadMoreMovies() {
        int lastPageLoaded = mMoviesAdapter.getLastPageLoaded();
        mMoviesPresenter.searchMovies(lastPageLoaded + 1, mCurrentSearchTerm);
    }

    @Override
    public boolean isInLastPage() {
        return mMoviesAdapter.isInLastPage();
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRecyclerView.setAdapter((RecyclerView.Adapter) mMoviesAdapter);
    }

    private void initOtherViews() {

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshMovies();
                    }
                });
    }
}
