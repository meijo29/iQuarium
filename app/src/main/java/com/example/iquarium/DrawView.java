package com.example.iquarium;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DrawView extends SurfaceView {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    List<Point> circlePoints;
    private Context context;

    private static int REFERENCE_POINT_COLOR = Color.YELLOW;
    private static int MEASURE_POINT_COLOR = Color.RED;

    public DrawView(Context context){
        super(context);
        this.context = context;
        paint.setColor(Color.RED);

        paint.setStyle(Paint.Style.FILL);
        circlePoints = new ArrayList<>();
        setWillNotDraw (false);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onDraw(Canvas canvas){
        int size = circlePoints.size();
        for(int i = 0; i < size; i++){

            //Set color based on order. First 2 points are the reference points.
            if(i < 2) paint.setColor(REFERENCE_POINT_COLOR);
            else paint.setColor(MEASURE_POINT_COLOR);

            Point p = circlePoints.get(i);

            canvas.drawCircle(p.x, p.y, 10, paint);
            if(i == 1){
                canvas.drawLine(circlePoints.get(0).x, circlePoints.get(0).y, circlePoints.get(1).x, circlePoints.get(1).y, paint);
            }
            if(i == 3){
                canvas.drawLine(circlePoints.get(2).x, circlePoints.get(2).y, circlePoints.get(3).x, circlePoints.get(3).y, paint);
            }

          //  toasMessage(i + "");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(circlePoints.size() < 4) {
                circlePoints.add(new Point(Math.round(event.getX()), Math.round(event.getY())));
                invalidate();
                if(circlePoints.size() == 4){
                    ((TextView) ((Activity)context).findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setScaleValue));
                }


            }
        }
        return false;
    }

    /**
     * Clears all drawn points and shapes
     */
    public void clearCanvasRed(){
        //circlePoints.clear();
     try {
        if(circlePoints.size() == 4) {
            circlePoints.remove(3);
            circlePoints.remove(2);
        }
        else if(circlePoints.size() == 3){
            circlePoints.remove(2);
        }
     }catch (Exception e){
       //  toasMessage(e.toString());
     }

        invalidate();
    }

    public void clearCanvas(){
        circlePoints.clear();

        invalidate();
    }


    /**
     * Calculates the measurement
     * @param reference The reference size
     * @param inputUnitIndex The input length unit index
     * @param outputUnitIndex The output length unit index
     * @return The value of the measurement, converted to outputUnitIndex
     */
    public double calculate(double reference, int inputUnitIndex, int outputUnitIndex){
        if(circlePoints.size() != 4){
            Toast.makeText(context, getResources().getString(R.string.error_noPoints), Toast.LENGTH_SHORT).show();
            return -1;
        }
        return Ruler.compute(circlePoints, reference, inputUnitIndex, outputUnitIndex);
    }


    private void toasMessage(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_LONG).show();
    }


}
