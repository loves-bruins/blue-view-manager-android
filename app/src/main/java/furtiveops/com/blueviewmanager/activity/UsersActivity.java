package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.dialog.ProgressDialogFragment;
import furtiveops.com.blueviewmanager.models.User;
import furtiveops.com.blueviewmanager.viewholders.UserViewHolder;

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
        setContentView(R.layout.home_screen_content_layout);
        final String userId = getIntent().getStringExtra(IntentConstants.USER_ID);

        UsersFragment fragment = UsersFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static class UsersFragment extends Fragment {
        public static final String TAG = UsersFragment.class.getSimpleName();

        @BindView(R.id.list)
        RecyclerView list;

        private Unbinder unbinder;

        private ProgressDialogFragment progressFragment;

        // [START define_database_reference]
        private DatabaseReference mDatabase;
        // [END define_database_reference]

        private FirebaseRecyclerAdapter<User, UserViewHolder> mAdapter;
        private LinearLayoutManager mManager;

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

            View view = inflater.inflate(R.layout.generic_recycler_view_layout, container, false);

            unbinder = ButterKnife.bind(this, view);

            progressFragment = ProgressDialogFragment.newInstance();

            // [START create_database_reference]
            mDatabase = FirebaseDatabase.getInstance().getReference();
            // [END create_database_reference]

            setupView();
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(null != mAdapter)
            {
                mAdapter.cleanup();
            }
        }

        private void setupView()
        {
            mManager = new LinearLayoutManager(getActivity());
            mManager.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(mManager);

            showProgress();
            final Query usersQuery = mDatabase.child("users").limitToFirst(100);

            mAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class, R.layout.user_item_layout, UserViewHolder.class, usersQuery) {
                @Override
                protected void populateViewHolder(UserViewHolder viewHolder, User model, final int position) {
                    final DatabaseReference ref = getRef(position);
                    viewHolder.bind(model, getRef(position).getKey(),  new UserViewHolder.ItemClickListener() {
                        @Override
                        public void onClick(View v) {
                            User user = mAdapter.getItem(position);
                            ((HomeActivity)getActivity()).navigateToUserHistory(ref.getKey());
                        }
                    });
                    hideProgress();
                }
            };

            list.setAdapter(mAdapter);
        }

        private void showProgress() {

            if (!progressFragment.isAdded()) {
                progressFragment.show(getFragmentManager(), ProgressDialogFragment.TAG);
            }
        }

        private void hideProgress() {

            if (progressFragment.isAdded()) {
                progressFragment.dismiss();
            }
        }

        public void createUserInDatabase(User user)
        {

        }
    }
}
