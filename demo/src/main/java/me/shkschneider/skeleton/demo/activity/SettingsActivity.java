package me.shkschneider.skeleton.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import me.shkschneider.skeleton.demo.fragment.SettingsFragment;
import me.shkschneider.skeleton.SkeletonFragmentActivity;

public class SettingsActivity extends SkeletonFragmentActivity {

    public static Intent getIntent(final Activity activity) {
        return new Intent(activity, SettingsActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        home(true);

        setContentFragment(new SettingsFragment());
    }

}
