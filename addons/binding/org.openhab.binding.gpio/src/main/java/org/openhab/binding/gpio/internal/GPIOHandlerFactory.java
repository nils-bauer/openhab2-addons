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

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.gpio.handler.GPIOHandler;

/**
 * The {@link GPIOHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author nils-dib - Initial contribution
 */
public class GPIOHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<ThingTypeUID>();

    public GPIOHandlerFactory() {
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_GPIO_SWITCH);
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_GPIO_CONTACT);
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {

        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);

    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_GPIO_SWITCH)) {
            return new GPIOHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_GPIO_CONTACT)) {
            return new GPIOHandler(thing);
        }

        return null;
    }
}
