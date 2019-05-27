package com.fantech.myapplication.vm;

import android.app.Application;

import com.fantech.myapplication.db.Note;
import com.fantech.myapplication.db.NoteRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//****************************************
public class NoteViewModel
        extends AndroidViewModel
//****************************************
{

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private MutableLiveData<Note>mSelectedNote;

    //****************************************
    public NoteViewModel(@NonNull Application application)
    //****************************************
    {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public MutableLiveData<Note> getNoteById(int noteId){ return repository.getNoteById(noteId);}
    public void insertNote(Note note)
    {
        repository.insert(note);
    }

    public void updateNote(Note note)
    {
        repository.update(note);
    }

    public void deleteNote(Note note)
    {
        repository.delete(note);
    }

    public void deleteAll()
    {
        repository.deleteAll();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }
}
