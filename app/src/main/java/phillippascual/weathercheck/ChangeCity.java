package phillippascual.weathercheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeCity extends AppCompatActivity {

    Button backButton;
    EditText enterCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city);

        backButton = findViewById(R.id.backButton);
        enterCity = findViewById(R.id.enterCity);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity = enterCity.getText().toString();
                Intent newIntent = new Intent(ChangeCity.this, MainActivity.class);
                newIntent.putExtra("City", newCity);

                startActivity(newIntent);

                return false;
            }
        });
    }
}
