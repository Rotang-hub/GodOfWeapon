package io.github.rotanghub.godofweapon.manager;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;

public class WeaponSkill implements Listener
{
    private Manager manager;

    public WeaponSkill(Manager manager)
    {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if(event.getHand().equals(EquipmentSlot.HAND))
        {
            if(event.getAction().toString().contains("RIGHT"))
            {
                if(manager.isSatelliteDagger(item) && manager.getCurrentSwordLevel(item) >= 3)  //위성단검 이속증가
                {
                    int coolDown = manager.getCoolTime("SatelliteDagger");;
                    if(player.getCooldown(item.getType()) == 0)
                    {
                        speedPlayer(player);
                        player.setCooldown(item.getType(), coolDown * 20);
                    }
                }

                if(manager.isSatelliteSword(item) && manager.getCurrentSwordLevel(item) >= 3)   //위성검 대쉬
                {
                    int coolDown = manager.getCoolTime("SatelliteSword");;
                    if(player.getCooldown(item.getType()) == 0)
                    {
                        dashPlayer(player);
                        player.setCooldown(item.getType(), coolDown * 20);
                    }
                }

                if(manager.isEvolveSatelliteSword(item))   //진 위성검 대쉬
                {
                    int coolDown = manager.getCoolTime("EvolveSatelliteSword");;
                    if(player.getCooldown(item.getType()) == 0)
                    {
                        dashPlayer(player);
                        player.setCooldown(item.getType(), coolDown * 20);
                    }
                }

                if(manager.isHerbSword(item))   //초섬검 회복
                {
                    int coolDown = manager.getCoolTime("HerbSword");
                    if(player.getCooldown(item.getType()) == 0)
                    {
                        int level = manager.getCurrentSwordLevel(item);
                        healPlayer(player, level);
                        player.setCooldown(item.getType(), coolDown * 20);
                    }
                }

                if(manager.isSonificationSword(item))   //음화검 음파
                {
                    int coolDown = manager.getCoolTime("SonificationSword");
                    if(player.getCooldown(item.getType()) == 0)
                    {
                        shotSonicBoom(player, manager.getWeaponDamage(item));
                        player.setCooldown(item.getType(), coolDown * 20);
                    }
                }
            }
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getBlockFace().equals(BlockFace.UP) && manager.isSonificationSwordRyu(item)) //파동검:류
            {
                int coolDown = manager.getCoolTime("SonificationSwordRyu");
                if(player.getCooldown(item.getType()) == 0)
                {
                    driveSword(player, item, event.getClickedBlock());
                    player.getInventory().setItem(EquipmentSlot.HAND, null);
                    player.setCooldown(item.getType(), coolDown * 20);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity)
        {
            Player player = (Player) event.getEntity();
            LivingEntity damager = (LivingEntity) event.getDamager();

            if(player.isBlocking())
            {
                EquipmentSlot hand = getShieldHand(player);
                if(hand == null) return;
                ItemStack shield = player.getEquipment().getItem(hand);

                if(manager.isCounterShield(shield))     //반격 방패
                {
                    int damage = (int) Math.round(event.getDamage());   //double to int
                    useCounter(player, damager, shield, damage);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked() instanceof LivingEntity)
        {
            Player player = event.getPlayer();
            LivingEntity target = (LivingEntity) event.getRightClicked();
            ItemStack item = player.getInventory().getItemInMainHand();

            if(item == null || item.getType().isAir()) return;

            if(manager.isBlindSword(item))   //절안검
            {
                int coolDown = manager.getCoolTime("BlindSword");
                int level = manager.getCurrentSwordLevel(item);
                int duration = manager.getDuration("BlindSword", level);
                if(player.getCooldown(item.getType()) == 0)
                {
                    blindingTarget(target, duration);
                    player.setCooldown(item.getType(), coolDown * 20);
                }
            }
            if(manager.isBlazingSword(item))    //염화검
            {
                int coolDown = manager.getCoolTime("BlindSword");
                int level = manager.getCurrentSwordLevel(item);
                int hit = manager.getHit(level);
                int damage = manager.getWeaponDamage(item) / 2;
                if(player.getCooldown(item.getType()) == 0)
                {
                    blazingTarget(player, target, damage, hit);
                    player.setCooldown(item.getType(), coolDown * 20);
                }
            }
        }
    }

    @EventHandler
    public void onCustomPlayerDeath(CustomPlayerDeathEvent event)
    {
        //event.getPlayer().sendMessage(event.getPlayer().getName());
    }

    public void speedPlayer(Player player)
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0, true, false));
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 3, 2);
    }

    public void dashPlayer(Player player)
    {
        double power = 1.2;
        Vector dir = player.getLocation().getDirection();
        Vector vec = dir.normalize().multiply(power);

        player.setVelocity(vec);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3, 2);
        player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, player.getLocation(), 0, 0, -1, 0, 0.1);
        player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, player.getLocation(), 0, 0, -1, 0, 0.1);
        player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, player.getLocation(), 0, 0, -1, 0, 0.1);
        player.swingMainHand();
    }

    public void healPlayer(Player player, int level)
    {
        int heal = manager.getHeal(level);
        int health = manager.getHealth(player);
        int add = health * heal / 100;

        manager.addCurrentHealth(player, add, null);
        //player.sendMessage("heal: " + heal + "  health: " + health + "  add: " + add);

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 7, 2);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 7, 2);
        player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), heal * 2, 0.3, 1, 0.3, 0.1);    //회복력 기반 파티클
    }

    public void shotSonicBoom(Player player, int damage)
    {
        Vector dir = player.getLocation().clone().getDirection().normalize();
        Location loc = player.getEyeLocation().clone();
        player.getWorld().playSound(loc, Sound.ENTITY_WARDEN_SONIC_BOOM, 3, 1);

        player.setVelocity(player.getVelocity().add(dir.clone().normalize().multiply(-0.5)));

        for(int i = 0; i < 80; i++)
        {
            Location targetLoc = loc.clone().add(dir.clone().multiply(i / 3));
            player.getWorld().spawnParticle(Particle.SONIC_BOOM, targetLoc, 1);

            for(LivingEntity e : player.getWorld().getLivingEntities())
            {
                if(e.equals(player)) continue;
                if(targetLoc.distance(e.getEyeLocation()) <= 1.5)
                {
                    e.damage(damage, player);
                }
            }
        }
    }

    public void blindingTarget(LivingEntity target, int duration)
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration * 20, 0, true, false));
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WARDEN_ROAR, 3, 0.4f);
        //target.getWorld().playSound(target.getLocation(), Sound);
    }

    public void blazingTarget(Player player, LivingEntity target, int damage, int hit)
    {
        player.swingMainHand();
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_FIRECHARGE_USE, 3, 0.6f);

        new BukkitRunnable()
        {
            int time = 1;
            @Override
            public void run()
            {
                if(time >= hit * 4 || target.isDead())
                {
                    cancel();
                    return;
                }
                Location loc = target.getLocation().add(target.getEyeLocation()).multiply(0.5);
                target.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 100, 0.2, 0.4, 0.2, 0);
                target.getWorld().spawnParticle(Particle.FLAME, loc, 3, 0.2, 0.4, 0.2, 0);
                time++;
            }
        }.runTaskTimer(manager.plugin, 0, 10);

        new BukkitRunnable()
        {
            int time = 1;

            @Override
            public void run()
            {
                if(time >= hit || target.isDead())
                {
                    cancel();
                    return;
                }
                target.damage(damage, player);
                Location loc = target.getLocation().add(target.getEyeLocation()).multiply(0.5);
                target.getWorld().spawnParticle(Particle.FLAME, loc, damage * 3, 0.2, 0.4, 0.2, 0.1);
                target.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 3, 0.6f);

                time++;
            }
        }.runTaskTimer(manager.plugin, 0, 40);
    }

    public void driveSword(Player player, ItemStack sword, Block block)
    {
        int level = manager.getCurrentSwordLevel(sword);
        int duration = manager.getDuration("SonificationSwordRyu", level);
        int distance = 7;
        int damage = manager.getWeaponDamage(sword) * 3 / 10;   //30%

        ArmorStand as = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setInvulnerable(true);
        as.getEquipment().setHelmet(sword);
        as.setHeadPose(new EulerAngle(1.5, 0, 0));
        as.setSilent(true);

        for(EquipmentSlot slot : EquipmentSlot.values())
        {
            as.addEquipmentLock(slot, ArmorStand.LockType.ADDING_OR_CHANGING);
        }

        AreaEffectCloud area = (AreaEffectCloud) block.getWorld().spawnEntity(as.getLocation().add(0, 1, 0), EntityType.AREA_EFFECT_CLOUD);
        area.setRadius(distance);
        area.setDuration(duration * 20 + 10);
        area.setParticle(Particle.SCULK_CHARGE_POP);

        AreaEffectCloud effectCloud = (AreaEffectCloud) block.getWorld().spawnEntity(as.getLocation().add(0, 1, 0), EntityType.AREA_EFFECT_CLOUD);
        effectCloud.setRadius(distance);
        effectCloud.setDuration(duration * 20 + 10);
        effectCloud.setParticle(Particle.SCULK_CHARGE, (float) Math.PI);

        for(int y = 0; y <= 30; y++)
        {
            Location loc = as.getLocation().clone();
            loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.add(0, y, 0), 1, 0, 0, 0, 0);
        }
        as.getWorld().spawnParticle(Particle.WARPED_SPORE, as.getLocation().add(0, 1, 0), 100, 0, 0, 0, 0.3);
        as.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, as.getLocation().add(0, 1, 0), 1, 0, 0, 0, 0);
        as.getWorld().playSound(as.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 3, 1);
        as.getWorld().playSound(as.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 4, 0.1f);

        new BukkitRunnable()
        {
            int time = 0;
            @Override
            public void run()
            {
                time++;

                if(!player.isOnline())
                {
                    cancel();
                    as.remove();
                    area.remove();
                    effectCloud.remove();
                    player.getWorld().dropItemNaturally(player.getLocation(), sword);

                    return;
                }
                if(player.isDead() || time > duration * 20)
                {
                    cancel();
                    as.remove();
                    area.remove();
                    effectCloud.remove();

                    Map<Integer, ItemStack> drops = player.getInventory().addItem(sword);
                    for(Map.Entry<Integer, ItemStack> entry : drops.entrySet())
                    {
                        Item drop = player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
                        drop.setOwner(player.getUniqueId());
                    }

                    return;
                }
                if(time % 10 == 0)
                {
                    for(LivingEntity le : player.getWorld().getLivingEntities())
                    {
                        if(le.equals(player) || le instanceof ArmorStand) continue;
                        if(isInArea(area, le, distance))
                        {
                            le.damage(damage, player);
                        }
                    }
                }
            }
        }.runTaskTimer(manager.plugin, 10, 1);
    }

    public boolean isInArea(AreaEffectCloud area, Entity entity, int distance)
    {
        if(area.getLocation().distance(entity.getLocation()) <= distance)   //범위
        {
            double subtract = entity.getLocation().getY() - area.getLocation().getY();

            if(Math.abs(subtract) <= 1.5)
            {
                return true;
            }
        }
        return false;
    }


    public void useCounter(Player player, LivingEntity damager, ItemStack shield, int damage)
    {
        int level = manager.getCurrentSwordLevel(shield);
        int knockBack = manager.getKnockBack(level);
        int counter = manager.getCounter(level);
        double counterDamage = ((double) damage) / 100 * counter;
        int coolDown = manager.getCoolTime("CounterShield");

        damager.damage((int) Math.round(counterDamage), player);
        Vector vec = damager.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
        damager.setVelocity(vec.multiply(((double) knockBack) / 2));

        EquipmentSlot slot = getShieldHand(player);

        player.getInventory().setItem(slot, null);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.setCooldown(Material.SHIELD, coolDown * 20);
                ItemStack clone = shield.clone();

                if(slot == null)
                {
                    player.getInventory().addItem(clone);
                }
                else if(slot.equals(EquipmentSlot.HAND))
                {
                    player.getInventory().setItem(slot, clone);
                }
                else if(slot.equals(EquipmentSlot.OFF_HAND))
                {
                    player.getInventory().setItem(slot, clone);
                }
            }
        }.runTaskLater(manager.plugin, 1);
    }

    public EquipmentSlot getShieldHand(Player player)
    {
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();

        //if(main == null || off == null || main.getType().isAir() || off.getType().isAir()) return null;

        if(main.getType().equals(Material.SHIELD)) return EquipmentSlot.HAND;
        if(off.getType().equals(Material.SHIELD)) return EquipmentSlot.OFF_HAND;

        return null;
    }
}
