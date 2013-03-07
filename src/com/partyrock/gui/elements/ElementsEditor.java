package com.partyrock.gui.elements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.blink.BlinkController;
import com.partyrock.element.lasers.LaserController;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.element.lights.LightController;
import com.partyrock.gui.LightWindow;
import com.partyrock.gui.dialog.InputDialog;
import com.partyrock.id.ID;
import com.partyrock.tools.PartyToolkit;

/**
 * GUI Editor for elements
 * 
 * @author Matthew
 * 
 */
public class ElementsEditor implements ElementTableRenderer, ElementsTableEditor, ElementDisplay {

    protected Shell shlElementsEditor;
    private Table table;

    private LightWindow main;
    private LightMaster master;

    /**
     * Launch the application. This is here for the SWT Designer
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            ElementsEditor window = new ElementsEditor(null);
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ElementsEditor() {
        this(null);
    }

    public ElementsEditor(LightWindow main) {
        this.main = main;
        this.master = main.getMaster();
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        updateElements();
        shlElementsEditor.open();
        shlElementsEditor.layout();
        while (!shlElementsEditor.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlElementsEditor = new Shell();
        shlElementsEditor.setSize(722, 530);
        shlElementsEditor.setText("Elements Editor");
        shlElementsEditor.setLayout(new GridLayout(1, false));

        Label lblAddElements = new Label(shlElementsEditor, SWT.CENTER);
        lblAddElements.setFont(SWTResourceManager.getFont("Lucida Grande", 20, SWT.NORMAL));
        lblAddElements.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        lblAddElements.setText("Elements Editor");

        table = new Table(shlElementsEditor, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tblclmnType = new TableColumn(table, SWT.CENTER);
        tblclmnType.setWidth(100);
        tblclmnType.setText("Type");
        // Set not editable
        tblclmnType.setData(new ElementsEditorColumnData(false));

        TableColumn tblclmnName = new TableColumn(table, SWT.CENTER);
        tblclmnName.setWidth(210);
        tblclmnName.setText("Name");

        TableColumn tblclmnId = new TableColumn(table, SWT.CENTER);
        tblclmnId.setWidth(379);
        tblclmnId.setText("ID");

        // Allow the table to be edited
        table.addListener(SWT.MouseDoubleClick, new ElementsDoubleClickListener(this, table));

        Label label = new Label(shlElementsEditor, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setAlignment(SWT.CENTER);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

        Label lblDoubleClickTo = new Label(shlElementsEditor, SWT.NONE);
        lblDoubleClickTo.setText("Double click an element to make changes. You cannot change the type of an element.");

        Composite composite = new Composite(shlElementsEditor, SWT.NONE);
        composite.setLayout(new GridLayout(4, false));

        Button btnNewButton = new Button(composite, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                addLights();
            }
        });
        btnNewButton.setBounds(0, 0, 94, 28);
        btnNewButton.setText("Add Lights");

        Button btnAddLsers = new Button(composite, SWT.NONE);
        btnAddLsers.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                addLasers();
            }
        });
        btnAddLsers.setText("Add Lasers");

        Button btnNewButton_1 = new Button(composite, SWT.NONE);
        btnNewButton_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                addLEDPanel();
            }
        });
        btnNewButton_1.setText("Add LED Panel");

        Button btnAddBlink = new Button(composite, SWT.NONE);
        btnAddBlink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                addBlink();
            }
        });
        btnAddBlink.setText("Add Blink");

    }

    public void updateItemWithText(TableItem item, int col, String text) {
        switch (col) {
            case 0: // type
                break; // shouldn't be editable
            case 1: // name
                ElementController controller = (ElementController) item.getData();
                item.setText(col, text);
                controller.setName(text);
                System.out.println(controller);
                break;
            case 2: // ID
                controller = (ElementController) item.getData();
                item.setText(col, text);
                controller.setID(text);
                System.out.println(controller);
                break;
        }

        // The WindowManager needs to update since an element has now updated
        main.getWindowManager().updateElements();
    }

    /**
     * When a button is clicked, should add new lights
     */
    public void addLights() {
        InputDialog dialog = new InputDialog(shlElementsEditor, "How many lights would you like to add?", "Add Lights");
        String amountString = dialog.open();

        int amount = 0;
        try {
            amount = Integer.parseInt(amountString);
        } catch (Exception e) {
            System.out.println("Invalid amount of lights specified");
            return;
        }

        for (int a = 0; a < amount; a++) {
            LightController controller = new LightController(master, ID.genID("li"), "Strand "
                    + master.getElements().size(), "l" + (master.getElements().size()));
            master.addElement(controller);
        }

        main.getWindowManager().updateElements();
        master.getLocationManager().unsavedChanges();
    }

    public void addLasers() {
        InputDialog dialog = new InputDialog(shlElementsEditor, "How many lasers would you like to add?", "Add Lasers");
        String amountString = dialog.open();

        int amount = 0;
        try {
            amount = Integer.parseInt(amountString);
        } catch (Exception e) {
            System.out.println("Invalid amount of lasers specified");
            return;
        }

        for (int a = 0; a < amount; a++) {
            LaserController controller = new LaserController(master, ID.genID("lz"), "Laser "
                    + master.getElements().size(), "z" + (master.getElements().size()));
            master.addElement(controller);
        }

        main.getWindowManager().updateElements();
        master.getLocationManager().unsavedChanges();
    }

    /**
     * Adds a blink
     */
    public void addBlink() {
        String id = PartyToolkit.openInput(shlElementsEditor,
                "What is the address (URL or IP will work) of the machine running blink-api?", "Add a Blink");

        if (id == null || id.trim().equals("")) {
            return;
        }

        BlinkController controller = new BlinkController(master, ID.genID("bl"), "Blink", id);
        master.addElement(controller);

        main.getWindowManager().updateElements();
        master.getLocationManager().unsavedChanges();
    }

    /**
     * Adds an LED Panel
     */
    public void addLEDPanel() {
        String id = PartyToolkit.openInput(shlElementsEditor,
                "Enter the dimensions of the LED panel in the form <width>x<height>", "Add an LED Panel");

        if (id == null || id.trim().equals("")) {
            return;
        }

        String[] dim = id.split("x");
        int width = Integer.parseInt(dim[0].trim());
        int height = Integer.parseInt(dim[1].trim());

        id = width + "x" + height;

        LEDPanelController controller = new LEDPanelController(master, ID.genID("lp"), "LED Panel", id, width, height);
        master.addElement(controller);

        master.getWindowManager().updateElements();
        master.getLocationManager().unsavedChanges();
    }

    /**
     * Updates elements in the ElementsEditor
     */
    public void updateElements() {
        // Adds the elements back to this window
        ElementUpdater.updateElements(this, table);
    }

    /**
     * Adds an element to the table
     */
    public void addElementAsRow(ElementController element) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(new String[] { element.getType().getTypeName(), element.getName(), element.getID() });
        item.setData(element);
    }

    public Shell getShell() {
        return shlElementsEditor;
    }

    public LightMaster getMaster() {
        return master;
    }

    @Override
    public boolean isDisposed() {
        return getShell().isDisposed();
    }
}
