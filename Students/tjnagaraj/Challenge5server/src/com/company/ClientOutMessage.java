package com.company;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by NAGARAJ on 2/15/2015.
 */
public class ClientOutMessage implements Serializable {
    Double latitude;
    Double longitude;
    Date   date;
    int    serialNumber;
}
