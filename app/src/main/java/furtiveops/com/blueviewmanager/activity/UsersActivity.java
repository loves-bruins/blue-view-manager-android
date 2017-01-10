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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.adapters.UsersAdapter;
import furtiveops.com.blueviewmanager.models.User;

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

        @BindView(R.id.list)
        ListView list;

        Unbinder unbinder;

        UsersAdapter adapter;

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

            unbinder = ButterKnife.bind(this, view);

            setupView();
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        private void setupView()
        {
            List<User> users = new ArrayList<User>();

            users.add(new User("", "loren.rogers@gmail.com", "user"));
            users.add(new User("", "blueviewaquatics@gmail.com", "admin"));
            users.add(new User("", "lrogersego@gmail.com", "user"));

            adapter = new UsersAdapter(getActivity(), users);

            list.setAdapter(adapter);
        }
    }
}
