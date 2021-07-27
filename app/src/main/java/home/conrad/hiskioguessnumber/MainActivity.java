package home.conrad.hiskioguessnumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private EditText input;
    private TextView log;
    private String answer;
    private int counter;
    private int code = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.inputEditText);
        log = findViewById(R.id.logTextView);

        init();
    }

    private void init() {
        counter = 0;
        input.setText("");
        log.setText("");
        answer = generateAnswer(code);
        Log.v("myLog", "answer : " + answer);
    }

    private String generateAnswer(int howManyCode) {
        HashSet<Integer> nums = new HashSet<>();
        while (nums.size() < howManyCode) {
            nums.add((int)(Math.random()*10));
        }

        LinkedList<Integer> list = new LinkedList<>();
        for (int i : nums) {
            list.add(i);
        }

        Collections.shuffle(list);

        StringBuffer sb = new StringBuffer();
        for (int i : list) {
            sb.append(i);
        }

        return sb.toString();
    }

    public void guess(View view) {
        counter++;
        String userInputText = input.getText().toString();
        String result = checkAB(userInputText);

        if (result.equals(code + "A0B")) {
            showDialogResult(true);
            return;
        }

        if (counter == 3) {
            showDialogResult(false);
            return;
        }

        String message = "第 " + counter + " 次 : 您輸入的是 " + userInputText + " 結果 : " + result;
        showDialogMessage(message);
        log.append(counter + ".\t" + userInputText + " : " + result + "\n");
        input.setText("");
    }

    private void showDialogResult(boolean isWin) {
        new AlertDialog.Builder(this)
                .setTitle(isWin ? "Win" : "Lose")
                .setMessage(isWin ? "Congratulation" : "Answer : " + answer)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        init();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    private String checkAB(String userGuess) {
        int a = 0;
        int b = 0;

        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == userGuess.charAt(i)) { a++; }
            if(answer.charAt(i) != userGuess.charAt(i) && answer.contains("" + userGuess.charAt(i))) { b++; }
        }

        return String.format("%dA%dB", a, b);
    }

    private void showDialogMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(message)
                .create()
                .show();
    }

    public void exit(View view) {
        new AlertDialog.Builder(this)
                .setTitle("EXIT")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

    private long clickLastTime = 0;
    @Override
    public void onBackPressed() {
        // 點擊第一次跳出 toast 提醒三秒內在點擊一次才會離開程式
        if ( System.currentTimeMillis() - clickLastTime < 3000 ) {
            super.onBackPressed();
        } else {
            clickLastTime = System.currentTimeMillis();
            Toast.makeText(this, "Press back again and then exit", Toast.LENGTH_SHORT).show();
        }
    }

    public void restart(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Restart Game ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        init();
                    }
                })
                .setNegativeButton("Cancel", null)
//                .setNeutralButton("Hello World", null)
                .setCancelable(false)
                .create()
                .show();
    }

    String[] items = {"2", "3", "4", "5"};
    int tempCode;
    public void setting(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Setting")
                .setSingleChoiceItems(items, code - 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("myLog", "setting choice : " + which);
                        tempCode = which + 2;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        code = tempCode;
                        Log.v("myLog", "setting code : " + code);
                        init();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }
}