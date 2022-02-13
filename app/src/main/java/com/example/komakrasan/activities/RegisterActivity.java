package com.example.komakrasan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.komakrasan.BuildConfig;
import com.example.komakrasan.R;
import com.example.komakrasan.model.ClassCounty;
import com.example.komakrasan.model.ClassProvince;
import com.example.komakrasan.network.ApiClient;
import com.example.komakrasan.network.ApiInterface;
import com.example.komakrasan.util.MySharedPreferences;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity{

    /*api*/
    private ApiInterface apiInterface;
    private List<ClassProvince> provinces;
    private List<ClassCounty> countyList;
    String  []arrayProvince;
    String  []arrayCounty;
    private AppCompatSpinner sp_province,sp_zone;
    private TextView tv_county,tv_province;
    private EditText et_name,et_num_family,et_phone,et_place;
    private ImageView iv_parson,iv_home,iv_camera_parson,iv_camera_home;
    private Button btnUpload;
    private Boolean first = false,second = false;
    ProgressBar progressBar;
    TextView tv_load;
    Handler handler;
    Runnable runnable;
    Timer timer;


    //SharedPreferences
    private MySharedPreferences preferences;
    private int idProvince,idCounty;

    //fields for capture image
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_Picker = 2;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private final int REQUEST_PREMISSION_CODE = 2342;
    private String currentPhotoPathParson = null;
    private String currentPhotoPathHome = null;
    private String mImageFileLocation = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        //find view method
        find();

        //get permission
        RequestStoragePermission();

        //getting province
        getProvince();

        //change the language of App white isRtl method
        String languageToLoad = "fa";
        Locale locale = new Locale(languageToLoad);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSendData();
            }
        });

    }

    private void getProvince(){

        /*making ProvinceApi */
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        /*getting Province*/
        Call<List<ClassProvince>> call = apiInterface.getProvince();
        call.enqueue(new Callback<List<ClassProvince>>() {
            @Override
            public void onResponse(Call<List<ClassProvince>> call, Response<List<ClassProvince>> response) {

                if (!response.isSuccessful()) {
                    Toasty.warning(RegisterActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(RegisterActivity.this, "the error cod is" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                /*list of Province */
                provinces = response.body();
                //array for storing name of province for setting them to adapter
                arrayProvince = new String[provinces.size()];
                for (int i = 0; i < arrayProvince.length; i++) {

                    arrayProvince[i] = provinces.get(i).getName();
                }
                //spinner Province
                sp_province.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayProvince));

                /**
                 * getting county
                 */
                getCounty();
            }

            @Override
            public void onFailure(Call<List<ClassProvince>> call, Throwable t) {

            }
        });

    }

    private void getCounty(){
        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //storing id province
                idProvince = provinces.get(i).getId();

                String text =adapterView.getItemAtPosition(i).toString();
                tv_province.setText(text);

                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<List<ClassCounty>> call = apiInterface.getCounty(idProvince);
                call.enqueue(new Callback<List<ClassCounty>>() {
                    @Override
                    public void onResponse(Call<List<ClassCounty>> call, Response<List<ClassCounty>> response) {

                        if (!response.isSuccessful()) {
                    Toasty.warning(RegisterActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();
//                            Toast.makeText(RegisterActivity.this, "the error cod is" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        /*list of Province */
                        countyList = response.body();
                        //array for storing name of province for setting them to adapter
                        arrayCounty = new String[countyList.size()];
                        for (int i = 0; i < arrayCounty.length; i++) {

                            arrayCounty[i] = countyList.get(i).getName();

                        }
                        //spinner Province
                        sp_zone.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayCounty));

                        /**
                         * getting nameOfCounty
                         */
                        getNameOfCounty();
                    }

                    @Override
                    public void onFailure(Call<List<ClassCounty>> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

   private void getNameOfCounty(){
       sp_zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               idCounty = countyList.get(i).getId();

               String text = adapterView.getItemAtPosition(i).toString();
//               Toast.makeText(RegisterActivity.this, ""+idCounty, Toast.LENGTH_SHORT).show();
               tv_county.setText(text);
           }
           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
   }


    public void btnSendData() {

        if (currentPhotoPathParson== null || currentPhotoPathParson.equals("")) {
            Toasty.info(RegisterActivity.this, R.string.info_toast, Toasty.LENGTH_SHORT, true).show();
            return;
        }

        String name = et_name.getText().toString();
        String phone = et_phone.getText().toString();
        String number = et_num_family.getText().toString();
        String place = et_place.getText().toString();

        if (name.isEmpty()) {
            et_name.setError(getString(R.string.write_name));
            et_name.findFocus();
            return;
        }
        if (phone.isEmpty()) {
            et_phone.setError(getString(R.string.write_phone));
            et_phone.findFocus();
            return;
        }
        if (number.isEmpty()) {
            et_num_family.setError(getString(R.string.write_family));
            et_num_family.findFocus();
            return;
        }
        if (currentPhotoPathHome== null || currentPhotoPathHome.equals("")) {
            Toasty.info(RegisterActivity.this, R.string.info_toast, Toasty.LENGTH_SHORT, true).show();
            return;
        }
        if (place.isEmpty()) {
            et_place.setError(getString(R.string.write_village));
            et_place.findFocus();
            return;
        }
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        tv_load.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                timer.cancel();
                progressBar.setVisibility(View.GONE);
                tv_load.setVisibility(View.GONE);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(runnable);
            }
        },3000,1000);


        //api method
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.sendData(
                createPartFromString(et_name.getText().toString()),
                createPartFromString(et_phone.getText().toString()),
                createPartFromString(et_num_family.getText().toString()),
                createPartFromString(et_place.getText().toString()),
                prepareFilePart(currentPhotoPathParson, "image", "userfile"),
                prepareFilePart(currentPhotoPathHome, "image", "userfile2"),
                idCounty
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (!response.isSuccessful()) {
                    Toasty.warning(RegisterActivity.this, R.string.warning_toast , Toasty.LENGTH_SHORT, true).show();
//                    Toast.makeText(RegisterActivity.this, "erore" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toasty.success(getApplicationContext(), getResources().getString(R.string.success_toast), Toasty.LENGTH_SHORT).show();
//                Intent intent = new Intent(ReportActivityHoard.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(RegisterActivity.this,R.string.error_toast,Toasty.LENGTH_SHORT).show();
//                Toast.makeText(RegisterActivity.this, "erore" +t, Toast.LENGTH_SHORT).show();
//                Log.i("error", "onFailure: "+t);

            }
        });
    }

    //method for get personImage
    public void btnParsonImage(View view){

        new MaterialDialog.Builder(this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                first = true;
                                selectImage();
                                break;
                            case 1:
                                first = true;
                                camera();
                                break;
                        }
                    }
                })
                .show();
    }

    //method for get HomeImage
    public void btnHomeImage(View view){

        new MaterialDialog.Builder(this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                second = true;
                                selectImage();
                                break;
                            case 1:
                                second = true;
                                camera();
                                break;
                        }
                    }
                })
                .show();

    }



    public void find() {

        et_name = findViewById(R.id.activity_register_EditText_name);
        et_num_family = findViewById(R.id.activity_register_EditText_number);
        et_phone = findViewById(R.id.activity_register_EditText_phone);
        et_place = findViewById(R.id.activity_register_EditText_place);
        iv_parson = findViewById(R.id.activity_register_ImageView_person);
        iv_home = findViewById(R.id.activity_register_ImageView_home);
        sp_province = findViewById(R.id.spinner_province);
        sp_zone = findViewById(R.id.spinner_county);
        tv_county = findViewById(R.id.activity_register_TextView_county);
        tv_province = findViewById(R.id.activity_register_TextView_province);
        btnUpload = findViewById(R.id.btn_send_data);
        progressBar = findViewById(R.id.spin_kit);
        tv_load = findViewById(R.id.text_view_load);
    }


    /*request method for capture image*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            if (first){

                iv_parson.setImageURI(Uri.parse(currentPhotoPathParson));
                galleryAddPic(currentPhotoPathParson);
                first = false;
            }else if(second){

                iv_home.setImageURI(Uri.parse(currentPhotoPathHome));
                galleryAddPic(currentPhotoPathHome);
                second = false;
            }

        } else if (requestCode == REQUEST_IMAGE_Picker && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            if (first){

                currentPhotoPathParson = getPath(uri);
                iv_parson.setImageURI(Uri.parse(currentPhotoPathParson));
                galleryAddPic(currentPhotoPathParson);
                first = false;

            }else if (second){

                currentPhotoPathHome = getPath(uri);
                iv_home.setImageURI(Uri.parse(currentPhotoPathHome));
                galleryAddPic(currentPhotoPathHome);
                second = false;
            }


        }else if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK){

            if (Build.VERSION.SDK_INT > 21) {

                if (first){

                    Glide.with(this).load(mImageFileLocation).into(iv_parson);
                    currentPhotoPathParson = mImageFileLocation;
                    Log.i("error", "onFailure: "+currentPhotoPathParson);

                    first =false;

                }else if (second){

                    Glide.with(this).load(mImageFileLocation).into(iv_home);
                    currentPhotoPathHome = mImageFileLocation;
                    Log.i("error", "onFailure: "+currentPhotoPathHome);

                    second = false;
                }

            }
            else{
                Uri uri = data.getData();
                if (first){

                    currentPhotoPathParson = getPath(uri);
                    iv_parson.setImageURI(Uri.parse(currentPhotoPathParson));
                    galleryAddPic(currentPhotoPathParson);

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    iv_parson.setImageBitmap(bitmap);
                    first =false;

                }else if (second){

                    currentPhotoPathHome = getPath(uri);
                    iv_parson.setImageURI(Uri.parse(currentPhotoPathHome));
                    galleryAddPic(currentPhotoPathHome);

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    iv_parson.setImageBitmap(bitmap);
                    second =false;
                }

            }

        }

    }


    //methods that are dependent on uploadImage
    private void galleryAddPic(String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_Picker);
        }
    }

    public void camera(){
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        }else {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_PIC_REQUEST);
        }

    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //methods that are dependent on APIs
    @NonNull
    private RequestBody createPartFromString(String descriptionPart) {
        return RequestBody.create(MultipartBody.FORM, descriptionPart);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String pathSave, String type, String field) {
        File originalFile = new File(String.valueOf(pathSave));
//        Log.i("gho",originalFile.getPath());
        RequestBody filePart = RequestBody.create(
                MediaType.parse(type + "/*"),
                originalFile);

        return MultipartBody.Part.createFormData(field, originalFile.getName(), filePart);
    }

    //methods that are dependent on permission
    public void RequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PREMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PREMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(this, R.string.permission, Toast.LENGTH_SHORT).show();
            } else {
                Toasty.warning(this, R.string.permission_not, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
