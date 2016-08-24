package dhbk.android.materialtemplate2.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.byoutline.secretsauce.activities.BaseAppCompatActivity;
import com.byoutline.secretsauce.fragments.MenuOption;
import com.byoutline.secretsauce.fragments.NavigationDrawerFragment;
import com.byoutline.secretsauce.utils.ViewUtils;

import dhbk.android.materialtemplate2.R;
import dhbk.android.materialtemplate2.fragments.KickMaterialFragment;


/**
 * Created by huynhducthanhphong on 8/23/16.
 * inject toolbar, show or hide toolbar when scroll
 */
public abstract class KickMaterialBaseActivity extends BaseAppCompatActivity implements KickMaterialFragment.HostActivity, NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final long HEADER_HIDE_ANIM_DURATION = 300;
    private int actionBarAutoHideMinY = 0;
    private int actionBarAutoHideSensitivity = 0;
    private boolean actionBarShown = true;
    private int actionBarAutoHideSignal = 0;

    /**
     * todo show or hide action bar depend on scroll
     * @param listView
     */
    @Override
    public void enableActionBarAutoHide(RecyclerView listView) {
        initActionBarAutoHide();
        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            final static int ITEMS_THRESHOLD = 1;
            int lastFvi = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                onMainContentScrolled(firstVisibleItem <= ITEMS_THRESHOLD ? 0 : Integer.MAX_VALUE,
                        lastFvi - firstVisibleItem > 0 ? Integer.MIN_VALUE :
                                lastFvi == firstVisibleItem ? 0 : Integer.MAX_VALUE
                );
                lastFvi = firstVisibleItem;
            }
        });
    }

    @Override
    public void showActionbar(boolean show, boolean animate) {
        if (animate) {
            autoShowOrHideActionBar(show);
        } else {
            if (show) {
                getSupportActionBar().show();
            } else {
                getSupportActionBar().hide();
            }
        }
    }

    @Override
    public void setToolbarAlpha(float alpha) {

    }

    @Override
    public Class<? extends Fragment> onNavigationDrawerItemSelected(MenuOption position) {
        // Currently there is no drawer
        return null;
    }

    /**
     * fixme - auto convert dp to pixel, auto set toolbar and tile name for toolbar
     */
    protected void injectViewsAndSetUpToolbar() {
        injectViewsAndSetUpToolbar(R.id.toolbar, R.id.toolbar_title_tv);
        ViewCompat.setElevation(this.toolbar, ViewUtils.convertDpToPixel(4.0F, this));
    }


    /**
     * Initializes the Action Bar auto-hide (aka Quick Recall) effect.
     * fixme -  get pixel from dp in dimen file.
     */
    private void initActionBarAutoHide() {
        actionBarAutoHideMinY = getResources().getDimensionPixelSize(R.dimen.action_bar_auto_hide_min_y);
        actionBarAutoHideSensitivity = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_sensivity);
    }

    /**
     * Indicates that the main content has scrolled (for the purposes of showing/hiding
     * the action bar for the "action bar auto hide" effect). currentY and deltaY may be exact
     * (if the underlying view supports it) or may be approximate indications:
     * deltaY may be INT_MAX to mean "scrolled forward indeterminately" and INT_MIN to mean
     * "scrolled backward indeterminately".  currentY may be 0 to mean "somewhere close to the
     * start of the list" and INT_MAX to mean "we don't know, but not at the start of the list"
     */
    private void onMainContentScrolled(int currentY, int deltaY) {
        if (deltaY > actionBarAutoHideSensitivity) {
            deltaY = actionBarAutoHideSensitivity;
        } else if (deltaY < -actionBarAutoHideSensitivity) {
            deltaY = -actionBarAutoHideSensitivity;
        }

        if (Math.signum(deltaY) * Math.signum(actionBarAutoHideSignal) < 0) {
            // deltaY is a motion opposite to the accumulated signal, so reset signal
            actionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            actionBarAutoHideSignal += deltaY;
        }

        boolean shouldShow = currentY < actionBarAutoHideMinY ||
                (actionBarAutoHideSignal <= -actionBarAutoHideSensitivity);
        autoShowOrHideActionBar(shouldShow);
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if (show == actionBarShown) {
            return;
        }

        actionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }

    /**
     * fixme show or hide action bar with animation
     * @param shown
     */
    protected void onActionBarAutoShowOrHide(boolean shown) {
        View view = toolbar;

        if (shown) {
            view.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        } else {
            view.animate()
                    .translationY(-view.getBottom())
                    .alpha(0)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        }
    }


}
