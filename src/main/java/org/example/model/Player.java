package org.example.model;

import org.example.exception.FailedHealingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Creature {

    private static final Logger LOG = LoggerFactory.getLogger(Player.class);
    private static final String HEALING_INFO_LOG = "The player %s was healed by %d units. Health before: %d. " +
            "Health after: %d. Number of remaining healing: %d.";

    private static final int MAX_COUNT_HEALING = 4;
    private static final double HEALING_MODIFIER = 0.3;
    private static final String HEALING_IS_NOT_AVAILABLE_MSG = "Player %s is dead - healing is not available";
    private static final String MAX_HEALING_MSG = "Player %s has healed the maximum number of times: %d";

    private int countOfHealing = 0;

    public Player(int attack, int protection, int health, int minDamage, int maxDamage) {
        super(attack, protection, health, minDamage, maxDamage);
    }

    public void heal() {
        if (this.getHealth() == MIN_HEALTH) {
            throw new FailedHealingException(String.format(HEALING_IS_NOT_AVAILABLE_MSG, this));
        }

        if (countOfHealing <= MAX_COUNT_HEALING) {
            final int amountOfHealing = (int) (HEALING_MODIFIER * this.getMaxHealth());
            final int healthBeforeHealing = this.getHealth();
            final int sumHealth = healthBeforeHealing + amountOfHealing;
            this.setHealth(Math.min(sumHealth, this.getMaxHealth()));
            ++this.countOfHealing;
            LOG.info(String.format(HEALING_INFO_LOG, this, amountOfHealing, healthBeforeHealing, sumHealth,
                    MAX_COUNT_HEALING - countOfHealing));
        } else {
            throw new FailedHealingException(String.format(MAX_HEALING_MSG, this, MAX_COUNT_HEALING));
        }

    }
}
