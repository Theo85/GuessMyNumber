package com.gmail.krawiec.teodor.guessmynumber;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int myNumber, userNumber, guessCount = 0, numberRange = 100;
    String listOfGuesses = "";
    boolean gameWon = false;
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        sharedPreferences = getSharedPreferences("com.gmail.krawiec.teodor.guessmynumber", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        textView2.setText("Best score: " + sharedPreferences.getInt("BestScore", 100));
        textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
        numberRange = sharedPreferences.getInt("NumberRange", 100);
        myNumber = rand.nextInt(numberRange) + 1;
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        editText2.setText(numberRange + "");
    }

    public void takeTheGuess(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        if (gameWon){
            displayMassage("It is time to start new game.");
            editText.setText(null);
        } else {
            if (editText.getText().toString().equals("")) {
                displayMassage("No number provided.");
            } else if (Integer.parseInt(editText.getText().toString()) == 0 || Integer.parseInt(editText.getText().toString()) > sharedPreferences.getInt("NumberRange",100)){
                displayMassage("Number out of range.");
                editText.setText(null);
            } else {
                guessCount++;
                userNumber = Integer.parseInt(editText.getText().toString());
                if (userNumber > myNumber) {
                    displayMassage("Your number is too big.");
                    textView5.setText("Last guess was: too big.");
                } else if (userNumber < myNumber) {
                    displayMassage("Your number is too small.");
                    textView5.setText("Last guess was: too small.");
                } else {
                    displayMassage("Congrats! You guessed my number!");
                    gameWon = true;
                    textView5.setText("You nailed it! My number is " + myNumber);
                    if (guessCount < sharedPreferences.getInt("BestScore", 100)) {
                        editor.putInt("SecondBestScore", sharedPreferences.getInt("BestScore", 100));
                        editor.putInt("BestScore", guessCount);
                        editor.commit();
                        textView2.setText("Best score: " + sharedPreferences.getInt("BestScore", 100));
                        textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
                    } else if (guessCount < sharedPreferences.getInt("SecondBestScore", 100)) {
                        editor.putInt("SecondBestScore", guessCount);
                        editor.commit();
                        textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
                    }
                }
                textView.setText("Times guessed: " + guessCount);
                listOfGuesses += userNumber + ", ";
                editor.putString("GuessList", "Your guesses: " + listOfGuesses);
                editor.commit();
                textView4.setText(sharedPreferences.getString("GuessList", "error"));
                editText.setText(null);
            }
        }
    }

    public void resetBestScore(View view) {
        resetBestScoreCode();
    }

    public void resetBestScoreCode() {
        editor.putInt("BestScore", 100);
        editor.putInt("SecondBestScore", 100);
        editor.commit();
        textView2.setText("Best score: " + sharedPreferences.getInt("BestScore", 100));
        textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
    }

    public void setCustomRange(View view) {
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        if (Integer.parseInt(editText2.getText().toString()) == sharedPreferences.getInt("NumberRange", 100)){
            displayMassage("The range remains not changed.");
        } else {
            if (editText2.getText().toString().equals("0")) {
                editText2.setText(sharedPreferences.getInt("NumberRange", 100) + "");
                displayMassage("Enter number higher than 0.");
            } else if (TextUtils.isEmpty(editText2.getText())){ //do rozwiązania
                editText2.setText(sharedPreferences.getInt("NumberRange", 100) + "");
                displayMassage("No number provided.");
            } else {
                numberRange = Integer.parseInt(editText2.getText().toString());
                editor.putInt("NumberRange", numberRange);
                displayMassage("Number range set to: " + numberRange);
                newGameCode();
                resetBestScoreCode();
            }
        }
    }

    public void newGame(View view) {
        newGameCode();
    }

    public void newGameCode() {
        myNumber = rand.nextInt(numberRange) + 1;
        guessCount = 0;
        gameWon = false;
        listOfGuesses = "";
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(null);
        textView.setText("Times guessed: " + guessCount);
        editor.putString("GuessList", "Your guesses: ");
        editor.commit();
        textView4.setText(sharedPreferences.getString("GuessList", "error"));
        textView5.setText("Last guess was: ");
    }
    public void displayMassage(String massage){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, massage, duration);
        toast.show();
    }
}
