/*
 * Copyright 2013 Roque Rueda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.roque.rueda.notepad;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class NoteActivity extends FragmentActivity {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
//    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    private TextFile mTextFile;
    private TextInput mTextInput;
    private final static String TAG = "NotePadRoque";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        if (savedInstanceState != null) {
            Fragment editTextFragment = new EditTextFragment();
            mTextInput = (TextInput) editTextFragment;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, editTextFragment)
                    .commit();
            String dummyContent = getString(R.string.app_name);
            savedInstanceState.putString(Constants.ARG_TEXT_FILE, dummyContent);
        }
        else {
            Fragment editTextFragment = new EditTextFragment();
            mTextInput = (TextInput) editTextFragment;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, editTextFragment)
                    .commit();
        }

        // Set up the dropdown list navigation in the action bar.
        /*actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        getActionBarThemedContextCompat(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_section1),
                                getString(R.string.title_section2),
                                getString(R.string.title_section3),
                        }),
                this);*/

        mTextFile = new TextFile(this);
    }

    /**
     * Backward-compatible version of {@link ActionBar#getThemedContext()} that
     * simply returns the {@link android.app.Activity} if
     * <code>getThemedContext</code> is unavailable.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private Context getActionBarThemedContextCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getActionBar().getThemedContext();
        } else {
            return this;
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        /*if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        /*outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.action_save:
                try
                {
                    mTextFile.createSDFile("test.txt", mTextInput.getText().toString());
                }
                catch (IOException ioex)
                {
                    Toast.makeText(this, ioex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return false;
    }
}
