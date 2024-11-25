package com.mouwafic.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Souris extends MouseAdapter {

  public int x, y;
  public boolean appuie;

  @Override
  public void mousePressed(MouseEvent e) {
    appuie = true;
  }
  @Override
  public void mouseReleased(MouseEvent e) {
    appuie = false;
  }
  @Override
  public void mouseDragged(MouseEvent e) {
    x = e.getX();
    y = e.getY();
  }
  @Override
  public void mouseMoved(MouseEvent e) {
    x = e.getX();
    y = e.getY();
  }

}
