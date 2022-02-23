package in.realtech.ibike_dealer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class content_dashboard extends AppCompatActivity implements View.OnClickListener {
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_dashboard);
        cardView = (CardView) findViewById(R.id.card_view9);

        cardView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent i ;
        switch (view.getId()) {
            case R.id.card_view9:
                i = new Intent(this, Panel_List.class);
                startActivity(i);
        }}

}
