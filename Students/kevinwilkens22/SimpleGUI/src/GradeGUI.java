import javax.swing.*;

/**
 * Created by kwilk_000 on 2/4/2015.
 */
public class GradeGUI {
    public static void main (String[] args){
    int grade;
    String letterGrade;

    String gradeWord = JOptionPane.showInputDialog("Enter you're score.");
    grade = Integer.parseInt(gradeWord);

    if (grade >= 90)
        letterGrade = "A";
    else if (grade >= 80)
        letterGrade = "B";
    else if (grade >= 70)
        letterGrade = "C";
    else if (grade >= 60)
        letterGrade = "D";
    else
        letterGrade = "F";


    JOptionPane.showMessageDialog(null, "Your letter grade is " + letterGrade + "!");
    }
}
