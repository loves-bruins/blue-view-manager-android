package furtiveops.com.blueviewmanager.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.Service;

/**
 * Created by lorenrogers on 1/30/17.
 */

public class WaterChangeServiceHolder  extends RecyclerView.ViewHolder {
    @BindView(R.id.date_value)
    TextView date;

    @BindView(R.id.cost_value)
    TextView cost;

    @BindView(R.id.notes_value)
    TextView notes;

    @BindView(R.id.cycle_test_results_layout)
    FrameLayout itemLayout;

    public interface ItemClickListener {
        void onClick(View v);
    }

    private WaterChangeServiceHolder.ItemClickListener listener;

    public WaterChangeServiceHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Service service, final ItemClickListener listener)
    {
        this.listener = listener;
        DateTime dt = new DateTime(service.getDate());
        date.setText(dt.toLocalDate().toString());
        notes.setText(service.getNotes());
        cost.setText(Double.toString(service.getPrice()));
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != listener)
                {
                    listener.onClick(v);
                }
            }
        });
    }
}
