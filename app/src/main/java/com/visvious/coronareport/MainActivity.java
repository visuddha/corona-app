package com.visvious.coronareport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String URL_API = "https://www.hpb.health.gov.lk/api/get-current-statistical";
    private static final Object TAG_REQUEST_QUEUE = MainActivity.class.getName();
    private static final String TAG = MainActivity.class.getSimpleName();
    String tag_json_obj = "json_obj_req";


    private TextView mReportDate;
    private TextView mTotalNumber;
    private TextView mTotalCases;
    private TextView mNewCases;
    private TextView mDeaths;
    private TextView mRecovered;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReportDate = findViewById(R.id.report_date);
        mTotalNumber = findViewById(R.id.tot_number);
        mTotalCases = findViewById(R.id.tot_cases);
        mNewCases = findViewById(R.id.tot_new_cases);
        mDeaths = findViewById(R.id.tot_dealth_cases);
        mRecovered = findViewById(R.id.tot_recovered_cases);

        request();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void request() {
        // ロードダイアログ表示
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL_API, null,
                new Response.Listener<JSONObject>() {


                    // レスポンス受信のリスナー
                    @Override
                    public void onResponse(JSONObject response) {
                        // ログ出力
                        Log.d(TAG, "onResponse: " + response.toString());

                        // ロードダイアログ終了
                        pDialog.hide();

                        try {

                            JSONObject data = response.getJSONObject("data");
                            String update_date_time = data.getString("update_date_time");

                            //2020-03-17 08:14:26
                            SimpleDateFormat dateFormatOfStringInDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date d1 = dateFormatOfStringInDB.parse(update_date_time);
                                SimpleDateFormat dateFormatYouWant = new SimpleDateFormat("dd.MM.yyyy hh:mm a");
                                String sCertDate = dateFormatYouWant.format(d1);
                                mReportDate.setText(sCertDate);
                            }catch (Exception e){
                                mReportDate.setText(update_date_time);
                            }



                            String local_total_cases = data.getString("local_total_cases");
                            mTotalCases.setText(local_total_cases);

                            String local_new_cases = data.getString("local_new_cases");
                            mNewCases.setText(local_new_cases);

                            String local_total_number_of_individuals_in_hospitals = data.getString("local_total_number_of_individuals_in_hospitals");
                            mTotalNumber.setText(local_total_number_of_individuals_in_hospitals);

                            String local_deaths = data.getString("local_deaths");
                            mDeaths.setText(local_deaths);

                            String local_recovered = data.getString("local_recovered");
                            mRecovered.setText(local_recovered);

//
//                            String title = response.getString("title");
//                            textview_title.setText(title);
//
//                            // 天気概況文を取得、テキストビューに登録
//                            JSONObject description = response.getJSONObject("description");
//                            String description_text = description.getString("text");
//                            textview_description.setText(description_text);
//
//                            // 天気予報の予報日毎の配列を取得
//                            JSONArray forecasts = response.getJSONArray("forecasts");
//                            for (int i = 0; i < forecasts.length(); i++) {
//                                JSONObject forecast = forecasts.getJSONObject(i);
//                                // 日付を取得
//                                String date = forecast.getString("date");
//                                // 予報を取得
//                                String telop = forecast.getString("telop");
//                                // リストビューに登録
//                                adapter.add(date + ":" + telop);
//                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    // リクエストエラーのリスナー
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.d(TAG, "Error: " + error.getMessage());
                        if (error instanceof NetworkError) {
                        } else if (error instanceof ServerError) {
                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof TimeoutError) {
                        }
                    }

                }
        );

//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
