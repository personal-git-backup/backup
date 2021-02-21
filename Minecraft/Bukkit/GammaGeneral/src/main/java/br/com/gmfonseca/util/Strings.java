package br.com.gmfonseca.util;

import br.com.gmfonseca.api.Config;
import br.com.gmfonseca.main.Main;

import java.util.HashMap;

public class Strings {

    private static HashMap<String, Config> configs = Main.CONFIGS;

    public static String SERVER_NAME = String.valueOf(configs.get("messages").getConfig().get("Server.name"));


}
