package de.davidkupper.CubeTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// TODO fix bug: currentAttempts does not save correct time
// TODO implement delete button
// TODO optimize 4x4 scramble (less w moves)
// TODO save times permanent
// TODO make show times display
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
    private TextView bestText;
    private TextView meanText;
    private TableLayout frontView;
    private TableLayout upView;
    private TableLayout downView;
    private TableLayout leftView;
    private TableLayout rightView;
    private TableLayout backView;

    // attributes
    private long time;
    private long startSystemTime;
    private Timer timer;
    private boolean isRunning;
    private TimerState timerState;
    private TimerState fallbackState;

    public enum TimerState {INIT, STOPPED, WAITING, READY, RUNNING}

    private Cube cube;
    private Cube[] cubes;
    private LinkedList<Attempt> currentAttempts;
    private LinkedList<Attempt>[] attemptsArray;

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
        bestText = findViewById(R.id.bestText);
        meanText = findViewById(R.id.meanText);
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


        // attributes initialisation
        time = 0;
        timer = new Timer();
        setTimerState(TimerState.INIT);

        cubes = new Cube[3];
        for (int i = 0; i < cubes.length; i++) {
            cubes[i] = new Cube(i + 2);
        }
        cube = cubes[1]; // 3x3 cube
        scrambleCube();

        // only temporary: TODO load attempts from file
        attemptsArray = new LinkedList[3];
        for (int i = 0; i < attemptsArray.length; i++)
            attemptsArray[i] = new LinkedList<>();
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
        if (size == 2)
            drawId = R.drawable.rect_rounded_big;
        else if (size == 3)
            drawId = R.drawable.rect_rounded_medium;
        else
            drawId = R.drawable.rect_rounded_small;

        for (int i = 0; i < view.getChildCount(); i++) {
            TableRow row = (TableRow) view.getChildAt(i);
            row.removeAllViews();
        }
        ImageView[][] imageMatrix = new ImageView[size][size];
        for (int i = 0; i < size; i++) {
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
            default:
                throw new IllegalArgumentException("parameter side not valid");
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
                    case INIT:
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
                    case INIT:
                    case STOPPED:
                    case RUNNING:
                        break;
                    case WAITING:
                        setTimerState(fallbackState);
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
        if (this.timerState == state)
            return;

        switch (state) {
            case INIT:
                fallbackState = TimerState.INIT;
                stopTimer();
                rootPane.setBackgroundColor(getResources().getColor(R.color.orange, null));
                visibilityExceptTimer(true);
                buttonsLayer.setVisibility(View.INVISIBLE);
                bestText.setVisibility(View.INVISIBLE);
                meanText.setVisibility(View.INVISIBLE);
                timeText.setText(getResources().getString(R.string.hold_release));
                setVerticalBias(timeText, 0.5f);
                break;
            case STOPPED:
                fallbackState = TimerState.STOPPED;
                stopTimer();
                rootPane.setBackgroundColor(getResources().getColor(R.color.orange, null));
                visibilityExceptTimer(true);
                if (this.timerState == TimerState.RUNNING) {
                    currentAttempts.add(new Attempt(time, scrambleText.getText().toString()));
                    scrambleCube();
                }
                updateTimeText(currentAttempts.getLast());
                Log.d("timertask","currAttempt time: " + currentAttempts.getLast().getTime());
                updateAvgTimesText();
                break;
            case WAITING:
                startTimer(new TimerTask() {
                    @Override
                    public void run() {
                        time = System.currentTimeMillis() - startSystemTime;
                        if (time >= 500) {
                            setTimerState(TimerState.READY);
                            stopTimer();
                        }
                    }
                });
                rootPane.setBackgroundColor(getResources().getColor(R.color.red, null));
                updateTimeText(0);
                visibilityExceptTimer(false);
                setVerticalBias(timeText, 0.45f);
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
                                if(isRunning) {
                                    time = System.currentTimeMillis() - startSystemTime;
                                    updateTimeText(time);
                                    Log.d("timertask", "time: " + time);
                                }
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
        if (timerState != TimerState.STOPPED && timerState != TimerState.INIT)
            throw new IllegalStateException("Cube size can only be set in TimerState.STOPPED");
        cube = cubes[size - 2];
        scrambleCube();
        currentAttempts = attemptsArray[size - 2];
        sizeBtn.setText(size + "x" + size);
        setTimerState(TimerState.INIT);
    }

    private void deleteTime() {

    }

    private void startTimer(TimerTask task) {
        time = 0;
        isRunning = true;
        startSystemTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(task, 0, 1);
    }

    private void stopTimer() {
        isRunning = false;
        timer.cancel();
        timer.purge();
        timer = new Timer();
    }

    private void updateTimeText(long time) {
        timeText.setText(timeToString(time));
        setTimeTextDnf(false);
    }

    private void updateTimeText(Attempt attempt) {
        timeText.setText(timeToString(attempt.getTime()));
        setTimeTextPlus2(attempt.isPlus2());
        setTimeTextDnf(attempt.isDnf());
    }

    private void updateAvgTimesText() {
        String best = getResources().getString(R.string.best);
        String mean3 = getResources().getString(R.string.mean3);
        String avg5 = getResources().getString(R.string.avg5);
        String avg12 = getResources().getString(R.string.avg12);

        int listSize = currentAttempts.size();
        best += timeToString(getBestTime(currentAttempts));
        mean3 += timeToString(getMean(currentAttempts, 3));
        avg5 += timeToString(getAvg(currentAttempts, 5));
        avg12 += timeToString(getAvg(currentAttempts, 12));

        bestText.setText(best + "\n" + avg5);
        meanText.setText(mean3 + "\n" + avg12);
    }

    private long getBestTime(List<Attempt> list) {
        if (list.isEmpty())
            return -1;
        Attempt bestAttempt = list.get(0);
        for (Attempt a : list) {
            if (a.compareTo(bestAttempt) < 0)
                bestAttempt = a;
        }
        return bestAttempt.getTime();
    }

    private long getMean(List<Attempt> list, int countFromLast) {
        list = getSubListOfLastElements(list, countFromLast);
        if(list == null)
            return -1;
        if(list.isEmpty())
            return -1;
        long mean = 0;
        for (Attempt a : list)
            mean += a.getRealTime();
        mean /= list.size();
        return mean;
    }

    private long getAvg(List<Attempt> list, int countFromLast) {
        list = getSubListOfLastElements(list, countFromLast);
        if(list == null)
            return -1;
        if(list.size() < 3)
            return -1;
        Collections.sort(list);
        int removeCount = 1; // normally 5% of the attempts rounded up, 5 -> 1, 12 -> 1
        list = list.subList(0, list.size()-removeCount);
        list = list.subList(removeCount, list.size());
        return getMean(list, list.size());
    }

    private List getSubListOfLastElements(List list, int countFromLast) {
        if(countFromLast < 1)
            throw new IllegalArgumentException("countFromLast must be greater or equal to 1");
        if(list.size() < countFromLast)
            return null;
        return list.subList(list.size() - countFromLast, list.size());
    }

    private void setTimeTextDnf(boolean dnf) {
        if (dnf) {
            timeText.setPaintFlags(timeText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            timeText.setTextColor(getResources().getColor(R.color.red, null));
        }
        else {
            timeText.setPaintFlags(timeText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            timeText.setTextColor(getResources().getColor(R.color.white, null));
        }
    }

    private void setTimeTextPlus2(boolean plus2) {
        String text = timeText.getText().toString();
        String sub = text.substring(text.length() - 3);
        if (plus2) {
            if (!sub.equals(" +2"))
                text += " +2";
        }
        else {
            if (sub.equals(" +2"))
                text = text.substring(0, text.length() - 3);
        }
        timeText.setText(text);
    }

    private void visibilityExceptTimer(boolean visible) {
        int v;
        if (visible)
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
        bestText.setVisibility(v);
        meanText.setVisibility(v);
    }

    private void setVerticalBias(View view, float bias) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.verticalBias = bias;
        timeText.setLayoutParams(layoutParams);
    }

    // button on click methods (so constructor is not packed full)
    private void sizeBtnClicked() {
        int size = cube.getSize();
        if (++size > 4)              // size restriction
            size = 2;
        setCubeSize(size);
    }

    private void deleteBtnClicked() {

    }

    private void dnfBtnClicked() {
        if (timerState != TimerState.STOPPED)
            throw new IllegalStateException("Time can only be set DNF in TimerState.STOPPED");
        currentAttempts.getLast().toggleDnf();
        setTimeTextDnf(currentAttempts.getLast().isDnf());
        updateAvgTimesText();
    }

    private void plus2BtnClicked() {
        if (timerState != TimerState.STOPPED)
            throw new IllegalStateException("Time can only be set +2 in TimerState.STOPPED");
        currentAttempts.getLast().togglePlus2();
        setTimeTextPlus2(currentAttempts.getLast().isPlus2());
        updateAvgTimesText();
    }

    public static String timeToString(long time) {
        if (time == -1)
            return "--:--.---";
        else if (time < 0)
            throw new IllegalArgumentException("time has to be > 0, or -1 for '--:--.---'");
        int minutes = (int) (time / 60000);
        int seconds = (int) ((time / 1000) - (minutes * 60));
        int millis = (int) (time - (minutes * 60000) - (seconds * 1000));
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }
}