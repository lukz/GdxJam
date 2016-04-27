package com.mygdx.jam.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.mygdx.jam.entities.Player;
import com.mygdx.jam.utils.Xbox360;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-30.
 */
public class PlayerGamepadController extends ControllerAdapter {

    private Player playerController;

    public PlayerGamepadController(Player player) {
        this.playerController = player;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisIndex, float value) {
        if(value > -0.1f && value < 0.1f) value = 0;

        if(axisIndex == Xbox360.AXIS_LEFT_X) {
            playerController.getDirection().x = value;
            return true;

        } else if(axisIndex == Xbox360.AXIS_LEFT_Y) {
            playerController.getDirection().y = -value;
            return true;

        }

        return false;
    }

    @Override public boolean buttonDown (Controller controller, int buttonIndex) {
        switch (buttonIndex) {
        case Xbox360.BUTTON_A:
        case Xbox360.BUTTON_B:
        case Xbox360.BUTTON_X:
        case Xbox360.BUTTON_Y:
        case Xbox360.BUTTON_RB:
        case Xbox360.BUTTON_R3:
            playerController.fire = true;
        }
        return super.buttonDown(controller, buttonIndex);
    }

    @Override public boolean buttonUp (Controller controller, int buttonIndex) {
        switch (buttonIndex) {
            case Xbox360.BUTTON_A:
            case Xbox360.BUTTON_B:
            case Xbox360.BUTTON_X:
            case Xbox360.BUTTON_Y:
            case Xbox360.BUTTON_RB:
            case Xbox360.BUTTON_R3:
                playerController.fire = false;
        }
        return super.buttonUp(controller, buttonIndex);
    }

    @Override
    public void connected(Controller controller) {
        super.connected(controller);
    }

    @Override
    public void disconnected(Controller controller) {
        super.disconnected(controller);
    }


}
