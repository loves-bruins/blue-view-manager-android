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

public class UsersAdapter extends ArrayAdapter<User> {
    private final List<User> users;

    public UsersAdapter(Context context, List<User> users) {
        super(context, R.layout.user_item_layout, users);
        this.users = users;
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
            view = viewInflater.inflate(R.layout.user_item_layout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        final User user = getItem(position);
        holder.name.setText(user.getUserName());
        holder.role.setText(user.getRole());

        return view;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    public class ViewHolder
    {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.role)
        TextView role;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
