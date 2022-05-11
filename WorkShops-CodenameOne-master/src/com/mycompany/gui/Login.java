package com.yummygout.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

public class Login extends Form {

    public static Form loginForm;

    public Login() {
        super("Connexion", new BoxLayout(BoxLayout.Y_AXIS));
        loginForm = this;
        addGUIs();
    }

    private void addGUIs() {


        Button frontendBtn = new Button("Front");
        frontendBtn.addActionListener(l -> new com.yummygout.gui.front.AccueilFront().show());
        this.add(frontendBtn);


        Button backendBtn = new Button("Back");
        backendBtn.addActionListener(l -> new com.yummygout.gui.back.AccueilBack().show());

        this.add(backendBtn);
    }

}
