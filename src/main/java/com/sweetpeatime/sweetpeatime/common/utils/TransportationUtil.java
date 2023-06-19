package com.sweetpeatime.sweetpeatime.common.utils;

import com.sweetpeatime.sweetpeatime.common.constant.CommonConstant;

public class TransportationUtil {
    private TransportationUtil(){}
    public static String getVehicleType(double wight){
        if(CommonConstant.VEHICLE_BIKE_MAX_LOAD_KG <= wight){
            return CommonConstant.VEHICLE_BIKE;
        }else{
            return CommonConstant.VEHICLE_CAR;
        }
    }
    public static double getTransportationFee(String vehicleType){
        if(CommonConstant.VEHICLE_BIKE.equals(vehicleType)){
            return CommonConstant.VEHICLE_BIKE_RATE;
        }else{
            return CommonConstant.VEHICLE_CAR_RATE;
        }
    }
}
