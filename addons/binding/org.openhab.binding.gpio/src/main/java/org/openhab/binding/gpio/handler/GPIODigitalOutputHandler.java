
package org.openhab.binding.gpio.handler;

import static org.openhab.binding.gpio.GPIOBindingConstants.*;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jpigpio.Alert;
import jpigpio.GPIO;
import jpigpio.JPigpio;
import jpigpio.PigpioException;

public class GPIODigitalOutputHandler extends BaseThingHandler {
    private Logger logger = LoggerFactory.getLogger(GPIODigitalOutputHandler.class);
    JPigpio jPigpio = null;
    GPIO gpio;

    public GPIODigitalOutputHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            try {
                updateState(getThing().getChannel(THING_TYPE_DIGITAL_OUTPUT_CHANNEL).getUID(), getValue());
            } catch (PigpioException e) {
                e.printStackTrace();
            }
        } else if (command instanceof OnOffType) {
            OnOffType s = (OnOffType) command;
            try {
                setValue(s);
            } catch (PigpioException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize() {
        jPigpio = ((PigpioBridgeHandler) getBridge().getHandler()).getJPiGpio();
        try {
            gpio = new GPIO(jPigpio, Integer.parseInt(getConfig().get(GPIO_ID).toString()), 0);
            jPigpio.gpioSetAlertFunc(gpio.getPin(), new Alert() {
                @Override
                public void alert(int gpio, int level, long tick) {
                    try {
                        updateState(getThing().getChannel(THING_TYPE_DIGITAL_OUTPUT_CHANNEL).getUID(), getValue());
                    } catch (PigpioException e) {
                        logger.error("Unknown jpigpio exception", e);
                    }
                }
            });
            updateStatus(ThingStatus.ONLINE);
        } catch (NumberFormatException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "Pin not numeric");
            logger.error("Non numeric pin number", e);
        } catch (PigpioException e) {
            if (e.getErrorCode() == PigpioException.PI_BAD_GPIO) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "Bad GPIO Pin");
                logger.error("Bad GPIO Pin", e);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                        e.getLocalizedMessage());
                logger.error("Unknown jpigpio exception", e);
            }
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void thingUpdated(Thing thing) {
        dispose();
        this.thing = thing;
        initialize();
    }

    /**
     * Gets the (inverted) value
     *
     * @return the value
     * @throws PigpioException
     */
    private OnOffType getValue() throws PigpioException {
        return ((boolean) getConfig().get(INVERT)) != gpio.getValue() ? OnOffType.ON : OnOffType.OFF;
    }

    /**
     * Sets the value (inverted)
     *
     * @param onOffType the value
     * @throws PigpioException
     */
    private void setValue(OnOffType onOffType) throws PigpioException {
        gpio.setValue(((boolean) getConfig().get(INVERT)) != (onOffType == OnOffType.ON));
    }
}
