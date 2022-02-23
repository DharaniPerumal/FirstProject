package in.realtech.ibike_dealer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] title;
    private final String[] desc;
    private final String[] text;
    private final Integer[] imageId;
    public CustomList(Activity context,
                      String[] title,String[] desc,String[] text, Integer[] imageId) {
        super(context, R.layout.list_single, title);
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.text = text;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtdesc = (TextView) rowView.findViewById(R.id.txt1);
        TextView txttext = (TextView) rowView.findViewById(R.id.txt2);

//        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(title[position]);
        txtdesc.setText(desc[position]);
        txttext.setText(text[position]);
//        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
