package com.websarva.wings.android.dasenapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NormalLineupFragment extends Fragment {

    private TextView[] names = new TextView[9];
    private TextView[] positions = new TextView[9];

    public static NormalLineupFragment newInstance() {

        return new NormalLineupFragment();
    }

    // レイアウト紐付け
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_normal_lineup, container, false);
        bindLayout(v);
        setLayout();

        return v;
    }


    private void bindLayout(View v) {

        names[0] = v.findViewById(R.id.name1_normal);
        names[1] = v.findViewById(R.id.name2_normal);
        names[2] = v.findViewById(R.id.name3_normal);
        names[3] = v.findViewById(R.id.name4_normal);
        names[4] = v.findViewById(R.id.name5_normal);
        names[5] = v.findViewById(R.id.name6_normal);
        names[6] = v.findViewById(R.id.name7_normal);
        names[7] = v.findViewById(R.id.name8_normal);
        names[8] = v.findViewById(R.id.name9_normal);

        positions[0] = v.findViewById(R.id.position1_normal);
        positions[1] = v.findViewById(R.id.position2_normal);
        positions[2] = v.findViewById(R.id.position3_normal);
        positions[3] = v.findViewById(R.id.position4_normal);
        positions[4] = v.findViewById(R.id.position5_normal);
        positions[5] = v.findViewById(R.id.position6_normal);
        positions[6] = v.findViewById(R.id.position7_normal);
        positions[7] = v.findViewById(R.id.position8_normal);
        positions[8] = v.findViewById(R.id.position9_normal);
    }

    private void setLayout() {
        for (int i = 0; i < 9; i++) {
            names[i].setText(CachedPlayerNamesInfo.instance.getNameNormal(i));
            positions[i].setText(CachedPlayerPositionsInfo.instance.getPositionNormal(i));
        }
    }
}
