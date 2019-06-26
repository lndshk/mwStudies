package study_examples;

import java.awt.Color;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.draw.Marker;
import com.motivewave.platform.sdk.study.*;

import study_examples.vrKAMA4.BBValues;
import study_examples.vrKAMA4.FastKamaSQ;
import study_examples.vrKAMA4.FastKamaValues;
import study_examples.vrKAMA4.SlowKamaShade;
import study_examples.vrKAMA4.SlowKamaValues;

/** KAMA with Squeeze */
@StudyHeader(
    namespace="com.mycompany", 
    id="vrKAMAwSW", 
    name="KAMA (1 Band)",
    desc="Kaufman's Adaptive Moving Average with Volatility Bands",
    menu="W. VanRip",
    underlayByDefault = true,
    overlay=true,
    signals=true)
public class vrKAMAwRenko extends Study 
{
	
//  Variable Names
	enum FastKamaLines { KAMA1, KAMA2, KAMA3, KAMA4, KAMA5, KAMAt };
	enum FastKamaShade { BULLPERFECT, BEARPERFECT};
	
	enum FastBandLines { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID, FastKamaSTD, 
						 FastKamaNORMALIZED, FastBandInvT1, FastBandInvT2, FastBandInvB1, FastBandInvB2};
	
	enum Signals 	   {CROSS_ABOVE_FAST_VOL_TOP, CROSS_BELOW_FAST_VOL_BOTTOM, FAST_BAND_SQUEEZE};
	
	final static String FastKamaPeriod 		= "fastKPeriod";
	final static String FastKamaFastest 	= "fastfastestKPeriod";
	final static String FastKamaSlowest 	= "fastslowestKPeriod";
	final static String FastKamaIncrement 	= "fastslowestKInc";
	final static String FastKamaPath1 		= "fastKPath1";
	final static String FastKamaPath2 		= "fastKPath2";
	final static String FastKamaPath3 		= "fastKPath3";
	final static String FastKamaPath4 		= "fastKPath4";
	final static String FastKamaPath5 		= "fastKPath5";
	final static String FastKamaPath_Inv 	= "fastKPathTop"; //Invisible line for perfect shading
	
	final static String FastKamaBullShade 	= "fastKBullShade";
	final static String FastKamaBearShade 	= "fastKBearShade";
	
	final static String Fast_Band_Input  	 = "Fast_BandInput";
	final static String Fast_Band_Method 	 = "Fast_BandMethod";
	final static String Fast_Band_Period 	 = "Fast_BandPeriod";
	final static String Fast_Band_KAMA_INPUT = "Fast_BandKAMAin";
  
	final static String Fast_Band_STD_1 	 = "Fast_Bandstd1";  //Std Devs
	final static String Fast_Band_STD_2 	 = "Fast_Bandstd2";
	final static String Fast_Band_STD_3 	 = "Fast_Bandstd3";
	final static String Fast_Band_STD_4 	 = "Fast_Bandstd4";
  
	final static String Fast_Band_STD_Path1  = "Fast_BandStdL1"; //Paths
	final static String Fast_Band_STD_Path2  = "Fast_BandStdL2";
	final static String Fast_Band_STD_Path3  = "Fast_BandStdL3";
	final static String Fast_Band_STD_Path4  = "Fast_BandStdL4";
	final static String Fast_Band_PATH       = "Fast_BandPath";
	
	final static String Fast_Band_XmarkerUP  = "Fast_BandxMkUP";
	final static String Fast_Band_XmarkerDN  = "Fast_BandxMkDN";
	
	final static String Fast_SQ_NORM 	  	 = "Fast_SQ_Normal"; //Paths
	final static String Fast_SQ_THRESHOLD 	 = "Fast_SQ_Thres";
	final static String Fast_SQ_MARKERUP  	 = "Fast_SQ_UP";
	final static String Fast_SQ_MARKERDN  	 = "Fast_SQ_DN";
	final static String Fast_SQ_Inv_T1    	 = "Fast_SQ_invT1";
	final static String Fast_SQ_Inv_T2    	 = "Fast_SQ_invT2";
	final static String Fast_SQ_Inv_B1    	 = "Fast_SQ_invB1";
	final static String Fast_SQ_Inv_B2    	 = "Fast_SQ_invB2";
	final static String Fast_SQ_FILL_T    	 = "Fast_SQ_FillT";
	final static String Fast_SQ_FILL_B    	 = "Fast_SQ_FillB";
	
//  Initialize the Settings
	@Override
	  public void initialize(Defaults defaults)
	  {
		SettingsDescriptor sd = new SettingsDescriptor();
	    setSettingsDescriptor(sd);
	    
	    /* NEW TAB */
	    /* KAMA Bands */
	    SettingTab tab = new SettingTab("Fast KAMA");
	    sd.addTab(tab);
	    
	    SettingGroup inputs = new SettingGroup("Fast KAMA Wave Inputs");
	    
	    inputs.addRow(new IntegerDescriptor(FastKamaPeriod, "KAMA Period", 10, 1, 999, 1),
  			  new IntegerDescriptor(FastKamaFastest, "Fast", 2, 1, 999, 1),
  			  new IntegerDescriptor(FastKamaSlowest, "Slow", 30, 1, 999, 1), 
  			  new IntegerDescriptor(FastKamaIncrement, "Fast Increment", 1, 1, 10, 1)); 
	    tab.addGroup(inputs);
	    
	    SettingGroup colors = new SettingGroup("Fast KAMA Wave Colors");
	    colors.addRow(new PathDescriptor(FastKamaPath1, "Line 1", defaults.getOrange(), 0.5f, 
                null, true, true, true));
	    colors.addRow(new PathDescriptor(FastKamaPath2, "Line 2", defaults.getGrey(), 0.5f, 
          	  null, true, true, true));
	    colors.addRow(new PathDescriptor(FastKamaPath3, "Line 3", defaults.getGrey(), 0.5f, 
                null, true, true, true));
	    colors.addRow(new PathDescriptor(FastKamaPath4, "Line 4", defaults.getGrey(), 0.5f, 
                null, true, true, true));
	    colors.addRow(new PathDescriptor(FastKamaPath5, "Line 5", defaults.getBlue(), 0.5f, 
                null, true, true, true));
	    colors.addRow(new PathDescriptor(FastKamaPath_Inv, " ",  null, 1.0f, null, false, true, false));
	    
	    Color upFill = new Color(69, 90, 100, 180);  // KAMA Wave Shading Color
	    Color dnFill = new Color(255, 200, 210, 110);
	    
	    colors.addRow(new ShadeDescriptor(FastKamaBullShade, "Up Wave", FastKamaPath1, FastKamaPath_Inv,
	    		Enums.ShadeType.ABOVE, upFill, true, true));
	    colors.addRow(new ShadeDescriptor(FastKamaBearShade, "Down Wave", FastKamaPath1, FastKamaPath_Inv,
	    		Enums.ShadeType.BELOW, dnFill, true, true));
	    tab.addGroup(colors);
	    
	    
	    /* NEW TAB */
	    /* Volatility Bands */
	    tab = new SettingTab("Vol Bands");
	    sd.addTab(tab);
	    
	    inputs = new SettingGroup("Fast KAMA Volatility Band Inputs");
	    inputs.addRow(new IntegerDescriptor(Fast_Band_Period, "Period ", 100, 1, 300, 1),
	    		        new IntegerDescriptor(Fast_Band_Input, "1 => fastest ", 3, 1, 5, 1),
	    			    new MAMethodDescriptor(Fast_Band_Method, "Method ", Enums.MAMethod.EMA));			   
	    tab.addGroup(inputs);
	    /* Std Deviations */
	    inputs = new SettingGroup("Standard Deviations");
	    inputs.addRow(new DoubleDescriptor(Fast_Band_STD_1, "Std Dev 1", 2.0, .1, 10, .1));  //Std Dev
	    inputs.addRow(new DoubleDescriptor(Fast_Band_STD_2, "Std Dev 2", 2.2, .1, 10, .1));
	    inputs.addRow(new DoubleDescriptor(Fast_Band_STD_3, "Std Dev 3", 2.4, .1, 10, .1));
	    inputs.addRow(new DoubleDescriptor(Fast_Band_STD_4, "Std Dev 4", 2.6, .1, 10, .1));
	    tab.addGroup(inputs);
	    
	    /* Band Colors */
	    colors = new SettingGroup("Line Colors ");
	    //colorsbb.addRow(new PathDescriptor(BB_PATH, "Middle", Color.decode("#ECEFF1"), 1.0f, null, true, true, true));
	    colors.addRow(new PathDescriptor(Fast_Band_STD_Path1, "Std Dev 1", Color.decode("#ECEFF1"), 1.0f, new float [] {3f,3f}, true, true, true));
	    colors.addRow(new PathDescriptor(Fast_Band_STD_Path2, "Std Dev 2", Color.decode("#B0BEC5"), 1.0f, new float [] {3f,3f}, true, true, true));
	    colors.addRow(new PathDescriptor(Fast_Band_STD_Path3, "Std Dev 3", Color.decode("#78909C"), 1.0f, new float [] {3f,3f}, true, true, true));   
	    colors.addRow(new PathDescriptor(Fast_Band_STD_Path4, "Std Dev 4", Color.decode("#546E7A"), 1.0f, new float [] {3f,3f}, true, true, true));
	    tab.addGroup(colors); 
	    
	    /* Band Markers */
	    SettingGroup markers = new SettingGroup("CrossOver Markers");
	    markers.addRow(new MarkerDescriptor(Fast_Band_XmarkerUP, "Price Crossing-Over Volatility Band", 
	            Enums.MarkerType.CIRCLE, Enums.Size.LARGE, defaults.getBlue(), defaults.getLineColor(), true, true));
	    markers.addRow(new MarkerDescriptor(Fast_Band_XmarkerDN, "Price Crossing-Under Volatility Band", 
	            Enums.MarkerType.CIRCLE, Enums.Size.LARGE, defaults.getRed(), defaults.getLineColor(), true, true));
	    tab.addGroup(markers);
	    
	    /* NEW TAB */
	    /* SQUEEZE */
	    tab = new SettingTab("Squeeze");
	    sd.addTab(tab);
	    
	    inputs = new SettingGroup("Volatility Squeeze Inputs");

	    inputs.addRow(new IntegerDescriptor(Fast_SQ_NORM, "Normalization Period", 120, 1, 3000, 1));
	    inputs.addRow(new DoubleDescriptor (Fast_SQ_THRESHOLD, "Squeeze Threshold", 6.0, .01, 100, .01)); 
	    inputs.addRow(new MarkerDescriptor (Fast_SQ_MARKERUP, "Compression Marker", 
	            	  Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
	    inputs.addRow(new MarkerDescriptor(Fast_SQ_MARKERDN, "Compression Marker", 
	    	          Enums.MarkerType.TRIANGLE, Enums.Size.SMALL, defaults.getYellow(), defaults.getLineColor(), true, true));
	    
	    Color FastSQFillTop = new Color(0, 250, 255, 80);
	    Color FastSQFillBottom = new Color(0, 250, 255, 80);
	    
	    inputs.addRow(new ShadeDescriptor(Fast_SQ_FILL_T, "Upper Squeeze Shading", Fast_SQ_Inv_T1, Fast_SQ_Inv_T2, 
	    		      Enums.ShadeType.ABOVE, FastSQFillTop, true, true));
	    inputs.addRow(new ShadeDescriptor(Fast_SQ_FILL_B, "Lower Squeeze Shading", Fast_SQ_Inv_B1, Fast_SQ_Inv_B2, 
	    		      Enums.ShadeType.ABOVE, FastSQFillBottom, true, true));
	    	    
	    inputs.addRow(new PathDescriptor(Fast_SQ_Inv_T1 , " ", Color.decode("#546E7A"), 1.0f, null, false, true, false));
	    inputs.addRow(new PathDescriptor(Fast_SQ_Inv_T2 , " ", Color.decode("#546E7A"), 1.0f, null, false, true, false));
	    inputs.addRow(new PathDescriptor(Fast_SQ_Inv_B1 , " ", Color.decode("#546E7A"), 1.0f, null, false, true, false));
	    inputs.addRow(new PathDescriptor(Fast_SQ_Inv_B2 , " ", Color.decode("#546E7A"), 1.0f, null, false, true, false));
	    
	    tab.addGroup(inputs);   
	    
	    // Graphing settings
	    
	    RuntimeDescriptor desc = new RuntimeDescriptor();
	    
	    desc.declarePath(FastKamaLines.KAMA1, FastKamaPath1);
	    desc.declarePath(FastKamaLines.KAMA2, FastKamaPath2);
	    desc.declarePath(FastKamaLines.KAMA3, FastKamaPath3);
	    desc.declarePath(FastKamaLines.KAMA4, FastKamaPath4);
	    desc.declarePath(FastKamaLines.KAMA5, FastKamaPath5);
	    desc.declarePath(FastKamaLines.KAMAt, FastKamaPath_Inv);
	    
	    desc.declarePath(FastBandLines.BB1L, Fast_Band_STD_Path1);  
	    desc.declarePath(FastBandLines.BB1U, Fast_Band_STD_Path1);
	    desc.declarePath(FastBandLines.BB2L, Fast_Band_STD_Path2);
	    desc.declarePath(FastBandLines.BB2U, Fast_Band_STD_Path2);
	    desc.declarePath(FastBandLines.BB3L, Fast_Band_STD_Path3);
	    desc.declarePath(FastBandLines.BB3U, Fast_Band_STD_Path3);
	    desc.declarePath(FastBandLines.BB4L, Fast_Band_STD_Path4);
	    desc.declarePath(FastBandLines.BB4U, Fast_Band_STD_Path4);    
	    desc.declarePath(FastBandLines.FastBandInvT1, Fast_SQ_Inv_T1);
	    desc.declarePath(FastBandLines.FastBandInvT2, Fast_SQ_Inv_T2);
	    desc.declarePath(FastBandLines.FastBandInvB1, Fast_SQ_Inv_B1);
	    desc.declarePath(FastBandLines.FastBandInvB2, Fast_SQ_Inv_B2);
	    
	    // Signals
	    desc.declareSignal(Signals.CROSS_ABOVE_FAST_VOL_TOP, "Price Piercing Upper Band");
	    desc.declareSignal(Signals.CROSS_BELOW_FAST_VOL_BOTTOM, "Price Piercing Lower Band");
	    desc.declareSignal(Signals.FAST_BAND_SQUEEZE, "Fast Band Compression");
	    
	    setRuntimeDescriptor(desc);
	    
	  }  //end of Initialize Settings
	
	  @Override
	  public int getMinBars()
	  {
		return 200; 
	  }
	
	  @Override
	  public void onBarUpdate(DataContext ctx)
	  {
	    if (!getSettings().isBarUpdates()) return;
	    doUpdate(ctx);
	  }

	  @Override
	  public void onBarClose(DataContext ctx)
	  {
	    doUpdate(ctx);
	  }
	
	  private void doUpdate(DataContext ctx)
	  {
		  int kPeriod = getSettings().getInteger(FastKamaPeriod);
		  int kFast = getSettings().getInteger(FastKamaFastest);
		  int kSlow = getSettings().getInteger(FastKamaSlowest);
		  int kIncre = getSettings().getInteger(FastKamaIncrement);
		  DataSeries series = ctx.getDataSeries();
		  int latest = series.size()-1;
		  
		  double currentKama=kamaLine(latest, ctx, kPeriod, kFast, kSlow, 1); // <- KAMA Calculation
		  series.setDouble(latest, FastKamaLines.KAMA1, currentKama);  // <- Update the series for graphing
		  
		  currentKama=kamaLine(latest, ctx, kPeriod, kFast+(kIncre*1), kSlow, 2); // <- KAMA Calculation
		  series.setDouble(latest, FastKamaLines.KAMA2, currentKama);  // <- Update the series for graphing
		  
		  currentKama=kamaLine(latest, ctx, kPeriod, kFast+(kIncre*2), kSlow, 3); // <- KAMA Calculation
		  series.setDouble(latest, FastKamaLines.KAMA3, currentKama);  // <- Update the series for graphing
		  
		  currentKama=kamaLine(latest, ctx, kPeriod, kFast+(kIncre*3), kSlow, 4); // <- KAMA Calculation
		  series.setDouble(latest, FastKamaLines.KAMA4, currentKama);  // <- Update the series for graphing
		  
		  currentKama=kamaLine(latest, ctx, kPeriod, kFast+(kIncre*4), kSlow, 5); // <- KAMA Calculation
		  series.setDouble(latest, FastKamaLines.KAMA5, currentKama);  // <- Update the series for graphing
		  
		  
		// Volatility Band Calculations
	      int fbPeriod = getSettings().getInteger(Fast_Band_Period);
	      Enums.MAMethod fbMethod = getSettings().getMAMethod(Fast_Band_Method, Enums.MAMethod.EMA);
	      double fbStd1 = getSettings().getDouble(Fast_Band_STD_1);
	      double fbStd2 = getSettings().getDouble(Fast_Band_STD_2);
	      double fbStd3 = getSettings().getDouble(Fast_Band_STD_3);
	      double fbStd4 = getSettings().getDouble(Fast_Band_STD_4);
	      int fbgetkama = getSettings().getInteger(Fast_Band_Input); 
		  String kamastring; 
		    
		    	switch (fbgetkama) {
				    case 1: kamastring = "KAMA1";
				    		break;
				    case 2: kamastring = "KAMA2";
				    		break;
				    case 3: kamastring = "KAMA3";
		    				break;
				    case 4: kamastring = "KAMA4";
		    				break;
		    		default:kamastring = "KAMA5";
		    				break;
		    				}	
		    	
	      Double fbStdDevs = series.std(latest, fbPeriod, FastKamaLines.valueOf(kamastring));  //std dev on Kama
	      //Double bbStdDevma = series.getDouble(i, FastBandLines.valueOf(kamastring));  //old ma method
		  Double fbStdDevma = series.ma(fbMethod, latest, fbPeriod, FastKamaLines.valueOf(kamastring)); 
	 
	      if (fbStdDevs == null) fbStdDevs = 0.0;
	      if (fbStdDevma == null) fbStdDevma = 0.0;

	      series.setDouble( latest, FastBandLines.BB1U, fbStdDevma + fbStdDevs* fbStd1);
	      series.setDouble( latest, FastBandLines.BB1L, fbStdDevma - fbStdDevs* fbStd1);
	      series.setDouble( latest, FastBandLines.BB2U, fbStdDevma + fbStdDevs* fbStd2);
	      series.setDouble( latest, FastBandLines.BB2L, fbStdDevma - fbStdDevs* fbStd2);
	      series.setDouble( latest, FastBandLines.BB3U, fbStdDevma + fbStdDevs* fbStd3);
	      series.setDouble( latest, FastBandLines.BB3L, fbStdDevma - fbStdDevs* fbStd3);
	      series.setDouble( latest, FastBandLines.BB4U, fbStdDevma + fbStdDevs* fbStd4);
	      series.setDouble( latest, FastBandLines.BB4L, fbStdDevma - fbStdDevs* fbStd4);
	      //series.setDouble( latest, FastBandLines.MID, fbStdDevma);  //not used
	      series.setDouble( latest, FastBandLines.FastKamaSTD, fbStdDevs);
		  
	      checkTopBand(ctx, latest);
	      checkBottomBand(ctx, latest);
	      
	  }  //end of doUpdate loop
	
	  //KAMA calculation method
	  public double kamaLine(int latest, DataContext ctx, int period, int kaFast, int kaSlow, int kamaSwitch)
		{ 
		    DataSeries series=ctx.getDataSeries();
		    if (kaSlow < kaFast) return 0.0;
			if (latest < period*2) return 0.0;
			
			Double prevKama = 0.0;
					
		    switch (kamaSwitch) {
			    case 1: prevKama = series.getDouble(latest-1, FastKamaLines.KAMA1);
			    		break;
			    case 2: prevKama = series.getDouble(latest-1, FastKamaLines.KAMA2);
			    		break;
			    case 3: prevKama = series.getDouble(latest-1, FastKamaLines.KAMA3);
	    				break;
			    case 4: prevKama = series.getDouble(latest-1, FastKamaLines.KAMA4);
	    				break;
	    		default: prevKama = series.getDouble(latest-1, FastKamaLines.KAMA5);
	    				break;
		    	}
			
		    if (prevKama == null) prevKama = 0.0;
			
			//double prevKama = series.getDouble(latest-1, FastKamaLines.KAMA1);
		    double fastest = (double)2/(kaFast+1);  //cast the integers to double
		    double slowest = (double)2/(kaSlow+1);
		    
		    double change = Math.abs(series.getDouble(latest-period, Enums.BarInput.CLOSE) - series.getDouble(latest, Enums.BarInput.CLOSE)); 
		    double kamavol = 0.0;
		    // loop to sum last number of periods
			for(int i = 0; i < period; i++) {
				kamavol = kamavol + Math.abs(series.getDouble(latest-i, Enums.BarInput.CLOSE) - series.getDouble(latest-(i+1), Enums.BarInput.CLOSE));
			}
			
			// Prevent divide by zero
			if (kamavol == 0) kamavol = 0.00000000000001;

			// Generate the Efficiency Ratio (ER) and Smoothing Constant (SC)
		    double efficiencyRatio = change/kamavol;
		    double smoothingConstant = Math.pow(((efficiencyRatio*(fastest-slowest))+slowest), 2);

		    // Calculate the current KAMA Values
		    double currentKama = prevKama + smoothingConstant*(series.getDouble(latest, Enums.BarInput.CLOSE) - prevKama);
		    //debug("ln 147, index -> " + latest + "   currentKama -> " + currentKama );
			return currentKama;

		} //end kamaline method
		  
	  //Determine if KAMAs lines are in order
	  private void checkPerfect(DataContext ctx, int i) {
		  DataSeries series=ctx.getDataSeries();
		  Double f1 = series.getDouble(i, FastKamaLines.KAMA1);
		  Double f2 = series.getDouble(i, FastKamaLines.KAMA2);
		  Double f3 = series.getDouble(i, FastKamaLines.KAMA3);
		  Double f4 = series.getDouble(i, FastKamaLines.KAMA4);
		  Double f5 = series.getDouble(i, FastKamaLines.KAMA5);
		  if (f1==null||f2==null||f3==null||f4==null||f5==null) return;
		  
		  if( f1>f2 && f2>f3 && f3>f4 && f4>f5 )
			  {
				series.setDouble(i, FastKamaLines.KAMAt, series.getDouble(i, FastKamaLines.KAMA5));  
			  }
		  else if (f1<f2 && f2<f3 && f3<f4 && f4<f5)
		  	  {
			    series.setDouble(i, FastKamaLines.KAMAt, series.getDouble(i, FastKamaLines.KAMA5));  
		  	  }
		  else
		  	  {
			    series.setDouble(i, FastKamaLines.KAMAt, series.getDouble(i, FastKamaLines.KAMA1));
		  	  }
	     }  //end of checkPerfect
	  
	  //Determine if there is volatility compression (Squeeze)
	  private void checkSqueeze(DataContext ctx, int i) {
		  
		  DataSeries series=ctx.getDataSeries();
		  
		  double sqThreshold     = getSettings().getDouble(Fast_SQ_THRESHOLD);
		  int 	 sqNormalization = getSettings().getInteger(Fast_SQ_NORM);  
		  Double sqCurStdDev     = series.getDouble(i, FastBandLines.FastKamaSTD);
		  Double sqPrevStdDev	 = series.getDouble(i-1, FastBandLines.FastKamaSTD);
		  Double sqPrevNormal	 = series.getDouble(i-1, FastBandLines.FastKamaNORMALIZED);
		  double sqCurNormal     = 0.0;
		  
		  
		  if(sqCurStdDev  == null) sqCurStdDev = 0.0;
		  if(sqPrevStdDev == null) sqPrevStdDev= 0.0;
		  if(sqPrevNormal == null) sqPrevNormal= 0.0;
		  if(i < 2*sqNormalization) return;  //ensure enough data is present
		  
		  //Get the maximum value over the normalization period
		  Double maxValue = sqPrevStdDev;
		  for (int z = i - sqNormalization; z <= i; z++) {
		        if(series.getDouble(z, FastBandLines.FastKamaSTD) == null) series.setDouble(z, FastBandLines.FastKamaSTD, 0.0);
		    	if(series.getDouble(z, FastBandLines.FastKamaSTD) > maxValue) {
		           maxValue = series.getDouble(z, FastBandLines.FastKamaSTD);
		        }
		  }
		  
		  //Get the minimum value over the normalization period
		  Double minValue = sqPrevStdDev; //Double minValue = 0.5;
		  for (int z = i - sqNormalization; z <= i; z++) {
			    if(series.getDouble(z, FastBandLines.FastKamaSTD) == null) series.setDouble(z, FastBandLines.FastKamaSTD, 0.0);
		        if(series.getDouble(z, FastBandLines.FastKamaSTD) < minValue) {        
		           minValue = series.getDouble(z, FastBandLines.FastKamaSTD);
		        }
		  }	    
		        
		  //Performs the Normalized calculation
		 		
		  
		    	if (maxValue > minValue)
		    	{
		    		sqCurNormal = 100 * ((sqCurStdDev - minValue) / (maxValue - minValue));
		    		series.setDouble(i, FastBandLines.FastKamaNORMALIZED, sqCurNormal);
		    	}
   		  //Squeeze Logic
		    	
			    if ( sqCurNormal < sqThreshold && sqPrevNormal > sqThreshold)
				    	{
				        /*
				    	debug("@ " + index + " SQUEEZE IS ON");
				    	debug("@ " + index + " Current-> " + series.getDouble(index, BBValues.NORMALIZED) + " Previous -> " + series.getDouble(index-1, BBValues.NORMALIZED));
				    	minValue = Math.round(minValue * 100000d) / 100000d;
				    	maxValue = Math.round(maxValue * 100000d) / 100000d;
				    	sqCurStdDev = Math.round(sqCurStdDev * 100000d) / 100000d;
				    	debug("Min -> " + minValue + " Max -> " + maxValue + " Current -> " + sqCurStdDev);
				    	*/
				    	
				    	Coordinate cHi = new Coordinate(series.getStartTime(i), series.getDouble(i, FastBandLines.BB1U));
				    	Coordinate cLo = new Coordinate(series.getStartTime(i), series.getDouble(i, FastBandLines.BB1L));
				    	MarkerInfo markerUP = getSettings().getMarker(Fast_SQ_MARKERUP);
				    	MarkerInfo markerDN = getSettings().getMarker(Fast_SQ_MARKERDN);
				    	if (markerUP.isEnabled()) addFigure(new Marker(cHi, Enums.Position.TOP, markerUP));
				    	if (markerDN.isEnabled()) addFigure(new Marker(cLo, Enums.Position.BOTTOM, markerDN));
				    	
				    	ctx.signal(i, Signals.FAST_BAND_SQUEEZE, "Fast KAMA Squeeze", round(series.getDouble(i, FastBandLines.BB1U)));
			    	
					    	series.setDouble(i, FastBandLines.FastBandInvT1, series.getDouble(i, FastBandLines.BB4U));
							series.setDouble(i, FastBandLines.FastBandInvT2, series.getDouble(i, FastBandLines.BB1U));
							series.setDouble(i, FastBandLines.FastBandInvB1, series.getDouble(i, FastBandLines.BB1L));
							series.setDouble(i, FastBandLines.FastBandInvB2, series.getDouble(i, FastBandLines.BB4L));
			    	
			    		}
			    else if (sqCurNormal < sqThreshold && sqPrevNormal< sqThreshold)  //squeeze is still present
					    {
					    	series.setDouble(i, FastBandLines.FastBandInvT1, series.getDouble(i, FastBandLines.BB4U));
							series.setDouble(i, FastBandLines.FastBandInvT2, series.getDouble(i, FastBandLines.BB1U));
							series.setDouble(i, FastBandLines.FastBandInvB1, series.getDouble(i, FastBandLines.BB1L));
							series.setDouble(i, FastBandLines.FastBandInvB2, series.getDouble(i, FastBandLines.BB4L));
					    }
			    else 															  //no squeeze is on
					    {
					    	series.setDouble(i, FastBandLines.FastBandInvT1, series.getDouble(i, FastBandLines.BB1U));
							series.setDouble(i, FastBandLines.FastBandInvT2, series.getDouble(i, FastBandLines.BB1U));
							series.setDouble(i, FastBandLines.FastBandInvB1, series.getDouble(i, FastBandLines.BB1L));
							series.setDouble(i, FastBandLines.FastBandInvB2, series.getDouble(i, FastBandLines.BB1L));
					    }
		  
	     }  //end of checkSqueeze
	  
	  //Check for price above band
	  private void checkTopBand(DataContext ctx, int i)  
	  {
	    DataSeries series = ctx.getDataSeries();
	    Double top = series.getDouble(i, FastBandLines.BB3U);
	    if (top == null) return;
	    Coordinate c = new Coordinate(series.getStartTime(i-1), top);
	    if (crossedAbove(series, i, Enums.BarInput.CLOSE, FastBandLines.BB3U) && !series.getBoolean(i, Signals.CROSS_ABOVE_FAST_VOL_TOP, false)) 
		    {
		      series.setBoolean(i, Signals.CROSS_ABOVE_FAST_VOL_TOP, true);
		      MarkerInfo marker = getSettings().getMarker(Fast_Band_XmarkerUP);
		      String msg = get("Price Piercing Upper Band", format(series.getClose(i)), format(top));
		      if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.TOP, marker, msg));
		      ctx.signal(i, Signals.CROSS_ABOVE_FAST_VOL_TOP, msg, round(series.getClose(i)));
		    }
	  }
	  
	  private void checkBottomBand(DataContext ctx, int i)  
	  {
	    DataSeries series = ctx.getDataSeries();
	    Double bottom = series.getDouble(i, FastBandLines.BB3L);
	    if (bottom == null) return;
	    Coordinate c = new Coordinate(series.getStartTime(i-1), bottom);
	    if (crossedBelow(series, i, Enums.BarInput.CLOSE, FastBandLines.BB3L) && !series.getBoolean(i, Signals.CROSS_BELOW_FAST_VOL_BOTTOM, false)) 
		    {
		      series.setBoolean(i, Signals.CROSS_BELOW_FAST_VOL_BOTTOM, true);
		      MarkerInfo marker = getSettings().getMarker(Fast_Band_XmarkerDN);
		      String msg = get("Price Piercing Lower Band", format(series.getClose(i)), format(bottom));
		      if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
		      ctx.signal(i, Signals.CROSS_BELOW_FAST_VOL_BOTTOM, msg, round(series.getClose(i)));
		    }
	  }
	  
	  //Main Calculate Values Method
	  @Override
	  protected synchronized void calculateValues(DataContext ctx)
	  {
		  int kPeriod = getSettings().getInteger(FastKamaPeriod);
		  int kFast = getSettings().getInteger(FastKamaFastest);
		  int kSlow = getSettings().getInteger(FastKamaSlowest);
		  int kIncre = getSettings().getInteger(FastKamaIncrement);
		  
		  DataSeries series = ctx.getDataSeries();
		  int latest = series.size()-1;
		  
		  boolean updates = getSettings().isBarUpdates();
		  for(int i = kPeriod; i <= latest; i++) {
		      if (series.isComplete(i, FastKamaLines.KAMA1)) continue;
		      if (!updates && !series.isBarComplete(i)) continue;
			  double currentKama=kamaLine(i, ctx, kPeriod, kFast, kSlow, 1); // <- KAMA Calculation
			  series.setDouble(i, FastKamaLines.KAMA1, currentKama);  // <- Update the series for graphing
			  series.setComplete(i, FastKamaLines.KAMA1, i >= 0 && i < latest); // bars are set to complete
		  }
		  
		  for(int i = kPeriod; i <= latest; i++) {
		      if (series.isComplete(i, FastKamaLines.KAMA2)) continue;
		      if (!updates && !series.isBarComplete(i)) continue;
			  double currentKama=kamaLine(i, ctx, kPeriod, kFast+(kIncre*1), kSlow, 2); // <- KAMA Calculation
			  series.setDouble(i, FastKamaLines.KAMA2, currentKama);  // <- Update the series for graphing
			  series.setComplete(i, FastKamaLines.KAMA2, i >= 0 && i < latest); // bars are set to complete
		  }
		  
		  for(int i = kPeriod; i <= latest; i++) {
		      if (series.isComplete(i, FastKamaLines.KAMA3)) continue;
		      if (!updates && !series.isBarComplete(i)) continue;
			  double currentKama=kamaLine(i, ctx, kPeriod, kFast+(kIncre*2), kSlow, 3); // <- KAMA Calculation
			  series.setDouble(i, FastKamaLines.KAMA3, currentKama);  // <- Update the series for graphing
			  series.setComplete(i, FastKamaLines.KAMA3, i >= 0 && i < latest); // bars are set to complete
		  }
		  
		  for(int i = kPeriod; i <= latest; i++) {
		      if (series.isComplete(i, FastKamaLines.KAMA4)) continue;
		      if (!updates && !series.isBarComplete(i)) continue;
			  double currentKama=kamaLine(i, ctx, kPeriod, kFast+(kIncre*3), kSlow, 4); // <- KAMA Calculation
			  series.setDouble(i, FastKamaLines.KAMA4, currentKama);  // <- Update the series for graphing
			  series.setComplete(i, FastKamaLines.KAMA4, i >= 0 && i < latest); // bars are set to complete
		  }
		  
		  for(int i = kPeriod; i <= latest; i++) {
		      if (series.isComplete(i, FastKamaLines.KAMA5)) continue;
		      if (!updates && !series.isBarComplete(i)) continue;
			  double currentKama=kamaLine(i, ctx, kPeriod, kFast+(kIncre*4), kSlow, 5); // <- KAMA Calculation
			  series.setDouble(i, FastKamaLines.KAMA5, currentKama);  // <- Update the series for graphing
			  series.setComplete(i, FastKamaLines.KAMA5, i >= 0 && i < latest); // bars are set to complete
			  
			  checkPerfect(ctx, i);
		  }
		  
		    // Volatility Band Calculations
		    int fbPeriod = getSettings().getInteger(Fast_Band_Period);
		    Enums.MAMethod fbMethod = getSettings().getMAMethod(Fast_Band_Method, Enums.MAMethod.EMA);
		    double fbStd1 = getSettings().getDouble(Fast_Band_STD_1);
		    double fbStd2 = getSettings().getDouble(Fast_Band_STD_2);
		    double fbStd3 = getSettings().getDouble(Fast_Band_STD_3);
		    double fbStd4 = getSettings().getDouble(Fast_Band_STD_4);
		    int fbgetkama = getSettings().getInteger(Fast_Band_Input);
		    
			String kamastring; 
		    
		    	switch (fbgetkama) {
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
		    	
		    for(int i = kPeriod; i <= latest; i++) {	
		    	
	    	Double fbStdDevs = series.std(i, fbPeriod, FastKamaLines.valueOf(kamastring));  //std dev on Kama
	    	//Double bbStdDevma = series.getDouble(i, FastBandLines.valueOf(kamastring));
		    Double fbStdDevma = series.ma(fbMethod, i, fbPeriod, FastKamaLines.valueOf(kamastring));  //placed statically
	 
	    	if (fbStdDevs == null) fbStdDevs = 0.0;
	    	if (fbStdDevma == null) fbStdDevma = 0.0;
	    	
	    	if (series.isComplete(i, FastBandLines.FastKamaSTD)) continue;
		    if (!updates && !series.isBarComplete(i)) continue;
		    //Band input { BB1U, BB2U, BB3U, BB4U, BB1L, BB2L, BB3L, BB4L, MID};
		    series.setDouble(i, FastBandLines.BB1U, fbStdDevma + fbStdDevs* fbStd1);
		    series.setDouble(i, FastBandLines.BB1L, fbStdDevma - fbStdDevs* fbStd1);
		    series.setDouble(i, FastBandLines.BB2U, fbStdDevma + fbStdDevs* fbStd2);
		    series.setDouble(i, FastBandLines.BB2L, fbStdDevma - fbStdDevs* fbStd2);
		    series.setDouble(i, FastBandLines.BB3U, fbStdDevma + fbStdDevs* fbStd3);
		    series.setDouble(i, FastBandLines.BB3L, fbStdDevma - fbStdDevs* fbStd3);
		    series.setDouble(i, FastBandLines.BB4U, fbStdDevma + fbStdDevs* fbStd4);
		    series.setDouble(i, FastBandLines.BB4L, fbStdDevma - fbStdDevs* fbStd4);
		    series.setDouble(i, FastBandLines.MID, fbStdDevma);
		    series.setDouble(i, FastBandLines.FastKamaSTD, fbStdDevs);
		    
		    series.setComplete(i, FastBandLines.BB1U, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB1L, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB2U, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB2L, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB3U, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB3L, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB4U, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.BB4L, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.MID, i >= 0 && i < latest);
		    series.setComplete(i, FastBandLines.FastKamaSTD, i >= 0 && i < latest);
		    
		    checkSqueeze(ctx, i);
			checkTopBand(ctx, i);
			checkBottomBand(ctx, i);
		    }
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
	  }  //end of calculateValues method
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
} //end of Study
