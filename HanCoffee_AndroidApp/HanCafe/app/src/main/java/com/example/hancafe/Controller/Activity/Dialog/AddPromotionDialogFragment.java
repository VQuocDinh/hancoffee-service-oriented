package com.example.hancafe.Controller.Activity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hancafe.Model.Promotion;
import com.example.hancafe.R;

import java.util.Random;

public class AddPromotionDialogFragment extends DialogFragment {

    private EditText editTextName;
    private EditText editTextDiscount;
    private EditText editTextDescription;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private EditText editTextCode;
    private OnAddPromotionListener listener;

    public interface OnAddPromotionListener {
        void onAddPromotion(Promotion promotion);
    }

    public void setOnAddPromotionListener(OnAddPromotionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_promotion, null);

        editTextName = view.findViewById(R.id.edit_text_name);
        editTextDiscount = view.findViewById(R.id.edit_text_discount);
        editTextDescription = view.findViewById(R.id.edit_text_description);
        editTextStartTime = view.findViewById(R.id.edit_text_start_time);
        editTextEndTime = view.findViewById(R.id.edit_text_end_time);

        builder.setView(view)
                .setTitle("Thêm mới khuyến mãi")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng nhấn nút "Thêm"
                        String name = editTextName.getText().toString().trim();
                        String discount = editTextDiscount.getText().toString().trim();
                        String description = editTextDescription.getText().toString().trim();
                        String startTime = editTextStartTime.getText().toString().trim();
                        String endTime = editTextEndTime.getText().toString().trim();
                        String code = generatePromotionCode(); // Tạo mã khuyến mãi ngẫu nhiên

                        // Tạo đối tượng Promotion từ thông tin nhập vào
                        Promotion promotion = new Promotion(name, discount, description, startTime, endTime, code); // Thêm mã khuyến mãi

                        // Gọi phương thức onAddPromotion() của listener và truyền đối tượng Promotion
                        listener.onAddPromotion(promotion);

                        // Đóng dialog
                        dismiss();
                    }
                });

        return builder.create();
    }
    private String generatePromotionCode() {
        // Mã gồm 6 ký tự số
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(numbers.length());
            sb.append(numbers.charAt(index));
        }
        return sb.toString();
    }


}
