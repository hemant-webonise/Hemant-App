package com.example.webonise.blooddonation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webonise.blooddonation.adapter.HistoryDBAdapter;
import com.example.webonise.blooddonation.model.History;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FillHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    DatePicker datePicker;
    Button btnAddHistory;
    EditText etLocation;
    TextView tvDate;
    private static int LOAD_IMAGE_RESULTS = 1;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageButton btnImage;
    private int pYear;
    private int pMonth;
    private int pDay;
    /** This integer will uniquely define the dialog to be used for displaying date picker.*/
    static final int DATE_DIALOG_ID = 0;
    private boolean isUpdate=true;
    int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_history);
        Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        isUpdate=bundle.getBoolean("update");
        Log.w("IS", String.valueOf(isUpdate));
        if(!isUpdate)
        {
            id= Integer.parseInt(getIntent().getExtras().getString("ID"));
            HistoryDBAdapter personDatabaseHelper = new HistoryDBAdapter(this);
            History history = new History();
            history.setLocation("UpdatedforId"+id);
            history.setDate(tvDate.getText().toString());
            history.setImage(imagePath);
            personDatabaseHelper.updateCertainDetail(id);
            personDatabaseHelper.close();

        }
        else {
        toolbar.setTitle("History");
        toolbar.setSubtitle("Lets add some good deeds..");
        setSupportActionBar(toolbar);
        initialize();
        setListeners();
    }
    }




    private void setListeners() {
        setDailog();
        btnAddHistory.setOnClickListener(this);
        btnImage.setOnClickListener(this);
    }

    private void setDailog() {
        tvDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // To show current date in the datepicker
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(
                        FillHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker,
                                          int selectedyear, int selectedmonth,
                                          int selectedday) {

                        mcurrentDate.set(Calendar.YEAR, selectedyear);
                        mcurrentDate.set(Calendar.MONTH, selectedmonth);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH,
                                selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                getResources().getString(
                                        R.string.date_card_formate),
                                Locale.US);

                        tvDate.setText(sdf.format(mcurrentDate.getTime()));
                    }
                }, mYear, mMonth, mDay);

                mDatePicker.setTitle(getResources().getString(R.string.alert_date_select));
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private void initialize() {
       /* datePicker=(DatePicker) findViewById(R.id.dpDate);*/

      /*  datePicker.setMaxDate(Calendar.DATE);
*/      tvDate=(TextView) findViewById(R.id.tvDate);
        btnAddHistory=(Button) findViewById(R.id.btnAddHistory);
        btnImage=(ImageButton) findViewById(R.id.btnImage);
        etLocation=(EditText)findViewById(R.id.etLocation);
    }
    String imagePath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagePath= String.valueOf(thumbnail);
        btnImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        imagePath=selectedImagePath;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        btnImage.setImageBitmap(bm);
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(FillHistoryActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.btnImage :
               /* Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE_RESULTS);*/
                selectImage();

                break;
            case R.id.btnAddHistory :
                Toast.makeText(this,/*String.valueOf(datePicker.getDayOfMonth())+*/imagePath,Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(etLocation.getText().toString())) {
                    etLocation.setError(getString(R.string.error_location));
                } else {
                    HistoryDBAdapter personDatabaseHelper = new HistoryDBAdapter(this);
                    History history = new History();
                    history.setLocation(etLocation.getText().toString());
//                    String date = String.valueOf(new StringBuilder().append(datePicker.getYear()).append(" ").append("-").append(datePicker.getMonth()).append("-").append(datePicker.getDayOfMonth()));
                    history.setDate(tvDate.getText().toString());
                    history.setImage(imagePath);
                    personDatabaseHelper.createDetails(history);
                    personDatabaseHelper.close();
                    Toast.makeText(getApplicationContext(), getString(R.string.successDatabase), Toast.LENGTH_LONG).show();
                    break;
                }
                break;

        }
    }
}
