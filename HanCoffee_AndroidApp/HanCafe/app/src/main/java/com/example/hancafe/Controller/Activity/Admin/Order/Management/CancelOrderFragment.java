package com.example.hancafe.Controller.Activity.Admin.Order.Management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.R;

public class CancelOrderFragment extends Fragment {
    RecyclerView rcvCategoryOrderManagement;
    LinearLayout lnEmpty;
    TextView tvEmpty;
    private static final int STATUS_CANCEL = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_category_order_management, container, false);

        rcvCategoryOrderManagement = view.findViewById(R.id.rcvOrderManagementCategory);

        lnEmpty = view.findViewById(R.id.lnEmpty);

        tvEmpty = view.findViewById(R.id.tvEmpty);
        String title = getResources().getString(R.string.title_order_status_cancel_empty);
        tvEmpty.setText(title);

        HelperOrderManagement.loadDataOrderManagement(getContext(), rcvCategoryOrderManagement, lnEmpty, STATUS_CANCEL);

        return view;
    }

}