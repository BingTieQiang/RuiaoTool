package com.ruiao.tools.ui.fragment.maintab;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ScrollView;

import com.ruiao.tools.R;
import com.ruiao.tools.ui.activity.functionfragment.PointAdapter;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.utils.CommonUtils;
import com.ruiao.tools.widget.hellocharts.gesture.ContainerScrollType;
import com.ruiao.tools.widget.hellocharts.model.Axis;
import com.ruiao.tools.widget.hellocharts.model.AxisValue;
import com.ruiao.tools.widget.hellocharts.model.Line;
import com.ruiao.tools.widget.hellocharts.model.LineChartData;
import com.ruiao.tools.widget.hellocharts.model.PointValue;
import com.ruiao.tools.widget.hellocharts.model.ValueShape;
import com.ruiao.tools.widget.hellocharts.model.Viewport;
import com.ruiao.tools.widget.hellocharts.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Gs on 2017/4/12.
 */

public class HomeFragment extends BaseFragment  {


    @BindView(R.id.lineView2)
    LineChartView lineChartView2;
    @BindView(R.id.lineView)
    LineChartView lineChartView;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.gv_point)
    GridView points;

    private  String[] lineLabels ;  //时间轴 X轴
    private int[] chartColors ;   //颜色集合
    private String[] arrs;    //参数集合
    @Override
    protected int getContentViewID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        CommonUtils.solveScrollConflict(lineChartView,scrollView);
//        drawChart();
        initData();
        drawLine();

    }

    /**
     * 初始化数据，
     */
    private void initData() {
        chartColors = new int[]{getResources().getColor(R.color.color_FE5E63),
                getResources().getColor(R.color.color_6CABFA),
                getResources().getColor(R.color.color_FBE382),
                (getResources().getColor(R.color.color_595959)),
                getResources().getColor(R.color.color_FF3636),
                getResources().getColor(R.color.green_500),
                getResources().getColor(R.color.color_FBE382),
                getResources().getColor(R.color.color_FBE382),
                getResources().getColor(R.color.color_FBE382),
                getResources().getColor(R.color.color_FBE382),

        };
        //底部坐标 （时间轴）
        lineLabels =  new String[]{"09-12", "09-11", "09-10", "09-09", "09-08", "09-07", "09-06", "09-09", "09-08", "09-07", "09-06","09-07", "09-06", "09-09", "09-08", "09-07", "09-06"};

        //底部坐标指示 圆点
        arrs = new String[]{"总烃","烃","烃2","hh"};


    }


    private void drawLine() {


        int maxNumberOfLines = 3;
        int numberOfPoints = lineLabels.length;
        ValueShape shape = ValueShape.CIRCLE;
        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }


        List<Line> lines = new ArrayList<Line>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < maxNumberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]).setLabelColor(getResources().getColor(R.color.color_FE5E63))
                        .setLabelTextsize(CommonUtils.dp2px(getActivity(), 15)));
            }
            Line line = new Line(values);
            line.setColor(chartColors[i]);
            line.setShape(shape);
            line.setPointRadius(3);
            line.setStrokeWidth(1);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(true);
            line.setHasLines(true);
            line.setHasPoints(true);
            //line.setPointColor(R.color.transparent);
            line.setHasGradientToTransparent(true);
//            if (pointsHaveDifferentColor){
//                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//            }
            lines.add(line);

        }

        LineChartData data = new LineChartData(lines);
        for (int i = 0; i < numberOfPoints; i++) {
            axisValues.add(new AxisValue(i).setLabel(lineLabels[i]));
        }
        Axis axisX = new Axis(axisValues).setMaxLabelChars(5);
        axisX.setTextColor(getResources().getColor(R.color.color_969696))
                .setTextSize(10).setLineColor(getResources().getColor(R.color.color_e6e6e6))
                .setHasSeparationLineColor(getResources().getColor(R.color.color_e6e6e6)).setHasTiltedLabels(true);
        data.setAxisXBottom(axisX);
        Axis axisY = new Axis().setHasLines(true).setHasSeparationLine(false).setMaxLabelChars(3);
        axisY.setTextColor(getResources().getColor(R.color.color_969696));
        axisY.setTextSize(10);


        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);


        lineChartView.setZoomEnabled(false);
        lineChartView.setScrollEnabled(true);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setValueSelectionEnabled(true);//点击折线点可以显示label
        lineChartView.setLineChartData(data);
        // Reset viewport height range to (0,100)
        lineChartView.setViewportCalculationEnabled(false);



        //让布局能够水平滑动要设置setCurrentViewport比setMaximumViewport小
        final Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 105;
        v.left = 0;
        v.right = numberOfPoints - 1 + 0.5f;
        lineChartView.setMaximumViewport(v);
        v.left = 0;
        v.right = Math.min(9, numberOfPoints - 1 + 0.5f);
        lineChartView.setCurrentViewport(v);

        //底部坐标指示 圆点
        points.setAdapter(new PointAdapter(getContext(),arrs,chartColors));

    }




}
