package com.softgesign.devintensive.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.softgesign.devintensive.ui.activities.BaseActivity;

/**
 * Created by Пан on 15.07.2016.
 */
public   class ScreenResolution extends BaseActivity{
    public static int getWidthDisplay(Context context){

        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int width = p.x;
        int height = p.y;
        return width;
    }
  public static int getHeightDisplay(Context context){

        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int height = p.y;
        return height;
    }

}
