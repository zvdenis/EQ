package com.SuperClub.EQ.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.SuperClub.EQ.Application.Configs;
import com.SuperClub.EQ.R;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import es.dmoral.toasty.Toasty;

public class ShowQRActivity extends AppCompatActivity {

    ImageView imageView;
    ImageView saveButton;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    String text;
    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_q_r);
        title = getIntent().getStringExtra("title");
        imageView = findViewById(R.id.qr_image);
        saveButton = findViewById(R.id.save_button);
        text = getIntent().getStringExtra(Configs.qrTextExtraName);
        saveButton.setOnClickListener(this::saveClick);

        updateImage();
    }


    private void updateImage() {


        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    public void saveClick(View view) {

        String name = title + ".jpg";
        saveMediaToStorage(bitmap, name);


    }


    void saveMediaToStorage(Bitmap bitmap, String name) {
        //Generating a file name
        String filename = name + ".jpg";
        if (android.os.Build.VERSION.SDK_INT >= 29) {

        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
        values.put(MediaStore.Images.Media.IS_PENDING, true);

        Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            try {
                saveImageToStream(bitmap, this.getContentResolver().openOutputStream(uri));
                values.put(MediaStore.Images.Media.IS_PENDING, false);
                this.getContentResolver().update(uri, values, null, null);
                Toasty.success(this, "Image Saved").show();
            } catch (FileNotFoundException e) {
                Toasty.error(this, "Unable to save image").show();
                e.printStackTrace();
            }

        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = name + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues cvalues = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cvalues);
                Toasty.success(this, "Image Saved").show();
            } catch (FileNotFoundException e) {
                Toasty.error(this, "Unable to save image").show();
                e.printStackTrace();
            }
        }
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}