import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.company.Regular;
import com.company.Savings;
import net.miginfocom.swing.*;

import java.sql.*;
import java.util.Vector;
/*
 * Created by JFormDesigner on Sat Aug 08 08:54:28 PDT 2020
 */



/**
 * @author Alex
 */
public class CIForm extends JFrame {
    Connection con1;
    PreparedStatement insert;
    public CIForm() {
        initComponents();
    }
    //Event handler for add button
    private void button1ActionPerformed(ActionEvent e) throws SQLException, ClassNotFoundException {
        // TODO add your code here
        String CustNo;
        String CustName;
        String initialDep;
        String nyears;
        int savtypeindex;
        String savtype=null;
        boolean blnValidate=false;
        long custno=0;
        double initdep = 0;
        int years=0;

        CustNo=textField1.getText();
        CustName=textField2.getText();
        initialDep=textField3.getText();
        nyears=textField4.getText();
        savtypeindex=comboBox1.getSelectedIndex();
        if(savtypeindex == 0){
            savtype="Savings-Deluxe";
        }else if(savtypeindex==1){
            savtype="Savings-Regular";
        }
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");
            blnValidate=false;
            try {
                custno = Long.parseLong(CustNo);
            } catch (final NumberFormatException exc) {
                JOptionPane.showMessageDialog(null,"Please enter valid Customer Number");
                blnValidate=true;
            }
            try {
                initdep = Double.parseDouble(initialDep);
            } catch (final NumberFormatException exc) {
                JOptionPane.showMessageDialog(null,"Please enter valid Initial Deposit");
                blnValidate=true;
            }
            try {
                years = Integer.parseInt(nyears);
            } catch (final NumberFormatException exc) {
                JOptionPane.showMessageDialog(null,"Please enter valid year");
                blnValidate=true;
            }
            if (blnValidate==true){
                return;
            }
        if(e.getSource()==button1) {


            insert = con1.prepareStatement("Select * from savingstable where custno = ?");

            insert.setString(1, CustNo);



            ResultSet rs = insert.executeQuery();

            if(rs.isBeforeFirst()){          //res.isBeforeFirst() is true if the cursor

                JOptionPane.showMessageDialog(null,"The Customer number you are trying to enter already exists ");

                textField1.setText("");
                textField2.setText("");
                textField3.setText("");
                textField4.setText("");
                comboBox1.setSelectedIndex(0);
                textField1.requestFocus();

                return;
            }
            if (savtypeindex==0){
                Regular regular=new Regular(custno,CustName,initdep,years,savtype);
            }
            //Savings savings=new Savings(custno,CustName,initdep,years,savtype);
            insert = con1.prepareStatement("insert into savingstable values(?,?,?,?,?)");

            insert.setString(1, CustNo);
            insert.setString(2, CustName);
            insert.setString(3, initialDep);
            insert.setString(4, nyears);
            insert.setString(5, savtype);
            insert.executeUpdate();

            JOptionPane.showMessageDialog(null, "Record added");

            textField1.setText("");
            textField2.setText("");
            textField3.setText("");
            textField4.setText("");
            comboBox1.setSelectedIndex(0);
            textField1.requestFocus();
            updatetable();
        }
    }
    //Edit Button Click event
    private void button2ActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        // TODO add your code here
        String CustNo;
        String CustName;
        String initialDep;
        String nyears;
        int savtypeindex;
        String savtype=null;
        boolean blnValidate=false;
        long custno=0;
        double initdep = 0;
        int years=0;

        CustNo=textField1.getText();
        CustName=textField2.getText();
        initialDep=textField3.getText();
        nyears=textField4.getText();
        savtypeindex=comboBox1.getSelectedIndex();
        if(savtypeindex == 0){
            savtype="Savings-Deluxe";
        }else if(savtypeindex==1){
            savtype="Savings-Regular";
        }
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");


        insert = con1.prepareStatement("update savingstable set custname=?,cdep=?,nyears=?,savtype=? where custno =?");

        insert.setString(5, CustNo);
        insert.setString(1, CustName);
        insert.setString(2, initialDep);
        insert.setString(3, nyears);
        insert.setString(4, savtype);

        insert.executeUpdate();

        JOptionPane.showMessageDialog(null, "Record edited");

        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        comboBox1.setSelectedIndex(0);
        textField1.requestFocus();





        updatetable();




    }
    //Delete button click event
    private void button3ActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        // TODO add your code here
        String CustNo;

        CustNo=textField1.getText();
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");


        int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete?", "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(result == JOptionPane.YES_OPTION){

            insert = con1.prepareStatement("delete from savingstable where custno =?");

            insert.setString(1, CustNo);

        }
        insert.execute();
        if (result==JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Record deleted");
        }else {
             JOptionPane.showMessageDialog(null, "Record not deleted");
        }
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        comboBox1.setSelectedIndex(0);
        textField1.requestFocus();

        updatetable();
    }
    //Click event on table
    private void table1MouseClicked(MouseEvent e) throws ClassNotFoundException, SQLException {
        // TODO add your code here
        String[] cols = {"Year", "Starting","Interest","Ending Value"};
        String[][] data = {{},{}};
        DefaultTableModel model = new DefaultTableModel(data, cols);
        table2.setModel(model);
        int c;
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");

        insert = con1.prepareStatement("Select * from savingstable");

        ResultSet rs = insert.executeQuery();



        ResultSetMetaData Res = rs.getMetaData();
        c = Res.getColumnCount();
        int row=table1.getSelectedRow();
        /*while(rs.next()) {
            Vector v2 = new Vector();

            for(int a =1;a<=c;a++){

                *//*v2.add(rs.getString("custno"));
                v2.add(rs.getString("custname"));
                v2.add(rs.getString("cdep"));
                v2.add(rs.getString("nyears"));
                v2.add(rs.getString("savtype"));*//*

            }
            df.addRow(v2);
        }*/
        if (table1.getModel().getValueAt(row, 5).toString()=="Savings-Regular"){
            Regular regular=new Regular(Long.parseLong(rs.getString("custno")),rs.getString("custname"),Double.parseDouble(rs.getString("cdep")),Integer.parseInt(rs.getString("nyears")),rs.getString("savtype"));
            regular.generateTable();
            Vector v2 = new Vector();
        }
    }

    private void scrollPane1MouseClicked(MouseEvent e) {
        // TODO add your code here
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Alex
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        textField2 = new JTextField();
        label3 = new JLabel();
        textField3 = new JTextField();
        label4 = new JLabel();
        textField4 = new JTextField();
        label5 = new JLabel();
        comboBox1 = new JComboBox<>();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();

        //======== this ========
        setName("frame1");
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[332,fill]" +
            "[352,fill]",
            // rows
            "[21]" +
            "[22]" +
            "[25]" +
            "[23]" +
            "[25]" +
            "[320]" +
            "[37]"));

        //---- label1 ----
        label1.setText("Enter the Customer Number");
        contentPane.add(label1, "cell 0 0");
        contentPane.add(textField1, "cell 1 0");

        //---- label2 ----
        label2.setText("Enter the Customer Name");
        contentPane.add(label2, "cell 0 1");
        contentPane.add(textField2, "cell 1 1");

        //---- label3 ----
        label3.setText("Enter the initial Deposit");
        contentPane.add(label3, "cell 0 2");
        contentPane.add(textField3, "cell 1 2");

        //---- label4 ----
        label4.setText("Enter the number of years");
        contentPane.add(label4, "cell 0 3");
        contentPane.add(textField4, "cell 1 3");

        //---- label5 ----
        label5.setText("Choose the type of savings");
        contentPane.add(label5, "cell 0 4");

        //---- comboBox1 ----
        comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
            "Savings-Deluxe",
            "Savings-Regular"
        }));
        contentPane.add(comboBox1, "cell 1 4");

        //======== scrollPane1 ========
        {
            scrollPane1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    scrollPane1MouseClicked(e);
                }
            });

            //---- table1 ----
            table1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        table1MouseClicked(e);
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1, "cell 0 5");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(table2);
        }
        contentPane.add(scrollPane2, "cell 1 5");

        //---- button1 ----
        button1.setText("Add");
        button1.addActionListener(e -> {
            try {
                button1ActionPerformed(e);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        contentPane.add(button1, "cell 0 6");

        //---- button2 ----
        button2.setText("Edit");
        button2.addActionListener(e -> {
            try {
                button2ActionPerformed(e);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        contentPane.add(button2, "cell 0 6");

        //---- button3 ----
        button3.setText("Delete");
        button3.addActionListener(e -> {
            try {
                button3ActionPerformed(e);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        contentPane.add(button3, "cell 0 6");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    //Create columns for table1
    public void SetColums(){
        String[] cols = {"Number", "Name","Deposit","Years","Type of Savings"};
        String[][] data = {{"d1", "d1.1"},{"d2", "d2.1"}};
        DefaultTableModel model = new DefaultTableModel(data, cols);
        table1.setModel(model);
    }
    //Method to retrieve data
    public void updatetable() throws ClassNotFoundException, SQLException {

        int c;
        Class.forName("com.mysql.jdbc.Driver");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");

        insert = con1.prepareStatement("Select * from savingstable");

        ResultSet rs = insert.executeQuery();



        ResultSetMetaData Res = rs.getMetaData();
        c = Res.getColumnCount();
        DefaultTableModel df = (DefaultTableModel) table1.getModel();
        df.setRowCount(0);

        while(rs.next()) {
            Vector v2 = new Vector();

            for(int a =1;a<=c;a++){

                v2.add(rs.getString("custno"));
                v2.add(rs.getString("custname"));
                v2.add(rs.getString("cdep"));
                v2.add(rs.getString("nyears"));
                v2.add(rs.getString("savtype"));
            }
            df.addRow(v2);
        }
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Alex
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JTextField textField2;
    private JLabel label3;
    private JTextField textField3;
    private JLabel label4;
    private JTextField textField4;
    private JLabel label5;
    private JComboBox<String> comboBox1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Git Repository Link:https://github.com/ojalex96/Finals.git
        CIForm ciForm = new CIForm();
        ciForm.SetColums();
        ciForm.updatetable();
        ciForm.setVisible(true);
    }
}
