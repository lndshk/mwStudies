//package com.motivewave.platform.study.ma;
package study_examples;

import com.motivewave.platform.sdk.common.DataSeries;
import com.motivewave.platform.sdk.common.Instrument;
import com.motivewave.platform.sdk.order_mgmt.OrderContext;
import com.motivewave.platform.sdk.study.StudyHeader;


/** Moving Average Cross Strategy. This is based of the MACross study and adds the ability to trade. */
@StudyHeader(
  namespace = "com.motivewave",
  id = "SMIpullback",
  rb = "com.motivewave.platform.study.nls.strings",
  name = "Shallow SMI pullback",
  desc = "Pullback off of the SMI",
  menu = "MENU_GENERAL",
  overlay = true,
  signals = true,
  strategy = true,
  autoEntry = false,
  manualEntry = false,
  supportsUnrealizedPL = true,
  supportsRealizedPL = true,
  supportsTotalPL = true,
  supportsPositionType = true,
  helpLink="http://www.motivewave.com/strategies/ma_cross_strategy.htm")
public class SMIpullback extends vrSMI
{
  @Override
  public void onActivate(OrderContext ctx)
  {
    if (getSettings().isEnterOnActivate()) {
      DataSeries series = ctx.getDataContext().getDataSeries();
      
      int ind = series.isLastBarComplete() ? series.size()-1 : series.size()-2;
      Double fastSMI = series.getDouble(ind, SMIVal.SMI1);
      Double slowSMI = series.getDouble(ind, SMIVal.SMI12);
      if (fastSMI == null || slowSMI == null) return;
      int tradeLots = getSettings().getTradeLots();
      float qty = tradeLots *= ctx.getInstrument().getDefaultQuantityAsFloat();

      switch(getSettings().getPositionType()) {
      case LONG: // Only Long Positions are allowed.
        if ((fastSMI < -50) && (slowSMI > 0)) ctx.buy(qty);
        break;
      case SHORT: // Only Short Positions are allowed.
        if ((fastSMI < -50) && (slowSMI > 0)) ctx.sell(qty);
        break;
      default: // Both Long and Short Positions Allowed
        if ((fastSMI < -50) && (slowSMI > 0)) ctx.buy(qty);
        else ctx.sell(qty);
      }
    }
  }

  @Override
  public void onSignal(OrderContext ctx, Object signal)
  {
    /*
	  Instrument instr = ctx.getInstrument();
    float position = ctx.getPositionAsFloat();
    float qty = (getSettings().getTradeLots() * instr.getDefaultQuantityAsFloat());

    switch(getSettings().getPositionType()) {
    case LONG: // Only Long Positions are allowed.
      if (position == 0 && signal == Signals.CROSS_ABOVE) {
        ctx.buy(qty); // Open Long Position
      }
      if (position > 0 && signal == Signals.CROSS_BELOW) {
        ctx.sell(qty); // Close Long Position
      }
      break;
    case SHORT: // Only Short Positions are allowed.
      if (position == 0 && signal == Signals.CROSS_BELOW) {
        ctx.sell(qty); // Open Short Position
      }
      if (position < 0 && signal == Signals.CROSS_ABOVE) {
        ctx.buy(qty); // Close Short Position
      }
      break;
    default: // Both Long and Short Positions Allowed
      qty += Math.abs(position); // Stop and Reverse if there is an open position
      if (position <= 0 && signal == Signals.CROSS_ABOVE) {
        ctx.buy(qty); // Open Long Position
      }
      if (position >= 0 && signal == Signals.CROSS_BELOW) {
        ctx.sell(qty); // Open Short Position
        
        */
      }
    
  }

