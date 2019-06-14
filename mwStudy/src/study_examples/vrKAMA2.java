package study_examples;

import java.awt.Color;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.study.*;
import com.motivewave.platform.sdk.common.Util;


// Kaufman's Adaptive Moving Average. Developed by Perry Kaufman, this indicator is an
// EMA using an Efficiency Ratio to modify the smoothing constant, which ranges from
// a minimum of Fast Length to a maximum of Slow Length. Since this moving average is
// adaptive it tends to follow prices more closely than other MA's.


@StudyHeader(
 namespace="com.mycompany", 
 id="KaMa2",
 rb="study_examples.nls.strings", // locale specific strings are loaded from here
 name="vrKaMa2",
 label="Kama",
 desc="Kaufman's Adaptive Moving Average",
 menu="W. VanRip",
 underlayByDefault = true,
 overlay=true,
 studyOverlay=true)
public class vrKAMA2 extends Study
{
	  enum FastKamaValues { KAMA1, KAMA2, KAMA3, KAMA4, KAMA5 };
	  enum FastKamaShade { TOP, BOTTOM};
	  enum SlowKamaValues { mKAMA1, mKAMA2, mKAMA3, mKAMA4, mKAMA5 };
	  enum SlowKamaShade { mTOP, mBOTTOM};
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
    //////////          CALCULATION KAMAS                                                        /////////
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
	
	series.setComplete(index);
	
  }
}