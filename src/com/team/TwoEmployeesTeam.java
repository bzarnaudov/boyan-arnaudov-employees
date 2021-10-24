package com.team;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TwoEmployeesTeam {
    private JPanel panelMain;
    private JButton openFileButton;
    private JTextField messageField;
    private JTable table1;

    private final JFileChooser openFileChooser;
    private File file;

    public TwoEmployeesTeam() {
        openFileChooser = new JFileChooser();
        openFileChooser.setCurrentDirectory(new File("c:\\temp"));
        openFileChooser.setFileFilter(new FileNameExtensionFilter("TXT text", "txt"));

        String[] columns = {"Employee ID #1", "Employee ID #2", "Project ID", "Days worked"};

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnValue = openFileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = openFileChooser.getSelectedFile();
                    Team team = findTeam(file);
                    if (team != null) {
                        String[][] data = {{String.valueOf(team.getEmployeeId1()), String.valueOf(team.getEmployeeId2()),
                                String.valueOf(team.getProjectId()), String.valueOf(team.getDaysWorkingTogether())}};
                        table1.setModel(new DefaultTableModel(data, columns));
                    } else {
                        String[][] emptyData = {{"There are no employees, who have worked together"}};
                        String[] emptyColumns = {""};
                        table1.setModel(new DefaultTableModel(emptyData, emptyColumns));
                    }
                    messageField.setText("Text file successfully loaded!");
                } else {
                    messageField.setText("No file chosen!");
                }
            }
        });


    }

    public Team findTeam(File file) {
        FileInputStream inputStream = null;
        Scanner sc = null;
        String[] parsedLine;
        String line;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Map<Integer, Map<Integer, List<Period>>> projects = new HashMap<>();
        Integer employeeId;
        Integer projectId;
        Period period;
        List<Period> periods;
        Map<Integer, List<Period>> employees;
        Team team = null;
        try {
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);

            while (sc.hasNextLine()) {
                line = sc.nextLine();
                parsedLine = line.split(", ");
                try {
                    employeeId = Integer.parseInt(parsedLine[0]);
                    projectId = Integer.parseInt(parsedLine[1]);
                    period = new Period(formatter.parse(parsedLine[2]),
                            parsedLine[3].equals("NULL") ? new Date() : formatter.parse(parsedLine[3]));

                    if (projects.containsKey(projectId)) {
                        employees = projects.get(projectId);
                        if (employees.containsKey(employeeId)) {
                            periods = employees.get(employeeId);
                            periods.add(period);
                        } else {
                            for (Map.Entry<Integer, List<Period>> employee : employees.entrySet()) {
                                periods = employee.getValue();
                                for (Period periodFromList : periods) {
                                    if (periodFromList.getEndDate().before(period.getStartDate()) ||
                                            period.getEndDate().before(periodFromList.getStartDate())) {
                                        // no overlap
                                    } else {
                                        Date laterStart = Collections.max(Arrays.asList(periodFromList.getStartDate(),
                                                period.getStartDate()));
                                        Date earlierEnd = Collections.min(Arrays.asList(periodFromList.getEndDate(),
                                                period.getEndDate()));
                                        int daysWorkingTogether = (int) (1 + (earlierEnd.getTime() - laterStart.getTime())
                                                / (1000 * 60 * 60 * 24));
                                        if (team == null || team.getDaysWorkingTogether() < daysWorkingTogether) {
                                            team = new Team(employeeId, employee.getKey(),
                                                    projectId, daysWorkingTogether);
                                        }
                                    }
                                }
                            }
                            periods = new ArrayList<>();
                            periods.add(period);
                            employees.put(employeeId, periods);
                        }
                    } else {
                        periods = new ArrayList<>();
                        periods.add(period);
                        employees = new HashMap<>();
                        employees.put(employeeId, periods);
                        projects.put(projectId, employees);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return team;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TwoEmployeesTeam");
        frame.setContentPane(new TwoEmployeesTeam().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
