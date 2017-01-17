package furtiveops.com.blueviewmanager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import furtiveops.com.blueviewmanager.R;

/**
 * Created by lorenrogers on 1/10/17.
 */

public class ProgressDialogFragment extends DialogFragment {
    public final static String TAG = ProgressDialogFragment.class.getSimpleName();

    @BindView(R.id.progress_bar_layout)
    RelativeLayout layout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;

    public static ProgressDialogFragment newInstance() {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.progress_bar_fragment_layout, null);

        unbinder = ButterKnife.bind(this, view);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Dialog dialog = new MaterialDialog.Builder(getActivity())
                .title("")
                .customView(view, false)
                .autoDismiss(false)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
        unbinder.unbind();
    }
}
