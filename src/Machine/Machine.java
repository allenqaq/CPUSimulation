package Machine;

import CacheBuilder.Cache;
import CacheBuilder.CacheCell;
import Memory.Memory;
import Registers.GeneralPurposeRegister;
import Registers.IndexRegister;
import Registers.OtherRegister;
import Tools.Utility;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

// this class manages the memory and register.
// once it was generated, the memory and register formed.
public class Machine {

    private File programFile = null;
    public IndexRegister indexRegister = null;
    public GeneralPurposeRegister generalPurposeRegister = null;
    public Memory memory = null;
    public Cache cache = null;
    public int[] conditionCodes = null;
    public LinkedList<String> inputStream = null;
    public FileInputStream fileInputStream = null;

    public int PC = 0;
    public int lineCount = 0;


    //    private Machine machine;
    private JFrame mainFrame;
    private JPanel panelMachineMonitor;

    // Left Row
    // Contains monitor of program content, IR, MAR, MBR, MSF, MFR, CC and PC
    private JScrollPane scrollProgramContent;
    private JTextArea textProgramContent, textInstructionRegister, textMemoryAddressRegister, textMemoryBufferRegister, textMachineStatusRegister, textMashineFaultRegister, textProgramCounter, textConditionCode;

    // Middle Row
    // Contains monitor of program instructions, Xx and Rx
    private JScrollPane scrollProgramInstructions;
    private JTextArea textProgramInstructions, textIndexRegister1, textIndexRegister2, textIndexRegister3, textGPR0, textGPR1, textGPR2, textGPR3;

    // Right Row
    // Contains I/O components and several buttons, including power, execution and load
    private JScrollPane scrollProgramOutputs;
    private JTextArea textProgramOutputs;
    private JTextArea textFileName;
    private JButton buttonLoadFile;
    private JTextArea textSetIndex;
    private JTextArea textSetValue;
    private JButton buttonSingleStep, buttonPowerOn, buttonPowerOff, buttonExecuteProgram, buttonLoadProgram, buttonSet;
    private JRadioButton selectMemory, selectGeneralPurposeRegister, selectIndexRegister;

    // Memory Monitor
    // Contains memory manipulation components, including memory viewer and loader,
    // and a button to randomly generate registers' value
    private JScrollPane scrollMemoryMonitor;
    private JTextArea textMemoryMonitor, textStartAt, textInput, textStopAt;
    private JRadioButton selectMemory2K, selectMemory4K;
    private JButton buttonReadMemory, buttonReloadMemory, buttonInput;

    // CacheBuilder Viewer
    private JScrollPane scrollCacheViewer;
    private JTextArea textCacheViewer;

    // Read Memory Progress
    // Components of the progress bar displayed when press read memory button
    private Timer timerReadMemory;
    private JProgressBar progressBarReadMemory;

    // Reload Memory Progress
    // Components of the progress bar displayed when press reload memory button
    private Timer timerReloadMemory;
    private JProgressBar progressBarReloadMemory;

    // Temp Variables
    // A text area used to update monitors
//    private JTextArea textUpdate;

    // save the decoded-instructions
    public static String codeSet[][] = new String[1000][6];

    private Machine() {
//        userInterface = new UserInterface(this);
        init();
        power(false);
    }

    // load input function
    private void loadComponents(Boolean power) {
        indexRegister = power ? new IndexRegister() : null;
        generalPurposeRegister = power ? new GeneralPurposeRegister() : null;
        memory = power ? new Memory(2048) : null;
        cache = power ? new Cache(16, this) : null;
        conditionCodes = power ? new int[4] : null;
        inputStream = power ? new LinkedList<>() : null;
    }


    // main function
    public static void main(String[] args) throws IOException {
        new Machine();
    }

    /**
     * This is the function we call after we convert an array to string, in order to remove the
     * remaining "[", "]" and ", "
     *
     * @return the actual string we want in the array
     */


    /**
     * This is the function we call when we want to update the contents in all fields displayed
     */

    private void update() {
        int start = Integer.parseInt(textStartAt.getText());
        int end = Integer.parseInt(textStopAt.getText());
//        if (end == 0) {
//            end = memory.getLength();
//        }
        textIndexRegister1.setText(Utility.DecimalismToBinary(indexRegister.getIndexRegisters(0)));
        textIndexRegister2.setText(Utility.DecimalismToBinary(indexRegister.getIndexRegisters(1)));
        textIndexRegister3.setText(Utility.DecimalismToBinary(indexRegister.getIndexRegisters(2)));

        textGPR0.setText(Utility.DecimalismToBinary(generalPurposeRegister.getGeneralPurposeRegister(0)));
        textGPR1.setText(Utility.DecimalismToBinary(generalPurposeRegister.getGeneralPurposeRegister(1)));
        textGPR2.setText(Utility.DecimalismToBinary(generalPurposeRegister.getGeneralPurposeRegister(2)));
        textGPR3.setText(Utility.DecimalismToBinary(generalPurposeRegister.getGeneralPurposeRegister(3)));

        textMemoryMonitor.setText("ADDRESS-----Content-----\n");
//        textUpdate = new JTextArea();
        if (start < end) {
            for (int i = start; i < end; i++) {
                textMemoryMonitor.append(i + ": ");
                textMemoryMonitor.append(Utility.DecimalismToBinary(memory.getMemory(i)));
                textMemoryMonitor.append("\n");
            }
        }
//        scrollMemoryMonitor.setViewportView(textUpdate);

        textProgramInstructions.setText("");
        for (String s : inputStream) {
            textProgramInstructions.append(s + "\n");
        }
    }

    /**
     * This is where we initialize all fields and labels and buttons, and add them into main frame
     * and display them. We also call the bindEvent() function at the end to bind all button events.
     */

    private void init() {
// Main Frame
        mainFrame = new JFrame("CSCI 6461 CPU Simulator");
        mainFrame.setResizable(false);
        mainFrame.setBounds(100, 30, 920, 710);
        mainFrame.setLayout(null);
// Program Monitor Panel
        panelMachineMonitor = new JPanel();
        panelMachineMonitor.setLayout(null);
        panelMachineMonitor.setSize(new Dimension(920, 690));
        panelMachineMonitor.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder(" Machine Monitor")));
// Left Column
        // Program Content
        textProgramContent = new JTextArea();
        textProgramContent.setEditable(false);
        scrollProgramContent = new JScrollPane(textProgramContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollProgramContent.setBounds(30, 40, 200, 200);
        // IR
        textInstructionRegister = getJTextArea(1, 1);
        // MAR
        textMemoryAddressRegister = getJTextArea(1, 2);
        // MBR
        textMemoryBufferRegister = getJTextArea(1, 3);
        // MSR
        textMachineStatusRegister = getJTextArea(1, 4);
        // MFR
        textMashineFaultRegister = getJTextArea(1, 5);
        // CC
        textConditionCode = getJTextArea(1, 6);
        // Program Counter
        textProgramCounter = getJTextArea(1, 7);
// Middle Column
        // Program Instructions
        textProgramInstructions = new JTextArea();
        textProgramInstructions.setEditable(false);
        scrollProgramInstructions = new JScrollPane(textProgramInstructions, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollProgramInstructions.setBounds(250, 40, 200, 200);
        // X1 ~ X3
        textIndexRegister1 = getJTextArea(2, 1);
        textIndexRegister2 = getJTextArea(2, 2);
        textIndexRegister3 = getJTextArea(2, 3);
        // textGPR0 ~ textGPR3
        textGPR0 = getJTextArea(2, 4);
        textGPR1 = getJTextArea(2, 5);
        textGPR2 = getJTextArea(2, 6);
        textGPR3 = getJTextArea(2, 7);
//Right Column
        // Program Outputs
        textProgramOutputs = new JTextArea();
        textProgramOutputs.setEditable(false);
        scrollProgramOutputs = new JScrollPane(textProgramOutputs, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollProgramOutputs.setBounds(470, 40, 200, 100);
        // File Stream
        textFileName = new JTextArea();
        textFileName.setEditable(false);
        textFileName.setBounds(470, 150, 200, 50);
        buttonLoadFile = new JButton("File Input");
        buttonLoadFile.setBounds(470, 200, 200, 50);
        // Set Group
        textSetIndex = getJTextArea(3, 1);
        textSetIndex.setText("0");
        textSetIndex.setEditable(true);
        textSetValue = getJTextArea(3, 2);
        textSetValue.setText("0");
        textSetValue.setEditable(true);
        selectGeneralPurposeRegister = new JRadioButton("GPRs (0-3)");
        selectGeneralPurposeRegister.setBounds(470, 380, 100, 25);
        selectIndexRegister = new JRadioButton("IXs (1-3)");
        selectIndexRegister.setBounds(570, 380, 100, 25);
        selectMemory = new JRadioButton("Memory", true);
        selectMemory.setBounds(470, 405, 100, 25);
        ButtonGroup selectRegisterGroup = new ButtonGroup();
        selectRegisterGroup.add(selectGeneralPurposeRegister);
        selectRegisterGroup.add(selectIndexRegister);
        selectRegisterGroup.add(selectMemory);
        selectGeneralPurposeRegister.setSelected(true);
        buttonSet = new JButton("Set");
        buttonSet.setBounds(570, 405, 100, 25);
        // Execute & Load Button
        buttonLoadProgram = new JButton(" Load Program");
        buttonLoadProgram.setBounds(470, 440, 200, 50);
        buttonExecuteProgram = new JButton(" Execute Program");
        buttonExecuteProgram.setBounds(470, 500, 200, 50);
        // Single Step Button
        buttonSingleStep = new JButton("Single Step");
        buttonSingleStep.setBounds(470, 560, 200, 50);
        // Power Buttons
        buttonPowerOn = new JButton(" Power On");
        buttonPowerOn.setBounds(470, 620, 100, 50);
        buttonPowerOff = new JButton(" Power Off");
        buttonPowerOff.setBounds(570, 620, 100, 50);
// Memory Monitor
        // Monitor
        textMemoryMonitor = new JTextArea();
        textMemoryMonitor.setEditable(false);
        textMemoryMonitor.append("ADDRESS-----Content-----");
        scrollMemoryMonitor = new JScrollPane(textMemoryMonitor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMemoryMonitor.setBounds(690, 40, 200, 200);
        // Range Input
        textStartAt = getJTextArea(4, 1);
        textStartAt.setText("0");
        textStartAt.setEditable(true);
        textStopAt = getJTextArea(4, 2);
        textStopAt.setText("0");
        textStopAt.setEditable(true);
        // Read Memory
        buttonReadMemory = new JButton("Read Memory");
        buttonReadMemory.setBounds(690, 380, 200, 50);
        // Read Memory Progress Bar
        progressBarReadMemory = new JProgressBar();
        progressBarReadMemory.setBounds(690, 395, 200, 20);
        progressBarReadMemory.setVisible(false);
        // Input Area
        textInput = new JTextArea();
        textInput.setBounds(690, 460, 90, 30);
        buttonInput = new JButton("Go");
        buttonInput.setBounds(790, 440, 100, 50);
        // Expandable Memory
        selectMemory2K = new JRadioButton(" 2K");
        selectMemory2K.setBounds(690, 500, 100, 25);
        selectMemory4K = new JRadioButton(" 4K", true);
        selectMemory4K.setBounds(690, 525, 100, 25);
        ButtonGroup selectMemoryGroup = new ButtonGroup();
        selectMemoryGroup.add(selectMemory2K);
        selectMemoryGroup.add(selectMemory4K);
        selectMemory2K.setSelected(true);
        buttonReloadMemory = new JButton("Reload");
        buttonReloadMemory.setBounds(790, 500, 100, 50);
        // CacheBuilder viewer
        textCacheViewer = new JTextArea();
        textCacheViewer.setEditable(false);
        scrollCacheViewer = new JScrollPane(textCacheViewer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollCacheViewer.setBounds(690, 580, 200, 90);
        // ADDED
        progressBarReloadMemory = new JProgressBar();
        progressBarReloadMemory.setBounds(790, 515, 100, 20);
        progressBarReloadMemory.setVisible(false);
// Proceed to Display
        addObjectToMachineMonitor();
        mainFrame.add(panelMachineMonitor);
        mainFrame.setVisible(true);
        bindEvents();
    }

    /**
     * Add components to main frame
     */

    private void addObjectToMachineMonitor() {
        panelMachineMonitor.add(getJLabel(1, " Program Contents", -1, -2));
        panelMachineMonitor.add(getJLabel(1, " Instruction Register", -1, 2));
        panelMachineMonitor.add(getJLabel(1, " Memory Address Register", -1, 3));
        panelMachineMonitor.add(getJLabel(1, " Memory Buffer Register", -1, 4));
        panelMachineMonitor.add(getJLabel(1, " Machine Status Register", -1, 5));
        panelMachineMonitor.add(getJLabel(1, " Machine Fault Register", -1, 6));
        panelMachineMonitor.add(getJLabel(1, " Condition Code", -1, 7));
        panelMachineMonitor.add(getJLabel(1, " Program Counter", -1, 8));
        panelMachineMonitor.add(getJLabel(2, " Program Input Stream", -1, -2));
        panelMachineMonitor.add(getJLabel(2, " Index Register X", 1, 0));
        panelMachineMonitor.add(getJLabel(2, " Index Register X", 2, 0));
        panelMachineMonitor.add(getJLabel(2, " Index Register X", 3, 0));
        panelMachineMonitor.add(getJLabel(2, " General Purpose Register ", 0, 4));
        panelMachineMonitor.add(getJLabel(2, " General Purpose Register ", 1, 4));
        panelMachineMonitor.add(getJLabel(2, " General Purpose Register ", 2, 4));
        panelMachineMonitor.add(getJLabel(2, " General Purpose Register ", 3, 4));
        panelMachineMonitor.add(getJLabel(3, " Program Output", -1, -2));
        panelMachineMonitor.add(getJLabel(3, " Index", -1, 2));
        panelMachineMonitor.add(getJLabel(3, " Value", -1, 3));
        panelMachineMonitor.add(getJLabel(4, " Memory Monitor", -1, -2));
        panelMachineMonitor.add(getJLabel(4, " Start at", -1, 2));
        panelMachineMonitor.add(getJLabel(4, " Stop at", -1, 3));
        panelMachineMonitor.add(getJLabel(4, " Input Area", -1, 5));
        panelMachineMonitor.add(getJLabel(4, " CacheBuilder Viewer", -1, 7));
        panelMachineMonitor.add(scrollProgramContent);
        panelMachineMonitor.add(textInstructionRegister);
        panelMachineMonitor.add(textMemoryAddressRegister);
        panelMachineMonitor.add(textMemoryBufferRegister);
        panelMachineMonitor.add(textMachineStatusRegister);
        panelMachineMonitor.add(textMashineFaultRegister);
        panelMachineMonitor.add(textConditionCode);
        panelMachineMonitor.add(textProgramCounter);
        panelMachineMonitor.add(scrollProgramInstructions);
        panelMachineMonitor.add(textIndexRegister1);
        panelMachineMonitor.add(textIndexRegister2);
        panelMachineMonitor.add(textIndexRegister3);
        panelMachineMonitor.add(textGPR0);
        panelMachineMonitor.add(textGPR1);
        panelMachineMonitor.add(textGPR2);
        panelMachineMonitor.add(textGPR3);
        panelMachineMonitor.add(scrollProgramOutputs);
        panelMachineMonitor.add(textSetIndex);
        panelMachineMonitor.add(textSetValue);
        panelMachineMonitor.add(selectGeneralPurposeRegister);
        panelMachineMonitor.add(selectIndexRegister);
        panelMachineMonitor.add(selectMemory);
        panelMachineMonitor.add(buttonSet);
        panelMachineMonitor.add(buttonLoadProgram);
        panelMachineMonitor.add(buttonExecuteProgram);
        panelMachineMonitor.add(buttonSingleStep);
        panelMachineMonitor.add(buttonPowerOn);
        panelMachineMonitor.add(buttonPowerOff);
        panelMachineMonitor.add(scrollMemoryMonitor);
        panelMachineMonitor.add(textStartAt);
        panelMachineMonitor.add(textStopAt);
        panelMachineMonitor.add(textInput);
        panelMachineMonitor.add(buttonInput);
        panelMachineMonitor.add(buttonReadMemory);
        panelMachineMonitor.add(scrollCacheViewer);
        // ADDED
        panelMachineMonitor.add(progressBarReadMemory);
//        panelMachineMonitor.add(buttonWriteAllRegisters);
        panelMachineMonitor.add(selectMemory2K);
        panelMachineMonitor.add(selectMemory4K);
        panelMachineMonitor.add(buttonReloadMemory);
        // ADDED
        panelMachineMonitor.add(progressBarReloadMemory);

        panelMachineMonitor.add(textFileName);
        panelMachineMonitor.add(buttonLoadFile);
    }

    /**
     * This is a function wrote to automatically create labels, without specifying the coordinates for
     * every label. If prefix = " GPR ", index = 1, offset = 4, you will get a label named " GPR 1" at
     * the position of 5th (4 + 1) label of a column
     *
     * @param column Column of label (1 - 4)
     * @param prefix Title prefix
     * @param index  Label index
     * @param offset Label offset
     * @return a new label
     */

    private JLabel getJLabel(int column, String prefix, int index, int offset) {
        JLabel mJLabel = new JLabel(prefix + ((index == -1) ? "" : index));
        mJLabel.setBounds(220 * column - 190, 200 + 60 * (index + offset), 200, 20);
        return mJLabel;
    }

    /**
     * This is a function wrote to automatically create text areas, similar to last one, no need to
     * specify the coordinates of every text area
     *
     * @param column Column of text area (1 - 4)
     * @param row    Row of text area
     * @return a new text area
     */

    private JTextArea getJTextArea(int column, int row) {
        JTextArea mJTextArea = new JTextArea(null, "", 0, TextArea.SCROLLBARS_NONE);
        mJTextArea.setBounds(220 * column - 190, 220 + 60 * row, 200, 30);
        mJTextArea.setEditable(false);
        return mJTextArea;
    }

    /**
     * We bind all button click events in this function
     */

    private void bindEvents() {
        mainFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        buttonSet.addActionListener(e -> {
            char[] value = textSetValue.getText().toCharArray();
            int index = Integer.parseInt(textSetIndex.getText());
            if (index == 233) {
                String line = textSetValue.getText();
                char[] stringArr = line.toCharArray();
                memory.setMemory(lineCount + 1000, Utility.BinaryToDecimalism(Utility.replace(String.valueOf(stringArr))));

                lineCount = lineCount + 1;
            } else {
                if (selectMemory.isSelected()) {
                    memory.setMemory(index, Utility.BinaryToDecimalism(Utility.replace(String.valueOf(value))));
                } else if (selectGeneralPurposeRegister.isSelected()) {
                    generalPurposeRegister.setGeneralPurposeRegister(index, Utility.BinaryToDecimalism(Utility.replace(String.valueOf(value))));
                } else if (selectIndexRegister.isSelected()) {
                    indexRegister.setIndexRegisters(index - 1, Utility.BinaryToDecimalism(Utility.replace(String.valueOf(value))));
                }
            }
            update();
        });

        buttonLoadProgram.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("./");
            chooser.setFileFilter(new FileNameExtensionFilter("txt", "TXT"));
            int returnVal = chooser.showOpenDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                programFile = chooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(programFile));
                    String line;
                    int i = 0;
                    while ((line = reader.readLine()) != null) {
                        char[] stringArr = line.toCharArray();
                        memory.setMemory(i + 1000, Utility.BinaryToDecimalism(Utility.replace(String.valueOf(stringArr))));
                        //LoadStore.decode(line, i);
                        i++;
//                        System.out.println(i);
                    }
                    lineCount = i;
                    reader.close();
                } catch (Exception a) {
                    a.printStackTrace();
                }
                for (int i = 0; i < lineCount; i++) {
                    Preprocessing.decode(Utility.DecimalismToBinary(memory.getMemory(i + 1000)), i);
                }
//                update();

//                textUpdate = new JTextArea();
                for (int i = 0; i < lineCount; i++) {
                    textProgramContent.append(i + 1 + ": ");
                    textProgramContent.append(Utility.DecimalismToBinary(memory.getMemory(i + 1000)));
                    textProgramContent.append("\n");
                }
//                scrollProgramContent.setViewportView(textUpdate);
            }
        });

        buttonExecuteProgram.addActionListener(e -> {
//            PC = 0;
            int er = 0;
            for (int i = 0; i != -1; i++) {
                if (PC == lineCount) {
                    PC = 0;
                    textProgramCounter.setText(Integer.toString(PC));
                    textMemoryBufferRegister.setText("None");
                    textMemoryAddressRegister.setText("None");
                    textInstructionRegister.setText("None");
                    cache.clearCache();
                    textCacheViewer.setText("");
                    JOptionPane.showMessageDialog(null, "No more instruction can be executed", "Warning", JOptionPane.WARNING_MESSAGE);
                    update();
                    textProgramCounter.setText(Integer.toString(PC + 1));
                    textMemoryBufferRegister.setText(OtherRegister.MBR);
                    textMemoryAddressRegister.setText(Integer.toString(OtherRegister.MAR));
                    textInstructionRegister.setText(Utility.replace(Arrays.toString(codeSet[PC])));
                    break;
                }
                else {

                    OtherRegister.MAR = 1000 + PC;
                    OtherRegister.MBR = Utility.DecimalismToBinary(memory.getMemory(OtherRegister.MAR));
                    er = Preprocessing.execution(codeSet[PC], Machine.this);
                    if (er == 11) {
                        PC = lineCount;
                        break;
                    } else if (er != 10) {
                        textMashineFaultRegister.setText("" + er);
                        JOptionPane.showMessageDialog(null, OtherRegister.MFR(er, Machine.this), "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        PC++;
                        update();

                        textProgramCounter.setText(Integer.toString(PC + 1));
                        textMemoryBufferRegister.setText(OtherRegister.MBR);
                        textMemoryAddressRegister.setText(Integer.toString(OtherRegister.MAR));
                        textInstructionRegister.setText(Utility.replace(Arrays.toString(codeSet[PC])));
                    }
                }
            }
            PC = 0;
            update();

            textProgramCounter.setText(Integer.toString(PC));
            textMemoryBufferRegister.setText("None");
            textMemoryAddressRegister.setText("None");
            textInstructionRegister.setText("Over");
            cache.clearCache();
            textCacheViewer.setText("");


        });

        buttonSingleStep.addActionListener(e -> {
            if (Integer.parseInt(textSetIndex.getText()) == 666) {
                int a = Integer.parseInt(textSetValue.getText()) - 1;

                OtherRegister.MAR = 1000 + a;
                OtherRegister.MBR = Utility.DecimalismToBinary(memory.getMemory(OtherRegister.MAR));
                Preprocessing.decode(OtherRegister.MBR, a);
                int er = Preprocessing.execution(codeSet[a], Machine.this);
                if (er != 10) {
                    textMashineFaultRegister.setText("" + er);
                    JOptionPane.showMessageDialog(null, OtherRegister.MFR(er, Machine.this), "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    PC = a + 1;
                    update();

                    textProgramCounter.setText(Integer.toString(PC));
                    textMemoryBufferRegister.setText(OtherRegister.MBR);
                    textMemoryAddressRegister.setText(Integer.toString(OtherRegister.MAR));
                    textInstructionRegister.setText(Utility.replace(Arrays.toString(codeSet[PC - 1])));
                }
            } else {

                if (PC == lineCount) {
                    PC = 0;
                    textProgramCounter.setText(Integer.toString(PC));
                    textMemoryBufferRegister.setText("None");
                    textMemoryAddressRegister.setText("None");
                    textInstructionRegister.setText("None");
                    cache.clearCache();
                    textCacheViewer.setText("");
                    JOptionPane.showMessageDialog(null, "No more instruction can be executed", "Warning", JOptionPane.WARNING_MESSAGE);
                    update();

                    textProgramCounter.setText(Integer.toString(PC + 1));
                    textMemoryBufferRegister.setText(OtherRegister.MBR);
                    textMemoryAddressRegister.setText(Integer.toString(OtherRegister.MAR));
                    textInstructionRegister.setText(Utility.replace(Arrays.toString(codeSet[PC + 1])));
                } else {
                    OtherRegister.MAR = 1000 + PC;
                    OtherRegister.MBR = Utility.DecimalismToBinary(memory.getMemory(OtherRegister.MAR));
                    int er = Preprocessing.execution(codeSet[PC], Machine.this);
                    if (er != 10) {
                        textMashineFaultRegister.setText("" + er);
                        JOptionPane.showMessageDialog(null, OtherRegister.MFR(er, Machine.this), "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        PC++;
                        update();

                        textProgramCounter.setText(Integer.toString(PC + 1));
                        textMemoryBufferRegister.setText(Utility.DecimalismToBinary(memory.getMemory(OtherRegister.MAR + 1)));
                        textMemoryAddressRegister.setText(Integer.toString(OtherRegister.MAR + 1));
                        textInstructionRegister.setText(Utility.replace(Arrays.toString(codeSet[PC + 1])));
                    }
                }
            }
        });

        buttonPowerOn.addActionListener(e -> power(true));

        buttonPowerOff.addActionListener(e -> power(false));

        buttonReadMemory.addActionListener(e -> {
            // ADDED
            buttonReadMemory.setVisible(false);
            progressBarReadMemory.setVisible(true);
            timerReadMemory = new Timer(25, e1 -> {
                int loadingValue = progressBarReadMemory.getValue();
                if (loadingValue < 100) {
                    progressBarReadMemory.setValue(++loadingValue);
                } else {
                    timerReadMemory.stop();
                    buttonReadMemory.setVisible(true);
                    progressBarReadMemory.setVisible(false);
                    progressBarReadMemory.setValue(0);
                    int start = Integer.parseInt(textStartAt.getText());
                    int end = Integer.parseInt(textStopAt.getText());
                    if (end == 0) {
                        end = memory.getLength();
                    }
                    textMemoryMonitor.setText("ADDRESS-----Content-----\n");
//                    textUpdate = new JTextArea();
                    for (int i = start; i < end; i++) {
                        textMemoryMonitor.append(i + ": ");
                        textMemoryMonitor.append(Utility.DecimalismToBinary(memory.getMemory(i)));
                        textMemoryMonitor.append("\n");
                    }
//                    scrollMemoryMonitor.setViewportView(textUpdate);
                }
            });
            timerReadMemory.start();
        });

        buttonInput.addActionListener(e -> {
            String inputString = textInput.getText();
            if (!Objects.equals(inputString, "")) {
                inputStream.add(inputString);
                textInput.setText("");
            }
            update();
        });

        buttonReloadMemory.addActionListener(e -> {
            // ADDED
            buttonReloadMemory.setVisible(false);
            progressBarReloadMemory.setVisible(true);
            timerReloadMemory = new Timer(5, e1 -> {
                int loadingValue = progressBarReloadMemory.getValue();
                if (loadingValue < 100) {
                    progressBarReloadMemory.setValue(++loadingValue);
                } else {
                    timerReloadMemory.stop();
                    buttonReloadMemory.setVisible(true);
                    progressBarReloadMemory.setVisible(false);
                    progressBarReloadMemory.setValue(0);
                    memory = new Memory(selectMemory2K.isSelected() ? 2048 : 4096);
//                    textUpdate = new JTextArea();
                    textMemoryMonitor.setText("ADDRESS-----Content-----\n");
//                    for (int i = 0; i < memory.getLength(); i++) {
//                        textMemoryMonitor.append(i + ": ");
//                        textMemoryMonitor.append(Utility.DecimalismToBinary(memory.getMemory(i)));
//                        textMemoryMonitor.append("\n");
//                    }
//                    scrollMemoryMonitor.setViewportView(textUpdate);
                }
            });
            timerReloadMemory.start();
        });

        buttonLoadFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("./");
            chooser.setFileFilter(new FileNameExtensionFilter("txt", "TXT"));
            int returnVal = chooser.showOpenDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    fileInputStream = new FileInputStream(chooser.getSelectedFile());
                    textFileName.setText(chooser.getSelectedFile().getName());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
//                int x = -1;
//                do {
//                    try {
//                        x = fileInputStream.read();
//                        textProgramOutputs.append(String.valueOf((char) x));
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                } while (x != -1);

                for (int i = 0; i < lineCount; i++) {
                    Preprocessing.decode(Utility.DecimalismToBinary(memory.getMemory(i + 1000)), i);
                }
                update();
            }
        });
    }

    /**
     * This is the function of power manipulation, we enable/disable ui elements, load/unload machine
     * components when power status is changed via clicking buttons
     *
     * @param power Power status (Power On - True, Power Off - False)
     */

    private void power(Boolean power) {
        textSetIndex.setEnabled(power);
        textSetValue.setEnabled(power);
        selectGeneralPurposeRegister.setEnabled(power);
        selectIndexRegister.setEnabled(power);
        selectMemory.setEnabled(power);
        buttonSet.setEnabled(power);
        buttonLoadProgram.setEnabled(power);
        buttonExecuteProgram.setEnabled(power);
        buttonSingleStep.setEnabled(power);
        buttonPowerOn.setEnabled(!power);
        buttonPowerOff.setEnabled(power);
        textStartAt.setEnabled(power);
        textStopAt.setEnabled(power);
        buttonReloadMemory.setEnabled(power);
        textInput.setEnabled(power);
        buttonInput.setEnabled(power);
        selectMemory2K.setEnabled(power);
        selectMemory4K.setEnabled(power);
        buttonReadMemory.setEnabled(power);

        textFileName.setEnabled(power);
        buttonLoadFile.setEnabled(power);

        loadComponents(power);
    }

    public void registerOutput(String outputString) {
        textProgramOutputs.append(outputString + "\n");
    }

    public Boolean getDeviceStatus(int deviceID) {
        if (deviceID == 0)
            // Keyboard
            return inputStream.size() == 0;
        else if (deviceID == 1)
            // Printer
            return textProgramOutputs.isEnabled();
        else
// Card Reader
            return deviceID == 2 && fileInputStream != null;
    }

//    private void updateCache(CacheCell cacheCell, Boolean clear) {
//        if (clear) {
//            textCacheViewer.setText("");
//            textCacheViewer.append("MemAddr\tMemContent\n");
//        } else if (cacheCell != null) {
//            textCacheViewer.append(cacheCell.getMemoryAddress() + "\t" + Utility.DecimalismToBinary(cacheCell.getMemoryContent()) + "\n");
//        }
//    }

    public void updateCache(CacheCell cacheCell, Boolean clear) {
        if (clear)
            textCacheViewer.setText("");
        else
            textCacheViewer.append(cacheCell.getMemoryAddress() + ":" + cacheCell.getMemoryContent() + "\n");
    }
}
