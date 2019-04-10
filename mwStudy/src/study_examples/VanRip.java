package study_examples;

import java.awt.Color;

import com.motivewave.platform.sdk.common.BarSize;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Defaults;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Inputs;
import com.motivewave.platform.sdk.common.desc.GuideDescriptor;
import com.motivewave.platform.sdk.common.desc.IndicatorDescriptor;
import com.motivewave.platform.sdk.common.desc.DoubleDescriptor;
import com.motivewave.platform.sdk.common.desc.InputDescriptor;
import com.motivewave.platform.sdk.common.desc.IntegerDescriptor;
import com.motivewave.platform.sdk.common.desc.MAMethodDescriptor;
import com.motivewave.platform.sdk.common.desc.PathDescriptor;
import com.motivewave.platform.sdk.common.desc.SettingGroup;
import com.motivewave.platform.sdk.common.desc.SettingTab;
import com.motivewave.platform.sdk.common.desc.SettingsDescriptor;
import com.motivewave.platform.sdk.common.desc.ShadeDescriptor;
import com.motivewave.platform.sdk.common.desc.ValueDescriptor;
import com.motivewave.platform.sdk.study.Plot;
import com.motivewave.platform.sdk.study.RuntimeDescriptor;
import com.motivewave.platform.sdk.study.StudyHeader;


/** Combines a KAMA, BBands SMI and RSI into one study. */
/** All indicators are modified specifically to work together */


@StudyHeader(
    namespace="com.Rsquared", 
    id="vrBB & MA", 
    rb="study_examples.nls.strings", // locale specific strings are loaded from here
    name="vrBB + SMA",
    desc="This study does all the things",
    menu="W. VanRip",
    overlay=true,
    signals=true) 
public class VanRip extends com.motivewave.platform.sdk.study.Study //VanRip class
{
	enum MAValues { MA };
	enum BBValues { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};			
			
  
  /* strings for SMA */

  
  final static String MA_PERIOD = "maPeriod";
  final static String MA_INPUT = "maInput";
  final static String MA_METHOD = "maMethod";
  final static String MA_PATH = "maPath";
  final static String MA_INDICATOR = "maIndicator";
  
  /* strings for BBands */
  final static String BB_INPUT = "BBInput";
  final static String BB_METHOD = "BBMethod";
  final static String BB_PERIOD = "BBPeriod";
  
  final static String BB_STD_1 = "BBstd1";  //Std Devs
  final static String BB_STD_2 = "BBstd2";
  final static String BB_STD_3 = "BBstd3";
  final static String BB_STD_4 = "BBstd4";
  
  final static String BB_STD_L1 = "BBStdL1"; //Paths
  final static String BB_STD_L2 = "BBStdL2";
  final static String BB_STD_L3 = "BBStdL3";
  final static String BB_STD_L4 = "BBStdL4";
  final static String BB_PATH = "BBPath";

  
  @Override
  public void initialize(Defaults defaults)
  {
	  
	SettingsDescriptor sd = new SettingsDescriptor();
    setSettingsDescriptor(sd);
    
    SettingTab tab = new SettingTab("Price Chart");
    sd.addTab(tab);
    
    /* Simple Moving Average */
    SettingGroup inputs = new SettingGroup("Moving Average");
    	
    inputs.addRow(new IntegerDescriptor(MA_PERIOD, "Period", 200, 1, 300, 1),
			   new MAMethodDescriptor(MA_METHOD, "Method", Enums.MAMethod.EMA),
			   new InputDescriptor(MA_INPUT, "Input", Enums.BarInput.CLOSE));

    inputs.addRow(new PathDescriptor(MA_PATH, get("LBL_LINE"), defaults.getYellow(), 1.5f, null, true, true, true));
    inputs.addRow(new IndicatorDescriptor(MA_INDICATOR,"Line Label", null, null, false, true, true));
        
    tab.addGroup(inputs);
        
        
    
    /* Bollinger Bands */
    SettingGroup inputs2 = new SettingGroup("Bollinger Band Inputs");
    inputs2.addRow(new IntegerDescriptor(BB_PERIOD, "Period", 50, 1, 300, 1),
    			   new MAMethodDescriptor(BB_METHOD, "Method", Enums.MAMethod.EMA),
    			   new InputDescriptor(BB_INPUT, "Input", Enums.BarInput.CLOSE));
    tab.addGroup(inputs2);
    
    SettingGroup inputs3 = new SettingGroup("Standard Deviations");
    inputs3.addRow(new DoubleDescriptor(BB_STD_1, "Std Dev 1", 2.0, .1, 3, .1));  //Std Dev
    inputs3.addRow(new DoubleDescriptor(BB_STD_2, "Std Dev 2", 2.2, .1, 3, .1));
    inputs3.addRow(new DoubleDescriptor(BB_STD_3, "Std Dev 3", 2.4, .1, 3, .1));
    inputs3.addRow(new DoubleDescriptor(BB_STD_4, "Std Dev 4", 2.6, .1, 3, .1));
    tab.addGroup(inputs3);
    
    /* Bollinger Band Colors */
    SettingGroup colors = new SettingGroup("Line Colors");
    colors.addRow(new PathDescriptor(BB_PATH, "Middle", Color.decode("#ECEFF1"), 1.0f, null, true, true, true));
    colors.addRow(new PathDescriptor(BB_STD_L1, get("Std Dev 1"),Color.decode("#ECEFF1"), 1.0f, new float [] {3f,3f}, true, true, true));
    colors.addRow(new PathDescriptor(BB_STD_L2, get("Std Dev 2"), Color.decode("#B0BEC5"), 1.0f, new float [] {3f,3f}, true, true, true));
    colors.addRow(new PathDescriptor(BB_STD_L3, get("Std Dev 3"), Color.decode("#78909C"), 1.0f, new float [] {3f,3f}, true, true, true));   
    colors.addRow(new PathDescriptor(BB_STD_L4, get("Std Dev 4"), Color.decode("#546E7A"), 1.0f, new float [] {3f,3f}, true, true, true));
    tab.addGroup(colors);
    
    RuntimeDescriptor desc = new RuntimeDescriptor();
    setRuntimeDescriptor(desc);

    //desc.exportValue(new ValueDescriptor(Values.MA, "MA", new String[] {MA_INPUT, MA_PERIOD, Inputs.SHIFT, Inputs.BARSIZE}));
    //desc.exportValue(new ValueDescriptor(Values.MTF, "MTF", new String[] {SMI_INPUT, MTF_MULTIPLIER}));
    //desc.exportValue(new ValueDescriptor(Values.RSI, get("LBL_RSI"), new String[] {RSI_INPUT, RSI_PERIOD}));
    //desc.exportValue(new ValueDescriptor(Values.MACD, get("LBL_MACD"), new String[] {MACD_INPUT, MACD_METHOD, MACD_PERIOD1, MACD_PERIOD2}));
    //desc.exportValue(new ValueDescriptor(Values.SIGNAL, get("LBL_MACD_SIGNAL"), new String[] {Inputs.SIGNAL_PERIOD}));
    //desc.exportValue(new ValueDescriptor(Values.HIST, get("LBL_MACD_HIST"), new String[] {MACD_PERIOD1, MACD_PERIOD2, Inputs.SIGNAL_PERIOD}));

    //desc.declareSignal(Signals.CROSS_ABOVE, get("LBL_CROSS_ABOVE_SIGNAL"));
    //desc.declareSignal(Signals.CROSS_BELOW, get("LBL_CROSS_BELOW_SIGNAL"));
    //desc.declareSignal(Signals.RSI_TOP, get("RSI_TOP"));
    //desc.declareSignal(Signals.RSI_BOTTOM, get("RSI_BOTTOM"));
    
    // Price plot (moving average)
    desc.getPricePlot().setLabelSettings(MA_INPUT, MA_PERIOD);
    desc.getPricePlot().setLabelPrefix("MA");
    desc.getPricePlot().declarePath(MAValues.MA, MA_PATH);
    desc.getPricePlot().declareIndicator(MAValues.MA, MA_INDICATOR);

    // Bollinger Band on the price plots
    desc.getPricePlot().declarePath(BBValues.BB1L, BB_STD_L1);  
    desc.getPricePlot().declarePath(BBValues.BB1U, BB_STD_L1);
    desc.getPricePlot().declarePath(BBValues.BB2L, BB_STD_L2);
    desc.getPricePlot().declarePath(BBValues.BB2U, BB_STD_L2);
    desc.getPricePlot().declarePath(BBValues.BB3L, BB_STD_L3);
    desc.getPricePlot().declarePath(BBValues.BB3U, BB_STD_L3);
    desc.getPricePlot().declarePath(BBValues.BB4L, BB_STD_L4);
    desc.getPricePlot().declarePath(BBValues.BB4U, BB_STD_L4);
    desc.getPricePlot().declarePath(BBValues.MID, BB_PATH);
  }

  @Override
  public void onLoad(Defaults defaults)
  {
   setMinBars(3000);
  }
  
  
  @Override  
  protected void calculate(int index, DataContext ctx)
  {
    DataSeries series = ctx.getDataSeries();
   
    //Moving Average
    Object inputMA= getSettings().getInput(MA_INPUT);
    int periodMA = getSettings().getInteger(MA_PERIOD);
    Enums.MAMethod methodMA = getSettings().getMAMethod(MA_METHOD);

    Double simpleMA = series.ma(methodMA, index, periodMA, inputMA);
    
    if (simpleMA == null) simpleMA = 0.00001;
    
    series.setDouble(index, MAValues.MA, simpleMA);
    
    debug("Index = " + index + "  MA value = " + simpleMA);
    debug("Index = " + index + "  MA input = " + inputMA + "  MA Period" + periodMA + "  methodMA" + methodMA);
    //Bollinger Bands

    Object bbinput= getSettings().getInput(BB_INPUT);
    int bbPeriod = getSettings().getInteger(BB_PERIOD);
    Enums.MAMethod bbmethod = getSettings().getMAMethod(BB_METHOD, Enums.MAMethod.EMA);
    double bbStd1 = getSettings().getDouble(BB_STD_1);
    double bbStd2 = getSettings().getDouble(BB_STD_2);
    double bbStd3 = getSettings().getDouble(BB_STD_3);
    double bbStd4 = getSettings().getDouble(BB_STD_4);

    Double bbStdDevs = series.std(index, bbPeriod, bbinput);
    Double bbStdDevma = series.ma(bbmethod, index, bbPeriod, bbinput);

    debug("Index = " + index + "  bbStdDevs = " + bbStdDevs + "  bbStdma" + bbStdDevma);
    
    if (bbStdDevs == null || bbStdDevma == null) {bbStdDevs = .0000001; bbStdDevma = 0.000001;}

    //Band input { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};
    series.setDouble(index, BBValues.BB1U, bbStdDevma + bbStdDevs * bbStd1);
    series.setDouble(index, BBValues.BB1L, bbStdDevma - bbStdDevs* bbStd1);
    series.setDouble(index, BBValues.BB2U, bbStdDevma + bbStdDevs* bbStd2);
    series.setDouble(index, BBValues.BB2L, bbStdDevma - bbStdDevs* bbStd2);
    series.setDouble(index, BBValues.BB3U, bbStdDevma + bbStdDevs* bbStd3);
    series.setDouble(index, BBValues.BB3L, bbStdDevma - bbStdDevs* bbStd3);
    series.setDouble(index, BBValues.BB4U, bbStdDevma + bbStdDevs* bbStd4);
    series.setDouble(index, BBValues.BB4L, bbStdDevma - bbStdDevs* bbStd4);
    series.setDouble(index, BBValues.MID, bbStdDevma);

  }  
}


