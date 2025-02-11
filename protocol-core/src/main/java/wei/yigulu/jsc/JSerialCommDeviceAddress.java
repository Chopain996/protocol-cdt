/*
 * Copyright 2017 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package wei.yigulu.jsc;

import java.net.SocketAddress;

/**
 * A {@link SocketAddress} subclass to wrap the serial port address of a jSerialComm
 * device (e.g. COM1, /dev/ttyUSB0).
 */
public class JSerialCommDeviceAddress extends SocketAddress {

    private static final long serialVersionUID = -2907820090993709523L;

    private final String value;

    /**
     * Creates a JSerialCommDeviceAddress representing the address of the serial port.
     *
     * @param value the address of the device (e.g. COM1, /dev/ttyUSB0, ...)
     */
    public JSerialCommDeviceAddress(String value) {
        this.value = value;
    }

    /**
     * @return The serial port address of the device (e.g. COM1, /dev/ttyUSB0, ...)
     */
    public String value() {
        return value;
    }
}
