package com.androidchatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidchatapp.Model.UserModel;
import com.androidchatapp.Views.RoundedImageView;
import com.androidchatapp.libs.Utility;
import com.androidchatapp.libs.Utils;
import com.firebase.client.Firebase;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText username, password, email, dob, comfirmpass, editContactNo, editCountryCode;
    ;
    Button registerButton;
    String user, pass, compass, dateofbirth, strEmail, strCountry, strContactNo, strCustomerImgName;
    private TextView txtcountryname;
    public static RoundedImageView imgButtonCamera;
    TextView login;
    String strDate;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri filePath;


    public static String defaultDate = "2016-01-01T06:04:57.691Z";
    //application specific
    public static Locale locale = Locale.ENGLISH;
    public final static SimpleDateFormat readFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
    private final static SimpleDateFormat readFormatDB =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
    public final static SimpleDateFormat readFormatDate =
            new SimpleDateFormat("yyyy-MM-dd", locale);
    public final static SimpleDateFormat writeFormatMonth =
            new SimpleDateFormat("MMM yyyy", locale);
    public final static SimpleDateFormat writeFormat =
            new SimpleDateFormat("HH:mm dd MMM yyyy", locale);
    public final static SimpleDateFormat writeFormatTime =
            new SimpleDateFormat("HH:mm", locale);
    public final static SimpleDateFormat writeFormatDateDB = new
            SimpleDateFormat("yyyy-MM-dd", locale);
    private final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", locale);
    /*   public final static SimpleDateFormat writeFormatActivity =
               new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Config.locale);*/
    public final static SimpleDateFormat writeFormatActivityYear =
            new SimpleDateFormat("dd/MM/yyyy", locale);
    private final static SimpleDateFormat queryFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);
    private final static SimpleDateFormat queryFormatDB =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            strDate = writeFormatActivityYear.format(date);


            try {
                date = writeFormatActivityYear.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int strAge = Integer.parseInt(getAge(date));
            if (ageValidationCustomer(strAge)) {
                // String _strDate = Utils.readFormat.format(date);
                dob.setText(strDate);
            } else {
                dob.setText("");
            }
        }

        @Override
        public void onDateTimeCancel() {
            // Overriding onDateTimeCancel() is optional.
        }
    };

    public boolean ageValidationCustomer(int age) {

        boolean isValid = false;


        if (age != 0 && age >= 18) {
            isValid = true;
        }

        return isValid;
    }

    public String getAge(Date date) {

        Calendar today = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();

        dob.setTime(date);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = age;

        return ageInt.toString();
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://androidchatapp-85f77.appspot.com");
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("profile_image");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgButtonCamera = (RoundedImageView) findViewById(R.id.imageButtonCamera);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        dob = (EditText) findViewById(R.id.dob);
        comfirmpass = (EditText) findViewById(R.id.comfirmpassword);
        editContactNo = (EditText) findViewById(R.id.editContactNo);
        editCountryCode = (EditText) findViewById(R.id.editCountryCode);

        txtcountryname = (TextView) findViewById(R.id.txtcountryname);

        registerButton = (Button) findViewById(R.id.registerButton);
        //login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                username.setFocusableInTouchMode(true);
                return false;
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                password.setFocusableInTouchMode(true);
                return false;
            }
        });
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                email.setFocusableInTouchMode(true);
                return false;
            }
        });
        comfirmpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                comfirmpass.setFocusableInTouchMode(true);
                return false;
            }
        });
        editContactNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editContactNo.setFocusableInTouchMode(true);
                return false;
            }
        });

       /* login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });*/

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2000);
                Date date = new Date();
                date.setTime(cal.getTimeInMillis());
                new SlideDateTimePicker.Builder(Register.this.getSupportFragmentManager())
                        .setListener(listener)
                        .setMaxDate(new Date())
                        .setInitialDate(date)
                        .setTimeShownStatus(false)
                        .build()
                        .show();
            }
        });

        imgButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });

        txtcountryname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryPickerDialog countryPicker =
                        new CountryPickerDialog(Register.this, new CountryPickerCallbacks() {
                            @Override
                            public void onCountrySelected(Country country, int flagResId) {
                                editCountryCode.setText(country.getDialingCode());
                                txtcountryname.setText(country.getCountryName(Register.this));
                            }
                        }, false, 0);
                countryPicker.show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateDetails()) {

                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    //String url = "https://android-chat-app-e711d.firebaseio.com/users.json";
                    String url = "https://androidchatapp-85f77.firebaseio.com/UserInfo.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            // Firebase reference = new Firebase("https://android-chat-app-e711d.firebaseio.com/users");
                            Firebase reference = new Firebase("https://androidchatapp-85f77.firebaseio.com/user");
                            if (s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("dateofbirth").setValue(dateofbirth);
                                reference.child(user).child("email").setValue(strEmail);
                                reference.child(user).child("country").setValue(strCountry);
                                reference.child(user).child("mobileno").setValue(strContactNo);
                                uploadImage();
                               // reference.child(user).child("profile_image").setValue(filePath);
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                username.setText("");
                                password.setText("");
                                comfirmpass.setText("");
                                dob.setText("");
                                email.setText("");
                                txtcountryname.setText("");
                                editContactNo.setText("");

                                //focus
                                username.setFocusable(false);
                                username.setFocusableInTouchMode(false);
                                password.setFocusable(false);
                                password.setFocusableInTouchMode(false);
                                comfirmpass.setFocusable(false);
                                comfirmpass.setFocusableInTouchMode(false);
                                email.setFocusable(false);
                                email.setFocusableInTouchMode(false);
                                txtcountryname.setFocusable(false);
                                txtcountryname.setFocusableInTouchMode(false);


                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("dateofbirth").setValue(dateofbirth);
                                        reference.child(user).child("email").setValue(strEmail);
                                        reference.child(user).child("country").setValue(strCountry);
                                        reference.child(user).child("mobileno").setValue(strContactNo);
                                        //reference.child(user).child("profile_image").setValue(filePath);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }

            }
        });
    }


    private boolean validateDetails() {
        boolean alldone = true;
        try {

            user = username.getText().toString().trim();
            pass = password.getText().toString().trim();
            compass = comfirmpass.getText().toString().trim();
            dateofbirth = dob.getText().toString().trim();
            strEmail = email.getText().toString().trim();
            strCountry = txtcountryname.getText().toString().trim();
            strContactNo = editContactNo.getText().toString().trim();


            View focusView = null;

            if (TextUtils.isEmpty(user)) {
                username.setError("Please Enter Valid Name");
                focusView = username;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
                username.setError(null);
            }

            if (!TextUtils.isEmpty(pass) && isPasswordValid(pass)
                    && !TextUtils.isEmpty(compass) && isPasswordValid(compass)) {

                if (!pass.trim().equalsIgnoreCase(compass.trim())) {
                    comfirmpass.setError("Password & ComfirmPassword not match...");
                    focusView = comfirmpass;
                    focusView.requestFocus();
                    return false;
                } else {
                    alldone = true;
                    comfirmpass.setError(null);
                }
            }

            if (TextUtils.isEmpty(pass)) {
                password.setError("This field is required");
                focusView = password;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
                password.setError(null);
            }

            if (TextUtils.isEmpty(compass)) {
                comfirmpass.setError("This field is required");
                focusView = comfirmpass;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
                comfirmpass.setError(null);
            }
            if (TextUtils.isEmpty(strEmail)) {
                email.setError("This field is required");
                focusView = email;
                focusView.requestFocus();
                return false;
            } else if (!isEmailValid(strEmail)) {
                email.setError("Invalid Email");
                focusView = email;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
                email.setError(null);
            }
            if (TextUtils.isEmpty(dateofbirth)) {
                dob.setError("Please Enter Valid Date");
                focusView = dob;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
                dob.setError(null);
            }
            if (TextUtils.isEmpty(strCountry) || strCountry.equalsIgnoreCase("Select Country")) {
                //editAddress.setError(getString(R.string.error_field_required));
                focusView = txtcountryname;
                focusView.requestFocus();
                Toast.makeText(getApplicationContext(), "Select a country", Toast.LENGTH_SHORT).show();
            } else {
                alldone = true;
                txtcountryname.setError(null);
            }

            if (!isPhoneValid(strContactNo)) {
                editContactNo.setError("Please Enter 10 digit Mobile no.");
                focusView = editContactNo;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
                editContactNo.setError(null);
            }
            if (TextUtils.isEmpty(strCustomerImgName)) {
                Toast.makeText(getApplicationContext(), "Please select profile picture", Toast.LENGTH_SHORT).show();
                focusView = imgButtonCamera;
                focusView.requestFocus();
                return false;
            } else {
                alldone = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return alldone;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void uploadImage(){
        if(filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference childRef = storageRef.child("profile_image/"+user+"pic.jpg");

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                    UserModel upload = new UserModel(user,taskSnapshot.getDownloadUrl().toString());
                    //adding an upload to firebase database
                    String uploadId = mDatabase.push().getKey();
                    mDatabase.child(uploadId).setValue(upload);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //calculating progress percentage
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    //displaying percentage in progress dialog
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        }
        else {
            Toast.makeText(Register.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Register.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

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
        byte[] byteArrayVar = bytes.toByteArray();
        strCustomerImgName = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            filePath = Uri.fromFile(destination);
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgButtonCamera.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        filePath = data.getData();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byteArray;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byteArray = byteArrayOutputStream.toByteArray();

                strCustomerImgName = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgButtonCamera.setImageBitmap(bm);
    }

    public boolean isEmailValid(String email) {
        boolean b;

        b = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if (b) {
            Pattern p = Pattern.compile("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$",
                    Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(email);
            b = m.matches();
        }

        return b;
    }

    public boolean isPhoneValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        } else {
            if (phoneNumber.length() < 6 || phoneNumber.length() > 10) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
            }
        }
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 1;
    }


}