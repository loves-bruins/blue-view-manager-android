package furtiveops.com.blueviewmanager.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.CycleTest;

/**
 * Created by lorenrogers on 1/25/17.
 */

public class CycleTestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.date_value)
    TextView date;

    @BindView(R.id.ammonia_level_value)
    TextView ammonia;

    @BindView(R.id.nitrate_level_value)
    TextView nitrate;

    @BindView(R.id.nitrite_level_value)
    TextView nitrite;

    @BindView(R.id.notes_value)
    TextView notes;

    @BindView(R.id.cycle_test_results_layout)
    CardView itemLayout;

    public interface ItemClickListener {
        void onClick(View v);
    }

    private CycleTestViewHolder.ItemClickListener listener;

    public CycleTestViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CycleTest cycleTest, final ItemClickListener listener)
    {
        this.listener = listener;

        date.setText(cycleTest.getDate());
        notes.setText(cycleTest.getNotes());
        ammonia.setText(Double.toString(cycleTest.getAmmonia()));
        nitrate.setText(Double.toString(cycleTest.getNitrate()));
        nitrite.setText(Double.toString(cycleTest.getNitrite()));
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
