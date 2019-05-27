package com.fantech.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fantech.myapplication.db.Note;
import com.fantech.myapplication.vm.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//********************************
public class MainActivity
        extends AppCompatActivity
        implements NoteAdapter.OnItemClickListener
//********************************
{
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int UPDATE_NOTE_REQUEST = ADD_NOTE_REQUEST + 1;
    private NoteViewModel noteViewModel;


    //********************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    //********************************
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    //********************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    //********************************
    {
        switch (item.getItemId())
        {
        case R.id.menu_delete_all_notes:
            noteViewModel.deleteAll();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT)
                 .show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    //********************************
    @Override
    protected void onCreate(Bundle savedInstanceState)
    //********************************
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        NoteAdapter adapter = new NoteAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        noteViewModel = ViewModelProviders.of(this)
                                          .get(NoteViewModel.class);
        noteViewModel.getAllNotes()
                     .observe(this, notes -> adapter.submitList(notes));
        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
                    {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
                    {
                        noteViewModel.deleteNote(
                                adapter.getNoteAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT)
                             .show();
                    }
                }).attachToRecyclerView(recyclerView);

    }


    //********************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    //********************************
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            noteViewModel.insertNote(note);
            Toast.makeText(this, "note saved", Toast.LENGTH_SHORT)
                 .show();
        }
        else if (requestCode == UPDATE_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1)
            {
                Toast.makeText(this, "Note can't be updated!", Toast.LENGTH_SHORT)
                     .show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.updateNote(note);
            Toast.makeText(this, "note updated", Toast.LENGTH_SHORT)
                 .show();
        }
        else
        {
            Toast.makeText(this, "note not saved", Toast.LENGTH_SHORT)
                 .show();
        }
    }

    //********************************
    @Override
    public void onItemClicked(Note note)
    //********************************
    {
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
        startActivityForResult(intent, UPDATE_NOTE_REQUEST);
    }
}
