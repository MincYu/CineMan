package jp.co.worksap.intern.ui.sales;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import jp.co.worksap.intern.blservice.salesanalysisservice.SalesAnalysisService;
import jp.co.worksap.intern.constants.Constants;
import jp.co.worksap.intern.entities.cinema.CinemaMstDTO;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.entities.commodity.RecordDTO;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.ui.main.Main;
import jp.co.worksap.intern.util.DateTypeConverter;
import jp.co.worksap.intern.util.PieChartBuilder;
import jp.co.worksap.intern.util.Utilities;
import jp.co.worksap.intern.vo.CommoAllocationVO;
import jp.co.worksap.intern.vo.CommoditySalesVO;
import jp.co.worksap.intern.vo.SalesVolumeVO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/7/25.
 */
public class CommodityTrendController {

    private static CommodityTrendController commodityTrendController;
    private AnchorPane panel;

    @FXML
    private Label regionLable;

    @FXML
    private ComboBox<String> commodityTpye;

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<String,Number> lineChart;

    @FXML
    private Label properPriceLable;

    @FXML
    private Label stockNumber;


    public static Pane launch(RegionMstDTO regionMstDTO, String type, SalesAnalysisService salesAnalysisService) throws IOException{

        FXMLLoader loader = new FXMLLoader(
                CommodityTrendController.class.getResource("CommodityTrend.fxml"));

        Pane pane = loader.load();
        commodityTrendController = loader.getController();

        if(pane instanceof AnchorPane) {
            commodityTrendController.panel = (AnchorPane) pane;
        }
        commodityTrendController.selectedType = type;
        commodityTrendController.regionMstDTO = regionMstDTO;
        commodityTrendController.analysis = salesAnalysisService;
        commodityTrendController.commodityTpye.setValue(type);
        commodityTrendController.regionLable.setText(regionMstDTO.getName());


        commodityTrendController.setCinemaMap();
        commodityTrendController.showPieChart(salesAnalysisService.getCommodityAllocation(
                CommodityType.valueOf(type),regionMstDTO.getRegionId()));
        commodityTrendController.showLineChart(salesAnalysisService.getCommodityTrend(
                CommodityType.valueOf(type),regionMstDTO.getRegionId()),type);

        commodityTrendController.commodityTpye.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    commodityTrendController.selectedType = newValue;
                    commodityTrendController.showPieChart(salesAnalysisService.getCommodityAllocation(
                            CommodityType.valueOf(newValue),regionMstDTO.getRegionId()));
                    commodityTrendController.showLineChart(salesAnalysisService.getCommodityTrend(
                            CommodityType.valueOf(newValue),regionMstDTO.getRegionId()),newValue);

                });

        return pane;
    }

    @FXML
    private void initialize(){

        CommodityType[] types = CommodityType.values();
        List<String> stringTypes = new ArrayList<>();

        for(CommodityType type : types){
            stringTypes.add(type.name());
        }
        commodityTpye.setItems(FXCollections.observableList(stringTypes));

    }

    private void showLineChart(Collection<CommoditySalesVO> vos, String name) {
        lineChart.getData().clear();

        double maxXValue = vos
                .stream()
                .mapToDouble(o->o.money)
                .max()
                .getAsDouble();

        double properPrice = vos
                .stream()
                .filter(o->o.money==maxXValue)
                .mapToDouble(o->o.money/o.number)
                .max()
                .getAsDouble();

        properPriceLable.setText(Math.round(properPrice*10)/10.0+"");

        vos
                .stream()
                .sorted((o1, o2) -> (o1.money/o1.number)>(o2.money/o2.number)?1:-1);


        lineChart.getXAxis().setTickLabelGap(0.1);
        lineChart.getXAxis().setLabel("price");
        lineChart.getYAxis().setLabel("sales profit&volume");

        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> volumeSeries = new XYChart.Series<>();

        for(CommoditySalesVO vo : vos){
            profitSeries.getData().add(new XYChart.Data<>(
                    Math.round(vo.money/vo.number*100)/100.00+"",vo.money-vo.number*vo.type.getCost()));
            volumeSeries.getData().add(new XYChart.Data<>(
                    Math.round(vo.money/vo.number*100)/100.00+"",vo.money));
        }

        profitSeries.setName("sales profit & price relation for "+ name);
        volumeSeries.setName("sales volume & price relation for "+ name);

        lineChart.getData().addAll(profitSeries,volumeSeries);

    }

    private void showPieChart(Collection<CommoAllocationVO> vos) {
        pieData.clear();
        pieChart.getData().clear();

        double sum = vos
                .stream()
                .mapToDouble(o ->( o.number+0.0))
                .sum();

        double stock = Constants.STOCK_NUMBER-sum;
        stockNumber.setText(stock+"");

        for (CommoAllocationVO vo : vos){
            pieData.put(cinemaMap.get(vo.cinemaId),Math.round(vo.number/sum*stock)/1.0);
        }
        pieChart = new PieChartBuilder(pieChart).addData(pieData,false,false).build();
    }


    private SalesAnalysisService analysis;
    private String selectedType;
    private RegionMstDTO regionMstDTO;
    private Map<String,Double> pieData = new HashMap<>();
    private Map<Long, String> cinemaMap = new HashMap<>();

    public void setCinemaMap() {

        Collection<CinemaMstDTO> cinemaMstDTOs = analysis.getCinemaInRegion(regionMstDTO.getRegionId());
        for(CinemaMstDTO cinemaMstDTO:cinemaMstDTOs){
            cinemaMap.put(cinemaMstDTO.getCinemaId(), cinemaMstDTO.getCinemaName());
        }
    }

}
