package com.elee.fenderchallenge.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elee.fenderchallenge.R;

import java.util.ArrayList;
import java.util.List;

/**
 * An ArrayAdapter for the ListView provided in MainActivity
 *
 * @author Eugene Lee
 */
public class StockAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list3 = new ArrayList<>();

    /**
     * Creates a new StockAdapter object for use with the ListView class.
     *
     * @param c         The application's current context
     * @param symbols   A List of values pertaining to the "Symbol" key
     * @param names     A List of values pertaining to the "Name" key
     * @param exchanges A List of values pertaining to the "Exchange" key
     */
    public StockAdapter(Context c, List<String> symbols, List<String> names, List<String> exchanges) {
        super(c, -1, symbols);
        context = c;
        list = symbols;
        list2 = names;
        list3 = exchanges;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_layout, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.symbol);
        TextView textView2 = (TextView) row.findViewById(R.id.name);
        TextView textView3 = (TextView) row.findViewById(R.id.exchange);
        textView.setText(list.get(position));
        textView2.setText(list2.get(position));
        textView3.setText(list3.get(position));

        return row;
    }
}
