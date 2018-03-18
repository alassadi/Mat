package com.company.mat;

/**
 * Created by ivana on 3/12/2018.
 */

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.company.mat.Model.RestaurantMenuItem;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<RestaurantMenuItem>> expandableListDetail;
    private LongPress activity;
    private TextView lastSelected;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<RestaurantMenuItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        activity = (LongPress) context;
    }

    @Override
    public RestaurantMenuItem getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = getChild(listPosition, expandedListPosition).getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item, null);
        }
        final TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        TextView priceView = convertView.findViewById(R.id.expandedListItemPrice);
        priceView.setText(this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).getPrice());
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_listview_group, null);
        }
        final TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, listTitleTextView.getText(), Toast.LENGTH_SHORT).show();

                activity.longPressedCategory(listTitle);
                if (lastSelected != null) {
                    lastSelected.setTypeface(Typeface.DEFAULT);
                }
                lastSelected = listTitleTextView;
                listTitleTextView.setTypeface(null, Typeface.BOLD);

                return true;
            }
        });
        listTitleTextView.setText(listTitle);
        listTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded) {
                    ((ExpandableListView) parent).collapseGroup(listPosition);
                } else {
                    ((ExpandableListView) parent).expandGroup(listPosition);
                }
            }
        });


        ImageButton remove = convertView.findViewById(R.id.deleteCategory);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.removeCategory(listTitle);
            }
        });


        if (listTitle.equalsIgnoreCase("add category")) {
            remove.setVisibility(View.GONE);
        }
        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public interface LongPress {
        void longPressedCategory(String name);

        void removeCategory(String category);
    }

}
