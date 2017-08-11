package com.example.android.dor.adapter;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.dor.R;
import com.example.android.dor.objectClass.ReportClass;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolderAdapter> {

    private LayoutInflater layoutInflater;
    private ArrayList<ReportClass> reportDetail=new ArrayList<>();
    int images[] = {R.drawable.pdf, R.drawable.word, R.drawable.excel, R.drawable.default_file};

    public CustomAdapter(Context context, ArrayList<ReportClass> reportList){
        layoutInflater=LayoutInflater.from(context);
        setReportList(reportList);
    }

    public  void setReportList(ArrayList<ReportClass> reportList){
        this.reportDetail = reportList;
        notifyItemRangeChanged(0, reportList.size());
    }

    @Override
    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        ViewHolderAdapter viewHolder =new ViewHolderAdapter(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAdapter holder, int position) {
        final ReportClass currentReport = reportDetail.get(position);
        holder.names.setText(currentReport.getName());
        holder.dates.setText(currentReport.getDate());
        String type = currentReport.getReportType();
        Log.d("Activity onBind", type);
        if(type.equals("doc") || type.equals("docx")){
            holder.img.setImageResource(images[1]);
        }
        else if(type.equals("pdf")){
            holder.img.setImageResource(images[0]);
        }
        else if(type.equals("xls") || type.equals("xlsx")){
            holder.img.setImageResource(images[2]);
        }
        else{
            holder.img.setImageResource(images[3]);
        }

        holder.dwnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(currentReport.getReportUrl()));
                request.setTitle(currentReport.getName()+"."+currentReport.getReportType());
                request.setDescription("File is being downloaded...");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,(currentReport.getName()+"."+currentReport.getReportType()));

                DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d("Activity report detail", ""+reportDetail.size());
        return reportDetail.size();
    }

    static class ViewHolderAdapter extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView names;
        private TextView dates;
        private ImageButton dwnload;

        public ViewHolderAdapter(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.imageView4);
            names=(TextView)itemView.findViewById(R.id.textView5);
            dates=(TextView)itemView.findViewById(R.id.textView6);
            dwnload=(ImageButton)itemView.findViewById(R.id.imageButton3);
        }
    }
}