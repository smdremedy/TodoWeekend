package com.soldiersofmobile.todoekspert;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodosAdapter extends BaseAdapter {

    private ArrayList<Todo> todos = new ArrayList<>();

    public void addAll(List<Todo> todosToAdd) {
        todos.addAll(todosToAdd);
        notifyDataSetChanged();
    }

    public void clear() {
        todos.clear();
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Todo getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.item_todo, parent, false);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Todo todo = getItem(position);
        viewHolder.itemCheckBox.setChecked(todo.isDone());
        viewHolder.itemCheckBox.setText(todo.getContent());
        viewHolder.itemEditButton.setText(todo.getObjectId());


        return view;
    }


    static class ViewHolder {
        @BindView(R.id.item_check_box)
        CheckBox itemCheckBox;
        @BindView(R.id.item_edit_button)
        Button itemEditButton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
