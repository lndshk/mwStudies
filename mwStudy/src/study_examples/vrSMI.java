package study_examples;

import java.awt.Color;

import com.motivewave.platform.sdk.common.Coordinate;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Defaults;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Inputs;
import com.motivewave.platform.sdk.common.MarkerInfo;
import com.motivewave.platform.sdk.common.Settings;
import com.motivewave.platform.sdk.common.Util;
import com.motivewave.platform.sdk.common.desc.ColorDescriptor;
import com.motivewave.platform.sdk.common.desc.DoubleDescriptor;
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
import com.motivewave.platform.sdk.study.Study;
import com.motivewave.platform.sdk.study.StudyHeader;

import study_examples.vrKAMAwSQ3.FastKamaLines;
import study_examples.vrKAMAwSQ3.Values;


/** Stochastic Momentum Index */
@StudyHeader(
    namespace="com.motivewave", 
    id="vrSMIcolor", 
    rb="",
    name="vrSMI",
    label="SMI",
    desc="VanRip SMI colors",
    menu="W. VanRip",
    overlay=false,
    studyOverlay=false,
    signals = true,
    requiresBarUpdates = true,
    helpLink="http://www.motivewave.com/studies/stochastic_momentum_index.htm")

public class vrSMI extends Study
{
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
	
	enum Values {MA};
	
	enum SMISignal{SHALLOW_BULL, SHALLOW_BEAR, HFT_BULL, HFT_BEAR};
	
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
	  
	  final static String SMI_LINE1  = "smiL1";  //3/5 - SMI Line Settings
	  final static String SMI_LINE2  = "smiL2";  //4/7
	  final static String SMI_LINE3  = "smiL3";  //5/9
	  final static String SMI_LINE4  = "smiL4";  //6/11
	  final static String SMI_LINE5  = "smiL5";  //7/13
	  final static String SMI_LINE6  = "smiL6";  //8/15
	  final static String SMI_LINE7  = "smiL7";  //9/17
	  final static String SMI_LINE8  = "smiL8";  //10/19
	  final static String SMI_LINE9  = "smiL9";  //11/21
	  final static String SMI_LINE10 = "smiL10"; //12/23
	  final static String SMI_LINE11 = "smiL11"; //13/25
	  final static String SMI_LINE12 = "smiL12"; //14/27
	  final static String SMI_LINE13 = "smiL13"; //15/29
	  final static String SMI_LINE14 = "smiL14"; //16/31
	  final static String SMI_LINE15 = "smiL15"; //17/33
	  final static String SMI_LINE16 = "smiL16"; //18/35
	  final static String SMI_LINE17 = "smiL17"; //19/37
	  final static String SMI_LINE18 = "smiL18"; //20/39
	  final static String SMI_LINE19 = "smiL19"; //21/41
	  final static String SMI_LINE20 = "smiL20"; //22/43
	  final static String SMI_LINE21 = "smiL21"; //23/45
	  final static String SMI_LINE22 = "smiL22"; //24/47
	  final static String SMI_LINE23 = "smiL23"; //25/49
	  
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
      
      
      final static String SMI_TOPGUIDE = "smitopguide";
      final static String SMI_MIDDLEGUIDE = "smimidguide";
      final static String SMI_BOTTOMGUIDE = "smibotguide";
	  
	  final static String HFT_BULL_MARKER = "hftBullMark"; 
	  final static String HFT_BEAR_MARKER = "hftBearMark"; 
	  
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
	      ////////////////////////////////////////////////////////////////////////////////////////////////
	    //////////          INITIALIZE SMI                                                /////////
	     ////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    
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
		
		
		
		tab = new SettingTab("Markers");
	    sd.addTab(tab);
	    
	    SettingGroup markers = new SettingGroup("Strategy Markers");
	    
	   
	    markers.addRow(new MarkerDescriptor(Inputs.UP_MARKER, "Shallow Bull Pullback", 
	        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getGreen(), defaults.getLineColor(), true, true));
	    markers.addRow(new MarkerDescriptor(Inputs.DOWN_MARKER, "Shallow Bear Pullback", 
		        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getRed(), defaults.getLineColor(), true, true));
	    markers.addRow(new MarkerDescriptor(HFT_BULL_MARKER, "HFT Bull Expansion", 
		        Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getPurple(), defaults.getLineColor(), true, true));
		markers.addRow(new MarkerDescriptor(HFT_BEAR_MARKER, "HFT Bear Expansion", 
			    Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getPurple(), defaults.getLineColor(), true, true));
	    tab.addGroup(markers);
	    
		//////    Guidelines for SMI Chart       //////
		
	    SettingGroup guides = new SettingGroup("Guides");
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
		
		//desc.getPricePlot().setLabelSettings(MTF_MULTIPLIER, MTF_BARSIZE);
		//desc.getPricePlot().setLabelPrefix("SMI");
		
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
    
    //Place Markers
    desc.declareSignal(SMISignal.SHALLOW_BULL, "Shallow Bull Pullback");
    desc.declareSignal(SMISignal.SHALLOW_BEAR, "Shallow Bear Pullback");
    desc.declareSignal(SMISignal.HFT_BULL, "HFT Bull Expansion");
    desc.declareSignal(SMISignal.HFT_BEAR, "HFT Bear Expansion");
    
    //debug("TopPixels = " + desc.getTopInsetPixels() + "   Bottom Pixels = " + desc.getBottomInsetPixels());
    setRuntimeDescriptor(desc);
  }
  
  @Override
  public void onLoad(Defaults defaults)
  {
   setMinBars(3000);
  }
  
  
  public void smiLine (int latest, DataContext ctx, int hlPeriod, int maPeriod, SMIVal smiFinal, 
		  SMIValD d, SMIValHL hl, SMIValD_MA dMa, SMIValHL_MA hlMa)
  {
	 
	   if (latest < hlPeriod) return;

	    DataSeries series = ctx.getDataSeries();
	    double HH = series.highest(latest, hlPeriod, Enums.BarInput.HIGH);
	    double LL = series.lowest(latest, hlPeriod, Enums.BarInput.LOW);
	    double M = (HH + LL)/2.0;
	    double D = series.getClose(latest) - M;
	    
	    series.setDouble(latest, d, D);
	    series.setDouble(latest, hl, HH - LL);
	    
	    if (latest < hlPeriod + maPeriod) return;
	    
	    Enums.MAMethod method = getSettings().getMAMethod(Inputs.METHOD);
	    series.setDouble(latest, dMa, series.ma(method, latest, maPeriod, d));
	    series.setDouble(latest, hlMa, series.ma(method, latest, maPeriod, hl));
	    
	    int smoothPeriod= getSettings().getInteger(SMI_SMOOTH);
	    if (latest < hlPeriod + maPeriod + smoothPeriod) return;
	    
	    Double D_SMOOTH = series.ma(method, latest, smoothPeriod, dMa);
	    Double HL_SMOOTH = series.ma(method, latest, smoothPeriod, hlMa);
	    
	    if (D_SMOOTH == null || HL_SMOOTH == null) return;
	    double HL2 = HL_SMOOTH/2;
	    double SMI = 0;
	    if (HL2 != 0) SMI = 100 * (D_SMOOTH/HL2);

	    series.setDouble(latest, smiFinal, SMI);
  }
  
  //Main Calculate Values Method
  @Override
  protected synchronized void calculateValues(DataContext ctx)
  {	  
	  DataSeries series = ctx.getDataSeries();
	  int latest = series.size()-1;
	  boolean updates = getSettings().isBarUpdates();
	  
  	int hlPeriod = getSettings().getInteger(SMI_HL1);
  	int maPeriod = getSettings().getInteger(SMI_MA1);
  	
  	smiLine(latest, ctx, hlPeriod, maPeriod, SMIVal.SMI1, SMIValD.D1, SMIValHL.HL1, SMIValD_MA.D_MA1, SMIValHL_MA.HL_MA1);
	  //need to test
	  for(int i = kPeriod; i <= latest; i++) {
	      if (series.isComplete(i, FastKamaLines.KAMA1)) continue;
	      if (!updates && !series.isBarComplete(i)) continue;
		  double currentKama=kamaLine(i, ctx, kPeriod, kFast, kSlow, 1); // <- KAMA Calculation
		  series.setDouble(i, FastKamaLines.KAMA1, currentKama);  // <- Update the series for graphing
		  series.setComplete(i, FastKamaLines.KAMA1, i >= 0 && i < latest); // bars are set to complete
	  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  }	  
  
  
  
  
  
  
  
  
  
  /*
  @Override  
  protected void calculate(int index, DataContext ctx)
  {
	    	DataSeries series = ctx.getDataSeries(); 
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
	   ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	        
      //Add signal generation code
	        //Short Term Pull-back
	        
	        double fastSig = series.getDouble(index, SMIVal.SMI1);
	        double fastSig2 = series.getDouble(index-1, SMIVal.SMI1);
	        double slowSig = series.getDouble(index, SMIVal.SMI20);  //SMIValmtf.SMI9 
	       
	        //info("Index = " + index + "  fastSig = " + round(fastSig)  + "  fastSig2 = " + round(fastSig2)+ "   slowSig = " + round(slowSig));
	        //debug("Index = " + index + "  KamaPerfect.BEAR = " + series.getBoolean(index, KamaPerfect.BEAR));
	        
	        Coordinate c = new Coordinate(series.getStartTime(index), fastSig);
	        
	        if ((fastSig2 < (double) 50.0) && (fastSig > (double) 50.0)
	   	       && (slowSig < (double) 0))
	        {
	          series.setBoolean(index, SMISignal.SHALLOW_BEAR, true);
	          MarkerInfo marker = getSettings().getMarker(Inputs.DOWN_MARKER);
	          String msg = get("Shallow Bear Pullback", format(fastSig), format(slowSig), format(series.getClose(index)));
	          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.TOP, marker, msg));
	          ctx.signal(index, SMISignal.SHALLOW_BEAR, msg, round(fastSig));
	         
	          
	        }
	        
	        if ((fastSig2 > (double)-50.0) && (fastSig < (double) -50.0)
	             && (slowSig > (double) 0))
	        {
	          series.setBoolean(index, SMISignal.SHALLOW_BULL, true);
	          MarkerInfo marker = getSettings().getMarker(Inputs.UP_MARKER);
	          String msg = get("Shallow Bull Pullback", format(fastSig), format(slowSig), format(series.getClose(index)));
	          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
	          ctx.signal(index, SMISignal.SHALLOW_BULL, msg, round(fastSig));
	         
	        }
	        
	        boolean xCrossAbove = false; 
	        boolean xCrossBelow = false; 
	        
	        for (int j = 30; j > 0; j--) {
	        	
	        	
		        if (crossedAbove(series, index-j, SMIValmtf.mSMI1, SMIValmtf.mSMI12)) {
		        	xCrossAbove = true;
		        	xCrossBelow = false;
		        	}
		        if (crossedBelow(series, index-j, SMIValmtf.mSMI1, SMIValmtf.mSMI12)) {
		        	xCrossAbove = false;
		        	xCrossBelow = true;
		        	}
		       
	        }
	        
	        fastSig = series.getDouble(index, SMIValmtf.mSMI1);
	        slowSig = series.getDouble(index, SMIValmtf.mSMI12);
	        
	        double Delta = Math.abs(fastSig-slowSig);
	        
	        if (xCrossAbove) debug("xcross Delta" + round(Delta) + "  Index = " + index );
	        
	        c = new Coordinate(series.getStartTime(index), fastSig);
	        
	        if (xCrossAbove && (Delta > (double) 5.0))

		        {
		           info("EXPANSION = " + index + "  fastSig = " + round(fastSig) + "   slowSig = " + round(slowSig));
		          series.setBoolean(index, SMISignal.SHALLOW_BULL, true);
		          MarkerInfo marker = getSettings().getMarker(HFT_BULL_MARKER);
		          String msg = get("Bull HTF Expansion", format(fastSig), format(slowSig), format(series.getClose(index)));
		          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
		          ctx.signal(index, SMISignal.HFT_BULL, msg, round(fastSig));  
		        }
	        
	        if (xCrossBelow && (Delta > (double) 5.0))

	        {
	           info("EXPANSION = " + index + "  fastSig = " + round(fastSig) + "   slowSig = " + round(slowSig));
	          series.setBoolean(index, SMISignal.SHALLOW_BEAR, true);
	          MarkerInfo marker = getSettings().getMarker(HFT_BEAR_MARKER);
	          String msg = get("Bear HTF Expansion", format(fastSig), format(slowSig), format(series.getClose(index)));
	          if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
	          ctx.signal(index, SMISignal.HFT_BEAR, msg, round(fastSig));  
	        }
        
	           
	        
    series.setComplete(index); 
  } */

 
  
  
}
