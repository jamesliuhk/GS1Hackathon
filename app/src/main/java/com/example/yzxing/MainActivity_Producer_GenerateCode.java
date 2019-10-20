package com.example.yzxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qrcode.BarcodeActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

public class MainActivity_Producer_GenerateCode extends AppCompatActivity {

    String TAG = "BarcodeActivity";
    final int WHITE = 0xFFFFFFFF;
    final int BLACK = 0xFF000000;

    final int CODE_TYPE_QR_CODE = 0x11;
    final int CODE_TYPE_CODE_128 = 0x12;

    int mType = CODE_TYPE_QR_CODE;
    Handler mHandler = new Handler(Looper.getMainLooper());
    Bitmap bitmap;
    ImageView mBarcodeImageView;
    Button mBarcodeReturn;

    int smallerDimension;
    int barcodeImageWidth;
    int barcodeImageHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__producer__generate_code);
        mBarcodeImageView = findViewById(com.example.qrcode.R.id.barcode_image);
        mBarcodeReturn = findViewById(R.id.barcode_return);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int width = displaySize.x;
        int height = displaySize.y;
        smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 7 / 8;
        barcodeImageWidth = smallerDimension;
        barcodeImageHeight = smallerDimension;
        Log.e(TAG, "onCreate: smallerDimension = " + smallerDimension);



        // from Producer to here
        Intent fromConfirm = getIntent();
        //Todo: you can get date form previous activity at here
        barcodeImageWidth = smallerDimension;
        mType = CODE_TYPE_CODE_128;
        barcodeImageHeight = smallerDimension / 2;
        mBarcodeReturn.setOnClickListener(Barcodereturn);
        String text = "0110028028009000131905151035467121808";  // todo
        encode(text);

    }

    private View.OnClickListener Barcodereturn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentJumpToProducer = new Intent(MainActivity_Producer_GenerateCode.this, MainActivity_Producer.class);

            startActivity(intentJumpToProducer);
        }
    };

    private void encode(String content) {
        Log.e(TAG, "encode: content = " + content);
        if (content == null) {
            return;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(content);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(content, getWantedCodeType(mType)
                    , barcodeImageWidth, barcodeImageHeight, hints);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            mHandler.post(mUpdateImageRunnable);
        } catch (Exception iae) {
            // Unsupported format
            Log.e(TAG, "encode: " + iae.getMessage());
        }
    }

    private Runnable mUpdateImageRunnable = new Runnable() {
        @Override
        public void run() {
            mBarcodeImageView.setImageBitmap(bitmap);
        }
    };

    /**
     * get apposite barcode type
     *
     * @param type
     * @return
     */
    private BarcodeFormat getWantedCodeType(int type) {
        switch (type) {
            case CODE_TYPE_QR_CODE:
                return BarcodeFormat.QR_CODE;
            case CODE_TYPE_CODE_128:
                return BarcodeFormat.CODE_128;
            default:
                return BarcodeFormat.QR_CODE;
        }
    }


    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}