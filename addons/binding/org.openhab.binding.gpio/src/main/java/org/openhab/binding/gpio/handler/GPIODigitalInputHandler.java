package org.openhab.binding.gpio.handler;

import static org.openhab.binding.gpio.GPIOBindingConstants.*;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jpigpio.Alert;
import jpigpio.GPIO;
import jpigpio.JPigpio;
import jpigpio.PigpioException;

public class GPIODigitalInputHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(GPIODigitalInputHandler.class);
    private JPigpio jPigpio = null;
    private GPIO gpio;
    private Date lastChanged;

    public GPIODigitalInputHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        try {
            updateState(getThing().getChannel(THING_TYPE_DIGITAL_INPUT_CHANNEL).getUID(), getValue());
        } catch (PigpioException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        jPigpio = ((PigpioBridgeHandler) getBridge().getHandler()).getJPiGpio();
        try {
            gpio = new GPIO(jPigpio, Integer.parseInt(getConfig().get(GPIO_ID).toString()), 1);
            jPigpio.gpioSetAlertFunc(gpio.getPin(), new Alert() {

                @Override
                public void alert(int gpio, int level, long tick) {
                    logger.debug("GPIO ALERT: gpio " + gpio + " level " + level + " tick " + tick);
                    lastChanged = new Date();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                Date thisChange = new Date();
                                OnOffType state = getValue();
                                Thread.sleep(((BigDecimal) getConfig().get(PULLUP_TIME)).longValue());

                                if ((boolean) getConfig().get(PULLUP_STRICT_MODE)) {
                                    // Check if value changed over time
                                    if (!thisChange.before(lastChanged)) {
                                        updateState(getThing().getChannel(THING_TYPE_DIGITAL_INPUT_CHANNEL).getUID(),
                                                getValue());
                                    }
                                } else {
                                    // Check if value is exact after time
                                    if (state.equals(getValue())) {
                                        updateState(getThing().getChannel(THING_TYPE_DIGITAL_INPUT_CHANNEL).getUID(),
                                                getValue());
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (PigpioException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            updateStatus(ThingStatus.ONLINE);
        } catch (NumberFormatException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "Pin not numeric");
            logger.error(e.getMessage());
        } catch (PigpioException e) {
            if (e.getErrorCode() == PigpioException.PI_BAD_GPIO) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "Bad GPIO Pin");
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                        e.getLocalizedMessage());
            }
            logger.error(e.getMessage());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
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

}