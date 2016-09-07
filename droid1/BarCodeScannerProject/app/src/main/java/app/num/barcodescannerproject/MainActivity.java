package app.num.barcodescannerproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.goebl.david.Webb;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String lastSeenString;
    Webb webb = Webb.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastSeenString = "";
    }

    public void QrScanner(View view){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Do something with the result here
        final Result rawResultTemp = rawResult;
        v.vibrate(150);
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        if( lastSeenString.compareTo(rawResult.getText().toString())!=0 ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String tempText = String.format("Scanner got [%s]%s", rawResultTemp.getBarcodeFormat().toString(), rawResultTemp.getText());
                        JSONObject result = webb
                                .get("https://api.telegram.org/bot267520346:AAGrQYd1DLv3GBUMszqMejLk-oe9xoJdQDU/sendMessage")
                                .param("text", tempText)
                                .param("chat_id", "@logjack")
                                .ensureSuccess()
                                .asJsonObject()
                                .getBody();
                    } catch (Exception e) {
                        Log.d("Some Error", "Failed to store!");
                        Log.e("Reason", e.toString());
                    }

                }
            }).start();
            lastSeenString = rawResult.getText();
            Log.d("lss",lastSeenString);
        }
        //If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
