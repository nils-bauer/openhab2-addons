/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpio;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link GPIOBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author nils-developersinblue - Initial contribution
 */
public class GPIOBindingConstants {

    public static final String BINDING_ID = "gpio";

    public final static ThingTypeUID THING_TYPE_PIGPIO_LOCAL_BRIDGE = new ThingTypeUID(BINDING_ID,
            "pigpio-local-bridge");
    public final static ThingTypeUID THING_TYPE_PIGPIO_REMOTE_BRIDGE = new ThingTypeUID(BINDING_ID,
            "pigpio-remote-bridge");

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_DIGITAL_INPUT = new ThingTypeUID(BINDING_ID, "gpio-digital-input");
    public final static ThingTypeUID THING_TYPE_DIGITAL_OUTPUT = new ThingTypeUID(BINDING_ID, "gpio-digital-output");

    // List of all Channel ids
    public final static String THING_TYPE_DIGITAL_INPUT_CHANNEL = "gpio-digital-input";
    public final static String THING_TYPE_DIGITAL_OUTPUT_CHANNEL = "gpio-digital-output";

    // Bridge config properties
    public static final String HOST = "ipAddress";
    public static final String PORT = "port";
    public static final String INVERT = "invert";
    public static final String PULLUP_TIME = "pullup_time";
    public static final String PULLUP_STRICT_MODE = "pullup_strict";

    // GPIO config properties
    public static final String GPIO_ID = "gpioId";
}
