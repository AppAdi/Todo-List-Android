package com.appadi.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appadi.todoapp.API.APIRequestData;
import com.appadi.todoapp.API.RetroServer;
import com.appadi.todoapp.Model.ResponseModel;
import com.appadi.todoapp.R;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {

    private int xId;
    private String xNama, xDeskripsi, xTanggal, xWaktu, xStatus;
    private String yNama, yDeskripsi, yTanggal, yWaktu, yStatus;
    private EditText etNama, etDeskripsi, etTanggal, etWaktu;
    private Button btnUbah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xNama = terima.getStringExtra("xNama");
        xDeskripsi = terima.getStringExtra("xDeskripsi");
        xTanggal = terima.getStringExtra("xTanggal");
        xWaktu = terima.getStringExtra("xWaktu");
        xStatus = terima.getStringExtra("xStatus");

        etNama = findViewById(R.id.et_nama);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etTanggal = findViewById(R.id.et_tanggal);
        etWaktu = findViewById(R.id.et_waktu);
        btnUbah = findViewById(R.id.btn_ubah);

        etNama.setText(xNama);
        etDeskripsi.setText(xDeskripsi);
        etTanggal.setText(xTanggal);
        etWaktu.setText(xWaktu);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        etWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(UbahActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String jam = hour+":"+minute;
                        etWaktu.setText(jam);
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UbahActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = year+"/"+month+"/"+day;
                        etTanggal.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNama = etNama.getText().toString();
                yDeskripsi = etDeskripsi.getText().toString();
                yTanggal = etTanggal.getText().toString();
                yWaktu = etWaktu.getText().toString();
//                if (xStatus == "Aktif"){
//                    yStatus = "Selesai";
//                }else{
//                    yStatus = "Aktif";
//                }
                updateData();
                Intent intent = new Intent(UbahActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId, yNama, yDeskripsi,yTanggal,yWaktu);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                int kode = response.body().getKode();
//                String pesan = response.body().getPesan();

//                Toast.makeText(UbahActivity.this, "Kode :"+kode+ "| Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}