package com.example.yuki.barcodetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MainListAdapter extends BaseAdapter {
    List<BookModel> listData;
    LayoutInflater inflater;

    MainListAdapter(Context context, List<BookModel> data){
        super();
        listData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public BookModel getItem(int position) {

        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.main_list_column, null);
            holder = new ViewHolder();
            holder.titleText = (TextView)convertView.findViewById(R.id.title_text);
            holder.authorText = (TextView)convertView.findViewById(R.id.author_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        BookModel book = getItem(position);
        holder.titleText.setText(book.getTitle()+"");
        holder.authorText.setText(book.getAuthor());

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView authorText;
    }
}