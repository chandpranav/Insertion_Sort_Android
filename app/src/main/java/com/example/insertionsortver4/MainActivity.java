package com.example.insertionsortver4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText numberInput;
    private Button addButton;
    private Button sortButton;
    private Button clearButton;
    private ListView listView;
    private TextView errorTextView;
    private ArrayAdapter<Integer> gridAdapter;
    private ArrayAdapter<String> listAdapter;
    private List<Integer> numbers = new ArrayList<>();
    private List<List<Integer>> sortingSteps = new ArrayList<>();
    private int currentIteration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberInput = findViewById(R.id.numberInput);
        addButton = findViewById(R.id.addButton);
        sortButton = findViewById(R.id.sortButton);
        clearButton = findViewById(R.id.clearButton);
        Button closeButton = findViewById(R.id.closeButton);
        listView = findViewById(R.id.listView);
        errorTextView = findViewById(R.id.errorTextView);

        gridAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numbers);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(listAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumbers();
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performInsertionSort();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void addNumbers() {
        String input = numberInput.getText().toString();
        if (!input.isEmpty()) {
            if (input.matches("^[0-9,\\s]*$")) {
            String[] numberStrings = input.split("[,\\s]+");
            for (String numStr : numberStrings) {
                try {
                    int number = Integer.parseInt(numStr.trim());
                    numbers.add(number);
                } catch (NumberFormatException e) {}
            }
            numberInput.getText().clear();

            sortingSteps.clear();
            sortingSteps.add(new ArrayList<>(numbers));

            updateResultText();
            errorTextView.setText("");
            } else {
                errorTextView.setText("Only digits please.");
            }
        }
    }

    private void performInsertionSort() {
        sortingSteps.clear();
        currentIteration = 0;
        insertionSortStep();
    }

    private void insertionSortStep() {
        if (currentIteration < numbers.size()) {
            int key = numbers.get(currentIteration);
            int j = currentIteration - 1;
            while (j >= 0 && numbers.get(j) > key) {
                numbers.set(j + 1, numbers.get(j));
                j--;
            }
            numbers.set(j + 1, key);
            currentIteration++;

            sortingSteps.add(new ArrayList<>(numbers));
            updateResultText();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertionSortStep();
                }
            }, 1000);
        }
    }

    private void updateResultText() {
        listAdapter.clear();
        for (List<Integer> step : sortingSteps) {
            listAdapter.add(step.toString());
        }
    }

    private void clearText() {
        numberInput.getText().clear();
        numbers.clear();
        listAdapter.clear();
        sortingSteps.clear();
        errorTextView.setText("");
    }
}
