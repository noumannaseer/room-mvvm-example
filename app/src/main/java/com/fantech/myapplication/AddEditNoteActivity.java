package com.fantech.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.fantech.myapplication.vm.NoteViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

//******************************
public class AddEditNoteActivity
        extends AppCompatActivity
//******************************
{
    public static final String EXTRA_ID = "com.fantech.myapplication.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.fantech.myapplication.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.fantech.myapplication.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.fantech.myapplication.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private NoteViewModel noteViewModel;

    //******************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //******************************
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        noteViewModel = ViewModelProviders.of(this)
                                          .get(NoteViewModel.class);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID))
        {
            setTitle("Edit note");
            int id = intent.getIntExtra(EXTRA_ID, -1);
            if (id == -1)
                return;
            noteViewModel.getNoteById(id)
                         .observe(this, note -> {
                             if (note == null)
                                 return;
                             editTextTitle.setText(note.getTitle());
                             editTextDescription.setText(note.getDescription());
                             numberPickerPriority.setValue(note.getPriority());
                         });

        }
        else
            setTitle("Add note");

    }

    //******************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    //******************************
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    //******************************
    private void saveNote()
    //******************************
    {
        String title = editTextTitle.getText()
                                    .toString();
        String description = editTextDescription.getText()
                                                .toString();
        int priority = numberPickerPriority.getValue();
        if (title.trim()
                 .isEmpty() || description.trim()
                                          .isEmpty())
        {
            Toast.makeText(this, "Empty values", Toast.LENGTH_SHORT)
                 .show();
            return;
        }


        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1)
            data.putExtra(EXTRA_ID, id);
        setResult(RESULT_OK, data);
        finish();
    }

    //******************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    //******************************
    {
        switch (item.getItemId())
        {
        case R.id.menu_save_note:
            saveNote();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }
}
