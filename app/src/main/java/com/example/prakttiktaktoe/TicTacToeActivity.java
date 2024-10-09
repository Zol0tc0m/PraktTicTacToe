package com.example.prakttiktaktoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToeActivity extends AppCompatActivity {

    SharedPreferences stats;
    boolean withBot;
    char[] board;
    char Player;
    TextView tvStatus;
    Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        stats = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        withBot = getIntent().getBooleanExtra("withBot", false);
        board = new char[9];
        Player = 'X';
        tvStatus = findViewById(R.id.tvStatus);
        buttons = new Button[9];

        for (int i = 0; i < 9; i++) {
            String buttonID = "btn_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
            buttons[i].setOnClickListener(new ButtonClickListener(i));
        }

        updateStatus();
    }

    private void updateStatus() {
        tvStatus.setText("Игрок " + Player + " ходит");
    }

    private void makeMove(int position) {
        if (board[position] == 0) {
            board[position] = Player;
            buttons[position].setText(String.valueOf(Player));
            if (checkWin()) {
                updateStats(true);
                finish();
            } else if (isDraw()) {
                updateStats(false);
                finish();
            } else {
                Player = (Player == 'X') ? 'O' : 'X';
                updateStatus();
                if (withBot && Player == 'O') {
                    BotMove();
                }
            }
        }
    }

    private void BotMove() {
        int position = -1;
        do {
            position = (int) (Math.random() * 9);
        } while (board[position] != 0);
        makeMove(position);
    }

    private boolean checkWin() {
        return (board[0] == Player && board[1] == Player && board[2] == Player) ||
                (board[3] == Player && board[4] == Player && board[5] == Player) ||
                (board[6] == Player && board[7] == Player && board[8] == Player) ||
                (board[0] == Player && board[3] == Player && board[6] == Player) ||
                (board[1] == Player && board[4] == Player && board[7] == Player) ||
                (board[2] == Player && board[5] == Player && board[8] == Player) ||
                (board[0] == Player && board[4] == Player && board[8] == Player) ||
                (board[2] == Player && board[4] == Player && board[6] == Player);
    }

    private boolean isDraw() {
        for (char cell : board) {
            if (cell == 0) {
                return false;
            }
        }
        return true;
    }

    private void updateStats(boolean isWin) {
        SharedPreferences.Editor editor = stats.edit();
        int wins = stats.getInt("wins", 0);
        int losses = stats.getInt("losses", 0);
        int draws = stats.getInt("draws", 0);

        if (isWin) {
            if (Player == 'X') {
                editor.putInt("wins", wins + 1);
            } else {
                editor.putInt("losses", losses + 1);
            }
        } else {
            editor.putInt("draws", draws + 1);
        }

        editor.apply();
    }

    private class ButtonClickListener implements View.OnClickListener {
        private int position;

        public ButtonClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            makeMove(position);
        }
    }
}