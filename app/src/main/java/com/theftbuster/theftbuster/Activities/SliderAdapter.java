package com.theftbuster.theftbuster.Activities;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theftbuster.theftbuster.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
    this.context = context;
    }

    public int[] slide_images = {
            R.drawable.ic_helpreceive,
            R.drawable.ic_helpsend,
            R.drawable.ic_helpsecurity,
            R.drawable.ic_helpsim,
            R.drawable.ic_helpattempts,
            R.drawable.ic_helpthanks
    };

    public int[] slide_colors = {

    };

    public String[][] get_slide_texts(Context context) {
        return new String[][] {
                context.getResources().getStringArray(R.array.view_helpReceive),
                context.getResources().getStringArray(R.array.view_helpSend),
                context.getResources().getStringArray(R.array.view_helpSecurity),
                context.getResources().getStringArray(R.array.view_helpRIO),
                context.getResources().getStringArray(R.array.view_helpAttempts),
                context.getResources().getStringArray(R.array.view_helpThank)
        };
    };

    @Override
    public int getCount() {
        return get_slide_texts(context).length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slider_image);
        TextView slideTitles = (TextView) view.findViewById(R.id.slider_title);
        TextView slideDescs = (TextView) view.findViewById(R.id.slider_text);

        slideImageView.setImageResource(slide_images[position]);
        slideTitles.setText(get_slide_texts(context)[position][0]);
        slideDescs.setText(Html.fromHtml(get_slide_texts(context)[position][1]));

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

}
