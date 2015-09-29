package rinat.noteswithdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);


       final Intent intent = getIntent();

        final TextView setTopic= (TextView)findViewById(R.id.viewTopicForNote);
        final TextView setNote= (TextView)findViewById(R.id.viewTextForNote);

        setTopic.setText(intent.getStringExtra("Topic"));
        setNote.setText(intent.getStringExtra("Note"));
        final long id =intent.getLongExtra("Id",0);

        LinearLayout viewNoteLayer=(LinearLayout )findViewById(R.id.viewNoteLayer);
        viewNoteLayer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intentChange = new Intent(ViewNote.this, AddNote.class);
                intentChange.putExtra("Topic", setTopic.getText());
                intentChange.putExtra("Note", setNote.getText());
                Log.d("myLog", "id=" + id);
                intentChange.putExtra("Id",id);
                intentChange.setAction("View");
                startActivity(intentChange);
                finish();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
