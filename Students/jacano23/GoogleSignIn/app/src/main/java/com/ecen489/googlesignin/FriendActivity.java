package com.ecen489.googlesignin;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class FriendActivity extends ListActivity implements ActionMode.Callback{
    boolean flag = true;
    List<Model> list = new ArrayList<Model>();

    protected Object mActionMode;
    public int selectedItem = -1;
    /** Called when the activity is first created. */

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // create an array of Strings, that will be put to our ListActivity
        ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this,
                getModel());
        setListAdapter(adapter);



    }
    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        while (flag){
            if (checkBox.isChecked()) {
                // Start the CAB using the ActionMode.Callback defined above
                flag = false;
                mActionMode = FriendActivity.this.startActionMode(FriendActivity.this);
            }
        }
    }

    private List<Model> getModel() {
        list.add(get("Josh"));
        list.add(get("Benito"));
        list.add(get("Kevin"));
        list.add(get("Anudeep"));
        list.add(get("Nagaraj"));
        list.add(get("Pranay"));
        // Initially select one of the items
        return list;
    }

    private Model get(String s) {
        return new Model(s);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        // Assumes that you have "contexual.xml" menu resources
        inflater.inflate(R.menu.rowselection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem1_show:
                // Action picked, so close the CAB
                mode.finish();
                flag = true;
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        selectedItem = -1;
    }
}