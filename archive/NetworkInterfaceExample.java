package ch.bemar.org.dhcp;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkInterfaceExample {

    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                System.out.println("Name: " + networkInterface.getName());
                System.out.println("Display Name: " + networkInterface.getDisplayName());
                System.out.println("Is Up: " + networkInterface.isUp());
                System.out.println("Is Loopback: " + networkInterface.isLoopback());
                System.out.println("Is Virtual: " + networkInterface.isVirtual());
                System.out.println("Hardware Address: " + bytesToHex(networkInterface.getHardwareAddress()));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        if (bytes != null) {
            for (byte b : bytes) {
                result.append(String.format("%02X", b));
                result.append("-");
            }
            if (result.length() > 0) {
                result.setLength(result.length() - 1);
            }
        }
        return result.toString();
    }
}
