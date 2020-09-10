package com.example.qrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.MailTo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrapp.Type_Check.EmailCheckClass;
import com.example.qrapp.Type_Check.GeoCheckClass;
import com.example.qrapp.Type_Check.PhoneCheckClass;
import com.example.qrapp.Type_Check.SmsCheckClass;
import com.example.qrapp.Type_Check.TypeCheckClass;
import com.example.qrapp.Type_Check.UriCheckClass;
import com.example.qrapp.Type_Check.WifiCheckClass;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.google.zxing.client.result.ParsedResultType.EMAIL_ADDRESS;
import static com.google.zxing.client.result.ParsedResultType.GEO;
import static com.google.zxing.client.result.ParsedResultType.TEL;
import static com.google.zxing.client.result.ParsedResultType.URI;
import static com.google.zxing.client.result.ParsedResultType.WIFI;
import static com.google.zxing.client.result.ParsedResultType.SMS;

public class Suggestion extends AppCompatActivity implements View.OnClickListener, TypeCheckClass.QRTypeListner {

    private static final String TAG = "Suggestion Class";

    ImageView qr_code_img, delete_img, share_img, copy_img;
    TextView content_txt, delete_txt, share_txt, copy_txt;
    Button sugg_button;

    boolean mPermission = false;
    private String mQRType ;

    String qrData1;
    String qrData2;
    int select=0;

    private TypeCheckClass mTypeCheckClass ;
    private EmailCheckClass mEmailCheckClass ;
    private WifiCheckClass mWifiCheckClass ;
    private SmsCheckClass mSMSCheckClass ;
    private PhoneCheckClass mPhoneCheckClass ;
    private UriCheckClass  mURICheckClass ;
    private GeoCheckClass  mGeoCheckClass ;

    private void initialize(){
        qr_code_img = findViewById(R.id.qrcode_result);
        delete_img = findViewById(R.id.delte_img);
        share_img = findViewById(R.id.share_img);
        copy_img = findViewById(R.id.copy_img);

        content_txt = findViewById(R.id.result_text);
        delete_txt = findViewById(R.id.delete_text);
        share_txt = findViewById(R.id.share_text);
        copy_txt = findViewById(R.id.copy_text);
        sugg_button = findViewById(R.id.creare_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeStatusBarColor();
        initialize();
        mTypeCheckClass = new TypeCheckClass(this);
        SetUpQRCode();


// Give suggestion for different QR code
        IntentSuggestions();
// Give suggestion for different QR code   btn
        sugg_button.setOnClickListener(this);
        delete_img.setOnClickListener(this);
        copy_img.setOnClickListener(this);
        share_img.setOnClickListener(this);
    }

    private void SetUpQRCode(){
        Bundle mBundle = getIntent().getExtras();
        if(mBundle!= null ){

             qrData1 = mBundle.getString("qrResult");
             qrData2 = mBundle.getString("qrImageData");
            Log.d(TAG, "SetUpQRCode() Scanner data ="+qrData1);
            if(qrData1 != null ){
                select=1;
                Log.d(TAG, "SetUpQRCode() Scanner data ="+qrData1);
                Log.d(TAG, "SetUpQRCode() Gallery data ="+qrData2);
                content_txt.setText(qrData1);
                Toast.makeText(this, ""+qrData1, Toast.LENGTH_SHORT).show();

                QRGEncoder qrgEncoder = new QRGEncoder(qrData1, null, QRGContents.Type.TEXT, 250);
                try {
                    Bitmap qrBitMap = qrgEncoder.getBitmap();
                    qr_code_img.setImageBitmap(qrBitMap);
                } catch (Exception e) {
                    Log.d(TAG, "SetUpQRCode() qrData1 EXCEPTION = "+e.getMessage());
                }
            }
            if(qrData2 != null){
                select=2;
                Log.d(TAG, "SetUpQRCode() Gallery data ="+qrData2);
                try {
                    Bitmap qrBitMap = BitmapFactory.decodeStream(this.openFileInput("qrImage"));
                    qr_code_img.setImageBitmap(qrBitMap);
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "SetUpQRCode() qrData2 EXCEPTION = "+e.getMessage());
                }
                content_txt.setText(qrData2);


            }
            if(hasImage(qr_code_img)){
                Bitmap qrBitMap = ((BitmapDrawable)qr_code_img.getDrawable()).getBitmap();
                GetQRResult(qrBitMap);
            }
        }
    }

    //  Give suggestion for different QR code
    private void IntentSuggestions() {
        String qrData = content_txt.getText().toString();
        if(!qrData.equals("")){
            if (mQRType.equals(TEL.toString())) {
                sugg_button.setText("Call");
            } else if (mQRType.equals(URI.toString())) {
                sugg_button.setText("Open in Browser");
            } else if (mQRType.equals(SMS.toString())) {
                if(select==1) {
                    String str = qrData1.replaceAll("[?]", "\n");
                    content_txt.setText(str);
                    Toast.makeText(this, "" + str, Toast.LENGTH_SHORT).show();
                    sugg_button.setText("Send SMS");
                }
                if(select==2) {
                    String str = qrData2.replaceAll("[?]", "\n");
                    content_txt.setText(str);
                    Toast.makeText(this, "" + str, Toast.LENGTH_SHORT).show();
                    sugg_button.setText("Send SMS");
                }
            } else if (mQRType.equals(GEO.toString())){
                sugg_button.setText("Search On GoogleMap");
            } else if (mQRType.equals(EMAIL_ADDRESS.toString())) {
                if(select==1){
                String str = qrData1.replaceAll("[&]","\n");
                String str1 = str.replaceAll("[?]","");
                content_txt.setText(str1);
                }
                if(select==2){
                    String str = qrData1.replaceAll("[&]","\n");
                    String str1 = str.replaceAll("[?]","");
                    content_txt.setText(str1);
                }
                sugg_button.setText("Send Email");
            } else if (mQRType.equals(WIFI.toString())){
                sugg_button.setText("Connect Wifi");
            } else {
                sugg_button.setText("Web Search");
            }
        }else{
            sugg_button.setText("-- --");
        }
    }

    @Override
    public void QRType(String Type, Object typeObject) {
        this.mQRType = Type ;
        Log.d(TAG, "QRType() TYPE = "+mQRType);
        if(typeObject != null){
            switch(mQRType){
                case "EMAIL_ADDRESS" : mEmailCheckClass = new EmailCheckClass(typeObject);
                    break ;
                case "WIFI" : mWifiCheckClass = new WifiCheckClass(typeObject);
                    break ;
                case "SMS" : mSMSCheckClass = new SmsCheckClass(typeObject);
                    break ;
                case "TEL" : mPhoneCheckClass = new PhoneCheckClass(typeObject);
                    break ;
                case "URI":  mURICheckClass = new UriCheckClass(typeObject);
                break;
                case "GEO":  mGeoCheckClass = new GeoCheckClass(typeObject);
                break;
                default:
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MakeCall();
            } else {
                Toast.makeText(this, "Permission Required.", Toast.LENGTH_LONG).show();
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {

                Toast.makeText(Suggestion.this,
                        "something went wrong ", Toast.LENGTH_SHORT).show();
            } else {
                WifiIntent();
            }
        }
    }

    // calling suggestion or call Intent
    private void MakeCall() {
        String number = content_txt.getText().toString();
        if (number.trim().length() > 0) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        123);
            } else {
                if(number.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
                    startActivity(intent);
                } else{
                    String dial = "tel:" + number;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
                    startActivity(intent);
                }
            }
        }
    }

    // Intent for browser
    private void BrowserIntent() {
        String url = content_txt.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    // Intent for search on web
    private void SearchIntent() {
        String web = content_txt.getText().toString();
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, web);
        startActivity(intent);
    }

    // Intent for wifi
    private void WifiIntent() {
        String wifi = content_txt.getText().toString();
        Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        wifiIntent.putExtra("ssid", wifi);
        startActivity(wifiIntent);
    }

    // Intent for search on GoogleMap
    private void MapIntent() {
        String location = content_txt.getText().toString();
        Uri gmIntentUri = Uri.parse(location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    // Intent for sms
    private void SentMessage() {
        String number = mSMSCheckClass.SetSMSNumber();
        String message = mSMSCheckClass.SetSMSBody();
        Log.d(TAG, "SentMessage() num = "+number +"\n message = "+message);
        Uri sms_uri = Uri.parse("sms:");
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.putExtra("address", number);
        sms_intent.putExtra("sms_body", message);
        try {
           // Intent chooser = Intent.createChooser(sms_intent, "Send SMS with :");
            startActivity(sms_intent);
        }catch(Exception e){
            Log.e(TAG, "SentMessage() EXCEPTION "+e.getMessage() );
        }
    }

    //Intent for email
    private void EmailIntent() {

        String emailTo = mEmailCheckClass.SetEmailTo();
        String subject = mEmailCheckClass.SetEmailSubject();
        String body = mEmailCheckClass.SetEmailBody() ;
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(new Uri.Builder().scheme("mailto").build())
                .putExtra(Intent.EXTRA_EMAIL, new String[]{ "<"+emailTo+">" })
                .putExtra(Intent.EXTRA_SUBJECT, subject)
                .putExtra(Intent.EXTRA_TEXT, body);
        Log.d(TAG, "EmailIntent() intent data = "+intent.getData());
        try {
            Intent chooser = Intent.createChooser(intent, "Send email with :");
            startActivity(chooser);
        } catch (Exception e) {
            Log.d(TAG, "EmailIntent() NO EMAIL APP AVAILABLE ... !");
        }
    }

    // Delete QR code
    private  void DeleteQR(){

        if(hasImage(qr_code_img)){
            AlertDialog.Builder builder = new AlertDialog.Builder(Suggestion.this);
            builder.setTitle("Delete QR :");
            builder.setMessage("Are you sure you want to delete QR data ?");
            builder.setIcon(R.drawable.icon_image);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Suggestion.this, "QR deleted ...",Toast.LENGTH_SHORT).show();
                    qr_code_img.setImageDrawable(null);
                    qr_code_img.setBackgroundColor(Color.rgb(128,128,128));
                    content_txt.setText("");
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Suggestion.this, "Cancelled Deletion ...",Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(this, "No QR code to delete ...", Toast.LENGTH_SHORT).show();
        }

    }




    // Share code data
    private void shareQR() {
        // share using File Provider
        if(hasImage(qr_code_img)) {
            Drawable drawable = qr_code_img.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            try {
                File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + "image.png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider", file);

                intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/png");

                startActivity(Intent.createChooser(intent, "Share QR via"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No QR code to share ...", Toast.LENGTH_SHORT).show();
        }
    }



    // save qr code
    private void saveToGallery() {
        // checking for imageview is empty or not.

        if (hasImage(qr_code_img)) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) qr_code_img.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            FileOutputStream fileOutputStream = null;
            File file = Environment.getExternalStorageDirectory();
            File dir = new File(file.getAbsolutePath() + "/Qr code scan");
            dir.mkdir();

            String filename = String.format("%d.png", System.currentTimeMillis());
            File outfile = new File(dir, filename);

            try {
                fileOutputStream = new FileOutputStream(outfile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "QR code saved \nInternal storage/Qr code scan", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d(TAG, "saveToGallery() EXCEPTION : " + e.getMessage());
                Toast.makeText(this, "Could not Download.!!!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No QR Code to Save ...", Toast.LENGTH_SHORT).show();
        }
    }

    // permission for storage
    private boolean checkpermission() {
        // checkpermission returns boolean value.

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.d(TAG, "onPermissionsChecked() Report = " + report);
                        if (report.areAllPermissionsGranted()) {
                            mPermission = true;
                            Toast.makeText(Suggestion.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        } else {
                            mPermission = false;
                            Toast.makeText(Suggestion.this, "Permissions are Required.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
        return mPermission;
    }

    // For calling
    @Override
    public void onClick(View v) {
        if (v == sugg_button) {
            String qrData = content_txt.getText().toString() ;
            if(!qrData.equals("")){
                if (mQRType.equals(TEL.toString())) {
                    MakeCall();
                } else if (mQRType.equals(URI.toString())) {
                    BrowserIntent();
                } else if (mQRType.equals(SMS.toString())) {
                    SentMessage();
                } else if (mQRType.equals(GEO.toString())) {
                    MapIntent();
                } else if (mQRType.equals(EMAIL_ADDRESS.toString())) {
                    EmailIntent();
                } else if (mQRType.equals(WIFI.toString())) {
                    WifiIntent();
                } else {
                    SearchIntent();
                }
            }else{
                Toast.makeText(this, "No data found ...", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == delete_img){
            DeleteQR();
        }
        if(v == copy_img){
            Copycode();
        }
        if(v == share_img){
            shareQR();

        }

    }

    // Action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.download:
//                 checkpermission() called and then if-else
//                 used to confirm for status of permission.
                checkpermission();
                if (!checkpermission()) {
                    checkpermission();
                } else {
                    saveToGallery();
                }
                break;
            case R.id.feedback:
                    FeedbackQR();
                break;

            case R.id.about:
                    Aboutus();
                break;

            default:

        }
        return super.onOptionsItemSelected(item);

    }

// About us page
    private void Aboutus() {
        startActivity(new Intent(getApplicationContext(),AboutUS.class));
    }

    // feedback to QR code
    private void FeedbackQR(){
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(new Uri.Builder().scheme("mailto").build())
                .putExtra(Intent.EXTRA_EMAIL, new String[]{ "ps875761@gmail.com" });
        startActivity(intent);

    }

    // Copy to clipboard
    private void Copycode(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content_txt.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(Suggestion.this, "Copied Successfully",Toast.LENGTH_SHORT).show();

    }


    private boolean hasImage (@NonNull ImageView view){
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    private void GetQRResult(Bitmap bMap){
        if(bMap != null){
            int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(bitmap);
                mTypeCheckClass.GetTypeOfQR(result);
            } catch (Exception e) {
                Log.d(TAG, "GetQRResult() EXCEPTION "+e.getMessage());
            }
        }
    }
    private void changeStatusBarColor() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background_login));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.notification));
        }
    }

}