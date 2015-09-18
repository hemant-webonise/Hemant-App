package com.example.webonise.blooddonation.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webonise.blooddonation.FillHistoryActivity;
import com.example.webonise.blooddonation.HistoryActivityWithSQL;
import com.example.webonise.blooddonation.R;
import com.example.webonise.blooddonation.model.History;

import java.util.List;

/**
 * Created by webonise on 10/9/15.
 */
public class HistoryAdapter extends BaseAdapter{

    List<History> historyList;
    Context context;
    HistoryDBAdapter historyDBAdapter;

    static Context mcontext;

    public HistoryAdapter(List<History> historyList, HistoryActivityWithSQL context) {
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_history, null);
        }
        TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        ImageButton  btnImage=(ImageButton) view.findViewById(R.id.btnImage);

        btnImage.setImageBitmap(getRoundedShape(BitmapFactory.decodeFile(historyList.get(i).getImage()),200));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update = new Intent(context, FillHistoryActivity.class);
                historyDBAdapter = new HistoryDBAdapter(context);
                update.putExtra("update",false);
                update.putExtra("ID",historyList.get(i).getId());
                context.startActivity(update);
            }
        });
        tvLocation.setText(historyList.get(i).getLocation());
        tvDate.setText(historyList.get(i).getDate());
        ImageButton imgButton = (ImageButton) view.findViewById(R.id.deletor);

        deleteCertainElementOnClick(i, imgButton);
        return view;
    }

    private void deleteCertainElementOnClick(final int i, ImageButton imgButton) {
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.delete_title))
                        .setMessage(context.getString(R.string.sure_delete))
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(context, context.getString(R.string.future_functionality) + "Value : " + i, Toast.LENGTH_LONG).show();
                                historyDBAdapter = new HistoryDBAdapter(context);
                                int id = historyList.get(i).getId();
                                historyDBAdapter.deleteCertainDetail(id);
                                historyDBAdapter.close();
                                ((HistoryActivityWithSQL) context).onDeleted();
                            }
                        }).create().show();
            }
        });
    }


    public Bitmap getRoundedShape(Bitmap scaleBitmapImage, int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,((float) targetHeight - 1) / 2,(Math.min(((float) targetWidth),((float) targetHeight)) / 2),Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        if (sourceBitmap!=null) {
            canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight), null);
            return targetBitmap;
        }
        else {
            return getRoundedShape(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.pick), 200);
        }
    }



    public interface CallBack {
        void onDeleted();
    }

}
