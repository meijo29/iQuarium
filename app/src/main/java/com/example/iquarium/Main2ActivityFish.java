package com.example.iquarium;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main2ActivityFish extends AppCompatActivity implements InputDialog.InputDialogListener {

   private DrawView drawView;
    private FrameLayout preview;
    private ImageButton btn_ok;
    private Button btn_draw;
    private ImageButton btn_takePicture, btn_choosephoto, btn_zoom, btn_clrRed, btn_back, btn_cancel;
    private File photoFile;
    private Uri photoURI;
    private ZoomLayout zoom;
    boolean ZoomON = false;
    InputDialog inputDialog;
    boolean DrawON = false;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    List<Point> circlepoints;
   // TextFrameLayout txtfrmlayout;
    private double result;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_fish);

        preview = findViewById(R.id.camera_preview);
        zoom = new ZoomLayout(this);
        drawView = new DrawView(this);
        btn_zoom = findViewById(R.id.zoombttn);
        btn_choosephoto = findViewById(R.id.button_choosePicture);
        btn_takePicture = findViewById(R.id.button_takePicture);
        btn_ok =  findViewById(R.id.button_calculate);
        btn_cancel = findViewById(R.id.button_cancel);
        btn_clrRed = findViewById(R.id.clearref);
        btn_back = findViewById(R.id.backbutton);

        btn_zoom.setVisibility(View.GONE);

        addPictureButton();
        requestRead();


        btn_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dispatchTakePictureIntent();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new InputDialog().show(getFragmentManager(), "input_dialog");

            }
        });

        btn_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(ZoomON == false) {
                 ZoomON = true;
                 preview.addView(zoom);
                 ((TextView) findViewById(R.id.info_lbl)).setText("Zoom mode");
             }
             else{
                 ZoomON = false;
                 preview.removeView(zoom);

                 if(drawView.circlePoints.size() == 4){
                     ((TextView) findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setScaleValue));
                 }
                 else {
                     ((TextView) findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setMeasurePoints));
                 }
             }
            }
        });

        btn_choosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchChoosePhotoIntent();
            }
        });

        btn_clrRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    try {
                        if (drawView.circlePoints != null) {

                  if (drawView.circlePoints.size() >= 2) {
                      drawView.clearCanvasRed();
                      btn_ok.setVisibility(View.GONE);
                      if(ZoomON == true){
                          ((TextView) findViewById(R.id.info_lbl)).setText("Zoom mode");
                      }
                      else {
                          ((TextView) findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setMeasurePoints));
                      }
                  }

                        }
                    }catch (Exception e){
                        toasMessage(e.toString());
                    }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clearCanvas();
                btn_ok.setVisibility(View.GONE);
                if(ZoomON == true){
                    ((TextView) findViewById(R.id.info_lbl)).setText("Zoom mode");
                }
                else {
                    ((TextView) findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setMeasurePoints));
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Main2ActivityFish.this, MainActivity.class);
                startActivity(intent);

            }
        });



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
//            case R.id.action_settings:
//                break;
            case R.id.action_cleanStorage:
                cleanPhotoStorage();
                break;
            case R.id.action_choosePhoto:
                dispatchChoosePhotoIntent();
                break;
            case R.id.action_takePicture:
                dispatchTakePictureIntent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
       if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
           //     photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error creating image", Toast.LENGTH_SHORT);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {

                    photoURI = FileProvider.getUriForFile(this, "com.example.iquarium.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
/*
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
*/
                }catch (Exception e){
                    toasMessage(e.toString());
                    return;
                }
            }
        }

    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }
    private void addPictureButton(){
        preview.removeAllViews();

        btn_cancel.setVisibility(View.GONE);
        btn_ok.setVisibility(View.GONE);
        btn_clrRed.setVisibility(View.GONE);
        btn_takePicture.setVisibility(View.VISIBLE);
        btn_choosephoto.setVisibility(View.VISIBLE);
    }


    private void pictureTaken(){
        preview.removeAllViews();

        btn_cancel.setVisibility(View.VISIBLE);
     //   btn_ok.setVisibility(View.VISIBLE);
        btn_takePicture.setVisibility(View.GONE);
        btn_choosephoto.setVisibility(View.GONE);
        btn_clrRed.setVisibility(View.VISIBLE);

        ImageSurface image = new ImageSurface(this, photoFile);
        preview.addView(image);

        ((TextView) findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setMeasurePoints));

      //  drawView = new DrawView(this);
        preview.addView(drawView);

        ZoomDrawControl();

    }

    private void ZoomDrawControl(){

        btn_zoom.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){

            if(drawView != null){

                if(drawView.circlePoints.size() < 4){
                    btn_ok.setVisibility(View.GONE);
                }
                else{
                    btn_ok.setVisibility(View.VISIBLE);
                }

            }

        }

        return false;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_PHOTO = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK)
                    pictureTaken();
                break;
            case REQUEST_SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    String filePath = Utils.getPath(this, uri);
                    photoFile = new File(filePath);
                    pictureTaken();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void cleanPhotoStorage(){
        File storageDir = getExternalFilesDir(null);
        File fList[] = storageDir.listFiles();
        //Search for pictures in the directory
        for(int i = 0; i < fList.length; i++){
            String pes = fList[i].getName();
            if(pes.endsWith(".jpg"))
                new File(fList[i].getAbsolutePath()).delete();
        }
        Toast.makeText(this, getResources().getString(R.string.storageDeleted), Toast.LENGTH_SHORT).show();
    }

    private void dispatchChoosePhotoIntent(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.action_choosePhoto)), REQUEST_SELECT_PHOTO);
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
        }
    }


    @Override
    public void onDialogPositiveClick(final DialogFragment dialog) {


       String reflength = ((EditText) dialog.getDialog().findViewById(R.id.reference_input)).getText().toString();

        if(reflength.matches("")){
            toasMessage("Reference length cannot be empty");
            return;
        }
        if(reflength.equalsIgnoreCase(".")){
            toasMessage("Enter a valid number");
            return;
        }

        int inputUnit = ((Spinner) dialog.getDialog().findViewById(R.id.input_unit_chooser)).getSelectedItemPosition();
        int outputUnit = ((Spinner) dialog.getDialog().findViewById(R.id.output_unit_chooser)).getSelectedItemPosition();
        try {
            double reference = Double.parseDouble(((EditText) dialog.getDialog().findViewById(R.id.reference_input)).getText().toString());
            result = drawView.calculate(reference, inputUnit, outputUnit);

            showResult(((Spinner) dialog.getDialog().findViewById(R.id.output_unit_chooser)).getSelectedItem() + "");
        }catch(NumberFormatException ex){
            Toast.makeText(this, getResources().getString(R.string.error_numberFormat), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Do absolutely nothing


    }

    private void showResult(String outputunit){
        if(result != -1) {
            /*
            DecimalFormat decimalFormat = new DecimalFormat(" #.## " + outputunit);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.result_lbl) + decimalFormat.format(result));
            builder.create().show();

             */
            DecimalFormat decimalFormat = new DecimalFormat("#.## " + outputunit);

            ((TextView) findViewById(R.id.info_lbl)).setText("RESULT: The length is " + decimalFormat.format(result));

        }
    }
}


