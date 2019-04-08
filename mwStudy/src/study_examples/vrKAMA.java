package study_examples;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.study.*;


// Kaufman's Adaptive Moving Average. Developed by Perry Kaufman, this indicator is an
// EMA using an Efficiency Ratio to modify the smoothing constant, which ranges from
// a minimum of Fast Length to a maximum of Slow Length. Since this moving average is
// adaptive it tends to follow prices more closely than other MA's.


@StudyHeader(
 namespace="com.mycompany", 
 id="KaMa",
 rb="study_examples.nls.strings", // locale specific strings are loaded from here
 name="vrKaMa",
 label="Kama study",
 desc="Kaufman's Adaptive Moving Average",
 menu="W. VanRip",
 overlay=true,
 studyOverlay=true)

public class vrKAMA extends Study
{
  enum FastKamaValues {KAMA};
  enum Values {MA};
  final static String KAMA_INPUT = "kaInput";
  final static String KAMA_PERIOD= "kaPeriod";
  final static String KAMA_FAST= "kaFast";
  final static String KAMA_SLOW= "kaSlow";
  final static String KAMA_PATH= "kaPath";
  
  
  /** This method initializes the study by doing the following:
      1. Define Settings (Design Time Information)
      2. Define Runtime Information (Label, Path and Exported Value) */
  @Override
  public void initialize(Defaults defaults)
  {
    SettingsDescriptor sd = new SettingsDescriptor();
    setSettingsDescriptor(sd);

    SettingTab tab = new SettingTab("Kama");
    sd.addTab(tab);

    SettingGroup inputs = new SettingGroup("Inputs");

    inputs.addRow(new InputDescriptor(KAMA_INPUT, "Input", Enums.BarInput.CLOSE));
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD, "Period", 10, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_FAST, "Fast", 2, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_SLOW, "Slow", 30, 1, 999, 1));
    tab.addGroup(inputs);
    
    SettingGroup colors = new SettingGroup("Line");
    colors.addRow(new PathDescriptor(KAMA_PATH, get("Path"), null, 1.0f, 
                  null, true, true, false));
    colors.addRow(new PathDescriptor(Inputs.PATH, get("Path"), null, 1.0f, 
            null, true, true, false));
    tab.addGroup(colors);
    
    // Describe the runtime settings using a 'StudyDescriptor'
    RuntimeDescriptor desc = new RuntimeDescriptor();
    
    desc.setLabelSettings(KAMA_INPUT, KAMA_PERIOD);
    desc.setLabelPrefix("KAMA");
    desc.declarePath(FastKamaValues.KAMA, KAMA_PATH);  
    desc.declarePath(Values.MA, Inputs.PATH);
    desc.exportValue(new ValueDescriptor(FastKamaValues.KAMA, "KAMA", 
                     new String[] {KAMA_INPUT, KAMA_PERIOD}));
    
    setRuntimeDescriptor(desc);

  }

  @Override
  public int getMinBars()
  {
    return getSettings().getInteger(KAMA_PERIOD)*2;
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

 
  protected void doUpdate(DataContext ctx)
  {
	  Settings settings = getSettings();
	    if (settings == null) return;
	  Util.calcSeriesMA(ctx, Enums.MAMethod.EMA, settings.getInput(KAMA_INPUT), settings.getInteger(KAMA_PERIOD), 
		        0, Values.MA, true, settings.isBarUpdates());
  }
  
  
  /** This method calculates the KAMA for the given index in the data series. */
  @Override
  protected void calculate(int index, DataContext ctx)
  {
    // Get the settings as defined by the user in the study dialog
    // getSettings() returns a Settings object that contains all
    // of the settings that were configured by the user.
    Object input = getSettings().getInput(KAMA_INPUT);
    int period = getSettings().getInteger(KAMA_PERIOD);
    int kaSlow = getSettings().getInteger(KAMA_SLOW);
    int kaFast = getSettings().getInteger(KAMA_FAST);
    
    if (index < period*2) return; 
    if (kaSlow < kaFast) return;
    DataSeries series = ctx.getDataSeries();
   
    double fastest = (double)2/(kaFast+1);  //cast the integers to double
    double slowest = (double)2/(kaSlow+1);
   
    
    double change = Math.abs(series.getDouble(index-period, input) - series.getDouble(index, input)); 
    double kamavol = 0;
   
	for(int i = 0; i < period; i++) {
		kamavol = kamavol + Math.abs(series.getDouble(index-i, input) - series.getDouble(index-(i+1), input));
	}
	debug("Out of the Loop Index =  " + index + "kamavol = " + kamavol);
	// Prevent div by zero
	if (kamavol == 0) kamavol = 0.0000001;

    double efficiencyRatio = change/kamavol;
    double smoothingConstant = Math.pow(((efficiencyRatio*(fastest-slowest))+slowest), 2);
    //debug("ER = " + efficiencyRatio + "SC = " + smoothingConstant);
   
    double prevKama = 0;
    if (index > period*3) {
    	debug("Index = " + index);
    prevKama = series.getDouble(index-1, FastKamaValues.KAMA);
    }
    debug("Previous Kama = " + prevKama);

    Double currentKama = prevKama + smoothingConstant*(series.getDouble(index, input) - prevKama);
    debug("Current Kama = " + currentKama);
    //debug("Setting KAMA value for index: " + index + " KAMA: " + currentKama);
    
    series.setDouble(index, FastKamaValues.KAMA, currentKama);
    
  }
}
