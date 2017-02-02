package amqo.com.privaliatmdb.views.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import amqo.com.privaliatmdb.model.contracts.ConnectivityReceiverContract;
import amqo.com.privaliatmdb.model.contracts.MoviesAdapterContract;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;
import amqo.com.privaliatmdb.model.contracts.MoviesScrollContract;
import amqo.com.privaliatmdb.receivers.ConnectivityNotifier;
import amqo.com.privaliatmdb.views.BaseScrollListener;
import amqo.com.privaliatmdb.views.utils.NotificationsHelper;
import amqo.com.privaliatmdb.views.utils.ScreenHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

import static amqo.com.privaliatmdb.R.id.search;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchMoviesFragment extends Fragment
        implements MoviesContract.ViewSearch,
        MoviesScrollContract.View,
        ConnectivityReceiverContract.View {

    @Inject MoviesContract.PresenterSearch mMoviesPresenter;
    @Inject RecyclerView.LayoutManager mLayoutManager;
    @Inject MoviesAdapterContract.View mMoviesAdapter;

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject BaseScrollListener mScrollListener;
    @Inject SearchQueryListener mSearchQueryListener;
    @Inject ConnectivityNotifier mConnectivityNotifier;

    @BindView(R.id.list_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    private final String CURRENT_SEARCH = "CURRENT_SEARCH";

    private boolean mIsLoading = false;
    private boolean mIsRefreshing = false;
    private boolean mNeedRefresh = false;

    private SearchView mSearchView;
    private String mCurrentSearchTerm;
    private Snackbar mConnectivitySnackbar;

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

        boolean connected = mConnectivityNotifier.isConnected();
        if(!TextUtils.isEmpty(mCurrentSearchTerm) && connected) {
            mMoviesPresenter.searchMovies(1, mCurrentSearchTerm);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mNeedRefresh)
            mConnectivitySnackbar = NotificationsHelper
                    .showSnackConnectivity(getContext(), getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean connected = mConnectivityNotifier.isConnected();
        onNetworkConnectionChanged(connected);
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
        if (!mConnectivityNotifier.isConnected()) return;
        if (TextUtils.isEmpty(mCurrentSearchTerm)) {
            mMoviesAdapter.refreshMovies(new Movies());
            return;
        }
        setLoading(true);
        mIsRefreshing = true;
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
        mSearchView.clearFocus();
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
        if (!mConnectivityNotifier.isConnected()) return;
        int lastPageLoaded = mMoviesAdapter.getLastPageLoaded();
        mMoviesPresenter.searchMovies(lastPageLoaded + 1, mCurrentSearchTerm);
    }

    @Override
    public boolean isInLastPage() {
        return mMoviesAdapter.isInLastPage();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            mNeedRefresh = true;
            mConnectivitySnackbar = NotificationsHelper
                    .showSnackConnectivity(getContext(), getView());
        } else {
            if (mConnectivitySnackbar != null) {
                mConnectivitySnackbar.dismiss();
                mConnectivitySnackbar = null;
            }
            if (mNeedRefresh) {
                mNeedRefresh = false;
                refreshMovies();
            }
        }
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
                        if (!mConnectivityNotifier.isConnected())
                            mSwipeRefreshLayout.setRefreshing(false);
                        else refreshMovies();
                    }
                });
    }
}
