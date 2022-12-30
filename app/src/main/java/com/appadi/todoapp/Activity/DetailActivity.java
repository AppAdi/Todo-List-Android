package com.appadi.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appadi.todoapp.API.APIRequestData;
import com.appadi.todoapp.API.RetroServer;
import com.appadi.todoapp.Model.ResponseModel;
import com.appadi.todoapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    int xId;
    private String xNama, xDeskripsi, xTanggal, xWaktu, xStatus;
    private TextView etNama, etDeskripsi, etTanggal, etWaktu;
    Button btnTask, btnDelete, btnKembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xNama = terima.getStringExtra("xNama");
        xDeskripsi = terima.getStringExtra("xDeskripsi");
        xTanggal = terima.getStringExtra("xTanggal");
        xWaktu = terima.getStringExtra("xWaktu");
        xStatus = terima.getStringExtra("xStatus");

        etNama = findViewById(R.id.tv_nama);
        etDeskripsi = findViewById(R.id.tv_deskripsi);
        etTanggal = findViewById(R.id.tv_tanggal);
        etWaktu = findViewById(R.id.tv_waktu);
        btnTask = findViewById(R.id.btn_task);
        btnDelete = findViewById(R.id.btn_delete);
        btnKembali = findViewById(R.id.btn_kembali);

        etNama.setText(xNama);
        etDeskripsi.setText(xDeskripsi);
        etTanggal.setText(xTanggal);
        etWaktu.setText(xWaktu);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kembali = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(kembali);
            }
        });

        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendData() {
        Intent kirim = new Intent(DetailActivity.this, UbahActivity.class);
        kirim.putExtra("xId", xId);
        kirim.putExtra("xNama", xNama);
        kirim.putExtra("xDeskripsi", xDeskripsi);
        kirim.putExtra("xTanggal", xTanggal);
        kirim.putExtra("xWaktu", xWaktu);
        kirim.putExtra("xStatus", xStatus);
        startActivity(kirim);
    }

    private void deleteData() {
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> hapusData = ardData.ardDeleteData(xId);

        hapusData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}