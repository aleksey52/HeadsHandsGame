package org.example;

import org.example.exception.CreatureIllegalArgumentException;
import org.example.exception.FailedAttackException;
import org.example.exception.FailedHealingException;
import org.example.model.Creature;
import org.example.model.Monster;
import org.example.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final Player player = new Player(15, 10, 100, 20, 50);
        final Creature monster0 = new Monster(20, 5, 20, 50, 70);
        final Creature monster1 = new Monster(30, 25, 200, 100, 150);

        monster0.hit(player);
        player.hit(monster0);
        while (player.getHealth() != player.getMaxHealth()) {
            player.heal();
        }
        monster1.hit(player);

        try {
            final Creature monster2 = new Monster(35, 35, -5, 100, 50);
        } catch (CreatureIllegalArgumentException exception) {
            LOG.error(exception.getMessage());
        }

        try {
            player.hit(monster1);
        } catch (FailedAttackException exception) {
            LOG.error(exception.getMessage());
        }

        try {
            player.heal();
        } catch (FailedHealingException exception) {
            LOG.error(exception.getMessage());
        }
    }
}