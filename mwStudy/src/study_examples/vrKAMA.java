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
  enum Values { KAMA };
  
  final static String KAMA_PERIOD = "kaPeriod";
  final static String KAMA_INPUT = "kaInput";
  final static String KAMA_METHOD = "kaMethod";
  final static String KAMA_FAST = "kaFast";
  final static String KAMA_SLOW = "kaSlow";
  final static String KAMA_PATH = "kaPath";
  
  
  /** This method initializes the study by doing the following:
      1. Define Settings (Design Time Information)
      2. Define Runtime Information (Label, Path and Exported Value) */
  @Override
  public void initialize(Defaults defaults)
  {
    // Describe the settings that may be configured by the user.
    // Settings may be organized using a combination of tabs and groups.  
    SettingsDescriptor sd = new SettingsDescriptor();
    setSettingsDescriptor(sd);

    SettingTab tab = new SettingTab(get("TAB_GENERAL"));
    sd.addTab(tab);

    SettingGroup inputs = new SettingGroup(get("LBL_INPUTS"));
    // Declare the inputs that are used to calculate the moving average.
    // Note: the 'Inputs' class defines several common input keys.
    // You can use any alpha-numeric string that you like.
    inputs.addRow(new InputDescriptor(KAMA_INPUT, get("Input"), Enums.BarInput.CLOSE));
    inputs.addRow(new IntegerDescriptor(KAMA_PERIOD, get("Period"), 10, 1, 999, 1));
    inputs.addRow(new IntegerDescriptor(KAMA_SLOW, get("Slow"), 2, 1, 999, 1));
    inputs.addRow(new IntegerDescriptor(KAMA_FAST, get("Fast"), 30, 1, 999, 1));
    
    tab.addGroup(inputs);
    
    SettingGroup colors = new SettingGroup(get("TAB_DISPLAY"));
    // Allow the user to change the settings for the path that will
    // draw the moving average on the graph.  In this case, we are going
    // to use the input key Inputs.PATH
    colors.addRow(new PathDescriptor(KAMA_PATH, get("Path"), null, 1.0f, 
                  null, true, true, false));
    tab.addGroup(colors);
    
    // Describe the runtime settings using a 'StudyDescriptor'
    RuntimeDescriptor desc = new RuntimeDescriptor();
    setRuntimeDescriptor(desc);

    // Describe how to create the label.  The label uses the 
    // 'label' attribute in the StudyHeader (see above) and adds the input values
    // defined below to generate a label.
    desc.setLabelSettings(KAMA_INPUT, KAMA_PERIOD);
    // Exported values can be used to display cursor data
    // as well as provide input parameters for other studies, 
    // generate alerts or scan for study patterns (see study scanner).
    desc.exportValue(new ValueDescriptor(Values.KAMA, get("My KAMA"), 
                     new String[] {KAMA_INPUT, KAMA_PERIOD}));
    // MotiveWave will automatically draw a path using the path settings
    // (described above with the key 'Inputs.LINE')  In this case 
    // it will use the values generated in the 'calculate' method
    // and stored in the data series using the key 'Values.MA'
    desc.declarePath(Values.KAMA, KAMA_PATH);
  }

  @Override
  public int getMinBars()
  {
    return getSettings().getInteger(Inputs.PERIOD)*2;
  }

  /** This method calculates the moving average for the given index in the data series. */
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
    
    // In order to calculate the exponential moving average
    // we need at least 'period' points of data
    if (index < period) return; 
    
    // Get access to the data series.  
    // This interface provides access to the historical data as well 
    // as utility methods to make this calculation easier.
    DataSeries series = ctx.getDataSeries();
    
    // This utility method allows us to calculate the Exponential 
    // Moving Average instead of doing this ourselves.
    // The DataSeries interface contains several of these types of methods.
    
    //Double average1 = series.ema(index, period, input);
    //Double average2 = series.sma(index, period, input);
    
    double change = Math.abs(series.getDouble(index-period, input) - series.getDouble(index, input));
    //change from so many periods ago
    
	double kamavol = 0;
	
	for(int i = 0; i < period; i++) {
		kamavol = kamavol + Math.abs(series.getDouble(index-i, input) - series.getDouble(index-(i+1), input));
	}

	// Prevent div by zero
	if (kamavol == 0)
	{
		//Value[0] = Value[1];
		return;
	}

    
    //if (average1 == null || average2 == null) return;
    
   //double ma = average1;
   //ma = (average1 + average2)/2;
    
    // Calculated values are stored in the data series using
    // a key (Values.MA).  The key can be any unique value, but
    // we recommend using an enumeration to organize these within
    // your class.  Notice that in the initialize method we declared
    // a path using this key.
    //debug("Setting MA value for index: " + index + " average: " + ma);
   //series.setDouble(index, Values.MA, ma); 
  }
}
