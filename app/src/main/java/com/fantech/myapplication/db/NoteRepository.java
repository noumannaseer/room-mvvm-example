package com.fantech.myapplication.db;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//********************************
public class NoteRepository
//********************************
{
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private MutableLiveData<Note> mSelectedNote = new MutableLiveData<>();

    //********************************
    public NoteRepository(Application application)
    //********************************
    {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();

    }

    //********************************
    private void setNote(Note note)
    //********************************
    {
        mSelectedNote.setValue(note);
    }

    //********************************
    public void insert(Note note)
    //********************************
    {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note)
    {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note)
    {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAll()
    {
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    //***************************************
    public MutableLiveData<Note> getNoteById(int id)
    //***************************************
    {
        GetNoteAsyncTask task = new GetNoteAsyncTask(this, noteDao);
        task.execute(id);
        return mSelectedNote;
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }

    //***************************************
    private static class GetNoteAsyncTask
            extends AsyncTask<Integer, Void, Note>
            //***************************************
    {

        public GetNoteAsyncTask(NoteRepository repository, NoteDao dao)
        {
            this.repository = repository;
            this.noteDao = dao;
        }

        private NoteRepository repository;
        private NoteDao noteDao;

        //***************************************
        @Override
        protected Note doInBackground(Integer... integers)
        //***************************************
        {
            int id = integers[0];
            return noteDao.getNoteById(id);
        }

        //***************************************
        @Override
        protected void onPostExecute(Note note)
        //***************************************
        {
            repository.setNote(note);
        }
    }


    //***************************************
    private static class InsertNoteAsyncTask
            extends AsyncTask<Note, Void, Void>
            //***************************************
    {

        private NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao dao)
        {
            this.noteDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes)
        {
            noteDao.insert(notes[0]);
            return null;
        }
    }


    //***************************************
    private static class UpdateNoteAsyncTask
            extends AsyncTask<Note, Void, Void>
            //***************************************
    {

        private NoteDao noteDao;

        public UpdateNoteAsyncTask(NoteDao dao)
        {
            this.noteDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes)
        {
            noteDao.update(notes[0]);
            return null;
        }
    }


    //***************************************
    private static class DeleteNoteAsyncTask
            extends AsyncTask<Note, Void, Void>
            //***************************************
    {

        private NoteDao noteDao;

        public DeleteNoteAsyncTask(NoteDao dao)
        {
            this.noteDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes)
        {
            noteDao.delete(notes[0]);
            return null;
        }
    }


    //***************************************
    private static class DeleteAllNoteAsyncTask
            extends AsyncTask<Void, Void, Void>
            //***************************************
    {

        private NoteDao noteDao;

        @Override
        protected Void doInBackground(Void... voids)
        {
            noteDao.deleteAllNotes();
            return null;
        }

        public DeleteAllNoteAsyncTask(NoteDao dao)
        {
            this.noteDao = dao;
        }

    }
}
