package study_examples;

import com.motivewave.platform.sdk.common.*;
import com.motivewave.platform.sdk.common.desc.*;
import com.motivewave.platform.sdk.draw.Marker;
import com.motivewave.platform.sdk.study.*;

@StudyHeader(
 namespace="com.mycompany", 
 id="vrMAMA",
 rb="study_examples.nls.strings", // locale specific strings are loaded from here
 name="vrMAMA",
 label="MAMA Study",
 desc="Ehler's Mother of Adaptive Moving Average",
 menu="W. VanRip",
 overlay=true,
 studyOverlay=true)

public class vrMAMA extends Study
{
	final static String FAST="Fast", SLOW="Slow";

	  protected enum Signals { BUY, SELL }
	  enum Values { SMOOTH, PERIOD, DET, Q1, I1, Q2, I2, RE, IM, SPRD, PHASE, MAMA, FAMA }

	  @Override
	  public void initialize(Defaults defaults)
	  {
	    SettingsDescriptor sd=new SettingsDescriptor();
	    SettingTab tab=new SettingTab(get("TAB_GENERAL"));
	    sd.addTab(tab);
	    setSettingsDescriptor(sd);

	    SettingGroup inputs=new SettingGroup(get("INPUTS"));
	    inputs.addRow(new InputDescriptor(Inputs.INPUT, get("INPUT"), Enums.BarInput.MIDPOINT));
	    inputs.addRow(new DoubleDescriptor(FAST, get("FAST_LIMIT"), .5, 0, 99.01, .01));
	    inputs.addRow(new DoubleDescriptor(SLOW, get("SLOW_LIMIT"), .05, 0, 99.01, .01));
	    tab.addGroup(inputs);

	    SettingGroup settings=new SettingGroup(get("PATHS"));
	    settings.addRow(new PathDescriptor(Inputs.PATH, get("LBL_MAMA"), defaults.getLineColor(), 1.0f, null, true, false, true));
	    settings.addRow(new PathDescriptor(Inputs.PATH2, get("FAMA"), defaults.getRed(), 1.0f, null, true, false, true));
	    tab.addGroup(settings);

	    tab=new SettingTab(get("TAB_DISPLAY"));
	    sd.addTab(tab);

	    settings=new SettingGroup(get("INDICATORS"));
	    settings.addRow(new IndicatorDescriptor(Inputs.IND, get("LBL_MAMA"), defaults.getLineColor(), null, false, true, true));
	    settings.addRow(new IndicatorDescriptor(Inputs.IND2, get("FAMA"), defaults.getRed(), null, false, true, true));
	    tab.addGroup(settings);

	    SettingGroup markers=new SettingGroup(get("MARKERS"));
	    tab.addGroup(markers);
	    markers.addRow(new MarkerDescriptor(Inputs.UP_MARKER, get("UP_MARKER"), Enums.MarkerType.TRIANGLE,
	        Enums.Size.VERY_SMALL, defaults.getGreen(), defaults.getLineColor(), true, true));
	    markers.addRow(new MarkerDescriptor(Inputs.DOWN_MARKER, get("DOWN_MARKER"), Enums.MarkerType.TRIANGLE,
	        Enums.Size.VERY_SMALL, defaults.getRed(), defaults.getLineColor(), true, true));

	    RuntimeDescriptor desc=new RuntimeDescriptor();
	    desc.setLabelSettings(Inputs.INPUT, FAST, SLOW);
	    desc.exportValue(new ValueDescriptor(Values.MAMA, get("LBL_MAMA"), new String[] { Inputs.INPUT, FAST, SLOW }));
	    desc.exportValue(new ValueDescriptor(Signals.SELL, Enums.ValueType.BOOLEAN, get("SELL"), null));
	    desc.exportValue(new ValueDescriptor(Signals.BUY, Enums.ValueType.BOOLEAN, get("BUY"), null));
	    desc.declareSignal(Signals.SELL, get("SELL"));
	    desc.declareSignal(Signals.BUY, get("BUY"));

	    desc.declarePath(Values.MAMA, Inputs.PATH);
	    desc.declarePath(Values.FAMA, Inputs.PATH2);
	    desc.declareIndicator(Values.MAMA, Inputs.IND);
	    desc.declareIndicator(Values.FAMA, Inputs.IND2);

	    desc.setRangeKeys(Values.MAMA, Values.FAMA);
	    setRuntimeDescriptor(desc);
	    setMinBars(25);
	  }

	  @Override
	  protected void calculate(int index, DataContext ctx)
	  {
	    if (index < 3) return;

	    Object key=getSettings().getInput(Inputs.INPUT, Enums.BarInput.MIDPOINT);
	    double fast=getSettings().getDouble(FAST);
	    double slow=getSettings().getDouble(SLOW);

	    DataSeries series=ctx.getDataSeries();

	    double period=0, phase=0, deltaPhase=0, alpha=0;
	    ;
	    double price=series.getDouble(index, key, 0);
	    double prevPrice1=series.getDouble(index - 1, key, 0);
	    double prevPrice2=series.getDouble(index - 2, key, 0);
	    double prevPrice3=series.getDouble(index - 3, key, 0);
	    double prevPrd=series.getDouble(index - 1, Values.PERIOD, 0);

	    double smooth=(4 * price + 3 * prevPrice1 + 2 * prevPrice2 + prevPrice3) / 10.0;
	    series.setDouble(index, Values.SMOOTH, smooth);
	    if (index < 9) return; // 3+6

	    double prevS2=series.getDouble(index - 2, Values.SMOOTH, 0);
	    double prevS4=series.getDouble(index - 4, Values.SMOOTH, 0);
	    double prevS6=series.getDouble(index - 6, Values.SMOOTH, 0);
	    double det=(.0962 * smooth + .5769 * prevS2 - .5769 * prevS4 - .0962 * prevS6) * (.075 * prevPrd + .54);
	    series.setDouble(index, Values.DET, det);
	    if (index < 15) return; // 3+6+6

	    double prevD2=series.getDouble(index - 2, Values.DET, 0);
	    double prevD3=series.getDouble(index - 3, Values.DET, 0);
	    double prevD4=series.getDouble(index - 4, Values.DET, 0);
	    double prevD6=series.getDouble(index - 6, Values.DET, 0);
	    double q1=(.0962 * det + .5769 * prevD2 - .5769 * prevD4 - .0962 * prevD6) * (.075 * prevPrd + .54);
	    double i1=prevD3;
	    series.setDouble(index, Values.Q1, q1);
	    series.setDouble(index, Values.I1, i1);
	    if (index < 21) return; // 3+6+6+6

	    double prevQ1x2=series.getDouble(index - 2, Values.Q1, 0);
	    double prevQ1x4=series.getDouble(index - 4, Values.Q1, 0);
	    double prevQ1x6=series.getDouble(index - 6, Values.Q1, 0);
	    double prevI1x2=series.getDouble(index - 2, Values.I1, 0);
	    double prevI1x4=series.getDouble(index - 4, Values.I1, 0);
	    double prevI1x6=series.getDouble(index - 6, Values.I1, 0);
	    double j1=(.0962 * i1 + .5769 * prevI1x2 - .5769 * prevI1x4 - .0962 * prevI1x6) * (.075 * prevPrd + .54);
	    double jq=(.0962 * q1 + .5769 * prevQ1x2 - .5769 * prevQ1x4 - .0962 * prevQ1x6) * (.075 * prevPrd + .54);

	    double i2=i1 - jq;
	    double q2=q1 + j1;
	    double prevI2=series.getDouble(index - 1, Values.I2, 0);
	    double prevQ2=series.getDouble(index - 1, Values.Q2, 0);
	    i2=.2 * i2 + .8 * prevI2;
	    q2=.2 * q2 + .8 * prevQ2;
	    series.setDouble(index, Values.Q2, q2);
	    series.setDouble(index, Values.I2, i2);

	    double re=i2 * prevI2 + q2 * prevQ2;
	    double im=i2 * prevQ2 - q2 * prevI2;
	    double prevRe=series.getDouble(index - 1, Values.RE, 0);
	    double prevIm=series.getDouble(index - 1, Values.IM, 0);
	    re=.2 * re + .8 * prevRe;
	    im=.2 * im + .8 * prevIm;
	    series.setDouble(index, Values.RE, re);
	    series.setDouble(index, Values.IM, im);
	    
	    period = prevPrd; //added as a start value
	    
	    if (im != 0 && re != 0) period= (4*2*Math.atan(1))/(Math.atan(im / re));
	    
	    if (period > 1.5 * prevPrd) period=1.5 * prevPrd;
	    if (period < .67 * prevPrd) period=.67 * prevPrd;
	    if (period < 6) period=6;
	    if (period > 50) period=50;
	    
	    
	    period= (.2 * period) + (.8 * prevPrd);
	    
	    series.setDouble(index, Values.PERIOD, period);// <= added setting the Period
	    
	    double prevSprd=series.getDouble(index - 1, Values.SPRD, 0);
	    double sPrd=.33 * period + .67 * prevSprd;
	    series.setDouble(index, Values.SPRD, sPrd);

	    if (i1 != 0) phase=180/(4*Math.atan(1))*Math.atan(q1 / i1);
	
	    series.setDouble(index, Values.PHASE, phase);
	    if (index < 22) return; // 3+6+6+6+1

	    double prevPhase=series.getDouble(index - 1, Values.PHASE, 0);
	    deltaPhase=prevPhase - phase;
	    if (deltaPhase < 1) deltaPhase=1;
	    alpha=fast / deltaPhase;
	    if (alpha < slow) alpha=slow;
	    double prevMama=series.getDouble(index - 1, Values.MAMA, 0);
	    double prevFama=series.getDouble(index - 1, Values.FAMA, 0);

	    double mama=alpha * price + (1 - alpha) * prevMama;
	    double fama=.5 * alpha * mama + (1 - .5 * alpha) * prevFama;
	    series.setDouble(index, Values.MAMA, mama);
	    series.setDouble(index, Values.FAMA, fama);

	    // Check for signal events
	    boolean buy=crossedAbove(series, index, Values.MAMA, Values.FAMA);
	    boolean sell=crossedBelow(series, index, Values.MAMA, Values.FAMA);

	    series.setBoolean(index, Signals.SELL, sell);
	    series.setBoolean(index, Signals.BUY, buy);

	    if (sell) {
	      Coordinate c=new Coordinate(series.getStartTime(index), mama);
	      MarkerInfo marker=getSettings().getMarker(Inputs.DOWN_MARKER);
	      String msg = get("SELL_PRICE_MAMA", Util.round(price, 2), Util.round(mama, 3));
	      if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.TOP, marker, msg));
	      ctx.signal(index, Signals.SELL, msg, price);
	    }
	    if (buy) {
	      Coordinate c=new Coordinate(series.getStartTime(index), mama);
	      MarkerInfo marker=getSettings().getMarker(Inputs.UP_MARKER);
	      String msg = get("BUY_PRICE_MAMA", Util.round(price, 2), Util.round(mama, 3));
	      if (marker.isEnabled()) addFigure(new Marker(c, Enums.Position.BOTTOM, marker, msg));
	      ctx.signal(index, Signals.BUY, msg, price);
	    }
	    series.setComplete(index);
	  }
	}