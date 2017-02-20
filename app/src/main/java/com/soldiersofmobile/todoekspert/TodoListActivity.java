package com.soldiersofmobile.todoekspert;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoListActivity extends AppCompatActivity {

    public static final String TODO_EXTRA = "todo";
    public static final int REQUEST_CODE_ADD = 123;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.todos_list_view)
    ListView todosListView;
    @BindView(R.id.content_todo_list)
    RelativeLayout contentTodoList;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private LoginManager loginManager;
    private TodoApi todoApi;
    private TodosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getApplication();
        loginManager = application.getLoginManager();
        todoApi = application.getTodoApi();

        if (loginManager.hasToLogin()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAdd(null);
            }
        });

        adapter = new TodosAdapter();
        todosListView.setAdapter(adapter);

    }

    @OnItemClick(R.id.todos_list_view)
    public void itemClick(int position) {

        goToAdd(adapter.getItem(position));
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                goToAdd(null);

                break;
            case R.id.action_refresh:

                Call<TodosResponse> call = todoApi.getTodos(loginManager.getToken());

                call.enqueue(new Callback<TodosResponse>() {
                    @Override
                    public void onResponse(Call<TodosResponse> call, Response<TodosResponse> response) {
                        if (response.isSuccessful()) {
                            TodosResponse todosResponse = response.body();
                            adapter.clear();
                            adapter.addAll(todosResponse.results);
                            for (Todo result : todosResponse.results) {
                                Log.d("TAG", result.toString());
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<TodosResponse> call, Throwable t) {

                    }
                });

                break;
            case R.id.action_logout:
                showLogoutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToAdd(Todo item) {
        Intent intent = new Intent(this, AddTodoActivity.class);

        if (item != null) {
            intent.putExtra(TODO_EXTRA, item);
        }

        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD:
                if (resultCode == RESULT_OK) {
                    Todo todo = (Todo) data.getParcelableExtra(TODO_EXTRA);
                    Toast.makeText(this, todo.toString(), Toast.LENGTH_SHORT).show();

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Canecelled", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginManager.logout();
                goToLogin();
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }
}
