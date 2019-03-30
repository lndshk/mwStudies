package study_examples;

import com.motivewave.platform.sdk.common.Coordinate;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Defaults;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Enums.MAMethod;
import com.motivewave.platform.sdk.common.Inputs;
import com.motivewave.platform.sdk.common.MarkerInfo;
import com.motivewave.platform.sdk.common.desc.GuideDescriptor;
import com.motivewave.platform.sdk.common.desc.IndicatorDescriptor;
import com.motivewave.platform.sdk.common.desc.IntegerDescriptor;
import com.motivewave.platform.sdk.common.desc.MAMethodDescriptor;
import com.motivewave.platform.sdk.common.desc.MarkerDescriptor;
import com.motivewave.platform.sdk.common.desc.PathDescriptor;
import com.motivewave.platform.sdk.common.desc.SettingGroup;
import com.motivewave.platform.sdk.common.desc.SettingTab;
import com.motivewave.platform.sdk.common.desc.SettingsDescriptor;
import com.motivewave.platform.sdk.common.desc.ValueDescriptor;
import com.motivewave.platform.sdk.draw.Marker;
import com.motivewave.platform.sdk.study.RuntimeDescriptor;
import com.motivewave.platform.sdk.study.StudyHeader;

import study_examples.vrSMI;

/** Stochastic Momentum Index */
@StudyHeader(
    namespace="com.motivewave", 
    id="vrSMItester", 
    rb="com.motivewave.platform.study.nls.strings",
    name="TITLE_SMI",
    label="SMI",
    desc="SMI for testing",
    menu="W. VanRip",
    overlay=false,
    studyOverlay=true,
    signals=false,
    helpLink="http://www.motivewave.com/studies/stochastic_momentum_index.htm")

public class vrSMImain extends com.motivewave.platform.sdk.study.Study 
{
	enum Values { SMI, SMI_SIGNAL, // Exported Values
	              // These values are used for calculating averages and smoothed averages
	              D, HL, D_MA, HL_MA };
	              
	//enum Signals { CROSS_ABOVE, CROSS_BELOW };
	
  final static String HL_PERIOD = "hlPeriod";
  final static String MA_PERIOD = "maPeriod";
  final static String SMOOTH_PERIOD = "smoothPeriod";
  
  
  @Override
  public void initialize(Defaults defaults)
  {
    SettingsDescriptor sd = new SettingsDescriptor();
    SettingTab tab = new SettingTab(get("TAB_GENERAL"));
    sd.addTab(tab);
    setSettingsDescriptor(sd);

    SettingGroup inputs = new SettingGroup(get("LBL_INPUTS"));
    inputs.addRow(new MAMethodDescriptor(Inputs.METHOD, get("LBL_METHOD"), Enums.MAMethod.EMA));
    inputs.addRow(new IntegerDescriptor(HL_PERIOD, get("LBL_HL_PERIOD"), 2, 1, 9999, 1));
    inputs.addRow(new IntegerDescriptor(MA_PERIOD, get("LBL_MA_PERIOD"), 8, 1, 9999, 1));
    inputs.addRow(new IntegerDescriptor(SMOOTH_PERIOD, get("LBL_SMOOTH_PERIOD"), 5, 1, 9999, 1));
    inputs.addRow(new IntegerDescriptor(Inputs.SIGNAL_PERIOD, get("LBL_SIGNAL_PERIOD"), 5, 1, 9999, 1));
    tab.addGroup(inputs);
    
    SettingGroup lines = new SettingGroup(get("LBL_LINES"));
    PathDescriptor path = new PathDescriptor(Inputs.PATH, get("LBL_SMI"), defaults.getLineColor(), 1.5f, null, true, false, true);
    path.setSupportsShowAsBars(true);
    lines.addRow(path);
    PathDescriptor signalPath = new PathDescriptor(Inputs.SIGNAL_PATH, get("LBL_SIGNAL_LINE"), defaults.getRed(), 1.0f, null, true, false, false);
    signalPath.setSupportsShowAsBars(true);
    lines.addRow(signalPath);
    lines.addRow(new MarkerDescriptor(Inputs.UP_MARKER, get("LBL_UP_MARKER"), 
        Enums.MarkerType.TRIANGLE, Enums.Size.VERY_SMALL, defaults.getGreen(), defaults.getLineColor(), true, false));
    lines.addRow(new MarkerDescriptor(Inputs.DOWN_MARKER, get("LBL_DOWN_MARKER"), 
        Enums.MarkerType.TRIANGLE, Enums.Size.VERY_SMALL, defaults.getRed(), defaults.getLineColor(), true, false));

    tab.addGroup(lines);

    tab = new SettingTab(get("TAB_ADVANCED"));
    sd.addTab(tab);
    SettingGroup indicators = new SettingGroup(get("LBL_INDICATORS"));
    indicators.addRow(new IndicatorDescriptor(Inputs.IND, get("LBL_INDICATOR"), null, null, false, true, true));
    indicators.addRow(new IndicatorDescriptor(Inputs.SIGNAL_IND, get("LBL_SIGNAL_IND"), defaults.getRed(), null, false, false, true));
    tab.addGroup(indicators);

    SettingGroup guides = new SettingGroup(get("LBL_GUIDES"));
    guides.addRow(new GuideDescriptor(Inputs.TOP_GUIDE, get("LBL_TOP_GUIDE"), 40, -100, 100, 1, true));
    GuideDescriptor mg = new GuideDescriptor(Inputs.MIDDLE_GUIDE, get("LBL_MIDDLE_GUIDE"), 0, -100, 100, 1, true);
    mg.setDash(new float[] {3, 3});
    guides.addRow(mg);
    guides.addRow(new GuideDescriptor(Inputs.BOTTOM_GUIDE, get("LBL_BOTTOM_GUIDE"), -40, -100, 100, 1, true));
    tab.addGroup(guides);

    RuntimeDescriptor desc = new RuntimeDescriptor();
    desc.setLabelSettings(HL_PERIOD, MA_PERIOD, SMOOTH_PERIOD, Inputs.SIGNAL_PERIOD);
    desc.exportValue(new ValueDescriptor(Values.SMI, get("LBL_SMI"), new String[] {HL_PERIOD, MA_PERIOD, SMOOTH_PERIOD}));
    desc.exportValue(new ValueDescriptor(Values.SMI_SIGNAL, get("LBL_SMI_SIGNAL"), new String[] {Inputs.SIGNAL_PERIOD}));
    desc.declarePath(Values.SMI, Inputs.PATH);
    //desc.declarePath(Values.SMI_SIGNAL, Inputs.SIGNAL_PATH);
    desc.declareIndicator(Values.SMI, Inputs.IND);
    //desc.declareIndicator(Values.SMI_SIGNAL, Inputs.SIGNAL_IND);
    
    //desc.declareSignal(Signals.CROSS_ABOVE, get("LBL_CROSS_ABOVE_SIGNAL"));
    //desc.declareSignal(Signals.CROSS_BELOW, get("LBL_CROSS_BELOW_SIGNAL"));
    
    desc.setFixedTopValue(100);
    desc.setFixedBottomValue(-100);
    desc.setMinTick(0.1);
    
    setRuntimeDescriptor(desc);
    
  }

  @Override  
  protected void calculate(int index, DataContext ctx)
  {
    
	int hlPeriod2 = getSettings().getInteger(HL_PERIOD);
    int maPeriod2 = getSettings().getInteger(MA_PERIOD);
    int smoothPeriod2= getSettings().getInteger(SMOOTH_PERIOD);
    Enums.MAMethod method2 = getSettings().getMAMethod(Inputs.METHOD);
    DataSeries series = ctx.getDataSeries();

    final SMIinput F1smi = new SMIinput(hlPeriod2, maPeriod2, smoothPeriod2, method2, null, null, null, null, null, 0.0);
    //Joe - this instantiates SMIinput. I made it 'final' so that the data would not be overwritten on every bar update
    
    
    vrSMI.getvrSMI(F1smi, index, ctx);  //calls the SMI method
    		
    series.setDouble(index, Values.SMI, F1smi.SMId);
    // Joe - line 133, I could not figure out a to make any value from the SMIinput class = Values.SMI, so I did this calc.
    //		 Do you have any ideas on what I can do that could remove this line entirely? I would like it in my method.
    
   
    series.setComplete(index); //Joe - is this required here for the SDK? 
  }

 
  
  
}
