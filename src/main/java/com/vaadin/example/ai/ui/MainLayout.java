package com.vaadin.example.ai.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.menu.MenuConfiguration;

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        var headerLayout = new HorizontalLayout(
            new DrawerToggle(),
            new H3("Java AI Demo App")
        );
        headerLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        addToNavbar(headerLayout);

        var navLinks = new VerticalLayout();
        MenuConfiguration.getMenuEntries().forEach(menuEntry -> {
            navLinks.add(new RouterLink(menuEntry.title(), menuEntry.menuClass()));
        });

        addToDrawer(navLinks);
    }
    
}
