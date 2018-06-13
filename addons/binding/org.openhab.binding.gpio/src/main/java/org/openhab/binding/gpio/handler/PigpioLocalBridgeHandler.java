/**
 *
 */
package org.openhab.binding.gpio.handler;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jpigpio.JPigpio;
import jpigpio.PigpioException;
import jpigpio.PigpioSocket;

/**
 * @author nils
 *
 */
public class PigpioLocalBridgeHandler extends PigpioBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(GPIODigitalOutputHandler.class);

    private JPigpio jPigpio;

    public PigpioLocalBridgeHandler(Bridge bridge) {
        super(bridge);

    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }

    @Override
    public void dispose() {
        updateStatus(ThingStatus.UNINITIALIZED);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing local PiGPIO bridge handler.");
        // TODO Use native code (JpigpioC has to be bundled native in OSGI)
        // Till than just use localhost ;P
        // jPigpio = new Pigpio();
        try {
            jPigpio = new PigpioSocket("localhost", 8888);
            updateStatus(ThingStatus.ONLINE);
        } catch (PigpioException e) {
            logger.error("Unknown jPigpio error", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, e.getLocalizedMessage());
        }
    }

    @Override
    public JPigpio getJPiGpio() {
        return jPigpio;
    }
}
