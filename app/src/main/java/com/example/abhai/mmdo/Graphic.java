package com.example.abhai.mmdo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Graphic extends AppCompatActivity {
    static ArrayList<double[]> points = new ArrayList<>();
    private ArrayList<LineGraphSeries<DataPoint>> lineGraphSeries = new ArrayList<>();

    private GraphView graphView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

        setGraphView();
        drawGraphic();
    }


    private void setGraphView() {
        graphView = (GraphView) findViewById(R.id.graphView);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(-20);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
    }


    private void drawGraphic() {
        double x = 0;
        for (int i = 0; i < points.size(); i++) {
            lineGraphSeries.add(new LineGraphSeries<DataPoint>());
            for (int j = 0; j < points.get(i).length; j++) {
                if (j % 2 == 0)
                    x = points.get(i)[j];
                else
                    lineGraphSeries.get(i).appendData(new DataPoint(x, points.get(i)[j]), true, points.get(i).length);
            }
            graphView.addSeries(lineGraphSeries.get(i));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onStop();
    }
}
