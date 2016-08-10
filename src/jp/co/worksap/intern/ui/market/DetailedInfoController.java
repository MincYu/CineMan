package jp.co.worksap.intern.ui.market;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import jp.co.worksap.intern.blservice.membermarketservice.MemberMarketService;
import jp.co.worksap.intern.entities.commodity.CommodityType;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.ui.utility.Set2String;
import jp.co.worksap.intern.util.PieChartBuilder;
import jp.co.worksap.intern.vo.PreferenceVO;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuminchen on 16/7/26.
 */
public class DetailedInfoController {

    private static DetailedInfoController controller;
    private AnchorPane panel;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label regionLabel;

    @FXML
    private Label groupLabel;

    public static Pane launch(MemberMarketService service, RegionMstDTO region, CommodityType type, CommodityType second) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                DetailedInfoController.class.getResource("DetailedInfo.fxml"));

        Pane pane = loader.load();
        controller = loader.getController();
        controller.marketService = service;
        controller.regionMstDTO =region;
        controller.type = type;
        controller.secondType = second;
        controller.regionLabel.setText(region.getName());
        controller.showPieChart();
        controller.groupLabel.setText(
                Set2String.set2String(service.getOneSet(region.getRegionId(),type,second)));
        if(pane instanceof AnchorPane) {
            controller.panel = (AnchorPane) pane;
        }
        return pane;
    }

    /**
     * show average custom in the level of customers
     */
    private void showPieChart() {
        pieData.clear();
        pieChart.getData().clear();
        Collection<PreferenceVO> preferenceVOs = marketService.getDetailedPrefer(regionMstDTO.getRegionId(),type,secondType);
        for(PreferenceVO preferenceVO : preferenceVOs){
            pieData.put(preferenceVO.type.name(),preferenceVO.percentage);
        }
        pieChart = new PieChartBuilder(pieChart).addData(pieData,false,true).build();

    }

    private MemberMarketService marketService;
    private RegionMstDTO regionMstDTO;
    private CommodityType type;
    private CommodityType secondType;
    private Map<String,Double> pieData = new HashMap<>();

}
