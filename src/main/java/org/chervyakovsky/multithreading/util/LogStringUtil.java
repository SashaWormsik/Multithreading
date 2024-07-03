package org.chervyakovsky.multithreading.util;

import org.chervyakovsky.multithreading.entity.Terminal;
import org.chervyakovsky.multithreading.entity.Truck;

public class LogStringUtil {
    public static String getStringForLog(Truck truck, Terminal terminal, String time) {
        String string = String.format("\tTruck ID=%3d, \tstate= %11s, \tisPerishable= %5s, \tforLoading= %5s, \tcargo=%3d, \tTERMINAL ID= %4s, time= %12s",
                truck.getTruckId(), truck.getTruckState(), truck.isPerishable(), truck.isForLoading(), truck.getCargoQuantity(),
                terminal == null ? "null" : terminal.getId(), time);
        return string;
    }

}
