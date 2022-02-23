package in.realtech.ibike_dealer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class FirstFragment extends Fragment implements AdapterView.OnItemClickListener {


    public static String[] titles = new String[]{};

    public static String[] descriptions = new String[]{};
    public static String[] text = new String[]{};

    public static final Integer[] images = {R.drawable.ac,
            R.drawable.ac, R.drawable.ac, R.drawable.ac};
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    ListView listView;
    List<RowItem> rowItems;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String orderId;
    String dt;
    String activation_code;
    Bitmap bitmap;
    AsyncTask asyncTask;
    SwipeRefreshLayout pullToRefresh;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_first_fragment, container, false);

        listView = view.findViewById(R.id.list);
        rowItems = new ArrayList<RowItem>();
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String mobile = sharedpreferences.getString("mobile", "mobile");
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (asyncTask == null) {
                    asyncTask =    new AsyncLogin().execute(mobile);
                    pullToRefresh.setRefreshing(false);
                  } else {
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                        asyncTask =  new AsyncLogin().execute(mobile);
                        pullToRefresh.setRefreshing(false);
                    } else {
                        asyncTask =   new AsyncLogin().execute(mobile);
                        pullToRefresh.setRefreshing(false);
                    }
                }
            }
        });
        pullToRefresh.setRefreshing(true);



        new AsyncLogin().execute(mobile);



        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }


    private class AsyncLogin extends AsyncTask<String, String, String> implements AdapterView.OnItemClickListener {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("https://igps.io/http.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("action", "dealer_sales")
                        .appendQueryParameter("op", "codes")
                        .appendQueryParameter("fitter_id", params[0])
                        .appendQueryParameter("status", "0");

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("result", result+ "unusedcodes");

            pullToRefresh.setRefreshing(false);
            rowItems.clear();
            JSONArray array = null;
            try {
                array = new JSONArray(result);

                int count = array.length();
                String str2 = Integer.toString(count);
                Log.i("total", str2);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject d = array.getJSONObject(i);
                     orderId = d.getString("orderId");
                     dt = d.getString("dt");
                     String plans = d.getString("plan");
                     activation_code = d.getString("activation_code");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFormat.parse(dt));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String dates = simpleDateFormat.format(date.getTime());

                    titles = new String[]{orderId};
                    descriptions = new String[]{dates};
                    text = new String[]{activation_code};
                    String[] plan = new String[]{plans};

                    for (int j = 0; j < titles.length; j++) {
                        RowItem item = new RowItem(images[j], titles[j], descriptions[j],text[j],plan[j]);
                        rowItems.add(item);
                    }

                    CustomBaseAdapter adapter = new CustomBaseAdapter(getContext(), rowItems);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(this);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

//            Toast toast = Toast.makeText(getContext(),
//                    "Item " + (position + 1) + ": " + rowItems.get(position),
//                    Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();

        }
    }



            public class RowItem {
        private int imageId;
        private String title;
        private String desc;
        private String text,plan;

        public RowItem(int imageId, String title, String desc,String text,String plan) {
            this.imageId = imageId;
            this.title = title;
            this.desc = desc;
            this.text = text;
            this.plan = plan;
        }
        public int getImageId() {
            return imageId;
        }
        public void setImageId(int imageId) {
            this.imageId = imageId;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
                public String getPlan() {
                    return plan;
                }
                public void setPlan(String plan) {
                    this.plan = plan;
                }

         public String getText() {
                    return text;
                }
          public void setText(String text) {
                    this.text = text;
                }
        @Override
        public String toString() {
            return title + "\n" + desc + "\n" + text;
        }
    }


    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        List<RowItem> rowItems;

        public CustomBaseAdapter(Context context, List<RowItem> items) {
            this.context = context;
            this.rowItems = items;
        }

        /*private view holder class*/
        private class ViewHolder {
            ImageView imageView;
            TextView txtTitle;
            TextView txtDesc;
            TextView txtText,plan;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.code_frag3, null);
                holder = new ViewHolder();
                holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
                holder.txtText = (TextView) convertView.findViewById(R.id.text);
                holder.plan = (TextView) convertView.findViewById(R.id.plan);
                holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);

            }

            else {
                holder = (ViewHolder) convertView.getTag();
            }

            RowItem rowItem = (RowItem) getItem(position);

            holder.txtDesc.setText(rowItem.getDesc());
            holder.txtTitle.setText(rowItem.getTitle());
            holder.txtText.setText(rowItem.getText());
            holder.plan.setText(rowItem.getPlan());

//            holder.imageView.setImageResource(rowItem.getImageId());

            QRGEncoder qrgEncoder = new QRGEncoder(rowItem.getText(), null, QRGContents.Type.TEXT, 500);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                // Setting Bitmap to ImageView
                holder.imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v("qr", e.toString());
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(getItem(position));
        }
    }



}
