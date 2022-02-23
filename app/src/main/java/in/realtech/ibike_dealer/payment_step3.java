package in.realtech.ibike_dealer;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

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

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class payment_step3 extends Fragment implements AdapterView.OnItemClickListener {


    public static final String[] titles1 = new String[]{"FreeCharge","MobiKwik","OLA Money","Reliance Jio Money","Airtel Money","Paytm","Amazon Pay","Phonepe"};
    public static final String[] descriptions1 = new String[]{"4001","4002","4003","4004","4006","4007","4008","4009"};
    public static final Integer[] images1 = {R.drawable.freecharge,R.drawable.mobikwik,R.drawable.olamoney,R.drawable.reliance_jio_money,R.drawable.airtel_money,R.drawable.paytm,R.drawable.amazon_pay,R.drawable.phonepe};
    ListView listView1;
    List<RowItem1> rowItems1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payment_fragment2, container, false);
//        TextView textView=v.findViewById(R.id.text);
//        textView.setText("Second Fragment");

        rowItems1 = new ArrayList<RowItem1>();
        for (int i = 0; i < titles1.length; i++) {
            RowItem1 item = new RowItem1(images1[i], titles1[i], descriptions1[i]);
            rowItems1.add(item);
        }

        listView1 = (ListView) v.findViewById(R.id.list);
        CustomBaseAdapter1 adapter = new CustomBaseAdapter1(getContext(), rowItems1);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(this);
        return v;

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getContext(),
                "Item " + (position + 1) + ": " + rowItems1.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
    public class CustomBaseAdapter1 extends BaseAdapter {
        Context context;
        List<RowItem1> rowItems1;

        public CustomBaseAdapter1(Context context, List<RowItem1> items) {
            this.context = context;
            this.rowItems1 = items;
        }

        /*private view holder class*/
        private class ViewHolder {
            ImageView imageView1;
            TextView txtTitle1;
            TextView txtDesc1;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.wallet, null);
                holder = new ViewHolder();
                holder.txtDesc1 = (TextView) convertView.findViewById(R.id.descwallet);
                holder.txtTitle1 = (TextView) convertView.findViewById(R.id.titlewallet);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.iconwallet);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            RowItem1 rowItem1 = (RowItem1) getItem(position);

            holder.txtDesc1.setText(rowItem1.getDesc());
            holder.txtTitle1.setText(rowItem1.getTitle());

            holder.txtDesc1.setVisibility(View.INVISIBLE);

            holder.imageView1.setImageResource(rowItem1.getImageId());

            return convertView;
        }

        @Override
        public int getCount() {
            return rowItems1.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems1.indexOf(getItem(position));
        }
    }

    public class RowItem1 {
        private int imageId;
        private String title;
        private String desc;

        public RowItem1(int imageId, String title, String desc) {
            this.imageId = imageId;
            this.title = title;
            this.desc = desc;
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
        @Override
        public String toString() {
            return title + "\n" + desc;
        }
    }
}