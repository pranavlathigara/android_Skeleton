package me.shkschneider.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.shkschneider.app.R;
import me.shkschneider.skeleton.helper.ApplicationHelper;
import me.shkschneider.skeleton.SkeletonFragment;

public class MainFragment extends SkeletonFragment {

    public MainFragment() {
        title("Main");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
        textView1.setText(ApplicationHelper.name());
        final TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
        textView2.setText(ApplicationHelper.versionName());

        return view;
    }

}