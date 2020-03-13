package com.example;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.agrosup_app.R;
import com.example.entities.Treatment;

import org.w3c.dom.Text;

import java.util.List;

public class TreatmentsListAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    List<Treatment> treatments;

    public TreatmentsListAdapter(Activity activity, List<Treatment> treatments) {
        inflater = activity.getLayoutInflater();
        this.treatments = treatments;
    }

    @Override
    public int getGroupCount() {
        return treatments.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return treatments.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return treatments.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fields_group, null);
        }

        Treatment treatment = (Treatment) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(treatment.getStartDate() +"\t" + treatment.getFieldId());
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.fields_child,null);
        }
        Treatment treatment = (Treatment) getGroup(groupPosition);
        TextView tv = (TextView) convertView.findViewById(R.id.fields_fieldsLVTV);
        String text =   "Start: "+treatment.getStartDate()+"\n"+
                "Koniec: "+treatment.getEndDate()+"\n"+
                "Operator: "+treatment.getOperatorId()+"\n"+
                "Powierzchnia: "+(treatment.getArea()/100)+" ha\n"+
                "Zużyte paliwo: "+treatment.getCombustedFuel()+" l\n"+
                "Maszyna: "+treatment.getMachineName();
        tv.setText(text);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
