package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.dialog.ProgressDialogFragment;

/**
 * Created by lorenrogers on 1/25/17.
 */

public class ServicesActivity extends AppCompatActivity {

    public static Intent makeIntent(final Context context, final String userId) {
        Intent intent = new Intent(context, ServicesActivity.class);

        return intent;
    }


    public static class ServicesFragment extends Fragment {

        public static String TAG = ServicesFragment.class.getSimpleName();

        @BindView(R.id.list)
        RecyclerView list;

        @BindView(R.id.bottom_bar)
        BottomBar bottomBar;

        private Unbinder unbinder;

        private ProgressDialogFragment progressFragment;
        private String userId;

        // [START define_database_reference]
        private DatabaseReference mDatabase;
        // [END define_database_reference]

        public static ServicesFragment newInstance(final String userId) {
            ServicesFragment fragment = new ServicesFragment();
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
            View view = inflater.inflate(R.layout.recycler_view_with_bottom_bar, container, false);

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

        private void setupView()
        {
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
