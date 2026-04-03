package com.choice.minionfarm.minion.animation;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.di.enums.Parts;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.mineacademy.fo.model.SimpleRunnable;

public class MinionAnimation extends SimpleRunnable {

    private final double UPPER_ROTATION = 7.0/6 * Math.PI;
    private final double LOWER_ROTATION = 7.0/4 * Math.PI;

    private final ArmorStand minionArmStand;
    private double currentRotation;
    private AnimationDirection animationDirection;
    private final double rotationChangesPerTick;

    public MinionAnimation(ArmorStand minionArmStand, double velocity) {
        this.minionArmStand = minionArmStand;
        this.currentRotation = UPPER_ROTATION;
        this.animationDirection = AnimationDirection.DOWN;
        this.rotationChangesPerTick = Math.abs(UPPER_ROTATION - LOWER_ROTATION) / (velocity / 2);

        initializeMinionArmStandRight();
    }

    public MinionAnimation(ArmorStand minionArmStand, double velocity, Parts parts) {
        this.minionArmStand = minionArmStand;
        this.currentRotation = UPPER_ROTATION;
        this.animationDirection = AnimationDirection.DOWN;
        this.rotationChangesPerTick = Math.abs(UPPER_ROTATION - LOWER_ROTATION) / (velocity / 2);

        switch (parts){
            case RIGHT_ARM -> initializeMinionArmStandRight();
            case LEFT_ARM -> initializeMinionArmStandLeft();
        }
    }

    private void initializeMinionArmStandLeft() {
        minionArmStand.setLeftArmPose(new EulerAngle(UPPER_ROTATION, 0, 0));
    }


    private void initializeMinionArmStandRight() {
        minionArmStand.setRightArmPose(new EulerAngle(UPPER_ROTATION, 0, 0));
    }

    public void startAnimation() {
        runTaskTimer(FarmAPI.getInstance(), 0L, 1L);
    }

    @Override
    public void run() {
        updateRotation();
        updateAnimationDirection();
        updateArmStandPose();
    }

    private void updateRotation() {
        switch (animationDirection) {
            case DOWN:
                currentRotation += rotationChangesPerTick;
                break;
            case UP:
                currentRotation -= rotationChangesPerTick;
                break;
        }
    }

    private void updateAnimationDirection() {
        if (currentRotation <= UPPER_ROTATION) {
            animationDirection = AnimationDirection.DOWN;
        }
        if (currentRotation >= LOWER_ROTATION) {
            animationDirection = AnimationDirection.UP;
        }
    }

    private void updateArmStandPose() {
        minionArmStand.setRightArmPose(new EulerAngle(currentRotation, 0, 0));
    }

    private enum AnimationDirection {
        DOWN, UP
    }
}
