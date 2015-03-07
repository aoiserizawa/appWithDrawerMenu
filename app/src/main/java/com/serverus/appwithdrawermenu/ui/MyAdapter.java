package com.serverus.appwithdrawermenu.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.serverus.appwithdrawermenu.R;

import java.util.List;
import java.util.Collections;

/**
 * Created by alvinvaldez on 2/26/15.
 */

// RecyclerView.Adapter<VH> VH means it expects you to have
// another class that extends RecyclerView.ViewHolder
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private LayoutInflater inflater;

    private Context context;
    //Collections.emptyList() Returns the empty list (immutable).
    // why use Collection.emptyList();?
    // because to declare data as empty because List is interface and
    // take either ArrayList or LinkedList
    // you can also do List<InformationRow>  data;
    List<InformationRow>  data = Collections.emptyList();

    public MyAdapter(Context context, List<InformationRow> data){
        // pass the context getActivity from
        //  adapter = new MyAdapter(getActivity(), getData());
        // in NavigationDrawerFragment.java
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    // delete an Item from the List
    public void deleteItem(int position){
        // remove the item from the ArrayList
        data.remove(position);

        // to notify the view that we deleted something
        // from the data
        notifyItemRemoved(position);
    }

    // makes the recycling of view for example list
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // get what row will be recycled
        View view = inflater.inflate(R.layout.custom_row, viewGroup, false);
        // gets the view to be bind with data
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    // this will bind the data to your elements inside the row of list for
    // onBindViewHolder for the data to be Bind with ViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        InformationRow current = data.get(position);

        holder.title.setText(current.title);

        holder.icon.setImageResource(current.iconId);

//        holder.icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Item Clicked "+position,Toast.LENGTH_SHORT).show();
//
//                deleteItem(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        // size of list to be displayed
        return data.size();
    }

    // Represents the Items of a Row in the recyclerview

    //class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

            // click listener for the deletion
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // we use the getPosition() because
                    // it is the recommended by the docs
                    //https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#onBindViewHolder(VH, int)
                    //Note that unlike ListView, RecyclerView will not call this method again if the position of the item changes in the data set unless the item itself is invalidated or the new position cannot be determined. For this reason, you should only use the position parameter while acquiring the related data item inside this method and should not keep a copy of it. If you need the position of an item later on (e.g. in a click listener), use getPosition() which will have the updated position.
                    deleteItem(getPosition());
                }
            });

            //icon.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            deleteItem(getPosition());
//        }
    }
}
