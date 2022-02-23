package in.realtech.ibike_dealer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dateselect extends AppCompatActivity {

    private TextView txtContent;
    private Animation animationUp;
    private Animation animationDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        txtContent = (TextView) findViewById(R.id.title_text);
        TextView txtTitle = (TextView) findViewById(R.id.content_text);
        txtContent.setVisibility(View.GONE);


        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtContent.isShown()) {
                    txtContent.setVisibility(View.GONE);
                    txtContent.startAnimation(animationUp);
                } else {
                    txtContent.setVisibility(View.VISIBLE);
                    txtContent.startAnimation(animationDown);
                }
            }
        });
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.dash_calender, menu);
          final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            View view = MenuItemCompat.getActionView(searchMenuItem);
          ImageButton  closeSearchViewImageButton = (ImageButton) view
                    .findViewById(R.id.closeImageButton);
            closeSearchViewImageButton
                    .setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(View v)
                        {
                            MenuItemCompat.collapseActionView(searchMenuItem);
                        }
                    });
          final EditText searchEditText = (EditText) view
                    .findViewById(R.id.searchEditText);
            searchEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Dateselect.this);
                    LayoutInflater inflater = Dateselect.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.activity_dateselect, null);
                    dialogBuilder.setView(dialogView);
                   final CalendarPickerView calendar = dialogView.findViewById(R.id.calendar_view);
                    Calendar pastYear = Calendar.getInstance();
                    pastYear.add(Calendar.YEAR, -1);
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.DATE,1);
                    calendar.init(pastYear.getTime(), nextYear.getTime()) //
                            .inMode(CalendarPickerView.SelectionMode.RANGE)
                            .withSelectedDate(new Date());
                    calendar.getSelectedDates();
                    Button btnn = dialogView.findViewById(R.id.btt);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    btnn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           Date first = calendar.getSelectedDates().get(0);
                           Date last = calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1);
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String strDate = dateFormat.format(first);
                            System.out.println("array"+calendar.getSelectedDates());
                            System.out.println("first"+first);
                            System.out.println("last"+last);

                            DateFormat datefor = new SimpleDateFormat("dd-MM-yyyy");
                            String strDa = datefor.format(last);
                            searchEditText.setText(strDate +" To " +strDa);
                            if(calendar.getSelectedDates().size()<2){
                                Toast.makeText(Dateselect.this,"Select From and To, (ex)This to this. ",Toast.LENGTH_SHORT).show();
                            }else {

                                alertDialog.dismiss();
                            }
                        }
                    });
//                                             AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            });

            return true;
        }


}


