package com.example.thegoldenratio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.io.ByteStreams;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class faceRecognition extends AppCompatActivity {

    private static final String subscriptionKey = "1bbcc1273dc742fa9bbc495dd2259cbb";

    private static final String uriBase =
            "https://goldenratioonface.cognitiveservices.azure.com/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=true";


    private static final String imageWithFaces =
            "{\"url\":\"https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elm120117wlcoverstory-011copy-1509935070.jpg\"}";

    private static final String faceAttributes =
            "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

    private static final String TAG = "TextGraphic";
    private static final int TEXT_COLOR = Color.RED;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;
    public int index = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        final OkHttpClient client = new OkHttpClient();



        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                URL url = null;
                try {
                    url = new URL(uriBase);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.jennif);
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                byte[] localImageBytes;
                try {
                    localImageBytes = ByteStreams.toByteArray(inputStream);
                    MediaType mediaType = MediaType.parse("application/octet-stream");
                    RequestBody body = RequestBody.create(mediaType,
                            localImageBytes);
                    Request request = new Request.Builder()
                            .url(uriBase).post(body)
                            .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                            .addHeader("Ocp-Apim-Subscription-Region", "centralindia")
                            .addHeader("Content-type", "application/json")
                            .build();
                    client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));


                    Response response = client.newCall(request).execute();
                    String reu = response.body().string();
                    Log.i("result", reu);
                    return reu;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "";
            }
        };
        String result ="";
        try {
            result = task.execute().get();
        }catch (Exception e){
            Log.i("result", e.toString());
        }
        try {
            JSONArray output = new JSONArray(result);
            JSONObject one = output.getJSONObject(0);
            JSONObject faceRectangle = one.getJSONObject("faceRectangle");
            double top = faceRectangle.getDouble("top");
            double left = faceRectangle.getDouble("left");
            double width = faceRectangle.getDouble("width");
            double height = faceRectangle.getDouble("height");

            JSONObject faceLandmarks = one.getJSONObject("faceLandmarks");

            //for nose
            JSONObject noseTip = faceLandmarks.getJSONObject("noseTip");
            final double noseTipX = noseTip.getDouble("x");
            final double noseTipY = noseTip.getDouble("y");

            //JSON parsing for right and left eyes
            JSONObject eyeRightInner = faceLandmarks.getJSONObject("eyeRightInner");
            final double eyeRightInnerX = eyeRightInner.getDouble("x");
            double eyeRightInnerY = eyeRightInner.getDouble("y");
            JSONObject eyeRightOuter = faceLandmarks.getJSONObject("eyeRightOuter");
            final double eyeRightOuterX = eyeRightOuter.getDouble("x");
            final double eyeRightOuterY = eyeRightOuter.getDouble("y");
            JSONObject eyeLeftInner = faceLandmarks.getJSONObject("eyeLeftInner");
            double eyeLeftInnerX = eyeLeftInner.getDouble("x");
            double eyeLeftInnerY = eyeLeftInner.getDouble("y");
            JSONObject eyeLeftOuter = faceLandmarks.getJSONObject("eyeLeftOuter");
            final double eyeLeftOuterX = eyeLeftOuter.getDouble("x");
            final double eyeLeftOuterY = eyeLeftOuter.getDouble("y");
            JSONObject eyeLeftTop = faceLandmarks.getJSONObject("eyeLeftTop");
            double eyeLeftTopX = eyeLeftTop.getDouble("x");
            double eyeLeftTopY = eyeLeftTop.getDouble("y");
            JSONObject eyeLeftBottom = faceLandmarks.getJSONObject("eyeLeftBottom");
            double eyeLeftBottomX = eyeLeftBottom.getDouble("x");
            double eyeLeftBottomY = eyeLeftBottom.getDouble("y");

            //JSON parsing for both pupils
            JSONObject pupilLeft = faceLandmarks.getJSONObject("pupilLeft");
            final double pupilLeftX = pupilLeft.getDouble("x");
            final double pupilLeftY = pupilLeft.getDouble("y");
            JSONObject pupilRight = faceLandmarks.getJSONObject("pupilRight");
            final double pupilRightX = pupilRight.getDouble("x");
            double pupilRightY = pupilRight.getDouble("y");

            //for mouth
            JSONObject mouthLeft = faceLandmarks.getJSONObject("mouthLeft");
            final double mouthLeftX = mouthLeft.getDouble("x");
            final double mouthLeftY = mouthLeft.getDouble("y");
            JSONObject mouthRight = faceLandmarks.getJSONObject("mouthRight");
            final double mouthRightX = mouthRight.getDouble("x");
            double mouthRightY = mouthRight.getDouble("y");

            ImageView forwardArrow = findViewById(R.id.forwardArrow);
            forwardArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index += 1;
                    if(index > 5){
                        index = 1;
                    }

                    final ImageView myImageView = findViewById(R.id.imageView);
                    final Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.jennif);
                    final Paint myRectPaint;
                    myRectPaint = new Paint();
                    myRectPaint.setColor(Color.GREEN);
                    myRectPaint.setStyle(Paint.Style.STROKE);
                    myRectPaint.setStrokeWidth(STROKE_WIDTH);
                    final float scale = getResources().getDisplayMetrics().density;
                    Log.i("scale", scale+"");

                    final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
                    Log.i("result", myBitmap.getWidth() + " "+ myBitmap.getHeight());
                    final Canvas tempCanvas = new Canvas(tempBitmap);

                    tempCanvas.drawBitmap(myBitmap, 0, 0, null);
                    double pp = 0.026458333;

                    switch(index) {
                        //for both eye and nose golden rectangle
                        case(1):
                        tempCanvas.drawRoundRect(new RectF((int)noseTipX, (int)eyeRightOuterY, (int)eyeRightOuterX, (int)noseTipY), 2, 2, myRectPaint);
                        tempCanvas.drawLine((int)eyeRightInnerX, (int)eyeRightOuterY, (int)eyeRightInnerX, (int)noseTipY, myRectPaint);
                        /*tempCanvas.drawRoundRect(new RectF((int)eyeLeftOuterX, (int)eyeLeftOuterY, (int)noseTipX, (int)noseTipY), 2, 2, myRectPaint);
                          tempCanvas.drawLine((int)eyeLeftInnerX, (int)eyeLeftOuterY, (int)eyeLeftInnerX, (int)noseTipY, myRectPaint);*/
                        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                        break;
                        case(2):
                        //for pupils and chin rectangle
                        myRectPaint.setColor(Color.CYAN);
                        double distBetweenPupils = pupilRightX - pupilLeftX;
                        int rectHeight = (int)((distBetweenPupils)*(1.618));
                        double smallRectHeight = distBetweenPupils/1.618;
                        double smallRectY = pupilLeftY + (rectHeight - smallRectHeight);
                        tempCanvas.drawRoundRect(new RectF((int)pupilLeftX, (int)pupilLeftY, (int)pupilRightX, (int)pupilLeftY+rectHeight), 2, 2, myRectPaint);
                        tempCanvas.drawLine((int)pupilLeftX, (int)smallRectY, (int)pupilRightX ,  (int)smallRectY, myRectPaint);
                        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                        break;
                        //for pupil/eyewidth rect
                        /*double pupilRectHeight = eyeLeftTopY - eyeLeftBottomY;
                        double pupilRectBreadth = pupilRectHeight*1.618;
                        double pupilRectX = eyeLeftTopX - (pupilRectBreadth)/2;
                        double pupilRectY = eyeLeftTopY;
                        myRectPaint.setColor(Color.MAGENTA);
                        tempCanvas.drawRoundRect(new RectF((int)pupilRectX, (int)pupilRectY, (int)(pupilRectX + pupilRectBreadth), (int)(pupilRectY+pupilRectHeight)), 2, 2, myRectPaint);
                        */
                        case(3):
                        //for lips rect
                        double lipsRectBreadth = mouthRightX - mouthLeftX;
                        double lipsRectHeight = lipsRectBreadth/1.618;
                        double lipsRectY = mouthLeftY - lipsRectHeight/2;
                        double lipsRectX = mouthLeftX;
                        double lipsRectXdas = mouthRightX;
                        double lipsRectYdas = lipsRectY+lipsRectHeight;
                        double smallRectX = lipsRectX + (lipsRectBreadth/2.618);
                        myRectPaint.setColor(Color.MAGENTA);
                        tempCanvas.drawRoundRect(new RectF((int)lipsRectX, (int)lipsRectY, (int)lipsRectXdas, (int)lipsRectYdas), 2, 2, myRectPaint);
                        tempCanvas.drawLine((float)smallRectX, (float)lipsRectY, (float)smallRectX, (float)lipsRectYdas, myRectPaint);
                        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                        break;
                        case(4):
                        //for pupilNoseRect
                        double pnX = pupilLeftX;
                        double pnY = pupilLeftY;
                        double pnRectBreadth = pupilRightX - pupilLeftX;
                        double pnRectHeight = pnRectBreadth/1.618;
                        double pnXdas = pupilRightX;
                        double pnYdas = pnY+pnRectHeight;
                        double smallpnRectY = pnRectHeight/2.618;
                        myRectPaint.setColor(Color.BLUE);
                        tempCanvas.drawRoundRect(new RectF((int)pnX, (int)pnY, (int)pnXdas, (int)pnYdas), 2, 2, myRectPaint);
                        tempCanvas.drawLine((float)pnX, (float)(pnYdas-smallpnRectY), (float)pnXdas, (float)(pnYdas-smallpnRectY), myRectPaint);
                        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                        break;
                        case(5):
                        //for center-inner-width
                        double ciwSmallRectX = eyeLeftOuterX;
                        double ciwSmallRectY = eyeLeftOuterY;
                        double ciwRectRightTopX = noseTipX;
                        double ciwRectRightTopY = eyeLeftOuterY;
                        double ciwSmallRectDista = noseTipX - eyeLeftOuterX;
                        double ciwSmallRectDistb = ciwSmallRectDista/1.618;
                        double ciwRectX = eyeLeftOuterX - ciwSmallRectDistb;
                        double ciwRectY = eyeLeftOuterY;
                        double ciwRectBreadth = noseTipX - ciwRectX;
                        double ciwRectHeight = ciwRectBreadth * 1.618;
                        double ciwRectXdas = ciwRectX + ciwRectBreadth;
                        double ciwRectYdas = ciwRectY + ciwRectHeight;
                        myRectPaint.setColor(Color.BLACK);
                        tempCanvas.drawRoundRect(new RectF((int)ciwRectX, (int)ciwRectY, (int)ciwRectXdas, (int)ciwRectYdas), 2, 2, myRectPaint);
                        tempCanvas.drawLine((float)ciwSmallRectX, (float)ciwSmallRectY, (float)ciwSmallRectX, (float)ciwSmallRectY + (float)ciwRectHeight, myRectPaint);
                        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                        break;
                    }
                }
            });
            ImageView backwardArrow = findViewById(R.id.backWardArrow);
            backwardArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index -= 1;
                    if(index < 1){
                        index = 5;
                    }

                    final ImageView myImageView = findViewById(R.id.imageView);
                    final Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.jennif);
                    final Paint myRectPaint;
                    myRectPaint = new Paint();
                    myRectPaint.setColor(Color.GREEN);
                    myRectPaint.setStyle(Paint.Style.STROKE);
                    myRectPaint.setStrokeWidth(STROKE_WIDTH);
                    final float scale = getResources().getDisplayMetrics().density;
                    Log.i("scale", scale+"");

                    final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
                    Log.i("result", myBitmap.getWidth() + " "+ myBitmap.getHeight());
                    final Canvas tempCanvas = new Canvas(tempBitmap);

                    tempCanvas.drawBitmap(myBitmap, 0, 0, null);
                    double pp = 0.026458333;

                    switch(index) {
                        //for both eye and nose golden rectangle
                        case(1):
                            tempCanvas.drawRoundRect(new RectF((int)noseTipX, (int)eyeRightOuterY, (int)eyeRightOuterX, (int)noseTipY), 2, 2, myRectPaint);
                            tempCanvas.drawLine((int)eyeRightInnerX, (int)eyeRightOuterY, (int)eyeRightInnerX, (int)noseTipY, myRectPaint);
                        /*tempCanvas.drawRoundRect(new RectF((int)eyeLeftOuterX, (int)eyeLeftOuterY, (int)noseTipX, (int)noseTipY), 2, 2, myRectPaint);
                          tempCanvas.drawLine((int)eyeLeftInnerX, (int)eyeLeftOuterY, (int)eyeLeftInnerX, (int)noseTipY, myRectPaint);*/
                            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                            break;
                        case(2):
                            //for pupils and chin rectangle
                            myRectPaint.setColor(Color.CYAN);
                            double distBetweenPupils = pupilRightX - pupilLeftX;
                            int rectHeight = (int)((distBetweenPupils)*(1.618));
                            double smallRectHeight = distBetweenPupils/1.618;
                            double smallRectY = pupilLeftY + (rectHeight - smallRectHeight);
                            tempCanvas.drawRoundRect(new RectF((int)pupilLeftX, (int)pupilLeftY, (int)pupilRightX, (int)pupilLeftY+rectHeight), 2, 2, myRectPaint);
                            tempCanvas.drawLine((int)pupilLeftX, (int)smallRectY, (int)pupilRightX ,  (int)smallRectY, myRectPaint);
                            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                            break;
                        //for pupil/eyewidth rect
                        /*double pupilRectHeight = eyeLeftTopY - eyeLeftBottomY;
                        double pupilRectBreadth = pupilRectHeight*1.618;
                        double pupilRectX = eyeLeftTopX - (pupilRectBreadth)/2;
                        double pupilRectY = eyeLeftTopY;
                        myRectPaint.setColor(Color.MAGENTA);
                        tempCanvas.drawRoundRect(new RectF((int)pupilRectX, (int)pupilRectY, (int)(pupilRectX + pupilRectBreadth), (int)(pupilRectY+pupilRectHeight)), 2, 2, myRectPaint);
                        */
                        case(3):
                            //for lips rect
                            double lipsRectBreadth = mouthRightX - mouthLeftX;
                            double lipsRectHeight = lipsRectBreadth/1.618;
                            double lipsRectY = mouthLeftY - lipsRectHeight/2;
                            double lipsRectX = mouthLeftX;
                            double lipsRectXdas = mouthRightX;
                            double lipsRectYdas = lipsRectY+lipsRectHeight;
                            double smallRectX = lipsRectX + (lipsRectBreadth/2.618);
                            myRectPaint.setColor(Color.MAGENTA);
                            tempCanvas.drawRoundRect(new RectF((int)lipsRectX, (int)lipsRectY, (int)lipsRectXdas, (int)lipsRectYdas), 2, 2, myRectPaint);
                            tempCanvas.drawLine((float)smallRectX, (float)lipsRectY, (float)smallRectX, (float)lipsRectYdas, myRectPaint);
                            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                            break;
                        case(4):
                            //for pupilNoseRect
                            double pnX = pupilLeftX;
                            double pnY = pupilLeftY;
                            double pnRectBreadth = pupilRightX - pupilLeftX;
                            double pnRectHeight = pnRectBreadth/1.618;
                            double pnXdas = pupilRightX;
                            double pnYdas = pnY+pnRectHeight;
                            double smallpnRectY = pnRectHeight/2.618;
                            myRectPaint.setColor(Color.BLUE);
                            tempCanvas.drawRoundRect(new RectF((int)pnX, (int)pnY, (int)pnXdas, (int)pnYdas), 2, 2, myRectPaint);
                            tempCanvas.drawLine((float)pnX, (float)(pnYdas-smallpnRectY), (float)pnXdas, (float)(pnYdas-smallpnRectY), myRectPaint);
                            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                            break;
                        case(5):
                            //for center-inner-width
                            double ciwSmallRectX = eyeLeftOuterX;
                            double ciwSmallRectY = eyeLeftOuterY;
                            double ciwRectRightTopX = noseTipX;
                            double ciwRectRightTopY = eyeLeftOuterY;
                            double ciwSmallRectDista = noseTipX - eyeLeftOuterX;
                            double ciwSmallRectDistb = ciwSmallRectDista/1.618;
                            double ciwRectX = eyeLeftOuterX - ciwSmallRectDistb;
                            double ciwRectY = eyeLeftOuterY;
                            double ciwRectBreadth = noseTipX - ciwRectX;
                            double ciwRectHeight = ciwRectBreadth * 1.618;
                            double ciwRectXdas = ciwRectX + ciwRectBreadth;
                            double ciwRectYdas = ciwRectY + ciwRectHeight;
                            myRectPaint.setColor(Color.BLACK);
                            tempCanvas.drawRoundRect(new RectF((int)ciwRectX, (int)ciwRectY, (int)ciwRectXdas, (int)ciwRectYdas), 2, 2, myRectPaint);
                            tempCanvas.drawLine((float)ciwSmallRectX, (float)ciwSmallRectY, (float)ciwSmallRectX, (float)ciwSmallRectY + (float)ciwRectHeight, myRectPaint);
                            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                            break;
                    }
                }
            });
        }catch (Exception e){
            Log.i("result", e.toString());
        }
    }

}
