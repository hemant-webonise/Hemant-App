package com.example.webonise.blooddonation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        btnImage.setImageBitmap(BitmapFactory.decodeFile(historyList.get(i).getImage()));
       /* btnImage.setImageBitmap(getRoundedShape(BitmapFactory.decodeFile(historyList.get(i).getImage()),200));
*/
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tvLocation.setText(context.getString(R.string.location_tv)+historyList.get(i).getLocation());
        tvDate.setText(context.getString(R.string.date_tv)+historyList.get(i).getDate());
        ImageButton imgButton = (ImageButton) view.findViewById(R.id.deletor);


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,context.getString(R.string.future_functionality)+"Value : "+i,Toast.LENGTH_LONG).show();
                HistoryDBAdapter historyDBAdapter = new HistoryDBAdapter(context);
                int id = historyList.get(i).getId();
                historyDBAdapter.deleteCertainDetail(id);
                historyDBAdapter.close();
                ((HistoryActivityWithSQL) context).onDeleted();
            }
        });
        return view;
    }


    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }



    public interface CallBack {
        void onDeleted();
    }

}
