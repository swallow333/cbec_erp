package com.hakkai.backend.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Obtain the IP address of the physical NIC
public abstract class ComputerInfo {
    private static String macAddressStr = null; //Store MAC address
    private static String computerName = System.getenv().get("COMPUTERNAME"); //Obtain the computer name from the environment variable
    private static final String[] WINDOWS_COMMAND = {"ipconfig", "/all"}; //windows command
    private static final String[] LINUX_COMMAND = {"/sbin/ifconfig", "-a"}; //linux command
    private static final Pattern PATTERN_MAC = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*", Pattern.CASE_INSENSITIVE); //Case insensitive matching MAC address for example, XX:XX:XX:XX:XX

    //Obtain all MAC addresses
    private static List<String> getMacAddressList() throws IOException {
        final ArrayList<String> macAddressList = new ArrayList<String>();
        final String os = System.getProperty("os.name"); //Obtain the operating system name
        final String[] command;
        if (os.startsWith("Windows")) {
            command = WINDOWS_COMMAND;
        } else if (os.startsWith("Linux")) {
            command = LINUX_COMMAND;
        } else {
            throw new IOException("Unknow operating system:" + os);
        }
        final Process process = Runtime.getRuntime().exec(command); //Run the operating system command to obtain the network interface card
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream())); //Read the output of the command
        for (String line = null; (line = bufReader.readLine()) != null;) {
            Matcher matcher = PATTERN_MAC.matcher(line); //Match each line. If the match is successful, the MAC address is extracted and added to the macAddressList
            if (matcher.matches()) {
                macAddressList.add(matcher.group(1)); //Obtain the value of the first capture group in the regular expression
            }
        }
        process.destroy(); //Destroy process
        bufReader.close(); //Release resource
        return macAddressList;
    }

    /**
     * 获取一个网卡地址（多个网卡时从中获取一个）
     *
     * @return
     */
    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.equals("")) {
            StringBuffer sb = new StringBuffer(); // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            try {
                List<String> macList = getMacAddressList();
                for (Iterator<String> iter = macList.iterator(); iter.hasNext();) {
                    String amac = iter.next();
                    if (!amac.equals("0000000000E0")) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (IOException ignored) {
            }

            macAddressStr = sb.toString();

        }

        return macAddressStr;
    }

    /**
     * 获取电脑名
     *
     * @return
     */
    public static String getComputerName() {
        if (computerName == null || computerName.equals("")) {
            computerName = System.getenv().get("COMPUTERNAME");
        }
        return computerName;
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public static String getIpAddrAndName() throws IOException {
        return InetAddress.getLocalHost().toString();
    }

    /**
     * 获取客户端IP地址
     *
     * @return
     */
    public static String getIpAddr() throws IOException {
        return InetAddress.getLocalHost().getHostAddress().toString();
    }

    /**
     * 限制创建实例
     */
    private ComputerInfo() {

    }

    public static void main(String[] args) throws IOException {
        System.out.println(ComputerInfo.getMacAddress());
        System.out.println(ComputerInfo.getComputerName());
        System.out.println(ComputerInfo.getIpAddr());
        System.out.println(ComputerInfo.getIpAddrAndName());
    }
}