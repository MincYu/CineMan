package jp.co.worksap.intern.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.ui.market.MemberClassifyController;
import jp.co.worksap.intern.ui.sales.SalesStrategyController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * make encapsulation in building pie chart
 * Created by yuminchen on 16/7/25.
 */
public class PieChartBuilder {

    private PieChart chart;
    private ObservableList<PieChart.Data> pieChartData = FXCollections
            .observableArrayList();
    private Map<String, Double> dataMap = new HashMap<String, Double>();

    /**
     *
     */
    public PieChartBuilder() {
        chart = new PieChart();
    }

    /**
     * reload chart，from fxml
     *
     * @param chart
     */
    public PieChartBuilder(PieChart chart) {
        this.chart = chart;
    }

    /**
     *
     *
     * @param chart
     * @param dataMap
     */
    public PieChartBuilder(PieChart chart, Map<String, Double> dataMap) {
        this(chart);
        this.dataMap = dataMap;
    }

    /**
     *
     * @param data
     * @return
     */
    public PieChartBuilder addData(Map<String, Double> data, boolean hasNext, boolean isMarket) {

        double sum = data
                .values()
                .stream()
                .reduce(0.0,Double::sum);

        for (String name : data.keySet()) {
            if (dataMap.get(name) == null) {
                dataMap.put(name, data.get(name));
                configureAddData(new PieChart.Data(name, data.get(name)),sum,hasNext,isMarket);
            }
        }
        return this;
    }

    /**
     *
     * @param key
     * @return
     */
    public PieChartBuilder removeData(final String key) {
        chart.getData().removeIf(new Predicate<PieChart.Data>() {
            @Override
            public boolean test(PieChart.Data t) {
                if (t.getName().equals(key)
                        && t.getPieValue() == dataMap.get(key)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        return this;
    }



    /**
     *
     * @param dataMap
     * @return
     */
    public PieChartBuilder setData(Map<String, Double> dataMap,boolean hasNext,boolean isMarket) {
        this.dataMap = dataMap;
        double sum = dataMap
                .values()
                .stream()
                .reduce(0.0,Double::sum);

        deleteAllData();
        for (String name : dataMap.keySet()) {
            configureAddData(new PieChart.Data(name, dataMap.get(name)),sum,hasNext,isMarket);
        }
        return this;
    }

    /**
     *
     * @return
     */
    public PieChartBuilder deleteAllData() {
        pieChartData = FXCollections.observableArrayList();
        chart.setData(pieChartData);
        return this;
    }

    /**
     *
     * @return
     */
    public Map<String, Double> getData() {
        return dataMap;
    }

    /**
     *
     * @return
     */
    public PieChart build() {
        return chart;
    }

    private Tooltip createToolTip(String data) {
        return new Tooltip(data);
    }

    private void configureAddData(PieChart.Data data,double sum,boolean hasNext, boolean isMarket) {

        chart.getData().add(data);

        final Tooltip tooltip;
        if(isMarket){
            tooltip = createToolTip(Math.round(data.getPieValue()*10000)/100.00+"%");
        }
        else {
            double rate =Math.round(data.getPieValue()*100/sum*10)/10.0;
            tooltip = createToolTip(data.getPieValue() + "\n" + rate + "%");
        }
        data.getNode().setOnMouseMoved(e->
                tooltip.show(data.getNode(), e.getScreenX() + 20, e.getScreenY()));



        data.getNode().setOnMouseExited(e->tooltip.hide());
        if (hasNext) {
            if(isMarket){

                data.getNode().setOnMouseClicked(e->{
                    String typeString = data.getName()
                        .split(":")[1]
                        .split(",")[0];
                    String second = data.getName()
                            .split(":")[1]
                            .split(",")[1];
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(typeString);
                    Matcher m1 = p.matcher(second);
                    typeString = m.replaceAll("");
                    second = m1.replaceAll("");

                    MemberClassifyController.getController().jump2Detail(
                            CommodityType.valueOf(typeString),CommodityType.valueOf(second));}
                );
            }
            else {
                data.getNode().setOnMouseClicked(e -> {
                    SalesStrategyController.getController().jump2Commodity(CommodityType.valueOf(data.getName()));
                });
            }
        }

    }
}
