package com.vuzix.sample.barcodefromintent;

import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.ScannerIntent;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE_SCAN = 90001;  // Must be unique within this Activity
    private Button mButtonScan;
    private EditText mTextEntryField;
    private TextView display;
    String scanstring;
    String thisusuario = "";
    boolean primer=true;

    /**webid permitidos:*/
    String[] webidpermit = new String[]{
            "F1DPW6Wlk0_Utku9vWTvxg45oACAAAAAUElTUlYxXEJBOkNPTkMuMQ",
            "webid en base64"
    };
    List<String> listwebid = Arrays.asList(webidpermit);

    /**usuarios permitidos*/
    String[] userpermit = new String[]{
            "d2ViYXBpdXNlcjohdHJ5My4xNHdlYmFwaSE=",
            "user2:password2 en base64"
    };
    List<String> listusers = Arrays.asList(userpermit);

    Downloader downloader = new Downloader(); //Crea clase asincrónica.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.displayPunto);
        display.setText("Escanee un usuario");
        mButtonScan = (Button) findViewById(R.id.btn_scan_barcode);
        mButtonScan.requestFocusFromTouch();
        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                OnScanClick();
            }
        });
    }

    private void OnScanClick() {
        Intent scannerIntent = new Intent(ScannerIntent.ACTION);
        try {
            startActivityForResult(scannerIntent, REQUEST_CODE_SCAN);
        } catch (ActivityNotFoundException activityNotFound) {
            Toast.makeText(this, R.string.only_on_m300, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SCAN:
                if (resultCode == Activity.RESULT_OK) {
                    ScanResult scanResult = data.getParcelableExtra(ScannerIntent.RESULT_EXTRA_SCAN_RESULT);
                    scanstring=scanResult.getText();

                    if (primer) {
                        if (listusers.contains(scanstring)) {
                            thisusuario = scanstring;
                            primer = false;
                            display.setText("Listo para usar");
                        } else {
                            display.setText("Usuario invalido, reintente");
                        }
                    } else
                    if (listwebid.contains(scanstring)) {
                        downloader.execute(scanstring, null, null);
                    } else {
                        display.setText("QR invalido, reintente");
                    }
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class Downloader extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            URLBasicAuth uba = new URLBasicAuth(params[0]);
            Parser par = new Parser(uba.conectar());
            return par.parse(); //Este return es un String que va al onPostExecute() siguiente.
        }

        @Override
        protected void onPostExecute(String s) {
            display.setText("Atributo: "+ s + ". Listo para escanear");
        }
    }
}
