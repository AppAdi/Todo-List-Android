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

public class TambahActivity extends AppCompatActivity {

    private EditText etNama, etDeskripsi, etTanggal, etWaktu;
    private Button btnSimpan, btnKembali;
    private String nama, deskripsi, tanggal, waktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etTanggal = findViewById(R.id.et_tanggal);
        etWaktu = findViewById(R.id.et_waktu);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnKembali = findViewById(R.id.btn_kembali);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        etWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TambahActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                DatePickerDialog dialog = new DatePickerDialog(TambahActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                deskripsi = etDeskripsi.getText().toString();
                tanggal = etTanggal.getText().toString();
                waktu = etWaktu.getText().toString();

                if (nama.trim().equals("")){
                    etNama.setError("Harus Diisi");
                }else if (deskripsi.trim().equals("")){
                    etDeskripsi.setError("Harus Diisi");
                }else if (tanggal.trim().equals("")){
                    etTanggal.setError("Harus Diisi");
                }else if (waktu.trim().equals("")){
                    etWaktu.setError("Harus Diisi");
                }else{
                    createData();
                    Intent intent = new Intent(TambahActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kembali = new Intent(TambahActivity.this, MainActivity.class);
                startActivity(kembali);
            }
        });

    }

    private void createData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = ardData.ardCreateData(nama, deskripsi, tanggal,waktu);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}