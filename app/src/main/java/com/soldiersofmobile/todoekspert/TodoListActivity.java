package com.soldiersofmobile.todoekspert;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class TodoListActivity extends AppCompatActivity {

    public static final String TODO_EXTRA = "todo";
    public static final int REQUEST_CODE_ADD = 123;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getApplication();
        loginManager = application.getLoginManager();

        if (loginManager.hasToLogin()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


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
                Intent intent = new Intent(this, AddTodoActivity.class);

                Todo todo = new Todo("kupic cos", true);
                intent.putExtra(TODO_EXTRA, todo);

                startActivityForResult(intent, REQUEST_CODE_ADD);

                break;
            case R.id.action_refresh:
                break;
            case R.id.action_logout:
                showLogoutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
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
