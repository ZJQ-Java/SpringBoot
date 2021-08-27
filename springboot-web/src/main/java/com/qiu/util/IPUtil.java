package com.qiu.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {

    private static Logger logger = LoggerFactory.getLogger(IPUtil.class);

    /*
     * "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR",
     * "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
     * "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"
     */
    private static final String[] HEADERS_TO_TRY = {"X-Forwarded-For", "X-Real-IP"};

    private IPUtil() {
    }

    /***
     * 获取客户端ip地址(可以穿透代理)
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = null;
        for (String header : HEADERS_TO_TRY) {
            ip = request.getHeader(header);
            if (!StringUtils.isBlank(ip) && !"unknown".equals(ip)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("get ip from header: " + header + " ip=" + ip);
                }
                return getFirstIp(ip);
            }
        }
        return request.getRemoteAddr();
    }

    private static String getFirstIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return ip;
        }
        if (ip.indexOf(',') != -1) {
            return ip.split(",", 3)[0].trim();
        }
        return ip;
    }

    /**
     * 获取服务器IP地址
     *
     * @return
     */
    private static List<String> getServerIpList() {
        List<String> ipList = new ArrayList<>();
        int count = 0;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                if (ni.getInetAddresses().hasMoreElements()) {
                    ip = ni.getInetAddresses().nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        ipList.add(ip.getHostAddress());
                    }
                    if (++count > 20) {
                        break;
                    }
                }
            }
        } catch (SocketException e) {
        }
        ipList.sort(String::compareTo);
        return ipList;
    }

    static List<String> linuxLocalIpList = new ArrayList<>();

    static {
        try {
            List<String> ipList = _getServerIpListForLinux();
            linuxLocalIpList = ipList;
        } catch (Exception e) {
        }
    }

    /**
     * 获取Linux服务器IP地址
     *
     * @return
     */
    public static List<String> getServerIpListForLinux() {
        return new ArrayList<>(linuxLocalIpList);
    }

    private static List<String> _getServerIpListForLinux() {
        List<String> ipList = new ArrayList<>();
        try {
            String[] cmd = new String[]{"iftop"};
            String output = ProcessUtil.process(cmd).output;
            if (output != null) {
                for (String s : output.split("\n")) {
                    if (StringUtils.isEmpty(s)) {
                        continue;
                    }
                    if (!s.startsWith("IP address is: ")) {
                        continue;
                    }
                    s = s.substring("IP address is: ".length());
                    if (s.startsWith("127")) {
                        continue;
                    }
                    ipList.add(s.trim());
                }
            }
        } catch (Exception e) {
        }
        ipList.sort(String::compareTo);
        return ipList;
    }
}
