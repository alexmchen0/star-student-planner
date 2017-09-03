package testing.ver1;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final String TAG = "-----------------------";
    private static final int TASKS = 0;
    private static final int EXAMS = 1;
    private static final int EVENTS = 2;
    private static final int REMINDERS = 3;

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    int index;
    ToDoListFragment tasksFragment;
    ToDoListFragment examsFragment;
    ToDoListFragment eventsFragment;
    ToDoListFragment remindersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change fab action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (index == TASKS)
                    intent = new Intent(NavigationDrawerActivity.this, AddTaskActivity.class);
                else if (index == EXAMS)
                    intent = new Intent(NavigationDrawerActivity.this, AddExamActivity.class);
                else if (index == EVENTS)
                    intent = new Intent(NavigationDrawerActivity.this, AddEventActivity.class);
                else
                    intent = new Intent(NavigationDrawerActivity.this, AddReminderActivity.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tasksFragment = new ToDoListFragment();
        examsFragment = new ToDoListFragment();
        eventsFragment = new ToDoListFragment();
        remindersFragment = new ToDoListFragment();

        index = TASKS;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, tasksFragment, "MY_FRAGMENT");
        ft.commit();

        navigationView.setCheckedItem(R.id.nav_tasks);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.tasks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "Entered onActivityResult for Activity");

        ToDoListFragment myFragment = (ToDoListFragment) getFragmentManager().findFragmentByTag("MY_FRAGMENT");
        if (myFragment != null && myFragment.isVisible()) {
            myFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //TODO: change menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        //menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    //TODO: change menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ToDoListFragment myFragment = (ToDoListFragment) getFragmentManager().findFragmentByTag("MY_FRAGMENT");
        switch (item.getItemId()) {
            case R.id.sort_button:
                DialogFragment newFragment = new SortDialogFragment();
                newFragment.show(getSupportFragmentManager(), "sort");
            case MENU_DELETE:
                if (myFragment != null && myFragment.isVisible()) {
                    myFragment.onOptionsItemSelected(item);
                }
                return true;
            case MENU_DUMP:
                if (myFragment != null && myFragment.isVisible()) {
                    myFragment.onOptionsItemSelected(item);
                }
                return true;
            case R.id.action_settings:
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    //TODO: change navigation item clicks
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (id == R.id.nav_tasks) {
            actionBar.setTitle(R.string.tasks);
            index = TASKS;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, tasksFragment, "MY_FRAGMENT");
            ft.commit();
        } else if (id == R.id.nav_exams) {
            actionBar.setTitle(R.string.exams);
            index = EXAMS;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, examsFragment, "MY_FRAGMENT");
            ft.commit();
        } else if (id == R.id.nav_events) {
            actionBar.setTitle(R.string.events);
            index = EVENTS;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, eventsFragment, "MY_FRAGMENT");
            ft.commit();
        } else if (id == R.id.nav_reminders) {
            index = REMINDERS;
            actionBar.setTitle(R.string.reminders);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, remindersFragment, "MY_FRAGMENT");
            ft.commit();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(NavigationDrawerActivity.this, SettingsActivity.class);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
        } else if (id == R.id.nav_help) {
            Uri form = Uri.parse("https://docs.google.com/forms/d/17q_tTfuvtiPtRVwxEiq0g5PFk64HSrsuiFI_mBFdlkk");
            Intent intent = new Intent(Intent.ACTION_VIEW, form);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class SortDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            boolean[] bullshit = {true, true, false};
            builder.setTitle("Sort by:")
                    .setMultiChoiceItems(R.array.sort_by_list, bullshit,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                    }
                                }
                            })
                    .setPositiveButton("Sort", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
