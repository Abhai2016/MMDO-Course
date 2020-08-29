package com.example.abhai.mmdo;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int COUNT_POINTS_FOR_OUTPUT = 6;
    private static final int COUNTS_POINTS = 40;

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;
    private View.OnClickListener onClickListener;
    private LinearLayout newLinearLayout;

    private EditText editTextX;
    private EditText editTextY;

    private CheckBox checkBoxMin;
    private CheckBox checkBoxMax;

    private int limitationsCount = 0;
    private double[] doublePoints;
    private double max = -1000;
    private double min = 1000;

    private ArrayList<LinearLayout> linearLayouts = new ArrayList<>();

    private ArrayList<EditText> editX = new ArrayList<>();
    private ArrayList<EditText> editY = new ArrayList<>();

    private ArrayList<TextView> textX = new ArrayList<>();
    private ArrayList<TextView> textY = new ArrayList<>();

    private ArrayList<EditText> condition = new ArrayList<>();
    private ArrayList<EditText> result = new ArrayList<>();

    private ArrayList<TextView> points = new ArrayList<>();
    private ArrayList<TextView> finalResult = new ArrayList<>();
    private ArrayList<Double> intersectionPointsX = new ArrayList<>();
    private ArrayList<Double> intersectionPointsY = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createListenerOnRadioGroup();
        createListenerForButton();

        Button buttonInequality = (Button) findViewById(R.id.buttonInequality);
        buttonInequality.setOnClickListener(onClickListener);

        Button buttonGraphic = (Button) findViewById(R.id.buttonGraphic);
        buttonGraphic.setOnClickListener(onClickListener);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        newLinearLayout = (LinearLayout) findViewById(R.id.newLinearLayout);

        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);

        checkBoxMin = (CheckBox) findViewById(R.id.checkBoxMin);
        checkBoxMax = (CheckBox) findViewById(R.id.checkBoxMax);
    }


    private void createListenerOnRadioGroup() {
        onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        limitationsCount = 3;
                        break;
                    case R.id.radioButton2:
                        limitationsCount = 4;
                        break;
                    case R.id.radioButton3:
                        limitationsCount = 5;
                        break;
                    case R.id.radioButton4:
                        limitationsCount = 6;
                        break;
                    case R.id.radioButton5:
                        limitationsCount = 7;
                        break;
                    case R.id.radioButton6:
                        limitationsCount = 8;
                        break;
                    case R.id.radioButton7:
                        limitationsCount = 9;
                        break;
                    case R.id.radioButton8:
                        limitationsCount = 10;
                        break;
                }
                if (!linearLayouts.isEmpty())
                    clearData();
                else
                    createViews(limitationsCount);
            }
        };
    }


    private void createListenerForButton() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!points.isEmpty())
                    updateData();
                switch (v.getId()) {
                    case R.id.buttonInequality:
                        try {
                            searchIntersectionPoints();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.buttonGraphic:
                        try {
                            searchPointsInequalitiesForGraphic();
                            searchPointFunctionForGraphic();
                            Intent intent = new Intent(MainActivity.this, Graphic.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
    }


    private void clearData() {
        if (linearLayouts.size() < limitationsCount)
            createViews(limitationsCount);
        else if (limitationsCount < linearLayouts.size())
            for (int i = linearLayouts.size() - 1; i >= limitationsCount; i--) {
                newLinearLayout.removeView( linearLayouts.get(i) );

                editX.remove(i);
                editY.remove(i);

                textX.remove(i);
                textY.remove(i);

                condition.remove(i);
                result.remove(i);
                linearLayouts.remove(i);
            }
    }


    private void createViews(int limit) {
        for (int i = linearLayouts.size(); i < limit; i++) {
            linearLayouts.add( new LinearLayout(MainActivity.this) );
            linearLayouts.get(i).setOrientation(LinearLayout.HORIZONTAL);

            editX.add( new EditText(MainActivity.this) );
            editX.get(i).setInputType(InputType.TYPE_CLASS_PHONE);
            editX.get(i).setTextSize(20);
            linearLayouts.get(i).addView( editX.get(i) );

            textX.add( new TextView(MainActivity.this) );
            textX.get(i).setText("x");
            textX.get(i).setTextSize(20);
            linearLayouts.get(i).addView( textX.get(i) );

            editY.add( new EditText(MainActivity.this) );
            editY.get(i).setInputType(InputType.TYPE_CLASS_PHONE);
            editY.get(i).setTextSize(20);
            linearLayouts.get(i).addView( editY.get(i) );

            textY.add( new TextView(MainActivity.this) );
            textY.get(i).setText("y");
            textY.get(i).setTextSize(20);
            linearLayouts.get(i).addView( textY.get(i) );

            condition.add( new EditText(MainActivity.this) );
            condition.get(i).setTextSize(20);
            linearLayouts.get(i).addView( condition.get(i) );

            result.add( new EditText(MainActivity.this) );
            result.get(i).setInputType(InputType.TYPE_CLASS_PHONE);
            result.get(i).setTextSize(20);
            linearLayouts.get(i).addView( result.get(i) );

            newLinearLayout.addView( linearLayouts.get(i) );
        }
    }


    private void updateData() {
        Graphic.points.clear();

        for (int i = 0; i < points.size(); i++)
            linearLayouts.get(i).removeView(points.get(i));
        points.clear();
    }


    private void searchPointsInequalitiesForGraphic() {
        for (int i = 0; i < limitationsCount; i++) {
            double doubleX = Double.valueOf(editX.get(i).getText().toString());
            double doubleY = Double.valueOf(editY.get(i).getText().toString());
            double doubleResult = Double.valueOf(result.get(i).getText().toString());

            StringBuilder stringBuilder = new StringBuilder(" точки ");
            if (doubleX == 0 || doubleY == 0)
                doublePoints = new double[4];
            else
                doublePoints = new double[COUNTS_POINTS];

            for (int j = 0; j < doublePoints.length; j++) {
                if (doubleX != 0 && doubleY != 0) {
                    if (j % 2 == 0) {
                        doublePoints[j] = j - 20;
                        if (j < COUNT_POINTS_FOR_OUTPUT)
                            stringBuilder.append("(" + floorNumber(doublePoints[j]) + ",");
                    } else {
                        doublePoints[j] = (doubleResult - (doubleX * doublePoints[j - 1])) / doubleY;
                        if (j < COUNT_POINTS_FOR_OUTPUT)
                            stringBuilder.append(floorNumber(doublePoints[j]) + ");");
                    }
                } else if (doubleX == 0) {
                    pointsForStraightLine(j, doubleResult, stringBuilder, "doubleX");
                    break;
                } else {
                    pointsForStraightLine(j, doubleResult, stringBuilder, "doubleY");
                    break;
                }
            }
            Graphic.points.add(doublePoints);
            points.add(new TextView(MainActivity.this));
            points.get(i).setTextSize(20);
            points.get(i).setText(stringBuilder);

            linearLayouts.get(i).addView(points.get(i));
        }
    }


    private void searchPointFunctionForGraphic() {
        double[] points = new double[4];
        double x = Double.valueOf(editTextX.getText().toString());
        double y = Double.valueOf(editTextY.getText().toString());
        if (x < 0) {
            points[0] = x;
            points[1] = y;
            points[2] = 0;
            points[3] = 0;
        } else {
            points[0] = 0;
            points[1] = 0;
            points[2] = x;
            points[3] = y;
        }
        Graphic.points.add(points);
    }


    private void pointsForStraightLine(int j, double doubleResult, StringBuilder stringBuilder, String string) {
        if (string.equals("doubleX")) {
            doublePoints[j] = -20;
            doublePoints[j + 1] = doubleResult;
            doublePoints[j + 2] = 20;
            doublePoints[j + 3] = doubleResult;
        } else if (string.equals("doubleY")){
            doublePoints[j] = doubleResult;
            doublePoints[j + 1] = -20;
            doublePoints[j + 2] = doubleResult;
            doublePoints[j + 3] = 20;
        }
        stringBuilder.append("(" + doublePoints[j] + "," + doublePoints[j + 1] + ");" +
                "(" + doublePoints[j + 2] + "," + doublePoints[j + 3] + ");");
    }


    private BigDecimal floorNumber(double number) {
        return BigDecimal.valueOf(number).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }


    private void searchIntersectionPoints() {
        if (!finalResult.isEmpty()) {
            for (int i = 0; i < finalResult.size(); i++)
                newLinearLayout.removeView(finalResult.get(i));
            finalResult.clear();
        }
        double[][] a = new double[2][2];
        double[] x = new double[2];
        double[] y = new double[2];
        intersectionPointsX.clear();
        intersectionPointsY.clear();
        max = -1000;
        min = 1000;

        for (int i = 0; i < limitationsCount - 1; i++)
            for (int j = i + 1; j < limitationsCount; j++) {
                double doubleX = Double.valueOf(editX.get(i).getText().toString());
                double doubleY = Double.valueOf(editY.get(i).getText().toString());
                double doubleResult = Double.valueOf(result.get(i).getText().toString());

                double doubleX2 = Double.valueOf(editX.get(j).getText().toString());
                double doubleY2 = Double.valueOf(editY.get(j).getText().toString());
                double doubleResult2 = Double.valueOf(result.get(j).getText().toString());

                if (doubleX != 0 && doubleY != 0 && doubleX2 != 0 && doubleY2 != 0) {
                    a[0][0] = doubleX;
                    a[0][1] = doubleY;
                    a[1][0] = doubleX2;
                    a[1][1] = doubleY2;

                    y[0] = doubleResult;
                    y[1] = doubleResult2;
                    x = gauss(a, y, 2);
                } else if (doubleX == 0 && doubleX2 == 0)
                    continue;
                else if (doubleY == 0 && doubleY2 == 0)
                    continue;
                else if (doubleX == 0) {
                    x[0] = (doubleResult2 - doubleY2 * doubleResult) / doubleX2;
                    x[1] = doubleResult;
                } else if (doubleY == 0) {
                    x[0] = doubleResult;
                    x[1] = (doubleResult2 - doubleX2 * doubleResult) / doubleY2;
                } else if (doubleX2 == 0) {
                    x[0] = (doubleResult - doubleY2 * doubleResult) / doubleX;
                    x[1] = doubleResult;
                } else {
                    x[0] = doubleResult;
                    x[1] = (doubleResult - doubleX2 * doubleResult) / doubleY;
                }
                intersectionPointsX.add(x[0]);
                intersectionPointsY.add(x[1]);
            }
        if (checkBoxMax.isChecked())
            searchMaxOrMin("max");
        if (checkBoxMin.isChecked())
            searchMaxOrMin("min");
        for (int i = 0; i < finalResult.size(); i++)
            newLinearLayout.addView(finalResult.get(i));
    }


    private void searchMaxOrMin(String string) {
        TextView textView = new TextView(MainActivity.this);
        textView.setTextSize(20);
        finalResult.add(textView);

        double result;
        int minPointNumber = 0;
        int maxPointNumber = 0;

        for (int i = 0; i < intersectionPointsX.size(); i++) {
            double doubleX = Double.valueOf(editTextX.getText().toString());
            double doubleY = Double.valueOf(editTextY.getText().toString());

            result = doubleX * intersectionPointsX.get(i) + doubleY * intersectionPointsY.get(i);
            if (string.equals("max")) {
                if (result > max) {
                    max = result;
                    maxPointNumber = i;
                }
            } else if (string.equals("min"))
                if (result < min) {
                    min = result;
                    minPointNumber = i;
                }
        }
        if (string.equals("max"))
            textView.setText("Максимум целевой функции достигается в точке (" + floorNumber(intersectionPointsX.get(maxPointNumber)) +
                    "," + floorNumber(intersectionPointsY.get(maxPointNumber)) + ") и равен " + max);
        else if (string.equals("min"))
            textView.setText("Минимум целевой функции достигается в точке (" + floorNumber(intersectionPointsX.get(minPointNumber)) +
                    "," + floorNumber(intersectionPointsY.get(minPointNumber)) + ") и равен " + min);
    }


    private static double[] gauss(double[][] a, double[] y, int n) {
        final double eps = 0.00001;  // точность
        double[] x;
        double max;

        int k;
        int index;

        x = new double[n];
        k = 0;
        while (k < n) {
            // Поиск строки с максимальным a[i][k]
            max = Math.abs(a[k][k]);
            index = k;
            for (int i = k + 1; i < n; i++)
                if (Math.abs(a[i][k]) > max) {
                    max = Math.abs(a[i][k]);
                    index = i;
                }
            // Перестановка строк
            if (max < eps) {
                // нет ненулевых диагональных элементов
                System.out.println("Решение получить невозможно из-за нулевого столбца ");
                System.out.println(index + " матрицы A" + "\n");
                return new double[0];
            }
            for (int j = 0; j < n; j++) {
                double temp = a[k][j];
                a[k][j] = a[index][j];
                a[index][j] = temp;
            }
            double temp = y[k];
            y[k] = y[index];
            y[index] = temp;
            // Нормализация уравнений
            for (int i = k; i < n; i++) {
                double temp1 = a[i][k];
                if (Math.abs(temp1) < eps) continue; // для нулевого коэффициента пропустить
                for (int j = 0; j < n; j++)
                    a[i][j] = a[i][j] / temp1;
                y[i] = y[i] / temp1;
                if (i == k)
                    continue; // уравнение не вычитать само из себя
                for (int j = 0; j < n; j++)
                    a[i][j] = a[i][j] - a[k][j];
                y[i] = y[i] - y[k];
            }
            k++;
        }
        // обратная подстановка
        for (k = n - 1; k >= 0; k--) {
            x[k] = y[k];
            for (int i = 0; i < k; i++)
                y[i] = y[i] - a[i][k] * x[k];
        }
        return x;
    }
}
