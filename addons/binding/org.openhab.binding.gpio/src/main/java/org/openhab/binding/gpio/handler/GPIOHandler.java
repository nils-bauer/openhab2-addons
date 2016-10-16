/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpio.handler;

import static org.openhab.binding.gpio.GPIOBindingConstants.*;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * The {@link GPIOHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author nils-dib - Initial contribution
 */
public class GPIOHandler extends BaseThingHandler implements GpioPinListenerDigital {

    // create gpio controller instance
    final GpioController gpio = GpioFactory.getInstance();

    GpioPinDigitalInput inputPin = null;

    GpioPinDigitalOutput outputPin = null;

    private Logger logger = LoggerFactory.getLogger(GPIOHandler.class);

    public GPIOHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        String pin = getThing().getConfiguration().get("pin").toString();

        if (command instanceof RefreshType) {
            if (outputPin != null) {
                updateState(channelUID, outputPin.getState().isHigh() ? OnOffType.ON : OnOffType.OFF);
            } else if (inputPin != null) {
                updateState(channelUID, outputPin.getState().isHigh() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
            }
        } else if (channelUID.getId().equals(GPIO_SWITCH)) {
            outputPin.setState(command == OnOffType.ON);
        }

    }

    @Override
    public void initialize() {

        final int pin = Integer.parseInt(getThing().getConfiguration().get("pin").toString());
        logger.debug("PIN: " + pin);
        logger.debug("THING: " + getThing().getLabel());

        if (getThing().getThingTypeUID().equals(THING_TYPE_GPIO_SWITCH)) {
            outputPin = gpio.provisionDigitalOutputPin(getPinByName(pin));
        } else if (getThing().getThingTypeUID().equals(THING_TYPE_GPIO_CONTACT)) {
            inputPin = gpio.provisionDigitalInputPin(getPinByName(pin));
            inputPin.addListener(this);
        }

        updateStatus(ThingStatus.ONLINE);

    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        logger.debug((" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState()));
        updateState(getThing().getChannel(GPIO_CONTACT).getUID(),
                event.getState().isHigh() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
    }

    public Pin getPinByName(int pin) {
        switch (pin) {
            case 0:
                return RaspiPin.GPIO_00;
            case 1:
                return RaspiPin.GPIO_01;
            case 2:
                return RaspiPin.GPIO_02;
            case 3:
                return RaspiPin.GPIO_03;
            case 4:
                return RaspiPin.GPIO_04;
            case 5:
                return RaspiPin.GPIO_05;
            case 6:
                return RaspiPin.GPIO_06;
            case 7:
                return RaspiPin.GPIO_07;
            case 8:
                return RaspiPin.GPIO_08;
            case 9:
                return RaspiPin.GPIO_09;
            case 10:
                return RaspiPin.GPIO_10;
            case 11:
                return RaspiPin.GPIO_11;
            case 12:
                return RaspiPin.GPIO_12;
            case 13:
                return RaspiPin.GPIO_13;
            case 14:
                return RaspiPin.GPIO_14;
            case 15:
                return RaspiPin.GPIO_15;
            case 16:
                return RaspiPin.GPIO_16;
            case 17:
                return RaspiPin.GPIO_17;
            case 18:
                return RaspiPin.GPIO_18;
            case 19:
                return RaspiPin.GPIO_19;
            case 20:
                return RaspiPin.GPIO_20;
            case 21:
                return RaspiPin.GPIO_21;
            case 22:
                return RaspiPin.GPIO_22;
            case 23:
                return RaspiPin.GPIO_23;
            case 24:
                return RaspiPin.GPIO_24;
            case 25:
                return RaspiPin.GPIO_25;
            case 26:
                return RaspiPin.GPIO_26;
            case 27:
                return RaspiPin.GPIO_27;
            case 28:
                return RaspiPin.GPIO_28;
            case 29:
                return RaspiPin.GPIO_29;
            case 30:
                return RaspiPin.GPIO_30;
            case 31:
                return RaspiPin.GPIO_31;
            default:
                return null;
        }

    }
}
