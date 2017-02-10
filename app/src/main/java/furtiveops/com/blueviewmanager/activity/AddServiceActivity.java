package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;

/**
 * Created by lorenrogers on 2/10/17.
 */

public class AddServiceActivity extends AppCompatActivity {

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
    }

    public static class AddServiceFragment extends Fragment
    {
        private String userId;
        private String serviceUUID;

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
        }
    }
}
