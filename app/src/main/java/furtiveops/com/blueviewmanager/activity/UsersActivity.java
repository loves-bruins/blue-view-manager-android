package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;

/**
 * Created by lorenrogers on 1/8/17.
 */

public class UsersActivity extends AppCompatActivity {
    private static String LOG_TAG = UsersActivity.class.getSimpleName();

    public static Intent makeIntent(Context context, String userId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(IntentConstants.USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        final String userId = getIntent().getStringExtra(IntentConstants.USER_ID);

        UsersFragment fragment = UsersFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static class UsersFragment extends Fragment {

        public static UsersFragment newInstance(String userId) {
            UsersFragment fragment = new UsersFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.USER_ID, userId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = getActivity().getLayoutInflater().inflate(R.layout.users_layout, container, false);
            return view;
        }
    }
}
