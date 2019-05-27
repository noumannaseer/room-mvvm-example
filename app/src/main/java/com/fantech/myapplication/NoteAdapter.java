package com.fantech.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fantech.myapplication.db.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

//*************************************************
public class NoteAdapter
        extends ListAdapter<Note, NoteAdapter.NoteHolder>
//*************************************************
{

    //*************************************************
    public NoteAdapter()
    //*************************************************
    {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>()
    {
        //*************************************************
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem)
        //*************************************************
        {
            return oldItem.getId() == newItem.getId();
        }

        //*************************************************
        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem)
        //*************************************************
        {
            return oldItem.getTitle().equals(newItem.getTitle())&&
                   oldItem.getDescription().equals(newItem.getDescription()) &&
                   oldItem.getPriority()==newItem.getPriority();
        }
    };

    //*************************************************
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*************************************************
    {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }


    //*************************************************
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position)
    //*************************************************
    {
        Note currentNote = getItem(position);
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewTitle.setText(currentNote.getTitle());
    }


    //*************************************************
    public Note getNoteAt(int position)
    //*************************************************
    {
        return getItem(position);
    }

    //*************************************************
    class NoteHolder
            extends RecyclerView.ViewHolder
            //*************************************************
    {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        //*************************************************
        public NoteHolder(View itemView)
        //*************************************************
        {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION)
                {
                    onItemClickListener.onItemClicked(getItem(position));
                }

            });
        }
    }


    //*************************************************
    public interface OnItemClickListener
            //*************************************************
    {
        void onItemClicked(Note note);
    }


    private OnItemClickListener onItemClickListener;

    //*************************************************
    public void setOnItemClickListener(OnItemClickListener listener)
    //*************************************************
    {
        this.onItemClickListener = listener;
    }

}
