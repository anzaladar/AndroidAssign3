package com.example.pucit.recyclerviewa;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private static final int TYPE=1;
    Context context;
    private  List<books> listRecyclerItem;
    ProgressDialog progress;
    private String bookName;


    public RVAdapter(Context context, List<books> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem=listRecyclerItem;
        progress = new ProgressDialog(this.context);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(true);
        progress.setIndeterminate(true);
        progress.setMessage("Downloading");
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:
            default:
                View layoutView= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
                ViewHolder holder = new ViewHolder(layoutView);
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ViewHolder vh = holder;
        final books book = listRecyclerItem.get(position);
        holder.title.setText(book.getTitle());
        holder.info.setText(book.getInfo());
        holder.level.setText(book.getLevel());
        Picasso.get().load("https://raw.githubusercontent.com/revolunet/PythonBooks/master/" + book.getCover()).into(holder.cover);
        String url = book.getUrl();
        String urlD = url.substring(url.length() - 3);
        if (urlD.equals("zip") || urlD.equals("pdf")) {
            holder.btn.setText("DOWNLOAD");
        } else {
            holder.btn.setText("READ ONLINE");
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = book.getUrl();
                String urlD = url.substring(url.length() - 3);
                if (urlD.equals("pdf") || urlD.equals("zip")) {
                    bookName = book.getTitle();
                    new DownloadFile().execute(url);
                } else {
                    Uri uri = Uri.parse(book.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }


            }
        });
    }



    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView url;
        public TextView level;
        public TextView info;
        public ImageView cover;
        public Button btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
           // url = itemView.findViewById(R.id.url);
            level = itemView.findViewById(R.id.level);
            cover = itemView.findViewById(R.id.cover);
            info = itemView.findViewById(R.id.info);
            btn=itemView.findViewById(R.id.btn);
        }
    }

    class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected String doInBackground(String... fileurl) {
            int count;
            try {
                URL url = new URL(fileurl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                final int PERMISSION_REQUEST_CODE = 12345;
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+bookName+".pdf");
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error display: ", e.getMessage());
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progressD)
        {
            super.onProgressUpdate(progressD);
            progress.setIndeterminate(false);
            progress.setMax(100);
            progress.setProgress(progressD[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (result != null)
                Toast.makeText(context,"DOWNLOADING ERROR: "+result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"FILE DOWNLOADED", Toast.LENGTH_LONG).show();
        }
    }

}
