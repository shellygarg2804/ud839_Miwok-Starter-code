package com.example.android.miwok;

import android.app.Activity;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class wordAdaptor extends ArrayAdapter<Word> {

    private int colorresourceid;
    public wordAdaptor(Activity context, ArrayList<Word> words, int background){

        super(context,0,words);
        colorresourceid=background;

    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View ListItemView= convertView;
        if(ListItemView==null)
        {
            ListItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Word currentword= getItem(position);

        String defaultcurrentword= currentword.getMdefaulttranslation();
        TextView textView1= (TextView)ListItemView.findViewById(R.id.default_Text_view);
        textView1.setText(defaultcurrentword);

        String Miwokcurrentword= currentword.getMmiwoktranslation();
        TextView textView2= (TextView)ListItemView.findViewById(R.id.Miwok_text_view);
        textView2.setText(Miwokcurrentword);

        int imageresourceid = currentword.getMimageresourceid();
        ImageView imageView = (ImageView) ListItemView.findViewById(R.id.IMAGE);
        if(currentword.hasImage()) {
            imageView.setImageResource(imageresourceid);
        }
        else{
            imageView.setVisibility(View.GONE);  //imageview is not shown if we do not provide a image (phrases activity)
        }

        //to set different color of each category:
        View text = ListItemView.findViewById(R.id.text);// fint the view by id
        int color = ContextCompat.getColor(getContext(),colorresourceid); // to get color
        text.setBackgroundColor(color);

        return ListItemView;

    }

}
