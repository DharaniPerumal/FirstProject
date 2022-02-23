package in.realtech.ibike_dealer;


        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.fragment.app.Fragment;

public class payment_step1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.payment_fragment, container, false);
//        TextView textView=v.findViewById(R.id.text);
//        textView.setText("First Fragment");
        return v;
    }
}