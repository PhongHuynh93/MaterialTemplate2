package dhbk.android.materialtemplate2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import org.parceler.Parcels;

import dhbk.android.materialtemplate2.R;
import dhbk.android.materialtemplate2.appjava.model.Category;
import dhbk.android.materialtemplate2.fragments.ProjectsListFragment;
import dhbk.android.materialtemplate2.managers.DataManager;

public class MainActivity extends KickMaterialBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fixme - add toolbar for this view
        injectViewsAndSetUpToolbar();
//        setUpDrawer(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ProjectsListFragment.newInstance(DataManager.getCategoryAll()))
                    .commit();
        }
    }

    /**
     * show a menu toolbar at the top
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * after choosing a item in a filter, show a fragment with that filter, change the title
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean categorySelection = requestCode == CategoriesListActivity.DEFAULT_REQUEST_CODE;
        if (categorySelection && resultCode == CategoriesListActivity.RESULT_CATEGORY_SELECTED) {
            Category category = Parcels.unwrap(data.getParcelableExtra(CategoriesListActivity.ARG_CATEGORY));
            // fixme - replace old frag witn new category frag by calling parent method
            showFragment(ProjectsListFragment.newInstance(category), true);
            // fixme - set title in toolbar again
            setToolbarText(getString(category.nameResId));
        }
    }

    /**
     * fixme - change alpha of a view (when we scroll, it's will opacity so we can see the content underneath)
     * @param alpha
     */
    @Override
    public void setToolbarAlpha(float alpha) {
        toolbar.getBackground().setAlpha((int) (alpha * 255));
    }



}
