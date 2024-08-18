package com.example.hancafe.Controller.Activity.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hancafe.Controller.Activity.Adapter.PromotionAdapter;
import com.example.hancafe.Controller.Activity.Dialog.AddPromotionDialogFragment;
import com.example.hancafe.Model.Promotion;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class PromotionFragment extends Fragment{
    private RecyclerView recyclerView;
    Button btnAddKM;
    private PromotionAdapter promotionAdapter;
    private List<Promotion> promotionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_promotions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Khởi tạo danh sách khuyến mãi và adapter
        promotionList = new ArrayList<>();
        // Thay đổi khởi tạo Adapter trong PromotionFragment
        promotionAdapter = new PromotionAdapter(getActivity(), promotionList);

        recyclerView.setAdapter(promotionAdapter);

        // Thêm listener để lắng nghe sự thay đổi trên Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                promotionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Promotion promotion = snapshot.getValue(Promotion.class);
                    promotionList.add(promotion);
                }
                promotionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        // Thêm nút "Thêm mới khuyến mãi"
        btnAddKM = view.findViewById(R.id.btnAddKM);
        btnAddKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi nút "Thêm mới khuyến mãi" được click
                // Ví dụ: Chuyển sang màn hình tạo mới khuyến mãi
                //startActivity(new Intent(getActivity(), AddPromotionActivity.class));
                showAddPromotionDialog();
            }
        });

        return view;
    }

    private void showAddPromotionDialog() {
        AddPromotionDialogFragment dialog = new AddPromotionDialogFragment();
        dialog.setOnAddPromotionListener(new AddPromotionDialogFragment.OnAddPromotionListener() {
            @Override
            public void onAddPromotion(Promotion promotion) {
                // Thêm dữ liệu vào Firebase
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");
                // Đảm bảo bạn cũng lưu trường code khi thêm dữ liệu
                String key = databaseReference.push().getKey();
                promotion.setCode(key); // Sử dụng key làm mã khuyến mãi (có thể tùy chỉnh theo yêu cầu của bạn)
                databaseReference.child(key).setValue(promotion);
            }
        });
        dialog.show(getChildFragmentManager(), "AddPromotionDialogFragment");
    }


}