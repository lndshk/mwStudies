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
 label="Kama stud",
 desc="Kaufman's Adaptive Moving Average",
 menu="W. VanRip",
 overlay=true,
 studyOverlay=true)
public class vrKAMA extends Study
{
  enum FastKamaValues { KAMA1, KAMA2, KAMA3, KAMA4, KAMA5 };
  
  final static String KAMA_INPUT = "kaInput";
  final static String KAMA_PERIOD_F1 = "kaPeriod";
  final static String KAMA_FAST_F1 = "kaFast";
  final static String KAMA_SLOW_F1 = "kaSlow";
  final static String KAMA_PATH_F1 = "kaPath";
  
  
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

    inputs.addRow(new InputDescriptor(KAMA_INPUT, get("Input"), Enums.BarInput.CLOSE));
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD_F1, get("Period"), 10, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_FAST_F1, get("Fast"), 2, 1, 999, 1),
    			  new IntegerDescriptor(KAMA_SLOW_F1, get("Slow"), 30, 1, 999, 1));
    tab.addGroup(inputs);
    
    SettingGroup colors = new SettingGroup("Line");
    colors.addRow(new PathDescriptor(KAMA_PATH_F1, get("Path"), null, 1.0f, 
                  null, true, true, false));
    tab.addGroup(colors);
    
    // Describe the runtime settings using a 'StudyDescriptor'
    RuntimeDescriptor desc = new RuntimeDescriptor();
    setRuntimeDescriptor(desc);

    desc.getPricePlot().setLabelSettings(KAMA_INPUT, KAMA_PERIOD_F1);
    desc.getPricePlot().setLabelPrefix("KAMA");
    desc.getPricePlot().declarePath(FastKamaValues.KAMA1, KAMA_PATH_F1);  
    desc.exportValue(new ValueDescriptor(FastKamaValues.KAMA1, "KAMA", 
                     new String[] {KAMA_INPUT, KAMA_PERIOD_F1}));
  }

  @Override
  public int getMinBars()
  {
    return getSettings().getInteger(KAMA_PERIOD_F1)*2;
  }

  /** This method calculates the moving average for the given index in the data series. */
  @Override
  protected void calculate(int index, DataContext ctx)
  {
    // Get the settings as defined by the user in the study dialog
    // getSettings() returns a Settings object that contains all
    // of the settings that were configured by the user.
    Object input = getSettings().getInput(KAMA_INPUT);
    int period = getSettings().getInteger(KAMA_PERIOD_F1);
    int kaSlow = getSettings().getInteger(KAMA_SLOW_F1);
    int kaFast = getSettings().getInteger(KAMA_FAST_F1);
    
    if (index < period*2) return; 
    if (kaSlow < kaFast) return;
    DataSeries series = ctx.getDataSeries();
    debug("Starting index = " + index);
   
    double fastest = (double)2/(kaFast+1);  //cast the integers to double
    double slowest = (double)2/(kaSlow+1);
    debug("fastest = " + fastest + "  slowest = " + slowest);
    
    double change = Math.abs(series.getDouble(index-period, input) - series.getDouble(index, input)); 
    double kamavol = 0;
    debug("change = " + change);
	for(int i = 0; i < period; i++) {
		kamavol = kamavol + Math.abs(series.getDouble(index-i, input) - series.getDouble(index-(i+1), input));
		//debug("  i in the loop = " + i + "   kamavol = " + kamavol);
	}
	debug("Out of the Loop Index =  " + index + "kamavol = " + kamavol);
	// Prevent div by zero
	if (kamavol == 0) return;

    double efficiencyRatio = change/kamavol;
    double smoothingConstant = Math.pow(((efficiencyRatio*(fastest-slowest))+slowest), 2);
    debug("ER = " + efficiencyRatio + "SC = " + smoothingConstant);
   
    double prevKama = 0;
    if (index > period*3) {
    prevKama = series.getDouble(index-1, FastKamaValues.KAMA1);
    }
    debug("Previous Kama = " + prevKama);

    Double currentKama = prevKama + smoothingConstant*(series.getDouble(index, input) - prevKama);
    debug("Current Kama = " + currentKama);
    //debug("Setting KAMA value for index: " + index + " KAMA: " + currentKama);
    
    //double currentKama = series.ma(MAMethod.SMA, index, period, input);
    series.setDouble(index, FastKamaValues.KAMA1, currentKama);
    
  }
}
