package com.appadi.todoapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appadi.todoapp.API.APIRequestData;
import com.appadi.todoapp.API.RetroServer;
import com.appadi.todoapp.Activity.DetailActivity;
import com.appadi.todoapp.Activity.MainActivity;
import com.appadi.todoapp.Activity.UbahActivity;
import com.appadi.todoapp.Model.DataModel;
import com.appadi.todoapp.Model.ResponseModel;
import com.appadi.todoapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listTodo;
    private int idTodo;
    private String statusTodo;

    public AdapterData(Context ctx, List<DataModel> listData){
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public AdapterData.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterData.HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvDeskripsi.setText(dm.getDeskripsi());
        holder.tvTanggal.setText(dm.getTanggal());
        holder.tvWaktu.setText(dm.getWaktu());
        holder.tvStatus.setText(dm.getStatus());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvNama, tvDeskripsi, tvTanggal, tvWaktu, tvStatus;
        RadioButton btnSelesai;
        CardView carditem;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvWaktu = itemView.findViewById(R.id.tv_waktu);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnSelesai = itemView.findViewById(R.id.btn_selesai);
            carditem = itemView.findViewById(R.id.cardlist_item);

            statusTodo = (tvStatus.getText().toString());
            if (statusTodo.equals("Selesai")) {
                Toast.makeText(ctx, "Ada Yang Selesai", Toast.LENGTH_LONG).show();
                carditem.setCardBackgroundColor(Color.LTGRAY);
            }

            btnSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idTodo = Integer.parseInt(tvId.getText().toString());
                    completeTask();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idTodo = Integer.parseInt(tvId.getText().toString());

                    getData();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi");
                    dialogPesan.setCancelable(true);

                    idTodo = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) ctx).retrieveData();
                                }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();
                    return false;
                }
            });
        }

        private void deleteData() {
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idTodo);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode :" + kode + "| Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void completeTask() {
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> completetask = ardData.ardCompleteData(idTodo);

            completetask.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

//                    Toast.makeText(ctx, "Kode :" + kode + "| Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                    Intent tes = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(tes);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData() {
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idTodo);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listTodo = response.body().getData();

                    int varidTodo = listTodo.get(0).getId();
                    String varNama = listTodo.get(0).getNama();
                    String varDeskripsi = listTodo.get(0).getDeskripsi();
                    String varTanggal = listTodo.get(0).getTanggal();
                    String varWaktu = listTodo.get(0).getWaktu();
                    String varStatus = listTodo.get(0).getStatus();

//                    Toast.makeText(ctx, "Kode :"+kode+ "| Pesan : "+pesan+ " | "+varidTodo+
//                            " | "+varNamaLaundry+" | "+varAlamatLaundry+" | "+varTeleponLaundry, Toast.LENGTH_SHORT).show();

                    Intent kirim = new Intent(ctx, DetailActivity.class);
                    kirim.putExtra("xId", varidTodo);
                    kirim.putExtra("xNama", varNama);
                    kirim.putExtra("xDeskripsi", varDeskripsi);
                    kirim.putExtra("xTanggal", varTanggal);
                    kirim.putExtra("xWaktu", varWaktu);
                    kirim.putExtra("xStatus", varStatus);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
