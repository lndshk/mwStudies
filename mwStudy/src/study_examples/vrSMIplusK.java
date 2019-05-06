package study_examples;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.motivewave.platform.sdk.common.BarSize;
import com.motivewave.platform.sdk.common.Coordinate;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Defaults;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Inputs;
import com.motivewave.platform.sdk.common.MarkerInfo;
import com.motivewave.platform.sdk.common.Util;
import com.motivewave.platform.sdk.common.desc.GuideDescriptor;
import com.motivewave.platform.sdk.common.desc.IndicatorDescriptor;
import com.motivewave.platform.sdk.common.desc.ColorDescriptor;
import com.motivewave.platform.sdk.common.desc.DoubleDescriptor;
import com.motivewave.platform.sdk.common.desc.InputDescriptor;
import com.motivewave.platform.sdk.common.desc.IntegerDescriptor;
import com.motivewave.platform.sdk.common.desc.MAMethodDescriptor;
import com.motivewave.platform.sdk.common.desc.MarkerDescriptor;
import com.motivewave.platform.sdk.common.desc.PathDescriptor;
import com.motivewave.platform.sdk.common.desc.SettingDescriptor;
import com.motivewave.platform.sdk.common.desc.SettingGroup;
import com.motivewave.platform.sdk.common.desc.SettingTab;
import com.motivewave.platform.sdk.common.desc.SettingsDescriptor;
import com.motivewave.platform.sdk.common.desc.ShadeDescriptor;
import com.motivewave.platform.sdk.common.desc.ValueDescriptor;
import com.motivewave.platform.sdk.draw.Marker;
import com.motivewave.platform.sdk.study.Plot;
import com.motivewave.platform.sdk.study.RuntimeDescriptor;
import com.motivewave.platform.sdk.study.StudyHeader;

import study_examples.CompositeSample.Values;
import study_examples.VanRip.BBValues;
import study_examples.VanRip.MAValues;
import study_examples.vrKamaTesting.FastKamaValues;
import study_examples.vrKamaTesting.SlowKamaValues;


/** Combines a KAMA, BBands SMI and RSI into one study. */
/** All indicators are modified specifically to work together */


@StudyHeader(
    namespace="com.Rsquared", 
    id="vrSMI & KAMA", 
    rb="study_examples.nls.strings", // locale specific strings are loaded from here
    name="vrSMI + KAMA",
    desc="This study has the regular and higher timeframe SMI. Additionally the KAMA indicator was added for signals.",
    menu="W. VanRip",
    overlay= false,
    signals = true,
    underlayByDefault = true,
    requiresBarUpdates = true) 

public class vrSMIplusK extends com.motivewave.platform.sdk.study.Study 


{
	  enum Counter{LOOP};
	  
	  enum SMAVal { MA };
	  enum BBValues { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};
	  enum BBValuesv2 { BB1Uv2, BB2Uv2, BB3Uv2, BB4Uv2, BB1Lv2, BB2Lv2, BB3Lv2, BB4Lv2, MIDv2};
	  
	  enum SMIVal { SMI1, SMI2, SMI3, SMI4, SMI5, SMI6, SMI7, SMI8, SMI9, SMI10,
								SMI11, SMI12, SMI13, SMI14, SMI15, SMI16, SMI17, SMI18, SMI19, SMI20, SMI21, SMI22, SMI23};
	  enum SMIValD {D1, D2, D3, D4, D5, D6, D7, D8, D9, D10,
								D11, D12, D13, D14, D15, D16, D17, D18, D19, D20, D21, D22, D23};
	  enum SMIValHL {HL1, HL2, HL3, HL4, HL5, HL6, HL7, HL8, HL9, HL10, 
								 HL11, HL12, HL13, HL14, HL15, HL16, HL17, HL18, HL19, HL20, HL21, HL22, HL23};
	  enum SMIValD_MA{D_MA1, D_MA2, D_MA3, D_MA4, D_MA5, D_MA6, D_MA7, D_MA8, D_MA9, D_MA10,
	 							D_MA11, D_MA12, D_MA13, D_MA14, D_MA15, D_MA16, D_MA17, D_MA18, D_MA19, D_MA20, D_MA21, D_MA22, D_MA23}; 
	  enum SMIValHL_MA {HL_MA1, HL_MA2, HL_MA3, HL_MA4, HL_MA5, HL_MA6, HL_MA7, HL_MA8, HL_MA9, HL_MA10, 
								HL_MA11, HL_MA12, HL_MA13, HL_MA14, HL_MA15, HL_MA16, HL_MA17, HL_MA18, HL_MA19, HL_MA20, HL_MA21, HL_MA22, HL_MA23}; 
	
	  enum SMIValmtf {mSMI1, mSMI2, mSMI3, mSMI4,mSMI5, mSMI6, mSMI7, mSMI8, mSMI9, mSMI10, mSMI11, mSMI12};
	  enum SMIValDmtf {mD1,  mD2, mD3, mD4,mD5, mD6, mD7, mD8, mD9, mD10, mD11, mD12};
	  enum SMIValHLmtf {mHL1, mHL2, mHL3, mHL4,mHL5, mHL6, mHL7, mHL8, mHL9, mHL10, mHL11, mHL12};
	  enum SMIValD_MAmtf {mD_MA1, mD_MA2, mD_MA3, mD_MA4,mD_MA5, mD_MA6, mD_MA7, mD_MA8, mD_MA9, mD_MA10, mD_MA11, mD_MA12};
	  enum SMIValHL_MAmtf{mHL_MA1,  mHL_MA2, mHL_MA3, mHL_MA4,mHL_MA5, mHL_MA6, mHL_MA7, mHL_MA8, mHL_MA9, mHL_MA10, mHL_MA11, mHL_MA12};
	
      enum FastKamaValues { KAMA1, KAMA2, KAMA3, KAMA4, KAMA5 };
      enum FastKamaShade { TOP, BOTTOM};
      enum SlowKamaValues { mKAMA1, mKAMA2, mKAMA3, mKAMA4, mKAMA5 };
      enum SlowKamaShade { mTOP, mBOTTOM};
      enum KamaDelta {FASTDELTA, SLOWDELTA};
  
      enum KamaPerfect{BULL, BEAR, BULL_SLOW, BEAR_SLOW};
	
	  enum SMISignal{SHALLOW_BULL, SHALLOW_BEAR, HFT_BULL, HFT_BEAR, DEEP_BULL, DEEP_BEAR};
	
	  /* strings for SMA */
	  final static String MA_PERIOD = "smaPeriod";
	  final static String MA_INPUT = "smaInput";
	  final static String MA_METHOD = "smaMethod";
	  final static String MA_PATH = "smaPath";
	  final static String MA_INDICATOR = "smaIndicator";
	  
	  /* strings for BBands */
	  final static String BB_INPUT = "BBInput";
	  final static String BB_METHOD = "BBMethod";
	  final static String BB_PERIOD = "BBPeriod";
	  final static String BB_KAMA_INPUT = "bbKAMAin";
	  final static String BB_KAMA_INPUTv2 = "bbKAMAinv2";
	  
	  final static String BB_STD_1 = "BBstd1";  //Std Devs
	  final static String BB_STD_2 = "BBstd2";
	  final static String BB_STD_3 = "BBstd3";
	  final static String BB_STD_4 = "BBstd4";
	  
	  final static String BB_STD_L1 = "BBStdL1"; //Paths
	  final static String BB_STD_L2 = "BBStdL2";
	  final static String BB_STD_L3 = "BBStdL3";
	  final static String BB_STD_L4 = "BBStdL4";
	  final static String BB_PATH = "BBPath";

	  final static String BB_INPUTv2 = "BBInputv2";
	  final static String BB_METHODv2 = "BBMethodv2";
	  final static String BB_PERIODv2 = "BBPeriodv2";
	  
	  final static String BB_STD_1v2 = "BBstd1v2";  //Std Devs
	  final static String BB_STD_2v2 = "BBstd2v2";
	  final static String BB_STD_3v2 = "BBstd3v2";
	  final static String BB_STD_4v2 = "BBstd4v2";
	  
	  final static String BB_STD_L1v2 = "BBStdL1v2"; //Paths
	  final static String BB_STD_L2v2 = "BBStdL2v2";
	  final static String BB_STD_L3v2 = "BBStdL3v2";
	  final static String BB_STD_L4v2 = "BBStdL4v2";
	  final static String BB_PATHv2 = "BBPathv2";
	  
	  
	  
	  /* *** strings for SMI *** */
	
	  final static String MTF_MULTIPLIER = "mtfMult"; //look for 12x the short timeframe
	  final static String MTF_BARSIZE = "barSizeNew";
	  final static String MTF_BARSIZE2 = "barSizeNew2";
	  final static String SMI_PLOT = "SMIPlot";
	  
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
	  
	  final static String SMI_LINE1 = "smiL1";  //3/5 - SMI Line Settings
	  final static String SMI_LINE2 = "smiL2";  //4/7
	  final static String SMI_LINE3 = "smiL3";  //5/9
	  final static String SMI_LINE4 = "smiL4";  //6/11
	  final static String SMI_LINE5 = "smiL5";  //7/13
	  final static String SMI_LINE6 = "smiL6";  //8/15
	  final static String SMI_LINE7 = "smiL7";  //9/17
	  final static String SMI_LINE8 = "smiL8";  //10/19
	  final static String SMI_LINE9 = "smiL9";  //11/21
	  final static String SMI_LINE10= "smiL10"; //12/23
	  final static String SMI_LINE11= "smiL11"; //13/25
	  final static String SMI_LINE12= "smiL12"; //14/27
	  final static String SMI_LINE13= "smiL13"; //15/29
	  final static String SMI_LINE14 = "smiL14"; //16/31
	  final static String SMI_LINE15 = "smiL15"; //17/33
	  final static String SMI_LINE16 = "smiL16"; //18/35
	  final static String SMI_LINE17 = "smiL17"; //19/37
	  final static String SMI_LINE18 = "smiL18"; //20/39
	  final static String SMI_LINE19 = "smiL19"; //21/41
	  final static String SMI_LINE20= "smiL20"; //22/43
	  final static String SMI_LINE21= "smiL21"; //23/45
	  final static String SMI_LINE22= "smiL22"; //24/47
	  final static String SMI_LINE23= "smiL23"; //25/49
	  
	  final static String SMI_LINE1_MTF = "smiL1mtf"; //14/27
	  final static String SMI_LINE2_MTF = "smiL2mtf"; //15/29
	  final static String SMI_LINE3_MTF = "smiL3mtf"; //16/31
	  final static String SMI_LINE4_MTF = "smiL4mtf"; //17/33
	  final static String SMI_LINE5_MTF = "smiL5mtf"; //18/35
	  final static String SMI_LINE6_MTF = "smiL6mtf"; //19/37
	  final static String SMI_LINE7_MTF = "smiL7mtf"; //20/39
	  final static String SMI_LINE8_MTF = "smiL8mtf"; //21/41
	  final static String SMI_LINE9_MTF = "smiL9mtf"; //22/43
	  final static String SMI_LINE10_MTF = "smiL10mtf"; //23/45
	  final static String SMI_LINE11_MTF = "smiL11mtf"; //24/47
      final static String SMI_LINE12_MTF = "smiL12mtf"; //25/49
      
      final static String LTCOLOR = "colortestlt"; //placeholder for SMI color test
      final static String DKCOLOR = "colortestdk"; //placeholder for SMI color test
      final static String LTRDCOLOR = "colortestltrd"; //placeholder for SMI color test
      final static String DKRDCOLOR = "colortestdkrd"; //placeholder for SMI color test
      final static String LTGRCOLOR = "colortestltgr"; //placeholder for SMI color test
      final static String DKGRCOLOR = "colortestdkgr"; //placeholder for SMI color test
      final static String DELTACOLOR = "colortestdelta"; //placeholder for SMI color test
      final static String MAXCOLOR = "colortestmax"; //placeholder for SMI color test
      
      final static String KAMA_INPUT = "kaInput"; //KAMA calculated on close
	  final static String KAMA_FAST_T_FILL = "kaFastTopFill"; //Fill colors
	  final static String KAMA_FAST_B_FILL = "kaFastBotFill"; 
	  final static String KAMA_SLOW_T_FILL = "kaSlowTopFill"; 
	  final static String KAMA_SLOW_B_FILL = "kaSlowBotFill"; 
	  
	  final static String KAMA_FAST_T_INVIS = "kaFastTopInvis"; //Fill colors
	  final static String KAMA_FAST_B_INVIS = "kaFastBotInvis"; 
	  final static String KAMA_SLOW_T_INVIS = "kaSlowTopInvis"; 
	  final static String KAMA_SLOW_B_INVIS = "kaSlowBotInvis"; 
	  
	  final static String KAMA_PERIOD_F1 	= "kaPeriodF1";
	  final static String KAMA_FAST_F1 		= "kaFastF1";
	  final static String KAMA_SLOW_F1 		= "kaSlowF1";
	  final static String KAMA_PATH_F1 		= "kaPathF1";
  
	  final static String KAMA_PERIOD_F2 	= "kaPeriodF2";
	  final static String KAMA_FAST_F2 		= "kaFastF2";
	  final static String KAMA_SLOW_F2 		= "kaSlowF2";
	  final static String KAMA_PATH_F2 		= "kaPathF2";
	  
	  final static String KAMA_PERIOD_F3 	= "kaPeriodF3";
	  final static String KAMA_FAST_F3 		= "kaFastF3";
	  final static String KAMA_SLOW_F3 		= "kaSlowF3";
	  final static String KAMA_PATH_F3 		= "kaPathF3";
	  
	  final static String KAMA_PERIOD_F4 	= "kaPeriodF4";
	  final static String KAMA_FAST_F4 		= "kaFastF4";
	  final static String KAMA_SLOW_F4 		= "kaSlowF4";
	  final static String KAMA_PATH_F4 		= "kaPathF4";
	  
	  final static String KAMA_PERIOD_F5 	= "kaPeriodF5";
	  final static String KAMA_FAST_F5 		= "kaFastF5";
	  final static String KAMA_SLOW_F5 		= "kaSlowF5";
	  final static String KAMA_PATH_F5 		= "kaPathF5";
	  
	  final static String KAMA_PERIOD_S1 	= "kaPeriodS1";
	  final static String KAMA_FAST_S1 		= "kaFastS1";
	  final static String KAMA_SLOW_S1 		= "kaSlowS1";
	  final static String KAMA_PATH_S1 		= "kaPathS1";
  
	  final static String KAMA_PERIOD_S2 	= "kaPeriodS2";
	  final static String KAMA_FAST_S2 		= "kaFastS2";
	  final static String KAMA_SLOW_S2 		= "kaSlowS2";
	  final static String KAMA_PATH_S2 		= "kaPathS2";
	  
	  final static String KAMA_PERIOD_S3 	= "kaPeriodS3";
	  final static String KAMA_FAST_S3 		= "kaFastS3";
	  final static String KAMA_SLOW_S3 		= "kaSlowS3";
	  final static String KAMA_PATH_S3 		= "kaPathS3";
	  
	  final static String KAMA_PERIOD_S4 	= "kaPeriodS4";
	  final static String KAMA_FAST_S4 		= "kaFastS4";
	  final static String KAMA_SLOW_S4 		= "kaSlowS4";
	  final static String KAMA_PATH_S4 		= "kaPathS4";
	  
	  final static String KAMA_PERIOD_S5 	= "kaPeriodS5";
	  final static String KAMA_FAST_S5 		= "kaFastS5";
	  final static String KAMA_SLOW_S5 		= "kaSlowS5";
	  final static String KAMA_PATH_S5 		= "kaPathS5";

	  
	  //Marker Descriptors
	  final static String HFT_BULL_MARKER = "hftBullMark"; 
	  final static String HFT_BEAR_MARKER = "hftBearMark"; 
	  final static String HFT_XOVER = "hftXOVER"; 
	  final static String HFT_DELTA = "hftDELTA";
	  final static String DEEP_BULL_MARKER = "deepBullMark"; 
	  final static String DEEP_BEAR_MARKER = "deepBearMark"; 
	  
	  
  
  @Override
  public void initialize(Defaults defaults)
  {
		SettingsDescriptor sd = new SettingsDescriptor();
	    setSettingsDescriptor(sd);
	    
	  
	    
	    SettingTab tab = new SettingTab("Price Chart");
	    sd.addTab(tab);
	    
	      ////////////////////////////////////////////////////////////////////////////////////////////////
	    //////////          INITIALIZE PRICE CHART INDICATORS                                     /////////
	     ////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    /* Simple Moving Average */
	    SettingGroup inputsMa = new SettingGroup("Moving Average");
	    	
	    inputsMa.addRow(new IntegerDescriptor(MA_PERIOD, "Period", 200, 1, 300, 1),
				   new MAMethodDescriptor(MA_METHOD, "Method", Enums.MAMethod.EMA),
				   new InputDescriptor(MA_INPUT, "Input", Enums.BarInput.CLOSE));

	    inputsMa.addRow(new PathDescriptor(MA_PATH, "Line Color", defaults.getYellow(), 1.5f, null, true, true, true));
	    inputsMa.addRow(new IndicatorDescriptor(MA_INDICATOR,"Line Label", null, null, false, true, true));
	    
	    tab.addGroup(inputsMa); 
	    
	    
	    tab = new SettingTab("Bbands");
	    sd.addTab(tab);
	    
	    //sd.addInvisibleSetting(new IntegerDescriptor(BB_PERIOD, "Period", 50, 1, 300,1));
	    
	    /* Bollinger Bands */
	    SettingGroup inputsBB = new SettingGroup("Fast KAMA Bollinger Band Inputs");
	    inputsBB.addRow(new IntegerDescriptor(BB_PERIOD, "Period ", 100, 1, 300, 1),
	    		        new IntegerDescriptor(BB_KAMA_INPUT, "1 => fastest ", 3, 1, 5, 1),
	    			    new MAMethodDescriptor(BB_METHOD, "Method ", Enums.MAMethod.EMA),
	    			    new InputDescriptor(BB_INPUT, "Input", Enums.BarInput.CLOSE));
	    			   
	    tab.addGroup(inputsBB);
	    
	    SettingGroup inputsbb = new SettingGroup("Standard Deviations");
	    inputsbb.addRow(new DoubleDescriptor(BB_STD_1, "Std Dev 1", 2.0, .1, 10, .1));  //Std Dev
	    inputsbb.addRow(new DoubleDescriptor(BB_STD_2, "Std Dev 2", 2.2, .1, 10, .1));
	    inputsbb.addRow(new DoubleDescriptor(BB_STD_3, "Std Dev 3", 2.4, .1, 10, .1));
	    inputsbb.addRow(new DoubleDescriptor(BB_STD_4, "Std Dev 4", 2.6, .1, 10, .1));
	    tab.addGroup(inputsbb);
	    
	    /* Bollinger Band Colors */
	    SettingGroup colorsbb = new SettingGroup("Line Colors ");
	    //colorsbb.addRow(new PathDescriptor(BB_PATH, "Middle", Color.decode("#ECEFF1"), 1.0f, null, true, true, true));
	    colorsbb.addRow(new PathDescriptor(BB_STD_L1, "Std Dev 1",Color.decode("#ECEFF1"), 1.0f, new float [] {3f,3f}, true, true, true));
	    colorsbb.addRow(new PathDescriptor(BB_STD_L2, "Std Dev 2", Color.decode("#B0BEC5"), 1.0f, new float [] {3f,3f}, true, true, true));
	    colorsbb.addRow(new PathDescriptor(BB_STD_L3, "Std Dev 3", Color.decode("#78909C"), 1.0f, new float [] {3f,3f}, true, true, true));   
	    colorsbb.addRow(new PathDescriptor(BB_STD_L4, "Std Dev 4", Color.decode("#546E7A"), 1.0f, new float [] {3f,3f}, true, true, true));
	    tab.addGroup(colorsbb);    
	  
	    SettingGroup inputsBBv2 = new SettingGroup("Slow KAMA Bollinger Band Inputs");
	    inputsBBv2.addRow(new IntegerDescriptor(BB_PERIODv2, "Period", 200, 1, 500, 1),
	    				new IntegerDescriptor(BB_KAMA_INPUTv2, "1 => fastest ", 5, 1, 5, 1),
	    			   new MAMethodDescriptor(BB_METHODv2, "Method", Enums.MAMethod.EMA),
	    				new InputDescriptor(BB_INPUTv2, "Input", Enums.BarInput.CLOSE));
	    			 
	    tab.addGroup(inputsBBv2);
	    
	    SettingGroup inputsbbv2 = new SettingGroup("Standard Deviations");
	    inputsbbv2.addRow(new DoubleDescriptor(BB_STD_1v2, "Std Dev 1", 6.0, .1, 10, .1));  //Std Dev
	    inputsbbv2.addRow(new DoubleDescriptor(BB_STD_2v2, "Std Dev 2", 6.2, .1, 10, .1));
	    inputsbbv2.addRow(new DoubleDescriptor(BB_STD_3v2, "Std Dev 3", 6.4, .1, 10, .1));
	    inputsbbv2.addRow(new DoubleDescriptor(BB_STD_4v2, "Std Dev 4", 6.6, .1, 10, .1));
	    tab.addGroup(inputsbbv2);
	    
	    /* Bollinger Band Colors */
	    SettingGroup colorsbbv2 = new SettingGroup("Line Colors");
	    //colorsbbv2.addRow(new PathDescriptor(BB_PATHv2, "Middle", Color.decode("#ECEFF1"), 1.0f, null, true, true, true));
	    colorsbbv2.addRow(new PathDescriptor(BB_STD_L1v2, "Std Dev 1", Color.decode("#ECEFF1"), 1.0f, new float [] {3f,3f}, true, true, true));
	    colorsbbv2.addRow(new PathDescriptor(BB_STD_L2v2, "Std Dev 2", Color.decode("#B0BEC5"), 1.0f, new float [] {3f,3f}, true, true, true));
	    colorsbbv2.addRow(new PathDescriptor(BB_STD_L3v2, "Std Dev 3", Color.decode("#78909C"), 1.0f, new float [] {3f,3f}, true, true, true));   
	    colorsbbv2.addRow(new PathDescriptor(BB_STD_L4v2, "Std Dev 4", Color.decode("#546E7A"), 1.0f, new float [] {3f,3f}, true, true, true));
	    tab.addGroup(colorsbbv2);    
	  
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	      ////////////////////////////////////////////////////////////////////////////////////////////////
	    //////////          INITIALIZE SMI                                                /////////
	     ////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    
	    /* Stochastic Momentum Index */
	    
	    tab = new SettingTab("SMI");
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
		
		SettingGroup colors4 = new SettingGroup("LTF SMI Line Colors");	
		colors4.addRow(new PathDescriptor(SMI_LINE1, "Line 1", Color.decode("#ECEFF1"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE2, "Line 2", Color.decode("#CFD8DC"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE3, "Line 3", Color.decode("#B0BEC5"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE4, "Line 4", Color.decode("#B0BEC5"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE5, "Line 5", Color.decode("#90A4AE"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE6, "Line 6", Color.decode("#90A4AE"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE7, "Line 7", Color.decode("#78909C"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE8, "Line 8", Color.decode("#78909C"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE9, "Line 9", Color.decode("#607D8B"), 1.0f, null, true, true, true));
		colors4.addRow(new PathDescriptor(SMI_LINE10, "Line 10", Color.decode("#607D8B"), 1.0f, null, true, true, true));

		tab.addGroup(colors4);
		
    tab = new SettingTab("SMI2");
			sd.addTab(tab);
			SettingGroup colors5 = new SettingGroup("LTF SMI Line Colors");	

			colors5.addRow(new PathDescriptor(SMI_LINE11, "Line 11", Color.decode("#546E7A"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE12, "Line 12", Color.decode("#546E7A"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE13, "Line 13", Color.decode("#455A64"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE14, "Line 14", Color.decode("#455A64"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE15, "Line 15", Color.decode("#37474F"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE16, "Line 16", Color.decode("#37474F"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE17, "Line 17", Color.decode("#37474F"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE18, "Line 18", Color.decode("#37474F"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE19, "Line 19", Color.decode("#263238"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE20, "Line 20", Color.decode("#263238"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE21, "Line 21", Color.decode("#263238"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE22, "Line 22", Color.decode("#263238"), 1.0f, null, true, true, true));
			colors5.addRow(new PathDescriptor(SMI_LINE23, "Line 23", Color.decode("#263238"), 1.0f, null, true, true, true));

			tab.addGroup(colors5);
	    
		/*  SMI MTF Input Tab   */  /* Stochastic Momentum Index */

	    tab = new SettingTab("HTF");
	    sd.addTab(tab);
	    
		inputs = new SettingGroup("Inputs");
		inputs.addRow(new IntegerDescriptor(MTF_MULTIPLIER, "Timeframe Multiplier", 12, 1, 50, 1));
		
		inputs.addRow(new IntegerDescriptor(SMI_HL1_MTF, "H/L Period", 14, 1, 300, 1),
					  new IntegerDescriptor(SMI_HL_MTF_INC, "Increment by", 1, 1, 300, 1));
		inputs.addRow(new IntegerDescriptor(SMI_MA1_MTF, "MA Period", 27, 1, 300, 1),
				  	  new IntegerDescriptor(SMI_MA_MTF_INC, "Increment by", 2, 1, 300, 1));
		tab.addGroup(inputs);
		
		
		SettingGroup mtfcolor = new SettingGroup("MTF SMI Line Colors");
		

		mtfcolor.addRow(new PathDescriptor(SMI_LINE1_MTF, "Line 1", Color.decode("#99C1C2"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE2_MTF, "Line 2", Color.decode("#8AB8B9"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE3_MTF, "Line 3", Color.decode("#7CAFB1"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE4_MTF, "Line 4", Color.decode("#6DA6A8"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE5_MTF, "Line 5", Color.decode("#5F9EA0"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE6_MTF, "Line 6", Color.decode("#579092"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE7_MTF, "Line 7", Color.decode("#4E8283"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE8_MTF, "Line 8", Color.decode("#467375"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE9_MTF, "Line 9", Color.decode("#3D6566"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE10_MTF, "Line 10", Color.decode("#345758"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE11_MTF, "Line 11", Color.decode("#2C4849"), 1.0f, new float[]{4,4}, true, true, true));
		mtfcolor.addRow(new PathDescriptor(SMI_LINE12_MTF, "Line 12", Color.decode("#233A3B"), 1.0f, new float[]{4,4}, true, true, true));
		
		mtfcolor.addRow(new ColorDescriptor(LTRDCOLOR,"Fast Down", Color.decode("#FFCDD2")),
					    new ColorDescriptor(LTCOLOR,"Fast Neutral", Color.decode("#F5F5F5")),
					    new ColorDescriptor(LTGRCOLOR,"Fast Up", Color.decode("#C8E6C9")));
		
		mtfcolor.addRow(new ColorDescriptor(DKRDCOLOR,"Slow Down", Color.decode("#590606")),
			    	    new ColorDescriptor(DKCOLOR,"Slow Neutral", Color.decode("#616161")),
			            new ColorDescriptor(DKGRCOLOR,"Slow Up", Color.decode("#003300")));
		
		mtfcolor.addRow(new DoubleDescriptor(DELTACOLOR, "Color Delta", 3, 0.1, 999, 0.1));
		mtfcolor.addRow(new DoubleDescriptor(MAXCOLOR, "Max Delta", 10, 0.1, 999, 0.1));
		
		tab.addGroup(mtfcolor);
    
       ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////          INITIALIZE FAST KAMA                                                  /////////
     ////////////////////////////////////////////////////////////////////////////////////////////////
	    
    tab = new SettingTab("Fast Kama");
    sd.addTab(tab);

    SettingGroup inputsFK = new SettingGroup("Fast KAMA Wave Inputs");

    inputsFK.addRow(new InputDescriptor(KAMA_INPUT, get("Input"), Enums.BarInput.CLOSE));
    
    inputsFK.addRow(new IntegerDescriptor(KAMA_PERIOD_F1, get("Period"), 10, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_FAST_F1, get("Fast"), 2, 1, 999, 1),
    		      new IntegerDescriptor(KAMA_SLOW_F1, get("Slow"), 30, 1, 999, 1)); 
    inputsFK.addRow(new IntegerDescriptor(KAMA_PERIOD_F2, get("Period"), 10, 1, 999, 1),
			  	  new IntegerDescriptor(KAMA_FAST_F2, get("Fast"), 3, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F2, get("Slow"), 30, 1, 999, 1)); 
    inputsFK.addRow(new IntegerDescriptor(KAMA_PERIOD_F3, get("Period"), 10, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_F3, get("Fast"), 4, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F3, get("Slow"), 30, 1, 999, 1)); 
    inputsFK.addRow(new IntegerDescriptor(KAMA_PERIOD_F4, get("Period"), 10, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_F4, get("Fast"), 5, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F4, get("Slow"), 30, 1, 999, 1)); 
    inputsFK.addRow(new IntegerDescriptor(KAMA_PERIOD_F5, get("Period"), 10, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_F5, get("Fast"), 6, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F5, get("Slow"), 30, 1, 999, 1)); 
    
    tab.addGroup(inputsFK);
    
    SettingGroup colorsFK = new SettingGroup("Fast KAMA Lines");
    colorsFK.addRow(new PathDescriptor(KAMA_PATH_F1, "Line 1", defaults.getOrange(), 0.5f, 
                  null, true, true, true));
    colorsFK.addRow(new PathDescriptor(KAMA_PATH_F2, "Line 2", defaults.getGrey(), 0.5f, 
            	  null, true, true, true));
    colorsFK.addRow(new PathDescriptor(KAMA_PATH_F3, "Line 3", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colorsFK.addRow(new PathDescriptor(KAMA_PATH_F4, "Line 4", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colorsFK.addRow(new PathDescriptor(KAMA_PATH_F5, "Line 5", defaults.getBlue(), 0.5f, 
                  null, true, true, true));
    colorsFK.addRow(new PathDescriptor(KAMA_FAST_T_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    colorsFK.addRow(new PathDescriptor(KAMA_FAST_B_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    
    Color upFillF = new Color(69, 90, 100, 180);
    Color dnFillF = new Color(255, 200, 210, 110);
    colorsFK.addRow(new ShadeDescriptor(KAMA_FAST_T_FILL, "Up Wave", KAMA_FAST_T_INVIS, KAMA_FAST_B_INVIS,
    		Enums.ShadeType.ABOVE, upFillF, true, true));
    colorsFK.addRow(new ShadeDescriptor(KAMA_FAST_B_FILL, "Down Wave", KAMA_FAST_T_INVIS, KAMA_FAST_B_INVIS,
    		Enums.ShadeType.BELOW, dnFillF, true, true));
    tab.addGroup(colorsFK);
    
      ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////          INITIALIZE SLOW KAMA                                                  /////////
     ////////////////////////////////////////////////////////////////////////////////////////////////
    tab = new SettingTab("Slow Kama");
    sd.addTab(tab);

    SettingGroup inputsKS = new SettingGroup("Slow KAMA Wave Inputs");

    //inputs2.addRow(new InputDescriptor(KAMA_INPUT, get("Input"), Enums.BarInput.CLOSE));
    
    inputsKS.addRow(new IntegerDescriptor(KAMA_PERIOD_S1, get("Period"), 20, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_FAST_S1, get("Fast"), 6, 1, 999, 1),
    		      new IntegerDescriptor(KAMA_SLOW_S1, get("Slow"), 30, 1, 999, 1)); 
    inputsKS.addRow(new IntegerDescriptor(KAMA_PERIOD_S2, get("Period"), 20, 1, 999, 1),
			  	  new IntegerDescriptor(KAMA_FAST_S2, get("Fast"), 7, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S2, get("Slow"), 30, 1, 999, 1)); 
    inputsKS.addRow(new IntegerDescriptor(KAMA_PERIOD_S3, get("Period"), 20, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_S3, get("Fast"), 8, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S3, get("Slow"), 30, 1, 999, 1)); 
    inputsKS.addRow(new IntegerDescriptor(KAMA_PERIOD_S4, get("Period"), 20, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_S4, get("Fast"), 9, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S4, get("Slow"), 30, 1, 999, 1)); 
    inputsKS.addRow(new IntegerDescriptor(KAMA_PERIOD_S5, get("Period"), 20, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_S5, get("Fast"), 10, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S5, get("Slow"), 30, 1, 999, 1)); 
    
    tab.addGroup(inputsKS);
    
    SettingGroup colorsKS = new SettingGroup("Slow KAMA Lines");
    colorsKS.addRow(new PathDescriptor(KAMA_PATH_S1, "Line 1", defaults.getOrange(), 0.5f, 
                  null, true, true, true));
    colorsKS.addRow(new PathDescriptor(KAMA_PATH_S2, "Line 2", defaults.getGrey(), 0.5f, 
            	  null, true, true, true));
    colorsKS.addRow(new PathDescriptor(KAMA_PATH_S3, "Line 3", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colorsKS.addRow(new PathDescriptor(KAMA_PATH_S4, "Line 4", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colorsKS.addRow(new PathDescriptor(KAMA_PATH_S5, "Line 5", defaults.getBlue(), 0.5f, 
                  null, true, true, true));
    colorsKS.addRow(new PathDescriptor(KAMA_SLOW_T_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    colorsKS.addRow(new PathDescriptor(KAMA_SLOW_B_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    
    Color upFillS = new Color(69, 90, 100, 100);
    Color dnFillS = new Color(255, 200, 210, 60);
    colorsKS.addRow(new ShadeDescriptor(KAMA_SLOW_T_FILL, "Up Wave", KAMA_SLOW_T_INVIS, KAMA_SLOW_B_INVIS, 
    		Enums.ShadeType.ABOVE, upFillS, true, true));
    colorsKS.addRow(new ShadeDescriptor(KAMA_SLOW_B_FILL, "Down Wave", KAMA_SLOW_T_INVIS, KAMA_SLOW_B_INVIS, 
    		Enums.ShadeType.BELOW, dnFillS, true, true));
    tab.addGroup(colorsKS);

    ////////////////////////////////////////////////////////////////////////////////////////////////
  //////////          INITIALIZE MARKERS                                                    /////////
   ////////////////////////////////////////////////////////////////////////////////////////////////
    
	tab = new SettingTab("Markers");
    sd.addTab(tab);
    
    SettingGroup markers = new SettingGroup("Strategy Markers");
    
   
    markers.addRow(new MarkerDescriptor(Inputs.UP_MARKER, "Shallow Bull Pullback", 
        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getGreen(), defaults.getLineColor(), true, true));
    markers.addRow(new MarkerDescriptor(Inputs.DOWN_MARKER, "Shallow Bear Pullback", 
	        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getRed(), defaults.getLineColor(), true, true));
    
    markers.addRow(new MarkerDescriptor(HFT_BULL_MARKER, "HFT Bull Expansion", 
	        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getPurple(), defaults.getLineColor(), true, true),
    		new IntegerDescriptor(HFT_XOVER, "Bars Since xOver", 30, 1, 999, 1));
	
    markers.addRow(new MarkerDescriptor(HFT_BEAR_MARKER, "HFT Bear Expansion", 
		    Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getPurple(), defaults.getLineColor(), true, true),
    		new DoubleDescriptor(HFT_DELTA, "Delta", 4, 0.1, 999, 0.1));
    
    markers.addRow(new MarkerDescriptor(DEEP_BULL_MARKER, "Deep Bull Pullback", 
            Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
    markers.addRow(new MarkerDescriptor(DEEP_BEAR_MARKER, "DEEP Bear Pullback", 
    	        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
   
    
    tab.addGroup(markers);
   
	//////    Guidelines for SMI Chart       //////
	
    SettingGroup guides = new SettingGroup("SMI Plot Guides");
    GuideDescriptor mg1 = new GuideDescriptor(Inputs.TOP_GUIDE, "Top Guide", 50, -100, 100, 1, true);
    mg1.setLineColor(Color.decode("#233A3B"));
    mg1.setDash(new float[] {3, 3});
    guides.addRow(mg1);
    
    GuideDescriptor mg2 = new GuideDescriptor(Inputs.MIDDLE_GUIDE, "Middle Guide", 0, -100, 100, 1, true);
    mg2.setLineColor(Color.decode("#233A3B"));
    mg2.setDash(new float[] {3, 3});
    guides.addRow(mg2);
    
    GuideDescriptor mg3 = new GuideDescriptor(Inputs.BOTTOM_GUIDE, "Bottom Guide", -50, -100, 100, 1, true);
    mg3.setLineColor(Color.decode("#233A3B"));
    mg3.setDash(new float[] {3, 3});
    guides.addRow(mg3);
    tab.addGroup(guides);


    // Draw the indicator on the chart
		 RuntimeDescriptor desc = new RuntimeDescriptor();
    
	     ////////////////////////////////////////////////////////////////////////////////////////////////////
	     ////// Draw SMA and Bollinger lines
		 
	     desc.getPricePlot().setLabelSettings(MA_INPUT, MA_PERIOD);
	     desc.getPricePlot().setLabelPrefix("MA");
	     desc.getPricePlot().declarePath(SMAVal.MA, MA_PATH);
	     desc.getPricePlot().declareIndicator(SMAVal.MA, MA_INDICATOR);

	     desc.getPricePlot().declarePath(BBValues.BB1L, BB_STD_L1);  
	     desc.getPricePlot().declarePath(BBValues.BB1U, BB_STD_L1);
	     desc.getPricePlot().declarePath(BBValues.BB2L, BB_STD_L2);
	     desc.getPricePlot().declarePath(BBValues.BB2U, BB_STD_L2);
	     desc.getPricePlot().declarePath(BBValues.BB3L, BB_STD_L3);
	     desc.getPricePlot().declarePath(BBValues.BB3U, BB_STD_L3);
	     desc.getPricePlot().declarePath(BBValues.BB4L, BB_STD_L4);
	     desc.getPricePlot().declarePath(BBValues.BB4U, BB_STD_L4);
	     //desc.getPricePlot().declarePath(BBValues.MID,  BB_PATH);
	     
	     desc.getPricePlot().declarePath(BBValuesv2.BB1Lv2, BB_STD_L1v2);  
	     desc.getPricePlot().declarePath(BBValuesv2.BB1Uv2, BB_STD_L1v2);
	     desc.getPricePlot().declarePath(BBValuesv2.BB2Lv2, BB_STD_L2v2);
	     desc.getPricePlot().declarePath(BBValuesv2.BB2Uv2, BB_STD_L2v2);
	     desc.getPricePlot().declarePath(BBValuesv2.BB3Lv2, BB_STD_L3v2);
	     desc.getPricePlot().declarePath(BBValuesv2.BB3Uv2, BB_STD_L3v2);
	     desc.getPricePlot().declarePath(BBValuesv2.BB4Lv2, BB_STD_L4v2);
	     desc.getPricePlot().declarePath(BBValuesv2.BB4Uv2, BB_STD_L4v2);
	     //desc.getPricePlot().declarePath(BBValuesv2.MIDv2,  BB_PATHv2);
		 
	     ////////////////////////////////////////////////////////////////////////////////////////////////////
	     ////// Draw KAMA lines

	     desc.getPricePlot().setLabelSettings(KAMA_INPUT, KAMA_PERIOD_F1, KAMA_PERIOD_S1);
	     desc.getPricePlot().setLabelPrefix("KAMA");
	     desc.getPricePlot().declarePath(FastKamaValues.KAMA1, KAMA_PATH_F1);
	     desc.getPricePlot().declarePath(FastKamaValues.KAMA2, KAMA_PATH_F2); 
	     desc.getPricePlot().declarePath(FastKamaValues.KAMA3, KAMA_PATH_F3); 
	     desc.getPricePlot().declarePath(FastKamaValues.KAMA4, KAMA_PATH_F4); 
	     desc.getPricePlot().declarePath(FastKamaValues.KAMA5, KAMA_PATH_F5); 
	     desc.getPricePlot().declarePath(FastKamaShade.TOP, KAMA_FAST_T_INVIS); 
	     desc.getPricePlot().declarePath(FastKamaShade.BOTTOM, KAMA_FAST_B_INVIS); 
	     
	     
	     desc.getPricePlot().declarePath(SlowKamaValues.mKAMA1, KAMA_PATH_S1);
	     desc.getPricePlot().declarePath(SlowKamaValues.mKAMA2, KAMA_PATH_S2); 
	     desc.getPricePlot().declarePath(SlowKamaValues.mKAMA3, KAMA_PATH_S3); 
	     desc.getPricePlot().declarePath(SlowKamaValues.mKAMA4, KAMA_PATH_S4); 
	     desc.getPricePlot().declarePath(SlowKamaValues.mKAMA5, KAMA_PATH_S5); 
	     desc.getPricePlot().declarePath(SlowKamaShade.mTOP, KAMA_SLOW_T_INVIS); 
	     desc.getPricePlot().declarePath(SlowKamaShade.mBOTTOM, KAMA_SLOW_B_INVIS); 	 
		 
		 
		    // SMI Plot
				
			//SMI Plots	
			desc.setLabelPrefix("SMI");
		    desc.setLabelSettings(SMI_HL1, SMI_MA1, SMI_SMOOTH);
		    
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
			desc.declarePath(SMIVal.SMI14, SMI_LINE14); 
		    desc.declarePath(SMIVal.SMI15, SMI_LINE15); 
		    desc.declarePath(SMIVal.SMI16, SMI_LINE16); 
		    desc.declarePath(SMIVal.SMI17, SMI_LINE17); 
		    desc.declarePath(SMIVal.SMI18, SMI_LINE18); 
		    desc.declarePath(SMIVal.SMI19, SMI_LINE19); 
		    desc.declarePath(SMIVal.SMI20, SMI_LINE20); 
		    desc.declarePath(SMIVal.SMI21, SMI_LINE21); 
		    desc.declarePath(SMIVal.SMI22, SMI_LINE22); 
		    desc.declarePath(SMIVal.SMI23, SMI_LINE23); 
		  
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
		    desc.setRangeKeys(SMIVal.SMI1);
		    desc.setFixedTopValue(80);
		    desc.setFixedBottomValue(-80);
		    desc.setMinTick(0.1);
		    desc.setTopInsetPixels(1);  //default is 5, setting to 1 to gain chart space
		    desc.setBottomInsetPixels(1);
		    
		    desc.exportValue(new ValueDescriptor(FastKamaValues.KAMA1, "KAMA", 
		                      new String[] {KAMA_INPUT, KAMA_PERIOD_F1}));
		
		    //Place Markers
		    desc.declareSignal(SMISignal.SHALLOW_BULL, "Shallow Bull Pullback");
		    desc.declareSignal(SMISignal.SHALLOW_BEAR, "Shallow Bear Pullback");
		    desc.declareSignal(SMISignal.HFT_BULL, "HFT Bull Expansion");
		    desc.declareSignal(SMISignal.HFT_BEAR, "HFT Bear Expansion");
		    desc.declareSignal(SMISignal.DEEP_BULL, "Deep Bull Pullback");
		    desc.declareSignal(SMISignal.DEEP_BEAR, "Deep Bear Pullback");
		    
    setRuntimeDescriptor(desc);
  }   /// END OF INITIALIZING METHOD ///
  
  @Override
  public void onLoad(Defaults defaults)
  {
	  
   setMinBars(3000);
   getSettings().setBarUpdates(true);
   
   debug("ln 760" + getSettings().isBarUpdates());
  }
  
  
  @Override
  public void onBarUpdate(DataContext ctx)
  {
	  if (!getSettings().isBarUpdates()) return;
	  doUpdate(ctx);
  }
  
  
  
  protected void doUpdate(DataContext ctx)
  {
	  DataSeries series = ctx.getDataSeries(); 
	  int myIndex = series.size() -1;  //set new index
       
	  ////////////////////////////////////////////////////////////////////////////////////////////////
      //////////          CALCULATION UPDATE SMI                                                /////////
      ////////////////////////////////////////////////////////////////////////////////////////////////
  	
			final String[] SMIenum = {"SMI1", "SMI2", "SMI3", "SMI4", "SMI5", "SMI6", "SMI7", "SMI8", "SMI9", "SMI10", "SMI11", "SMI12", "SMI13", 
															"SMI14", "SMI15", "SMI16", "SMI17", "SMI18", "SMI19", "SMI20", "SMI21", "SMI22", "SMI23" };
			final String[] SMIenumD = {"D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11", "D12", "D13", 
															"D14", "D15", "D16", "D17", "D18", "D19", "D20", "D21", "D22", "D23" };
			final String[] SMIenumHL = {"HL1", "HL2", "HL3", "HL4", "HL5", "HL6", "HL7", "HL8", "HL9", "HL10", "HL11", "HL12", "HL13",
															"HL14", "HL15", "HL16", "HL17", "HL18", "HL19", "HL20", "HL21", "HL22", "HL23" };
			final String[] SMIenumD_MA = {"D_MA1", "D_MA2", "D_MA3", "D_MA4", "D_MA5", "D_MA6", "D_MA7", "D_MA8", "D_MA9", "D_MA10", "D_MA11", "D_MA12", "D_MA13",
															"D_MA14", "D_MA15", "D_MA16", "D_MA17", "D_MA18", "D_MA19", "D_MA20", "D_MA21", "D_MA22", "D_MA23" }; 
			final String[] SMIenumHL_MA = {"HL_MA1", "HL_MA2", "HL_MA3", "HL_MA4", "HL_MA5", "HL_MA6", "HL_MA7", "HL_MA8", "HL_MA9", "HL_MA10", "HL_MA11", "HL_MA12", "HL_MA13",
															"HL_MA14", "HL_MA15", "HL_MA16", "HL_MA17", "HL_MA18", "HL_MA19", "HL_MA20", "HL_MA21", "HL_MA22", "HL_MA23" };
			
			
			for(int z = (myIndex-2); z <= myIndex; z++) 
			{	
				
				for(int i = 0; i <= 22; i++)  // i + 1-> Loop 23 times
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
		  		
		  		if (z < hlPeriod) continue;
		  	    double HH = series.highest(z, hlPeriod, Enums.BarInput.HIGH);
		  	    double LL = series.lowest(z, hlPeriod, Enums.BarInput.LOW);
		  	    double M = (HH + LL)/2.0;
		  	    double D = series.getClose(z) - M;
		  	    
		  	    series.setDouble(z, SMIlineD, D);
		  	    series.setDouble(z, SMIlineHL, HH - LL);
		  	    
		  	    if (z < hlPeriod + maPeriod) continue;

		  	    series.setDouble(z, SMIlineD_MA, series.ma(method, z, maPeriod, SMIlineD));
		  	    series.setDouble(z, SMIlineHL_MA, series.ma(method, z, maPeriod, SMIlineHL));
		  	    
		  	    if (z < hlPeriod + maPeriod + smoothPeriod) continue;
		  	    
		  	    Double D_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineD_MA);
		  	    Double HL_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineHL_MA);
		  	    
		  	    if (D_SMOOTH == null || HL_SMOOTH == null) continue;
		  	    double HL2 = HL_SMOOTH/2;
		  	    double SMI = 0;
		  	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);
		  	   
		  	    series.setDouble(z, SMIline, SMI);
		        }
			}

  	final String[] SMIenummtf = {"mSMI1", "mSMI2", "mSMI3", "mSMI4", "mSMI5", "mSMI6", "mSMI7", "mSMI8", "mSMI9", "mSMI10", "mSMI11", "mSMI12", "mSMI13" };
  	final String[] SMIenumDmtf = {"mD1", "mD2", "mD3", "mD4", "mD5", "mD6", "mD7", "mD8", "mD9", "mD10", "mD11", "mD12", "mD13" };
  	final String[] SMIenumHLmtf = {"mHL1", "mHL2", "mHL3", "mHL4", "mHL5", "mHL6", "mHL7", "mHL8", "mHL9", "mHL10", "mHL11", "mHL12", "mHL13" };
  	final String[] SMIenumD_MAmtf = {"mD_MA1", "mD_MA2", "mD_MA3", "mD_MA4", "mD_MA5", "mD_MA6", "mD_MA7", "mD_MA8", "mD_MA9", "mD_MA10", "mD_MA11", "mD_MA12", "mD_MA13" };
  	final String[] SMIenumHL_MAmtf = {"mHL_MA1", "mHL_MA2", "mHL_MA3", "mHL_MA4", "mHL_MA5", "mHL_MA6", "mHL_MA7", "mHL_MA8", "mHL_MA9", "mHL_MA10", "mHL_MA11", "mHL_MA12", "mHL_MA13" };
			
		for(int z = (myIndex-2); z <= myIndex; z++) 
		{
			
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
	  		
		  	    if (z < hlPeriod) continue;

		  	    double HH = series.highest(z, hlPeriod, Enums.BarInput.HIGH);
		  	    double LL = series.lowest(z, hlPeriod, Enums.BarInput.LOW);
		  	    double M = (HH + LL)/2.0;
		  	    double D = series.getClose(z) - M;
		  	    
		  	    series.setDouble(z, SMIlineD, D);
		  	    series.setDouble(z, SMIlineHL, HH - LL);
		  	    
		  	    if (z < hlPeriod + maPeriod) continue;

		  	    series.setDouble(z, SMIlineD_MA, series.ma(method, z, maPeriod, SMIlineD));
		  	    series.setDouble(z, SMIlineHL_MA, series.ma(method, z, maPeriod, SMIlineHL));
		  	    
		  	    if (z < hlPeriod + maPeriod + smoothPeriod) continue;
		  	    
		  	    Double D_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineD_MA);
		  	    Double HL_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineHL_MA);
		  	    
		  	    if (D_SMOOTH == null || HL_SMOOTH == null) continue;
		  	    double HL2 = HL_SMOOTH/2;
		  	    double SMI = 0;
		  	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);
		  	    series.setDouble(z, SMIline, SMI);
		  	   
  		}
		}    
	  
	  
  }
  
  
  
  @Override  
  protected void calculateValues(DataContext ctx)
  {
				DataSeries series = ctx.getDataSeries(); 
				int myIndex = series.size() -1;  //set new index
				debug ("Entered Calculate Values Loop " );
				
				debug ("Counter Loop size equals" + series.getInt(0, Counter.LOOP ));

				boolean dataInitialized;
				
				if (series.getInt(0, Counter.LOOP ) == 1) 
				{ dataInitialized = true; }
				else
				{ dataInitialized = false; }
				
								
			
				
		        ////////////////////////////////////////////////////////////////////////////////////////////////
		      //////////          CALCULATION MOVING AVERAGE AND BOLLINGER BANDS                       /////////
		        ////////////////////////////////////////////////////////////////////////////////////////////////
				
				//Util.calcLatestMA(ctx, getSettings().getMAMethod(MA_METHOD), getSettings().getInput(MA_INPUT), getSettings().getInteger(MA_PERIOD), 
				       // 0, SMAVal.MA, true);
				
			    Object inputMA= getSettings().getInput(MA_INPUT);
			    int periodMA = getSettings().getInteger(MA_PERIOD);
			    Enums.MAMethod methodMA = getSettings().getMAMethod(MA_METHOD);
				
			    Object bbinput= getSettings().getInput(BB_INPUT);
			    int bbPeriod = getSettings().getInteger(BB_PERIOD);
			    Enums.MAMethod bbmethod = getSettings().getMAMethod(BB_METHOD, Enums.MAMethod.EMA);
			    double bbStd1 = getSettings().getDouble(BB_STD_1);
			    double bbStd2 = getSettings().getDouble(BB_STD_2);
			    double bbStd3 = getSettings().getDouble(BB_STD_3);
			    double bbStd4 = getSettings().getDouble(BB_STD_4);
				
			    for(int z = 0; z <= myIndex; z++) 
				{	
			        
			    	if ((dataInitialized) && (z < myIndex-10)) continue;
			    	
			    	//Moving average calculation
			    	Double simpleMA = series.ma(methodMA, z, periodMA, inputMA);
			        
			        if (simpleMA == null) continue;
			        
			        series.setDouble(z, SMAVal.MA, simpleMA);
			       
				}
			    
			    
			    //Old BASE BBAND CALCULATION
			    /*
			    for(int z = 0; z <= myIndex; z++) 
					{	
				        	
			        //Bband calculation
			    	
			    	Double bbStdDevs = series.std(z, bbPeriod, FastKamaValues.KAMA5);
				    Double bbStdDevma = series.ma(bbmethod, z, bbPeriod, FastKamaValues.KAMA5);
			    	
				    // Double bbStdDevs = series.std(z, bbPeriod, bbinput);
				    // Double bbStdDevma = series.ma(bbmethod, z, bbPeriod, bbinput);
				   
				    
				    if (bbStdDevs == null || bbStdDevma == null) continue; //{bbStdDevs = .0000001; bbStdDevma = 0.000001;}
	
				    //Band input { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};
				    series.setDouble(z, BBValues.BB1U, bbStdDevma + bbStdDevs * bbStd1);
				    series.setDouble(z, BBValues.BB1L, bbStdDevma - bbStdDevs* bbStd1);
				    series.setDouble(z, BBValues.BB2U, bbStdDevma + bbStdDevs* bbStd2);
				    series.setDouble(z, BBValues.BB2L, bbStdDevma - bbStdDevs* bbStd2);
				    series.setDouble(z, BBValues.BB3U, bbStdDevma + bbStdDevs* bbStd3);
				    series.setDouble(z, BBValues.BB3L, bbStdDevma - bbStdDevs* bbStd3);
				    series.setDouble(z, BBValues.BB4U, bbStdDevma + bbStdDevs* bbStd4);
				    series.setDouble(z, BBValues.BB4L, bbStdDevma - bbStdDevs* bbStd4);
				    series.setDouble(z, BBValues.MID, bbStdDevma);
				}	
				*/
				
	        ////////////////////////////////////////////////////////////////////////////////////////////////
	        //////////          CALCULATION SMI                                                        /////////
	        ////////////////////////////////////////////////////////////////////////////////////////////////
	    	
				final String[] SMIenum = {"SMI1", "SMI2", "SMI3", "SMI4", "SMI5", "SMI6", "SMI7", "SMI8", "SMI9", "SMI10", "SMI11", "SMI12", "SMI13", 
																"SMI14", "SMI15", "SMI16", "SMI17", "SMI18", "SMI19", "SMI20", "SMI21", "SMI22", "SMI23" };
				final String[] SMIenumD = {"D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11", "D12", "D13", 
																"D14", "D15", "D16", "D17", "D18", "D19", "D20", "D21", "D22", "D23" };
				final String[] SMIenumHL = {"HL1", "HL2", "HL3", "HL4", "HL5", "HL6", "HL7", "HL8", "HL9", "HL10", "HL11", "HL12", "HL13",
																"HL14", "HL15", "HL16", "HL17", "HL18", "HL19", "HL20", "HL21", "HL22", "HL23" };
				final String[] SMIenumD_MA = {"D_MA1", "D_MA2", "D_MA3", "D_MA4", "D_MA5", "D_MA6", "D_MA7", "D_MA8", "D_MA9", "D_MA10", "D_MA11", "D_MA12", "D_MA13",
																"D_MA14", "D_MA15", "D_MA16", "D_MA17", "D_MA18", "D_MA19", "D_MA20", "D_MA21", "D_MA22", "D_MA23" }; 
				final String[] SMIenumHL_MA = {"HL_MA1", "HL_MA2", "HL_MA3", "HL_MA4", "HL_MA5", "HL_MA6", "HL_MA7", "HL_MA8", "HL_MA9", "HL_MA10", "HL_MA11", "HL_MA12", "HL_MA13",
																"HL_MA14", "HL_MA15", "HL_MA16", "HL_MA17", "HL_MA18", "HL_MA19", "HL_MA20", "HL_MA21", "HL_MA22", "HL_MA23" };
				
				
				for(int z = 0; z <= myIndex; z++) 
				{	
					if ((dataInitialized) && (z < myIndex-10)) continue;
					
					for(int i = 0; i <= 22; i++)  // i + 1-> Loop 23 times
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
			  		
			  		if (z < hlPeriod) continue;
			  	    double HH = series.highest(z, hlPeriod, Enums.BarInput.HIGH);
			  	    double LL = series.lowest(z, hlPeriod, Enums.BarInput.LOW);
			  	    double M = (HH + LL)/2.0;
			  	    double D = series.getClose(z) - M;
			  	    
			  	    series.setDouble(z, SMIlineD, D);
			  	    series.setDouble(z, SMIlineHL, HH - LL);
			  	    
			  	    if (z < hlPeriod + maPeriod) continue;
	
			  	    series.setDouble(z, SMIlineD_MA, series.ma(method, z, maPeriod, SMIlineD));
			  	    series.setDouble(z, SMIlineHL_MA, series.ma(method, z, maPeriod, SMIlineHL));
			  	    
			  	    if (z < hlPeriod + maPeriod + smoothPeriod) continue;
			  	    
			  	    Double D_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineD_MA);
			  	    Double HL_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineHL_MA);
			  	    
			  	    if (D_SMOOTH == null || HL_SMOOTH == null) continue;
			  	    double HL2 = HL_SMOOTH/2;
			  	    double SMI = 0;
			  	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);
			  	   
			  	    series.setDouble(z, SMIline, SMI);
			        }
				}

	    	final String[] SMIenummtf = {"mSMI1", "mSMI2", "mSMI3", "mSMI4", "mSMI5", "mSMI6", "mSMI7", "mSMI8", "mSMI9", "mSMI10", "mSMI11", "mSMI12", "mSMI13" };
	    	final String[] SMIenumDmtf = {"mD1", "mD2", "mD3", "mD4", "mD5", "mD6", "mD7", "mD8", "mD9", "mD10", "mD11", "mD12", "mD13" };
	    	final String[] SMIenumHLmtf = {"mHL1", "mHL2", "mHL3", "mHL4", "mHL5", "mHL6", "mHL7", "mHL8", "mHL9", "mHL10", "mHL11", "mHL12", "mHL13" };
	    	final String[] SMIenumD_MAmtf = {"mD_MA1", "mD_MA2", "mD_MA3", "mD_MA4", "mD_MA5", "mD_MA6", "mD_MA7", "mD_MA8", "mD_MA9", "mD_MA10", "mD_MA11", "mD_MA12", "mD_MA13" };
	    	final String[] SMIenumHL_MAmtf = {"mHL_MA1", "mHL_MA2", "mHL_MA3", "mHL_MA4", "mHL_MA5", "mHL_MA6", "mHL_MA7", "mHL_MA8", "mHL_MA9", "mHL_MA10", "mHL_MA11", "mHL_MA12", "mHL_MA13" };
				
			for(int z = 0; z <= myIndex; z++) 
			{
				if ((dataInitialized) && (z < myIndex-10)) continue;
				
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
		  		
			  	    if (z < hlPeriod) continue;
	
			  	    double HH = series.highest(z, hlPeriod, Enums.BarInput.HIGH);
			  	    double LL = series.lowest(z, hlPeriod, Enums.BarInput.LOW);
			  	    double M = (HH + LL)/2.0;
			  	    double D = series.getClose(z) - M;
			  	    
			  	    series.setDouble(z, SMIlineD, D);
			  	    series.setDouble(z, SMIlineHL, HH - LL);
			  	    
			  	    if (z < hlPeriod + maPeriod) continue;
	
			  	    series.setDouble(z, SMIlineD_MA, series.ma(method, z, maPeriod, SMIlineD));
			  	    series.setDouble(z, SMIlineHL_MA, series.ma(method, z, maPeriod, SMIlineHL));
			  	    
			  	    if (z < hlPeriod + maPeriod + smoothPeriod) continue;
			  	    
			  	    Double D_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineD_MA);
			  	    Double HL_SMOOTH = series.ma(method, z, smoothPeriod, SMIlineHL_MA);
			  	    
			  	    if (D_SMOOTH == null || HL_SMOOTH == null) continue;
			  	    double HL2 = HL_SMOOTH/2;
			  	    double SMI = 0;
			  	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);
			  	    series.setDouble(z, SMIline, SMI);
			  	   
	    		}
			}    

             ////////////////////////////////////////////////////////////////////////////////////////////////
    			//////////          CALCULATION KAMAS                                                        /////////
    			////////////////////////////////////////////////////////////////////////////////////////////////
	/** 5 FAST KAMA Indicator Lines */  
	  
	final String[] FastKamaPeriod = {KAMA_PERIOD_F1, KAMA_PERIOD_F2, KAMA_PERIOD_F3, KAMA_PERIOD_F4, KAMA_PERIOD_F5};
	final String[] FastKamaFast = {KAMA_FAST_F1, KAMA_FAST_F2, KAMA_FAST_F3, KAMA_FAST_F4, KAMA_FAST_F5 };
	final String[] FastKamaSlow = {KAMA_SLOW_F1, KAMA_SLOW_F2, KAMA_SLOW_F3, KAMA_SLOW_F4, KAMA_SLOW_F5 }; 
	final String[] FastKamaEnum = {"KAMA1", "KAMA2","KAMA3", "KAMA4", "KAMA5"};
	
	// Get the settings as defined by the user in the study dialog
    //int period = getSettings().getInteger(KAMA_PERIOD_F1);

	
	
	
	
	for (int z = 0; z <= myIndex; z++) {
		
		if ((dataInitialized) && (z < myIndex-10)) continue;
		
		for(int i = 0; i <= 4; i++)  //Loop 5 times
		{
			FastKamaValues updateFastK = FastKamaValues.valueOf(FastKamaEnum[i]);
			Object input = getSettings().getInput(KAMA_INPUT);
			//debug("Index = " + myIndex + "  i loop value " + i + "input" + input);
			int period = getSettings().getInteger(FastKamaPeriod[i]);
			//debug("Index = " + myIndex + "  i loop value " + i + "period" + period);
			int kaFast = getSettings().getInteger(FastKamaFast[i]);
			int kaSlow = getSettings().getInteger(FastKamaSlow[i]);
			double prevKama = 0.000000000000001;  //initialize the previous Kama
			//debug(" Here at myIndex1 = " + myIndex + "   prevKama = " + prevKama); 
			
			if ((z > period*3) && (series.getDouble(z-1, updateFastK) != null)) {   //myIndex changed to j 2x
				prevKama = series.getDouble(z-1, updateFastK); 
	    		} 
			//debug(" Here at myIndex2 = " + myIndex + "   prevKama = " + prevKama); 
			double currentKama=vrUtility.kamaLine(z, ctx, period, kaFast, kaSlow, input, prevKama); // <- KAMA Calculation // J added
			//debug(" Here at myIndex3 = " + myIndex + "   currentKama = " + prevKama); 
			series.setDouble(z, updateFastK, currentKama);  // <- Update the series for graphing   // J added
		}
	}    
	
	final String[] SlowKamaPeriod = {KAMA_PERIOD_S1, KAMA_PERIOD_S2, KAMA_PERIOD_S3, KAMA_PERIOD_S4, KAMA_PERIOD_S5};
	final String[] SlowKamaFast = {KAMA_FAST_S1, KAMA_FAST_S2, KAMA_FAST_S3, KAMA_FAST_S4, KAMA_FAST_S5 };
	final String[] SlowKamaSlow = {KAMA_SLOW_S1, KAMA_SLOW_S2, KAMA_SLOW_S3, KAMA_SLOW_S4, KAMA_SLOW_S5 }; 
	final String[] SlowKamaEnum = {"mKAMA1", "mKAMA2","mKAMA3", "mKAMA4", "mKAMA5"};
	
	for (int z = 0; z <= myIndex; z++) {
		
		if ((dataInitialized) && (z < myIndex-10)) continue;
		
		for(int i = 0; i <= 4; i++)  //Loop 5 times
		{
			SlowKamaValues updateSlowK = SlowKamaValues.valueOf(SlowKamaEnum[i]);
			Object input = getSettings().getInput(KAMA_INPUT);
			//debug("Index = " + myIndex + "  i loop value " + i + "input" + input);
			int period = getSettings().getInteger(SlowKamaPeriod[i]);
			//debug("myIndex = " + myIndex + "  i loop value " + i + "period" + period);
			int kaFast = getSettings().getInteger(SlowKamaFast[i]);
			int kaSlow = getSettings().getInteger(SlowKamaSlow[i]);
			double prevKama = 0.000000000000001;  //initialize the previous Kama
			//debug(" Here at myIndexslow = " + myIndex + "   prevKama = " + prevKama); 
			if ((z > period*3) && (series.getDouble(z-1, updateSlowK) != null)) {
				prevKama = series.getDouble(z-1, updateSlowK);
	    		} 
			//debug(" Here at myIndexslow2 = " + myIndex + "   prevKama = " + prevKama); 
			double currentKama=vrUtility.kamaLine(z, ctx, period, kaFast, kaSlow, input, prevKama); // <- KAMA Calculation
			series.setDouble(z, updateSlowK, currentKama);  // <- Update the series for graphing
		}
	
	}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////           KAMA SHADING                                                      /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
	
	// FAST KAMA SHADING
	for (int z = 0; z <= myIndex; z++) {
	
	if ((dataInitialized) && (z < myIndex-10)) continue;
		
	Double FDelta = Math.abs(series.getDouble(z, FastKamaValues.KAMA1)-series.getDouble(z, FastKamaValues.KAMA5));
	Double SDelta = Math.abs(series.getDouble(z, SlowKamaValues.mKAMA1)-series.getDouble(z, SlowKamaValues.mKAMA5));
	
	if (FDelta == null || SDelta == null) continue;
	
	series.setDouble(z, KamaDelta.FASTDELTA, FDelta);
	series.setDouble(z, KamaDelta.SLOWDELTA, SDelta);
	
	if ((series.getDouble(z, FastKamaValues.KAMA1) > series.getDouble(z, FastKamaValues.KAMA2)) &&
	   (series.getDouble(z, FastKamaValues.KAMA2) > series.getDouble(z, FastKamaValues.KAMA3)) &&
	   (series.getDouble(z, FastKamaValues.KAMA3) > series.getDouble(z, FastKamaValues.KAMA4)) &&
	   (series.getDouble(z, FastKamaValues.KAMA4) > series.getDouble(z, FastKamaValues.KAMA5)))
	    {
		series.setDouble(z, FastKamaShade.TOP, series.getDouble(z, FastKamaValues.KAMA1));
		series.setDouble(z, FastKamaShade.BOTTOM, series.getDouble(z, FastKamaValues.KAMA5));
		series.setBoolean(z, KamaPerfect.BULL, true); //set Bull Flag
		series.setBoolean(z, KamaPerfect.BEAR, false); //
	
		} else if 
	   ((series.getDouble(z, FastKamaValues.KAMA1) < series.getDouble(z, FastKamaValues.KAMA2)) &&
	   (series.getDouble(z, FastKamaValues.KAMA2) < series.getDouble(z, FastKamaValues.KAMA3)) &&
	   (series.getDouble(z, FastKamaValues.KAMA3) < series.getDouble(z, FastKamaValues.KAMA4)) &&
	   (series.getDouble(z, FastKamaValues.KAMA4) < series.getDouble(z, FastKamaValues.KAMA5))) 
		{
	   series.setDouble(z, FastKamaShade.TOP, series.getDouble(z, FastKamaValues.KAMA1));
	   series.setDouble(z, FastKamaShade.BOTTOM, series.getDouble(z, FastKamaValues.KAMA5));
	   series.setBoolean(z, KamaPerfect.BULL, false); //set Bull Flag
	   series.setBoolean(z, KamaPerfect.BEAR, true); //
		 //if (series.getBoolean(z, KamaPerfect.BEAR) == null) debug("Bear Null Value");
		 //info("K2 z =" + z + "  Bear  = " + series.getBoolean(z, KamaPerfect.BEAR));
		 
		} else {
		 series.setDouble(z, FastKamaShade.TOP, series.getDouble(z, FastKamaValues.KAMA3));
		 series.setDouble(z, FastKamaShade.BOTTOM, series.getDouble(z, FastKamaValues.KAMA3));
		 series.setBoolean(z, KamaPerfect.BULL, false); //set Bull Flag
		 series.setBoolean(z, KamaPerfect.BEAR, false);
		 
		}  
	
	// SLOW KAMA SHADING
	
	if ((series.getDouble(z, SlowKamaValues.mKAMA1) > series.getDouble(z, SlowKamaValues.mKAMA2)) &&
	   (series.getDouble(z, SlowKamaValues.mKAMA2) > series.getDouble(z, SlowKamaValues.mKAMA3)) &&
	   (series.getDouble(z, SlowKamaValues.mKAMA3) > series.getDouble(z, SlowKamaValues.mKAMA4)) &&
	   (series.getDouble(z, SlowKamaValues.mKAMA4) > series.getDouble(z, SlowKamaValues.mKAMA5)))
	    {
		series.setDouble(z, SlowKamaShade.mTOP, series.getDouble(z, SlowKamaValues.mKAMA1));
		series.setDouble(z, SlowKamaShade.mBOTTOM, series.getDouble(z, SlowKamaValues.mKAMA5));
		series.setBoolean(z, KamaPerfect.BULL_SLOW, true); //set Bull Slow Flag
		series.setBoolean(z, KamaPerfect.BEAR_SLOW, false); //

		} else if 
	   ((series.getDouble(z, SlowKamaValues.mKAMA1) < series.getDouble(z, SlowKamaValues.mKAMA2)) &&
	   (series.getDouble(z, SlowKamaValues.mKAMA2) < series.getDouble(z, SlowKamaValues.mKAMA3)) &&
	   (series.getDouble(z, SlowKamaValues.mKAMA3) < series.getDouble(z, SlowKamaValues.mKAMA4)) &&
	   (series.getDouble(z, SlowKamaValues.mKAMA4) < series.getDouble(z, SlowKamaValues.mKAMA5))) 
		{
		 series.setDouble(z, SlowKamaShade.mTOP, series.getDouble(z, SlowKamaValues.mKAMA1));
	     series.setDouble(z, SlowKamaShade.mBOTTOM, series.getDouble(z, SlowKamaValues.mKAMA5));
		   series.setBoolean(z, KamaPerfect.BULL_SLOW, false); //set Bull Slow Flag
		   series.setBoolean(z, KamaPerfect.BEAR_SLOW, true); //
		} else {
		 series.setDouble(z, SlowKamaShade.mTOP, series.getDouble(z, SlowKamaValues.mKAMA3));
		 series.setDouble(z, SlowKamaShade.mBOTTOM, series.getDouble(z, SlowKamaValues.mKAMA3));
		 series.setBoolean(z, KamaPerfect.BULL_SLOW, false); //
		   series.setBoolean(z, KamaPerfect.BEAR_SLOW, false); //
		 
		}
     
	}  //end of KAMA Shading loop
	
	//BBANDs BASED UPON KAMAs
	    
    bbinput= getSettings().getInput(BB_INPUT);
    bbPeriod = getSettings().getInteger(BB_PERIOD);
    bbmethod = getSettings().getMAMethod(BB_METHOD, Enums.MAMethod.EMA);
    bbStd1 = getSettings().getDouble(BB_STD_1);
    bbStd2 = getSettings().getDouble(BB_STD_2);
    bbStd3 = getSettings().getDouble(BB_STD_3);
    bbStd4 = getSettings().getDouble(BB_STD_4);
    
    int bbgetkama = getSettings().getInteger(BB_KAMA_INPUT);
	String kamastring; 
    
    switch (bbgetkama) {
		    case 1: kamastring = "KAMA1";
		    		break;
		    case 2: kamastring = "KAMA2";
		    		break;
		    case 3: kamastring = "KAMA3";
    				break;
		    case 4: kamastring = "KAMA4";
    				break;
    		default: kamastring = "KAMA5";
    				break;
    }
    
    
	for(int z = 0; z <= myIndex; z++) 
		{	
	      
		if ((dataInitialized) && (z < myIndex-10)) continue;
        //Bband calculation
		//bbinput= getSettings().getInput(BB_INPUT);
    	//Double bbStdDevs = series.std(z, bbPeriod, bbinput);  //std dev on price
		
    	Double bbStdDevs = series.std(z, bbPeriod, FastKamaValues.valueOf(kamastring));  //std dev on Kama
    	Double bbStdDevma = series.getDouble(z, FastKamaValues.valueOf(kamastring));
	    //Double bbStdDevma = series.ma(bbmethod, z, bbPeriod, FastKamaValues.KAMA5);  //placed statically
    	    
	    if (bbStdDevs == null || bbStdDevma == null) continue; //{bbStdDevs = .0000001; bbStdDevma = 0.000001;}

	    //Band input { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};
	    series.setDouble(z, BBValues.BB1U, bbStdDevma + bbStdDevs * bbStd1);
	    series.setDouble(z, BBValues.BB1L, bbStdDevma - bbStdDevs* bbStd1);
	    series.setDouble(z, BBValues.BB2U, bbStdDevma + bbStdDevs* bbStd2);
	    series.setDouble(z, BBValues.BB2L, bbStdDevma - bbStdDevs* bbStd2);
	    series.setDouble(z, BBValues.BB3U, bbStdDevma + bbStdDevs* bbStd3);
	    series.setDouble(z, BBValues.BB3L, bbStdDevma - bbStdDevs* bbStd3);
	    series.setDouble(z, BBValues.BB4U, bbStdDevma + bbStdDevs* bbStd4);
	    series.setDouble(z, BBValues.BB4L, bbStdDevma - bbStdDevs* bbStd4);
	    series.setDouble(z, BBValues.MID, bbStdDevma);
	}	
	
	bbinput= getSettings().getInput(BB_INPUTv2);
    bbPeriod = getSettings().getInteger(BB_PERIODv2);
    bbmethod = getSettings().getMAMethod(BB_METHODv2, Enums.MAMethod.EMA);
    bbStd1 = getSettings().getDouble(BB_STD_1v2);
    bbStd2 = getSettings().getDouble(BB_STD_2v2);
    bbStd3 = getSettings().getDouble(BB_STD_3v2);
    bbStd4 = getSettings().getDouble(BB_STD_4v2);
	
    bbgetkama = getSettings().getInteger(BB_KAMA_INPUTv2);
	    
    switch (bbgetkama) {
	    case 1: kamastring = "mKAMA1";
	    		break;
	    case 2: kamastring = "mKAMA2";
	    		break;
	    case 3: kamastring = "mKAMA3";
				break;
	    case 4: kamastring = "mKAMA4";
				break;
		default: kamastring = "mKAMA5";
				break;
	    }
	    
    
    for(int z = 0; z <= myIndex; z++) 
		{	
    	if ((dataInitialized) && (z < myIndex-10)) continue;  	
        //Bband calculation
    	
    	//Double bbStdDevs = series.std(z, bbPeriod, bbinput);
    	Double bbStdDevs = series.std(z, bbPeriod, SlowKamaValues.valueOf(kamastring)); 
    	Double bbStdDevma = series.getDouble(z, SlowKamaValues.valueOf(kamastring));
	    //Double bbStdDevma = series.ma(bbmethod, z, bbPeriod, SlowKamaValues.mKAMA5);
    	    
	    if (bbStdDevs == null || bbStdDevma == null) continue; //{bbStdDevs = .0000001; bbStdDevma = 0.000001;}

	    //Band input { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};
	    series.setDouble(z, BBValuesv2.BB1Uv2, bbStdDevma + bbStdDevs * bbStd1);
	    series.setDouble(z, BBValuesv2.BB1Lv2, bbStdDevma - bbStdDevs* bbStd1);
	    series.setDouble(z, BBValuesv2.BB2Uv2, bbStdDevma + bbStdDevs* bbStd2);
	    series.setDouble(z, BBValuesv2.BB2Lv2, bbStdDevma - bbStdDevs* bbStd2);
	    series.setDouble(z, BBValuesv2.BB3Uv2, bbStdDevma + bbStdDevs* bbStd3);
	    series.setDouble(z, BBValuesv2.BB3Lv2, bbStdDevma - bbStdDevs* bbStd3);
	    series.setDouble(z, BBValuesv2.BB4Uv2, bbStdDevma + bbStdDevs* bbStd4);
	    series.setDouble(z, BBValuesv2.BB4Lv2, bbStdDevma - bbStdDevs* bbStd4);
	    series.setDouble(z, BBValuesv2.MIDv2, bbStdDevma);
	    
	    
	}	
	
    ////////////////////////////////////////////////////////////////////////////////////////////////
  //////////          KEY MAPPING FOR COLOR CHANGES                                          /////////
   ////////////////////////////////////////////////////////////////////////////////////////////////

    
    //  Gray Scale Values
    int FadeSteps = 12;
   Color pStartColor = getSettings().getColor(LTCOLOR);
   Color pEndColor = getSettings().getColor(DKCOLOR);
   
    Map<String, Color> pathCol = new HashMap<String, Color>();
    
    //debug("pStartColor Grey = " + pStartColor);
    //debug("pEndColor Grey = " + pEndColor);
    
    if (pStartColor !=null && pEndColor != null) {
    	
	    final int dRed = pEndColor.getRed() - pStartColor.getRed();
	    final int dGreen = pEndColor.getGreen() - pStartColor.getGreen();
	    final int dBlue = pEndColor.getBlue() - pStartColor.getBlue();
	    
	    if (dRed != 0 || dGreen != 0 || dBlue != 0) {
		    for(int i = 0; i < 12; i++) {
		    	Color c = new Color(
		    			pStartColor.getRed() + ((dRed * i) / FadeSteps),
		    			pStartColor.getGreen() + ((dGreen * i) / FadeSteps),
		    			pStartColor.getBlue() + ((dBlue * i) / FadeSteps));
		    	pathCol.put("Path" + i, c);
		    	//debug("Path" + i + " Color => " + c);
		    	
		    }
	    }
    }
    
    
    //  Green Scale Values
    FadeSteps = 12;
    pStartColor = getSettings().getColor(LTGRCOLOR);
    pEndColor = getSettings().getColor(DKGRCOLOR);
   
    Map<String, Color> GreenCol = new HashMap<String, Color>();   //Green Color Map
    
    
    if (pStartColor !=null && pEndColor != null) {
    	
	    final int dRed = pEndColor.getRed() - pStartColor.getRed();
	    final int dGreen = pEndColor.getGreen() - pStartColor.getGreen();
	    final int dBlue = pEndColor.getBlue() - pStartColor.getBlue();
	    
	    if (dRed != 0 || dGreen != 0 || dBlue != 0) {
		    for(int i = 0; i < 12; i++) {
		    	Color c = new Color(
		    			pStartColor.getRed() + ((dRed * i) / FadeSteps),
		    			pStartColor.getGreen() + ((dGreen * i) / FadeSteps),
		    			pStartColor.getBlue() + ((dBlue * i) / FadeSteps));
		    	GreenCol.put("GPath" + i, c);
		    	
		    	
		    }
	    }
    }
	
    //  Red Scale Values
    FadeSteps = 12;
    pStartColor = getSettings().getColor(LTRDCOLOR);
    pEndColor = getSettings().getColor(DKRDCOLOR);
   
    Map<String, Color> RedCol = new HashMap<String, Color>();   //Red Color Map

    
    if (pStartColor !=null && pEndColor != null) {
    	
	    final int dRed = pEndColor.getRed() - pStartColor.getRed();
	    final int dGreen = pEndColor.getGreen() - pStartColor.getGreen();
	    final int dBlue = pEndColor.getBlue() - pStartColor.getBlue();
	    
	    if (dRed != 0 || dGreen != 0 || dBlue != 0) {
		    for(int i = 0; i < 12; i++) {
		    	Color c = new Color(
		    			pStartColor.getRed() + ((dRed * i) / FadeSteps),
		    			pStartColor.getGreen() + ((dGreen * i) / FadeSteps),
		    			pStartColor.getBlue() + ((dBlue * i) / FadeSteps));
		    	RedCol.put("RPath" + i, c);
		    
		    	
		    }
	    }
    }
	
	
	//////       CREATE DELTA COLOR SERIES      //////
	for (int z = 0; z <= myIndex; z++) {
		
		if (z < 1000) continue;
    
		if ((dataInitialized) && (z < myIndex-10)) continue;
		
    double fastSig = series.getDouble(z, SMIValmtf.mSMI1);
    double slowSig = series.getDouble(z, SMIValmtf.mSMI12);
    
    double deltaThreshold = getSettings().getDouble(DELTACOLOR, 3.0);
    double deltaMax = getSettings().getDouble(MAXCOLOR, 10.0);
    double Delta = Math.abs(fastSig-slowSig);
	
    //for (int i = 0; i < 12; i++)
	  // {   
		//   SMIValmtf SMIline = SMIValmtf.valueOf(SMIenummtf[i]);
		//   Color currentColor = GreenCol.get("GPath" + i);
		//   series.setPathColor(z, SMIline, currentColor); //sets the color for the line
	   //}
    
    	if ((fastSig > slowSig) && (Delta > deltaThreshold)) { //green and trending
	    	
		   for (int i = 0; i < 12; i++)
		   {   
			   SMIValmtf SMIline = SMIValmtf.valueOf(SMIenummtf[i]);
			   Color currentColor = vrUtility.colorFade(pathCol.get("Path" + i), GreenCol.get("GPath" + i), Delta, deltaMax, deltaThreshold);
			   series.setPathColor(z, SMIline, currentColor); //sets the color for the line
		   }
	    	
	   } else if ((fastSig < slowSig) && (Delta > deltaThreshold)) {
	    
		   for (int i = 0; i < 12; i++)
		   {   
			   SMIValmtf SMIline = SMIValmtf.valueOf(SMIenummtf[i]);
			   Color currentColor = vrUtility.colorFade(pathCol.get("Path" + i), RedCol.get("RPath" + i), Delta, deltaMax, deltaThreshold);
			   series.setPathColor(z, SMIline, currentColor); //sets the color for the line
		   }
	   } else {
		   
		   for (int i = 0; i < 12; i++)
		   {   
			   SMIValmtf SMIline = SMIValmtf.valueOf(SMIenummtf[i]);
			   Color currentColor = pathCol.get("Path" + i);
			   series.setPathColor(z, SMIline, currentColor); //sets the color for the line
		   }
		     
	   }
	  
	
	} //end of color series z loop
	
	
	
	   //////////////////////////////////////////////////////////////////////////////////////////////////////////     
      ///////       SIGNAL  GENERATION                                                                 //////////
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	///  Shallow Pull-back Logic
	for (int z = 0; z <= myIndex; z++) {
			
			if (z < 100) continue;
			if ((dataInitialized) && (z < myIndex-10)) continue;
			
			//Short Term Pull-back
	        
	        double fastSig = series.getDouble(z, SMIVal.SMI1);
	        double fastSig2 = series.getDouble(z-1, SMIVal.SMI1);
	        double slowSig = series.getDouble(z, SMIVal.SMI20);  //SMIValmtf.SMI9 
	        
	        Coordinate c = new Coordinate(series.getStartTime(z), fastSig);
	        
	        if ((fastSig2 < (double) 50.0) && (fastSig > (double) 50.0)
	   	       && (slowSig < (double) 0) && (series.getBoolean(z, KamaPerfect.BEAR)==true) 
	   	       && (series.getBoolean(z, KamaPerfect.BEAR_SLOW)==true))
	        {
	          series.setBoolean(z, SMISignal.SHALLOW_BEAR, true);
	          MarkerInfo marker = getSettings().getMarker(Inputs.DOWN_MARKER);
	          String msg = get("Shallow Bear Pullback", format(fastSig), format(slowSig), format(series.getClose(z)));
	          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.TOP, marker, msg));
	          ctx.signal(z, SMISignal.SHALLOW_BEAR, msg, round(fastSig));  
	        }
	        
	        if ((fastSig2 > (double)-50.0) && (fastSig < (double) -50.0)
	             && (slowSig > (double) 0)  && (series.getBoolean(z, KamaPerfect.BULL)==true) 
	             && (series.getBoolean(z, KamaPerfect.BULL_SLOW)==true))
	        {
	          series.setBoolean(z, SMISignal.SHALLOW_BULL, true);
	          MarkerInfo marker = getSettings().getMarker(Inputs.UP_MARKER);
	          String msg = get("Shallow Bull Pullback", format(fastSig), format(slowSig), format(series.getClose(z)));
	          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
	          ctx.signal(z, SMISignal.SHALLOW_BULL, msg, round(fastSig));     
	        }    
	        
		}  
	      
	
	    /// MOMO EXPANSION LOGIC
	
		for (int z = 0; z <= myIndex; z++) {
			
			if (z < 1000) continue; //set z to 1000 to allow SMIs to ramp up
			if ((dataInitialized) && (z < myIndex-10)) continue;
          //HFT expansion Logic
	        
			int xOverBardelay = getSettings().getInteger(HFT_XOVER, 30);
			double deltaThreshold = getSettings().getDouble(HFT_DELTA, 4.0);
			
			boolean xCrossAbove = false; 
	        boolean xCrossBelow = false; 
	        
	        for (int j = xOverBardelay; j > 0; j--) {
	        	
	        	
		        if (crossedAbove(series, z-j, SMIValmtf.mSMI1, SMIValmtf.mSMI12))
		            {
		        	xCrossAbove = true;
		        	xCrossBelow = false;
		        	}
		        if (crossedBelow(series, z-j, SMIValmtf.mSMI1, SMIValmtf.mSMI12))
		           {
		        	xCrossAbove = false;
		        	xCrossBelow = true;
		        	}
		       
	        }
	        
	      
	        double fastSig = series.getDouble(z, SMIValmtf.mSMI1);
	        double slowSig = series.getDouble(z, SMIValmtf.mSMI12);
	        
	        double Delta = Math.abs(fastSig-slowSig);
	        
	        double fastSig1 = series.getDouble(z-1, SMIValmtf.mSMI1);
	        double slowSig1 = series.getDouble(z-1, SMIValmtf.mSMI12);
	        
	        double Delta1 = Math.abs(fastSig1-slowSig1); //compare to last delta
	        
	        //if (xCrossAbove) debug("xcrossAbove Delta  " + round(Delta) + " @ z = " + z );
	        
	        Coordinate c = new Coordinate(series.getStartTime(z), fastSig);
	        
	        if (xCrossAbove && (Delta > deltaThreshold) && (Delta1 < deltaThreshold))  //Delta 1 component helps to trigger only once

		        {
	        	  
		         // info("EXPANSION triggered @z = " + z + "  fastSig = " + round(fastSig) + "   slowSig = " + round(slowSig));
		          series.setBoolean(z, SMISignal.HFT_BULL, true);
		          MarkerInfo marker = getSettings().getMarker(HFT_BULL_MARKER);
		          String msg = get("Bull HTF Expansion", format(fastSig), format(slowSig), format(series.getClose(z)));
		          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
		          ctx.signal(z, SMISignal.HFT_BULL, msg, round(fastSig));  
		        }
	        
	        if (xCrossBelow && (Delta > deltaThreshold) && (Delta1 < deltaThreshold))

	        {
	          //info("EXPANSION = " + z + "  fastSig = " + round(fastSig) + "   slowSig = " + round(slowSig));
	          series.setBoolean(z, SMISignal.HFT_BEAR, true);
	          MarkerInfo marker = getSettings().getMarker(HFT_BEAR_MARKER);
	          String msg = get("Bear HTF Expansion", format(fastSig), format(slowSig), format(series.getClose(z)));
	          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.TOP, marker, msg));
	          ctx.signal(z, SMISignal.HFT_BEAR, msg, round(fastSig));  
	        }

  		}  //ends z loop for MOMO expansion
		
		//Begin Deep Pullback Logic - Price enters Slow Kama Wave, wave is perfect and the SlowK5 is below the 200SMA
		
		for (int z = 0; z <= myIndex; z++) {
			
			if (z < 1000) continue; //set z to 1000 to allow SMIs to ramp up
			if ((dataInitialized) && (z < myIndex-10)) continue;
			
		double priceH = series.getHigh(z);
		double priceH1 = series.getHigh(z-1);
		double priceL = series.getLow(z);
		double priceL1 = series.getLow(z-1);
		
        Coordinate c = new Coordinate(series.getStartTime(z), priceL);
        
        //debug("Z => " + z + " mKAMA5 = " + series.getDouble(z, SlowKamaValues.mKAMA5) + "   200 ma => " + series.getDouble(z, SMAVal.MA));
        
        if (( priceL1 > series.getDouble(z-1, SlowKamaValues.mKAMA1)) // previous low is above the kama1 band
            	&& (priceL < series.getDouble(z, SlowKamaValues.mKAMA1)) //current low has pierced the current band
            	&& (series.getBoolean(z, KamaPerfect.BULL_SLOW) == true) //current Slow Kama is Perfect
            	&& (series.getDouble(z, SlowKamaValues.mKAMA5) > series.getDouble(z, SMAVal.MA))) // Slowest Kama is above the 200 SMA
	        {	  
        	  //info("Deep Bull Pullback = " + z );
	          series.setBoolean(z, SMISignal.DEEP_BULL, true);
	          MarkerInfo marker = getSettings().getMarker(DEEP_BULL_MARKER);
	          String msg = get("Deep Bull Pullback", format(priceL), format(series.getClose(z)));
	          if (marker.isEnabled()) addFigure(Plot.PRICE, new Marker(c, Enums.Position.BOTTOM, marker, msg));
	          ctx.signal(z, SMISignal.DEEP_BULL, msg, round(priceL));  
	        }
        
        c = new Coordinate(series.getStartTime(z), priceH);
        if (( priceH1 < series.getDouble(z-1, SlowKamaValues.mKAMA1)) // previous high is under the previous band
        	&& (priceH > series.getDouble(z, SlowKamaValues.mKAMA1)) //current high has pierced the current band
        	&& (series.getBoolean(z, KamaPerfect.BEAR_SLOW) == true) //current Slow Kama is Perfect
        	&& (series.getDouble(z, SlowKamaValues.mKAMA5) < series.getDouble(z, SMAVal.MA))) // Slowest Kama is below the 200 SMA
	        {
	          //info("Deep Bear Pullback = " + z );
	          series.setBoolean(z, SMISignal.DEEP_BEAR, true);
	          MarkerInfo marker = getSettings().getMarker(DEEP_BEAR_MARKER);
	          String msg = get("Deep Bear Pullback ", format(priceH), format(series.getClose(z)));
	          if (marker.isEnabled()) addFigure(Plot.PRICE, new Marker(c, Enums.Position.TOP, marker, msg));
	          ctx.signal(z, SMISignal.DEEP_BEAR, msg, round(priceH));  
	         // setBarColor(z, Enums.BarData.PRICE ,Color.decode("#3D6566"));
	        }

		
		} // end Deep pullback signals
		
		
		
		
		series.setInt(0, Counter.LOOP, 1); // Flag, full set of calculations have been completed
	
	
  	} // ends calculateValues loop
}

	
