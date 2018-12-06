package com.example.haiba.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ProgressBar progress;
    private TextView currT;
    private TextView minT;
    private TextView maxT;
    private TextView windS;
    private ImageView image;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        currT = findViewById(R.id.currT);
        minT = findViewById(R.id.minT);
        maxT = findViewById(R.id.maxT);
        windS = findViewById(R.id.windSpeed);
        image = findViewById(R.id.currW);

        ForecastQuery fq  =new ForecastQuery();
        fq.execute();

    }

    public class ForecastQuery extends AsyncTask<String,Integer,String>{
        String windSpeed;
        String minTemp;
        String maxTemp;
        String currTemp;
        Bitmap bitmap;
        String iconName;

        @Override
        protected String doInBackground(String...strings){
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream in = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

                while(parser.next() != XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }
                    if(parser.getName().equals("temperature")){

                        currTemp = parser.getAttributeValue(null,"value");
                        publishProgress(25);

                        minTemp=parser.getAttributeValue(null,"min");

                        maxTemp = parser.getAttributeValue(null,"max");
                        publishProgress(50);

                    }

                    if(parser.getName().equals("speed")){
                        windSpeed = parser.getAttributeValue(null,"value");
                        publishProgress(75);
                    }

                    if(parser.getName().equals("weather")){
                        iconName = parser.getAttributeValue(null,"icon");
                    }


                }
                conn.disconnect();

                String imagefile=iconName + ".png";

                if(fileExistance(iconName)){
                    FileInputStream fis = null;
                    try{
                        fis=openFileInput(imagefile);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                    bitmap = BitmapFactory.decodeStream(fis);
                    Log.i(ACTIVITY_NAME,"Local file " + imagefile);
                }else{
                    URL imageURL = new URL("http://openweathermap.org/img/w/" + imagefile);
                    bitmap=getImage(imageURL);
                    Log.i(ACTIVITY_NAME,"Download" + imagefile);
                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    publishProgress(100);

                }

            }catch(IOException e){
                e.printStackTrace();
            }catch(XmlPullParserException e){
                e.printStackTrace();
            }



            return null;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer...value){
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result){
            currT.setText("current temperature is "+currTemp);
            minT.setText("min temperature is "+minTemp);
            maxT.setText("max temperature is "+maxTemp);
            windS.setText("wind speed is "+windSpeed);
            image.setImageBitmap(bitmap);
            progress.setVisibility(View.INVISIBLE);
        }




    }


        public Bitmap getImage(URL url){
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode == 200){
                    return BitmapFactory.decodeStream(connection.getInputStream());
                }else{
                    return null;
                }
            }catch(Exception e){
                return null;
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
            }
        }

}
