package study_examples;

import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Defaults;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Inputs;
import com.motivewave.platform.sdk.common.desc.GuideDescriptor;
import com.motivewave.platform.sdk.common.desc.IndicatorDescriptor;
import com.motivewave.platform.sdk.common.desc.InputDescriptor;
import com.motivewave.platform.sdk.common.desc.IntegerDescriptor;
import com.motivewave.platform.sdk.common.desc.MAMethodDescriptor;
import com.motivewave.platform.sdk.common.desc.PathDescriptor;
import com.motivewave.platform.sdk.common.desc.SettingGroup;
import com.motivewave.platform.sdk.common.desc.SettingTab;
import com.motivewave.platform.sdk.common.desc.SettingsDescriptor;
import com.motivewave.platform.sdk.common.desc.ValueDescriptor;
import com.motivewave.platform.sdk.study.RuntimeDescriptor;
import com.motivewave.platform.sdk.study.StudyHeader;
import com.motivewave.platform.sdk.common.X11Colors;

/** Stochastic Momentum Index */
@StudyHeader(
    namespace="com.motivewave", 
    id="vrSMItester", 
    rb="",
    name="vrSMI",
    label="SMI",
    desc="VanRip SMI",
    menu="W. VanRip",
    overlay=false,
    studyOverlay=true,
    signals=false,
    helpLink="http://www.motivewave.com/studies/stochastic_momentum_index.htm")

public class vrSMI extends com.motivewave.platform.sdk.study.Study 
{
	enum SMIVal { SMI1, SMI2, SMI3, SMI4, SMI5, SMI6, SMI7, SMI8, SMI9, SMI10, SMI11, SMI12, SMI13};
	enum SMIValD {D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13};
	enum SMIValHL {HL1, HL2, HL3, HL4, HL5, HL6, HL7, HL8, HL9, HL10, HL11, HL12, HL13};
	enum SMIValD_MA{D_MA1, D_MA2, D_MA3, D_MA4, D_MA5, D_MA6, D_MA7, D_MA8, D_MA9, D_MA10, D_MA11, D_MA12, D_MA13};
	enum SMIValHL_MA {HL_MA1, HL_MA2, HL_MA3, HL_MA4, HL_MA5, HL_MA6, HL_MA7, HL_MA8, HL_MA9, HL_MA10, HL_MA11, HL_MA12, HL_MA13};
	
	enum SMIValmtf {mSMI1, mSMI2, mSMI3, mSMI4,mSMI5, mSMI6, mSMI7, mSMI8, mSMI9, mSMI10, mSMI11, mSMI12};
	enum SMIValDmtf {mD1,  mD2, mD3, mD4,mD5, mD6, mD7, mD8, mD9, mD10, mD11, mD12};
	enum SMIValHLmtf {mHL1, mHL2, mHL3, mHL4,mHL5, mHL6, mHL7, mHL8, mHL9, mHL10, mHL11, mHL12};
	enum SMIValD_MAmtf {mD_MA1, mD_MA2, mD_MA3, mD_MA4,mD_MA5, mD_MA6, mD_MA7, mD_MA8, mD_MA9, mD_MA10, mD_MA11, mD_MA12};
	enum SMIValHL_MAmtf{mHL_MA1,  mHL_MA2, mHL_MA3, mHL_MA4,mHL_MA5, mHL_MA6, mHL_MA7, mHL_MA8, mHL_MA9, mHL_MA10, mHL_MA11, mHL_MA12};
	
	enum Values {MA};
	
	  /* *** strings for SMI *** */
	final static String TEST = "mtfTEST"; 
	  
	  final static String MTF_MULTIPLIER = "mtfMult"; //look for 12x the short timeframe
	  final static String MTF_BARSIZE = "barSizeNew";
	  final static String MTF_BARSIZE2 = "barSizeNew2";
	  
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
	  final static String SMI_HL1_MTF = "smiHL1mtf"; //SMI MTF settings
	  final static String SMI_MA1_MTF = "smiMA1mtf";
	  
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
	  final static String SMI_LINE5_MTF = "smiL5mtf";
	  final static String SMI_LINE6_MTF = "smiL6mtf";
	  final static String SMI_LINE7_MTF = "smiL7mtf";
	  final static String SMI_LINE8_MTF = "smiL8mtf";
	  final static String SMI_LINE9_MTF = "smiL9mtf";
	  final static String SMI_LINE10_MTF = "smiL10mtf";
	  final static String SMI_LINE11_MTF = "smiL11mtf";
	  final static String SMI_LINE12_MTF = "smiL12mtf";
	  
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
	    
	    inputs = new SettingGroup("SMI Constants");
	    
		inputs.addRow(new MAMethodDescriptor(SMI_METHOD, "Method Input", Enums.MAMethod.EMA),
				      new InputDescriptor(SMI_INPUT, "Fast SMI Inputs", Enums.BarInput.CLOSE));

		//inputs.addRow(new IntegerDescriptor(SMI_SIGNAL, "Signal Period", 1, 1, 300, 1),
		inputs.addRow(new IntegerDescriptor(SMI_SMOOTH, "Smooth Period", 2, 1, 300, 1));
	   
		tab.addGroup(inputs);
	    
	    inputs = new SettingGroup("Inputs");
	    
	    inputs.addRow(new IntegerDescriptor(SMI_HL1, "H/L Period", 3, 1, 300, 1),
					  new IntegerDescriptor(SMI_HL_INC, "Increment by", 1, 1, 300, 1));
		inputs.addRow(new IntegerDescriptor(SMI_MA1, "MA Period", 5, 1, 300, 1),
				  	  new IntegerDescriptor(SMI_MA_INC, "Increment by", 2, 1, 300, 1));
		inputs.addRow(new IndicatorDescriptor(Inputs.IND, get("LBL_INDICATOR"), null, null, false, true, true));
		tab.addGroup(inputs);
		
		SettingGroup colors4 = new SettingGroup("FAST SMI Line Colors");	
		colors4.addRow(new PathDescriptor(SMI_LINE1, "Line 1", X11Colors.IVORY, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE2, "Line 2", X11Colors.ORANGE, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE3, "Line 3", X11Colors.DARK_ORANGE, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE4, "Line 4", X11Colors.RED, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE5, "Line 5", X11Colors.DARK_RED, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE6, "Line 6", X11Colors.CYAN, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE7, "Line 7", X11Colors.DODGER_BLUE, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE8, "Line 8", X11Colors.BLUE, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE9, "Line 9", X11Colors.LIME, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE10, "Line 10", X11Colors.GOLD, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE11, "Line 11", X11Colors.YELLOW, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE12, "Line 12", X11Colors.DARK_GRAY, 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE13, "Line 13", X11Colors.DARK_GRAY, 1.0f, null, true, true, true));
		tab.addGroup(colors4);
		
		//////    Guidelines for SMI Chart       //////
		
	    SettingGroup guides = new SettingGroup("Guides");
	    GuideDescriptor mg1 = new GuideDescriptor(Inputs.TOP_GUIDE, "Top Guide", 50, -100, 100, 1, true);
	    mg1.setLineColor(X11Colors.GREEN);
	    mg1.setDash(new float[] {3, 3});
	    guides.addRow(mg1);
	    
	    GuideDescriptor mg2 = new GuideDescriptor(Inputs.MIDDLE_GUIDE, "Middle Guide", 0, -100, 100, 1, true);
	    mg2.setLineColor(X11Colors.IVORY);
	    mg2.setDash(new float[] {3, 3});
	    guides.addRow(mg2);
	    
	    GuideDescriptor mg3 = new GuideDescriptor(Inputs.BOTTOM_GUIDE, "Bottom Guide", -50, -100, 100, 1, true);
	    mg3.setLineColor(X11Colors.RED);
	    mg3.setDash(new float[] {3, 3});
	    guides.addRow(mg3);
	    tab.addGroup(guides);
	    
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
		
		
		SettingGroup mtfcolor = new SettingGroup("MTF SMI Line Colors");	

		mtfcolor.addRow(new PathDescriptor(SMI_LINE1_MTF, "Line 1", X11Colors.BLUE, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE2_MTF, "Line 2", X11Colors.BLUE, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE3_MTF, "Line 3", X11Colors.BLUE, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE4_MTF, "Line 4", X11Colors.LIME, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE5_MTF, "Line 5", X11Colors.LIME, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE6_MTF, "Line 6", X11Colors.LIME, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE7_MTF, "Line 7", X11Colors.GOLD, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE8_MTF, "Line 8", X11Colors.GOLD, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE9_MTF, "Line 9", X11Colors.GOLD, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE10_MTF, "Line 10", X11Colors.DARK_GRAY, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE11_MTF, "Line 11", X11Colors.DARK_GRAY, 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE12_MTF, "Line 12", X11Colors.DARK_GRAY, 1.0f, new float[]{4,4}, true, true, true));
		
		tab.addGroup(mtfcolor);
    
    // Draw the indicator on the chart
		 RuntimeDescriptor desc = new RuntimeDescriptor();
		
		desc.getPricePlot().setLabelSettings(MTF_MULTIPLIER, MTF_BARSIZE);
		desc.getPricePlot().setLabelPrefix("SMI");
		
	//SMI Plots	
	desc.setLabelPrefix("SMI");
    desc.setLabelSettings(SMI_HL1, SMI_MA1, SMI_SMOOTH);
    desc.exportValue(new ValueDescriptor(SMIVal.SMI1, "SMI", new String[] {SMI_HL1, SMI_MA1, SMI_SMOOTH}));
    desc.declarePath(SMIVal.SMI1, SMI_LINE1); 
    desc.declarePath(SMIVal.SMI2, SMI_LINE2); 
    desc.declarePath(SMIVal.SMI3, SMI_LINE3); 
    desc.declarePath(SMIVal.SMI4, SMI_LINE4); 
    desc.declarePath(SMIVal.SMI5, SMI_LINE5); 
    desc.declarePath(SMIVal.SMI6, SMI_LINE6); 
    desc.declarePath(SMIVal.SMI7, SMI_LINE7); 
    desc.declarePath(SMIVal.SMI8, SMI_LINE8); 
    desc.declarePath(SMIVal.SMI9, SMI_LINE9); 
    desc.declarePath(SMIVal.SMI10, SMI_LINE10); 
    desc.declarePath(SMIVal.SMI11, SMI_LINE11); 
    desc.declarePath(SMIVal.SMI12, SMI_LINE12); 
    desc.declarePath(SMIVal.SMI13, SMI_LINE13); 
  
    desc.declarePath(SMIValmtf.mSMI1, SMI_LINE1_MTF); 
    desc.declarePath(SMIValmtf.mSMI2, SMI_LINE2_MTF); 
    desc.declarePath(SMIValmtf.mSMI3, SMI_LINE3_MTF); 
    desc.declarePath(SMIValmtf.mSMI4, SMI_LINE4_MTF);  
    desc.declarePath(SMIValmtf.mSMI5, SMI_LINE5_MTF); 
    desc.declarePath(SMIValmtf.mSMI6, SMI_LINE6_MTF); 
    desc.declarePath(SMIValmtf.mSMI7, SMI_LINE7_MTF); 
    desc.declarePath(SMIValmtf.mSMI8, SMI_LINE8_MTF);
    desc.declarePath(SMIValmtf.mSMI9, SMI_LINE9_MTF); 
    desc.declarePath(SMIValmtf.mSMI10, SMI_LINE10_MTF); 
    desc.declarePath(SMIValmtf.mSMI11, SMI_LINE11_MTF); 
    desc.declarePath(SMIValmtf.mSMI12, SMI_LINE12_MTF);
    desc.declareIndicator(SMIVal.SMI1, Inputs.IND);
   
    desc.setFixedTopValue(100);
    desc.setFixedBottomValue(-100);
    desc.setMinTick(0.1);
    
    setRuntimeDescriptor(desc);
  }
  
  @Override
  public int getMinBars()
  {
   //return 1000 bars
    int bars = 1000;
    return bars;
  }
  
  
  
  @Override  
  protected void calculate(int index, DataContext ctx)
  {
	    	DataSeries series = ctx.getDataSeries(); 
	        ////////////////////////////////////////////////////////////////////////////////////////////////
	        //////////          CALCULATION SMI                                                        /////////
	        ////////////////////////////////////////////////////////////////////////////////////////////////
	    	
	    	final String[] SMIenum = {"SMI1", "SMI2", "SMI3", "SMI4", "SMI5", "SMI6", "SMI7", "SMI8", "SMI9", "SMI10", "SMI11", "SMI12", "SMI13" };
	    	final String[] SMIenumD = {"D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11", "D12", "D13" };
	    	final String[] SMIenumHL = {"HL1", "HL2", "HL3", "HL4", "HL5", "HL6", "HL7", "HL8", "HL9", "HL10", "HL11", "HL12", "HL13" };
	    	final String[] SMIenumD_MA = {"D_MA1", "D_MA2", "D_MA3", "D_MA4", "D_MA5", "D_MA6", "D_MA7", "D_MA8", "D_MA9", "D_MA10", "D_MA11", "D_MA12", "D_MA13" };
	        final String[] SMIenumHL_MA = {"HL_MA1", "HL_MA2", "HL_MA3", "HL_MA4", "HL_MA5", "HL_MA6", "HL_MA7", "HL_MA8", "HL_MA9", "HL_MA10", "HL_MA11", "HL_MA12", "HL_MA13" };
   
	        for(int i = 0; i <= 12; i++)  // i + 1-> Loop 13 times
	    	{
	        	SMIVal SMIline = SMIVal.valueOf(SMIenum[i]);
	        	SMIValD SMIlineD = SMIValD.valueOf(SMIenumD[i]);
	        	SMIValHL SMIlineHL = SMIValHL.valueOf(SMIenumHL[i]);
	        	SMIValD_MA SMIlineD_MA = SMIValD_MA.valueOf(SMIenumD_MA[i]);
	        	SMIValHL_MA SMIlineHL_MA = SMIValHL_MA.valueOf(SMIenumHL_MA[i]);
	        	
	        	int hlPeriod = getSettings().getInteger(SMI_HL1);
		    	int maPeriod = getSettings().getInteger(SMI_MA1);
		  		Enums.MAMethod method = getSettings().getMAMethod(SMI_METHOD);
		  		int smoothPeriod= getSettings().getInteger(SMI_SMOOTH);
		  		
		  		int hlP_incr = getSettings().getInteger(SMI_HL_INC);
		  		int maP_incr = getSettings().getInteger(SMI_MA_INC);
		  		
		  		//increment hlPeriod and maPeriod
		  		hlPeriod = hlPeriod + (i * hlP_incr);
		  		maPeriod = maPeriod + (i * maP_incr);
		  		
		  		if (index < hlPeriod) return;

		  	    double HH = series.highest(index, hlPeriod, Enums.BarInput.HIGH);
		  	    double LL = series.lowest(index, hlPeriod, Enums.BarInput.LOW);
		  	    double M = (HH + LL)/2.0;
		  	    double D = series.getClose(index) - M;
		  	    
		  	    series.setDouble(index, SMIlineD, D);
		  	    series.setDouble(index, SMIlineHL, HH - LL);
		  	    
		  	    if (index < hlPeriod + maPeriod) return;

		  	    series.setDouble(index, SMIlineD_MA, series.ma(method, index, maPeriod, SMIlineD));
		  	    series.setDouble(index, SMIlineHL_MA, series.ma(method, index, maPeriod, SMIlineHL));
		  	    
		  	    if (index < hlPeriod + maPeriod + smoothPeriod) return;
		  	    
		  	    Double D_SMOOTH = series.ma(method, index, smoothPeriod, SMIlineD_MA);
		  	    Double HL_SMOOTH = series.ma(method, index, smoothPeriod, SMIlineHL_MA);
		  	    
		  	    if (D_SMOOTH == null || HL_SMOOTH == null) return;
		  	    double HL2 = HL_SMOOTH/2;
		  	    double SMI = 0;
		  	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);

		  	    series.setDouble(index, SMIline, SMI);
		        
	    	}
	        
	    	final String[] SMIenummtf = {"mSMI1", "mSMI2", "mSMI3", "mSMI4", "mSMI5", "mSMI6", "mSMI7", "mSMI8", "mSMI9", "mSMI10", "mSMI11", "mSMI12", "mSMI13" };
	    	final String[] SMIenumDmtf = {"mD1", "mD2", "mD3", "mD4", "mD5", "mD6", "mD7", "mD8", "mD9", "mD10", "mD11", "mD12", "mD13" };
	    	final String[] SMIenumHLmtf = {"mHL1", "mHL2", "mHL3", "mHL4", "mHL5", "mHL6", "mHL7", "mHL8", "mHL9", "mHL10", "mHL11", "mHL12", "mHL13" };
	    	final String[] SMIenumD_MAmtf = {"mD_MA1", "mD_MA2", "mD_MA3", "mD_MA4", "mD_MA5", "mD_MA6", "mD_MA7", "mD_MA8", "mD_MA9", "mD_MA10", "mD_MA11", "mD_MA12", "mD_MA13" };
	        final String[] SMIenumHL_MAmtf = {"mHL_MA1", "mHL_MA2", "mHL_MA3", "mHL_MA4", "mHL_MA5", "mHL_MA6", "mHL_MA7", "mHL_MA8", "mHL_MA9", "mHL_MA10", "mHL_MA11", "mHL_MA12", "mHL_MA13" };
   
	        for(int i = 0; i <= 11; i++)  // i + 1-> Loop 12 times
	    	{
	        	SMIValmtf SMIline = SMIValmtf.valueOf(SMIenummtf[i]);
	        	SMIValDmtf SMIlineD = SMIValDmtf.valueOf(SMIenumDmtf[i]);
	        	SMIValHLmtf SMIlineHL = SMIValHLmtf.valueOf(SMIenumHLmtf[i]);
	        	SMIValD_MAmtf SMIlineD_MA = SMIValD_MAmtf.valueOf(SMIenumD_MAmtf[i]);
	        	SMIValHL_MAmtf SMIlineHL_MA = SMIValHL_MAmtf.valueOf(SMIenumHL_MAmtf[i]);
	        	
	        	int hlPeriod = getSettings().getInteger(SMI_HL1_MTF);
		    	int maPeriod = getSettings().getInteger(SMI_MA1_MTF);
		  		Enums.MAMethod method = getSettings().getMAMethod(SMI_METHOD);
		  		int smoothPeriod= getSettings().getInteger(SMI_SMOOTH);
		  		int multiplier = getSettings().getInteger(MTF_MULTIPLIER);
		  		
		  		int hlP_incr = getSettings().getInteger(SMI_HL_MTF_INC);
		  		int maP_incr = getSettings().getInteger(SMI_MA_MTF_INC);
		  		
		  		//increment hlPeriod and maPeriod
		  		hlPeriod = (hlPeriod * multiplier) + (i * hlP_incr * multiplier);
		  		maPeriod = (maPeriod * multiplier) + (i * maP_incr * multiplier);
		  		
		  	    if (index < hlPeriod) return;

		  	    double HH = series.highest(index, hlPeriod, Enums.BarInput.HIGH);
		  	    double LL = series.lowest(index, hlPeriod, Enums.BarInput.LOW);
		  	    double M = (HH + LL)/2.0;
		  	    double D = series.getClose(index) - M;
		  	    
		  	    series.setDouble(index, SMIlineD, D);
		  	    series.setDouble(index, SMIlineHL, HH - LL);
		  	    
		  	    if (index < hlPeriod + maPeriod) return;

		  	    series.setDouble(index, SMIlineD_MA, series.ma(method, index, maPeriod, SMIlineD));
		  	    series.setDouble(index, SMIlineHL_MA, series.ma(method, index, maPeriod, SMIlineHL));
		  	    
		  	    if (index < hlPeriod + maPeriod + smoothPeriod) return;
		  	    
		  	    Double D_SMOOTH = series.ma(method, index, smoothPeriod, SMIlineD_MA);
		  	    Double HL_SMOOTH = series.ma(method, index, smoothPeriod, SMIlineHL_MA);
		  	    
		  	    if (D_SMOOTH == null || HL_SMOOTH == null) return;
		  	    double HL2 = HL_SMOOTH/2;
		  	    double SMI = 0;
		  	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);

		  	    series.setDouble(index, SMIline, SMI);

	    	} 
	        
      
	        
	        if (!series.isBarComplete(index)) return;
	
   
    series.setComplete(index); 
  }

 
  
  
}
