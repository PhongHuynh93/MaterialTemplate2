package dhbk.android.materialtemplate2.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.byoutline.secretsauce.utils.LogUtils;

import dhbk.android.materialtemplate2.activities.KickMaterialBaseActivity;


/**
 * Created by huynhducthanhphong on 8/23/16.
 * fixme - implement interface for activity implement, imlement life cycle of fragment
 */
public abstract class KickMaterialFragment extends Fragment {
    private static final String TAG = LogUtils.makeLogTag(KickMaterialFragment.class);

    protected HostActivity hostActivity;

    // make it have options
    public KickMaterialFragment() {
        setHasOptionsMenu(true);
    }

    // make activity implement this
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            hostActivity = (HostActivity) activity;
        } catch (ClassCastException e) {
            throw new IllegalStateException(activity.getClass().getSimpleName()
                    + " does not implement " + HostActivity.class.getSimpleName()
                    + " interface");
        }
    }

    // detach from activity, todo - but what is the purpose of this method
    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = new StubHostActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpValidators();
    }

    // hide keyboard
    @Override
    public void onDestroyView() {
        if (!isAdded()) {
            hostActivity.hideKeyboard();
        }
        super.onDestroyView();
    }

    public String getScreenName() {
        return getClass().getSimpleName();
    }


    protected void setUpValidators() {
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionbar();
    }

    public abstract String getFragmentActionbarName();

    /**
     * set tile for toolbar in activity
     * @param title
     */
    public void setActionbarTitle(String title) {
        KickMaterialBaseActivity baseActivity = (KickMaterialBaseActivity) getActivity();
        if (baseActivity != null) {
            if (!TextUtils.isEmpty(title)) {
                baseActivity.setTitle(title);
            }
        }
    }

    // listener for listview
    protected void setUpListeners() {
        //empty by default
    }

    // set the tile in toolbar in activity by getting it from method
    private void setActionbar() {
        KickMaterialBaseActivity baseActivity = (KickMaterialBaseActivity) getActivity();
        if (baseActivity != null) {
            if (!TextUtils.isEmpty(getFragmentActionbarName())) {
                baseActivity.setTitle(getFragmentActionbarName());
            }
//            baseActivity.setDisplayHomeAsUpEnabled(showBackButtonInActionbar());
        }
    }




    /**
     * fixme - add interface to communicate with activity, extend base activity from library
     */
    public interface HostActivity extends com.byoutline.secretsauce.activities.HostActivityV4 {
        void enableActionBarAutoHide(final RecyclerView listView);

        void showActionbar(boolean show, boolean animate);

        void setToolbarAlpha(float alpha);
    }


    private static class StubHostActivity extends com.byoutline.secretsauce.activities.StubHostActivityV4 implements HostActivity {
        @Override
        public void enableActionBarAutoHide(RecyclerView listView) {
        }

        @Override
        public void showActionbar(boolean show, boolean animate) {
        }

        @Override
        public void setToolbarAlpha(float alpha) {
        }
    }


}
