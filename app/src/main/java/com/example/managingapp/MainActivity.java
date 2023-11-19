package com.example.managingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView taskListView;
    private Button addButton;
    private EditText taskInput;
    private Switch taskSwitch;
    private static final String PREFS_NAME = "MyTaskPreferences";
    private static final String TASKS_KEY = "tasks";
    private static final String SWITCH_STATUS_KEY = "switchStatus";

    private void saveSwitchStatus(boolean switchStatus) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SWITCH_STATUS_KEY, switchStatus);
        editor.apply();
    }

    ArrayList<Task> tasksList = new ArrayList<>();
    private void saveTasksToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonTasks = gson.toJson(tasksList);

        editor.putString(TASKS_KEY, jsonTasks);
        editor.apply();
    }
    private void loadTasksFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonTasks = sharedPreferences.getString(TASKS_KEY, "");

        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        tasksList = gson.fromJson(jsonTasks, type);

        if (tasksList == null) {
            tasksList = new ArrayList<>();
        }
    }




    // Method to add a task to the tasksList
    public void addTask(Task newTask) {
        tasksList.add(newTask);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) taskListView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        saveTasksToSharedPreferences();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTasksFromSharedPreferences();

        taskListView = findViewById(R.id.taskListView);
         addButton = findViewById(R.id.addButton);
        taskInput = findViewById(R.id.myEditText);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = taskInput.getText().toString().trim();
                String due = "due";// Trim removes leading/trailing spaces

                if (!newTask.isEmpty()) {
                    addTask(new Task(newTask,due));
                } else {
                    // Alert the user that the task input is empty or handle it as needed
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ArrayAdapter<Task> adapter = new ArrayAdapter<>(this, R.layout.task_list_item, R.id.taskTextView, tasksList);

        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task clickedTask = tasksList.get(position);


                clickedTask.setDone(!clickedTask.isDone());
                Switch taskSwitch = view.findViewById(R.id.taskSwitch);
                if (taskSwitch != null) {
                    taskSwitch.setChecked(clickedTask.isDone());
                }

                adapter.notifyDataSetChanged();
                saveTasksToSharedPreferences();

            }
        });








    }



    public void removeTask(View view) {

        int position = taskListView.getPositionForView(view);

        // Remove the task from the list
        tasksList.remove(position);


        ArrayAdapter<String> adapter = (ArrayAdapter<String>) taskListView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        saveTasksToSharedPreferences();
    }



}