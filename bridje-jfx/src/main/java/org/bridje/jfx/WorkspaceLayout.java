/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bridje.jfx;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

/**
 *
 */
final class WorkspaceLayout<T extends Node> extends AnchorPane
{
    private T left;
    
    private T right;
    
    private T top;
    
    private T bottom;
    
    private T center;
    
    private final SplitPane hSlit;
    
    private final SplitPane vSlit;

    public WorkspaceLayout()
    {
        hSlit = new SplitPane();
        hSlit.setOrientation(Orientation.HORIZONTAL);
        vSlit = new SplitPane(hSlit);
        vSlit.setOrientation(Orientation.VERTICAL);
        getChildren().add(vSlit);
        setTopAnchor(vSlit, 0d);
        setLeftAnchor(vSlit, 0d);
        setBottomAnchor(vSlit, 0d);
        setRightAnchor(vSlit, 0d);
    }
    
    public T get(Position position)
    {
        switch(position)
        {
            case TOP:
                return getTop();
            case LEFT:
                return getLeft();
            case RIGHT:
                return getRight();
            case CENTER:
                return getCenter();
            case BOTTON:
                return getBottom();
        }
        return null;
    }

    public void set(Position position, T node)
    {
        switch(position)
        {
            case TOP:
                setTop(node);
                break;
            case LEFT:
                setLeft(node);
                break;
            case RIGHT:
                setRight(node);
                break;
            case CENTER:
                setCenter(node);
                break;
            case BOTTON:
                setBottom(node);
                break;
        }
    }

    public T getLeft()
    {
        return left;
    }

    public void setLeft(T left)
    {
        fixSplits(hSlit, left, this.left, 0);
        this.left = left;
        fixDividers(hSlit);
    }

    public T getRight()
    {
        return right;
    }

    public void setRight(T right)
    {
        fixSplits(hSlit, right, this.right, 2);
        this.right = right;
        fixDividers(hSlit);
    }

    public T getTop()
    {
        return top;
    }

    public void setTop(T top)
    {
        fixSplits(vSlit, top, this.top, 0);
        this.top = top;
        fixDividers(vSlit);
    }

    public T getBottom()
    {
        return bottom;
    }

    public void setBottom(T bottom)
    {
        fixSplits(vSlit, bottom, this.bottom, 2);
        this.bottom = bottom;
        fixDividers(vSlit);
    }

    public T getCenter()
    {
        return center;
    }

    public void setCenter(T center)
    {
        fixSplits(hSlit, center, this.center, (left == null) ? 0 : 1);
        this.center = center;
        fixDividers(hSlit);
    }

    private void fixSplits(SplitPane split, T node, T prev, int index)
    {
        if(prev != null && !prev.equals(node) )
        {
            split.getItems().remove(prev);
        }

        if(node != null && !split.getItems().contains(node))
        {
            if(index > split.getItems().size() - 1)
            {
                split.getItems().add(node);
            }
            else
            {
                split.getItems().add(index, node);
            }
        }
    }
    
    public void fixDividers(SplitPane split)
    {
        if(split.getItems().size() >= 2)
        {
            Node firstItem = split.getItems().get(0);
            if(firstItem.equals(getLeft()) || firstItem.equals(getTop()))
            {
                split.setDividerPosition(0, 0.1);
            }
            
            Node lastItem = split.getItems().get(split.getItems().size()-1);
            if(lastItem.equals(getRight()) || lastItem.equals(getBottom()))
            {
                split.setDividerPosition(split.getDividers().size()-1, 0.9);
            }
        }
    }
    
    public boolean isEmpty()
    {
        return center == null
                && left == null
                && right == null
                && bottom == null
                && top == null;
    }
}
