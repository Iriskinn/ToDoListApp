package com.sample.drawer.utils;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sample.drawer.Data;
import com.sample.drawer.R;
import com.sample.drawer.TaskList;
import com.sample.drawer.fragments.AboutFragment;
import com.sample.drawer.fragments.AddListFragment;
import com.sample.drawer.fragments.AddTaskFragment;
import com.sample.drawer.fragments.DoneListFragment;
import com.sample.drawer.fragments.ListFragment;
import com.sample.drawer.fragments.OpenSourceFragment;
import com.sample.drawer.fragments.SettingsFragment;


public class Utils {

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            //
        }

    }

    public static Drawer.OnDrawerItemClickListener handlerOnClick(final Drawer.Result drawerResult, final ActionBarActivity activity) {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                if (drawerItem != null) {

                    if (drawerItem.getIdentifier() == 1) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AddListFragment()).commit();
                        activity.setTitle(R.string.add_list);
                    } else if (drawerItem.getIdentifier() == 4) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new DoneListFragment()).commit();
                        activity.setTitle(R.string.done_list);
                    } else if (drawerItem.getIdentifier() == 5) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AboutFragment()).commit();
                        activity.setTitle(R.string.about);
                    } else if (drawerItem.getIdentifier() == 6) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new OpenSourceFragment()).commit();
                        activity.setTitle(R.string.open_source);
                    } else if (drawerItem.getIdentifier() == 10) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new SettingsFragment()).commit();
                        activity.setTitle(R.string.settings);
                    } else if (drawerItem.getIdentifier() >= 100) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                                new ListFragment(drawerItem.getIdentifier() - 100)).commit();
                        activity.setTitle(Data.lists.get(drawerItem.getIdentifier() - 100).getName());
                    }

                }
            }
        };
    }

    public static Drawer.Result createCommonDrawer(final ActionBarActivity activity, Toolbar toolbar, AccountHeader.Result headerResult) {

        String doneBage = " ";
        if (Data.doneList.size() > 0) {
            doneBage = Integer.toString(Data.doneList.size());
        }

        final Drawer.Result drawerResult = new Drawer()
            .withActivity(activity)
            .withHeader(R.layout.drawer_header)
            .withToolbar(toolbar)
            .withOnDrawerListener(new Drawer.OnDrawerListener() {
                @Override
                public boolean equals(Object o) {
                    return super.equals(o);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    //Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard(activity);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    //Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                }
            }).build();

        drawerResult.addItems(
                new PrimaryDrawerItem().withName(R.string.add_list).withIcon(GoogleMaterial.Icon.gmd_add).withIdentifier(1),
                new DividerDrawerItem()
        );

        int id = 0;
        for (TaskList list : Data.lists) {
            drawerResult.addItem(
                    new PrimaryDrawerItem().withName(list.getName()).withIcon(GoogleMaterial.Icon.gmd_list).withIdentifier(100 + id++)
            );
        }


        drawerResult.addItems(
                new PrimaryDrawerItem().withName(R.string.done_list).withIcon(GoogleMaterial.Icon.gmd_done_all).withBadge(doneBage).withIdentifier(4),
                new PrimaryDrawerItem().withName(R.string.settings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(10),

                new DividerDrawerItem(),
                new PrimaryDrawerItem().withName(R.string.about).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(5)
        );


           // drawerResult.build();


        drawerResult.setOnDrawerItemClickListener(handlerOnClick(drawerResult, activity));

        return drawerResult;
    }


}
