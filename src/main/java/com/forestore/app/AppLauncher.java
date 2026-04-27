package com.forestore.app;

import com.forestore.ui.StarterForm;

import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(()->{
            StarterForm form = new StarterForm();
            form.setVisible(true);
        });
    }
}
