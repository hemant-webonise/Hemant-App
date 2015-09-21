package com.example.webonise.blooddonation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webonise.blooddonation.adapter.HistoryDBAdapter;
import com.example.webonise.blooddonation.app.Constant;
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

    Button btnAddHistory;
    HistoryDBAdapter personDatabaseHelper;
    EditText etLocation;
    TextView tvDate;
    private static int LOAD_IMAGE_RESULTS = 1;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageButton btnImage;
    private boolean isUpdate = true;
    int id;
    Bundle bundle;
    Toolbar toolbar;
    Cursor cursor;
    ImageButton calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_history);
        bundle = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (bundle != null) {
            isUpdate = bundle.getBoolean("update");
            Log.w("IS", String.valueOf(isUpdate));
            if (!isUpdate) {
                toolbar.setTitle(getString(R.string.updateTitle));
                toolbar.setSubtitle(getString(R.string.subtitle));
                setSupportActionBar(toolbar);
                initialize();
                setListeners();
                id = bundle.getInt("ID");
                personDatabaseHelper = new HistoryDBAdapter(this);
                cursor = personDatabaseHelper.getCertainDetail(id);
                etLocation.setText("" + cursor.getString(cursor.getColumnIndex(Constant.COLUMN_LOCATION)));
                tvDate.setText("" + cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE)));

                imagePath = "" + cursor.getString(cursor.getColumnIndex(Constant.COLUMN_IMAGE));
                if (imagePath.equals("" + null)) {
                    btnImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pick));
                } else {
                    btnImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                }
            }
        } else {
            toolbar.setTitle(getString(R.string.add));
            toolbar.setSubtitle(getString(R.string.subtitle));
            setSupportActionBar(toolbar);
            initialize();
            setListeners();
        }
    }


    private void setListeners() {
        setDailog();
        btnAddHistory.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        calendar=(ImageButton) findViewById(R.id.calendar);
        calendar.setOnClickListener(this);
    }

    private void setDailog() {
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
    }

    private void datePickerDialog() {
        // To show current date in the datepicker
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH,
                        selectedday);
                SimpleDateFormat sdf = new SimpleDateFormat(
                        getResources().getString(
                                R.string.date_card_formate),
                        Locale.US);

                tvDate.setText(sdf.format(mcurrentDate.getTime()));
                btnAddHistory.setAlpha((float) 0.8);
                btnAddHistory.setBackgroundColor(Color.GREEN);

            }
        }, mYear, mMonth, mDay);

        mDatePicker.setTitle(getResources().getString(R.string.alert_date_select));
        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        if(!isEmpty(etLocation)){
            mDatePicker.show();
           }else{
            Toast.makeText(this,getString(R.string.allert_name),Toast.LENGTH_LONG).show();
        }

    }

    private void initialize() {
       /* datePicker=(DatePicker) findViewById(R.id.dpDate);*/

      /*  datePicker.setMaxDate(Calendar.DATE);
*/
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnAddHistory = (Button) findViewById(R.id.btnAddHistory);
        btnImage = (ImageButton) findViewById(R.id.btnImage);
        etLocation = (EditText) findViewById(R.id.etLocation);
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

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

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
        imagePath = String.valueOf(destination);
        btnImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        imagePath = selectedImagePath;
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
        final CharSequence[] items = {getString(R.string.choose_photo), getString(R.string.choose_laibrary), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(FillHistoryActivity.this);
        builder.setTitle(getString(R.string.title_add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.choose_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getString(R.string.choose_laibrary))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.select_file)),
                            SELECT_FILE);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private boolean isEmpty(EditText input) {
        return input.getText().toString().trim().length() == 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnImage:
                selectImage();
                break;
            case R.id.calendar:
                 datePickerDialog();
                break;

            case R.id.btnAddHistory:
                if (isEmpty(etLocation)) {
                    etLocation.setError(getString(R.string.error_location));
                } else if (tvDate.getText().toString().equals(getString(R.string.select_date))) {
                    datePickerDialog();

                } else if (toolbar.getTitle() == getString(R.string.updateTitle)) {
                    id = bundle.getInt("ID");
                    if (imagePath != null) {
                        personDatabaseHelper = new HistoryDBAdapter(this);
                        /*Cause error*/
                        personDatabaseHelper.updateCertainDetail(id, etLocation.getText().toString(), tvDate.getText().toString(), imagePath);
                        personDatabaseHelper.close();
                        Intent returnBack = new Intent(this, HistorySQLActivity.class);
                        startActivity(returnBack);
                        finish();
                    } else {
                        imagePath = String.valueOf(R.drawable.pick);
                        HistoryDBAdapter personDatabaseHelper = new HistoryDBAdapter(this);
                        personDatabaseHelper.updateCertainDetail(id, etLocation.getText().toString(), tvDate.getText().toString(), imagePath);
                        personDatabaseHelper.close();
                        Intent returnBank = new Intent(this, HistorySQLActivity.class);
                        startActivity(returnBank);
                        finish();
                    }
                } else {
                    HistoryDBAdapter personDatabaseHelper = new HistoryDBAdapter(this);
                    History history = new History();
                    history.setLocation(etLocation.getText().toString());
                    history.setDate(tvDate.getText().toString());
                    history.setImage(imagePath);
                    personDatabaseHelper.createDetails(history);
                    personDatabaseHelper.close();
                    Toast.makeText(getApplicationContext(), getString(R.string.successDatabase), Toast.LENGTH_LONG).show();
                    btnAddHistory.setText("Added");
                    Intent returnBank = new Intent(this, HistorySQLActivity.class);
                    startActivity(returnBank);
                    finish();
                }

                break;

        }
    }
}
