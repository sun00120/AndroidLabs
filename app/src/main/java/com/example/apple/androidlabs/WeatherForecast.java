package com.example.apple.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "weatherForecast";
    protected static final String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    protected ProgressBar progressBar;
    protected TextView currentTemperature;
    protected TextView minTemperature;
    protected TextView maxTemperature;
    protected TextView windSpeed;
    protected ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        currentTemperature = findViewById(R.id.currentTemperature);
        minTemperature = findViewById(R.id.minTemperature);
        maxTemperature = findViewById(R.id.maxTemperature);
        imageView = findViewById(R.id.imageView3);
        windSpeed = findViewById(R.id.wind_speed);
        ForecastQuery fq = new ForecastQuery();
        fq.execute();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {


        Bitmap bitmap;
        String maxT;
        String minT;
        String currentT;
        String iconName;
        String wind_speed;


        @Override
        protected String doInBackground(String... args) {
            XmlPullParser parser = Xml.newPullParser();
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in,"UTF-8");

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature")) {
                        currentT = parser.getAttributeValue(null, "value");
                        publishProgress(25);

                        minT = parser.getAttributeValue(null, "min");
                        maxT = parser.getAttributeValue(null, "max");
                        publishProgress(50);
                    }
                    if (parser.getName().equals("speed")) {
                        wind_speed = parser.getAttributeValue(null, "value");
                        publishProgress(75);
                    }
                    if (parser.getName().equals("weather")) {
                        iconName = parser.getAttributeValue(null, "icon");
                        String immageFile = iconName + ".png";
                        if (fileExistance(immageFile)) {
                            FileInputStream fis = null;
                            try {
                                fis = openFileInput(immageFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            bitmap = BitmapFactory.decodeStream(fis);
                            Log.i(ACTIVITY_NAME, "Image exists");
                        } else {
                            URL immageUrl = new URL("http://openweathermap.org/img/w/" + immageFile);
                            Log.i(ACTIVITY_NAME, "Searching " + immageFile);
                            bitmap = HttpUtils.getImage(immageUrl);
                            FileOutputStream outputStream = openFileOutput(immageFile, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "A new image is added");
                        }
                    }
                }

            } catch (IOException e){
                e.printStackTrace();
            } catch (XmlPullParserException e){
                e.printStackTrace();
            }
            publishProgress(100);
            Log.i(ACTIVITY_NAME,"Background processing completed");
            return "Done";
        }


        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
            Log.i(ACTIVITY_NAME,"In onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String result) {
            String degree =" " +Character.toString((char)0x00B0)+" ";
            maxTemperature.setText(maxTemperature.getText()+" "+maxT+degree+"C");
            minTemperature.setText(minTemperature.getText()+" "+minT+degree+"C");
            currentTemperature.setText(currentTemperature.getText()+" "+currentT+degree+"C");
            windSpeed.setText(windSpeed.getText()+" "+wind_speed+" m/s");
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }


    static class HttpUtils {
        public static Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}



