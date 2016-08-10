package jp.co.worksap.intern.ui.sales;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import jp.co.worksap.intern.bl.salesanalysis.SalesAnalysisImpl;
import jp.co.worksap.intern.blservice.salesanalysisservice.SalesAnalysisService;
import jp.co.worksap.intern.entities.cinema.CinemaMstDTO;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.ui.GeneralController;
import jp.co.worksap.intern.ui.market.MemberClassifyController;
import jp.co.worksap.intern.util.DateTypeConverter;
import jp.co.worksap.intern.util.PieChartBuilder;
import jp.co.worksap.intern.vo.CommoditySalesVO;
import jp.co.worksap.intern.vo.SalesVolumeVO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/7/25.
 */
public class SalesStrategyController {


    private static SalesStrategyController controller;

    private AnchorPane panel;


    @FXML
    private ComboBox region;

    @FXML
    private ComboBox cinema;

    @FXML
    private DatePicker picker;

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private void OKListener(){
        LocalDate date = picker.getValue();
        showPieChart(salesAnalysis.getDailyCommoditySalesInRegion(selectedRegion.getRegionId(),date));
    }


    public static SalesStrategyController getController() {
        return controller;
    }

    /**
     * set up the view
     * @return
     * @throws IOException
     */
    public static Pane launch() throws IOException{

        if(controller!=null){
            return controller.panel;
        }

        FXMLLoader loader = new FXMLLoader(
                SalesStrategyController.class.getResource("SalesStrategy.fxml"));

        Pane pane = loader.load();
        controller = loader.getController();

        if(pane instanceof AnchorPane) {
            controller.panel = (AnchorPane) pane;
        }
        return pane;

    }

    public void jump2Commodity(CommodityType type){

        try {
            Pane pane = CommodityTrendController.launch(selectedRegion,type.name(),salesAnalysis);
            GeneralController.getController().changeCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void initialize() throws IOException{
        salesAnalysis = new SalesAnalysisImpl();
        initInfo();
        initRegion();
    }

    private void initRegion() {
        List<String> regionNames = regionMstDTOs
                .stream()
                .map(o->o.getName())
                .collect(Collectors.toList());
        region.setItems(FXCollections.observableList(regionNames));
        region.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    selectedRegion = regionMstDTOs
                            .stream()
                            .filter(o->o.getName().equals(newValue))
                            .collect(Collectors.toList())
                            .get(0);
                    cinemaMstDTOs = salesAnalysis.getCinemaInRegion(selectedRegion.getRegionId());
                    initCinema();
                    showPieChart(salesAnalysis.getCommoditySalesInRegion(selectedRegion.getRegionId()));
                    lineChart.getData().clear();
                    seriesMap.clear();
                    showLineChart(salesAnalysis.getSalesVolumeInRegion(
                            selectedRegion.getRegionId()),selectedRegion.getName());
                });
    }

    private void initCinema() {
        List<String> cinemaNames = cinemaMstDTOs
                .stream()
                .map(o -> o.getCinemaName())
                .collect(Collectors.toCollection(LinkedList::new));

        cinema.setItems(FXCollections.observableList(cinemaNames));
        cinema.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    try {
                        selectedCinema = cinemaMstDTOs
                                .stream()
                                .filter(o -> o.getCinemaName().equals(newValue))
                                .collect(Collectors.toList())
                                .get(0);
                    }
                    catch (Exception e) {
                    }
                    showPieChart(salesAnalysis.getCommoditySalesInCinema(selectedCinema.getCinemaId()));
                    showLineChart(salesAnalysis.getSalesVolumeInCinema(
                            selectedCinema.getCinemaId()),selectedCinema.getCinemaName());
                });
    }



    private void showPieChart(Collection<CommoditySalesVO> commoditySalesVOs) {
        pieData.clear();
        pieChart.getData().clear();
        for (CommoditySalesVO vo : commoditySalesVOs){
            pieData.put(vo.type.name(),vo.money);
        }
        pieChart = new PieChartBuilder(pieChart).addData(pieData,true,false).build();

    }

    private void showLineChart(Collection<SalesVolumeVO> salesVolumeVOs,String name){
        if(seriesMap.containsKey(name)){
            return;
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for(SalesVolumeVO vo : salesVolumeVOs){
            series.getData().add(new XYChart.Data<>(
                    DateTypeConverter.localDate2String(vo.date),vo.totalMoney));
        }

        series.setName("sales volume for "+ name);
        lineChart.getData().addAll(series);
        seriesMap.put(name,series);
    }

    /**
     * initialize the data
     */
    private void initInfo() {
        regionMstDTOs = salesAnalysis.getAllRegion();
    }

    private SalesAnalysisService salesAnalysis;
    private Collection<RegionMstDTO> regionMstDTOs = new LinkedList<>();
    private Collection<CinemaMstDTO> cinemaMstDTOs = new LinkedList<>();
    private RegionMstDTO selectedRegion;
    private CinemaMstDTO selectedCinema;
    private Map<String,Double> pieData = new HashMap<>();
    private Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();





}
