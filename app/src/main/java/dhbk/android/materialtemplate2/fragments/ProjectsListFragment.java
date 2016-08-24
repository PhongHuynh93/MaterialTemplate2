package dhbk.android.materialtemplate2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.byoutline.cachedfield.CachedFieldWithArg;
import com.byoutline.cachedfield.FieldState;
import com.byoutline.cachedfield.FieldStateListener;
import com.byoutline.secretsauce.utils.ViewUtils;
import com.software.shell.fab.ActionButton;
import com.squareup.otto.Bus;

import org.parceler.Parcels;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.materialtemplate2.KickMaterialApp;
import dhbk.android.materialtemplate2.R;
import dhbk.android.materialtemplate2.activities.CategoriesListActivity;
import dhbk.android.materialtemplate2.adapters.ProjectClickListener;
import dhbk.android.materialtemplate2.adapters.ProjectsAdapter;
import dhbk.android.materialtemplate2.adapters.SharedViews;
import dhbk.android.materialtemplate2.appjava.model.Category;
import dhbk.android.materialtemplate2.appjava.model.DiscoverResponse;
import dhbk.android.materialtemplate2.managers.LoginManager;
import dhbk.android.materialtemplate2.model.DiscoverQuery;
import dhbk.android.materialtemplate2.utils.LUtils;
import dhbk.android.materialtemplate2.views.EndlessRecyclerView;
import timber.log.Timber;

/**
 * Created by huynhducthanhphong on 8/23/16.
 */
public class ProjectsListFragment extends KickMaterialFragment implements ProjectClickListener, FieldStateListener, EndlessRecyclerView.EndlessScrollListener {
    private static final String PREFS_SHOW_HEADER = "PREFS_SHOW_HEADER";
    private static final String INSTANCE_STATE_SUMMARY_SCROLLED = "INSTANCE_STATE_SUMMARY_SCROLLED";
    private GridLayoutManager layoutManager;

    // the category which user has chosen
    private Category category;

    //    root view after inflate
    private View rootView;

    //     endless recyclerview
    @BindView(R.id.project_recycler_view)
    EndlessRecyclerView projectListRv;
    @BindView(R.id.swipe_refresh_projects_srl)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bubbles_iv)
    ImageView bubblesIv;
    @BindView(R.id.show_categories_fab)
    ActionButton showCategoriesFab;
    @BindView(R.id.main_parent_rl)
    View mainParent;
    @Inject
    Bus bus;
    @Inject
    CachedFieldWithArg<DiscoverResponse, DiscoverQuery> discoverField;
    @Inject
    LoginManager loginManager;
    @Inject
    SharedPreferences sharedPreferences;

    // max scroll
    private float maxScroll;

    //
    private float actionbarScrollPoint;

//    adapter for list
    private ProjectsAdapter adapter;
    private float summaryScrolled;
    private int page = 1;

    /**
     * fixme - get the category and wrap in bundle
     *
     * @param category
     * @return
     */
    public static ProjectsListFragment newInstance(@Nullable Category category) {
        ProjectsListFragment instance = new ProjectsListFragment();
        Bundle args = new Bundle();
        args.putParcelable(CategoriesListActivity.ARG_CATEGORY, Parcels.wrap(category));
        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_projects, container, false);
        KickMaterialApp.component.inject(this);
        ButterKnife.bind(this, rootView);

        // toolbar in activity can hide
        hostActivity.enableActionBarAutoHide(projectListRv);

        // fixme - change dp from resource to pixel
        maxScroll = 2 * getResources().getDimensionPixelSize(R.dimen.project_header_padding_top) + ViewUtils.dpToPx(48, getActivity());
        actionbarScrollPoint = ViewUtils.dpToPx(24, getActivity());

        // get the category from activity
        getArgs();

        setHasOptionsMenu(true);
        return rootView;
    }

    /**
     * when view create, initialize view
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapters();
        setUpListeners();
        configureSwipeRefresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putFloat(INSTANCE_STATE_SUMMARY_SCROLLED, summaryScrolled);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            summaryScrolled = savedInstanceState.getFloat(INSTANCE_STATE_SUMMARY_SCROLLED);
        }
        super.onViewStateRestored(savedInstanceState);
    }


    // restore previous state
    @Override
    public void onResume() {
        super.onResume();
        restoreDefaultScreenLook();
        bus.register(this);
        discoverField.addStateListener(this);
        loadCurrentPage();

        if (category != null) {
            showCategoriesFab.setButtonColor(ContextCompat.getColor(getContext(), category.colorResId));
        }
    }

    @Override
    public void onPause() {
        discoverField.removeStateListener(this);
        bus.unregister(this);
        super.onPause();
    }

    @Override
    public int getLastVisibleItemPosition() {
        return layoutManager.findLastVisibleItemPosition();
    }

    @Override
    public void loadMoreData() {

    }

    @Override
    public boolean hasMoreDataAndNotLoading() {
        return false;
    }

    @Override
    public void fieldStateChanged(FieldState newState) {

    }

    @Override
    public void projectClicked(int position, SharedViews views) {

    }

    /**
     * get the name of the choosen category
     *
     * @return name of the choose category
     */
    @Override
    public String getFragmentActionbarName() {
        if (category != null) {
            return getString(category.nameResId);
        } else {
            return "Projects";
        }
    }

    /**
     * fixme - unwrap the parcel to get the category
     */
    private void getArgs() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(CategoriesListActivity.ARG_CATEGORY)) {
            category = Parcels.unwrap(args.getParcelable(CategoriesListActivity.ARG_CATEGORY));
        } else {
            Timber.e("Category not passed");
        }
    }

    private void setUpAdapters() {
        /** NEW ADAPTER **/
        layoutManager = new GridLayoutManager(getActivity(), 2);

        final boolean showHeader = sharedPreferences.getBoolean(PREFS_SHOW_HEADER, true);
        sharedPreferences.edit().putBoolean(PREFS_SHOW_HEADER, false).apply();

        // FIXME: 8/24/16 set GridLayoutManager when to span image
        final ProjectsAdapter.ItemViewTypeProvider itemViewTypeProvider = new ProjectsAdapter.ItemViewTypeProvider(showHeader);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (itemViewTypeProvider.getViewType(position) == ProjectsAdapter.NORMAL_ITEM) {
                    return 1;
                }
                return 2;
            }
        });

        projectListRv.setEndlessScrollListener(this);
        projectListRv.setLayoutManager(layoutManager);


        adapter = new ProjectsAdapter(getActivity(), this, showHeader, itemViewTypeProvider);
        projectListRv.setAdapter(adapter);
    }

    /**
     * show a different color when swipe from top to bottom
     */
    public void configureSwipeRefresh() {
        int altColor = category == null ? R.color.green_dark : category.colorResId;
        swipeRefreshLayout.setColorSchemeResources(altColor, R.color.green_primary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Throw away all loaded categories and start over.
            int pageToRefresh = 1;
            discoverField.refresh(DiscoverQuery.getDiscoverQuery(category, pageToRefresh));
        });
    }

    private void restoreDefaultScreenLook() {
        hostActivity.showActionbar(true, false);
        LUtils.setStatusBarColor(getActivity(), ContextCompat.getColor(getContext(), R.color.status_bar_color));
    }

    private void loadCurrentPage() {
        final DiscoverQuery query = DiscoverQuery.getDiscoverQuery(category, page);
        discoverField.postValue(query);
//        loginManager.logIn(EmailAndPass.create("g774092@trbvm.com", "g774092@trbvm.com"));
    }

}
