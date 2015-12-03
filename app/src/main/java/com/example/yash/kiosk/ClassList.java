package com.example.yash.kiosk;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassList extends ArrayAdapter<String> {

    DatabaseHelper db = new DatabaseHelper(getContext());


    ArrayList<String> classname = new ArrayList<>();
    ArrayList<String> time      = new ArrayList<>();
    ArrayList<String> venue     = new ArrayList<>();

    ClassList(Context context, ArrayList<String> name, ArrayList<String> name1,ArrayList<String> name2)
    {
        super(context, R.layout.classlist, name);
        classname = name;
        time      = name1;
        venue     = name2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Integer images[] = {R.drawable.pic1};

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = null;

        if (customView == null) {
            customView = inflater.inflate(R.layout.classlist, parent, false);

           //View customView = inflater.inflate(R.layout.classlist, parent, false);
       /*     ProgressBar progressBar = (ProgressBar)customView.findViewById(R.id.progressBar);
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 999); // see this max value coming back here, we animale towards that value
            animation.setDuration (5000); //in milliseconds
            animation.setInterpolator (new DecelerateInterpolator());
            animation.start ();*/
            TextView t1 = (TextView) customView.findViewById(R.id.classname);
            TextView t2 = (TextView) customView.findViewById(R.id.time);
            TextView t3 = (TextView) customView.findViewById(R.id.venue);
            ImageView imageView = (ImageView) customView.findViewById(R.id.imageDisplay);



            t1.setText(classname.get(position));
            t2.setText(time.get(position));
            t3.setText(venue.get(position));
            imageView.setImageResource(images[0]);

        }


        return customView;
    }

}
