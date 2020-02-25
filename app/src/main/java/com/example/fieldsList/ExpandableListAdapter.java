package com.example.fieldsList;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrosup_app.R;
import com.example.database.FieldWithParcels;
import com.example.entities.Parcel;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final SparseArray<FieldWithParcels> groups = new SparseArray<>();
    private LayoutInflater inflater;
    private Activity activity;

    public ExpandableListAdapter(Activity act, List<FieldWithParcels> groups) {
        activity = act;
        inflater = act.getLayoutInflater();
        int i=0;
        for(FieldWithParcels item: groups) {
            this.groups.put(i++,item);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).parcels.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {

        Parcel parcel = (Parcel) getChild(groupPosition, childPosition);
        final String children = parcel.getParcelNumber()+ " - "+ parcel.getCultivatedArea()+" ar";
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fields_child, null);
        }

        if(childPosition == 0) {
            FieldWithParcels field = (FieldWithParcels) getGroup(groupPosition);
            TextView tv =(TextView) convertView.findViewById(R.id.fields_fieldsDescTV);
            tv.setText("Uprawa: "+field.getPlant());
            tv.setVisibility(View.VISIBLE);
        } else {
            TextView tv =(TextView) convertView.findViewById(R.id.fields_fieldsDescTV);
            tv.setVisibility(View.GONE);
        }

        text = (TextView) convertView.findViewById(R.id.fields_fieldsLVTV);
        text.setText(children);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, children,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).parcels.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        FieldWithParcels field = (FieldWithParcels) getGroup(groupPosition);
        Toast.makeText(activity, field.getPlant(),
                Toast.LENGTH_SHORT).show();
        TextView tv = new TextView(activity);
        tv.setText(field.getPlant());

    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fields_group, null);
        }
        FieldWithParcels field = (FieldWithParcels) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(field.toString());
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
