package dhbk.android.materialtemplate2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dhbk.android.materialtemplate2.R;


public class SearchListFragment extends Fragment {
    public SearchListFragment() {
        // Required empty public constructor
    }

    public static SearchListFragment newInstance() {
        SearchListFragment fragment = new SearchListFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

}
