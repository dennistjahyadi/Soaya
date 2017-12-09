package com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.adapter.UserGetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 7/22/2016.
 */
public class FriendListAdapter extends ArrayAdapter<UserGetter> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private List<UserGetter> items;
    private List<UserGetter> itemsAll;
    private List<UserGetter> suggestions;
    private int viewResourceId;
    Context context;

    public FriendListAdapter(Context context, int viewResourceId, List<UserGetter> items) {
        super(context, viewResourceId, items);
        this.context = context;
        this.items = items;
        this.itemsAll = items;
        this.suggestions = new ArrayList<UserGetter>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        UserGetter customer = items.get(position);
        if (customer != null) {
            ImageView imageView;
            TextView textViewUsername,textViewName;
            imageView = (ImageView)v.findViewById(R.id.imageViewPhoto);
            textViewUsername = (TextView)v.findViewById(R.id.textViewUsername);
            textViewName = (TextView)v.findViewById(R.id.textViewName);
            if(customer.getProfileUrl()==null|| customer.getProfileUrl()==""||customer.getProfileUrl().isEmpty())
            {

                Picasso.with(context).load(R.drawable.default_user_icon_profile).into(imageView);

            }else
            {
                Picasso.with(context).load(customer.getProfileUrl()).transform(new CircleTransform()).into(imageView);
            }
            textViewUsername.setText(customer.getUsername());
            textViewName.setText(customer.getName());

            /*  TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);

            if (customerNameLabel != null) {
             Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(customer.getName());
            }*/
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((UserGetter)(resultValue)).getUsername();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (UserGetter customer : itemsAll) {
                    if(customer.getUsername().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<UserGetter> filteredList = (ArrayList<UserGetter>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (UserGetter c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}