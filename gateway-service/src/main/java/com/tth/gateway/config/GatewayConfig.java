package com.tth.gateway.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.common.constant.ResourcePath;
import com.tth.common.dto.RabbitMQType;
import com.tth.common.utils.StringUtil;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GatewayConfig {

    //Typesafe config
    public static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;

    //Constant root api path
    public static final String API_ROOT_PATH = ResourcePath.VERSION + "/";

    //Time out khi call RPC (s)
    public static Integer RPC_TIMEOUT = 5;

    //List mã dịch vụ
    public static List<String> SERVICE_LIST = new ArrayList<>();

    //Map chứa các key,value ngoài các map định nghĩa bên dưới
    public static final Map<String, String> SERVICE_MAP = new HashMap<>();

    //Map chứa list path private
    public static final Map<String, List<String>> SERVICE_PATH_PRIVATE_MAP = new HashMap<>();

    //Map chứa list path
    public static final Map<String, List<String>> SERVICE_PATH_MAP = new HashMap<>();

    //Map chứa kiểu rabbit (RPC, Worker, Pub/Sub) cho path + method
    public static final Map<String, String> RABBIT_TYPE_MAP = new HashMap<>();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            System.out.println("======= Loading GatewayConfig config... =======");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "gateway.properties"), "UTF-8"));
            Enumeration e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);

                System.out.println("+ key: " + key + " => value: " + value);
                if (key.equalsIgnoreCase("rpc.timeout")) {
                    if(StringUtil.isNumberic(value)){
                        RPC_TIMEOUT = Integer.parseInt(value.trim());
                    }
                } else if (key.equalsIgnoreCase("service.list")) {
                    String[] arrDomain = value.split(",");
                    SERVICE_LIST = Arrays.asList(arrDomain);
                } else {
                    SERVICE_MAP.put(key, value);
                    if (key.contains(".path.rabbit.file")) {
                        //Load rabbit json file
                        loadRabbitJson(value);
                    } else if (key.contains(".path.private")) {
                        String[] arrPrivatePath = value.split(",");
                        List<String> pathPrivateList = Arrays.asList(arrPrivatePath);
                        SERVICE_PATH_PRIVATE_MAP.put(key.replace(".path.private", ""), pathPrivateList);
                    } else if (key.contains(".path")) {
                        String[] arrPath = value.split(",");
                        List<String> pathList = Arrays.asList(arrPath);
                        SERVICE_PATH_MAP.put(key.replace(".path", ""), pathList);
                    }
                }
            }
            System.out.println("=== Load GatewayConfig config successfull!!! ===");
        } catch (IOException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void loadRabbitJson(String jsonFile) {
        try {
            FileReader reader = new FileReader(CONFIG_DIR + jsonFile);
            ObjectMapper mapper = new ObjectMapper();
            List<RabbitMQType> rabbitMqTypeList = mapper.readValue(reader, new TypeReference<List<RabbitMQType>>() {
            });
            if (rabbitMqTypeList != null && rabbitMqTypeList.size() > 0) {
                rabbitMqTypeList.forEach((tmp) -> {
                    System.out.println(tmp.getMethod() + " " + tmp.getPath() + " => " + tmp.getRabbit());
                    RABBIT_TYPE_MAP.put(tmp.getMethod() + " " + tmp.getPath(), tmp.getRabbit());
                });
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
