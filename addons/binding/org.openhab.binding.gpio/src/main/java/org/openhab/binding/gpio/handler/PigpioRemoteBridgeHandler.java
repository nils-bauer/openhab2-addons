/**
 *
 */
package org.openhab.binding.gpio.handler;

import static org.openhab.binding.gpio.GPIOBindingConstants.*;

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
public class PigpioRemoteBridgeHandler extends PigpioBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(GPIODigitalOutputHandler.class);

    private JPigpio jPigpio;

    public PigpioRemoteBridgeHandler(Bridge bridge) {
        super(bridge);

    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }

    @Override
    public void dispose() {
        updateStatus(ThingStatus.UNINITIALIZED);
        logger.debug("Handler disposed.");
    }

    @Override
    public void initialize() {
        logger.debug("Initializing remote PiGPIO bridge handler.");

        if (getConfig().get(HOST) == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                    "Cannot connect to PiGPIO Service on remote raspberry. IP address not set.");
        } else if (getConfig().get(PORT) == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                    "Cannot connect to PiGPIO Service on remote raspberry. Port not set.");
        } else {
            try {
                jPigpio = new PigpioSocket(getConfig().get(HOST).toString(),
                        Integer.parseInt(getConfig().get(PORT).toString()));
                updateStatus(ThingStatus.ONLINE);
            } catch (NumberFormatException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "Port not numeric");
                logger.error(e.getMessage());
            } catch (PigpioException e) {
                if (e.getErrorCode() == PigpioException.PI_BAD_SOCKET_PORT) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                            "Port out of range");
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                            e.getLocalizedMessage());
                }
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public JPigpio getJPiGpio() {
        return jPigpio;
    }
}
