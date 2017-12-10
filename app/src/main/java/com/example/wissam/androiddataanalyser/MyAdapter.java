package com.example.wissam.androiddataanalyser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by wissam on 04/10/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<String> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<DataPoint> myDataset) {
        mDataset = new ArrayList<String>();

        for(int i=0; i < myDataset.size(); ++i){
            Data tmp = new Data(myDataset.get(i).getX(), myDataset.get(i).getY());
            mDataset.add( "numberSolution: "+ tmp.getNumberSolution() + "\nTime: "+ tmp.getTime() );
        }
    }
    public void addItem(Double time, Double value){
        mDataset.add("numberSolution: "+  time +"\nTime: "+ value);
        this.notifyDataSetChanged();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get inflater and get view by resource id itemLayout
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.textview, parent, false);
        // return ViewHolder with View
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

