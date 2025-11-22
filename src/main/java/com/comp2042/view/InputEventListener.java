package com.comp2042.view;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();

    boolean onGhostCheck(ViewData brick);

    DownData onHardDrop(ViewData brick);

    ViewData getCurrentBrick();
}
