package com.rnkj.rain.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by francis on 2014/4/23.
 */
public class DeviceInfoUtil {

    public static double latitude = 0.0;
    public static double longitude = 0.0;
    /**
     * 设备ID(IMEI)
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * Gets imsi.
     *
     * @param context the context
     * @return the imsi
     */
    public static String getImsi(Context context) {
        String result = "";
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        result = mTelephonyMgr.getSubscriberId();
        if (TextUtils.isEmpty(result)) result = "0";
        return result;
    }

    public static HashMap<String, String> getDeviceInfo(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("deviceId", getDeviceId(context));
        map.put("imsi", getImsi(context));
        return map;
    }

    /**
     * 获取经度纬度
     *
     * @param context
     */
    public static void getLatAndlng(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        } else {
            LocationListener locationListener = new LocationListener() {
                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {

                }

            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
            }
        }
    }

    /**
     * Gets simCountryIso
     *
     * @param context the context
     * @return the simCountryIso
     */
    public static String getSimCountryIso(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSimCountryIso();
    }

    /**
     * Gets PhoneType
     *
     * @param context the context
     * @return the PhoneType
     */
    public static int getPhoneType(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getPhoneType();
    }

    public static String getPhoneName(Context context) {
        return Build.MODEL;
    }

    /**
     * Gets cpu name.
     *
     * @return the cpu name
     */
    public static String getCpuName() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return "";
    }

    /**
     * Gets SimOperatorName
     *
     * @param context the context
     * @return the SimOperatorName
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSimOperatorName();
    }

    public static String getSimOperator(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSimOperator();
    }

    /**
     * Gets NetworkType
     *
     * @param context the context
     * @return the NetworkType
     */
    public static int getNetworkType(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getNetworkType();
    }

    /**
     * Get Release version
     */
    public static String RELEASE_VERSION = Build.VERSION.RELEASE;


    /**
     * Get Phone number
     *
     * @param context the context
     * @return the Phonenumber
     */
    public static String getPhoneNumber(Context context) {
        String result = "";
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        result = mTelephonyMgr.getLine1Number();
        if (TextUtils.isEmpty(result)) result = "";
        return result;
    }

    /**
     * set userinfo
     *
     * @param context
     * @return userinfo map
     */
    public static HashMap<String, String> getUserInfo(Context context) {

        HashMap<String, String> map = new HashMap<String, String>();
        getLatAndlng(context);
        map.put("deviceId", getDeviceId(context));
        map.put("imsi", getImsi(context));
        map.put("areaCode", getSimCountryIso(context));
        map.put("phoneNetwork", String.valueOf(getNetworkType(context)));
        map.put("PhoneNumber", getPhoneNumber(context));
        map.put("phoneOS", RELEASE_VERSION);
        map.put("phoneType", getPhoneName(context));
        map.put("telecomOperator", getSimOperator(context));
        map.put("phoneCPU", getCpuName());
        map.put("latitude", String.valueOf(latitude));
        map.put("longtitude", String.valueOf(longitude));

        return map;

    }

    public static boolean HasSim(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int absent = manager.getSimState();
        if (1 == absent) {
            return false;
        } else {
            return true;
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString() == null ? "" : inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.i("lijunhua1", ex.toString());
        }
        return null;
    }

    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        if (TextUtils.isEmpty(ip)) ip = "";
        return ip;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static String GetIp(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                //判断当前网络是WIFI还是GPRS
                if (mNetworkInfo.getType() == 1){
                    return getWifiIpAddress(context);
                }else{
                    return getLocalIpAddress();}
            }
            return "";
        }
        return "";
    }

    /**
     *
     * @param ipaddr
     * @return public ip
     */

    public static String GetNetIp(String ipaddr){
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(ipaddr);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection)connection;
            int responseCode = httpConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line );
                inStream.close();
                int index1 = strber.indexOf("[")+1;
                int index2 = strber.indexOf("]");
                String ip = strber.substring(index1,index2);
                return ip;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    static Method systemProperties_get = null;

    static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }

    public static String GetRil_SN(){
        String v = null;
        String sn = "ril.serialnumber";
//        String []propertys = {"ro.boot.serialno", "ro.serialno"};
//        for (String key : propertys){
////			String v = android.os.SystemProperties.get(key);
//            v = getAndroidOsSystemProperties(key);
//            Log.e("", "get " + key + " : " + v);
//        }
        v = getAndroidOsSystemProperties(sn);
        return v;
    }

    public static String GetRo_SN(){
        String v = null;
        String []propertys = {"ro.boot.serialno", "ro.serialno"};
        for (String key : propertys){
//			String v = android.os.SystemProperties.get(key);
            v = getAndroidOsSystemProperties(key);
            Log.e("", "get " + key + " : " + v);
        }
        return v;
    }



    public static String getMacDress(Context context) {
        // WifiManager objects may be null
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    public static String getLocaleLanguage(){
        return Locale.getDefault().getLanguage();
    }


}


