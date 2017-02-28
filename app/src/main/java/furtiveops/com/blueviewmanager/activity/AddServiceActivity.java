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

import org.joda.time.DateTime;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.CycleTest;
import furtiveops.com.blueviewmanager.models.Service;

/**
 * Created by lorenrogers on 2/10/17.
 */

public class AddServiceActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddServiceActivity.class.getSimpleName();

    public static Intent makeIntent(final Context context, final String userId, final String serviceUUID)
    {
        Intent intent = new Intent(context, AddServiceActivity.class);
        intent.putExtra(IntentConstants.USER_ID, userId);
        intent.putExtra(IntentConstants.SERVICE_UUID, serviceUUID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen_content_layout);

        final String userId = getIntent().getStringExtra(IntentConstants.USER_ID);
        final String serviceUUID = getIntent().getStringExtra(IntentConstants.SERVICE_UUID);

        AddServiceFragment fragment = AddServiceFragment.newInstance(userId, serviceUUID);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static class AddServiceFragment extends Fragment
    {
        @BindView(R.id.date_value)
        DatePicker datePicker;

        @BindView(R.id.cost_value)
        EditText cost;

        @BindView(R.id.notes_value)
        EditText notes;

        private String userId;
        private String serviceUUID;

        private Unbinder unbinder;

        // [START define_database_reference]
        private DatabaseReference database;
        // [END define_database_reference]

        public static AddServiceFragment newInstance(final String userId, final String serviceUUID)
        {
            AddServiceFragment fragment = new AddServiceFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.USER_ID, userId);
            bundle.putString(IntentConstants.SERVICE_UUID, serviceUUID);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            userId = getArguments().getString(IntentConstants.USER_ID);
            serviceUUID = getArguments().getString(IntentConstants.SERVICE_UUID);

            // [START create_database_reference]
            database = FirebaseDatabase.getInstance().getReference();
            // [END create_database_reference]
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.new_service_layout, container, false);
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
            createServiceRow(userId, serviceUUID);
            getActivity().finish();
        }

        public void createServiceRow(final String userId, final String serviceUUID)
        {
            // [START single_value_read]
            database.child("users").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Write new post
                            int day = datePicker.getDayOfMonth();
                            int month = datePicker.getMonth();
                            int year = datePicker.getYear();
                            DateTime dateTime = new DateTime(year, month, day, 0, 0, 0);

                            writeNewPost(serviceUUID, userId, dateTime.getMillis(),
                                    Double.parseDouble(cost.getText().toString()),
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
        private void writeNewPost(String serviceUUID, String userId, Long date, Double price, String notes) {
            Service test = new Service(serviceUUID, userId, date, price, notes);
            Map<String, Object> postValues = test.toMap();
            final String key = database.child("user_services").child(userId).child(serviceUUID).push().getKey();

            database.child("user_services").child(userId).child(serviceUUID).child(key).setValue(postValues);
        }
        // [END write_fan_out]

        private void setupView()
        {

        }
    }
}
