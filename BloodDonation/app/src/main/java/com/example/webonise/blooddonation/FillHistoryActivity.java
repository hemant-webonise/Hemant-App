package com.example.webonise.blooddonation;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.webonise.blooddonation.adapter.HistoryDBAdapter;
import com.example.webonise.blooddonation.model.History;

import java.util.Calendar;


public class FillHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    DatePicker datePicker;
    Button btnAddHistory;
    EditText etLocation;
    private static int LOAD_IMAGE_RESULTS = 1;
    ImageButton btnImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        toolbar.setSubtitle("Lets add some good deeds..");
        setSupportActionBar(toolbar);
        initialize();
        setListeners();
    }


    private void setListeners() {
        btnAddHistory.setOnClickListener(this);
        btnImage.setOnClickListener(this);
    }

    private void initialize() {
        datePicker=(DatePicker) findViewById(R.id.dpDate);

        datePicker.setMaxDate(Calendar.DATE);

        btnAddHistory=(Button) findViewById(R.id.btnAddHistory);
        btnImage=(ImageButton) findViewById(R.id.btnImage);
        etLocation=(EditText)findViewById(R.id.etLocation);
    }
    String imagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            // Now we need to set the GUI ImageView data with data read from the picked file.
            btnImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnImage :
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
                break;
            case R.id.btnAddHistory :
                Toast.makeText(this,/*String.valueOf(datePicker.getDayOfMonth())+*/imagePath,Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(etLocation.getText().toString())) {
                    etLocation.setError(getString(R.string.error_location));
                } else {
                    HistoryDBAdapter personDatabaseHelper = new HistoryDBAdapter(this);
                    History history = new History();
                    history.setLocation(etLocation.getText().toString());
                    String date = String.valueOf(new StringBuilder().append(datePicker.getYear()).append(" ").append("-").append(datePicker.getMonth()).append("-").append(datePicker.getDayOfMonth()));
                    history.setDate(date);
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
