/*
 * ScholarPubSearchView.java
 */
package scholarpubsearch;

import java.awt.datatransfer.Clipboard;
import messages.CFP;
import messages.CfpQuery;
import messages.PublicationRequest;
import messages.Propose;
import messages.Publications;
import messages.StringAttributeList;
import jade.gui.GuiEvent;
import java.awt.FileDialog;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import messages.Publication;

/**
 * The application's main frame.
 */
public class ScholarPubSearchView extends FrameView{
    private static final int DEFAULT_RES_NUMBER = 10;
    private UserInputAgent myAgent;

    public ScholarPubSearchView(SingleFrameApplication app, UserInputAgent a) {
        super(app);
        myAgent = a;

        initComponents();
        SearchForm.initColumnSizes(jTable1, jTable1.getModel(), PublicationListModel.longValues);
        SearchForm.initColumnSizes(jTable2, jTable2.getModel(), PublicationModel.longValues);
        //initSearchPanel
        comboBoxes = new ArrayList<javax.swing.JComboBox>();
        comboBoxes.add(jComboBox1); comboBoxes.add(jComboBox2);
        comboBoxes.add(jComboBox3); comboBoxes.add(jComboBox4);
        comboBoxes.add(jComboBox5);
        textBoxes = new ArrayList<javax.swing.JTextField>();
        textBoxes.add(jTextField1); textBoxes.add(jTextField2);
        textBoxes.add(jTextField3); textBoxes.add(jTextField4);
        textBoxes.add(jTextField5);
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ScholarPubSearchApp.getApplication().getMainFrame();
            aboutBox = new ScholarPubSearchAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ScholarPubSearchApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox();
        jTextField5 = new javax.swing.JTextField();
        arxivCheckBox = new javax.swing.JCheckBox();
        scholarCheckBox = new javax.swing.JCheckBox();
        Search = new javax.swing.JButton();
        resultCountComboBox = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        deleteMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        mainPanel.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                mainPanelComponentRemoved(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(412, 174));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(scholarpubsearch.ScholarPubSearchApp.class).getContext().getResourceMap(ScholarPubSearchView.class);
        jTable1.setFont(resourceMap.getFont("jTable1.font")); // NOI18N
        jTable1.setModel(new scholarpubsearch.PublicationListModel());
        jTable1.setFillsViewportHeight(true);
        jTable1.setGridColor(resourceMap.getColor("jTable1.gridColor")); // NOI18N
        jTable1.setName("jTable1"); // NOI18N
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jTable1ComponentAdded(evt);
            }
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTable1ComponentRemoved(evt);
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jTable1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTable1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
        );

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(422, 216));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable2.setFont(resourceMap.getFont("jTable2.font")); // NOI18N
        jTable2.setModel(new PublicationModel());
        jTable2.setColumnSelectionAllowed(true);
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable2.setFillsViewportHeight(true);
        jTable2.setGridColor(resourceMap.getColor("jTable2.gridColor")); // NOI18N
        jTable2.setName("jTable2"); // NOI18N
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        jTable2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
        );

        jPanel3.setName("jPanel3"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Abstract", "Author", "Year", "Journal", "Subject area" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Abstract", "Author", "Year", "Journal", "Subject area" }));
        jComboBox2.setSelectedIndex(1);
        jComboBox2.setName("jComboBox2"); // NOI18N

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Abstract", "Author", "Year", "Journal", "Subject area" }));
        jComboBox3.setSelectedIndex(2);
        jComboBox3.setName("jComboBox3"); // NOI18N

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Abstract", "Author", "Year", "Journal", "Subject area" }));
        jComboBox4.setSelectedIndex(3);
        jComboBox4.setName("jComboBox4"); // NOI18N

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N

        jTextField3.setName("jTextField3"); // NOI18N

        jTextField4.setName("jTextField4"); // NOI18N

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Abstract", "Author", "Year", "Journal", "Subject area" }));
        jComboBox5.setSelectedIndex(4);
        jComboBox5.setName("jComboBox5"); // NOI18N
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jTextField5.setName("jTextField5"); // NOI18N

        arxivCheckBox.setSelected(true);
        arxivCheckBox.setText(resourceMap.getString("arxivCheckBox.text")); // NOI18N
        arxivCheckBox.setName("arxivCheckBox"); // NOI18N

        scholarCheckBox.setText(resourceMap.getString("scholarCheckBox.text")); // NOI18N
        scholarCheckBox.setName("scholarCheckBox"); // NOI18N

        Search.setText(resourceMap.getString("Search.text")); // NOI18N
        Search.setName("Search"); // NOI18N
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        resultCountComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "results...", "10", "15", "20", "25", "30", "40", "50", "100" }));
        resultCountComboBox.setName("resultCountComboBox"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(resultCountComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 87, Short.MAX_VALUE)
                            .addComponent(arxivCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                        .addGap(5, 5, 5)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(scholarCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                .addGap(21, 21, 21)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scholarCheckBox)
                    .addComponent(arxivCheckBox))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search)
                    .addComponent(resultCountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(202, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setEnabled(false);
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setEnabled(false);
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem5);

        jSeparator3.setName("jSeparator3"); // NOI18N
        fileMenu.add(jSeparator3);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(scholarpubsearch.ScholarPubSearchApp.class).getContext().getActionMap(ScholarPubSearchView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem.setText(resourceMap.getString("deleteMenuItem.text")); // NOI18N
        deleteMenuItem.setEnabled(false);
        deleteMenuItem.setName("deleteMenuItem"); // NOI18N
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        menuBar.add(jMenu1);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 437, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents


    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
    }//GEN-LAST:event_jComboBox5ActionPerformed

    //start search query
    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        CfpQuery cfpQuery = new CfpQuery();
        PublicationRequest request = new PublicationRequest();
        fillRequestFields(request);
        fillCfpQueryFields(cfpQuery, request);
        sendCfpToMyAgent(cfpQuery);
    }

    //save publication in xml file
    private void savePublications(Publications pubs) {
        FileDialog saveFile = new FileDialog(ScholarPubSearchApp.getApplication().getMainFrame(), "Save file", FileDialog.SAVE);
        saveFile.setDirectory(defaultDirectory);
        saveFile.setFile(defaultFile);
        saveFile.setFilenameFilter(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        saveFile.show();
        if (saveFile.getFile() != null) {
            defaultDirectory = saveFile.getDirectory();
            defaultFile = saveFile.getFile();
            try {
                FileOutputStream outFile;
                File f = new File(saveFile.getDirectory() + saveFile.getFile());
                if (!f.exists()) {
                    f.createNewFile();
                }
                outFile = new FileOutputStream(saveFile.getDirectory() + saveFile.getFile());
                Propose propose = new Propose();
               
                propose.setPublications(pubs);
                String pubString = propose.marshal();
                outFile.write(pubString.getBytes());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    private void sendCfpToMyAgent(CfpQuery cfpQuery) {
        GuiEvent ge = new GuiEvent(this, UserInputAgent.CFP_EVENT);
        CFP cfp = new CFP();
        cfp.setCFPQuery(cfpQuery);
        ge.addParameter(cfp);
        myAgent.postGuiEvent(ge);
        invokeSearchForm();
    }//GEN-LAST:event_SearchActionPerformed

    private void jTable1ComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTable1ComponentRemoved
    }//GEN-LAST:event_jTable1ComponentRemoved

	public void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange
            }//GEN-LAST:event_jTable1PropertyChange

	private void jTable1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTable1ComponentAdded
    }//GEN-LAST:event_jTable1ComponentAdded

    //show selected publication in jTable2
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();
        PublicationListModel model = (PublicationListModel) jTable1.getModel();
        Publication selectedPublication = model.getPublication(selectedRow);
        ((PublicationModel) jTable2.getModel()).showPublication(selectedPublication);
        if(selectedPublication != null && jTable1.getSelectedColumn() == 0) {
            Boolean selected =
                    (Boolean) model.getValueAt(selectedRow,0);
            model.setValueAt(!selected, selectedRow, 0);
            boolean noSelectedPubs = model.noSelectedPubs();
            jMenuItem4.setEnabled(!noSelectedPubs);
            jMenuItem5.setEnabled(!noSelectedPubs);
            deleteMenuItem.setEnabled(!noSelectedPubs);
        }
        setUpValueColumn(jTable2,1);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyPressed
    }//GEN-LAST:event_jTable2KeyPressed

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased
    }//GEN-LAST:event_jTable2KeyReleased

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
    }//GEN-LAST:event_jTable1FocusGained

    private void jTable1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTable1CaretPositionChanged

    }//GEN-LAST:event_jTable1CaretPositionChanged

    //open file with saved publications
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FileDialog openFile = new FileDialog(
                ScholarPubSearchApp.getApplication().getMainFrame());
        openFile.setMode(FileDialog.LOAD);
        openFile.setFilenameFilter(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        openFile.show();
        if (openFile.getFile() != null) {
            defaultDirectory = openFile.getDirectory();
            defaultFile = openFile.getFile();
            FileInputStream inFile;
            try {
                inFile = new FileInputStream(openFile.getDirectory()+openFile.getFile());
                int bytesAvailable = inFile.available();
                byte[] bytes = new byte[bytesAvailable];
                inFile.read(bytes);
                String publicationsString = new String(bytes);
                Publications pubs = Propose.unmarshal(publicationsString);
                setPublications(pubs);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            } catch (JAXBException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
         Publications p = new Publications();
                p.setPublication(((PublicationListModel) jTable1.getModel()).getPublications());
        savePublications(p);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
         invokeSearchForm();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    //save selected publications
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        PublicationListModel model = (PublicationListModel) jTable1.getModel();
        Publications pubs = new Publications();
        ArrayList<String> publicationIds = new ArrayList<String>();
        for(int i = 0; i < model.getRowCount(); i++) {
            if(model.getValueAt(i, 0) == Boolean.TRUE) {
                Publication p = model.getPublication(i);
                publicationIds.add(p.getId());
                pubs.getPublication().add(model.getPublication(i));
            }
        }
        savePublications(pubs);
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    //import into bibtex file
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        PublicationListModel model = (PublicationListModel) jTable1.getModel();
        Publications pubs = new Publications();
        String bibtexFileString = "";
        for(int i = 0; i < model.getRowCount(); i++) {
            if(model.getValueAt(i, 0) == Boolean.TRUE) {
                Publication p = model.getPublication(i);
                String bibtexRecord = p.getBibtex();
                if(bibtexRecord!=null && !bibtexRecord.equals(""))
                    bibtexFileString +=p.getBibtex() +"\n";
            }
        }

        FileDialog saveFile = new FileDialog(ScholarPubSearchApp.getApplication().getMainFrame(), "Save file", FileDialog.SAVE);
        saveFile.setDirectory(defaultDirectory);
        saveFile.setFile(defaultFile);
        saveFile.setFilenameFilter(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith("*.bib");
            }
        });
        saveFile.show();
        if (saveFile.getFile() != null) {
            defaultDirectory = saveFile.getDirectory();
            defaultFile = saveFile.getFile();
            try {
                FileOutputStream outFile;
                File f = new File(saveFile.getDirectory() + saveFile.getFile());
                if (!f.exists()) {
                    f.createNewFile();
                }
                outFile = new FileOutputStream(saveFile.getDirectory() + saveFile.getFile());
                outFile.write(bibtexFileString.getBytes());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        PublicationListModel model = (PublicationListModel) jTable1.getModel();
        Publications pubs = new Publications();
        for(int i = 0; i < model.getRowCount(); i++) {
            if(model.getValueAt(i, 0) == Boolean.FALSE) {
                Publication p = model.getPublication(i);
                pubs.getPublication().add(p);
            }
        }
        setPublications(pubs);
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void mainPanelComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_mainPanelComponentRemoved

    }//GEN-LAST:event_mainPanelComponentRemoved

    //show list of publication on main form
    public void setPublications(Publications p) {
        PublicationListModel model = (PublicationListModel) jTable1.getModel();
        if(p!= null && p.getPublication()!=null)
            (model).showPublications(p);
        SearchForm.initColumnSizes(jTable1, model, PublicationListModel.longValues);
        setUpValueColumn(jTable1,1);
        setUpValueColumn(jTable1,2);
        setUpValueColumn(jTable1,4);
    }

    //add list of publication on main form
    public void addPublications(Publications p) {
        PublicationListModel model = (PublicationListModel) jTable1.getModel();
        List<Publication> merged = model.getPublications();
        merged.addAll(p.getPublication());
        Publications publications  = new Publications();
        publications.setPublication(merged);
        if(publications!= null && publications.getPublication()!=null)
            (model).showPublications(publications);
        SearchForm.initColumnSizes(jTable1, model, PublicationListModel.longValues);
        setUpValueColumn(jTable1,1);
        setUpValueColumn(jTable1,2);
        setUpValueColumn(jTable1,4);
    }

    //construct CFP request from main form
    private void fillRequestFields(PublicationRequest request) {
        for (int i = 0; i < comboBoxes.size(); i++) {
            javax.swing.JComboBox comboBox = comboBoxes.get(i);
            javax.swing.JTextField textField = textBoxes.get(i);
            String field = comboBox.getSelectedItem().toString();
            String value = textField.getText();
            SearchForm.addRequestField(request, field, value);
        }
    }
    
    //construct CFP request from main form
    private void fillCfpQueryFields(CfpQuery cfpQuery, PublicationRequest request) {
        StringAttributeList sources = new StringAttributeList();
        if (arxivCheckBox.isSelected()) {
            sources.getAttribute().add(dataSources.ArxivOrgAgent.SERVICE_NAME);
        }
        if (scholarCheckBox.isSelected()) {
            sources.getAttribute().add(dataSources.GoogleScholarAgent.SERVICE_NAME);
        }
        cfpQuery.setSourceList(sources);
        cfpQuery.setPublicationRequest(request);
        //#publications
        int resultsCount = DEFAULT_RES_NUMBER;
        if(resultCountComboBox.getSelectedIndex() > 0)
            resultsCount = Integer.decode(
                resultCountComboBox.getSelectedItem().toString());
        cfpQuery.setResultsNumber(resultsCount);
        
    }

    private void invokeSearchForm() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SearchForm form = new SearchForm(myAgent);
                form.setVisible(true);
                myAgent.setActiveSearchForm(form);
            }
        });
    }

    public static void setUpValueColumn(JTable table, int column) {
        TableColumn value = table.getColumnModel().getColumn(column);
        MultiLineCellRenderer renderer =
                new MultiLineCellRenderer();
        value.setCellRenderer(renderer);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Search;
    private javax.swing.JCheckBox arxivCheckBox;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JComboBox resultCountComboBox;
    private javax.swing.JCheckBox scholarCheckBox;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private ArrayList<javax.swing.JComboBox> comboBoxes;
    ArrayList<javax.swing.JTextField> textBoxes;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private String defaultDirectory;
    private String defaultFile;

    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
