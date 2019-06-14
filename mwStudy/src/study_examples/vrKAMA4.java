package study_examples;

import java.awt.Color;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.draw.Marker;
import com.motivewave.platform.sdk.study.*;

import study_examples.vrFrameWork.SMISignal;



// Kaufman's Adaptive Moving Average. Developed by Perry Kaufman, this indicator is an
// EMA using an Efficiency Ratio to modify the smoothing constant, which ranges from
// a minimum of Fast Length to a maximum of Slow Length. Since this moving average is
// adaptive it tends to follow prices more closely than other MA's.


@StudyHeader(
 namespace="com.mycompany", 
 id="KaMa4",
 rb="study_examples.nls.strings", // locale specific strings are loaded from here
 name="vrKaMa4",
 label="Kama",
 desc="Kaufman's Adaptive Moving Average",
 menu="W. VanRip",
 underlayByDefault = true,
 overlay=true,
 signals=true,
 studyOverlay=true)

public class vrKAMA4 extends Study
{
	  enum FastKamaValues { KAMA1, KAMA2, KAMA3, KAMA4, KAMA5 };
	  enum FastKamaShade { TOP, BOTTOM};
	  enum SlowKamaValues { mKAMA1, mKAMA2, mKAMA3, mKAMA4, mKAMA5 };
	  enum SlowKamaShade { mTOP, mBOTTOM};
	  enum BBValues { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID, KAMASTDEV, NORMALIZED};
	  enum BBValuesv2 { BB1Uv2, BB2Uv2, BB3Uv2, BB4Uv2, BB1Lv2, BB2Lv2, BB3Lv2, BB4Lv2, MIDv2, KAMASTDEV2, NORMALIZEDv2};
	  enum FastKamaSQ {sqTOP1, sqBOTTOM1, sqTOP2, sqBOTTOM2,};
	  enum SlowKamaSQ {sqTOP1v2, sqBOTTOM1v2, sqTOP2v2, sqBOTTOM2v2};
	  enum KamaPerfect{BULL, BEAR};
	  
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
	  
	  final static String SQ_THRESHOLD = "SQthres";     //Squeeze constants
	  final static String SQ_NORM = "SQnormaliztion";
	  final static String SQ_THRESHOLD_Slow = "SQthresSlow";     //Squeeze constants
	  final static String SQ_NORM_Slow= "SQnormaliztionSlow";
	  
	  final static String SQ_FAST_FILL1 = "SQFastFill1";  
	  final static String SQ_FAST_FILL2 = "SQFastFill2";  //Squeeze Fills
	  final static String SQ_SLOW_FILL1 = "SQSlowFill1";
	  final static String SQ_SLOW_FILL2 = "SQSlowFill2";
	  final static String SQ_FAST_T1_INVIS = "SQFTinv";     //Squeeze constants
	  final static String SQ_FAST_B1_INVIS = "SQFBinv";
	  final static String SQ_FAST_T2_INVIS = "SQFT2inv2";     //Squeeze constants
	  final static String SQ_FAST_B2_INVIS = "SQFB2inv2";
	  final static String SQ_SLOW_T1_INVIS = "SQSTinv";     //Squeeze Fills
	  final static String SQ_SLOW_B1_INVIS = "SQSBinv";
	  final static String SQ_SLOW_T2_INVIS = "SQST2inv";     //Squeeze Fills
	  final static String SQ_SLOW_B2_INVIS = "SQSB2inv";
	  
	  final static String SQ_F_MARKERUP = "SQFMarUP";     //Squeeze Fills
	  final static String SQ_F_MARKERDN = "SQFMarDN";
	  final static String SQ_S_MARKERUP = "SQSMarUP";     //Squeeze Fills
	  final static String SQ_S_MARKERDN = "SQSMarDN";
	  
  /** This method initializes the study by doing the following:
      1. Define Settings (Design Time Information)
      2. Define Runtime Information (Label, Path and Exported Value) */
  @Override
  public void initialize(Defaults defaults)
  {  
	SettingsDescriptor sd = new SettingsDescriptor();
    setSettingsDescriptor(sd);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////          INITIALIZE FAST KAMA                                                         /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    SettingTab tab = new SettingTab("Fast Kama");
    sd.addTab(tab);

    SettingGroup inputs = new SettingGroup("Fast KAMA Wave Inputs");

    inputs.addRow(new InputDescriptor(KAMA_INPUT, get("Input"), Enums.BarInput.CLOSE));
    
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD_F1, get("Period"), 10, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_FAST_F1, get("Fast"), 2, 1, 999, 1),
    		      new IntegerDescriptor(KAMA_SLOW_F1, get("Slow"), 30, 1, 999, 1)); 
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD_F2, get("Period"), 10, 1, 999, 1),
			  	  new IntegerDescriptor(KAMA_FAST_F2, get("Fast"), 3, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F2, get("Slow"), 30, 1, 999, 1)); 
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD_F3, get("Period"), 10, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_F3, get("Fast"), 4, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F3, get("Slow"), 30, 1, 999, 1)); 
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD_F4, get("Period"), 10, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_F4, get("Fast"), 5, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F4, get("Slow"), 30, 1, 999, 1)); 
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD_F5, get("Period"), 10, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_F5, get("Fast"), 6, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_F5, get("Slow"), 30, 1, 999, 1)); 
    
    tab.addGroup(inputs);
    
    SettingGroup colors = new SettingGroup("Fast KAMA Lines");
    colors.addRow(new PathDescriptor(KAMA_PATH_F1, "Line 1", defaults.getOrange(), 0.5f, 
                  null, true, true, true));
    colors.addRow(new PathDescriptor(KAMA_PATH_F2, "Line 2", defaults.getGrey(), 0.5f, 
            	  null, true, true, true));
    colors.addRow(new PathDescriptor(KAMA_PATH_F3, "Line 3", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colors.addRow(new PathDescriptor(KAMA_PATH_F4, "Line 4", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colors.addRow(new PathDescriptor(KAMA_PATH_F5, "Line 5", defaults.getBlue(), 0.5f, 
                  null, true, true, true));
    colors.addRow(new PathDescriptor(KAMA_FAST_T_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    colors.addRow(new PathDescriptor(KAMA_FAST_B_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    
    Color upFillF = new Color(69, 90, 100, 180);
    Color dnFillF = new Color(255, 200, 210, 110);
    colors.addRow(new ShadeDescriptor(KAMA_FAST_T_FILL, "Up Wave", KAMA_FAST_T_INVIS, KAMA_FAST_B_INVIS,
    		Enums.ShadeType.ABOVE, upFillF, true, true));
    colors.addRow(new ShadeDescriptor(KAMA_FAST_B_FILL, "Down Wave", KAMA_FAST_T_INVIS, KAMA_FAST_B_INVIS,
    		Enums.ShadeType.BELOW, dnFillF, true, true));
    tab.addGroup(colors);
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////          INITIALIZE SLOW KAMA                                                         /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    tab = new SettingTab("Slow Kama");
    sd.addTab(tab);

    SettingGroup inputs2 = new SettingGroup("Slow KAMA Wave Inputs");

    //inputs2.addRow(new InputDescriptor(KAMA_INPUT, get("Input"), Enums.BarInput.CLOSE));
    
    inputs2.addRow(new IntegerDescriptor(KAMA_PERIOD_S1, get("Period"), 20, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_FAST_S1, get("Fast"), 6, 1, 999, 1),
    		      new IntegerDescriptor(KAMA_SLOW_S1, get("Slow"), 30, 1, 999, 1)); 
    inputs2.addRow(new IntegerDescriptor(KAMA_PERIOD_S2, get("Period"), 20, 1, 999, 1),
			  	  new IntegerDescriptor(KAMA_FAST_S2, get("Fast"), 7, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S2, get("Slow"), 30, 1, 999, 1)); 
    inputs2.addRow(new IntegerDescriptor(KAMA_PERIOD_S3, get("Period"), 20, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_S3, get("Fast"), 8, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S3, get("Slow"), 30, 1, 999, 1)); 
    inputs2.addRow(new IntegerDescriptor(KAMA_PERIOD_S4, get("Period"), 20, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_S4, get("Fast"), 9, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S4, get("Slow"), 30, 1, 999, 1)); 
    inputs2.addRow(new IntegerDescriptor(KAMA_PERIOD_S5, get("Period"), 20, 1, 999, 1),
			      new IntegerDescriptor(KAMA_FAST_S5, get("Fast"), 10, 1, 999, 1),
		          new IntegerDescriptor(KAMA_SLOW_S5, get("Slow"), 30, 1, 999, 1)); 
    
    tab.addGroup(inputs2);
    
    SettingGroup colors2 = new SettingGroup("Slow KAMA Lines");
    colors2.addRow(new PathDescriptor(KAMA_PATH_S1, "Line 1", defaults.getOrange(), 0.5f, 
                  null, true, true, true));
    colors2.addRow(new PathDescriptor(KAMA_PATH_S2, "Line 2", defaults.getGrey(), 0.5f, 
            	  null, true, true, true));
    colors2.addRow(new PathDescriptor(KAMA_PATH_S3, "Line 3", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colors2.addRow(new PathDescriptor(KAMA_PATH_S4, "Line 4", defaults.getGrey(), 0.5f, 
                  null, true, true, true));
    colors2.addRow(new PathDescriptor(KAMA_PATH_S5, "Line 5", defaults.getBlue(), 0.5f, 
                  null, true, true, true));
    colors2.addRow(new PathDescriptor(KAMA_SLOW_T_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    colors2.addRow(new PathDescriptor(KAMA_SLOW_B_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    
    Color upFillS = new Color(69, 90, 100, 100);
    Color dnFillS = new Color(255, 200, 210, 60);
    colors2.addRow(new ShadeDescriptor(KAMA_SLOW_T_FILL, "Up Wave", KAMA_SLOW_T_INVIS, KAMA_SLOW_B_INVIS, 
    		Enums.ShadeType.ABOVE, upFillS, true, true));
    colors2.addRow(new ShadeDescriptor(KAMA_SLOW_B_FILL, "Down Wave", KAMA_SLOW_T_INVIS, KAMA_SLOW_B_INVIS, 
    		Enums.ShadeType.BELOW, dnFillS, true, true));
    tab.addGroup(colors2);
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////          INITIALIZE KAMA BANDS                                              /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
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
    //////////          INITIALIZE SQUEEZE SETTINGS                                              /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    tab = new SettingTab("Squeeze Settings");
    sd.addTab(tab);

    inputs = new SettingGroup("KAMA Squeeze Inputs");

    inputs.addRow(new IntegerDescriptor(SQ_NORM, "Normalization Period", 120, 1, 3000, 1));
    
    inputs.addRow(new DoubleDescriptor(SQ_THRESHOLD, "Squeeze Threshold", 6.0, .01, 100, .01)); 
    
    inputs.addRow(new MarkerDescriptor(SQ_F_MARKERUP, "Fast KAMA Compression", 
            Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
    inputs.addRow(new MarkerDescriptor(SQ_F_MARKERDN, "Fast KAMA Compression", 
    	        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
    
    
    inputs.addRow(new PathDescriptor(SQ_FAST_T1_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    inputs.addRow(new PathDescriptor(SQ_FAST_B1_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    inputs.addRow(new PathDescriptor(SQ_FAST_T2_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    inputs.addRow(new PathDescriptor(SQ_FAST_B2_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    
    
    Color BBSQFastFill = new Color(180, 100, 100, 50);
    Color BBSQFastFill2 = new Color(180, 100, 100, 50);
    
    inputs.addRow(new ShadeDescriptor(SQ_FAST_FILL1, "Fast KAMA Squeeze Upper", SQ_FAST_T1_INVIS, SQ_FAST_B1_INVIS, 
    		Enums.ShadeType.ABOVE, BBSQFastFill, true, true));
    inputs.addRow(new ShadeDescriptor(SQ_FAST_FILL2, "Fast KAMA Squeeze Lower", SQ_FAST_T2_INVIS, SQ_FAST_B2_INVIS, 
    		Enums.ShadeType.ABOVE, BBSQFastFill2, true, true));
    
    
    
    
    inputs.addRow(new IntegerDescriptor(SQ_NORM_Slow, "Slow Normalization Period", 120, 1, 3000, 1));
    inputs.addRow(new DoubleDescriptor(SQ_THRESHOLD_Slow, "Slow Squeeze Threshold", 6.0, .01, 100, .01)); 
    
    inputs.addRow(new MarkerDescriptor(SQ_S_MARKERUP, "Slow KAMA Squeeze Upper", 
            Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
    inputs.addRow(new MarkerDescriptor(SQ_S_MARKERDN, "Slow KAMA Squeeze Upper", 
    	        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
    
    
    inputs.addRow(new PathDescriptor(SQ_SLOW_T1_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    inputs.addRow(new PathDescriptor(SQ_SLOW_B1_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    inputs.addRow(new PathDescriptor(SQ_SLOW_T2_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    inputs.addRow(new PathDescriptor(SQ_SLOW_B2_INVIS, "Make Invisible", null, 1.0f, 
            null, false, true, true));
    
    Color BBSQSlowFill = new Color(69, 90, 100, 100);
    Color BBSQSlowFill2 = new Color(69, 90, 100, 100);
    
    inputs.addRow(new ShadeDescriptor(SQ_SLOW_FILL1, "Slow KAMA Squeeze Upper", SQ_SLOW_T1_INVIS, SQ_SLOW_B1_INVIS, 
    		Enums.ShadeType.ABOVE, BBSQSlowFill, true, true));
    inputs.addRow(new ShadeDescriptor(SQ_SLOW_FILL2, "Slow KAMA Squeeze Lower", SQ_SLOW_T2_INVIS, SQ_SLOW_B2_INVIS, 
    		Enums.ShadeType.ABOVE, BBSQSlowFill2, true, true));
    
    tab.addGroup(inputs);
    
    

    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////           RUN TIME                                                          /////////
    //////////////////////////////////////////////////////////////////////////////////////////////// 
    
    
    // Describe the runtime settings using a 'StudyDescriptor'
    RuntimeDescriptor desc = new RuntimeDescriptor();
    setRuntimeDescriptor(desc);

    desc.setLabelSettings(KAMA_INPUT, KAMA_PERIOD_F1, KAMA_PERIOD_S1);
    desc.setLabelPrefix("KAMA");
    desc.declarePath(FastKamaValues.KAMA1, KAMA_PATH_F1);
    desc.declarePath(FastKamaValues.KAMA2, KAMA_PATH_F2); 
    desc.declarePath(FastKamaValues.KAMA3, KAMA_PATH_F3); 
    desc.declarePath(FastKamaValues.KAMA4, KAMA_PATH_F4); 
    desc.declarePath(FastKamaValues.KAMA5, KAMA_PATH_F5); 
    desc.declarePath(FastKamaShade.TOP, KAMA_FAST_T_INVIS); 
    desc.declarePath(FastKamaShade.BOTTOM, KAMA_FAST_B_INVIS); 
    
    
    desc.declarePath(SlowKamaValues.mKAMA1, KAMA_PATH_S1);
    desc.declarePath(SlowKamaValues.mKAMA2, KAMA_PATH_S2); 
    desc.declarePath(SlowKamaValues.mKAMA3, KAMA_PATH_S3); 
    desc.declarePath(SlowKamaValues.mKAMA4, KAMA_PATH_S4); 
    desc.declarePath(SlowKamaValues.mKAMA5, KAMA_PATH_S5); 
    desc.declarePath(SlowKamaShade.mTOP, KAMA_SLOW_T_INVIS); 
    desc.declarePath(SlowKamaShade.mBOTTOM, KAMA_SLOW_B_INVIS);    
    desc.exportValue(new ValueDescriptor(FastKamaValues.KAMA1, "KAMA", 
                     new String[] {KAMA_INPUT, KAMA_PERIOD_F1}));
    
    desc.declarePath(BBValues.BB1L, BB_STD_L1);  
    desc.declarePath(BBValues.BB1U, BB_STD_L1);
    desc.declarePath(BBValues.BB2L, BB_STD_L2);
    desc.declarePath(BBValues.BB2U, BB_STD_L2);
    desc.declarePath(BBValues.BB3L, BB_STD_L3);
    desc.declarePath(BBValues.BB3U, BB_STD_L3);
    desc.declarePath(BBValues.BB4L, BB_STD_L4);
    desc.declarePath(BBValues.BB4U, BB_STD_L4);
    //desc.declarePath(BBValues.MID,  BB_PATH);
    desc.declarePath(FastKamaSQ.sqTOP1, SQ_FAST_T1_INVIS);
    desc.declarePath(FastKamaSQ.sqBOTTOM1, SQ_FAST_B1_INVIS);
    desc.declarePath(FastKamaSQ.sqTOP2, SQ_FAST_T2_INVIS);
    desc.declarePath(FastKamaSQ.sqBOTTOM2, SQ_FAST_B2_INVIS);
    desc.declarePath(SlowKamaSQ.sqTOP1v2, SQ_SLOW_T1_INVIS);
    desc.declarePath(SlowKamaSQ.sqBOTTOM1v2, SQ_SLOW_B1_INVIS);
    desc.declarePath(SlowKamaSQ.sqTOP2v2, SQ_SLOW_T2_INVIS);
    desc.declarePath(SlowKamaSQ.sqBOTTOM2v2, SQ_SLOW_B2_INVIS);
    
    
    desc.declarePath(BBValuesv2.BB1Lv2, BB_STD_L1v2);  
    desc.declarePath(BBValuesv2.BB1Uv2, BB_STD_L1v2);
    desc.declarePath(BBValuesv2.BB2Lv2, BB_STD_L2v2);
    desc.declarePath(BBValuesv2.BB2Uv2, BB_STD_L2v2);
    desc.declarePath(BBValuesv2.BB3Lv2, BB_STD_L3v2);
    desc.declarePath(BBValuesv2.BB3Uv2, BB_STD_L3v2);
    desc.declarePath(BBValuesv2.BB4Lv2, BB_STD_L4v2);
    desc.declarePath(BBValuesv2.BB4Uv2, BB_STD_L4v2);
    //desc.declarePath(BBValuesv2.MIDv2,  BB_PATHv2);
    
    desc.declareSignal(SQ_S_MARKERUP, "Slow KAMA Squeeze");
    desc.declareSignal(SQ_F_MARKERUP, "Fast KAMA Squeeze");
    
    setRuntimeDescriptor(desc);
    
  }

  @Override
  public int getMinBars()
  {
	   //return 1000 bars
	    int bars = 4000;
	    return bars;
 
  }

  /** This method calculates the moving average for the given index in the data series. */
  @Override
  protected void calculate(int index, DataContext ctx)
  {
	DataSeries series=ctx.getDataSeries();
	
	
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////          CALCULATION KAMAS                                                  /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
	/** 5 FAST KAMA Indicator Lines */  
	  
	final String[] FastKamaPeriod = {KAMA_PERIOD_F1, KAMA_PERIOD_F2, KAMA_PERIOD_F3, KAMA_PERIOD_F4, KAMA_PERIOD_F5};
	final String[] FastKamaFast = {KAMA_FAST_F1, KAMA_FAST_F2, KAMA_FAST_F3, KAMA_FAST_F4, KAMA_FAST_F5 };
	final String[] FastKamaSlow = {KAMA_SLOW_F1, KAMA_SLOW_F2, KAMA_SLOW_F3, KAMA_SLOW_F4, KAMA_SLOW_F5 }; 
	final String[] FastKamaEnum = {"KAMA1", "KAMA2","KAMA3", "KAMA4", "KAMA5"};
	
	// Get the settings as defined by the user in the study dialog
    //int period = getSettings().getInteger(KAMA_PERIOD_F1);
 
	for(int i = 0; i <= 4; i++)  //Loop 5 times
	{
		FastKamaValues updateFastK = FastKamaValues.valueOf(FastKamaEnum[i]);
		Object input = getSettings().getInput(KAMA_INPUT);
		//debug("Index = " + index + "  i loop value " + i + "input" + input);
		int period = getSettings().getInteger(FastKamaPeriod[i]);
		//debug("Index = " + index + "  i loop value " + i + "period" + period);
		int kaFast = getSettings().getInteger(FastKamaFast[i]);
		int kaSlow = getSettings().getInteger(FastKamaSlow[i]);
		double prevKama = 0.000000000000001;  //initialize the previous Kama
		
		if ((index > period*3) && (series.getDouble(index-1, updateFastK) != null)) {
			prevKama = series.getDouble(index-1, updateFastK);
    		} 
    
		double currentKama=vrUtility.kamaLine(index, ctx, period, kaFast, kaSlow, input, prevKama); // <- KAMA Calculation
		series.setDouble(index, updateFastK, currentKama);  // <- Update the series for graphing
	}
    
	final String[] SlowKamaPeriod = {KAMA_PERIOD_S1, KAMA_PERIOD_S2, KAMA_PERIOD_S3, KAMA_PERIOD_S4, KAMA_PERIOD_S5};
	final String[] SlowKamaFast = {KAMA_FAST_S1, KAMA_FAST_S2, KAMA_FAST_S3, KAMA_FAST_S4, KAMA_FAST_S5 };
	final String[] SlowKamaSlow = {KAMA_SLOW_S1, KAMA_SLOW_S2, KAMA_SLOW_S3, KAMA_SLOW_S4, KAMA_SLOW_S5 }; 
	final String[] SlowKamaEnum = {"mKAMA1", "mKAMA2","mKAMA3", "mKAMA4", "mKAMA5"};
	
	for(int i = 0; i <= 4; i++)  //Loop 5 times
	{
		SlowKamaValues updateSlowK = SlowKamaValues.valueOf(SlowKamaEnum[i]);
		Object input = getSettings().getInput(KAMA_INPUT);
		//debug("Index = " + index + "  i loop value " + i + "input" + input);
		int period = getSettings().getInteger(SlowKamaPeriod[i]);
		//debug("Index = " + index + "  i loop value " + i + "period" + period);
		int kaFast = getSettings().getInteger(SlowKamaFast[i]);
		int kaSlow = getSettings().getInteger(SlowKamaSlow[i]);
		double prevKama = 0.000000000000001;  //initialize the previous Kama
		
		if ((index > period*3) && (series.getDouble(index-1, updateSlowK) != null)) {
			prevKama = series.getDouble(index-1, updateSlowK);
    		} 
    
		double currentKama=vrUtility.kamaLine(index, ctx, period, kaFast, kaSlow, input, prevKama); // <- KAMA Calculation
		series.setDouble(index, updateSlowK, currentKama);  // <- Update the series for graphing
	}
	
	

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////           KAMA SHADING                                                      /////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
	
	// FAST KAMA SHADING
	
	if ((series.getDouble(index, FastKamaValues.KAMA1) > series.getDouble(index, FastKamaValues.KAMA2)) &&
	   (series.getDouble(index, FastKamaValues.KAMA2) > series.getDouble(index, FastKamaValues.KAMA3)) &&
	   (series.getDouble(index, FastKamaValues.KAMA3) > series.getDouble(index, FastKamaValues.KAMA4)) &&
	   (series.getDouble(index, FastKamaValues.KAMA4) > series.getDouble(index, FastKamaValues.KAMA5)))
	    {
		series.setDouble(index, FastKamaShade.TOP, series.getDouble(index, FastKamaValues.KAMA1));
		series.setDouble(index, FastKamaShade.BOTTOM, series.getDouble(index, FastKamaValues.KAMA5));
		series.setBoolean(index, KamaPerfect.BULL, true); //set Bull Flag
		series.setBoolean(index, KamaPerfect.BEAR, false); //
		
		
		} else if 
	   ((series.getDouble(index, FastKamaValues.KAMA1) < series.getDouble(index, FastKamaValues.KAMA2)) &&
	   (series.getDouble(index, FastKamaValues.KAMA2) < series.getDouble(index, FastKamaValues.KAMA3)) &&
	   (series.getDouble(index, FastKamaValues.KAMA3) < series.getDouble(index, FastKamaValues.KAMA4)) &&
	   (series.getDouble(index, FastKamaValues.KAMA4) < series.getDouble(index, FastKamaValues.KAMA5))) 
		{
		 series.setDouble(index, FastKamaShade.TOP, series.getDouble(index, FastKamaValues.KAMA1));
	     series.setDouble(index, FastKamaShade.BOTTOM, series.getDouble(index, FastKamaValues.KAMA5));
	     series.setBoolean(index, KamaPerfect.BULL, false); //set Bull Flag
		 series.setBoolean(index, KamaPerfect.BEAR, true); //
		 //if (series.getBoolean(index, KamaPerfect.BEAR) == null) debug("Bear Null Value");
		 //info("K2 Index =" + index + "  Bear  = " + series.getBoolean(index, KamaPerfect.BEAR));
		 
		} else {
		 series.setDouble(index, FastKamaShade.TOP, series.getDouble(index, FastKamaValues.KAMA3));
		 series.setDouble(index, FastKamaShade.BOTTOM, series.getDouble(index, FastKamaValues.KAMA3));
		 series.setBoolean(index, KamaPerfect.BULL, false); //set Bull Flag
		 series.setBoolean(index, KamaPerfect.BEAR, false);
		 
		}
	
	// SLOW KAMA SHADING
	
	if ((series.getDouble(index, SlowKamaValues.mKAMA1) > series.getDouble(index, SlowKamaValues.mKAMA2)) &&
	   (series.getDouble(index, SlowKamaValues.mKAMA2) > series.getDouble(index, SlowKamaValues.mKAMA3)) &&
	   (series.getDouble(index, SlowKamaValues.mKAMA3) > series.getDouble(index, SlowKamaValues.mKAMA4)) &&
	   (series.getDouble(index, SlowKamaValues.mKAMA4) > series.getDouble(index, SlowKamaValues.mKAMA5)))
	    {
		series.setDouble(index, SlowKamaShade.mTOP, series.getDouble(index, SlowKamaValues.mKAMA1));
		series.setDouble(index, SlowKamaShade.mBOTTOM, series.getDouble(index, SlowKamaValues.mKAMA5));

		} else if 
	   ((series.getDouble(index, SlowKamaValues.mKAMA1) < series.getDouble(index, SlowKamaValues.mKAMA2)) &&
	   (series.getDouble(index, SlowKamaValues.mKAMA2) < series.getDouble(index, SlowKamaValues.mKAMA3)) &&
	   (series.getDouble(index, SlowKamaValues.mKAMA3) < series.getDouble(index, SlowKamaValues.mKAMA4)) &&
	   (series.getDouble(index, SlowKamaValues.mKAMA4) < series.getDouble(index, SlowKamaValues.mKAMA5))) 
		{
		 series.setDouble(index, SlowKamaShade.mTOP, series.getDouble(index, SlowKamaValues.mKAMA1));
	     series.setDouble(index, SlowKamaShade.mBOTTOM, series.getDouble(index, SlowKamaValues.mKAMA5));
		} else {
		 series.setDouble(index, SlowKamaShade.mTOP, series.getDouble(index, SlowKamaValues.mKAMA3));
		 series.setDouble(index, SlowKamaShade.mBOTTOM, series.getDouble(index, SlowKamaValues.mKAMA3));
		}
	
	//FAST Kama BBand Calculations
	
    Object bbinput= getSettings().getInput(BB_INPUT);
    int bbPeriod = getSettings().getInteger(BB_PERIOD);
    Enums.MAMethod bbmethod = getSettings().getMAMethod(BB_METHOD, Enums.MAMethod.EMA);
    double bbStd1 = getSettings().getDouble(BB_STD_1);
    double bbStd2 = getSettings().getDouble(BB_STD_2);
    double bbStd3 = getSettings().getDouble(BB_STD_3);
    double bbStd4 = getSettings().getDouble(BB_STD_4);
    
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
    	Double bbStdDevs = series.std(index, bbPeriod, FastKamaValues.valueOf(kamastring));  //std dev on Kama
    	Double bbStdDevma = series.getDouble(index, FastKamaValues.valueOf(kamastring));
	    //Double bbStdDevma = series.ma(bbmethod, z, bbPeriod, FastKamaValues.KAMA5);  //placed statically
 
    	if (bbStdDevs == null) bbStdDevs = 0.0;
    	if (bbStdDevma == null) bbStdDevma = 0.0;
    	
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
	    series.setDouble(index, BBValues.KAMASTDEV, bbStdDevs);
	
	  //SLOW Kama BBand Calculations
	
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
	    bbStdDevs = series.std(index, bbPeriod, SlowKamaValues.valueOf(kamastring)); 
    	bbStdDevma = series.getDouble(index, SlowKamaValues.valueOf(kamastring));
	
    	if (bbStdDevs == null) bbStdDevs = 0.0;
    	if (bbStdDevma == null) bbStdDevma = 0.0;
    	
	    series.setDouble(index, BBValuesv2.BB1Uv2, bbStdDevma + bbStdDevs * bbStd1);
	    series.setDouble(index, BBValuesv2.BB1Lv2, bbStdDevma - bbStdDevs* bbStd1);
	    series.setDouble(index, BBValuesv2.BB2Uv2, bbStdDevma + bbStdDevs* bbStd2);
	    series.setDouble(index, BBValuesv2.BB2Lv2, bbStdDevma - bbStdDevs* bbStd2);
	    series.setDouble(index, BBValuesv2.BB3Uv2, bbStdDevma + bbStdDevs* bbStd3);
	    series.setDouble(index, BBValuesv2.BB3Lv2, bbStdDevma - bbStdDevs* bbStd3);
	    series.setDouble(index, BBValuesv2.BB4Uv2, bbStdDevma + bbStdDevs* bbStd4);
	    series.setDouble(index, BBValuesv2.BB4Lv2, bbStdDevma - bbStdDevs* bbStd4);
	    series.setDouble(index, BBValuesv2.MIDv2, bbStdDevma);
	    series.setDouble(index, BBValuesv2.KAMASTDEV2, bbStdDevs);
	    
    	
	    ////////////////////////////////////////////////////////////////////////////////////////////////
	    //////////          FAST KAMA SQUEEZE LOGIC                                            /////////
	    ////////////////////////////////////////////////////////////////////////////////////////////////  
	    
		int sqNorm= getSettings().getInteger(SQ_NORM);
	    double sqThres = getSettings().getDouble(SQ_THRESHOLD);
	    Double sqCurStdDev = series.getDouble(index, BBValues.KAMASTDEV);
	    
	    //get the maximum value
	    Double maxValue = series.getDouble(index - 1, BBValues.KAMASTDEV);
	    for (int z = index - sqNorm; z <= index; z++) {
	        if(series.getDouble(z, BBValues.KAMASTDEV) == null || maxValue == null) continue;
	    	if (series.getDouble(z, BBValues.KAMASTDEV) > maxValue) {
	            maxValue = series.getDouble(z, BBValues.KAMASTDEV);
	        }
	    }
	  
	    //get the minimum value
	    //Double minValue = series.getDouble(index - 1, BBValues.KAMASTDEV);
	    Double minValue = 0.5;
	    for (int z = index - sqNorm; z <= index; z++) {
	    	if(series.getDouble(z, BBValues.KAMASTDEV) == null || minValue == null ) continue;
	        if (series.getDouble(z, BBValues.KAMASTDEV) < minValue) {         /*  *******/
	            minValue = series.getDouble(z, BBValues.KAMASTDEV);
	        }
	    }	    
	    
	    
	    //rounds the values for comparison and performs the Normalized calculation
	    if (minValue != null && maxValue != null && sqCurStdDev != null && index > 3*sqNorm)
	    {
	    	//minValue = Math.round(minValue * 100000d) / 100000d;
	    	//maxValue = Math.round(maxValue * 100000d) / 100000d;
	    	//sqCurStdDev = Math.round(sqCurStdDev * 100000d) / 100000d;
	    	
	    	//debug("Min -> " + minValue + " Max -> " + maxValue + " Current -> " + sqCurStdDev);
	    	
	    	if (maxValue > minValue) // && sqCurStdDev > minValue) 
	    	{
	    		series.setDouble(index, BBValues.NORMALIZED, 100 * ((sqCurStdDev - minValue) / (maxValue - minValue)));
	    	}
	    	else
	    	{
	    		series.setDouble(index, BBValues.NORMALIZED, 0.0);	
	    	}    	
	    	
	    	if (series.getDouble(index-1, BBValues.NORMALIZED) == null) series.setDouble(index-1, BBValues.NORMALIZED, null);
	    	
		    if (    series.getDouble(index-1, BBValues.NORMALIZED) != null &&
		    		series.getDouble(index, BBValues.NORMALIZED) != null &&
		    		series.getDouble(index, BBValues.NORMALIZED) < sqThres && 
		    		series.getDouble(index-1, BBValues.NORMALIZED) > sqThres)
		    {
		        /*
		    	debug("@ " + index + " SQUEEZE IS ON");
		    	debug("@ " + index + " Current-> " + series.getDouble(index, BBValues.NORMALIZED) + " Previous -> " + series.getDouble(index-1, BBValues.NORMALIZED));
		    	minValue = Math.round(minValue * 100000d) / 100000d;
		    	maxValue = Math.round(maxValue * 100000d) / 100000d;
		    	sqCurStdDev = Math.round(sqCurStdDev * 100000d) / 100000d;
		    	debug("Min -> " + minValue + " Max -> " + maxValue + " Current -> " + sqCurStdDev);
		    	*/
		    	
		    	Coordinate cHi = new Coordinate(series.getStartTime(index), series.getDouble(index, BBValues.BB1U));
		    	Coordinate cLo = new Coordinate(series.getStartTime(index), series.getDouble(index, BBValues.BB1L));
		    	MarkerInfo markerUP = getSettings().getMarker(SQ_F_MARKERUP);
		    	MarkerInfo markerDN = getSettings().getMarker(SQ_F_MARKERDN);
		    	if (markerUP.isEnabled()) addFigure(new Marker(cHi, Enums.Position.TOP, markerUP));
		    	if (markerDN.isEnabled()) addFigure(new Marker(cLo, Enums.Position.BOTTOM, markerDN));
		    	ctx.signal(index, SQ_F_MARKERUP, "Fast KAMA Squeeze", round(series.getDouble(index, BBValues.BB1U)));
		    	
		    	series.setDouble(index, FastKamaSQ.sqTOP1, series.getDouble(index, BBValues.BB4U));
				series.setDouble(index, FastKamaSQ.sqBOTTOM1, series.getDouble(index, BBValues.BB1U));
				series.setDouble(index, FastKamaSQ.sqTOP2, series.getDouble(index, BBValues.BB1L));
				series.setDouble(index, FastKamaSQ.sqBOTTOM2, series.getDouble(index, BBValues.BB4L));
		    	
		    }
		    else if (	series.getDouble(index-1, BBValues.NORMALIZED) != null &&
		    			series.getDouble(index, BBValues.NORMALIZED) != null &&
		    			series.getDouble(index, BBValues.NORMALIZED) < sqThres && 
		    			series.getDouble(index-1, BBValues.NORMALIZED) < sqThres)
				    {
				    	series.setDouble(index, FastKamaSQ.sqTOP1, series.getDouble(index, BBValues.BB4U));
						series.setDouble(index, FastKamaSQ.sqBOTTOM1, series.getDouble(index, BBValues.BB1U));
						series.setDouble(index, FastKamaSQ.sqTOP2, series.getDouble(index, BBValues.BB1L));
						series.setDouble(index, FastKamaSQ.sqBOTTOM2, series.getDouble(index, BBValues.BB4L));
				    }
		    else 
				    {
			    	series.setDouble(index, FastKamaSQ.sqTOP1, series.getDouble(index, BBValues.BB1U));
					series.setDouble(index, FastKamaSQ.sqBOTTOM1, series.getDouble(index, BBValues.BB1U));
					series.setDouble(index, FastKamaSQ.sqTOP2, series.getDouble(index, BBValues.BB1L));
					series.setDouble(index, FastKamaSQ.sqBOTTOM2, series.getDouble(index, BBValues.BB1L));
				    }
	    
	    }
				////////////////////////////////////////////////////////////////////////////////////////////////
				//////////          SLOW KAMA SQUEEZE LOGIC                                                      /////////
				////////////////////////////////////////////////////////////////////////////////////////////////  
				
				sqNorm= getSettings().getInteger(SQ_NORM_Slow);
				sqThres = getSettings().getDouble(SQ_THRESHOLD_Slow);
				sqCurStdDev = series.getDouble(index, BBValuesv2.KAMASTDEV2);
				
				//get the maximum value
				maxValue = series.getDouble(index - 1, BBValuesv2.KAMASTDEV2);
				for (int z = index - sqNorm; z <= index; z++) {
				if(series.getDouble(z, BBValuesv2.KAMASTDEV2) == null || maxValue == null) continue;
				if (series.getDouble(z, BBValuesv2.KAMASTDEV2) > maxValue) {
				maxValue = series.getDouble(z, BBValuesv2.KAMASTDEV2);
				}
				}
				
				//get the minimum value
				//minValue = series.getDouble(index - 1, BBValuesv2.KAMASTDEV2);
				minValue = 0.5;
				
				for (int z = index - sqNorm; z <= index; z++) {
					if(series.getDouble(z, BBValuesv2.KAMASTDEV2) == null || minValue == null ) continue;
					if(series.getDouble(z, BBValuesv2.KAMASTDEV2) < minValue) { /*  *******/
						minValue = series.getDouble(z, BBValuesv2.KAMASTDEV2);
					}
				}	    
				
				
				//rounds the values for comparison and performs the Normalized calculation
				if (minValue != null && maxValue != null && sqCurStdDev != null && index > 3*sqNorm)
				{
				//minValue = Math.round(minValue * 100000d) / 100000d;
				//maxValue = Math.round(maxValue * 100000d) / 100000d;
				//sqCurStdDev = Math.round(sqCurStdDev * 100000d) / 100000d;
				
				//debug("Min -> " + minValue + " Max -> " + maxValue + " Current -> " + sqCurStdDev);
				
				if (maxValue > minValue) // && sqCurStdDev > minValue) 
				{
				series.setDouble(index, BBValuesv2.NORMALIZEDv2, 100 * ((sqCurStdDev - minValue) / (maxValue - minValue)));
				}
				else
				{
				series.setDouble(index, BBValuesv2.NORMALIZEDv2, 0.0);	
				}    	
				
				if (series.getDouble(index-1, BBValuesv2.NORMALIZEDv2) == null) series.setDouble(index-1, BBValuesv2.NORMALIZEDv2, null);
				
				if (    series.getDouble(index-1, BBValuesv2.NORMALIZEDv2) != null &&
				series.getDouble(index, BBValuesv2.NORMALIZEDv2) != null &&
				series.getDouble(index, BBValuesv2.NORMALIZEDv2) < sqThres && 
				series.getDouble(index-1, BBValuesv2.NORMALIZEDv2) > sqThres)
				{
				/*
				debug("@ " + index + " SQUEEZE IS ON");
				debug("@ " + index + " Current-> " + series.getDouble(index, BBValues.NORMALIZED) + " Previous -> " + series.getDouble(index-1, BBValues.NORMALIZED));
				minValue = Math.round(minValue * 100000d) / 100000d;
				maxValue = Math.round(maxValue * 100000d) / 100000d;
				sqCurStdDev = Math.round(sqCurStdDev * 100000d) / 100000d;
				debug("Min -> " + minValue + " Max -> " + maxValue + " Current -> " + sqCurStdDev);
				*/
				
				Coordinate cHi = new Coordinate(series.getStartTime(index), series.getDouble(index, BBValuesv2.BB1Uv2));
				Coordinate cLo = new Coordinate(series.getStartTime(index), series.getDouble(index, BBValuesv2.BB1Lv2));
				MarkerInfo markerUP = getSettings().getMarker(SQ_S_MARKERUP);
				MarkerInfo markerDN = getSettings().getMarker(SQ_S_MARKERDN);
				if (markerUP.isEnabled()) addFigure(new Marker(cHi, Enums.Position.TOP, markerUP));
				if (markerDN.isEnabled()) addFigure(new Marker(cLo, Enums.Position.BOTTOM, markerDN));
				ctx.signal(index, SQ_S_MARKERUP, "Slow KAMA Squeeze", round(series.getDouble(index, BBValuesv2.BB1Uv2)));
				
				series.setDouble(index, SlowKamaSQ.sqTOP1v2, series.getDouble(index, BBValuesv2.BB4Uv2));
				series.setDouble(index, SlowKamaSQ.sqBOTTOM1v2, series.getDouble(index, BBValuesv2.BB1Uv2));
				series.setDouble(index, SlowKamaSQ.sqTOP2v2, series.getDouble(index, BBValuesv2.BB1Lv2));
				series.setDouble(index, SlowKamaSQ.sqBOTTOM2v2, series.getDouble(index, BBValuesv2.BB4Lv2));
				
				}
				else if (	series.getDouble(index-1, BBValuesv2.NORMALIZEDv2) != null &&
				series.getDouble(index, BBValuesv2.NORMALIZEDv2) != null &&
				series.getDouble(index, BBValuesv2.NORMALIZEDv2) < sqThres && 
				series.getDouble(index-1, BBValuesv2.NORMALIZEDv2) < sqThres)
				{
				series.setDouble(index, SlowKamaSQ.sqTOP1v2, series.getDouble(index, BBValuesv2.BB4Uv2));
				series.setDouble(index, SlowKamaSQ.sqBOTTOM1v2, series.getDouble(index, BBValuesv2.BB1Uv2));
				series.setDouble(index, SlowKamaSQ.sqTOP2v2, series.getDouble(index, BBValuesv2.BB1Lv2));
				series.setDouble(index, SlowKamaSQ.sqBOTTOM2v2, series.getDouble(index, BBValuesv2.BB4Lv2));
				}
				else 
				{
				series.setDouble(index, SlowKamaSQ.sqTOP1v2, series.getDouble(index, BBValuesv2.BB1Uv2));
				series.setDouble(index, SlowKamaSQ.sqBOTTOM1v2, series.getDouble(index, BBValuesv2.BB1Uv2));
				series.setDouble(index, SlowKamaSQ.sqTOP2v2, series.getDouble(index, BBValuesv2.BB1Lv2));
				series.setDouble(index, SlowKamaSQ.sqBOTTOM2v2, series.getDouble(index, BBValuesv2.BB1Lv2));
				}	    
				}
	    
	    
	    
	    
	    
	    
	    
	    
	series.setComplete(index);
  }
 }