package com.retroquack.kwak123.mymovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.HeterogeneousExpandableList;

import com.retroquack.kwak123.mymovies.R;
import com.retroquack.kwak123.mymovies.objects.DetailClass;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kwak123 on 1/2/2017.
 */

public abstract class DetailExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mListHeaders;
    private HashMap<String, List<DetailClass>> mListItems;

    public DetailExpandableAdapter(Context context, List<String> listHeaders,
                                   HashMap<String, List<DetailClass>> listItems) {
        mContext = context;
        mListHeaders = listHeaders;
        mListItems = listItems;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListItems.get(mListHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * TODO: Implement the detail item layout.
     */

//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
//                             View convertView, ViewGroup parent) {
//        final DetailClass detailItem = (DetailClass) getChild(groupPosition, childPosition);
//
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.detail_item, null);
//        }
//
//        return null;
//    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupCount() {
        return mListHeaders.size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * TODO: Do I need to fully implement this method?
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupName = mListHeaders.get(groupPosition);
        List<DetailClass> groupChildren = mListItems.get(groupName);

        return groupChildren.size();
    }
}
