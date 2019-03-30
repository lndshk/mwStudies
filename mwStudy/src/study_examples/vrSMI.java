package study_examples;

import com.motivewave.platform.sdk.common.BarSize;
import com.motivewave.platform.sdk.common.Coordinate;
import com.motivewave.platform.sdk.common.DataContext;
import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Defaults;
import com.motivewave.platform.sdk.common.Enums;
import com.motivewave.platform.sdk.common.Enums.MAMethod;
import com.motivewave.platform.sdk.common.GuideInfo;
import com.motivewave.platform.sdk.common.Inputs;
import com.motivewave.platform.sdk.common.LineInfo;
import com.motivewave.platform.sdk.common.MarkerInfo;
import com.motivewave.platform.sdk.common.Util;
import com.motivewave.platform.sdk.common.desc.BarDescriptor;
import com.motivewave.platform.sdk.common.desc.BarSizeDescriptor;
import com.motivewave.platform.sdk.common.desc.BooleanDescriptor;
import com.motivewave.platform.sdk.common.desc.ColorDescriptor;
import com.motivewave.platform.sdk.common.desc.EnabledDependency;
import com.motivewave.platform.sdk.common.desc.GuideDescriptor;
import com.motivewave.platform.sdk.common.desc.IndicatorDescriptor;
import com.motivewave.platform.sdk.common.desc.DoubleDescriptor;
import com.motivewave.platform.sdk.common.desc.InputDescriptor;
import com.motivewave.platform.sdk.common.desc.IntegerDescriptor;
import com.motivewave.platform.sdk.common.desc.MAMethodDescriptor;
import com.motivewave.platform.sdk.common.desc.MarkerDescriptor;
import com.motivewave.platform.sdk.common.desc.PathDescriptor;
import com.motivewave.platform.sdk.common.desc.SettingGroup;
import com.motivewave.platform.sdk.common.desc.SettingTab;
import com.motivewave.platform.sdk.common.desc.SettingsDescriptor;
import com.motivewave.platform.sdk.common.desc.ShadeDescriptor;
import com.motivewave.platform.sdk.common.desc.ValueDescriptor;
import com.motivewave.platform.sdk.draw.Marker;
import com.motivewave.platform.sdk.study.Plot;
import com.motivewave.platform.sdk.study.RuntimeDescriptor;
import com.motivewave.platform.sdk.study.StudyHeader;

import study_examples.vrSMImain.Values;
//import study_examples.vrSMItester.Signals;

import com.motivewave.platform.sdk.common.X11Colors;

//A Java class that can return multiple values of different types
//encapsulate the items into a new object class
//and returning an object of class. 

//A class that is used to store and return 
//members of different types 

class SMIinput { 
	public int hlPeriod; // To store the High/Low Period
	public int maPeriod; // To store the period of the moving average
	public int smoothPeriod; // To store the smoothingPeriod
	public Enums.MAMethod method; //= getSettings().getMAMethod(Inputs.METHOD);
	
	public DataSeries D; // These values are used for calculating averages and smoothed averages
	public DataSeries HL;
	public DataSeries D_MA;
	public DataSeries HL_MA;
	
	public DataSeries SMIds;  //Output ds = DataSeries and Double = d
	public Double SMId;
    
    
	public SMIinput(int hl, int ma, int sp, Enums.MAMethod meth, 
			DataSeries Dsig, DataSeries HLsig, DataSeries D_MAsig, DataSeries HL_MAsig,
			DataSeries SMIsig, Double SMIdouble)  //add ', DataSeries SIGsig' if I want to add signals
	{ 
		this.hlPeriod = hl; 
		this.maPeriod = ma; 
		this.smoothPeriod = sp; 
		this.method = meth; 
		this.D = Dsig;
		this.HL = HLsig;
		this.D_MA = D_MAsig;
		this.HL_MA = HL_MAsig;
		this.SMIds = SMIsig;
		this.SMId = SMIdouble;

	} 
} 

public class vrSMI {

	public static void getvrSMI(SMIinput a, int index, DataContext ctx)
	{	
		    if (index < a.hlPeriod) return; //return if the index is less than the high period

		    DataSeries series = ctx.getDataSeries();  //data series based on price
		    
		    double HH = series.highest(index, a.hlPeriod, Enums.BarInput.HIGH);
		    double LL = series.lowest(index, a.hlPeriod, Enums.BarInput.LOW);
		    double M = (HH + LL)/2.0;
		    double D = series.getClose(index) - M;
	
		    series.setDouble(index, a.D, D);
		    series.setDouble(index, a.HL , HH - LL);
	
		    if (index < a.hlPeriod + a.maPeriod) return;
	
		    series.setDouble(index, a.D_MA, series.ma(a.method, index, a.maPeriod, a.D));
		    series.setDouble(index, a.HL_MA, series.ma(a.method, index, a.maPeriod, a.HL));
		    
		    if (index < a.hlPeriod + a.maPeriod + a.smoothPeriod) return;
		    
		    Double D_SMOOTH = series.ma(a.method, index, a.smoothPeriod, a.D_MA);
		    Double HL_SMOOTH = series.ma(a.method, index, a.smoothPeriod, a.HL_MA);
		    
		    if (D_SMOOTH == null || HL_SMOOTH == null) return;
		    double HL2 = HL_SMOOTH/2;
		    a.SMId = 0.0; //double SMI = 0;
		    if (HL2 != 0) a.SMId = 100 * (D_SMOOTH/HL2);
		    
		    series.setDouble(index, a.SMIds, a.SMId);
		    // Joe - duplicate question, how can cast a.SMIds so that Values.SMI = a.SMIds in the vrSMImain calculate loop
		    
		    if (!series.isBarComplete(index)) return;
		      
		   
	}

}

