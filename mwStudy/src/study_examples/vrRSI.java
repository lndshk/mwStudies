package study_examples;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.study.*;

/** Relative Strength Index */


@StudyHeader(
 namespace="com.mycompany", 
 id="RSI",
 name="vrRSI",
 label="RSI study",
 desc="RSI with error bands",
 menu="W. VanRip",
 overlay=false,
 studyOverlay=false,
 signals=true)

public class vrRSI extends com.motivewave.platform.sdk.study.Study
{
  enum RSIValues { RSI, UP, DOWN };
  enum RSISignals { RSI_TOP, RSI_BOTTOM };
  enum BBValuesrsi { BBTOP, INNERTOP, MIDDLE, INNERBOTTOM, BBBOTTOM }
  
  final static String RSI_LINE = "rsiLine";
  final static String RSI_IND = "rsiInd";
  
  final static String RSI_INPUT = "rsiinput";
  final static String RSI_PERIOD = "rsiPeriod";
  final static String RSI_METHOD = "rsiMethod";
  final static String RSI_TOP_FILL = "rsiTopFill";
  final static String RSI_BOTTOM_FILL = "rsiBotFill";
  final static String RSI_PLOT = "RSIPlot";

  //Bollinger Band strings
  final static String BB_PERIOD = "bbPeriod";
  final static String OUTER_STD = "topStd", INNER_STD = "bottomStd";
  //final static String BB_INPUT = "rsiinput";
  final static String BB_METHOD = "bbMethod";
  final static String INNERPATH = "bbInnerP";
  final static String OUTERPATH = "bbOuterP";
  final static String BBRSIFILL = "bbRSIfill";
  
  
  /** This method initializes the study by doing the following:
      1. Define Settings (Design Time Information)
      2. Define Runtime Information (Label, Path and Exported Value) */
  @Override
  public void initialize(Defaults defaults)
  {
    SettingsDescriptor sd = new SettingsDescriptor();
    setSettingsDescriptor(sd);

    SettingTab tab = new SettingTab("RSI");
    sd.addTab(tab);

    SettingGroup rsi_inputs = new SettingGroup("Inputs");
    rsi_inputs.addRow(new InputDescriptor(RSI_INPUT, "Input", Enums.BarInput.CLOSE));
    rsi_inputs.addRow(new MAMethodDescriptor(RSI_METHOD, "Method", Enums.MAMethod.EMA));
    rsi_inputs.addRow(new IntegerDescriptor(RSI_PERIOD, "Period", 14, 1, 9999, 1));
    tab.addGroup(rsi_inputs);
    
    SettingGroup rsi_lines = new SettingGroup("Lines");
    rsi_lines.addRow(new PathDescriptor(RSI_LINE, "RSI Line", defaults.getLineColor(), 1.0f, null));
    rsi_lines.addRow(new ShadeDescriptor(RSI_TOP_FILL, get("LBL_TOP_FILL"), Inputs.TOP_GUIDE, RSI_LINE, Enums.ShadeType.ABOVE, defaults.getTopFillColor(), true, true));
    rsi_lines.addRow(new ShadeDescriptor(RSI_BOTTOM_FILL, get("LBL_BOTTOM_FILL"), Inputs.BOTTOM_GUIDE, RSI_LINE, Enums.ShadeType.BELOW, defaults.getBottomFillColor(), true, true));
    rsi_lines.addRow(new IndicatorDescriptor(RSI_IND, get("LBL_RSI_IND"), null, null, false, true, true));
    tab.addGroup(rsi_lines);

    //tab = new SettingTab(get("TAB_ADVANCED"));
    //sd.addTab(tab);
    SettingGroup guides = new SettingGroup("Guide Lines");
    guides.addRow(new GuideDescriptor(Inputs.TOP_GUIDE, "Top Guide", 70, 1, 100, 1, true));
    GuideDescriptor gd = new GuideDescriptor(Inputs.TOP_GUIDE2, "Top Guide 2", 60, 1, 100, 1, true);
    gd.setEnabled(false);
    guides.addRow(gd);
    GuideDescriptor mg = new GuideDescriptor(Inputs.MIDDLE_GUIDE, "Middle Guide", 50, 1, 100, 1, true);
    mg.setDash(new float[] {3, 3});
    guides.addRow(mg);
    gd = new GuideDescriptor(Inputs.BOTTOM_GUIDE2, "Bottom Guide 2", 40, 1, 100, 1, true);
    gd.setEnabled(false);
    guides.addRow(gd);
    guides.addRow(new GuideDescriptor(Inputs.BOTTOM_GUIDE, "Bottom Guide", 30, 1, 100, 1, true));
    tab.addGroup(guides);

    SettingTab tab2 = new SettingTab("BB");
    sd.addTab(tab2);

    SettingGroup bb_inputs = new SettingGroup("Inputs");
    bb_inputs.addRow(new MAMethodDescriptor(BB_METHOD, "Method", Enums.MAMethod.EMA));
    bb_inputs.addRow(new IntegerDescriptor(BB_PERIOD, "Period", 14, 1, 9999, 1));
    bb_inputs.addRow(new DoubleDescriptor(INNER_STD, get("Inner Std Dev"), 0.118, 0.001, 10, 0.01));
    bb_inputs.addRow(new DoubleDescriptor(OUTER_STD, get("Outer Std Dev"), 2.0, 0.1, 10, 0.1));
    
    tab2.addGroup(bb_inputs);

    SettingGroup bb_colors = new SettingGroup(get("LBL_COLORS"));
    bb_colors.addRow(new PathDescriptor(INNERPATH, "Inner Band Lines", X11Colors.CADET_BLUE, 1.0f, null, true, true, false));
    bb_colors.addRow(new PathDescriptor(OUTERPATH, "Outter Band Lines", X11Colors.DARK_SLATE_GRAY, 1.0f, new float[] {3f, 3f}, true, true, true));
    bb_colors.addRow(new ShadeDescriptor(BBRSIFILL, "Inner Band Fill", INNERPATH, INNERPATH, Enums.ShadeType.BOTH, defaults.getFillColor(), false, true));
    tab2.addGroup(bb_colors);


    // Describe the runtime settings using a 'StudyDescriptor'
    RuntimeDescriptor desc = new RuntimeDescriptor();
    
    desc.setLabelSettings(RSI_INPUT, RSI_PERIOD);
    desc.setLabelPrefix("RSI");
    desc.setTabName("RSI");
    desc.declarePath(RSIValues.RSI, RSI_LINE);
    desc.declareIndicator(RSIValues.RSI, RSI_IND);
    desc.setMaxBottomValue(15);
    desc.setMinTopValue(85);
    desc.setRangeKeys(RSIValues.RSI);
    desc.setMinTick(0.1);

    //added bb plot  { BBTOP, INNERTOP, MIDDLE, INNERBOTTOM, BBBOTTOM }

    desc.declarePath(BBValuesrsi.INNERTOP, INNERPATH);
    desc.declarePath(BBValuesrsi.INNERBOTTOM, INNERPATH);
    desc.declarePath(BBValuesrsi.BBTOP, OUTERPATH);
    desc.declarePath(BBValuesrsi.BBBOTTOM, OUTERPATH);
    
    
    
    
    // RSI Plot
    /*
    Plot rsiPlot = new Plot();
    desc.addPlot(RSI_PLOT, rsiPlot);
    
    rsiPlot.setLabelSettings(RSI_INPUT, RSI_PERIOD);
    rsiPlot.setLabelPrefix("RSI");
    rsiPlot.setTabName("RSI");
    rsiPlot.declarePath(RSIValues.RSI, RSI_LINE);
    rsiPlot.declareIndicator(RSIValues.RSI, RSI_IND);
    rsiPlot.declareGuide(Inputs.TOP_GUIDE);
    rsiPlot.declareGuide(Inputs.MIDDLE_GUIDE);
    rsiPlot.declareGuide(Inputs.BOTTOM_GUIDE);
    rsiPlot.setMaxBottomValue(15);
    rsiPlot.setMinTopValue(85);
    rsiPlot.setRangeKeys(RSIValues.RSI);
    rsiPlot.setMinTick(0.1);

    //added bb plot  { BBTOP, INNERTOP, MIDDLE, INNERBOTTOM, BBBOTTOM }

    rsiPlot.declarePath(BBValuesrsi.INNERTOP, INNERPATH);
    rsiPlot.declarePath(BBValuesrsi.INNERBOTTOM, INNERPATH);
    rsiPlot.declarePath(BBValuesrsi.BBTOP, OUTERPATH);
    rsiPlot.declarePath(BBValuesrsi.BBBOTTOM, OUTERPATH);
    */
    
    setRuntimeDescriptor(desc);

  }

  @Override  
  protected void calculate(int index, DataContext ctx)
  {
    int period = getSettings().getInteger(RSI_PERIOD);
    if (index < 1) return; // not enough data
    DataSeries series = ctx.getDataSeries();
    Object input = getSettings().getInput(RSI_INPUT);
    
    double diff = series.getDouble(index, input) - series.getDouble(index-1, input);
    double up = 0, down = 0;
    if (diff > 0) up = diff;
    else down = diff;
    
    series.setDouble(index, RSIValues.UP, up);
    series.setDouble(index, RSIValues.DOWN, Math.abs(down));
    
    if (index <= period +1) return;
    
    Enums.MAMethod method = getSettings().getMAMethod(RSI_METHOD);
    Double avgUp = series.ma(method, index,  period, RSIValues.UP);
    Double avgDown = series.ma(method, index,  period, RSIValues.DOWN);
    if (avgUp == null || avgDown == null) return;
    double RS = avgUp / avgDown;
    double RSI = 100.0 - ( 100.0 / (1.0 + RS));

    series.setDouble(index, RSIValues.RSI, RSI);  // <- RSI Calculated here

    // Calculate BB on the RSI
    int bbPeriod = getSettings().getInteger(BB_PERIOD);
    Enums.MAMethod bbmethod = getSettings().getMAMethod(BB_METHOD, Enums.MAMethod.EMA);
    double innerStd = getSettings().getDouble(INNER_STD);
    double outerStd = getSettings().getDouble(OUTER_STD);
   
    //int latest = series.size()-1;
    //Object inputrsi = Enums.BarInput[].values(RSIValues.RSI);
   
    Double rsiStdDev = series.std(index, bbPeriod, RSIValues.RSI );
    Double bbma = series.ma(bbmethod, index, bbPeriod, RSIValues.RSI);
    debug("index = " + index + " bbma = " + bbma + " rsiStdDev = " + rsiStdDev);
    
    if (rsiStdDev == null || bbma == null) {rsiStdDev = .0000001; bbma = 0.000001;}

    //{ BBTOP, INNERTOP, MIDDLE, INNERBOTTOM, BBBOTTOM }
    series.setDouble(index, BBValuesrsi.BBTOP, bbma + rsiStdDev*outerStd);
    series.setDouble(index, BBValuesrsi.INNERTOP, bbma + rsiStdDev*innerStd);
    series.setDouble(index, BBValuesrsi.INNERBOTTOM, bbma - rsiStdDev*innerStd);
    series.setDouble(index, BBValuesrsi.BBBOTTOM, bbma- rsiStdDev*outerStd);


    // Do we need to generate an RSI signal?
    GuideInfo topGuide = getSettings().getGuide(Inputs.TOP_GUIDE);
    GuideInfo bottomGuide = getSettings().getGuide(Inputs.BOTTOM_GUIDE);
    if (crossedAbove(series, index, RSIValues.RSI, topGuide.getValue())) {
      series.setBoolean(index, RSISignals.RSI_TOP, true);
      ctx.signal(index, RSISignals.RSI_TOP, get("SIGNAL_RSI_TOP", topGuide.getValue(), round(RSI)), round(RSI));
    }
    else if (crossedBelow(series, index, RSIValues.RSI, bottomGuide.getValue())) {
      series.setBoolean(index, RSISignals.RSI_BOTTOM, true);
      ctx.signal(index, RSISignals.RSI_BOTTOM, get("SIGNAL_RSI_BOTTOM", bottomGuide.getValue(), round(RSI)), round(RSI));
    }
    
    series.setComplete(index, series.isBarComplete(index));
  } 
    
  
}
