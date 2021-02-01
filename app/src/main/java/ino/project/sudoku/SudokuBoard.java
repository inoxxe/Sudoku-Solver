package ino.project.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int cellsFillColor;
    private final int cellsHighlightColor;
    private final int letterColor;
    private final int letterColorSolve;
    private final int letterColorError;

    private final Paint boardColorPaint = new Paint();
    private final Paint colorCellpaint = new Paint();
    private final Paint cellsFillColorPaint = new Paint();
    private final Paint cellsHighlightColorPaint = new Paint();

    private final Paint letterPaint = new Paint();
    private final Paint letterColorErrorPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();
    private int cellSize;

    private final Solver solver = new Solver();

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard,
                0,0);
        try {
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor,0);
            cellsFillColor = a.getInteger(R.styleable.SudokuBoard_cellsFillColor,0);
            cellsHighlightColor = a.getInteger(R.styleable.SudokuBoard_cellsHighlightColor,0);
            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor,0);
            letterColorSolve = a.getInteger(R.styleable.SudokuBoard_letterColorSolve,0);
            letterColorError = a.getInteger(R.styleable.SudokuBoard_letterColorError,0);
        }finally {
             a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());

        cellSize = dimension / 9;
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(28);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellsFillColorPaint.setStyle(Paint.Style.FILL);
        cellsFillColorPaint.setAntiAlias(true);
        cellsFillColorPaint.setColor(cellsFillColor);

        colorCellpaint.setStyle(Paint.Style.STROKE);
        colorCellpaint.setStrokeWidth(6);
        colorCellpaint.setColor(boardColor);
        colorCellpaint.setAntiAlias(true);

        cellsHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellsHighlightColorPaint.setAntiAlias(true);
        cellsHighlightColorPaint.setColor(cellsHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        letterColorErrorPaint.setStyle(Paint.Style.FILL);
        letterColorErrorPaint.setAntiAlias(true);
        letterColorErrorPaint.setColor(letterColorError);

        fillCell(canvas);
        colorCell(canvas, solver.getSelected_row(),solver.getSelected_column());
        canvas.drawRoundRect(0,0,getWidth(),getHeight(),48,48,boardColorPaint);
        DrawBoard(canvas);
        drawNumbers(canvas);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean isValid;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            solver.setSelected_row((int) Math.ceil(y/cellSize));
            solver.setSelected_column((int) Math.ceil(x/cellSize));
            isValid = true;
        }else{
            isValid = false;
        }

        return isValid;
    }

    private void drawNumbers(Canvas canvas){
        letterPaint.setTextSize(cellSize);

        for(int r=0 ; r<9 ; r++){
            for(int c=0 ; c<9 ; c++){
                if(solver.check(r,c)){
                    if(solver.getBoard()[r][c] !=0){
                        String text = Integer.toString(solver.getBoard()[r][c]);
                        float width, height;

                        letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
                        width = letterPaint.measureText(text);
                        height = letterPaintBounds.height();

                        canvas.drawText(text,(c*cellSize)+ ((cellSize-width)/2),
                                (r*cellSize+cellSize)- ((cellSize-height)/2),letterPaint);

                    }
                }

            }
        }

        letterPaint.setColor(letterColorSolve);

        for (ArrayList<Object> letter : solver.getEmptyBoxIndex()){
            int r = (int) letter.get(0);
            int c = (int) letter.get(1);

            String text = Integer.toString(solver.getBoard()[r][c]);
            float width, height;

            letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
            width = letterPaint.measureText(text);
            height = letterPaintBounds.height();

            canvas.drawText(text,(c*cellSize)+ ((cellSize-width)/2),
                    (r*cellSize+cellSize)- ((cellSize-height)/2),letterPaint);
        }
    }

    public void fillCell(Canvas canvas){
        for(int r=0 ; r<=9 ; r++){
            if(r%2 != 0){
                for(int c=0 ; c<=9 ; c++){
                    if(c%2 == 0){
                        canvas.drawRect((c-1) * cellSize,(r-1)*cellSize,c* cellSize,
                                r*cellSize,cellsFillColorPaint);
                    }else if(c%2 != 0){
                        canvas.drawRect((c-1) * cellSize,(r-1)*cellSize,c* cellSize,
                                r*cellSize,cellsHighlightColorPaint);
                    }
                }
            }else{
                for(int c=0 ; c<=9 ; c++){
                    if(c%2 != 0){
                        canvas.drawRect((c-1) * cellSize,(r-1)*cellSize,c* cellSize,
                                r*cellSize,cellsFillColorPaint);
                    }else if(c%2 == 0){
                        canvas.drawRect((c-1) * cellSize,(r-1)*cellSize,c* cellSize,
                                r*cellSize,cellsHighlightColorPaint);
                    }
                }
            }

        }
    }

    private void colorCell(Canvas canvas, int r , int c){

        if(solver.getSelected_column() != -1 && solver.getSelected_row() != -1){

            canvas.drawRoundRect((c-1) * cellSize,(r-1)*cellSize,c* cellSize,
                    r*cellSize,12,12,colorCellpaint);
        }

        invalidate();
    }


    private void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    private void DrawBoard(Canvas canvas){
        for(int c = 0 ; c < 10 ; c++){
            if(c%3 ==0) {
                drawThinLine();
                canvas.drawLine(cellSize * c, 0, cellSize * c,
                        getWidth(), boardColorPaint);
            }

        }

        for(int r = 0 ; r < 10 ; r++){
            if(r%3 == 0){
                canvas.drawLine(0,cellSize*r, getWidth(),
                        cellSize* r,boardColorPaint);}


        }
    }

    public Solver getSolver(){
        return this.solver;
    }
}
