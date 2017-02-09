package furtiveops.com.blueviewmanager.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.Service;

/**
 * Created by lorenrogers on 1/31/17.
 */

public class  BaseServiceViewHolder extends RecyclerView.ViewHolder {

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
        boolean onLongClick(View v);
    }

    private ItemClickListener listener;

    public void bind(Service service, final BaseServiceViewHolder.ItemClickListener listener)
    {
        this.listener = listener;
        DateTime dt = new DateTime(service.getDate());
        date.setText(dt.toLocalDate().toString());
        notes.setText(service.getNotes());
        cost.setText(Double.toString(service.getPrice()));
        itemLayout.setOnClickListener(v -> {
            if(null != listener)
            {
                listener.onClick(v);
            }
        });

        itemLayout.setOnLongClickListener(v -> {
            boolean result = false;
            if(null != listener) {
                result = listener.onLongClick(v);
            }
            return result;
        });
    }

    public BaseServiceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
