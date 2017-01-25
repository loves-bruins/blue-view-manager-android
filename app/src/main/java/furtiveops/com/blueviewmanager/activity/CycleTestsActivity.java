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
import furtiveops.com.blueviewmanager.models.CycleTest;
import furtiveops.com.blueviewmanager.viewholders.CycleTestViewHolder;

/**
 * Created by lorenrogers on 1/25/17.
 */

public class CycleTestsActivity extends AppCompatActivity {

    public static Intent makeIntent(final Context context, final String userId) {
        Intent intent = new Intent(context, CycleTestsActivity.class);
        intent.putExtra(IntentConstants.USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        final String userId = getIntent().getStringExtra(IntentConstants.USER_ID);

        CycleTestsFragment fragment = CycleTestsFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static class CycleTestsFragment extends Fragment {
        public static final String TAG = CycleTestsFragment.class.getSimpleName();

        @BindView(R.id.list)
        RecyclerView list;

        private Unbinder unbinder;

        private ProgressDialogFragment progressFragment;
        private String userId;

        // [START define_database_reference]
        private DatabaseReference mDatabase;
        // [END define_database_reference]

        private FirebaseRecyclerAdapter<CycleTest, CycleTestViewHolder> mAdapter;
        private LinearLayoutManager mManager;

        public static CycleTestsFragment newInstance(final String userId)
        {
            CycleTestsFragment fragment = new CycleTestsFragment();
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
            View view = inflater.inflate(R.layout.generic_list_layout, container, false);

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
            Query usersQuery = mDatabase.child("tests").child(userId).child("cycle_tests").orderByKey();

            mAdapter = new FirebaseRecyclerAdapter<CycleTest, CycleTestViewHolder>(CycleTest.class, R.layout.cycle_test_results_item, CycleTestViewHolder.class, usersQuery) {
                @Override
                protected void populateViewHolder(CycleTestViewHolder viewHolder, CycleTest model, final int position) {
                    final DatabaseReference ref = getRef(position);
                    viewHolder.bind(model, new CycleTestViewHolder.ItemClickListener() {
                        @Override
                        public void onClick(View v) {
                            CycleTest user = mAdapter.getItem(position);
                            ((HomeActivity)getActivity()).navigateToUserHistory(user.getUid());
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
    }
}
