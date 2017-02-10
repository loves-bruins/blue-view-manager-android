package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.CycleTest;

/**
 * Created by lorenrogers on 2/10/17.
 */

public class AddCycleTestActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddCycleTestActivity.class.getSimpleName();

    public static Intent makeIntent(final Context context, final String userId)
    {
        Intent intent = new Intent(context, AddCycleTestActivity.class);
        intent.putExtra(IntentConstants.USER_ID, userId);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_content_layout);

        final String userId = getIntent().getStringExtra(IntentConstants.USER_ID);
        AddCycleTestFragment fragment = AddCycleTestFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static class AddCycleTestFragment extends Fragment
    {
        @BindView(R.id.date_value)
        DatePicker datePicker;

        @BindView(R.id.ammonia_level_value)
        EditText ammonia;

        @BindView(R.id.nitrate_level_value)
        EditText nitrate;

        @BindView(R.id.nitrite_level_value)
        EditText nitrite;

        @BindView(R.id.notes_value)
        EditText notes;

        private String userId;
        private Unbinder unbinder;

        // [START define_database_reference]
        private DatabaseReference database;
        // [END define_database_reference]

        public static AddCycleTestFragment newInstance(final String userId)
        {
            AddCycleTestFragment fragment = new AddCycleTestFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.USER_ID, userId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            userId = getArguments().getString(IntentConstants.USER_ID);

            // [START create_database_reference]
            database = FirebaseDatabase.getInstance().getReference();
            // [END create_database_reference]

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.new_cycle_test_layout, container, false);
            unbinder = ButterKnife.bind(this, view);

            setupView();

            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        @OnClick(R.id.save)
        public void save()
        {
            createCycleTestRow(userId);
            getActivity().finish();
        }

        public void createCycleTestRow(final String userId)
        {
            // [START single_value_read]
            database.child("users").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Write new post
                            writeNewPost(userId, "February 10, 2017",
                                    Double.parseDouble(ammonia.getText().toString()),
                                    Double.parseDouble(nitrate.getText().toString()),
                                    Double.parseDouble(nitrite.getText().toString()),
                                    notes.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(LOG_TAG, "createCycleTestRow", databaseError.toException());
                        }
                    });
            // [END single_value_read]
        }

        // [START write_fan_out]
        private void writeNewPost(String userId, String date, Double ammonia, Double nitrate, Double nitrite, String notes) {
            CycleTest test = new CycleTest(userId, date, ammonia, nitrate, nitrite, notes);
            Map<String, Object> postValues = test.toMap();
            final String key = database.child("tests").child(userId).child("cycle_tests").push().getKey();

            database.child("tests").child(userId).child("cycle_tests").child(key).setValue(postValues);
        }
        // [END write_fan_out]

        private void setupView()
        {

        }
    }
}
