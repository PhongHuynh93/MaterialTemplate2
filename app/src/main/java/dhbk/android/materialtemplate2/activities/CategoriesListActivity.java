package dhbk.android.materialtemplate2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dhbk.android.materialtemplate2.R;


public class CategoriesListActivity extends AppCompatActivity {
    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    public static final int DEFAULT_REQUEST_CODE = 101;
    public static final int RESULT_CATEGORY_SELECTED = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);
    }
}
