package com.example.android.dor.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.android.dor.R;
import com.example.android.dor.activity.ReportsActivity;
import com.example.android.dor.objectClass.NotificationClass;
import com.example.android.dor.objectClass.ReportClass;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by darip on 23-06-2017.
 */

public class CustomNotificationAdapter extends RecyclerView.Adapter<CustomNotificationAdapter.ViewHolderAdapter> {

    private LayoutInflater layoutInflater;
    private ArrayList<NotificationClass> notificationDetail=new ArrayList<>();
    int images[] = {R.drawable.r_logo, R.drawable.v_logo, R.drawable.c_logo, R.drawable.default_file};
    Context context;

//    CustomNotificationAdapter.ViewHolderAdapter.Callbacks callbacks;

    public CustomNotificationAdapter(Context context, ArrayList<NotificationClass> notificationList){
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        setNotificationList(notificationList);
    }

    public  void setNotificationList(ArrayList<NotificationClass> notificationList){
        this.notificationDetail = notificationList;
        notifyItemRangeChanged(0, notificationList.size());
    }

    @Override
    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_notification_layout, parent, false);

        ViewHolderAdapter viewHolder =new ViewHolderAdapter(view, notificationDetail, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAdapter holder, int position) {
        final NotificationClass currentNotification = notificationDetail.get(position);
        holder.names.setText(currentNotification.getNotiName()+"("+currentNotification.getNotiDetails()+")");
        holder.dates.setText(currentNotification.getNotiTimestamp());
        String type = currentNotification.getNotiType();
        Log.d("Activity onBind", type);
        if(type.equals("R")){
            holder.img.setImageResource(images[0]);
        }
        else if(type.equals("V")){
            holder.img.setImageResource(images[1]);
        }
        else if(type.equals("C")){
            holder.img.setImageResource(images[2]);
        }
        else{
            holder.img.setImageResource(images[3]);
        }
    }

    @Override
    public int getItemCount() {

        Log.d("Activity report detail", ""+notificationDetail.size());
        return notificationDetail.size();
    }

//    public NotificationClass getClickNotiType(int pos){
//        return notificationDetail.get(pos);
//    }
   public static class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img;
        private TextView names;
        private TextView dates;
        Context context;
    ArrayList<NotificationClass> notiDetail=new ArrayList<>();
//        Callbacks callbacks;
////        private ImageButton dwnload;
//        public interface Callbacks{
//            public void onItemSelect(View v,int pos);
//        }
        public ViewHolderAdapter(View itemView, ArrayList<NotificationClass> notificationList, Context context) {
            super(itemView);
//            this.callbacks = callbacks;
            this.context = context;
            this.notiDetail = notificationList;
            img = (ImageView)itemView.findViewById(R.id.imageView5);
            names=(TextView)itemView.findViewById(R.id.textView7);
            dates=(TextView)itemView.findViewById(R.id.textView9);
            itemView.setOnClickListener(this);
//            dwnload=(ImageButton)itemView.findViewById(R.id.imageButton3);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            NotificationClass currentNotification = this.notiDetail.get(position);
            String s = currentNotification.getNotiType();
            if(s.equals("R")){
                Intent intent = new Intent(this.context, ReportsActivity.class);
                this.context.startActivity(intent);
            }
//            Toast.makeText(this.context,"getnotitype:"+ s, Toast.LENGTH_SHORT).show();
        }
    }
}
