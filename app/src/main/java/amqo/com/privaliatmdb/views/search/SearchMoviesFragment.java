package amqo.com.privaliatmdb.views.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
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
import amqo.com.privaliatmdb.model.contracts.MoviesContract.PresenterSearch;
import amqo.com.privaliatmdb.views.BaseMoviesFragment;
import amqo.com.privaliatmdb.views.BaseScrollListener;

import static amqo.com.privaliatmdb.R.id.search;

public class SearchMoviesFragment extends BaseMoviesFragment {

    // Here the injection is for the implementation of the Contracts
    // This is to make constructor injection work
    @Inject BaseScrollListener mScrollListener;
    @Inject SearchQueryListener mSearchQueryListener;

    private final String CURRENT_SEARCH = "CURRENT_SEARCH";

    private SearchView mSearchView;
    private String mSavedSearchTerm;

    public static SearchMoviesFragment newInstance() {
        return new SearchMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_SEARCH)) {
            mSavedSearchTerm = savedInstanceState.getString(CURRENT_SEARCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        super.bindViews(view);

        MoviesApplication.getInstance().getActiveComponent().inject(this);

        mRecyclerView.addOnScrollListener(mScrollListener);

        refreshMovies();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_SEARCH, mSearchView.getQuery().toString());
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
        if(!TextUtils.isEmpty(mSavedSearchTerm)) {
            mSearchView.setQuery(mSavedSearchTerm, false);
            ((PresenterSearch)mMoviesPresenter).setNewQuery(mSavedSearchTerm);
            refreshMovies();
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

    // Parent abstract methods

    @Override
    protected void movieInteraction(Movie movie) {

        mSearchView.clearFocus();
    }

    @Override
    protected void refreshMovies() {

        if(mConnectivityNotifier.isConnected()) {
            mMoviesPresenter.refreshMovies();
        }
    }
}
