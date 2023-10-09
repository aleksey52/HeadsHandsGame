package org.example.model;

import org.example.exception.CreatureIllegalArgumentException;
import org.example.exception.FailedAttackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public abstract class Creature {

    private static final Logger LOG = LoggerFactory.getLogger(Creature.class);
    private static final String CREATURE_IS_DIED_LOG = "Creature %s is died";
    private static final String SUCCESSFUL_STRIKE_LOG = "Strike of %s on %s is successful with damage %d";
    private static final String UNSUCCESSFUL_STRIKE_LOG = "Strike of %s is not successful";

    private static final int MIN_ATTACK_PROTECTION = 1;
    private static final int MAX_ATTACK_PROTECTION = 30;
    protected static final int MIN_HEALTH = 0;
    private static final int MIN_DAMAGE = 1;
    private static final int MIN_WINNING_DICE = 5;
    private static final String ILLEGAL_ATTACK_PROTECTION_MSG = "Attack and protection parameters should be" +
            " in the range from %d to %d";
    private static final String ILLEGAL_HEALTH_MSG = "The health parameter must be at least %d";
    private static final String ILLEGAL_DAMAGE_MSG = "The minimum damage must be greater than %d," +
            " and the maximum damage must not be less than the minimum";
    private static final String FAILED_ATTACK_MSG = "The creature %s is dead - an attack is not possible";

    private int attack;
    private int protection;
    private int maxHealth;
    private int health;
    private boolean isDied;
    private int minDamage;
    private int maxDamage;

    public Creature(int attack, int protection, int health, int minDamage, int maxDamage) {
        if (attack >= MIN_ATTACK_PROTECTION && attack <= MAX_ATTACK_PROTECTION
                && protection >= MIN_ATTACK_PROTECTION && protection <= MAX_ATTACK_PROTECTION) {
            this.attack = attack;
            this.protection = protection;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_ATTACK_PROTECTION_MSG,
                    MIN_ATTACK_PROTECTION, MAX_ATTACK_PROTECTION));
        }

        if (health >= MIN_HEALTH) {
            this.maxHealth = health;
            this.health = health;
            this.isDied = health == MIN_HEALTH;

            if (isDied) {
                LOG.info(String.format(CREATURE_IS_DIED_LOG, this));
            }
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_HEALTH_MSG, MIN_HEALTH));
        }

        if (minDamage >= MIN_DAMAGE && maxDamage >= minDamage) {
            this.minDamage = minDamage;
            this.maxDamage = maxDamage;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_DAMAGE_MSG, MIN_DAMAGE));
        }
    }

    public void hit(Creature targetCreature) {
        if (this.isDied) {
            throw new FailedAttackException(String.format(FAILED_ATTACK_MSG, this));
        }

        int attackModifier = this.attack - targetCreature.getProtection() + 1;
        boolean strikeIsSuccessful = false;
        Random random = new Random(System.currentTimeMillis());

        do {
            int dice = 1 + random.nextInt(6);
            if (dice >= MIN_WINNING_DICE) {
                strikeIsSuccessful = true;
            }
            --attackModifier;
        } while (attackModifier > 0 && !strikeIsSuccessful);

        if (strikeIsSuccessful) {
            final int currentDamage = this.minDamage + random.nextInt(this.maxDamage - this.minDamage + 1);
            final int healthDamageDifference = targetCreature.getHealth() - currentDamage;
            final int targetCreatureHealth = Math.max(healthDamageDifference, MIN_HEALTH);

            targetCreature.setHealth(targetCreatureHealth);

            LOG.info(String.format(SUCCESSFUL_STRIKE_LOG, this, targetCreature, currentDamage));
            if (targetCreature.isDied()) {
                LOG.info(String.format(CREATURE_IS_DIED_LOG, targetCreature));
            }
        } else {
            LOG.info(String.format(UNSUCCESSFUL_STRIKE_LOG, this));
        }
    }

    public void setAttack(int attack) {
        if (attack >= MIN_ATTACK_PROTECTION && attack <= MAX_ATTACK_PROTECTION) {
            this.attack = attack;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_ATTACK_PROTECTION_MSG,
                    MIN_ATTACK_PROTECTION, MAX_ATTACK_PROTECTION));
        }
    }

    public void setProtection(int protection) {
        if (protection >= MIN_ATTACK_PROTECTION && protection <= MAX_ATTACK_PROTECTION) {
            this.protection = protection;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_ATTACK_PROTECTION_MSG,
                    MIN_ATTACK_PROTECTION, MAX_ATTACK_PROTECTION));
        }
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth >= MIN_HEALTH) {
            this.maxHealth = maxHealth;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_HEALTH_MSG, MIN_HEALTH));
        }
    }

    public void setHealth(int health) {
        if (health >= 0) {
            this.health = health;
            this.isDied = health == MIN_HEALTH;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_HEALTH_MSG, MIN_HEALTH));
        }
    }

    public void setMinAndMaxDamage(int minDamage, int maxDamage) {
        if (minDamage >= MIN_DAMAGE && maxDamage >= minDamage) {
            this.minDamage = minDamage;
            this.maxDamage = maxDamage;
        } else {
            throw new CreatureIllegalArgumentException(String.format(ILLEGAL_DAMAGE_MSG, MIN_DAMAGE));
        }
    }

    public int getAttack() {
        return attack;
    }

    public int getProtection() {
        return protection;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDied() {
        return isDied;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }
}
