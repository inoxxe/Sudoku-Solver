package ino.project.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SudokuBoard gameBoard;
    private Solver gameBoardSolver;
    TextView num1,num2,num3,num4,num5,num6,num7,num8,num9;
    Button solveBtn,newGame;
    Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        gameBoard = findViewById(R.id.SudokuBoard);
        gameBoardSolver = gameBoard.getSolver();

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);
        solveBtn = findViewById(R.id.solveBtn);
        newGame = findViewById(R.id.btn_newgame);
        chronometer = findViewById(R.id.time_counter);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();




        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(1);
                gameBoard.invalidate();
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(2);
                gameBoard.invalidate();
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(3);
                gameBoard.invalidate();
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(4);
                gameBoard.invalidate();
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(5);
                gameBoard.invalidate();
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(6);
                gameBoard.invalidate();
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(7);
                gameBoard.invalidate();
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(8);
                gameBoard.invalidate();
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoardSolver.setNumberPos(9);
                gameBoard.invalidate();
            }
        });

    }

    public void Solve(View view) {
        gameBoardSolver.getEmptyBoxIndexs();
        SolveBoardThread solveBoardThread = new SolveBoardThread();
        new Thread(solveBoardThread).start();
        gameBoard.invalidate();
        chronometer.stop();
    }

    public void Reset(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        gameBoardSolver.resetBoard();
        gameBoard.invalidate();
    }

    class SolveBoardThread implements Runnable{
        @Override
        public void run(){
            gameBoardSolver.solve(gameBoard);
        }
    }
}