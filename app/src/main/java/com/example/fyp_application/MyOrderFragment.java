package com.example.fyp_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends Fragment {

    public MyOrderFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrderRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        myOrderRecyclerView = view.findViewById(R.id.my_order_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.moistorizer, 2, "Moistorizer", "Delivered on Mon, 15th JAN 2013"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.black_head, 2, "Black Head Remover", "Delivered on Mon, 15th FEB 2019"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.oil_free, 2, "Oil free Massager", "Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.faceoil, 2, "Face Oil", "Delivered on Mon, 25th AUG 2020"));

        MyOrderAdaptor myOrderAdaptor = new MyOrderAdaptor(myOrderItemModelList);
        myOrderRecyclerView.setAdapter(myOrderAdaptor);
        myOrderAdaptor.notifyDataSetChanged();
        return view;
    }
}
