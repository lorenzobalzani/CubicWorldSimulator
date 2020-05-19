package it.cubicworldsimulator.game.gui;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextInput;

/**
 * It models a generic gui using LEGUI library.
 */
public interface GenericGui {
    void setAspectRatio();
    Button createButton(String text, Vector2f position, Vector2f size);
    Label createLabel(String messageText, Vector2f position);
    Label createOptionLabel(String title, Panel panelToAdd);
    TextInput createTextInput(String title, Panel panelToAdd);
}
