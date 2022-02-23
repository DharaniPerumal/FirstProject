package in.realtech.ibike_dealer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import static in.realtech.ibike_dealer.TotalStock.CONNECTION_TIMEOUT;
import static in.realtech.ibike_dealer.TotalStock.READ_TIMEOUT;
import static pub.devrel.easypermissions.EasyPermissions.requestPermissions;

@SuppressLint("Registered")
public class Customers extends AppCompatActivity {
    ListView listView;
    ListViewAdapterssss adapter;
    String[] title;
    String[] description;
    String[] text;
    MenuItem searchViewItem;
    SearchView searchView;
    private List<String> dataimei;
    private List<String> imgList;
    ArrayList<Model> arrayList = new ArrayList<Model>();
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    private static final String TAG = "CardListActivity";
    String name;
    String mail;
    String alt_mobile;
    String username;
    String address;
    String added_at;
    String state;
    String image;
    String postal;
    String district;
    AsyncTask asyncTask;
    //    SwipeRefreshLayout pullToRefresh;
    public static final Integer[] animals = {};
    Context context = Customers.this;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String customers = getIntent().getStringExtra("customers");
        this.setTitle("Total Customers" + " (" + customers + " nos.)");
        title = new String[]{};
        description = new String[]{};
        listView = findViewById(R.id.listView1);
        Log.i("checking", "example run");
//        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobile = sharedpreferences.getString("mobile", "mobile");
        new AsyncLogin().execute();
//
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
////                if (asyncTask == null) {
//                    asyncTask =  new AsyncLogin().execute();
//                    pullToRefresh.setRefreshing(false);
////                } else {
////                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
////                        asyncTask.cancel(true);
////                        asyncTask =  new AsyncLogin().execute();
////                        pullToRefresh.setRefreshing(false);
////                    } else {
////                        asyncTask =  new AsyncLogin().execute();
////                        pullToRefresh.setRefreshing(false);
////                    }
////                }
//            }
//        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Customers.this);
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
                        .appendQueryParameter("action", "fitter_users")
                        .appendQueryParameter("fitter_id", mobile);

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();

            dataimei = new ArrayList<String>();
            imgList = new ArrayList<String>();
            Log.i("result", result);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {

                    JSONObject d = data.getJSONObject(i);
                    name = d.getString("name");
                    mail = d.getString("mail");
                    username = d.getString("username");
                    mobile = d.getString("mobile");
                    alt_mobile = d.getString("alt_mobile");
                    address = d.getString("address");
                    district = d.getString("district");
                    state = d.getString("state");
                    image = d.getString("image");
                    Log.i("Images",image);
                    imgList.add(image);

                    postal = d.getString("postal");
                    added_at = d.getString("added_at");
                    dataimei.add(username + ":" + mobile + ":" + alt_mobile + ":" + address + ":" + district + ":" + state + ":" + postal + ":" + added_at);
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Calendar date = Calendar.getInstance();
//                    date.setTime(dateFormat.parse(fitter_date));
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
//                    String date1 = simpleDateFormat.format(date.getTime());

                    title = new String[]{name};
                    description = new String[]{mail};
                    text = new String[]{mobile};
                   String[] imgs = new String[]{image};
                    String[] user = new String[]{username};
                    String[] districts = new String[]{district};
                    String[] sate = new String[]{state};
                    String[] post = new String[]{postal};
                    String[] add = new String[]{added_at};
                    String[] addres = new String[]{address};


                    for (int j = 0; j < title.length; j++) {
                        Model model = new Model(title[j], description[j], text[j],imgs[j],user[j],districts[j],sate[j],post[j],add[j],addres[j]);
                        arrayList.add(model);
                    }
                    Collections.reverse(arrayList);
                    adapter = new ListViewAdapterssss(Customers.this, arrayList, dataimei);
                    listView.setAdapter(adapter);


                }


            } catch (
                    JSONException e) {
                e.printStackTrace();

//            } catch (ParseException e) {
//                e.printStackTrace();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Model model = (Model) parent.getItemAtPosition(position);

                    String header = model.getTitle();
                    String mail = model.getDesc();
                    String mobile = model.getText();
                    String users = model.getName();
                    String districts = model.getDistrict();
                    String states = model.getState();
                    String postals = model.getPost();
                    String addedat = model.getDate();
                    String addess = model.getAddress();
                    int icons = model.getIcon();
                    String imag=model.getImgs();
//                    String headerTitle = (dataimei.get(position));
                    String headerTitle = (dataimei.get(position));
                    Intent intent = new Intent(Customers.this, Customers_detail_display.class);
                    intent.putExtra("name", header);
                    intent.putExtra("mail", mail);
                    intent.putExtra("icons", icons);
                    intent.putExtra("images", imag);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("username", users);
                    intent.putExtra("dis", districts);
                    intent.putExtra("states", states);
                    intent.putExtra("postals", postals);
                    intent.putExtra("added_at", addedat);
                    intent.putExtra("address", addess);
                    intent.putExtra("headerTitle", headerTitle);
                    startActivity(intent);

                    Log.v(TAG, headerTitle);
                    //display value here
                }
            });


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        searchViewItem = menu.findItem(R.id.app_bar_search);
        searchView = (android.widget.SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ListViewAdapterssss extends BaseAdapter {
        //variables
        Context mContext;
        LayoutInflater inflater;
        List<Model> modellist;
        List<String> dataimei;
        ArrayList<Model> arrayList;
        String Images;


        //constructor
        public ListViewAdapterssss(Context context, List<Model> modellist, List<String> dataimei) {
            mContext = context;
            this.modellist = modellist;
            this.dataimei = dataimei;
            inflater = LayoutInflater.from(mContext);
            this.arrayList = new ArrayList<Model>();
            this.arrayList.addAll(modellist);
        }

        public ListViewAdapterssss getFilter() {
            return null;
        }

        public class ViewHolder {
            TextView mTitleTv, mDescTv, mText;
            ImageView icon, calicons;

        }

        @Override
        public int getCount() {
            return modellist.size();
        }

        @Override
        public Object getItem(int i) {
            return modellist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int postition, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.customer_row, null);
                //locate the views in row.xml
                holder.mTitleTv = view.findViewById(R.id.mainTitle);
                holder.mDescTv = view.findViewById(R.id.mainDesc);
                holder.mText = view.findViewById(R.id.maintext);

                holder.calicons = (ImageView) view.findViewById(R.id.imgs);
                holder.calicons.setImageResource(R.drawable.ic_call_black_24dp);

                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
            //set the results into textviews
            holder.mTitleTv.setText(modellist.get(postition).getTitle());
            holder.mDescTv.setText(modellist.get(postition).getDesc());
            holder.mText.setText(modellist.get(postition).getText());

            holder.calicons.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Permissions.check(mContext, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                        private int REQUEST_PHONE_CALL;

                        @Override
                        public void onGranted() {
//                Toast.makeText(mContext, "Phone granted.", Toast.LENGTH_SHORT).show();
                            String phone = modellist.get(postition).getText();
                            String d = "tel:" + phone;
                            Log.i("Make call", "helllo");
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse(d));
                            try {
                                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions((Activity) mContext, Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL);

                                } else {
                                    mContext.startActivity(phoneIntent);
                                }
                                mContext.startActivity(phoneIntent);

                                Log.i("Finished making a call", "");

                                Log.i("Finished making a call", "");
                            } catch (Exception e) {
                            }

                        }

                        private void requestPermissions(Activity mContext, String callPhone, int request_phone_call) {

                        }

                        @Override
                        public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                Toast.makeText(mContext, "permison denied.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

//            holder.icon.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent=new Intent(Customers.this,FullscreenActivity.class);
//                    intent.putExtra("mobile", mobile);
//                    intent.putExtra("images", modellist.get(postition).getImgs());
//                    startActivity(intent);
//
//                }
//            });
            Images = modellist.get(postition).getImgs();


            return view;
        }


        private void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            modellist.clear();
            if (charText.length() == 0) {
                modellist.addAll(arrayList);
            } else {
                for (Model model : arrayList) {
                    if (model.getTitle().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        modellist.add(model);
                    }
                    if (model.getDesc().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        modellist.add(model);

                    }
                }
            }
           adapter.notifyDataSetChanged();
        }

    }


    public class Model {
        String title;
        String desc;
        String text;
        String imgs;
        String user;
        String district;
        String state;
        String post;
        String addedat;
        String address;
        int animals;

        //constructor
        private Model(String title, String desc, String text,String imgs,String user,String district,String state,String post,String addedat,String address) {
            this.title = title;
            this.desc = desc;
            this.text = text;
            this.imgs = imgs;
            this.user = user;
            this.district = district;
            this.state = state;
            this.post = post;
            this.addedat = addedat;
            this.address = address;

        }
        //getters
        public String getTitle() {
            return this.title;
        }
        private String getImgs() {
            return this.imgs;
        }
        public void setTitle(String query) {
            this.title = query;
        }
        public void setDesc(String query) {
            this.desc = query;
        }
        public String getText() {
            return this.text;
        }
        public String getName() {
            return this.user;
        }
        public String getDistrict() {
            return this.district;
        }
        public String getState() {
            return this.state;
        }
        public String getPost() {
            return this.post;
        }
        public String getDate() {
            return this.addedat;
        }
        public String getAddress() {
            return this.address;
        }

        public String getDesc() {
            return this.desc;
        }

        public int getIcon() {
            return this.animals;
        }
    }
}


//
//
//        private void setImage(ImageView imageView, int i, String mobile) {
//            String imgNameServer = imgList.get(i);
//            if (!imgNameServer.equals("null")) {
//                String imgNameLocal = sharedpreferences.getString(mobile + "_pi1", "");
//                if (imgNameLocal.equals(imgNameServer)) {
//                    File imgDir = new File(getFilesDir() + File.separator + "photos");
//                    if (!imgDir.exists()) {
//                        imgDir.mkdirs();
//                    }
//                    File imgFile = new File(imgDir.getPath() + File.separator + imgNameLocal);
//                    if (imgFile.exists()) {
//                        Glide.with(Customers.this)
//                                .asBitmap()
//                                .load(imgFile)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .skipMemoryCache(false)
//                                .dontAnimate()
//                                .placeholder(R.drawable.acc_images)
//                                .centerCrop()
//                                .override(dpToPx(160), dpToPx(160))
//                                .apply(RequestOptions.circleCropTransform())
//                                .listener(new RequestListener<Bitmap>() {
//                                    @Override
//                                    public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                                        Log.v(TAG, "onLoadFailed: " + model);
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//
//                                        return false;
//                                    }
//                                })
//                                .into(imageView);
//                    } else {
//                        deleteLocalFile(imgNameLocal);
//                        downloadVehicleImage(i, mobile, imgNameServer);
//                    }
//                } else {
//                    Log.v(TAG, imgNameLocal + ": " + imgNameServer);
//                    deleteLocalFile(imgNameLocal);
//                    downloadVehicleImage(i, mobile, imgNameServer);
//                }
//            }
//        }
//
//        private int dpToPx(int i) {
//            return 0;
//        }
//
//        private void downloadVehicleImage(final int i, final String mobile, final String img) {
//            Log.v(TAG, "Downloading");
//            String url = "https://igps.io/user_images/" + img;
//            String filePath = getFilesDir() + File.separator + "photos";
//            File imgDir = new File(filePath);
//            if (!imgDir.exists()) {
//                imgDir.mkdirs();
//            }
//            final File imgFile = new File(imgDir.getPath() + File.separator + img);
//            Glide.with(Customers.this)
//                    .asBitmap()
//                    .load(url)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            try {
//                                FileOutputStream outputStream = new FileOutputStream(imgFile, true);
//                                String[] iPathArr = img.split("\\.");
//                                String extension = iPathArr[iPathArr.length - 1];
//                                if (extension.equalsIgnoreCase("png")) {
//                                    resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                                } else {
//                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                                }
//                                outputStream.close();
//                                SharedPreferences.Editor toEdit = sharedpreferences.edit();
//                                toEdit.putString(mobile + "_pi1", img);
//                                toEdit.apply();
//
//                                toEdit.clear();
//                                refillImages(i, mobile, img);
//                            } catch (Exception e) {
//                                Log.v(TAG, "File download error: " + e.getLocalizedMessage());
//                            }
//                        }
//                    });
//        }
//
//        private void refillImages(int index, String mobile, String imgName) {
//
//            SharedPreferences.Editor toEdit = sharedpreferences.edit();
//                                toEdit.putString(mobile + "_jpg6", imgName);
//                                toEdit.apply();
//                                toEdit.clear();
////            genHelper.addPref(mobile + "_pi", imgName, sharedpreferences, false);
//            imgList.set(index, imgName);
//            adapter.refillIcon(imgList);
//            adapter.notifyDataSetChanged();
//        }
//
//        private void refillIcon(List<String> imgList) {
//
//
//        }
//
//        private void deleteLocalFile(String imgName) {
//            try {
//                File imgDir = new File(getFilesDir() + File.separator + "photos");
//                if (!imgDir.exists()) {
//                    imgDir.mkdirs();
//                }
//                File imgFile = new File(imgDir.getPath() + File.separator + imgName);
//                if (imgFile.exists()) {
//                    if (imgFile.delete()) {
//                        Log.v(TAG, imgName + " deleted");
//                    } else {
//                        Log.v(TAG, imgName + " not deleted");
//                    }
//                } else {
//                    Log.v(TAG, imgName + " not exists");
//                }
//            } catch (Exception e) {
//                Log.v(TAG, "File delete error: " + e.getLocalizedMessage());
//            }
//        }





//        private void setImage(ImageView imageView, int i, String mobile,String imagess) {
//            String imgNameServer = imagess;
//            if (!imgNameServer.equals("null")) {
//                String imgNameLocal = sharedpreferences.getString(mobile + "_jpg6", "");
//                if (imgNameLocal.equals(imgNameServer)) {
//                    File imgDir = new File(getFilesDir() + File.separator + "photos");
//                    if (!imgDir.exists()) {
//                        imgDir.mkdirs();
//                    }
//                    File imgFile = new File(imgDir.getPath() + File.separator + imgNameLocal);
//                    if (imgFile.exists()) {
//                        Glide.with(Customers.this)
//                                .asBitmap()
//                                .load(imgFile)
//                                .apply(RequestOptions.circleCropTransform())
//                                .listener(new RequestListener<Bitmap>() {
//                                    @Override
//                                    public boolean onLoadFailed(GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
//                                        Log.v(TAG, "onLoadFailed: " + model);
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
////                                        placeholders.set(i, new BitmapDrawable(getResources(), resource));
//                                        return false;
//                                    }
//                                })
//                                .into(imageView);
//                    } else {
//                        deleteLocalFile(imgNameLocal);
//                        downloadVehicleImage(i, mobile, imgNameServer);
//                    }
//                } else {
//                    Log.v(TAG, imgNameLocal + ": " + imgNameServer);
//                    deleteLocalFile(imgNameLocal);
//                    downloadVehicleImage(i, mobile, imgNameServer);
//                }
//            }
//        }
//
//        private void downloadVehicleImage(final int i, final String mobile, final String img) {
//            Log.v(TAG, "Downloading");
//            String url = "https://igps.io/user_images/user_images/" + img;
//            String filePath = getFilesDir() + File.separator + "photos";
//            File imgDir = new File(filePath);
//            if (!imgDir.exists()) {
//                imgDir.mkdirs();
//            }
//            final File imgFile = new File(imgDir.getPath() + File.separator + img);
//            Glide.with(Customers.this)
//                    .asBitmap()
//                    .load(url)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            try {
//                                FileOutputStream outputStream = new FileOutputStream(imgFile, true);
//                                String[] iPathArr = img.split("\\.");
//                                String extension = iPathArr[iPathArr.length - 1];
//                                if (extension.equalsIgnoreCase("png")) {
//                                    resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                                } else {
//                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                                }
//                                outputStream.close();
//                                SharedPreferences.Editor toEdit = sharedpreferences.edit();
//                                toEdit.putString(mobile + "_jpg6", img);
//                                toEdit.apply();
//                                toEdit.clear();
//
//                            } catch (Exception e) {
//                                Log.v(TAG, "File download error: " + e.getLocalizedMessage());
//                            }
//                        }
//                    });
//        }
//
//
//        private void deleteLocalFile(String imgName) {
//            try {
//                File imgDir = new File(getFilesDir() + File.separator + "photos");
//                if (!imgDir.exists()) {
//                    imgDir.mkdirs();
//                }
//                File imgFile = new File(imgDir.getPath() + File.separator + imgName);
//                if (imgFile.exists()) {
//                    if (imgFile.delete()) {
//                        Log.v(TAG, imgName + " deleted");
//                    } else {
//                        Log.v(TAG, imgName + " not deleted");
//                    }
//                } else {
//                    Log.v(TAG, imgName + " not exists");
//                }
//            } catch (Exception e) {
//                Log.v(TAG, "File delete error: " + e.getLocalizedMessage());
//            }
//        }
