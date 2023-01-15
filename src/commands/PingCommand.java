package commands;

import javax.json.*;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;

public class PingCommand extends AbstractCommand {

    protected static final String IP_V4 = "IPv4";
    protected static final String IP_V6 = "IPv6";
    protected static final String IP_INVALID = "ip invalid";

    @Override
    public void run() {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            JsonArrayBuilder jsonData = Json.createArrayBuilder();

            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface n = enumeration.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();

                String interfaceNetwork = n.getName();

                JsonArrayBuilder jsonAddresses = Json.createArrayBuilder();
                while (ee.hasMoreElements()) {
                    InetAddress i = ee.nextElement();

                    String ip = i.getHostAddress()
                            .replace("%" + interfaceNetwork, "");
                    jsonAddresses.add(
                            Json.createObjectBuilder()
                                    .add("host", i.getHostName()
                                            .replace("%" + interfaceNetwork, ""))
                                    .add("ip", ip)
                                    .add("ip version", getIpVersion(ip))
                    );
                }

                jsonData.add(
                        Json.createObjectBuilder()
                                .add("interface", interfaceNetwork)
                                .add("mtu", n.getMTU())
                                .add("addresses", jsonAddresses));
            }

            printWriter.println(jsonData.build().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized static String getIpVersion(String ipAddress) {
        if (isIPv4(ipAddress)) {
            return IP_V4;
        }
        if (isIPv6(ipAddress)) {
            return IP_V6;
        }

        return IP_INVALID;
    }

    protected synchronized static boolean isIPv4(String ipAddress) {
        boolean isIPv4 = false;

        if (ipAddress != null) {
            try {
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                isIPv4 = (inetAddress instanceof Inet4Address) && inetAddress.getHostAddress().equals(ipAddress);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        return isIPv4;
    }

    protected synchronized static boolean isIPv6(String ipAddress) {
        boolean isIPv6 = false;

        if (ipAddress != null) {
            try {
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                isIPv6 = (inetAddress instanceof Inet6Address);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        return isIPv6;
    }
}
