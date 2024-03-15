package ch.bemar.dhcp.constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PxeDhcpOptionsConverter {
    // Konvertiert die Optionen in ein Byte-Array
    public byte[] convertToBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Option PXE Discovery Control
        stream.write(constructSingleByteOption(6, (byte) 8)); // Beispielwert für 'unsigned integer 8'

        // Option PXE Boot Server
        stream.write(constructBootServerOption(8, (short) 123, (byte) 1, "192.168.1.1"));

        // Option PXE Boot Menu
        stream.write(constructBootMenuOption(9, (short) 123, (byte) 1, "Beispielmenü"));

        // Option PXE Menu Prompt
        stream.write(constructMenuPromptOption(10, (byte) 30, "Drücken Sie F12 für Netzwerkboot."));

        return stream.toByteArray();
    }

    private byte[] constructSingleByteOption(int code, byte value) {
        // Code (1 Byte) + Länge (1 Byte) + Wert (1 Byte)
        return new byte[]{(byte) code, 1, value};
    }

    private byte[] constructBootServerOption(int code, short port, byte type, String ipAddress) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write((byte) code); // Option Code
        stream.write((byte) 7); // Länge: 2 Bytes für port, 1 Byte für type, 4 Bytes für IP
        stream.write(ByteBuffer.allocate(2).putShort(port).array()); // Port
        stream.write(type); // Type
        stream.write(ipStringToByteArray(ipAddress)); // IP-Adresse
        return stream.toByteArray();
    }

    private byte[] constructBootMenuOption(int code, short reference, byte platform, String text) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] textBytes = text.getBytes();
        stream.write((byte) code); // Option Code
        stream.write((byte) (3 + textBytes.length)); // Länge
        stream.write(ByteBuffer.allocate(2).putShort(reference).array()); // Reference
        stream.write(platform); // Platform
        stream.write(textBytes); // Text
        return stream.toByteArray();
    }

    private byte[] constructMenuPromptOption(int code, byte timeout, String prompt) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] promptBytes = prompt.getBytes();
        stream.write((byte) code); // Option Code
        stream.write((byte) (1 + promptBytes.length)); // Länge
        stream.write(timeout); // Timeout
        stream.write(promptBytes); // Prompt Text
        return stream.toByteArray();
    }

    // Hilfsmethode, um eine IP-Adresse in ein Byte-Array umzuwandeln
    private byte[] ipStringToByteArray(String ipString) {
        String[] ipParts = ipString.split("\\.");
        byte[] ipBytes = new byte[ipParts.length];
        for (int i = 0; i < ipParts.length; i++) {
            int part = Integer.parseInt(ipParts[i]);
            ipBytes[i] = (byte) part;
        }
        return ipBytes;
    }

    public static void main(String[] args) {
        PxeDhcpOptionsConverter converter = new PxeDhcpOptionsConverter();
        try {
            byte[] dhcpOptions = converter.convertToBytes();
            // Hier könnten Sie dhcpOptions weiterverwenden, z.B. ausgeben oder in einem größeren Paket senden.
            System.out.println("DHCP Options in Bytes: " + java.util.Arrays.toString(dhcpOptions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
