package com.saquib.hello.coachingapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapterFragment extends PagerAdapter {
    private ArrayList<String> images;
    private Context context;
    private LayoutInflater inflater;

    public SliderAdapterFragment(Context context, ArrayList<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_layout, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        //Toast.makeText(context,"The image url is "+images.get(position),Toast.LENGTH_LONG).show();
        Picasso.get().load(images.get(position)).fit().centerCrop().into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
