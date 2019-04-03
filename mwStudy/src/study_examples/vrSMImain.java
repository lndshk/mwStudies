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
import com.motivewave.platform.sdk.common.desc.InputDescriptor;
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
    rb="",
    name="SMI for Testing",
    label="SMI",
    desc="SMI for testing",
    menu="W. VanRip",
    overlay=false,
    studyOverlay=true,
    signals=false,
    helpLink="http://www.motivewave.com/studies/stochastic_momentum_index.htm")

public class vrSMImain extends com.motivewave.platform.sdk.study.Study 
{
	  enum SMIValuesMTF1 { SMI, D, HL, D_MA, HL_MA };
	
	  /* *** strings for SMI *** */
	
	  final static String MTF_MULTIPLIER = "mtfMult"; //look for 12x the short timeframe
	  final static String SMI_METHOD = "smiMethod";
	  final static String SMI_INPUT = "smiInput";
	  final static String SMI_SMOOTH = "smiSmooth";
	  final static String SMI_SIGNAL = "smiSignal";
	  
	  final static String SMI_HL_INC = "smiHLinc"; //Current timeframe increments
	  final static String SMI_MA_INC = "smiMAinc";
	  final static String SMI_HL_MTF_INC = "smiHLmtfinc"; //MTF increments
	  final static String SMI_MA_MTF_INC = "smiMAmtfinc";
	  
	  final static String SMI_HL1 = "smiHL1"; //Current timeframe settings
	  final static String SMI_MA1 = "smiMA1";
	  final static String SMI_HL2 = "smiHL2";
	  final static String SMI_MA2 = "smiMA2";
	  final static String SMI_HL3 = "smiHL3";
	  final static String SMI_MA3 = "smiMA3";
	  final static String SMI_HL4 = "smiHL4";
	  final static String SMI_MA4 = "smiMA4";
	  final static String SMI_HL5 = "smiHL5";
	  final static String SMI_MA5 = "smiMA5";
	  final static String SMI_HL6 = "smiHL6";
	  final static String SMI_MA6 = "smiMA6";
	  final static String SMI_HL7 = "smiHL7";
	  final static String SMI_MA7 = "smiMA7";
	  final static String SMI_HL8 = "smiHL8";
	  final static String SMI_MA8 = "smiMA8";
	  final static String SMI_HL9 = "smiHL9";
	  final static String SMI_MA9 = "smiMA9";
	  final static String SMI_HL10 = "smiHL10";
	  final static String SMI_MA10 = "smiMA10";
	  final static String SMI_HL11 = "smiHL11";
	  final static String SMI_MA11 = "smiMA11";
	  final static String SMI_HL12 = "smiHL12";
	  final static String SMI_MA12 = "smiMA12";
	  final static String SMI_HL13 = "smiHL13";
	  final static String SMI_MA13 = "smiMA13";
	  
	  final static String SMI_HL1_MTF = "smiHL1mtf"; //SMI MTF settings
	  final static String SMI_MA1_MTF = "smiMA1mtf";
	  final static String SMI_HL2_MTF = "smiHL2mtf";
	  final static String SMI_MA2_MTF = "smiMA2mtf";
	  final static String SMI_HL3_MTF = "smiHL3mtf";
	  final static String SMI_MA3_MTF = "smiMA3mtf";
	  final static String SMI_HL4_MTF = "smiHL4mtf";
	  final static String SMI_MA4_MTF = "smiMA4mtf";
	  
	  final static String SMI_LINE1 = "smiL1";  //SMI Line Settings
	  final static String SMI_LINE2 = "smiL2";
	  final static String SMI_LINE3 = "smiL3";
	  final static String SMI_LINE4 = "smiL4";
	  final static String SMI_LINE5 = "smiL5";
	  final static String SMI_LINE6 = "smiL6";
	  final static String SMI_LINE7 = "smiL7";
	  final static String SMI_LINE8 = "smiL8";
	  final static String SMI_LINE9 = "smiL9";
	  final static String SMI_LINE10= "smiL10";
	  final static String SMI_LINE11= "smiL11";
	  final static String SMI_LINE12= "smiL12";
	  final static String SMI_LINE13= "smiL13";
	  
	  final static String SMI_LINE1_MTF = "smiL1mtf";
	  final static String SMI_LINE2_MTF = "smiL2mtf";
	  final static String SMI_LINE3_MTF = "smiL3mtf";
	  final static String SMI_LINE4_MTF = "smiL4mtf";
 /* final static String HL_PERIOD = "hlPeriod";
  final static String MA_PERIOD = "maPeriod";
  final static String SMOOTH_PERIOD = "smoothPeriod";
  final static String SMIPATH = "smiPath"; */
  
  @Override
  public void initialize(Defaults defaults)
  {
		SettingsDescriptor sd = new SettingsDescriptor();
	    setSettingsDescriptor(sd);
	      
	  
	  /* Stochastic Momentum Index */
	    
	    SettingTab tab = new SettingTab("SMI");
	    sd.addTab(tab);
	    
	    SettingGroup inputs = new SettingGroup("SMI");
	    
	    inputs = new SettingGroup(get("LBL_INPUTS"));
	    
		inputs.addRow(new MAMethodDescriptor(SMI_METHOD, get("LBL_METHOD"), Enums.MAMethod.EMA),
				      new InputDescriptor(SMI_INPUT, get("LBL_INPUT"), Enums.BarInput.CLOSE));

		//inputs.addRow(new IntegerDescriptor(SMI_SIGNAL, "Signal Period", 1, 1, 300, 1),
		inputs.addRow(new IntegerDescriptor(SMI_SMOOTH, "Smooth Period", 2, 1, 300, 1));
	   
		tab.addGroup(inputs);
	    
	    inputs = new SettingGroup("Inputs");
	    
	    inputs.addRow(new IntegerDescriptor(SMI_HL1, "H/L Period", 3, 1, 300, 1),
					  new IntegerDescriptor(SMI_HL_INC, "Increment by", 1, 1, 300, 1));
		inputs.addRow(new IntegerDescriptor(SMI_MA1, "MA Period", 5, 1, 300, 1),
				  	  new IntegerDescriptor(SMI_MA_INC, "Increment by", 2, 1, 300, 1));
		tab.addGroup(inputs);
		
		SettingGroup colors4 = new SettingGroup("FAST SMI Line Colors");	
		colors4.addRow(new PathDescriptor(SMI_LINE1, "Line 1", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE2, "Line 2", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE3, "Line 3", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE4, "Line 4", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE5, "Line 5", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE6, "Line 6", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE7, "Line 7", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE8, "Line 8", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE9, "Line 9", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE10, "Line 10", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE11, "Line 11", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE12, "Line 12", null, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE13, "Line 13", null, 1.0f, null, true, true, true));
		tab.addGroup(colors4);
		
		/*  SMI MTF Input Tab   */  /* Stochastic Momentum Index */

	    tab = new SettingTab("MTF");
	    sd.addTab(tab);
	    
		inputs = new SettingGroup("Inputs");
	    
		inputs.addRow(new IntegerDescriptor(MTF_MULTIPLIER, "Timeframe Multiplier", 12, 1, 50, 1));
		
		inputs.addRow(new IntegerDescriptor(SMI_HL1_MTF, "H/L Period", 14, 1, 300, 1),
					  new IntegerDescriptor(SMI_HL_MTF_INC, "Increment by", 1, 1, 300, 1));
		inputs.addRow(new IntegerDescriptor(SMI_MA1_MTF, "MA Period", 27, 1, 300, 1),
				  	  new IntegerDescriptor(SMI_MA_MTF_INC, "Increment by", 2, 1, 300, 1));
		tab.addGroup(inputs);
		
		SettingGroup colors3 = new SettingGroup("MTF SMI Line Colors");	
		colors3.addRow(new PathDescriptor(SMI_LINE1_MTF, "Line 1", null, 1.0f, null, true, true, true));
		colors3.addRow(new PathDescriptor(SMI_LINE2_MTF, "Line 2", null, 1.0f, null, true, true, true));
		colors3.addRow(new PathDescriptor(SMI_LINE3_MTF, "Line 3", null, 1.0f, null, true, true, true));
		colors3.addRow(new PathDescriptor(SMI_LINE4_MTF, "Line 4", null, 1.0f, null, true, true, true));

		tab.addGroup(colors3);

    
    
    
    
    
    
    
    // Draw the indicator on the chart
    
    RuntimeDescriptor desc = new RuntimeDescriptor();
    desc.setLabelSettings(SMI_HL1, SMI_MA1, SMI_SMOOTH);
    desc.exportValue(new ValueDescriptor(SMIValuesMTF1.SMI, "SMI", new String[] {SMI_HL1, SMI_MA1, SMI_SMOOTH}));
    desc.declarePath(SMIValuesMTF1.SMI, SMI_LINE1);
    desc.declareIndicator(SMIValuesMTF1.SMI, SMI_LINE1);
   
    desc.setFixedTopValue(100);
    desc.setFixedBottomValue(-100);
    desc.setMinTick(0.1);
    
    setRuntimeDescriptor(desc);
    
  }

  @Override  
  protected void calculate(int index, DataContext ctx)
  {
	    	DataSeries series = ctx.getDataSeries(); 	
	//////////////  
	  		int hlPeriod = getSettings().getInteger(SMI_HL1);
	    	int maPeriod = getSettings().getInteger(SMI_MA1);
	  		Enums.MAMethod method = getSettings().getMAMethod(SMI_METHOD);
	  		int smoothPeriod= getSettings().getInteger(SMI_SMOOTH);
	  		
	  		if (index < hlPeriod) return;

	    	double HH = series.highest(index, hlPeriod, Enums.BarInput.HIGH);
	    	double LL = series.lowest(index, hlPeriod, Enums.BarInput.LOW);
	    	double M = (HH + LL)/2.0;
	    	double D = series.getClose(index) - M;
	    
	    	series.setDouble(index, SMIValuesMTF1.D, D);
	    	series.setDouble(index, SMIValuesMTF1.HL, HH - LL);
	    	
	    	if (index < hlPeriod + maPeriod) return;
	    
	    	series.setDouble(index, SMIValuesMTF1.D_MA, series.ma(method, index, maPeriod, SMIValuesMTF1.D));
	    	series.setDouble(index, SMIValuesMTF1.HL_MA, series.ma(method, index, maPeriod, SMIValuesMTF1.HL));
	    	
	        if (index < hlPeriod + maPeriod + smoothPeriod) return;
	    
	        Double D_SMOOTH = series.ma(method, index, smoothPeriod, SMIValuesMTF1.D_MA);
	        Double HL_SMOOTH = series.ma(method, index, smoothPeriod, SMIValuesMTF1.HL_MA);
	    
	        if (D_SMOOTH == null || HL_SMOOTH == null) return;
	        double HL2 = HL_SMOOTH/2;
	        double SMI = 0;
	        if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);

	        series.setDouble(index, SMIValuesMTF1.SMI, SMI);
	////////////////    
	        if (!series.isBarComplete(index)) return;
	
   
    series.setComplete(index); 
  }

 
  
  
}