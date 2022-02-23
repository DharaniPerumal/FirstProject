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

public class payment_step2 extends Fragment implements AdapterView.OnItemClickListener {


    public static final String[] titles = new String[]{"Allahabad Bank",
            "Andhra Bank", "Axis Bank", "Bank of Baroda - Corporate","Bank of Baroda - Retail","Bank of India","Bank of Maharashtra","Canara Bank","Catholic Syrian Bank","Central Bank of India","City Union Bank","Corporation Bank","DBS Bank Ltd","DCB Bank - Corporate","DCB Bank - Personal","Deutsche Bank","Dhanlakshmi Bank","Federal Bank","HDFC Bank","ICICI Bank","IDBI Bank","Indian Bank","Indian Overseas Bank","IndusInd Bank","Jammu and Kashmir Bank","Karnataka Bank Ltd","Karur Vysya Bank","Kotak Mahindra Bank","Laxmi Vilas Bank","Oriental Bank of Commerce","Punjab & Sind Bank","Punjab National Bank - Corporate","Punjab National Bank - Retail","Saraswat Bank","South Indian Bank","Standard Chartered Bank","State Bank Of India","Tamilnad Mercantile Bank Ltd","UCO Bank","Union Bank of India","United Bank of India","Vijaya Bank","Yes Bank Ltd","TEST Bank"};

    public static final String[] descriptions = new String[]{"3001","3002","3003","3060","3005","3006","3007","3009","3010","3011","3012","3013","3017","3062","3018","3016","3019","3020","3021","3022","3023","3026","3027","3028","3029","3030","3031","3032","3033","3035","3037","3065","3038","3040","3042","3043","3044","3052","3054","3055","3056","3057","3058","3333"};

    public static final Integer[] images = {R.drawable.allahabad_bank, R.drawable.andhra_bank, R.drawable.axis_bank, R.drawable.bank_of_baroda_corporate,R.drawable.bank_of_baroda_retail, R.drawable.bank_of_india, R.drawable.bank_of_maharashtra, R.drawable.canara_bank,R.drawable.catholic_syrian_bank, R.drawable.central_bank_of_india , R.drawable.city_union_bank, R.drawable.corporation_bank ,R.drawable.dbs_bank_ltd, R.drawable.dcb_bank_corporate, R.drawable.dcb_bank_personal , R.drawable.deutsche_bank ,R.drawable.dhanlakshmi_bank , R.drawable.federal_bank , R.drawable.hdfc_bank, R.drawable.icici_bank,R.drawable.idbi_bank, R.drawable.indian_bank, R.drawable.indian_overseas_bank, R.drawable.ind_bank,R.drawable.jammu_and_kashmir_bank, R.drawable.karnataka_bank_ltd, R.drawable.karur_vysya_bank, R.drawable.kotak_mahindra_bank,R.drawable.laxmi_vilas_bank, R.drawable.oriental_bank_of_commerce, R.drawable.punjab_sind_bank, R.drawable.punjab_national_bank_corporate,R.drawable.punjab_national_bank_retail, R.drawable.saraswat_bank, R.drawable.south_indian_bank, R.drawable.standard_chartered_bank,R.drawable.state_bank_of_india, R.drawable.tamilnad_mercantile_bank_ltd, R.drawable.uco_bank, R.drawable.union_bank_of_india,R.drawable.united_bank_of_india, R.drawable.vijaya_bank, R.drawable.yes_bank_ltd, R.drawable.test_bank};

    ListView listView;
    List<RowItem> rowItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payment_fragment2, container, false);
//        TextView textView=v.findViewById(R.id.text);
//        textView.setText("Second Fragment");

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }

        listView = (ListView) v.findViewById(R.id.list);
        CustomBaseAdapter adapter = new CustomBaseAdapter(getContext(), rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return v;

}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
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
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.netbanking, null);
                holder = new ViewHolder();
                holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
                holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            RowItem rowItem = (RowItem) getItem(position);

            holder.txtDesc.setText(rowItem.getDesc());
            holder.txtTitle.setText(rowItem.getTitle());

            holder.txtDesc.setVisibility(View.INVISIBLE);

            holder.imageView.setImageResource(rowItem.getImageId());

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

    public class RowItem {
        private int imageId;
        private String title;
        private String desc;

        public RowItem(int imageId, String title, String desc) {
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
