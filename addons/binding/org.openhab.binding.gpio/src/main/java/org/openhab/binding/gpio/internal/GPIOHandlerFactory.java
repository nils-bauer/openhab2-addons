/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpio.internal;

import static org.openhab.binding.gpio.GPIOBindingConstants.*;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.gpio.handler.GPIODigitalInputHandler;
import org.openhab.binding.gpio.handler.GPIODigitalOutputHandler;
import org.openhab.binding.gpio.handler.PigpioBridgeHandler;
import org.openhab.binding.gpio.handler.PigpioLocalBridgeHandler;
import org.openhab.binding.gpio.handler.PigpioRemoteBridgeHandler;

/**
 * The {@link GPIOHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author nils-developersinblue - Initial contribution
 */
public class GPIOHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<ThingTypeUID>();

    public GPIOHandlerFactory() {
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_DIGITAL_INPUT);
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_DIGITAL_OUTPUT);

        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_PIGPIO_LOCAL_BRIDGE);
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_PIGPIO_REMOTE_BRIDGE);
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {

        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);

    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_DIGITAL_INPUT)) {
            return new GPIODigitalInputHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_DIGITAL_OUTPUT)) {
            return new GPIODigitalOutputHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_PIGPIO_LOCAL_BRIDGE)) {
            return new PigpioLocalBridgeHandler((Bridge) thing);
        } else if (thingTypeUID.equals(THING_TYPE_PIGPIO_REMOTE_BRIDGE)) {
            return new PigpioRemoteBridgeHandler((Bridge) thing);
        }
        return null;
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof PigpioBridgeHandler) {

        }
    }
}
