package jp.co.worksap.intern.ui.market;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import jp.co.worksap.intern.bl.membermarket.MemberMarketImpl;
import jp.co.worksap.intern.bl.salesanalysis.SalesAnalysisImpl;
import jp.co.worksap.intern.blservice.membermarketservice.MemberMarketService;
import jp.co.worksap.intern.blservice.salesanalysisservice.SalesAnalysisService;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.ui.GeneralController;
import jp.co.worksap.intern.ui.sales.CommodityTrendController;
import jp.co.worksap.intern.ui.sales.SalesStrategyController;
import jp.co.worksap.intern.ui.utility.Set2String;
import jp.co.worksap.intern.util.PieChartBuilder;
import jp.co.worksap.intern.vo.CommoditySalesVO;
import jp.co.worksap.intern.vo.PreferenceVO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/7/26.
 */
public class MemberClassifyController {

    private static MemberClassifyController controller;
    private AnchorPane panel;

    @FXML
    private PieChart pieChart;

    @FXML
    private ComboBox<String> regionNames;

    @FXML
    private ComboBox<Integer> memberTypes;

    @FXML
    private Label bestGroup;
    @FXML
    private Label firstGroup;
    @FXML
    private Label secondGroup;
    @FXML
    private Label thirdGroup;
    @FXML
    private Label fourthGroup;

    public static Pane launch() throws IOException{
        if(controller!=null){
            return controller.panel;
        }

        FXMLLoader loader = new FXMLLoader(
                MemberClassifyController.class.getResource("MemberClassify.fxml"));

        Pane pane = loader.load();
        controller = loader.getController();

        if(pane instanceof AnchorPane) {
            controller.panel = (AnchorPane) pane;
        }
        return pane;
    }

    @FXML
    private void initialize() throws IOException{

        salesAnalysis = new SalesAnalysisImpl();
        market = new MemberMarketImpl();

        List<String> regions = salesAnalysis.getAllRegion()
                .stream()
                .map(o -> o.getName())
                .collect(Collectors.toList());
        regionNames.setItems(FXCollections.observableList(regions));
        regionNames.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showPieChart(market.classifyCustomer(
                            salesAnalysis.getRegionInfoByName(newValue).getRegionId(),4));
                    memberTypes.setValue(4);
                    changeGroup(market.getSuitSet(
                            salesAnalysis.getRegionInfoByName(regionNames.getValue()).getRegionId()));

                }
        );

        List<Integer> numbers = Arrays.asList(2,3,4,5);
        memberTypes.setItems(FXCollections.observableList(numbers));
        memberTypes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showPieChart(market.classifyCustomer(
                            salesAnalysis.getRegionInfoByName(regionNames.getValue()).getRegionId(),newValue));
                    changeGroup(market.getSuitSet(
                            salesAnalysis.getRegionInfoByName(regionNames.getValue()).getRegionId()));

                }
        );

    }

    private void changeGroup(Collection<Set<CommodityType>> suitSet) {

        clearAll();
        LinkedList<Set<CommodityType>> lists = suitSet.stream().collect(Collectors.toCollection(LinkedList::new));
        bestGroup.setText(Set2String.set2String(lists.get(0)));

        Iterator<Set<CommodityType>> itetator = lists.iterator();
        itetator.next();
        while (itetator.hasNext()){
            if(firstGroup.getText()==""){
                firstGroup.setText(Set2String.set2String(itetator.next()));
                continue;
            }
            if(secondGroup.getText()==""){
                secondGroup.setText(Set2String.set2String(itetator.next()));
                continue;
            }
            if(thirdGroup.getText()==""){
                thirdGroup.setText(Set2String.set2String(itetator.next()));
                continue;
            }
            if(fourthGroup.getText()==""){
                fourthGroup.setText(Set2String.set2String(itetator.next()));
                continue;
            }

        }
    }

    private void clearAll() {
        bestGroup.setText("");
        firstGroup.setText("");
        secondGroup.setText("");
        thirdGroup.setText("");
        fourthGroup.setText("");
    }

    private void showPieChart(Collection<PreferenceVO> preferenceVOs) {
        pieData.clear();
        pieChart.getData().clear();
        for (PreferenceVO vo : preferenceVOs){
            pieData.put("preference: \n"+ vo.type.name()+",\n"+vo.second.name(),vo.percentage);
        }
        pieChart = new PieChartBuilder(pieChart).addData(pieData,true,true).build();

    }

    private SalesAnalysisService salesAnalysis;
    private MemberMarketService market;
    private Map<String,Double> pieData = new HashMap<>();

    public static MemberClassifyController getController() {
        return controller;
    }

    public void jump2Detail(CommodityType type, CommodityType second){

        try {
            Pane pane = DetailedInfoController.launch(
                    market,salesAnalysis.getRegionInfoByName(regionNames.getValue()),type,second);
            GeneralController.getController().changeCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
