package com.choice.minionfarm.minion.util;

import com.choice.minionfarm.minion.animation.MinionAnimation;
import com.choice.minionfarm.minion.di.enums.Parts;
import com.choice.minionfarm.minion.di.enums.Parts.*;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import static com.choice.minionfarm.minion.di.enums.Parts.LEFT_ARM;
import static com.choice.minionfarm.minion.di.enums.Parts.RIGHT_ARM;
import static com.choice.minionfarm.minion.di.enums.Parts.HEAD;
import static com.choice.minionfarm.minion.di.enums.Parts.LEFT_LEG;
import static com.choice.minionfarm.minion.di.enums.Parts.RIGHT_LEG;


public class BodyPart {
    private Parts parts;

    private ArmorStand armorStand;
    public BodyPart(ArmorStand armorStand, Parts parts){
        this.parts = parts;
        this.armorStand = armorStand;
    }

    public BodyPart(ArmorStand armorStand){
        this.armorStand = armorStand;
    }


    public void setBodyPose(Parts parts){
        this.parts = parts;
    }

    public void setPose(Location target) {
        EulerAngle ea;
        Location origin;
        double initYaw;
        double yaw;
        double pitch;

        if (parts == HEAD) {
            origin = armorStand.getEyeLocation();
            initYaw = origin.getYaw();
            Vector tgt = target.toVector();
            origin.setDirection(((Vector) tgt).subtract(origin.toVector()));
            yaw = origin.getYaw() - initYaw;
            pitch = origin.getPitch();
            if (yaw < -180) {
                yaw = yaw + 360;
            } else if (yaw >= 180) {
                yaw -= 360;
            }
        } else {
            origin = armorStand.getLocation();
            if (parts == LEFT_ARM || parts == RIGHT_ARM) {
                origin = origin.add(0, 1.4, 0);
            } else if (parts == LEFT_LEG || parts == RIGHT_LEG) {
                origin = origin.add(0, 0.8, 0);
            }
            initYaw = origin.getYaw();
            Vector tgt = target.toVector();
            origin.setDirection(tgt.subtract(origin.toVector()));
            yaw = origin.getYaw() - initYaw;
            pitch = origin.getPitch();
            pitch -= 90;

        }
        ea = new EulerAngle(Math.toRadians(pitch), Math.toRadians(yaw), 0);
        setPose(ea);

    }

    public void rotateBody(Location target) {
        double deltaX = target.getX() - armorStand.getLocation().getX();
        double deltaZ = target.getZ() - armorStand.getLocation().getZ();
        float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90;
        armorStand.setRotation(yaw, 0);
    }

    private MinionAnimation animation;
    public void animateRightArmAsync() {
        animation = new MinionAnimation(armorStand, 8);
        animation.startAnimation();
    }

    public void animateLeftArmAsync() {
        animation = new MinionAnimation(armorStand, 20, LEFT_ARM);
        animation.startAnimation();
    }

    public void cancelAnimation() {
        if (animation == null) return;
        animation.cancel();
        setPose(new EulerAngle(0, 0, 0));
        animation = null;
    }

    public void setPose(EulerAngle angle) {
        switch (parts) {
            case HEAD:
                armorStand.setHeadPose(angle);
                break;
            case BODY:
                armorStand.setBodyPose(angle);
                break;
            case LEFT_ARM:
                armorStand.setLeftArmPose(angle);
                break;
            case LEFT_LEG:
                armorStand.setLeftLegPose(angle);
                break;
            case RIGHT_ARM:
                armorStand.setRightArmPose(angle);
                break;
            case RIGHT_LEG:
                armorStand.setRightLegPose(angle);
                break;
        }
    }
}