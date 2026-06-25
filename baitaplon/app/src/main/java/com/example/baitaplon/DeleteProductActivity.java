package com.example.baitaplon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteProductActivity extends AppCompatActivity {

    EditText edtId;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        edtId = findViewById(R.id.edtId);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(v -> {

            int id =
                    Integer.parseInt(
                            edtId.getText().toString()
                    );

            ProductDAO dao =
                    new ProductDAO(this);

            boolean result =
                    dao.deleteProduct(id);

            if(result){

                Toast.makeText(
                        this,
                        "Xóa thành công",
                        Toast.LENGTH_SHORT
                ).show();

            }else{

                Toast.makeText(
                        this,
                        "Không tìm thấy sản phẩm",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });
    }
}