package br.com.gmfonseca.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Natural implements Listener{

    @EventHandler
    private void noHungry(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    private void noRain(WeatherChangeEvent e){
        if(e.toWeatherState()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void noExplosion(EntityExplodeEvent e){
        e.setCancelled(true);
    }
}
