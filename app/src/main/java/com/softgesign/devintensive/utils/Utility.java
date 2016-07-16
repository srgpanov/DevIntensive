package com.softgesign.devintensive.utils;


import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        listView.measure(0,0);
        params.height = listView.getMeasuredHeight() * listAdapter.getCount() + (listView.getDividerHeight() * (listAdapter.getCount() - 1));;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}