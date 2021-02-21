package br.com.gmfonseca.main;

import br.com.gmfonseca.util.GameState;
import br.com.gmfonseca.world.World;

import java.awt.*;
import java.io.*;

public class Menu {

    public String[] options = {"novo jogo", "carregar jogo", "sair"};
    public int currentOption = 0;
    public int maxOption = options.length - 1;

    public boolean up = false;
    public boolean down = false;
    public boolean enter = false;

    public static boolean pause = false;
    public static boolean saveExists = false;
    public static boolean load = false;

    public static void saveGame(String[] keys, int[] values, int encode) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("save.txt"));

            for (int i = 0; i < keys.length; i++) {
                StringBuilder current = new StringBuilder(keys[i]);
                current.append(":");
                char[] value = Integer.toString(values[i]).toCharArray();
                for (int j = 0; j < value.length; j++) {
                    value[j] += encode;
                    current.append(value[j]);
                }

                writer.write(current.toString());
                if (i < keys.length - 1) {
                    writer.newLine();
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadGame(int encode) {
        StringBuilder line = new StringBuilder();

        File file = new File("save.txt");
        if (file.exists()) {
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));

                while ((singleLine = reader.readLine()) != null) {
                    String[] trans = singleLine.split(":");
                    char[] val = trans[1].toCharArray();
                    trans[1] = "";
                    for (int i = 0; i < val.length; i++) {
                        val[i] -= encode;
                        trans[1] += val[i];
                    }
                    line.append(trans[0]).append(":").append(trans[1]).append("/");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return line.toString();
    }

    public static void applySave(String str){
        String[] spl = str.split("/");
        for (int i = 0; i < spl.length; i++) {
            String[] spl2 = spl[i].split(":");
            switch (spl2[0]) {
                case "level":
                    Game.restartGame();
            }
        }
    }

    public void tick() {

        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) currentOption = maxOption;
        } else if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOption) currentOption = 0;
        }

        if (enter) {
            enter = false;
            switch (options[currentOption]) {
                case "novo jogo":
                    Game.gameState = GameState.NORMAL;
                    break;
                case "carregar jogo":
                    break;
                case "sair":
                    break;
            }
        }

    }

    public void render(Graphics g) {
        Graphics2D gd = (Graphics2D) g;
        gd.setColor(new Color(0, 0, 0, 100));
        gd.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.RED);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString("FAKERZelda", Game.WIDTH * Game.SCALE / 2 - 120, Game.HEIGHT * Game.SCALE / 2 - 160);
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 24));
        g.drawString("New Game", Game.WIDTH * Game.SCALE / 2 - 80, Game.HEIGHT * Game.SCALE / 2 - 100);
        g.drawString("Load Game", Game.WIDTH * Game.SCALE / 2 - 85, Game.HEIGHT * Game.SCALE / 2 - 60);
        g.drawString("Exit", Game.WIDTH * Game.SCALE / 2 - 40, Game.HEIGHT * Game.SCALE / 2 - 20);

        switch ((options[currentOption])) {
            case "novo jogo":
                g.drawString("> ", Game.WIDTH * Game.SCALE / 2 - 100, Game.HEIGHT * Game.SCALE / 2 - 100);
                break;
            case "carregar jogo":
                g.drawString("> ", Game.WIDTH * Game.SCALE / 2 - 105, Game.HEIGHT * Game.SCALE / 2 - 60);
                break;
            case "sair":
                g.drawString("> ", Game.WIDTH * Game.SCALE / 2 - 60, Game.HEIGHT * Game.SCALE / 2 - 20);
                break;
        }
    }

}
