/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.corecomponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.logging.Level;
import org.sleuthkit.autopsy.coreutils.Logger;
import javax.swing.ListSelectionModel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.IconView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataResultViewer;

/**
 * Thumbnail view of images in data result
 */
@ServiceProvider(service = DataResultViewer.class)
public class DataResultViewerThumbnail extends AbstractDataResultViewer {

    private transient ExplorerManager em = new ExplorerManager();

    /** Creates new form DataResultViewerThumbnail */
    public DataResultViewerThumbnail() {
        initComponents();

        // only allow one item to be selected at a time
        ((IconView) thumbnailScrollPanel).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.em.addPropertyChangeListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        thumbnailScrollPanel = new IconView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(thumbnailScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(thumbnailScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane thumbnailScrollPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return this.em;
    }

    @Override
    public Node getSelectedNode() {
        Node result = null;

        Node[] selectedNodes = this.getExplorerManager().getSelectedNodes();
        if (selectedNodes.length > 0) {
            result = selectedNodes[0];
        }
        return result;
    }

    @Override
    public boolean isSupported(Node selectedNode) {
        if (selectedNode == null) {
            return false;
        }
        Children ch = selectedNode.getChildren();
        for (Node n : ch.getNodes()) {  
            if (ThumbnailViewChildren.isSupported(n)) {
                return true;
            }
        }
        return false;
    }
    
     /**
     * Expand node
     * @param n Node to expand
     */
    @Override
    public void expandNode(Node n) {
       
    }
    
    @Override
    public void setNode(Node givenNode) {
        // change the cursor to "waiting cursor" for this operation
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (givenNode != null) {
                Node root = new AbstractNode(new ThumbnailViewChildren(givenNode));
                em.setRootContext(root);
            } else {
                Node emptyNode = new AbstractNode(Children.LEAF);
                em.setRootContext(emptyNode); // make empty node
                
                IconView iv = ((IconView) this.thumbnailScrollPanel);
                iv.setBackground(Color.BLACK);
            }
        } finally {
            this.setCursor(null);
        }
    }

    @Override
    public String getTitle() {
        return "Thumbnail View";
    }

    @Override
    public DataResultViewer getInstance() {
        return new DataResultViewerThumbnail();
    }

    @Override
    public void resetComponent() {
    }

    @Override
    public void clearComponent() {
        em.removePropertyChangeListener(this);
        this.thumbnailScrollPanel.removeAll();
        this.thumbnailScrollPanel = null;
        try {
            this.em.getRootContext().destroy();
            em = null;
        } catch (IOException ex) {
            // TODO: What's the proper thing to do here? Should it log? Not throw runtime exception?
            throw new RuntimeException("Error: can't clear the component of the Thumbnail Result Viewer.", ex);
        }
    }

    @Override
    public Component getComponent() {
        return this;
    }
    
    @Override
    public void setSelectedNodes(Node[] selected) {
        try{
            this.em.setSelectedNodes(selected);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DataResultViewerThumbnail.class.getName())
                    .log(Level.WARNING, "Couldn't set selected nodes.", ex);
        }
    }
}
