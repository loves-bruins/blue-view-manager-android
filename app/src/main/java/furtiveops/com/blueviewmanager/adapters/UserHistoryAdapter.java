package furtiveops.com.blueviewmanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.User;

/**
 * Created by lorenrogers on 1/10/17.
 */

public class UserHistoryAdapter extends ArrayAdapter<String> {
    private final List<String> items;

    public UserHistoryAdapter(Context context, List<String> items) {
        super(context, R.layout.user_history_item_layout, items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        final ViewHolder holder;
        if(null == convertView)
        {
            final LayoutInflater viewInflater = (LayoutInflater)getContext()
                                                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.user_history_item_layout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        final String item = getItem(position);
        holder.item.setText(item);

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder
    {
        @BindView(R.id.history_item)
        TextView item;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
