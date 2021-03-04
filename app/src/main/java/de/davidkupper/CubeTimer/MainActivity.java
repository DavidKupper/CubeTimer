package de.davidkupper.CubeTimer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
// TODO implement button functions
// TODO implement average time displays
// TODO optimize 4x4 scramble (less w moves)
// TODO save times permanent
public class MainActivity extends AppCompatActivity {
    // views
    private View rootPane;
    private TextView timeText;
    private TextView scrambleText;
    private Button sizeBtn;
    private LinearLayout buttonsLayer;
    private Button deleteBtn;
    private Button dnfBtn;
    private Button plus2Btn;
    private TableLayout frontView;
    private TableLayout upView;
    private TableLayout downView;
    private TableLayout leftView;
    private TableLayout rightView;
    private TableLayout backView;

    // attributes
    private long time;
    private String currTime;
    private Timer timer;
    private TimerState timerState;
    public enum TimerState {STOPPED, WAITING, READY, RUNNING}
    private Cube cube;
    private ArrayList<Attempt> currentAttempts;
    private ArrayList<Attempt>[] attemptsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views by id
        rootPane = findViewById(R.id.rootPane);
        timeText = findViewById(R.id.timeText);
        scrambleText = findViewById(R.id.scrambleText);
        sizeBtn = findViewById(R.id.sizeBtn);
        buttonsLayer = findViewById(R.id.buttonsLayer);
        deleteBtn = findViewById(R.id.deleteBtn);
        dnfBtn = findViewById(R.id.dnfBtn);
        plus2Btn = findViewById(R.id.plus2Btn);
        frontView = findViewById(R.id.front);
        upView = findViewById(R.id.up);
        downView = findViewById(R.id.down);
        leftView = findViewById(R.id.left);
        rightView = findViewById(R.id.right);
        backView = findViewById(R.id.back);

        // button on click listeners
        sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeBtnClicked();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBtnClicked();
            }
        });
        dnfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dnfBtnClicked();
            }
        });
        plus2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus2BtnClicked();
            }
        });

        // vanish time control buttons
        buttonsLayer.setVisibility(View.INVISIBLE);


        // attributes initialisation
        time = 0;
        timer = new Timer();
        timerState = TimerState.STOPPED;
        cube = new Cube(3);
        scrambleCube();

        // only temporary: TODO load attempts from file
        attemptsArray = new ArrayList[3];
        for(ArrayList<Attempt> a : attemptsArray)
            a = new ArrayList<>();
        // end temporary
        currentAttempts = attemptsArray[1]; // attempts list of 3x3


    }

    private void scrambleCube() {
        cube.reset();
        String scramble = cube.getRandomScramble();
        cube.scramble(scramble);
        scrambleText.setText(scramble);
        displayCube();
    }

    private void displayCube() {
        setSideView(upView, cube.getSideMatrix(Cube.Side.UP));
        setSideView(downView, cube.getSideMatrix(Cube.Side.DOWN));
        setSideView(leftView, cube.getSideMatrix(Cube.Side.LEFT));
        setSideView(rightView, cube.getSideMatrix(Cube.Side.RIGHT));
        setSideView(frontView, cube.getSideMatrix(Cube.Side.FRONT));
        setSideView(backView, cube.getSideMatrix(Cube.Side.BACK));
    }

    // restriction: only 2x2 3x3 4x4
    private void setSideView(TableLayout view, Cube.Side[][] matrix) {
        int size = matrix.length;
        int drawId = 0;
        if(size == 2)
            drawId = R.drawable.rect_rounded_big;
        else if(size == 3)
            drawId = R.drawable.rect_rounded_medium;
        else
            drawId = R.drawable.rect_rounded_small;

        for(int i = 0; i < view.getChildCount(); i++) {
            TableRow row = (TableRow) view.getChildAt(i);
            row.removeAllViews();
        }
        ImageView[][] imageMatrix = new ImageView[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                imageMatrix[i][j] = new ImageView(this);
                Drawable d = getResources().getDrawable(drawId, null);
                d.setColorFilter(getColorOfSide(matrix[i][j]), PorterDuff.Mode.MULTIPLY);
                imageMatrix[i][j].setBackground(d);
                TableRow row = (TableRow) view.getChildAt(i);
                row.addView(imageMatrix[i][j]);
            }
        }
    }

    private int getColorOfSide(Cube.Side side) {
        switch (side) {
            case UP:
                return getResources().getColor(R.color.defaultUp, null);
            case DOWN:
                return getResources().getColor(R.color.defaultDown, null);
            case LEFT:
                return getResources().getColor(R.color.defaultLeft, null);
            case RIGHT:
                return getResources().getColor(R.color.defaultRight, null);
            case FRONT:
                return getResources().getColor(R.color.defaultFront, null);
            case BACK:
                return getResources().getColor(R.color.defaultBack, null);
            case NONE:
                return getResources().getColor(R.color.black, null);
            default: throw new IllegalArgumentException("parameter side not valid");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (timerState) {
                    case WAITING:
                    case READY:
                        break;
                    case STOPPED:
                        setTimerState(TimerState.WAITING);
                        break;
                    case RUNNING:
                        setTimerState(TimerState.STOPPED);
                        break;
                    default:
                        throw new IllegalStateException("timerState is not valid");
                }

                break;
            case MotionEvent.ACTION_UP:
                switch (timerState) {
                    case STOPPED:
                    case RUNNING:
                        break;
                    case WAITING:
                        setTimerState(TimerState.STOPPED);
                        break;
                    case READY:
                        setTimerState(TimerState.RUNNING);
                        break;
                    default:
                        throw new IllegalStateException("timerState is not valid");
                }
                break;
        }
        return false;
    }

    private void setTimerState(TimerState state) {
        if(this.timerState == state)
            return;

        switch (state) {
            case STOPPED:
                stopTimer();
                rootPane.setBackgroundColor(getResources().getColor(R.color.orange, null));
                visibilityExceptTimer(true);
                if(this.timerState == TimerState.WAITING) {
                    if(currentAttempts.isEmpty())
                        timeText.setText(getResources().getString(R.string.hold_release));
                    else
                        updateTimeText(currentAttempts.get(currentAttempts.size()-1).getTime());
                }
                if(this.timerState == TimerState.RUNNING) {
                    currentAttempts.add(new Attempt(time, scrambleText.getText().toString()));
                    scrambleCube();
                }

                break;
            case WAITING:
                startTimer(new TimerTask() {
                    @Override
                    public void run() {
                        time++;
                        if(time >= 500) {
                            setTimerState(TimerState.READY);
                            stopTimer();
                        }
                    }
                });
                rootPane.setBackgroundColor(getResources().getColor(R.color.red, null));
                updateTimeText(0);
                visibilityExceptTimer(false);
                break;
            case READY:
                rootPane.setBackgroundColor(getResources().getColor(R.color.green, null));
                break;
            case RUNNING:
                startTimer(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                time++;
                                updateTimeText(time);
                            }
                        });
                    }
                });
                rootPane.setBackgroundColor(getResources().getColor(R.color.red, null));
                scrambleCube();
                break;
        }
        this.timerState = state;
    }

    private void setCubeSize(int size) {
        if(timerState != TimerState.STOPPED)
            throw new IllegalStateException("Cube size can only be set in Timer State STOPPED");
        cube = new Cube(size); // size restriction is handled in constructor
        scrambleCube();
        currentAttempts = attemptsArray[size-2];
        sizeBtn.setText(size + "x" + size);

    }

    private void startTimer(TimerTask task) {
        time = 0;
        timer.scheduleAtFixedRate(task, 0,1);
    }

    private void stopTimer() {
        timer.cancel();
        timer = new Timer();
    }

    private void updateTimeText(long time) {
        timeText.setText(timeToString(time));
    }

    private void visibilityExceptTimer(boolean visible) {
        int v;
        if(visible)
            v = View.VISIBLE;
        else
            v = View.INVISIBLE;

        frontView.setVisibility(v);
        upView.setVisibility(v);
        downView.setVisibility(v);
        leftView.setVisibility(v);
        rightView.setVisibility(v);
        backView.setVisibility(v);

        scrambleText.setVisibility(v);
        sizeBtn.setVisibility(v);
        buttonsLayer.setVisibility(v);
    }

    // button on click methods (so constructor is not packed full)
    private void sizeBtnClicked() {
        int size = cube.getSize();
        if(++size > 4)              // size restriction
            size = 2;
        setCubeSize(size);
    }

    private void deleteBtnClicked() {

    }

    private void dnfBtnClicked() {

    }

    private void plus2BtnClicked() {

    }

    public static String timeToString(long time) {
        if(time == -1)
            return "DNF";
        else if(time < 0)
            throw new IllegalArgumentException("time has to be > 0, or -1 for DNF");
        int minutes = (int) (time / 60000);
        int seconds = (int) ((time / 1000) - (minutes * 60));
        int millis = (int) (time - (minutes * 60000) - (seconds * 1000));
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }
}