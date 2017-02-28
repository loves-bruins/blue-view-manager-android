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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        setContentView(R.layout.home_screen_content_layout);

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

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        testCountDownLatch();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(position == 0) {
                        ((HomeActivity)getActivity()).navigateToCycleTestHistory(userId);
                    }
                    else if(position == 1)
                    {
                        ((HomeActivity)getActivity()).navigateToServices(userId);
                    }
                }
            });
        }

        public void testCountDownLatch() throws Exception {
            // should be called twice
            final CountDownLatch testLatch = new CountDownLatch(2);
            ExecutorService executor = Executors.newFixedThreadPool(1);
            AsyncProcessor processor = new AsyncProcessor(new Observer() {
                // this observer would be the analogue for a listener in your async process
                public void update(Observable o, Object arg) {
                    System.out.println("Counting down...");
                    testLatch.countDown();
                }
            });

            //submit two tasks to be process
            // (in my real world example, these were JMS messages)
            executor.submit(processor);
            executor.submit(processor);

            System.out.println("Submitted tasks. Time to wait...");
            long time = System.currentTimeMillis();
            testLatch.await(5000, TimeUnit.MILLISECONDS); // bail after a reasonable time
            long totalTime = System.currentTimeMillis() - time;

            System.out.println("I awaited for " + totalTime +
                    "ms. Did latch count down? " + (testLatch.getCount() == 0));

            executor.shutdown();
        }

        // just a process that takes a random amount of time
        // (up to 2 seconds) and calls its listener
        public class AsyncProcessor implements Callable<Object> {
            private Observer listener;
            private AsyncProcessor(Observer listener) {
                this.listener = listener;
            }

            public Object call() throws Exception {
                // some processing here which can take all kinds of time...
                int sleepTime = new Random().nextInt(2000);
                System.out.println("Sleeping for " + sleepTime + "ms");
                Thread.sleep(sleepTime);
                listener.update(null, null); // not standard usage, but good for a demo
                return null;
            }
        }
    }
}
