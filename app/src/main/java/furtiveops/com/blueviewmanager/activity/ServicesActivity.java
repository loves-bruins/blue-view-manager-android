package furtiveops.com.blueviewmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.dialog.BottomSheetCRUDDialogFragment;
import furtiveops.com.blueviewmanager.dialog.ProgressDialogFragment;
import furtiveops.com.blueviewmanager.models.Service;
import furtiveops.com.blueviewmanager.viewholders.BaseServiceViewHolder;

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

        //private FirebaseRecyclerAdapter<Service, WaterChangeServiceHolder> mAdapter;

        private LinearLayoutManager mManager;

        private HashMap<Integer, FirebaseRecyclerAdapter<Service, BaseServiceViewHolder>> servicesMap = new HashMap<>();

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

            for(int i = 0; i < bottomBar.getTabCount(); i++) {
                BottomBarTab barTab = bottomBar.getTabAtPosition(i);
                servicesMap.put(barTab.getId(), null);
            }

            bottomBar.setOnTabSelectListener(tabId -> {
                Log.i(TAG, "TabId = " + Integer.toString(tabId));
                switch (tabId) {
                    case R.id.tab_water_change:
                        list.setAdapter(null);
                        setupView(getAdapterForViewId(tabId, "0ef5b8bd-fabd-4d02-8f4c-b7362c2a6810"));
                        break;
                    case R.id.tab_algae_scrape:
                        list.setAdapter(null);
                        setupView(getAdapterForViewId(tabId, "619d6018-7ff1-427d-9e2c-230512ac4760"));
                        break;
                    case R.id.tab_general_cleaning:
                        list.setAdapter(null);
                        setupView(getAdapterForViewId(tabId, "dcd0e180-13a1-4b01-ba56-4e7bbd4a07e5"));
                        break;
                    case R.id.tab_sand_siphon:
                        list.setAdapter(null);
                        setupView(getAdapterForViewId(tabId, "13604ecc-90e6-49fb-8585-78efd958c49e"));
                        break;
                }
            }, true);

            return view;
        }

        public FirebaseRecyclerAdapter<Service, BaseServiceViewHolder> getAdapterForViewId(int id, final String guid) {
            FirebaseRecyclerAdapter<Service, BaseServiceViewHolder> adapter = null;

            if(servicesMap.containsKey(id)) {
                adapter = servicesMap.get(id);
                if(null == adapter) {
                    Query usersQuery = mDatabase.child("user_services").child(userId).child(guid).orderByKey();
                    adapter = new FirebaseRecyclerAdapter<Service, BaseServiceViewHolder>(Service.class, R.layout.service_performed_item, BaseServiceViewHolder.class, usersQuery) {
                        @Override
                        protected void populateViewHolder(BaseServiceViewHolder viewHolder, Service model, final int position) {
                            viewHolder.bind(model, new BaseServiceViewHolder.ItemClickListener() {
                                @Override
                                public void onClick(View v) {

                                }

                                @Override
                                public boolean onLongClick(View v) {
                                    BottomSheetCRUDDialogFragment bottomSheetMenu = new BottomSheetCRUDDialogFragment();
                                    bottomSheetMenu.show(getFragmentManager(), bottomSheetMenu.getTag());
                                    return true;
                                }
                            });
                        }

                        @Override
                        protected void onDataChanged() {
                            super.onDataChanged();
                            hideProgress();
                        }
                    };
                    servicesMap.put(id, adapter);
                }
            }
            return adapter;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        private void setupView(FirebaseRecyclerAdapter<Service, BaseServiceViewHolder> adapter)
        {
            mManager = new LinearLayoutManager(getActivity());
            mManager.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(mManager);

            showProgress();
            list.setAdapter(adapter);
        }

        private void showProgress() {

            progressFragment.show(getFragmentManager(), ProgressDialogFragment.TAG);
        }

        private void hideProgress() {

            progressFragment.dismiss();
        }
    }
}
