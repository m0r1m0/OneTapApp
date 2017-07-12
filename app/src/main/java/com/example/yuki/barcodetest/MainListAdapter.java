package com.example.yuki.barcodetest;

import android.content.Context;
import android.util.Log;
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
        Log.d("debug","MainListAdapter");

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
            holder.salesDate_text = (TextView)convertView.findViewById(R.id.salesDate_text);
            holder.series_text = (TextView)convertView.findViewById(R.id.series_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        BookModel book = getItem(position);
        holder.titleText.setText(book.getTitle()+"");
        holder.authorText.setText(book.getAuthor());
        holder.salesDate_text.setText(book.getSalesDate());
        holder.series_text.setText(book.getSeriesName());
        Log.d("debug",book.getItemurl());
        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView authorText;
        public TextView salesDate_text;
        public TextView series_text;
    }
}