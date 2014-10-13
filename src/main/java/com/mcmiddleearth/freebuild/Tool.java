/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.freebuild;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 *
 * @author Ivan1pl
 */
public final class Tool implements Listener{
    public static Material ModelTool;
    public static Material liquidTool;
    public static Material fireTool;
    
    @EventHandler
    public void onPlayerUseModelTool(PlayerInteractEvent e){
        if(!e.isCancelled() && e.getPlayer().hasPermission("plotmanager.create") && DBmanager.IncompleteModel != null
                && e.getPlayer().getItemInHand().getType() == ModelTool && e.hasBlock()){
            Action action = e.getAction();
            if(action == Action.LEFT_CLICK_BLOCK){
                DBmanager.IncompleteModel.setPoint1(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(Freebuild.prefix + "First point set");
                e.setCancelled(true);
            }
            else if(action == Action.RIGHT_CLICK_BLOCK){
                DBmanager.IncompleteModel.setPoint2(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(Freebuild.prefix + "Second point set");
                e.setCancelled(true);
            }
        }
    }
    
    private boolean interactionAllowed(Player pl, Location l) {
        boolean allowed = false;
        if(DBmanager.plots.containsKey(pl.getName())) {
            ArrayList<Plot> plots = DBmanager.plots.get(pl.getName());
            for(Plot p : plots) {
                if(p.isIn(l)) {
                    allowed = true;
                    break;
                }
            }
        }
        if(DBmanager.BuildPastPlots && DBmanager.pastPlots.containsKey(pl.getName())) {
            ArrayList<Plot> plots = DBmanager.pastPlots.get(pl.getName());
            for(Plot p : plots) {
                if(p.isIn(l)) {
                    allowed = true;
                    break;
                }
            }
        }
        return allowed;
    }
    
    @EventHandler
    public void onPlayerUseLiquidTool(PlayerInteractEvent e) {
        if(!e.isCancelled() && e.getPlayer().getItemInHand().getType() == liquidTool && e.hasBlock()) {
            Location l = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
            if(interactionAllowed(e.getPlayer(),l)) {
                e.setCancelled(true);
                if(l.getBlock().getType() == Material.AIR || l.getBlock().getType() == Material.LAVA || l.getBlock().getType() == Material.WATER
                        || l.getBlock().getType() == Material.STATIONARY_WATER || l.getBlock().getType() == Material.STATIONARY_LAVA) {
                    if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        l.getBlock().setType(Material.AIR);
                        l.getBlock().setType(Material.LAVA);
                    }
                    else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        l.getBlock().setType(Material.AIR);
                        l.getBlock().setType(Material.WATER);
                    }
                    BlockState state = l.getBlock().getState();
                    ItemStack is = state.getData().toItemStack();
                    is.setDurability((short) 0);
                    state.setData(is.getData());
                    state.update(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerUseFireTool(PlayerInteractEvent e) {
        if(!e.isCancelled() && e.getPlayer().getItemInHand().getType() == fireTool && e.hasBlock()) {
            Location l = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
            if(interactionAllowed(e.getPlayer(),l)) {
                e.setCancelled(true);
                if(l.getBlock().getType() == Material.AIR) {
                    if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        l.getBlock().setType(Material.FIRE);
                    }
                }
            }
        }
    }
}