# GPIO
This binding adds GPIO support via the Jpigpio libary for openhab2.
## Features

 - control Digital GPIOs via openhab
 - local and remote PIs as bridges

## Supported Things
### pigpio local bridge
Use this when running openhab on an raspberry pi. (See pigpio install for install instructions)

### pigpio remote bridge
Use this to control the GPIOs of a remote raspberry. (See pigpio install for install instructions)

### digital input thing
Get the digital input of an GPIO pin

### digital output thing
Set a GPIO Pin to on or off
 
## pigpio install instructions
On local and remote PIs you have to install pigpio:
```
sudo apt-get install pigpiod
sudo raspi-config 
```
-> Interfacing Options --> Remote GPIO --> YES --> OK --> Finish
```
sudo systemctl enable pigpiod 
sudo systemctl start pigpiod
```