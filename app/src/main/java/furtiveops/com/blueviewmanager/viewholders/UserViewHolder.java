package furtiveops.com.blueviewmanager.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.User;

/**
 * Created by lorenrogers on 1/10/17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.role)
    TextView role;

    @BindView(R.id.user_item_layout)
    LinearLayout itemLayout;

    public interface ItemClickListener {
        void onClick(View v);
    }

    private ItemClickListener listener;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(User user, final String userId, final ItemClickListener listener)
    {
        this.listener = listener;
        name.setText(user.getUserName());
        role.setText(user.getRole());
        user.setUid(userId);
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
