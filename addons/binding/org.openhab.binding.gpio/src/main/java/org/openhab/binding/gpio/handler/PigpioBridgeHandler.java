package org.openhab.binding.gpio.handler;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;

import jpigpio.JPigpio;

public abstract class PigpioBridgeHandler extends BaseBridgeHandler {

    public PigpioBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    public abstract JPigpio getJPiGpio();

}
