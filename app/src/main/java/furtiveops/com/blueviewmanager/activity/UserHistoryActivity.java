package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.adapters.UserHistoryAdapter;

/**
 * Created by lorenrogers on 1/15/17.
 */

public class UserHistoryActivity extends AppCompatActivity {
    public static Intent makeIntent(final Context context, final String userId) {
        Intent intent = new Intent(context, UserHistoryActivity.class);
        intent.putExtra(IntentConstants.USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_layout);

        final String userId = getIntent().getStringExtra(IntentConstants.USER_ID);
        UserHistoryFragment fragment = UserHistoryFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();


    }

    public static class UserHistoryFragment extends Fragment {
        public static final String TAG = UserHistoryFragment.class.getSimpleName();

        @BindView(R.id.list)
        ListView list;

        private String userId;
        private UserHistoryAdapter adapter;


        public static UserHistoryFragment newInstance(final String userId) {
            UserHistoryFragment fragment = new UserHistoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.USER_ID, userId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            userId = getArguments().getString(IntentConstants.USER_ID);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.user_history_layout, container, false);

            ButterKnife.bind(this, view);

            setupView();

            return view;
        }

        private void setupView() {
            List<String> items = new ArrayList<String>();
            items.add("Cycle Tests");
            items.add("Services");
            items.add("Purchases");

            adapter = new UserHistoryAdapter(getActivity(), items);
            list.setAdapter(adapter);
        }
    }
}
