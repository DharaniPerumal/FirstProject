package in.realtech.ibike_dealer.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.realtech.ibike_dealer.Completed_Cancel;
import in.realtech.ibike_dealer.Dashboard;
import in.realtech.ibike_dealer.Model_Class.Panel_Model;
import in.realtech.ibike_dealer.Otpview;
import in.realtech.ibike_dealer.PanelList;
import in.realtech.ibike_dealer.R;

public class Service_Panel_Adapter extends RecyclerView.Adapter<Service_Panel_Adapter.MyViewHolder> {
    private Context mcontext;
    private List<Panel_Model> panelModelArrayList;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private PanelList panelList;

    public Service_Panel_Adapter(Context mcontext, List<Panel_Model> panelModelArrayList, PanelList panelList) {
        this.mcontext = mcontext;
        this.panelModelArrayList = panelModelArrayList;
        this.panelList = panelList;
    }

    public void setData(List<Panel_Model> list) {
        this.panelModelArrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        sharedpreferences = mcontext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final Panel_Model root_response = panelModelArrayList.get(position);
        holder.mUserName.setText(root_response.getUsername());
        holder.mVehileNo.setText(root_response.getVehicleno());
        holder.mAvailableData.setText(root_response.getDt());
        holder.mContact1.setText(root_response.getContact1());
        holder.mcontact2.setText(root_response.getContact2());
        holder.mPlace.setText(root_response.getPlace());
        holder.mSerialNo.setText(root_response.getSerialno());
        holder.btn.setText(root_response.getReassign_count());
        String curentUserId = sharedpreferences.getString("id", "id");
        final String currentUserName = sharedpreferences.getString("name", "name");
        if (root_response.isBtnVisibled) {
            holder.sphiddenlay.setVisibility(View.VISIBLE);
            holder.btn.setVisibility(View.GONE);
        }
        if (root_response.getReassign_count().equals("0")) {
          if (root_response.fitter_name!=null && root_response.fitter_name.equals(currentUserName))
          {
              holder.sphiddenlay.setVisibility(View.VISIBLE);
              holder.btn.setVisibility(View.GONE);
          }
          else if (root_response.fitter_name!=null)
          {
            holder.btn.setText("Accepted by"+root_response.fitter_name);
          }
          else{
              holder.btn.setText("Accept");
          }

        } else if (root_response.getReassign_count().equals("-3")) {
            holder.btn.setText("Completed");
        } else if (Integer.parseInt(root_response.getReassign_count()) > 0) {
            holder.btn.setText("Delay" + root_response.getReassign_count());
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.btn.getText().toString().equals("Accept")) {
//                    new Asyncaccept(root_response,holder.btn.getText().toString(),holder).execute();
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
                    View views = LayoutInflater.from(mcontext).inflate(R.layout.popup_completed, null, false);
                    dialog.setView(views);
                    final AlertDialog dialogInterface = dialog.show();
                    Button btncancel = views.findViewById(R.id.btn_cancel);
                    Button btnOk = views.findViewById(R.id.btn_ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //pranesh equal filterrname
                            root_response.setBtnVisibled(true);
                            holder.sphiddenlay.setVisibility(View.VISIBLE);
                            holder.btn.setVisibility(View.GONE);
                            root_response.fitter_name = currentUserName;
                            new Asyncaccept(root_response, holder.btn.getText().toString(), holder, position).execute();
                            dialogInterface.dismiss();
                        }
                    });

                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onClick(View view) {
                            dialogInterface.dismiss();
                        }
                    });

                    dialog.setCancelable(true);
                }
            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);//alert dialog-mcontext
                View views = LayoutInflater.from(mcontext).inflate(R.layout.service_list, null, false);//taken by view ,so views
                dialog.setView(views);//dialog.setview
                dialog.setCancelable(true);//press back it will cancell
                dialog.show();//show the view
                TextView txtSno = views.findViewById(R.id.txtSNo);
                TextView txtUsername = views.findViewById(R.id.txtUsername);
                TextView txtVehicleNo = views.findViewById(R.id.txtvehicleno);
                TextView txtContact1 = views.findViewById(R.id.txtcontact1);
                TextView txtContact2 = views.findViewById(R.id.txtcontact2);
                TextView txtImei = views.findViewById(R.id.txtimei);
                TextView txtPlace = views.findViewById(R.id.txtplace);
                TextView txtSerial = views.findViewById(R.id.txtserialno);
                TextView txtProblem_category = views.findViewById(R.id.txtproblem_category);
                TextView txtProduct_type = views.findViewById(R.id.txtproduct_type);
                TextView txtProblem = views.findViewById(R.id.txtproblem);
                TextView txtVehicle_avail = views.findViewById(R.id.txtvehicle_avail);
                TextView txtDt = views.findViewById(R.id.txtdt);
                TextView txtReassignReason = views.findViewById(R.id.txtreass_reason);
                TextView txtReassignDate = views.findViewById(R.id.txtreass_date);
                TextView txtReassignCount = views.findViewById(R.id.txtreass_count);
                TextView txtFitterName = views.findViewById(R.id.txtfitter_name);
                TextView txtFitterAcceptTime = views.findViewById(R.id.txtfitter_accept);
                TextView txtCompletionTme = views.findViewById(R.id.txtfitter_completion);
                TextView txtCompliant = views.findViewById(R.id.txtcompliant);
                TextView txtSolution = views.findViewById(R.id.txtsolution);
                TextView txtCashCollected = views.findViewById(R.id.txtcash_collected);
                TextView txtCancelReason = views.findViewById(R.id.txtcancelreason);


                txtSno.setText(txtSno.getText().toString() + root_response.getSno());
                txtUsername.setText(txtUsername.getText().toString() + root_response.getUsername());
                txtVehicleNo.setText(txtVehicleNo.getText().toString() + root_response.getVehicleno());
                txtContact1.setText(txtContact1.getText().toString() + root_response.getContact1());
                txtContact2.setText(txtContact2.getText().toString() + root_response.getContact2());
                txtImei.setText(txtImei.getText().toString() + root_response.getImei());
                txtPlace.setText(txtPlace.getText().toString() + root_response.getPlace());
                txtSerial.setText(txtSerial.getText().toString() + root_response.getSerialno());
                txtProblem_category.setText(txtProblem_category.getText().toString() + root_response.getProblem_category());
                txtProduct_type.setText(txtProduct_type.getText().toString() + root_response.getProduct_type());
                txtProblem.setText(txtProblem.getText().toString() + root_response.getProblem());
                txtVehicle_avail.setText(txtVehicle_avail.getText().toString() + root_response.getVehicle_avail());
                txtDt.setText(txtDt.getText().toString() + root_response.getDt());
                txtReassignReason.setText(txtReassignReason.getText().toString() + root_response.getReassign_reason());
                txtReassignDate.setText(txtReassignDate.getText().toString() + root_response.getReassign_date());
                txtReassignCount.setText(txtReassignCount.getText().toString() + root_response.getReassign_count());
                txtFitterName.setText(txtFitterName.getText().toString() + root_response.getFitter_name());
                txtFitterAcceptTime.setText(txtFitterAcceptTime.getText().toString() + root_response.getFitter_accept_time());
                txtCompletionTme.setText(txtCompletionTme.getText().toString() + root_response.getCompletion_time());
                txtCompliant.setText(txtCompliant.getText().toString() + root_response.getCompliant());
                txtSolution.setText(txtSolution.getText().toString() + root_response.getSolution());
                txtCashCollected.setText(txtCashCollected.getText().toString() + root_response.getCash_collected());
                txtCancelReason.setText(txtCancelReason.getText().toString() + root_response.getCancel_reason());
            }
        });


        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return panelModelArrayList.size();
    }

    public void addData(List<Panel_Model> panelModelArrayList) {
        this.panelModelArrayList = panelModelArrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName, mVehileNo, mAvailableData, mPlace, mContact1, mcontact2, mSerialNo;
        ImageView info;
        Button btn;
        LinearLayout sphiddenlay;
        Button btn_complete;
        Button btn_cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserName = (TextView) itemView.findViewById(R.id.username);
            mVehileNo = (TextView) itemView.findViewById(R.id.vehicle_no);
            mAvailableData = (TextView) itemView.findViewById(R.id.available_date);
            mPlace = (TextView) itemView.findViewById(R.id.place);
            mContact1 = (TextView) itemView.findViewById(R.id.mobile_1);
            mcontact2 = (TextView) itemView.findViewById(R.id.mobile_2);
            mSerialNo = (TextView) itemView.findViewById(R.id.serial_no);
            info = itemView.findViewById(R.id.info);
            btn = itemView.findViewById(R.id.Service_button);
            sphiddenlay = itemView.findViewById(R.id.sp_hiddenbtn);
            btn_complete = itemView.findViewById(R.id.btn_complete);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
        }
    }

    private class Asyncaccept extends AsyncTask<String, String, String> {
        private Panel_Model mPanel;
        URL url = null;
        ProgressDialog pdLoading;
        HttpURLConnection conn;
        String staus;
        String query;
        MyViewHolder holders;
        int positions;

        public Asyncaccept(Panel_Model root_response, String s, MyViewHolder holder, int positions) {
            this.mPanel = root_response;
            this.holders = holder;
            this.staus = s;
            this.positions = positions;
        }

        @Override
        protected void onPreExecute() {
            pdLoading = new ProgressDialog(mcontext);
            pdLoading.show();
            url = null;

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                // Enter URL address where your php file resides
                //url = new URL("htpp://igps.io/sales");

                url = new URL("http://65.0.207.184/service/api/dealer_app_api.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                // "action":"accept","sno":"1","imei":"","vehicle_no":"","fitter_name":"","fitter_accept_time":""
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                // Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("action", staus.toLowerCase(Locale.ROOT))
//                        .appendQueryParameter("sno", mPanel.getSno())
//                        .appendQueryParameter("imei", mPanel.getImei())
//                        .appendQueryParameter("vehicle_no", mPanel.getVehicleno())
//                        .appendQueryParameter("fitter_name", mPanel.getFitter_name())
//                        .appendQueryParameter("fitter_accept_time",formatter.format(date));
                //query = builder.build().getEncodedQuery();
                //Log.d("TAG", "queryData: " + query);
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write("{\"action\":\"accept\"," +
                        "\"sno\":\"\"," +
                        "\"imei\":\"\"," +
                        "\"vehicle_no\":\"\"," +
                        "\"fitter_name\":\"\"," +
                        "\"fitter_accept_time\":\"\"," +
                        " }");
                writer.flush();
                writer.close();
                os.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                pdLoading.dismiss();
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
                    pdLoading.dismiss();
                    return ("unSuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                pdLoading.dismiss();
                return "exception";
            } finally {
                pdLoading.dismiss();
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("TAG", "onPostExecute: servicePanel" + s);
            if (staus.equals("Accept")) {
                panelList.setRefreshData(mPanel.sno, positions, mPanel.isBtnVisibled());
            }
        }
    }
}
